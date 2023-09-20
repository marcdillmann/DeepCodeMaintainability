import os
import sys
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import mean_squared_error, accuracy_score, classification_report, roc_auc_score
from sklearn.linear_model import LinearRegression
from sklearn.linear_model import LogisticRegression
from scipy.stats import spearmanr
from utils import convert_to_binary


def extract_avg_likert_score(likert_string):
    probabilities = list(map(float, likert_string.strip("{}").split(",")))
    return sum([(i+1)*prob for i, prob in enumerate(probabilities)])


def compute_spearman_rank_correlation(csv_file_path, characteristic):
    # Load the CSV data into a pandas DataFrame
    df = pd.read_csv(csv_file_path)
    df['Avg_Likert'] = df[characteristic].apply(extract_avg_likert_score)

    # Spearman rank correlation
    correlation, p_value = spearmanr(df['Cross-Entropy'], df['Avg_Likert'])

    return {
        "Spearman Rank Correlation": round(correlation, 4),
        "P-value": round(p_value, 4)
    }


def compute_regression_metrics(csv_file_path, characteristic):
    df = pd.read_csv(csv_file_path)
    df['Avg_Likert'] = df[characteristic].apply(extract_avg_likert_score)

    # Prepare data
    x = df[['Cross-Entropy']]
    y = df['Avg_Likert']

    # Split data
    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=42)

    # Normalize the data
    scaler = StandardScaler()
    x_train = scaler.fit_transform(x_train)
    x_test = scaler.transform(x_test)

    # Train the model
    regressor = LinearRegression()
    regressor.fit(x_train, y_train)

    # Predict
    y_pred = regressor.predict(x_test)

    # Evaluate
    mse = mean_squared_error(y_test, y_pred)

    return {
        "Mean Squared Error For Regression": round(mse, 4)
    }


def compute_binary_metrics(csv_file_path, characteristic):
    df = pd.read_csv(csv_file_path)

    binary_classifications = []
    for _, row in df.iterrows():
        values = row[characteristic]
        number_strings = values.strip("{}").split(",")
        probabilities = list(map(float, number_strings))
        binary = convert_to_binary(probabilities, characteristic)
        binary_classifications.append(binary)

    df['Characteristic Binary'] = binary_classifications

    # Prepare data for binary classification
    x = df[['Cross-Entropy']]
    y_binary = df['Characteristic Binary']

    # Split data
    x_train, x_test, y_train, y_test = train_test_split(x, y_binary, test_size=0.2, random_state=42)

    # Normalize the data
    scaler = StandardScaler()
    x_train = scaler.fit_transform(x_train)
    x_test = scaler.transform(x_test)

    # Train the model
    classifier = LogisticRegression()
    classifier.fit(x_train, y_train)

    # Predict
    y_pred = classifier.predict(x_test)

    # Predict probabilities for AUC
    y_pred_prob = classifier.predict_proba(x_test)[:, 1]

    # Evaluate
    accuracy = accuracy_score(y_test, y_pred)
    precision_true = classification_report(y_test, y_pred, output_dict=True)['True']['precision']
    precision_false = classification_report(y_test, y_pred, output_dict=True)['False']['precision']
    recall_true = classification_report(y_test, y_pred, output_dict=True)['True']['recall']
    recall_false = classification_report(y_test, y_pred, output_dict=True)['False']['recall']
    f1_true = classification_report(y_test, y_pred, output_dict=True)['True']['f1-score']
    f1_false = classification_report(y_test, y_pred, output_dict=True)['False']['f1-score']
    auc = roc_auc_score(y_test, y_pred_prob)

    # Compute decision boundary
    decision_boundary = get_logistic_regression_decision_boundary(classifier, scaler)

    return {
        "Accuracy": round(accuracy, 2),
        "Precision (True)": round(precision_true, 2),
        "Precision (False)": round(precision_false, 2),
        "Recall (True)": round(recall_true, 2),
        "Recall (False)": round(recall_false, 2),
        "F1-Score (True)": round(f1_true, 2),
        "F1-Score (False)": round(f1_false, 2),
        "AUC": round(auc, 2),
        "Decision Boundary": round(decision_boundary, 2)
    }


def get_logistic_regression_decision_boundary(clf, scaler):
    # Extract the intercept and coefficient from the trained classifier
    beta_0 = clf.intercept_[0]
    beta_1 = clf.coef_[0][0]

    # Calculate the scaled decision boundary
    decision_boundary_scaled = -beta_0 / beta_1

    # Convert the decision boundary to original scale
    decision_boundary = (decision_boundary_scaled * scaler.scale_[0] + scaler.mean_[0])

    return decision_boundary


def generate_characteristic_results(directory_path, characteristic):
    all_files = [f for f in os.listdir(directory_path) if os.path.isfile(os.path.join(directory_path, f))]

    results = []

    for file in all_files:
        csv_file_path = os.path.join(directory_path, file)
        try:
            spearman_results = compute_spearman_rank_correlation(csv_file_path, characteristic)
            regression_results = compute_regression_metrics(csv_file_path, characteristic)
            binary_results = compute_binary_metrics(csv_file_path, characteristic)

            model_name = file.split('_')[1]
            max_tokens = file.split('_')[2].replace('.csv', '')

            combined_results = {
                "Model": model_name,
                **binary_results,
                **regression_results,
                **spearman_results,
                "Max Input (Tokens)": max_tokens
            }

            results.append(combined_results)
        except Exception as e:
            print(f"Error processing {file}: {e}")

    # Save results to CSV
    df_results = pd.DataFrame(results)
    df_results.to_csv(f"results/{characteristic}_results.csv", index=False)


if __name__ == "__main__":

    if len(sys.argv) != 2:
        print("Usage: python PerformanceAnalysis.py <directory_path>")
        sys.exit(1)

    directory_path = sys.argv[1]

    generate_characteristic_results(directory_path, 'Overall')
    generate_characteristic_results(directory_path, 'Readability')
    generate_characteristic_results(directory_path, 'Understandability')
    generate_characteristic_results(directory_path, 'Complexity')
    generate_characteristic_results(directory_path, 'Modularization')

