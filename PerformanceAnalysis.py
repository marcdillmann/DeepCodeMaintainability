import sys
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import mean_squared_error, accuracy_score, classification_report, roc_auc_score
from sklearn.linear_model import LinearRegression
from sklearn.linear_model import LogisticRegression
from scipy.stats import spearmanr


def extract_avg_likert_score(likert_string):
    probabilities = list(map(float, likert_string.strip("{}").split(",")))
    return sum([(i+1)*prob for i, prob in enumerate(probabilities)])


def compute_spearman_rank_correlation(csv_file_path):
    # Load the CSV data into a pandas DataFrame
    df = pd.read_csv(csv_file_path)
    df['Avg_Likert'] = df['Overall'].apply(extract_avg_likert_score)

    # Spearman rank correlation
    correlation, p_value = spearmanr(df['Cross-Entropy'], df['Avg_Likert'])

    print(f"Spearman Rank Correlation between Cross-Entropy and Average Likert Score: {correlation:.4f}")
    print(f"P-value: {p_value:.4f}")


def compute_regression_metrics(csv_file_path):
    df = pd.read_csv(csv_file_path)
    df['Avg_Likert'] = df['Overall'].apply(extract_avg_likert_score)

    # Prepare data
    X = df[['Cross-Entropy']]
    y = df['Avg_Likert']

    # Split data
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    # Normalize the data
    scaler = StandardScaler()
    X_train = scaler.fit_transform(X_train)
    X_test = scaler.transform(X_test)

    # Train the model
    regressor = LinearRegression()
    regressor.fit(X_train, y_train)

    # Predict
    y_pred = regressor.predict(X_test)

    # Evaluate
    mse = mean_squared_error(y_test, y_pred)
    print(f"Mean Squared Error for Regression: {mse}")


def compute_binary_metrics(csv_file_path):
    df = pd.read_csv(csv_file_path)

    # Prepare data for binary classification
    X = df[['Cross-Entropy']]
    y_binary = df['Overall Binary']

    # Split data
    X_train_bin, X_test_bin, y_train_bin, y_test_bin = train_test_split(X, y_binary, test_size=0.2, random_state=42)

    # Normalize the data
    scaler_bin = StandardScaler()
    X_train_bin = scaler_bin.fit_transform(X_train_bin)
    X_test_bin = scaler_bin.transform(X_test_bin)

    # Train the model
    classifier = LogisticRegression()
    classifier.fit(X_train_bin, y_train_bin)

    # Predict
    y_pred_bin = classifier.predict(X_test_bin)

    # Predict probabilities for AUC
    y_pred_prob_bin = classifier.predict_proba(X_test_bin)[:, 1]

    # Evaluate
    accuracy = accuracy_score(y_test_bin, y_pred_bin)
    f1 = classification_report(y_test_bin, y_pred_bin, output_dict=True)['True']['f1-score']
    auc = roc_auc_score(y_test_bin, y_pred_prob_bin)

    print(f"Accuracy for Binary Classification: {accuracy * 100:.2f}%")
    print(f"F1-Score: {f1:.2f}")
    print(f"AUC: {auc:.2f}")
    print(classification_report(y_test_bin, y_pred_bin))


if __name__ == "__main__":

    if len(sys.argv) != 2:
        print("Usage: python PerformanceAnalysis.py <path_to_csv>")
        sys.exit(1)

    csv_file_path = sys.argv[1]

    compute_spearman_rank_correlation(csv_file_path)
    compute_regression_metrics(csv_file_path)
    compute_binary_metrics(csv_file_path)

