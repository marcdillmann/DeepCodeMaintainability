# DeepCodeMaintainability
This repository provides an approach to evaluate the maintainability of code using deep-learning-based models, which is created for the master thesis "Assessment of Deep Learning Models for Code Quality Evaluation" by Marc Dillmann at the RPTU Kaiserslautern-Landau in corporation with the data science division of Fraunhofer IESE. By leveraging pre-trained transformer models from the Hugging Face library, the system computes the cross-entropy of given java code files, providing a metric for code maintainability.


## Features
- Cross-Entropy Calculation: Evaluate code quality based on the computed cross-entropy.
- Chunking Strategy: Efficiently process long code files by splitting them into chunks matching the maximum input size of the corresponding model.
- CSV Output: Generate a detailed CSV report containing cross-entropy values and additional metrics for each evaluated file.
- Support for Multiple Transformer Models: Easily plug-and-play different transformer models from the Hugging Face library.

## Installation
Clone the repository:
```
git clone https://github.com/marcdillmann/DeepCodeMaintainability.git
```
Navigate to the repository's directory:
```
cd DeepCodeMaintainability
```
Create a virtual Environment and install the required Python packages:
```
pip install virtualenv
virtualenv venv
source venv/bin/activate
pip install -r requirements.txt
```

## Usage

Run the main script:
```
python main.py <model_name>
```
Currently supported model names:
- GPT-2
- CodeGPTSmallJava
- CodeGPTJava
- Codegen350m
- Bloomz-560m
- Bloomz-1b1
- Bloomz-1b7
- Bloomz-3b
- Bloomz-7b1
- CodeLlama-7b
- CodeLlama-13b
- CodeBERTaTextGeneration
- CodeBERTaFillMask

Once completed, check the output directory (or your specified directory) for the generated CSV report, which has the format 'Evaluation_modelname_timestamp'.

## Customization
Transformer Models: The system is designed to be compatible with various transformer models.
To extend the program to use models other than the ones currently implemented, create a new model at the bottom of 'CodeMaintainabilityEvaluators.py'.
The existing models can be used as a template. Then add it to 'main.py', similar to the already supported models.

## Performance Measurement
Evaluating the effectiveness of our models is an important part. We use a variety of metrics to measure the performance of our models on the code maintainability assessment task.

### Regression Metrics:
Mean Squared Error (MSE): Measures the average of the squares of the errors or deviations.

### Classification Metrics:
Accuracy: Calculates the ratio of the number of correct predictions to the total number of input samples.

F1-Score: Harmonic mean of precision and recall, provides a more robust evaluation metric when dealing with imbalanced datasets.

AUC (Area Under Curve): The AUC-ROC curve is a performance measurement for the classification problem at various threshold settings. AUC represents the probability that a random positive example is positioned to the right of a random negative example.

Accuracy (Binary Classification): Specifically for our binary classification task of determining maintainability as True or False.

### Correlation Metric:
Spearman Rank Correlation: Measures the strength and direction of the monotonic relationship between two ranked variables. In our case, it evaluates the correlation between computed cross entropy and maintainability assessments.
To better understand and interpret these metrics, it's crucial to understand the problem space and the nature of the data. For instance:

A high F1-score indicates good precision and recall balance, which is especially important when classes are imbalanced.
The Spearman Rank Correlation value lies between -1 and 1. A value close to 1 implies a strong positive correlation, meaning as one variable increases, the other also does and vice-versa. A negative correlation implies that as one variable increases, the other variable decreases. 

## License
This project is licensed under the MIT License.