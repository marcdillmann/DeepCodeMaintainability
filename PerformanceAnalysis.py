import os
import sys
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
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
    # Load the csv data
    df = pd.read_csv(csv_file_path)
    df['Avg_Likert'] = df[characteristic].apply(extract_avg_likert_score)

    # Spearman rank correlation
    correlation, p_value = spearmanr(df['Cross-Entropy'], df['Avg_Likert'])

    return {
        "Spearman Rank Correlation": round(correlation, 4),
        "P-value": round(p_value, 4)
    }


def compute_regression_metrics(csv_file_path, characteristic, model_name):
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
    y_pred_test = regressor.predict(x_test)
    y_pred_train = regressor.predict(x_train)

    # Evaluate
    mse = mean_squared_error(y_test, y_pred_test)

    # Plot
    x_train_unscaled = scaler.inverse_transform(x_train)
    plot_linear_regression(df, x_train_unscaled, y_pred_train, model_name, characteristic)

    return {
        "Mean Squared Error For Regression": round(mse, 4)
    }


def plot_linear_regression(df, x, y, model_name, characteristic):
    # Plotting
    plt.figure(figsize=(10, 6))

    # Scatter plot
    plt.scatter(df['Cross-Entropy'], df['Avg_Likert'], alpha=0.6)

    # Regression line
    plt.plot(x, y, color='red', label='Regression Line')

    # Titles and labels
    plt.title(f"Linear Regression {model_name} {characteristic}")
    plt.xlabel("Cross-Entropy")
    plt.ylabel("Average Likert Score")
    plt.grid(True)
    plt.tight_layout()

    output_path = f"plots/{characteristic}/LinearRegression/{model_name}.png"
    # Save the plot
    plt.savefig(output_path, format='png', dpi=300)

    plt.close()


def compute_binary_metrics(csv_file_path, characteristic, model_name):
    df = pd.read_csv(csv_file_path)
    df['Avg_Likert'] = df[characteristic].apply(extract_avg_likert_score)

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
    y_binary_pred = classifier.predict(x_test)

    # Predict probabilities for AUC
    y_test_prob = classifier.predict_proba(x_test)[:, 1]

    # Evaluate
    accuracy = accuracy_score(y_test, y_binary_pred)
    precision_true = classification_report(y_test, y_binary_pred, output_dict=True)['True']['precision']
    precision_false = classification_report(y_test, y_binary_pred, output_dict=True)['False']['precision']
    recall_true = classification_report(y_test, y_binary_pred, output_dict=True)['True']['recall']
    recall_false = classification_report(y_test, y_binary_pred, output_dict=True)['False']['recall']
    f1_true = classification_report(y_test, y_binary_pred, output_dict=True)['True']['f1-score']
    f1_false = classification_report(y_test, y_binary_pred, output_dict=True)['False']['f1-score']
    auc = roc_auc_score(y_test, y_test_prob)

    # Compute decision boundary
    decision_boundary = get_logistic_regression_decision_boundary(classifier, scaler)

    # Plot
    x_train_unscaled = scaler.inverse_transform(x_train)
    coef = classifier.coef_[0][0]
    plot_logistic_regression(df, x_train_unscaled, decision_boundary, coef, model_name, characteristic)

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


def plot_logistic_regression(df, x_train, decision_boundary, coef, model_name, characteristic):

    plt.figure(figsize=(10, 6))

    # Determine colors for regions
    left_color = 'red' if coef > 0 else 'green'
    right_color = 'green' if coef > 0 else 'red'

    # Color the regions, with correct ordering for the legend
    if coef > 0:
        plt.fill_between([decision_boundary, max(x_train)[0]], 0.9, 4.1, color=right_color, alpha=0.2,
                         label=f"Classifier Prediction: {'Positive' if coef > 0 else 'Negative'}")
        plt.fill_between([min(x_train)[0], decision_boundary], 0.9, 4.1, color=left_color, alpha=0.2,
                         label=f"Classifier Prediction: {'Negative' if coef > 0 else 'Positive'}")
    else:
        plt.fill_between([min(x_train)[0], decision_boundary], 0.9, 4.1, color=left_color, alpha=0.2,
                         label=f"Classifier Prediction: {'Negative' if coef > 0 else 'Positive'}")
        plt.fill_between([decision_boundary, max(x_train)[0]], 0.9, 4.1, color=right_color, alpha=0.2,
                         label=f"Classifier Prediction: {'Positive' if coef > 0 else 'Negative'}")

    x_pos = []
    x_neg = []
    y_pos = []
    y_neg = []

    for _, row in df.iterrows():
        label = row['Characteristic Binary']
        if label:
            x_pos.append(row['Cross-Entropy'])
            y_pos.append(row['Avg_Likert'])
        else:
            x_neg.append(row['Cross-Entropy'])
            y_neg.append(row['Avg_Likert'])

    plt.scatter(x_pos, y_pos, color='green', alpha=1.0, label='Correct Assessment: Positive')
    plt.scatter(x_neg, y_neg, color='red', alpha=1.0, label='Correct Assessment: Negative')

    # Decision boundary
    plt.axvline(x=decision_boundary, color='black', linestyle='--', label='Decision Boundary')

    plt.title(f'Logistic Regression {model_name} {characteristic}')
    plt.xlabel('Cross-Entropy')
    plt.ylabel("Average Likert Score")
    plt.legend()
    plt.tight_layout()

    output_path = f"plots/{characteristic}/LogisticRegression/{model_name}.png"
    # Save the plot
    plt.savefig(output_path, format='png', dpi=300)

    plt.close()


def generate_characteristic_results(directory_path, characteristic):
    all_files = [f for f in os.listdir(directory_path) if os.path.isfile(os.path.join(directory_path, f))]

    results = []

    for file in all_files:
        csv_file_path = os.path.join(directory_path, file)
        try:
            model_name = file.split('_')[1]
            max_tokens = file.split('_')[2].replace('.csv', '')

            spearman_results = compute_spearman_rank_correlation(csv_file_path, characteristic)
            regression_results = compute_regression_metrics(csv_file_path, characteristic, model_name)
            binary_results = compute_binary_metrics(csv_file_path, characteristic, model_name)

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

