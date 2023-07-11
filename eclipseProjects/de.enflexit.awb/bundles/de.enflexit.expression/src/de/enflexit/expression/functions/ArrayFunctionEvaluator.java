package de.enflexit.expression.functions;

import de.enflexit.expression.ExpressionData;
import de.enflexit.expression.ExpressionData.DataColumn;
import de.enflexit.expression.ExpressionData.DataType;
import de.enflexit.expression.ExpressionResult;

/**
 * The Class ArrayFunctionEvaluator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ArrayFunctionEvaluator {

	private ExpressionFunction expressionFunction;
	private ExpressionData expressionData;
	private int sourceDataColumnIndex; 
	
	/**
	 * Instantiates a new array function evaluator.
	 *
	 * @param expressionFunction the expression function
	 * @param expressionData the expression data
	 */
	public ArrayFunctionEvaluator(ExpressionFunction expressionFunction, ExpressionData expressionData) {
		this(expressionFunction, expressionData, 1);
	}
	/**
	 * Instantiates a new array function evaluator.
	 *
	 * @param expressionFunction the expression function
	 * @param expressionData the expression data
	 * @param sourceDataColumnIndex the source data column index
	 */
	public ArrayFunctionEvaluator(ExpressionFunction expressionFunction, ExpressionData expressionData, int sourceDataColumnIndex) {
		this.expressionFunction = expressionFunction;
		this.expressionData = expressionData;
		this.sourceDataColumnIndex = sourceDataColumnIndex;
	}
	
	/**
	 * Returns the expression result.
	 * @return the expression result
	 */
	public ExpressionResult getExpressionResult() {
		
		ExpressionResult result = new ExpressionResult();
		ExpressionData resultData = null;
		double aggregatedValue = 0;
		
		if (this.expressionData.isSingleValueResult()==true) {
			result.setExpressionData(this.expressionData);
			
		} else if (this.expressionData.isArray()==true) {
			
			switch (this.expressionData.getDataType()) {
			case Boolean:
				aggregatedValue = this.getAggregationResultDouble(this.expressionData.getBooleanArray(), this.expressionData.getDataType());
				if (aggregatedValue==1.0) {
					resultData = new ExpressionData(true);
				} else if (aggregatedValue==0.0) {
					resultData = new ExpressionData(false);
				} else {
					resultData = new ExpressionData(aggregatedValue);
				}
				break;
			
			case Integer:
				aggregatedValue = this.getAggregationResultDouble(this.expressionData.getIntegerArray(), this.expressionData.getDataType());
				resultData = new ExpressionData(aggregatedValue);
				break;
			
			case Long:
				aggregatedValue = this.getAggregationResultDouble(this.expressionData.getLongArray(), this.expressionData.getDataType());
				resultData = new ExpressionData(aggregatedValue);
				break;
				
			case Double:
				aggregatedValue = this.getAggregationResultDouble(this.expressionData.getDoubleArray(), this.expressionData.getDataType());
				resultData = new ExpressionData(aggregatedValue);
				break;
			}
			
			// --- Set ExpressionData to result -----------
			result.setExpressionData(resultData);
			
		} else if (this.expressionData.isTimeSeries()==true) {
			
			DataColumn dCol = this.expressionData.getDataColumn(this.sourceDataColumnIndex);
			switch (dCol.getDataType()) {
			case Boolean:
				aggregatedValue = this.getAggregationResultDouble(dCol.getBooleanList().toArray(), dCol.getDataType());
				if (aggregatedValue==1.0) {
					resultData = new ExpressionData(true);
				} else if (aggregatedValue==0.0) {
					resultData = new ExpressionData(false);
				} else {
					resultData = new ExpressionData(aggregatedValue);
				}
				break;
			
			case Integer:
				aggregatedValue = this.getAggregationResultDouble(dCol.getIntegerList().toArray(), dCol.getDataType());
				resultData = new ExpressionData(aggregatedValue);
				break;
			
			case Long:
				aggregatedValue = this.getAggregationResultDouble(dCol.getLongList().toArray(), dCol.getDataType());
				resultData = new ExpressionData(aggregatedValue);
				break;
				
			case Double:
				aggregatedValue = this.getAggregationResultDouble(dCol.getDoubleList().toArray(), dCol.getDataType());
				resultData = new ExpressionData(aggregatedValue);
				break;
			}
			
			// --- Set ExpressionData to result -----------
			result.setExpressionData(resultData);
			
		}
		return result;
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
				sum += value;
				break;

			case MAX:
				aggregatedValue = Math.max(aggregatedValue, value);
				break;
				
			case MIN:
				aggregatedValue = Math.min(aggregatedValue, value);
				break;
			}
		}
		
		if (this.expressionFunction==ExpressionFunction.AVG) {
			aggregatedValue = sum / dataArray.length;
		}
		
		return aggregatedValue;
	}
	
	
	
}
