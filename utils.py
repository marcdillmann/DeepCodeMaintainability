import re
import csv
import os
import os.path
import pandas as pd
import numpy as np
from datetime import datetime


def preprocess_java_code(code):
    # Remove single line comments
    code = re.sub(r"//.*", "", code)
    # Remove multi-line comments
    code = re.sub(r"/\*.*?\*/", "", code, flags=re.DOTALL)
    # Remove empty lines and strip leading/trailing whitespaces
    code = "\n".join([line.strip() for line in code.splitlines() if line.strip()])
    return code


def read_csv(csv_file_path):
    csv_file_dir = os.path.dirname(csv_file_path)
    df = pd.read_csv(csv_file_path)

    java_file_paths = []
    overall_probabilities = []
    overall_binary_classifications = []

    for index, row in df.iterrows():
        relative_path = row['path'].replace("\\", "/")
        java_file_path = os.path.join(csv_file_dir, relative_path)
        java_file_paths.append(java_file_path)

        overall = row['overall']

        number_strings = overall.replace('{', '').replace('}', '').split(',')
        probabilities = list(map(float, number_strings))
        overall_probabilities.append(probabilities)
        index = np.argmax(probabilities)
        binary_overall = False if index == 0 else True
        overall_binary_classifications.append(binary_overall)

    results = {
        'java_file_paths': java_file_paths,
        'overall_probabilities': overall_probabilities,
        'overall_binary_classifications': overall_binary_classifications
    }

    return results


def write_csv(csv_data, cross_entropies, output_directory, model_name):
    # Get the current timestamp and format it
    timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')

    # Constructing the CSV file path
    csv_file_path = os.path.join(output_directory, f"Evaluation_{model_name}_{timestamp}.csv")

    # Make sure the lengths are consistent
    assert len(csv_data['java_file_paths']) == len(cross_entropies)

    with open(csv_file_path, 'w', newline='') as csvfile:
        csv_writer = csv.writer(csvfile)

        # Writing the header
        csv_writer.writerow(["File", "Cross-Entropy", "Overall", "Overall Binary"])

        # Writing the data
        for i in range(len(csv_data['java_file_paths'])):
            file_path = csv_data['java_file_paths'][i]
            entropy = cross_entropies[i]
            overall = "{" + ",".join(map(str, csv_data['overall_probabilities'][i])) + "}"
            binary = csv_data['overall_binary_classifications'][i]

            csv_writer.writerow([file_path, entropy, overall, binary])

    print(f"Results written to {csv_file_path}")


# Source: https://www.geeksforgeeks.org/break-list-chunks-size-n-python/
def split_into_chunks(l, n):
    return [l[i * n:(i + 1) * n] for i in range((len(l) + n - 1) // n)]
