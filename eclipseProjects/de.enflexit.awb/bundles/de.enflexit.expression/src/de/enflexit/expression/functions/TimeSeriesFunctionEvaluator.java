package de.enflexit.expression.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.enflexit.expression.ExpressionData;
import de.enflexit.expression.ExpressionResult;

public class TimeSeriesFunctionEvaluator {
	
	private ExpressionFunction expressionFunction;
	private ArrayList<ExpressionData> parameters;
	
	public TimeSeriesFunctionEvaluator(ExpressionFunction expressionFunction, ArrayList<ExpressionData> parameters) {
		this.expressionFunction = expressionFunction;
		this.parameters = parameters;
	}
	
	public ExpressionResult getExpressionResult() {
		ExpressionResult expressionResult = null;
		
		if (expressionFunction==ExpressionFunction.TimeSeriesDiscretization) {
			expressionResult = this.discretizeTimeSeries(parameters);
		}
		
		return expressionResult;
	}
	
	private ExpressionResult discretizeTimeSeries(ArrayList<ExpressionData> parameters) {
		ExpressionResult expressionResult = new ExpressionResult();
		
		if (expressionFunction==ExpressionFunction.TimeSeriesDiscretization) {
			if (this.parameters==null) {
				expressionResult.addErrorMessage("No parameters passed!");
			} else if (this.parameters.size()!=4) {
				expressionResult.addErrorMessage("Wrong number of parameters passed!");
			}

			long startTime = Double.valueOf(parameters.get(1).getDoubleValue()).longValue();
			long endTime = Double.valueOf(parameters.get(2).getDoubleValue()).longValue();
			if (endTime <= startTime) {
				expressionResult.addErrorMessage("Invalid parameters: End time must be after start time!");
			}

			ExpressionData timeSeries = parameters.get(0);
			List<Long> timeStamps = timeSeries.getDataColumn(0).getLongList();
			Long timeSeriesStart = Collections.min(timeStamps);
			Long timeSeriesEnd = Collections.max(timeStamps);
			if (startTime < timeSeriesStart || startTime > timeSeriesEnd || endTime > timeSeriesEnd) {
				expressionResult.addErrorMessage("Invalid parameters: The specified time range exceeds the original time series!");
			}
			
			int numberOfSteps = parameters.get(3).getIntegerValue();
			long stepLength = (endTime-startTime) / numberOfSteps;
			
			Double[] stepValues = new Double[numberOfSteps];
			
			long currStepStart = startTime;
			int i = 0;
			while (currStepStart<endTime) {
				long currStepEnd = currStepStart + stepLength;
				double stepValue = this.calculateStepValue(timeSeries, currStepStart, currStepEnd);
				stepValues[i] = stepValue;
				i++;
				currStepStart += stepLength;
			}
			
			expressionResult.setExpressionData(new ExpressionData(stepValues));
		}
		
		return expressionResult;
	}
	
	private double calculateStepValue(ExpressionData timeSeries, long stepStartTime, long stepEndTime) {
		double avgValue = 0;
		List<Long> timeStamps = timeSeries.getDataColumn(0).getLongList();
		List<Double> values = timeSeries.getDataColumn(1).getDoubleList();
		
		// --- Find the first step to consider --------------------------------
		int indexFrom = 0;{
			while (timeStamps.get(indexFrom)<stepStartTime) {
				indexFrom++;
			}
			
			// --- If there is no perfect match, go one step back to include the state that contains stepStartTime   
			if (timeStamps.get(indexFrom)>stepStartTime) {
				indexFrom--;
			}
		}

		if (timeStamps.get(indexFrom+1)>=stepEndTime) {
			// --- Only one state to consider -----------------------
			avgValue = values.get(indexFrom);
		} else {
			int nextIndex=indexFrom+1;
			int statesConsidered = 0;
			while(timeStamps.get(nextIndex)<=stepEndTime) {
				avgValue += values.get(nextIndex);
				statesConsidered++;
			}
			avgValue /= statesConsidered;
		}
		
		return avgValue;
	}
		
}
