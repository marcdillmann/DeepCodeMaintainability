import torch
from tqdm import tqdm
from transformers import AutoTokenizer, AutoModelForCausalLM, AutoModelForMaskedLM
from utils import preprocess_java_code, read_csv, write_csv, split_into_chunks
from peft import PeftConfig, PeftModel


class CodeMaintainabilityEvaluator:
    def __init__(self, name, model, tokenizer, csv_file_path, output_directory, max_input_size):
        self.name = name
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.tokenizer = tokenizer
        if self.tokenizer.pad_token is None:
            self.tokenizer.pad_token = self.tokenizer.eos_token
        self.model = model.to(self.device)
        self.model.eval()
        # Unfortunately, the naming convention for the maximum input sequence length of the models is not consistent,
        # so the size needs to be retrieved manually from the config.json file of the model and set this way
        # Subtract 6 to leave space for special tokens
        self.max_length = max_input_size - 6
        self.csv_file_path = csv_file_path
        self.output_directory = output_directory
        self.loss_fn = torch.nn.CrossEntropyLoss()

    def print_model_info(self):
        print(f"Name: {self.name}")
        print(f"Model Name: {self.model.__class__.__name__}")
        print(f"Device: {self.model.device}")

    def evaluate_model_loss(self, code):
        inputs = self.tokenizer(code, return_tensors='pt', truncation=True).to(self.device)
        input_ids = inputs['input_ids']
        with torch.no_grad():
            outputs = self.model(input_ids, labels=input_ids)
            # outputs = self.model(**inputs)
        # loss = F.softmax(outputs.logits, dim=-1).numpy()[0]
        loss = outputs.loss.item()
        return loss

    def evaluate_cross_entropy_loss(self, code):
        raise Exception("evaluate_cross_entropy_loss needs to be implemented.")

    def evaluate_file(self, file_path):
        with open(file_path, 'r', encoding='ISO-8859-1') as f:
            code = f.read()

        code = preprocess_java_code(code)

        return self.evaluate_cross_entropy_loss(code)

    def evaluate_files(self):
        print("====================================")
        print(f"Start Evaluation using Model:")
        self.print_model_info()
        csv_data = read_csv(self.csv_file_path)
        print("====================================")
        print()

        cross_entropies = []
        for file in tqdm(csv_data['java_file_paths']):
            cross_entropy = self.evaluate_file(file)
            cross_entropies.append(cross_entropy)

        write_csv(csv_data, cross_entropies, self.output_directory, self.name)

        print("====================================")
        print(f"Evaluation finished.")
        print("====================================")
        print()


class CodeMaintainabilityEvaluatorTextGeneration(CodeMaintainabilityEvaluator):

    def evaluate_cross_entropy_loss(self, code):
        # The following code is adapted from https://github.com/simonzachau/SWQD-predict-software-maintainability
        # Tokenize to get tokens
        tokens = self.tokenizer.tokenize(code)

        # Split into chunks
        # Leave space for special chars
        tokens_chunks = split_into_chunks(tokens, self.max_length)

        total_loss = 0.0
        total_number_of_tokens = 0
        for tokens_chunk in tokens_chunks:
            # In the edge case, that the last chunk has size 1, we skip that chunk because of the offset in the
            # cross entropy loss computation
            if len(tokens_chunk) == 1:
                continue
            correctly_sized_string = self.tokenizer.convert_tokens_to_string(tokens_chunk)
            inputs = self.tokenizer.encode_plus(
                correctly_sized_string,
                return_tensors='pt',
                padding=True,
                truncation=True,
                max_length=self.max_length
            ).to(self.device)

            # Compute loss for each chunk
            input_ids = inputs['input_ids']
            with torch.no_grad():
                logits = self.model(**inputs).logits

            # Since CrossEntropyLoss expects labels and not encodings,
            # we use the actual input_ids as labels
            # The model's prediction is offset by one position,
            # hence we exclude the last prediction and the first actual token from comparison
            loss = self.loss_fn(logits[:, :-1, :].squeeze(), input_ids[:, 1:].squeeze())
            number_of_tokens = len(tokens_chunk)
            total_loss += number_of_tokens*loss.item()
            total_number_of_tokens += number_of_tokens

        # Compute the average by scaling the loss in each iteration with the number of tokens and then divide by the
        # total number of tokens so chunks with fewer tokens do not have an over-pronounced influence
        avg_loss = total_loss / total_number_of_tokens
        return avg_loss


