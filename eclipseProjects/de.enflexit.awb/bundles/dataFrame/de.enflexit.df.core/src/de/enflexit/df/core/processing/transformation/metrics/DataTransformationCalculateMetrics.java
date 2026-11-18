package de.enflexit.df.core.processing.transformation.metrics;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.graph.TransformationGraphController;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

public class DataTransformationCalculateMetrics extends AbstractDataTransformation {
	
	public static final String PARAM_NAME_SELECTED_METRICS = "selectedMetrics";
	public static final String PARAM_NAME_REFERENCE_COLUMN = "referenceColumn";
	public static final String PARAM_NAME_COMPARE_COLUMNS = "compareColumns";
	
	/**
	 * The supported metrics
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	public enum Metrics {
		R_SQUARED("R²"),
		RMSE("Root mean squared error"), 
		MAE("Mean absolute error"), 
		MSE("Mean squared error");
		
		final String label;

	    /**
    	 * Instantiates a new metrics.
    	 * @param label the label
    	 */
    	private Metrics(String label) {
	        this.label = label;
	    }
    	
    	/**
	     * Gets the label.
	     * @return the string
	     */
	    public String label() {
			return label;
		}
	}
	private Table inputTable;

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		return DataTransformationServiceCalculateMetrics.TRANSFORMATION_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getLabelText()
	 */
	@Override
	public String getLabelText() {
//		return "Metrics";
		return "";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#verifyParameters()
	 */
	@Override
	public boolean verifyParameters() {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation(de.enflexit.df.core.processing.transformation.graph.TransformationGraphController)
	 */
	@Override
	public Table performTransformation(TransformationGraphController graphController) {
		List<Metrics> metricsToCalculate = this.getSelectedMetricsList();
		List<String> compareColumnNames = this.getCompareColumnsList();
		
		List<Column<?>> resultTableColumns = new ArrayList<Column<?>>(); 
		
		StringColumn columnNames = StringColumn.create("Column");
		columnNames.addAll(compareColumnNames);
		resultTableColumns.add(columnNames);
		
		Column<?> referenceColumn = this.getInputTable().column(this.getTransformationParameters().getStringValue(PARAM_NAME_REFERENCE_COLUMN));
		
		if (referenceColumn instanceof NumericColumn<?> referenceColumnNumeric) {
			for(Metrics metric : metricsToCalculate) {
				DoubleColumn metricColumn = DoubleColumn.create(metric.label());
				for(String compareColumnName : compareColumnNames) {
					Column<?> compareColumn = this.getInputTable().column(compareColumnName);
					if (compareColumn instanceof NumericColumn<?> compareColumnNumeric) {
						
						MetricsCalculation metricsCalculation = this.getMetricsCalculation(metric);
						Double metricValue = metricsCalculation.calculateMetric(referenceColumnNumeric, compareColumnNumeric);
						metricColumn.append(metricValue);
					} else {
						throw new IllegalArgumentException("Compare column " + compareColumn.name() + " is not numeric!");
					}
				}
				resultTableColumns.add(metricColumn);
			}
		} else {
			throw new IllegalArgumentException("Reference column " + referenceColumn.name() + " is not numeric!");
		}
		
		Table resultTable = Table.create(this.getOutputTableName(), resultTableColumns);
		return resultTable;
	}
	
	/**
	 * Gets the metrics calculation.
	 * @param metric the metric
	 * @return the metrics calculation
	 */
	private MetricsCalculation getMetricsCalculation(Metrics metric) {
		switch (metric) {
		case R_SQUARED: 
			return new MetricsCalculationRSquared();
		case RMSE:
			return new MetricsCalculationRMSE();
		case MAE:
			return new MetricsCalculationMAE();
		case MSE:
			return new MetricsCalculationMSE();
		default:
			throw new IllegalArgumentException("Unexpected metric: " + metric);
		}
	}
	
	/**
	 * Gets the list of selected metrics from the parameters.
	 * @return the selected metrics list
	 */
	private List<Metrics> getSelectedMetricsList(){
		String selectedMetricsListString = this.getTransformationParameters().getStringValue(PARAM_NAME_SELECTED_METRICS);
		return MetricsCalculationParametersHelper.parseMetricsListString(selectedMetricsListString);
	}
	
	/**
	 * Gets the list of compare columns from the parameters.
	 * @return the compare columns list
	 */
	private List<String> getCompareColumnsList(){
		String compareColumnsListString = this.getTransformationParameters().getStringValue(PARAM_NAME_COMPARE_COLUMNS);
		return MetricsCalculationParametersHelper.parseCompareColumnsString(compareColumnsListString);
	}
	
	/**
	 * Gets the input table.
	 * @return the input table
	 */
	private Table getInputTable() {
		if (inputTable==null) {
			inputTable = this.getInputNodes().get(0).getDataTable();
		}
		return inputTable;
	}

}
