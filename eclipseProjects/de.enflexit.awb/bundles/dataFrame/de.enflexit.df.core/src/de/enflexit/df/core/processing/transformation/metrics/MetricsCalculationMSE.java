package de.enflexit.df.core.processing.transformation.metrics;

import tech.tablesaw.api.NumericColumn;

/**
 * This {@link MetricsCalculation} calculates the mean squared error for two columns.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class MetricsCalculationMSE implements MetricsCalculation {
	
	private static final String METRIC_IDENTIFIER = "MSE";
	private static final String METRIC_DISPLAY_NAME = "Mean Square Error";

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.metrics.MetricsCalculation#getMetricsIdentifier()
	 */
	@Override
	public String getMetricsIdentifier() {
		return METRIC_IDENTIFIER;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.metrics.MetricsCalculation#getMetricDisplayName()
	 */
	@Override
	public String getMetricDisplayName() {
		return METRIC_DISPLAY_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.metrics.MetricsCalculation#calculateMetric(tech.tablesaw.api.NumericColumn, tech.tablesaw.api.NumericColumn)
	 */
	@Override
	public Double calculateMetric(NumericColumn<?> referenceColumn, NumericColumn<?> compareColumn) {
		
		double sumSquaredError = 0.0;
		int n=0;
		
		for (int i = 0; i < referenceColumn.size(); i++) {

			if (referenceColumn.isMissing(i) || compareColumn.isMissing(i)) {
				continue;
			}

			double reference = referenceColumn.getDouble(i);
			double compare = compareColumn.getDouble(i);
			double error = reference - compare;

			sumSquaredError += error * error;
			n++;
		}
		
		Double result = n>0 ? sumSquaredError / n : Double.NaN;
		return result;
	}

}
