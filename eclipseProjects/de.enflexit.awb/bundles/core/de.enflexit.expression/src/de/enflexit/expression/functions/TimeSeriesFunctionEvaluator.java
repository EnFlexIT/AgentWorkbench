package de.enflexit.expression.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.enflexit.expression.ExpressionData;
import de.enflexit.expression.ExpressionData.DataColumn;
import de.enflexit.expression.ExpressionData.DataType;
import de.enflexit.expression.ExpressionResult;

/**
 * This class is responsible for the evaluation of time series related {@link ExpressionFunction}s.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesFunctionEvaluator {
	
	private enum DiscretizationMode{
		LINEAR, STEPWISE, AVERAGE
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
			} else if (expressionFunction==ExpressionFunction.DiscretizeAverage) {
				expressionResult.setExpressionData(this.discretize(parameters, DiscretizationMode.AVERAGE));
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
		// --- Check if all required parameters are provided --------
		if (this.parameters==null) {
			this.errorMessage = "No parameters passed!";
			return false;
		}
		if (this.parameters.size()!=4) {
			this.errorMessage = "Wrong number of parameters passed!";
			return false;
		}
		for (int i=0; i<4; i++) {
			if (this.parameters.get(i)==null) {
				this.errorMessage = "Parameter no. " + i + " is null!";
				return false;
			}
		}

		// --- Check the parameter types ----------------------------
		if (this.parameters.get(0).isTimeSeries()==false) {
			this.errorMessage = "The first parameter must be a time series!";
			return false;
		}
		if (this.parameters.get(1).getDataType()!=DataType.Double) {
			this.errorMessage = "The second parameter must be of type double!";
			return false;
		}
		if (this.parameters.get(2).getDataType()!=DataType.Double) {
			this.errorMessage = "The third parameter must be of type double!";
			return false;
		}
		if (this.isIntegerValue(this.parameters.get(3))==false) {
			this.errorMessage = "The fourth parameter must be of type integer!";
			return false;
		}
		
		// --- Check if the time range is valid ---------------------
		long startTime = Double.valueOf(parameters.get(1).getDoubleValue()).longValue();
		long endTime = Double.valueOf(parameters.get(2).getDoubleValue()).longValue();
		if (endTime <= startTime) {
			this.errorMessage = "Invalid parameters: End time must be after start time!";
			return false;
		}
		
		// --- Check if the time range is covered by the original data
		ExpressionData timeSeries = parameters.get(0);
		List<Long> timeStamps = timeSeries.getDataColumn(0).getLongList();
		Long timeSeriesStart = Collections.min(timeStamps);
		Long timeSeriesEnd = Collections.max(timeStamps);
		if (startTime < timeSeriesStart || startTime > timeSeriesEnd || endTime > timeSeriesEnd) {
			this.errorMessage = "Invalid parameters: The specified time range exceeds the original time series!";
			return false;
		}
		
		// --- All checks passed -> everything fine -----------------
		return true;
	}
	
	/**
	 * Checks if the provided parameter is either an integer, or a double actually containing an integer value
	 * @param parameter the parameter
	 * @return true, if is integer value
	 */
	private boolean isIntegerValue(ExpressionData parameter) {
		if (parameter.getDataType()==DataType.Integer) {
			// --- The parameter is of type integer -----------------
			return true;
		} else if (parameter.getDataType()==DataType.Double) {
			// --- The parameter is of type double -> check if it has no decimal places
			double value = parameter.getDoubleValue();
			return Math.floor(value)==value;
		}
		return false;
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
		
		if (this.isDescending(timeSeries)==true) {
			for (DataColumn dataColumn : timeSeries.getDataColumnList()) {
				dataColumn.reverseDataList();
			}
		}
		
		int numberOfSteps = this.getIntegerValueFromParameter(parameters.get(3));
		long stepLength;
		if (numberOfSteps>1) {
			stepLength = (endTime-startTime) / (numberOfSteps-1);
		} else {
			stepLength = endTime-startTime;
		}
		
		Double[] stepValues = new Double[numberOfSteps];
		
		long currStepStart = startTime;
		int i = 0;
		while (currStepStart<=endTime) {
			
			switch(discretizationMode) {
			case LINEAR:
				stepValues[i] = this.getInterpolatedValueForTime(timeSeries, currStepStart);
				break;
			case STEPWISE:
				stepValues[i] = this.getStepValueForTime(timeSeries, currStepStart);
				break;
			case AVERAGE:
				stepValues[i] = this.calculateAverage(timeSeries, currStepStart, currStepStart+stepLength);
				break;
			}
			
			i++;
			currStepStart += stepLength;
		}
		
		ExpressionData expressionResult = new ExpressionData(stepValues);
		return expressionResult;
	}
	
	private boolean isDescending(ExpressionData timeSeries) {
		List<Long> timeStamps = timeSeries.getDataColumn(0).getLongList();
		if (timeStamps.size()<=1) {
			return false;
		} else {
			return timeStamps.get(0)>timeStamps.get(1);
		}
	}
	
	/**
	 * Gets the integer value from parameter of type integer or double.
	 * @param parameter the parameter
	 * @return the integer value from parameter
	 */
	private Integer getIntegerValueFromParameter(ExpressionData parameter) {
		if (parameter.getDataType()==DataType.Integer) {
			return parameter.getIntegerValue();
		} else if (parameter.getDataType()==DataType.Double) {
			return Double.valueOf(parameter.getDoubleValue()).intValue();
		} else {
			return null;
		}
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
		int index = 0;
		while (timeStamps.get(index)<timeSearch) {
			index++;
		}

		if (timeStamps.get(index)==timeSearch) {
			// --- Exact match -> return the matching entry ---------
			return values.get(index);
		} else {
			// --- No exact match -> return the previous entry ------
			return values.get(index-1);
		}
	}
	
	private double calculateAverage(ExpressionData timeSeries, long timeFrom, long timeTo) {
		List<Long> timeStamps = timeSeries.getDataColumn(0).getLongList();
		List<Double> values = timeSeries.getDataColumn(1).getDoubleList();
		
		int index = 0;
		// --- Find the relevant time series entry ------------------
		while (timeStamps.get(index)<timeFrom) {
			index++;
		}
		if (timeStamps.get(index)>timeFrom)	index--;
		
		double avgValue = 0;
		int numValues = 0;
		
		do {
			avgValue += values.get(index);
			index++;
			numValues++;
		} while (timeStamps.get(index)<timeTo);
		
		return avgValue/numValues;
	}
		
}