class CodeMaintainabilityEvaluatorFillMask(CodeMaintainabilityEvaluator):

    def evaluate_cross_entropy_loss(self, code):
        total_loss = 0.0
        total_number_of_tokens = 0

        # Tokenize the input code
        tokenized_input = self.tokenizer.tokenize(code)
        num_tokens = len(tokenized_input)

        # Calculate how many chunks are needed
        num_chunks = (num_tokens + self.max_length - 1) // self.max_length

        # Evaluate each chunk separately
        for chunk_idx in range(num_chunks):
            start_idx = chunk_idx * self.max_length
            end_idx = min((chunk_idx + 1) * self.max_length, num_tokens)
            chunk_tokens = tokenized_input[start_idx:end_idx]

            for i in range(len(chunk_tokens)):
                # Create a copy of the tokens and mask the token at position i
                masked_input = chunk_tokens.copy()
                masked_input[i] = self.tokenizer.mask_token

                masked_string = self.tokenizer.convert_tokens_to_string(masked_input)

                inputs = self.tokenizer.encode_plus(
                    masked_string,
                    return_tensors='pt',
                    truncation=True,
                    padding=True
                ).to(self.device)
                with torch.no_grad():
                    logits = self.model(**inputs).logits

                mask_position = i

                # Predicted logits for the mask position
                mask_logits = logits[0, mask_position, :]

                # True label
                true_label = self.tokenizer.convert_tokens_to_ids(chunk_tokens[i])
                true_label = torch.tensor(true_label).unsqueeze(0).to(self.device)

                # Compute loss
                loss = self.loss_fn(mask_logits.unsqueeze(0), true_label)
                total_loss += loss.item()
                total_number_of_tokens += 1

        # Average the loss across all tokens
        average_loss = total_loss / total_number_of_tokens
        return average_loss


##################################
##### Text Generation Models #####
##################################

######################
## GPT-based Models ##
######################
class GPT2JavaFineTuned(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = './gpt2_java_finetuned'
        max_input_size = 1024
        model = AutoModelForCausalLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('gpt2_java_finetuned', model, tokenizer, csv_file_path, output_directory, max_input_size)


class CodeGPTSmallJava(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'microsoft/CodeGPT-small-java-adaptedGPT2'
        max_input_size = 1024
        model = AutoModelForCausalLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('CodeGPT-small-java', model, tokenizer, csv_file_path, output_directory, max_input_size)


class CodeGPTJava(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'thmk/codegpt-java-10.2'
        max_input_size = 1024
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        model = AutoModelForCausalLM.from_pretrained(model_string)
        super().__init__('CodeGPT-java-10.2', model, tokenizer, csv_file_path, output_directory, max_input_size)


class Codegen350m(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'ammarnasr/codegen-350M-mono-java'
        max_input_size = 1024
        peft_config = PeftConfig.from_pretrained(model_string)
        model = AutoModelForCausalLM.from_pretrained(peft_config.base_model_name_or_path)
        model = PeftModel.from_pretrained(model, model_string)
        tokenizer = AutoTokenizer.from_pretrained(peft_config.base_model_name_or_path)
        super().__init__('codegen-350m-mono-java', model, tokenizer, csv_file_path, output_directory, max_input_size)


class Bloomz560m(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'bigscience/bloomz-560m'
        max_input_size = 1024
        model = AutoModelForCausalLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('Bloomz-560m', model, tokenizer, csv_file_path, output_directory, max_input_size)


class Bloomz1b1(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'bigscience/bloomz-1b1'
        max_input_size = 1536
        model = AutoModelForCausalLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('Bloomz-1b1', model, tokenizer, csv_file_path, output_directory, max_input_size)


class Bloomz1b7(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'bigscience/bloomz-1b7'
        max_input_size = 2048
        model = AutoModelForCausalLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('Bloomz-1b7', model, tokenizer, csv_file_path, output_directory, max_input_size)


class Bloomz3b(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'bigscience/bloomz-3b'
        max_input_size = 2560
        model = AutoModelForCausalLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('Bloomz-3b', model, tokenizer, csv_file_path, output_directory, max_input_size)


class Bloomz7b1(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'bigscience/bloomz-7b1'
        max_input_size = 4096
        model = AutoModelForCausalLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('Bloomz-7b1', model, tokenizer, csv_file_path, output_directory, max_input_size)


########################
## Llama-based Models ##
########################
class CodeLlama7b(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'codellama/CodeLlama-7b-hf'
        max_input_size = 16384
        model = AutoModelForCausalLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('CodeLlama-7b-hf', model, tokenizer, csv_file_path, output_directory, max_input_size)


class CodeLlama13b(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'codellama/CodeLlama-13b-hf'
        max_input_size = 16384
        model = AutoModelForCausalLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('CodeLlama-13b-hf', model, tokenizer, csv_file_path, output_directory, max_input_size)


############################
##### Fill Mask Models #####
############################
class CodeBERTaFillMask(CodeMaintainabilityEvaluatorFillMask):
    def __init__(self, csv_file_path, output_directory):
        model_string = 'huggingface/CodeBERTa-small-v1'
        max_input_size = 514
        model = AutoModelForMaskedLM.from_pretrained(model_string)
        tokenizer = AutoTokenizer.from_pretrained(model_string)
        super().__init__('CodeBERTaFillMask', model, tokenizer, csv_file_path, output_directory, max_input_size)
