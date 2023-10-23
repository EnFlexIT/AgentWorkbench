package de.enflexit.expression.functions;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.expression.ExpressionData;
import de.enflexit.expression.ExpressionData.DataColumn;
import de.enflexit.expression.ExpressionData.DataType;
import de.enflexit.expression.ExpressionResult;

/**
 * The Class ArrayFunctionEvaluator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ArrayFunctionEvaluator {

	private ExpressionFunction expressionFunction;
	private List<ExpressionData> parametersList;
	private int sourceDataColumnIndex; 
	
	/**
	 * Instantiates a new array function evaluator with a single parameter (that can also be an array or time series).
	 * @param expressionFunction the expression function
	 * @param parameterData the expression data
	 */
	public ArrayFunctionEvaluator(ExpressionFunction expressionFunction, ExpressionData parameterData) {
		this(expressionFunction, parameterData, 1);
	}
	/**
	 * Instantiates a new array function evaluator with a single parameter (that can also be an array or time series).
	 * @param expressionFunction the expression function
	 * @param parameterData the expression data
	 * @param sourceDataColumnIndex the source data column index
	 */
	public ArrayFunctionEvaluator(ExpressionFunction expressionFunction, ExpressionData parameterData, int sourceDataColumnIndex) {
		this(expressionFunction, new ArrayList<ExpressionData>(), sourceDataColumnIndex);
		this.parametersList.add(parameterData);
	}
	
	/**
	 * Instantiates a new array function evaluator with a list of parameters (that can also be arrays or time series).
	 * @param expressionFunction the expression function
	 * @param parametersList the parameters list
	 */
	public ArrayFunctionEvaluator(ExpressionFunction expressionFunction, List<ExpressionData> parametersList) {
		this(expressionFunction, parametersList, 1);
	}
	
	/**
	 * Instantiates a new array function evaluator with a list of parameters (that can also be arrays or time series).
	 * @param expressionFunction the expression function
	 * @param parametersList the parameters list
	 * @param sourceDataColumnIndex the source data column index
	 */
	public ArrayFunctionEvaluator(ExpressionFunction expressionFunction, List<ExpressionData> parametersList, int sourceDataColumnIndex) {
		this.expressionFunction = expressionFunction;
		this.parametersList = parametersList;
		this.sourceDataColumnIndex = sourceDataColumnIndex;
	}
	
	/**
	 * Returns the expression result.
	 * @return the expression result
	 */
	public ExpressionResult getExpressionResult() {
		double resultValue;
		
		// --- Set the appropriate start value for the current function -------
		switch (this.expressionFunction) {
		case MIN:
			resultValue = Double.MAX_VALUE;
			break;
		case MAX:
			resultValue = Double.MIN_VALUE;
			break;
		default:
			resultValue = 0;
			break;
		}
		
		for (ExpressionData parameter : this.parametersList) {
			double paramValue = this.getDoubleResultFromParameter(parameter);
			switch (this.expressionFunction) {
			case AVG:
			case SUM:
				resultValue += paramValue;
				break;
			case MIN:
				resultValue = Math.min(resultValue, paramValue);
				break;
			case MAX:
				resultValue = Math.max(resultValue, paramValue);
				break;
			default:
				break;
			}
		}
		
		if (this.expressionFunction==ExpressionFunction.AVG) {
			resultValue /= parametersList.size();
		}
		
		return new ExpressionResult(resultValue);
	}
	
	/**
	 * Gets the double result from the specified parameter. For single value parameters, the value will be converted if necessary
	 * and returned. For array and time series parameters, the currently evaluated function will be applied on the elements. 
	 * @param parameter the parameter
	 * @return the double result from parameter
	 */
	private double getDoubleResultFromParameter(ExpressionData parameter) {
		double resultValue = 0;
		Object[] dataArray = null;
		
		if (parameter.isSingleValueResult()) {
			switch(parameter.getDataType()) {
			case Boolean:
				if (parameter.getBooleanValue()==true) {
					resultValue = 1.0;
				} else {
					resultValue = 0.0;
				}
				break;
			case Integer:
				resultValue = Integer.valueOf(parameter.getIntegerValue()).doubleValue();
				break;
			case Long:
				resultValue = Long.valueOf(parameter.getLongValue()).doubleValue();
				break;
			case Double:
				resultValue  = parameter.getDoubleValue();
				break;
			}
		} else if (parameter.isArray()==true) {
			switch(parameter.getDataType()) {
			case Boolean:
				dataArray = parameter.getBooleanArray();
				break;
			case Integer:
				dataArray = parameter.getIntegerArray();
				break;
			case Long:
				dataArray = parameter.getLongArray();
				break;
			case Double:
				dataArray = parameter.getDoubleArray();
				break;
			}
			resultValue = this.getAggregationResultDouble(dataArray, parameter.getDataType());
			
		} else if (parameter.isTimeSeries()==true) {
			DataColumn dataColumn = parameter.getDataColumn(this.sourceDataColumnIndex);
			switch (dataColumn.getDataType()) {
			case Boolean:
				dataArray = dataColumn.getBooleanList().toArray();
				break;
			case Integer:
				dataArray = dataColumn.getIntegerList().toArray();
				break;
			case Long:
				dataArray = dataColumn.getLongList().toArray();
				break;
			case Double:
				dataArray = dataColumn.getDoubleList().toArray();
				break;
			}
			resultValue = this.getAggregationResultDouble(dataArray, dataColumn.getDataType());
		}
		
		return resultValue;
	}
	
	/**
	 * Returns the aggregation result double.
	 *
	 * @param dataArray the data array to work on
	 * @param dataType the data type
	 * @return the aggregation result double
	 */
	private double getAggregationResultDouble(Object[] dataArray, DataType dataType) {
		
		double aggregatedValue = 0;
		double sum = 0;
		
		if (this.expressionFunction==ExpressionFunction.MAX) {
			aggregatedValue = Double.MIN_VALUE;
		} else if (this.expressionFunction==ExpressionFunction.MIN) {
			aggregatedValue = Double.MAX_VALUE;
		}
		
		for (Object valueObject : dataArray) {
			
			if (valueObject==null) continue;
			
			Double value = null;
			switch (dataType) {
			case Boolean:
				value = ((Boolean) valueObject)==true ? 1.0 : 0.0;
				break;
			case Integer:
				value = (Double) valueObject;
				break;
			case Long:
				value = (Double) valueObject;
				break;
			case Double:
				value = (Double) valueObject;
				break;
			}
			if (value==null) continue;
			
			switch (this.expressionFunction) {
			case AVG:
			case SUM:
				sum += value;
				break;

			case MAX:
				aggregatedValue = Math.max(aggregatedValue, value);
				break;
				
			case MIN:
				aggregatedValue = Math.min(aggregatedValue, value);
				break;
			default:
				break;
			}
		}
		
		if (this.expressionFunction==ExpressionFunction.AVG) {
			aggregatedValue = sum / dataArray.length;
		} else if (this.expressionFunction==ExpressionFunction.SUM) {
			return sum;
		}
		return aggregatedValue;
	}
	
}
