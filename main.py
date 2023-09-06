import sys
from CodeMaintainabilityEvaluators import (GPT2, Bloomz560m, CodeBERTaTextGeneration, CodeBERTaFillMask,
                                           CodeGPTSmallJava, Codegen350m, CodeGPTJava)


# Definitions
csv_file_path = 'dataset/labels.csv'
output_directory = 'output'

avail_model_text = \
    ("Available models: 'GPT-2', 'Bloomz-560m', 'CodeBERTaTextGeneration', 'CodeBERTaFillMask', 'CodeGPTSmallJava',"
     " 'Codegen350m', 'CodeGPTJava'")


if __name__ == '__main__':

    if len(sys.argv) != 3:
        print("Usage: python main.py <model> <use_chunks>")
        print(avail_model_text)
        print("Use chunks can be either True or False")
        print("Example: python main.py GPT-2 False")
        sys.exit(1)

    model = sys.argv[1]
    if sys.argv[2] == "True":
        use_chunks = True
    elif sys.argv[2] == "False":
        use_chunks = False
    else:
        print("use_chunks has to be True or False")
        sys.exit(1)

    if model == "GPT-2":
        approach = GPT2(csv_file_path, output_directory, use_chunks)
    elif model == "Bloomz-560m":
        approach = Bloomz560m(csv_file_path, output_directory, use_chunks)
    elif model == "CodeBERTaTextGeneration":
        approach = CodeBERTaTextGeneration(csv_file_path, output_directory, use_chunks)
    elif model == "CodeBERTaFillMask":
        approach = CodeBERTaFillMask(csv_file_path, output_directory, use_chunks)
    elif model == "CodeGPTSmallJava":
        approach = CodeGPTSmallJava(csv_file_path, output_directory, use_chunks)
    elif model == "Codegen350m":
        approach = Codegen350m(csv_file_path, output_directory, use_chunks)
    elif model == "CodeGPTJava":
        approach = CodeGPTJava(csv_file_path, output_directory, use_chunks)
    else:
        print("Model not recognized.")
        print(avail_model_text)
        sys.exit(1)

    approach.evaluate_files()
