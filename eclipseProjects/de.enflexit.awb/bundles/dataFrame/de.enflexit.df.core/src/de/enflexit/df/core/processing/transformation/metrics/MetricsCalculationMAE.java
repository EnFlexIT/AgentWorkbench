package de.enflexit.df.core.processing.transformation.metrics;

import tech.tablesaw.api.NumericColumn;

/**
 * This {@link MetricsCalculation} calculates the mean absolute error for two columns.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class MetricsCalculationMAE implements MetricsCalculation {
	
	private static final String METRIC_IDENTIFIER = "MAE";
	private static final String METRIC_DISPLAY_NAME = "Mean Absolute Error";

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
	public String getDisplayName() {
		return METRIC_DISPLAY_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.metrics.MetricsCalculation#calculateMetric(tech.tablesaw.api.NumericColumn, tech.tablesaw.api.NumericColumn)
	 */
	@Override
	public Double calculateMetric(NumericColumn<?> referenceColumn, NumericColumn<?> compareColumn) {
		
		int n=0;
		double sumAbsoluteError = 0.0;
		
		for (int i = 0; i < referenceColumn.size(); i++) {

			if (referenceColumn.isMissing(i) || compareColumn.isMissing(i)) {
				continue;
			}

			double reference = referenceColumn.getDouble(i);
			double compare = compareColumn.getDouble(i);
			double error = reference - compare;

			sumAbsoluteError += Math.abs(error);
			n++;
		}
		
        Double result = n>0 ? sumAbsoluteError / n : Double.NaN;
		return result;
	}

}
