# DeepCodeMaintainability
This repository provides an approach to evaluate the maintainability of code using deep-learning-based models, which is created for the master thesis "Assessment of Deep Learning Models for Code Quality Evaluation" by Marc Dillmann at the RPTU Kaiserslautern-Landau in corporation with the data science division of Fraunhofer IESE. By leveraging pre-trained transformer models from the Hugging Face library, the system computes the cross-entropy of given java code files, providing a metric for code maintainability.


## Features
- Cross-Entropy Calculation: Evaluate code quality based on the computed cross-entropy.
- Chunking Strategy: Efficiently process long code files by splitting them into manageable chunks.
- CSV Output: Generate a detailed CSV report containing cross-entropy values and additional metrics for each evaluated file.
- Support for Multiple Transformer Models: Easily plug-and-play different transformer models from the Hugging Face library.

## Installation
Clone the repository:
```
git clone https://github.com/marcdillmann/DeepCodeMaintainability.git
```
Navigate to the repository's directory:
```
cd CodeQualityEvaluation
```
Install the required Python packages:
```
pip install -r requirements.txt
```

## Usage

Run the main script:
```
python main.py <model_name>
```
Currently supported model names:
- GPT-2 
- Bloomz-560m 
- CodeBERTaTextGeneration 
- CodeBERTaFillMask

Once completed, check the output directory (or your specified directory) for the generated CSV report, which has the format 'Evaluation_modelname_timestamp'.

## Customization
Transformer Models: The system is designed to be compatible with various transformer models.
To extend the program to use models other than the ones currently implemented, create a new model at the bottom of 'CodeMaintainabilityEvaluators.py'.
The existing models can be used as a template. Then add it to 'main.py', similar to the already supported models.

## License
This project is licensed under the MIT License.