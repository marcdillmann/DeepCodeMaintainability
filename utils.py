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
    readability_probabilities = []
    understandability_probabilities = []
    complexity_probabilities = []
    modularization_probabilities = []
    overall_probabilities = []
    overall_binary_classifications = []
    overall_highest_probability_index = []

    for index, row in df.iterrows():
        relative_path = row['path'].replace("\\", "/")
        java_file_path = os.path.join(csv_file_dir, relative_path)
        java_file_paths.append(java_file_path)

        # readability
        readability = row['readability']
        number_strings = readability.strip("{}").split(',')
        probabilities = list(map(float, number_strings))
        readability_probabilities.append(probabilities)

        # understandability
        understandability = row['understandability']
        number_strings = understandability.strip("{}").split(',')
        probabilities = list(map(float, number_strings))
        understandability_probabilities.append(probabilities)

        # complexity
        complexity = row['complexity']
        number_strings = complexity.strip("{}").split(',')
        probabilities = list(map(float, number_strings))
        complexity_probabilities.append(probabilities)

        # modularization
        modularization = row['modularization']
        number_strings = modularization.strip("{}").split(',')
        probabilities = list(map(float, number_strings))
        modularization_probabilities.append(probabilities)

        # overall
        overall = row['overall']
        number_strings = overall.strip("{}").split(',')
        probabilities = list(map(float, number_strings))
        overall_probabilities.append(probabilities)
        index = np.argmax(probabilities)
        overall_highest_probability_index.append(index)
        overall_binary = convert_to_binary(probabilities, 'overall')
        overall_binary_classifications.append(overall_binary)

    results = {
        'java_file_paths': java_file_paths,
        'readability_probabilities': readability_probabilities,
        'understandability_probabilities': understandability_probabilities,
        'complexity_probabilities': complexity_probabilities,
        'modularization_probabilities': modularization_probabilities,
        'overall_probabilities': overall_probabilities,
        'overall_highest_probability_index': overall_highest_probability_index,
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
        csv_writer.writerow(["File", "Cross-Entropy", "Overall", "Overall Binary", "Overall Highest Probability Index",
                             "Readability", "Understandability", "Complexity", "Modularization"])

        # Writing the data
        for i in range(len(csv_data['java_file_paths'])):
            file_path = csv_data['java_file_paths'][i]
            #cross_entropy = cross_entropies[i].item()
            cross_entropy = cross_entropies[i]
            overall = "{" + ",".join(map(str, csv_data['overall_probabilities'][i])) + "}"
            overall_binary = csv_data['overall_binary_classifications'][i]
            overall_index = csv_data['overall_highest_probability_index'][i]
            readability = "{" + ",".join(map(str, csv_data['readability_probabilities'][i])) + "}"
            understandability = "{" + ",".join(map(str, csv_data['understandability_probabilities'][i])) + "}"
            complexity = "{" + ",".join(map(str, csv_data['complexity_probabilities'][i])) + "}"
            modularization = "{" + ",".join(map(str, csv_data['modularization_probabilities'][i])) + "}"

            csv_writer.writerow([file_path, cross_entropy, overall, overall_binary, overall_index, readability,
                                 understandability, complexity, modularization])

    print(f"Results written to {csv_file_path}")


# Source: https://www.geeksforgeeks.org/break-list-chunks-size-n-python/
def split_into_chunks(l, n):
    return [l[i * n:(i + 1) * n] for i in range((len(l) + n - 1) // n)]


def convert_to_binary(probabilities, characteristic):
    # Adapted from https://github.com/simonzachau/SWQD-predict-software-maintainability
    index = np.argmax(probabilities)
    agree_indices = [0, 1, 2] if characteristic == 'Complexity' or characteristic == 'Modularization' else [0]
    binary = True if index in agree_indices else False
    return binary
