import sys
from CodeMaintainabilityEvaluators import (GPT2Medium, GPT2Large, GPT2XL, CodeGPTSmallJava, CodeGPTJava, Codegen350m,
                                           Bloomz560m, Bloomz1b1, Bloomz1b7, Bloomz3b, Bloomz7b1, CodeLlama7b,
                                           CodeLlama13b, CodeBERTaTextGeneration, CodeBERTaFillMask)


# Definitions
csv_file_path = 'dataset/labels.csv'
output_directory = 'output'

avail_model_text = \
    ("Available models: gpt2-medium, gpt2-large, gpt2-xl, CodeGPTSmallJava, CodeGPTJava, Codegen350m, Bloomz-560m, "
     "Bloomz-1b1, Bloomz-1b7, Bloomz-3b, Bloomz-7b1, CodeLlama-7b, CodeLlama-13b, CodeBERTaTextGeneration, "
     "CodeBERTaFillMask")


if __name__ == '__main__':

    if len(sys.argv) != 2:
        print("Usage: python main.py <model>")
        print(avail_model_text)
        print("Example: python main.py gpt2-medium")
        sys.exit(1)

    model = sys.argv[1]

    if model == "gpt2-medium":
        approach = GPT2Medium(csv_file_path, output_directory)
    elif model == "gpt2-large":
        approach = GPT2Large(csv_file_path, output_directory)
    elif model == "gpt2-xl":
        approach = GPT2XL(csv_file_path, output_directory)
    elif model == "CodeGPTSmallJava":
        approach = CodeGPTSmallJava(csv_file_path, output_directory)
    elif model == "CodeGPTJava":
        approach = CodeGPTJava(csv_file_path, output_directory)
    elif model == "Codegen350m":
        approach = Codegen350m(csv_file_path, output_directory)
    elif model == "Bloomz-560m":
        approach = Bloomz560m(csv_file_path, output_directory)
    elif model == "Bloomz-1b1":
        approach = Bloomz1b1(csv_file_path, output_directory)
    elif model == "Bloomz-1b7":
        approach = Bloomz1b7(csv_file_path, output_directory)
    elif model == "Bloomz-3b":
        approach = Bloomz3b(csv_file_path, output_directory)
    elif model == "Bloomz-7b1":
        approach = Bloomz7b1(csv_file_path, output_directory)
    elif model == "CodeLlama-7b":
        approach = CodeLlama7b(csv_file_path, output_directory)
    elif model == "CodeLlama-13b":
        approach = CodeLlama13b(csv_file_path, output_directory)
    elif model == "CodeBERTaTextGeneration":
        approach = CodeBERTaTextGeneration(csv_file_path, output_directory)
    elif model == "CodeBERTaFillMask":
        approach = CodeBERTaFillMask(csv_file_path, output_directory)
    else:
        print("Model not recognized.")
        print(avail_model_text)
        sys.exit(1)

    approach.evaluate_files()
