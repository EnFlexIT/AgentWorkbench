package de.enflexit.df.core.processing.transformation.metrics;

import tech.tablesaw.api.NumericColumn;

/**
 * The Interface MetricsCalculation.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface MetricsCalculation {
	
	/**
	 * THis method should return a unique ID for this metric (like RMSE or R_SQUARED).
	 * @return the metrics ID
	 */
	public String getMetricsIdentifier();
	/**
	 * This method should return a user-friendly display name to be used in the UI.
	 * @return the metric display name
	 */
	public String getDisplayName();
	
	/**
	 * Implement this method to calculate the actual value for this metric on two columns.
	 * @param referenceColumn the reference column
	 * @param compareColumn the compare column
	 * @return the double
	 */
	public Double calculateMetric(NumericColumn<?> referenceColumn, NumericColumn<?> compareColumn); 
}
