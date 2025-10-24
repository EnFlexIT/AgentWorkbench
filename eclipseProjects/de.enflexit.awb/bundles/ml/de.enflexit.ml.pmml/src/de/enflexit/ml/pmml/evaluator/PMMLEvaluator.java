package de.enflexit.ml.pmml.evaluator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.jpmml.evaluator.Computable;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.jpmml.evaluator.TargetField;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;

public class PMMLEvaluator {
	
	private File modelFile;
	private URL modelURL;
	
	private Evaluator evaluator;
	private Map<String, ?> prediction;
	
	private String errorMessage;
	
	/**
	 * Instantiates a new PMML evaluator, loading the model from a file.
	 * @param modelFile the model file
	 */
	public PMMLEvaluator(File modelFile) {
		if (modelFile==null) {
			throw new IllegalArgumentException("Model file must not be null!");
		}
		this.modelFile = modelFile;
		this.loadModel();
	}
	
	/**
	 * Instantiates a new PMML evaluator, loading the model from an URL.
	 * @param modelURL the model URL
	 */
	public PMMLEvaluator(URL modelURL) {
		if (modelURL==null) {
			throw new IllegalArgumentException("Model URL must not be null!");
		}
		this.modelURL = modelURL;
		this.loadModel();
	}
	
	/**
	 * Loads the configured PMML model.
	 */
	public void loadModel() {
		if (this.modelFile!=null) {
			this.evaluator = this.loadModelFromFile();
		} else if (this.modelURL!=null) {
			this.evaluator = this.loadModelFromURL();
		}
	}
	
	/**
	 * Loads a PMML  model from configured file.
	 * @return the evaluator
	 */
	private Evaluator loadModelFromFile() {
		Evaluator evaluator = null;
		
		if (modelFile!=null) {
			try {
				evaluator = new LoadingModelEvaluatorBuilder().load(modelFile).build();
				evaluator.verify();
			} catch (IOException | ParserConfigurationException | SAXException | JAXBException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error loading PMML model from file: " + modelFile.getAbsolutePath());
				e.printStackTrace();
			}
		}
		
		return evaluator;
	}
	
	/**
	 * Loads a PMML model from the configured URL.
	 * @return the evaluator
	 */
	private Evaluator loadModelFromURL() {
		Evaluator evaluator = null;
		
		if (modelURL!=null) {
			try {
				InputStream inputStream = this.modelURL.openStream(); 
				evaluator = new LoadingModelEvaluatorBuilder().load(inputStream).build();
				evaluator.verify();
			} catch (IOException | ParserConfigurationException | SAXException | JAXBException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error loading PMML model from file: " + modelFile.getAbsolutePath());
				e.printStackTrace();
			}
		}
		
		return evaluator;
	}
	
	/**
	 * Gets the input fields (=features) for the PMML model.
	 * @return the input fields
	 */
	public List<InputField> getInputFields(){
		if (evaluator!=null) {
			return this.evaluator.getInputFields();
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the names of all input fields (=features).
	 * @return the input field names
	 */
	public List<String> getInputFieldNames(){
		List<String> fieldNames = new ArrayList<String>();
		for (InputField inputField : this.getInputFields()) {
			fieldNames.add(inputField.getName());
		}
		return fieldNames;
	}
	
	/**
	 * Gets the target fields (=labels) for the PMML model.
	 * @return the target fields
	 */
	public List<TargetField> getTargetFields(){
		if (evaluator!=null) {
			return this.evaluator.getTargetFields();
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the names of all target fields (=labels).
	 * @return the target field names
	 */
	public List<String> getTargetFieldNames(){
		List<String> fieldNames = new ArrayList<String>();
		for (TargetField targetField : this.getTargetFields()) {
			fieldNames.add(targetField.getName());
		}
		return fieldNames;
	}
	
	/**
	 * Checks if a PMML model was successfully loaded.
	 * @return true, if is model loaded
	 */
	public boolean isModelLoaded() {
		return this.evaluator!=null;
	}
	
	/**
	 * Makes a prediction for the provided input values using the loaded model.
	 * @param inputValues the input values
	 * @return the predicted target values, null in case of failure
	 */
	public Map<String, ?> makePrediction(Map<String, ?> inputValues) {
		if (this.evaluator==null) {
			this.errorMessage = "No Model loaded!";
			return null;
		}
		
		List<String> missingInputs = this.checkForMissingInputs(inputValues);
		if (missingInputs.size()>0) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Missing input values: " + String.join(", ", missingInputs));
//			this.errorMessage = "Missing input values: " + String.join(", ", missingInputs);
			return null;
		}
		
		this.errorMessage = "";
		this.prediction = evaluator.evaluate(inputValues);
		return this.prediction;
	}
	
	/**
	 * Check if all required inputs are provided.
	 * @param inputs the provided inputs
	 * @return a list of missing inputs, empty if all required inputs are provided.
	 */
	private List<String> checkForMissingInputs(Map<String, ?> inputs) {
		List<String> missingInputs = new ArrayList<String>();
		Set<String> inputKeys = inputs.keySet();
		for (InputField inputField : this.getInputFields()) {
			if (inputKeys.contains(inputField.getName())==false) {
				missingInputs.add(inputField.getName());
			}
		}
		return missingInputs;
	}
	
	/**
	 * Gets the error current error message.
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * Predicts a single double value based on the provided input values.
	 * Make sure to specify a value for every required input (check getInputFieldNames() if unsure).
	 * Assumes the model has only one target field. If there are more, the first one is returned. If your model has
	 * more than one target fields, use the makePrediction() method to get the whole result for further processing. 
	 * @param inputValues the input values
	 * @return the double
	 */
	public Double predictDoubleValue(Map<String, ?> inputValues) {
		Map<String, ?> prediction = this.makePrediction(inputValues);
		if (prediction==null) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Value prediction failed!");
			return null;
		} else {
			Object predictionObject = prediction.get(this.getTargetFieldNames().get(0));
			if (predictionObject instanceof Computable) {
				Object predictedValue = ((Computable)predictionObject).getResult();
				if (predictedValue instanceof Number) {
					return ((Number)predictedValue).doubleValue();
				}
			} else if (predictionObject instanceof Number) {
				return ((Number) predictionObject).doubleValue();
			}
			System.err.println("[" + this.getClass().getSimpleName() + "] Unexpected type of prediction result!");
			return null;
		}
	}
}
