import torch
from tqdm import tqdm
from transformers import AutoTokenizer, AutoModelForCausalLM, AutoModelForMaskedLM
from utils import preprocess_java_code, read_csv, write_csv, split_into_chunks


class CodeMaintainabilityEvaluator:
    def __init__(self, name, model, tokenizer, csv_file_path, output_directory):
        self.name = name
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        #self.device = torch.device("cpu")
        self.tokenizer = tokenizer
        if self.tokenizer.pad_token is None:
            self.tokenizer.pad_token = self.tokenizer.eos_token
        self.model = model.to(self.device)
        self.model.eval()
        self.max_length = getattr(self.model.config, 'max_position_embeddings', 512) - 6  # space for special tokens
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
        for tokens_chunk in tokens_chunks:
            correctly_sized_string = self.tokenizer.convert_tokens_to_string(tokens_chunk)
            inputs = self.tokenizer.encode_plus(
                correctly_sized_string,
                return_tensors='pt',
                padding=True,
                truncation=True
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
            total_loss += loss.item()

        avg_loss = total_loss / len(tokens_chunks)
        return avg_loss


class CodeMaintainabilityEvaluatorFillMask(CodeMaintainabilityEvaluator):

    def evaluate_cross_entropy_loss(self, code):
        total_loss = 0.0

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

            chunk_loss = 0.0
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
                chunk_loss += loss.item()

            # Average the loss for this chunk
            average_chunk_loss = chunk_loss / len(chunk_tokens)
            total_loss += average_chunk_loss

        # Average the loss across all chunks
        average_loss = total_loss / num_chunks
        return average_loss


### Text Generation Models ###
class GPT2(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model = AutoModelForCausalLM.from_pretrained('gpt2-xl')
        tokenizer = AutoTokenizer.from_pretrained('gpt2-xl')
        super().__init__('GPT-2', model, tokenizer, csv_file_path, output_directory)


class Bloomz560m(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model = AutoModelForCausalLM.from_pretrained('bigscience/bloomz-560m')
        tokenizer = AutoTokenizer.from_pretrained('bigscience/bloomz-560m')
        super().__init__('Bloomz-560m', model, tokenizer, csv_file_path, output_directory)


class CodeBERTaTextGeneration(CodeMaintainabilityEvaluatorTextGeneration):
    def __init__(self, csv_file_path, output_directory):
        model = AutoModelForCausalLM.from_pretrained('huggingface/CodeBERTa-small-v1', is_decoder=True)
        tokenizer = AutoTokenizer.from_pretrained('huggingface/CodeBERTa-small-v1')
        super().__init__('CodeBERTaTextGeneration', model, tokenizer, csv_file_path, output_directory)


### Fill Mask Models ###
class CodeBERTaFillMask(CodeMaintainabilityEvaluatorFillMask):
    def __init__(self, csv_file_path, output_directory):
        model = AutoModelForMaskedLM.from_pretrained('huggingface/CodeBERTa-small-v1')
        tokenizer = AutoTokenizer.from_pretrained('huggingface/CodeBERTa-small-v1')
        super().__init__('CodeBERTaFillMask', model, tokenizer, csv_file_path, output_directory)