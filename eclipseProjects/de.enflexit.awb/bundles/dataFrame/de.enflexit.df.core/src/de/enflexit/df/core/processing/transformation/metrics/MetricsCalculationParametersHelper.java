package de.enflexit.df.core.processing.transformation.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides static methods to support parameter handling for Metrics calculation.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class MetricsCalculationParametersHelper {
	
	private static final String SEPARATOR = ";";
	
	/**
	 * Builds a single string for a list of selected column names.
	 * @param selectedColumnsList the list of selected column names
	 * @return the string
	 */
	public static String buildColumnsString(List<String> selectedColumnsList) {
		return selectedColumnsList.stream().collect(Collectors.joining(SEPARATOR));
	}
	/**
	 * Parses a string representation of a selected column names list.
	 * @param compareColumnsListString the compare columns list string
	 * @return the list
	 */
	public static List<String> parseCompareColumnsString(String compareColumnsListString) {
		return Arrays.asList(compareColumnsListString.split(SEPARATOR));
	}

	/**
	 * Builds a single string for a list of selected metrics.
	 * @param selectedMetricsList the selected metrics list
	 * @return the string
	 */
	public static String buildMetricsListString(List<MetricsCalculation> selectedMetricsList) {
		ArrayList<String> metricsStringList = new ArrayList<String>();
		for (MetricsCalculation metric : selectedMetricsList) {
			metricsStringList.add(metric.getMetricsIdentifier());
		}
		return metricsStringList.stream().collect(Collectors.joining(SEPARATOR));
	}
	/**
	 * Parses a string representation of a selected metrics list.
	 * @param selectedMetricsListString the selected metrics list string
	 * @return the list
	 */
	public static List<MetricsCalculation> parseMetricsListString(String selectedMetricsListString) {
		ArrayList<MetricsCalculation> metricsList = new ArrayList<MetricsCalculation>();
		String[] metricsArray = selectedMetricsListString.split(SEPARATOR);
		for (int i=0; i<metricsArray.length; i++) {
			MetricsCalculation metric = MetricsCalculationRegistry.getInstance().getMetricsCalculationByIdentifier(metricsArray[i]);
			if (metric!=null) {
				metricsList.add(metric);
			}
		}
		return metricsList;
	}
	

}
