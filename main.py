import sys
from CodeMaintainabilityEvaluators import GPT2, Bloomz560m, CodeBERTaTextGeneration, CodeBERTaFillMask


# Definitions
csv_file_path = 'dataset/labels.csv'
output_directory = 'output'


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: python main.py <model>")
        print("Available models: 'GPT-2', 'Bloomz-560m', 'CodeBERTaTextGeneration', CodeBERTaFillMask")
        sys.exit(1)

    model = sys.argv[1]

    if model == "GPT-2":
        approach = GPT2(csv_file_path, output_directory)
    elif model == "Bloomz-560m":
        approach = Bloomz560m(csv_file_path, output_directory)
    elif model == "CodeBERTaTextGeneration":
        approach = CodeBERTaTextGeneration(csv_file_path, output_directory)
    elif model == "CodeBERTaFillMask":
        approach = CodeBERTaFillMask(csv_file_path, output_directory)
    else:
        print("Model not recognized.")
        print("Available models: 'GPT-2', 'Bloomz-560m', 'CodeBERTaTextGeneration', CodeBERTaFillMask")
        sys.exit(1)

    approach.evaluate_files()
