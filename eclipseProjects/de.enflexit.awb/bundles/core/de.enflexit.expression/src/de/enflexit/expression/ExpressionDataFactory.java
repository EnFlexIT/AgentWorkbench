package de.enflexit.expression;

import de.enflexit.expression.ExpressionData.DataType;

/**
 * A factory for creating ExpressionResult objects (basically .
 */
public class ExpressionDataFactory {

	/**
	 * Creates a new ExpressionData instance for a time series, where the first column is the time stamp.
	 *
	 * @param noOfDataColumns the no of data columns
	 * @return the expression data that can be used within an {@link ExpressionResult} 
	 */
	public static ExpressionData createTimeSeriesExpressionData(int noOfDataColumns) {

		// --- Create TimeSeriesDescription ---------------
		TimeSeriesDescription tsd = new TimeSeriesDescription();
		for (int i = 0; i < noOfDataColumns; i++) {
			tsd.addDataColumn("Value_" + (i+1), DataType.Double);
		}
		// --- Use the main production method -------------
		return createTimeSeriesExpressionData(tsd);
	}

	/**
	 * Creates a new ExpressionData instance based on the specified {@link TimeSeriesDescription}.
	 *
	 * @param tsd the time series description
	 * @return the expression data
	 */
	public static ExpressionData createTimeSeriesExpressionData(TimeSeriesDescription tsd) {
		if (tsd==null) return null;
		return new ExpressionData(tsd);
	}
	
}
