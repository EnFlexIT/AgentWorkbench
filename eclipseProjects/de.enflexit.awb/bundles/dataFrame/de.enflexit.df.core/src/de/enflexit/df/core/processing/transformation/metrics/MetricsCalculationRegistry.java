package de.enflexit.df.core.processing.transformation.metrics;

import java.util.ArrayList;

public class MetricsCalculationRegistry {
	private ArrayList<MetricsCalculation> metricsList;

	public MetricsCalculationRegistry() {
		this.getMetricsList().add(new MetricsCalculationRSquared());
		this.getMetricsList().add(new MetricsCalculationRMSE());
		this.getMetricsList().add(new MetricsCalculationMAE());
		this.getMetricsList().add(new MetricsCalculationMSE());
	}
	
	public ArrayList<MetricsCalculation> getMetricsList() {
		if (metricsList==null) {
			metricsList = new ArrayList<MetricsCalculation>();
		}
		return metricsList;
	}
	
}
