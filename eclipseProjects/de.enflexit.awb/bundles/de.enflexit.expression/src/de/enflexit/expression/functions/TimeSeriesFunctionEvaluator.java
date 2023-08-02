package de.enflexit.expression.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.enflexit.expression.ExpressionData;
import de.enflexit.expression.ExpressionResult;

/**
 * This class is responsible for the evaluation of time series related {@link ExpressionFunction}s.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesFunctionEvaluator {
	
	private enum DiscretizationMode{
		LINEAR, STEPWISE
	}
	
	private ExpressionFunction expressionFunction;
	private ArrayList<ExpressionData> parameters;
	
	private String errorMessage;
	
	/**
	 * Instantiates a new time series function evaluator.
	 * @param expressionFunction the expression function
	 * @param parameters the parameters
	 */
	public TimeSeriesFunctionEvaluator(ExpressionFunction expressionFunction, ArrayList<ExpressionData> parameters) {
		this.expressionFunction = expressionFunction;
		this.parameters = parameters;
	}
	
	/**
	 * Gets the expression result.
	 * @return the expression result
	 */
	public ExpressionResult getExpressionResult() {
		ExpressionResult expressionResult = new ExpressionResult();
		
		if (this.checkParameters()==true) {
			// --- Parameters are okay, start discretization --------
			if (expressionFunction==ExpressionFunction.DiscretizeLinear) {
				expressionResult.setExpressionData(this.discretize(parameters, DiscretizationMode.LINEAR));
			} else if (expressionFunction==ExpressionFunction.DiscretizeStepWise) {
				expressionResult.setExpressionData(this.discretize(parameters, DiscretizationMode.STEPWISE));
			}
		} else {
			// --- Parameters are not okay, show an error message ---
			expressionResult.addErrorMessage(this.errorMessage);
		}
		
		return expressionResult;
	}
	
	/**
	 * Checks if the provided parameters are valid.
	 * @return true, if successful
	 */
	private boolean checkParameters() {
		if (this.parameters==null) {
			this.errorMessage = "No parameters passed!";
			return false;
		}
		
		if (this.parameters.size()!=4) {
			this.errorMessage = "Wrong number of parameters passed!";
			return false;
		}
		
		//TODO check parameter types
		
		long startTime = Double.valueOf(parameters.get(1).getDoubleValue()).longValue();
		long endTime = Double.valueOf(parameters.get(2).getDoubleValue()).longValue();
		if (endTime <= startTime) {
			this.errorMessage = "Invalid parameters: End time must be after start time!";
			return false;
		}
		
		ExpressionData timeSeries = parameters.get(0);
		List<Long> timeStamps = timeSeries.getDataColumn(0).getLongList();
		Long timeSeriesStart = Collections.min(timeStamps);
		Long timeSeriesEnd = Collections.max(timeStamps);
		if (startTime < timeSeriesStart || startTime > timeSeriesEnd || endTime > timeSeriesEnd) {
			this.errorMessage = "Invalid parameters: The specified time range exceeds the original time series!";
			return false;
		}
		
		return true;
	}
	
	/**
	 * Discretizes the time series using linear interpolation.
	 * @param parameters the parameters
	 * @return the expression data
	 */
	private ExpressionData discretize(ArrayList<ExpressionData> parameters, DiscretizationMode discretizationMode) {
		
		long startTime = Double.valueOf(parameters.get(1).getDoubleValue()).longValue();
		long endTime = Double.valueOf(parameters.get(2).getDoubleValue()).longValue();

		ExpressionData timeSeries = parameters.get(0);
		
		int numberOfSteps = parameters.get(3).getIntegerValue();
		long stepLength = (endTime-startTime) / numberOfSteps;
		
		Double[] stepValues = new Double[numberOfSteps];
		
		long currStepStart = startTime;
		int i = 0;
		while (currStepStart<endTime) {
			if (discretizationMode==DiscretizationMode.LINEAR) {
				stepValues[i] = this.getInterpolatedValueForTime(timeSeries, currStepStart);
			} else {
				stepValues[i] = this.getStepValueForTime(timeSeries, currStepStart);
			}
			i++;
			currStepStart += stepLength;
		}
		
		ExpressionData expressionResult = new ExpressionData(stepValues);
		return expressionResult;
	}
	
	/**
	 * Determines the value for one specific time in an interpolated discretization.
	 * @param timeSeries the time series
	 * @param timeSearch the time of interest
	 * @return the value
	 */
	private double getInterpolatedValueForTime(ExpressionData timeSeries, long timeSearch) {
		List<Long> timeStamps = timeSeries.getDataColumn(0).getLongList();
		List<Double> values = timeSeries.getDataColumn(1).getDoubleList();
		
		int index = 0;
		while (timeStamps.get(index)<timeSearch) {
			index++;
		}
		
		if (timeStamps.get(index)==timeSearch) {
			// --- Exact match -> return the value ------------------
			return values.get(index);
		} else {
			// --- No exact match -> interpolate linear -------------
			long timeFrom = timeStamps.get(index-1);
			long timeTo = timeStamps.get(index);
			
			double valueFrom = values.get(index-1);
			double valueTo = values.get(index);
			
			double intermediate1 = (valueTo-valueFrom)/(timeTo-timeFrom);
			double intermediate2 = intermediate1 * (timeSearch -timeFrom);
			
			return valueFrom + intermediate2;
		}
	}
	
	/**
	 * Gets the value for a specific time for a stepwise discretization.
	 * @param timeSeries the time series
	 * @param timeSearch the time of interest
	 * @return the value
	 */
	private double getStepValueForTime(ExpressionData timeSeries, long timeSearch) {
		List<Long> timeStamps = timeSeries.getDataColumn(0).getLongList();
		List<Double> values = timeSeries.getDataColumn(1).getDoubleList();
		
		// --- Find the relevant time series entry ------------------
		int index = 0;{
			while (timeStamps.get(index)<timeSearch) {
				index++;
			}
		}

		if (timeStamps.get(index)==timeSearch) {
			// --- Exact match -> return the matching entry ---------
			return values.get(index);
		} else {
			// --- No exact match -> return the previous entry ------
			return values.get(index-1);
		}
	}
		
}
