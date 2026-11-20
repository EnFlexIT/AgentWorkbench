package de.enflexit.df.core.processing.transformation.metrics;

import java.util.ArrayList;

/**
 * THis class manages the available metrics calculations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class MetricsCalculationRegistry {
	
	private ArrayList<MetricsCalculation> metricsList;
	
	private static MetricsCalculationRegistry instance;
	
	/**
	 * Instantiates a new metrics calculation registry. Private singleton constructor.
	 */
	private MetricsCalculationRegistry() {
		this.getMetricsList().add(new MetricsCalculationRSquared());
		this.getMetricsList().add(new MetricsCalculationRMSE());
		this.getMetricsList().add(new MetricsCalculationMAE());
		this.getMetricsList().add(new MetricsCalculationMSE());
	}
	
	/**
	 * Gets the single instance of MetricsCalculationRegistry.
	 * @return single instance of MetricsCalculationRegistry
	 */
	public static MetricsCalculationRegistry getInstance() {
		if (instance==null) {
			instance = new MetricsCalculationRegistry();
		}
		return instance;
	}
	
	/**
	 * Gets the list of available metrics.
	 * @return the metrics list
	 */
	public ArrayList<MetricsCalculation> getMetricsList() {
		if (metricsList==null) {
			metricsList = new ArrayList<MetricsCalculation>();
		}
		return metricsList;
	}
	
	/**
	 * Gets the metrics calculation with the specified identifier.
	 * @param identifier the identifier
	 * @return the metrics calculation, null if not found
	 */
	public MetricsCalculation getMetricsCalculationByIdentifier(String identifier) {
		for (MetricsCalculation metCalc : this.getMetricsList()) {
			if (metCalc.getMetricsIdentifier().equals(identifier)) {
				return metCalc;
			}
		}
		return null;
	}
	
}
