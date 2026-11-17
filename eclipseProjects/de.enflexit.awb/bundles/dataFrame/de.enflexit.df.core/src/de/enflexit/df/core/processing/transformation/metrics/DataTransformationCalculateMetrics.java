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
		
		if (referenceColumn instanceof NumericColumn<?> ncRefCol) {
			for(Metrics metric : metricsToCalculate) {
				DoubleColumn metricColumn = DoubleColumn.create(metric.label());
				for(String compareColumnName : compareColumnNames) {
					Column<?> compareColumn = this.getInputTable().column(compareColumnName);
					if (compareColumn instanceof NumericColumn<?> ncCompCOl) {
						Double metricValue = this.calculateMetric(ncRefCol, ncCompCOl, metric);
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
	 * Calculates the specified metric on the provided columns.
	 * @param referenceColumn the reference column
	 * @param compareColumn the compare column
	 * @param metric the metric
	 * @return the double
	 */
	private Double calculateMetric(NumericColumn<?> referenceColumn, NumericColumn<?> compareColumn, Metrics metric) {
		
		// --- Determine the number of valid compare pairs and the reference sum --------
		int n = 0;
		double sumReference = 0.0;
		
		for (int i = 0; i < referenceColumn.size(); i++) {
			if (referenceColumn.isMissing(i) || compareColumn.isMissing(i)) {
				continue;
			}

			double reference = referenceColumn.getDouble(i);

			sumReference += reference;
			n++;
		}
		
		double meanReference = sumReference / n;

		// --- Calculate some error values required later -----------
	    double sumSquaredError = 0.0;
	    double sumAbsoluteError = 0.0;
	    double sumSquaredTotal = 0.0;
	    
		for (int i = 0; i < referenceColumn.size(); i++) {

			if (referenceColumn.isMissing(i) || compareColumn.isMissing(i)) {
				continue;
			}

			double reference = referenceColumn.getDouble(i);
			double compare = compareColumn.getDouble(i);
			double error = reference - compare;

			sumSquaredError += error * error;
			sumAbsoluteError += Math.abs(error);
			sumSquaredTotal += Math.pow(reference - meanReference, 2);
		}

		// --- Calculate the actually required metric ---------------
		Double result;
		switch (metric) {

        case R_SQUARED:
            result = sumSquaredTotal > 0.0
                ? 1.0 - sumSquaredError / sumSquaredTotal
                : Double.NaN;
            break;

        case RMSE:
            result = Math.sqrt(sumSquaredError / n);
            break;

        case MAE:
            result = sumAbsoluteError / n;
            break;

        case MSE:
            result = sumSquaredError / n;
            break;

        default:
            throw new IllegalArgumentException(
                "Unsupported metric: " + metric
            );
	    }
	
	    return result;
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
