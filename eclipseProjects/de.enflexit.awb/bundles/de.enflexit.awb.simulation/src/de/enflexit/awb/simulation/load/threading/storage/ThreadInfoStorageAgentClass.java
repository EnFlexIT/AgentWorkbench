package de.enflexit.awb.simulation.load.threading.storage;

/**
 * Protocol class for storing Thread-Load-Information of an Agent-Class
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageAgentClass extends ThreadInfoStorageXYSeries{
	
	/** The average predictive metric. */
	private double avgPredictiveMetric;
	
	/** The average real metric. */
	private double avgRealMetric;
	
	/** The minimum predictive metric. */
	private double minPredictiveMetric;
	
	/** The minimum real metric. */
	private double minRealMetric;
	
	/** The max predictive metric. */
	private double maxPredictiveMetric;
	
	/** The max real metric. */
	private double maxRealMetric;
	
	/**
	 * Instantiates a new thread info storage agent class.
	 * @param name the name
	 */
	public ThreadInfoStorageAgentClass(String name) {
		super(name);
		
		this.avgPredictiveMetric 	= 0;
		this.avgRealMetric 			= 0;
		this.minPredictiveMetric 	= 0;
		this.minRealMetric 			= 0;
		this.maxPredictiveMetric 	= 0;
		this.maxRealMetric 			= 0;
	}
	
	/**
	 * Gets the avg predictive metric.
	 *
	 * @return the avgPredictiveMetric
	 */
	public double getAvgPredictiveMetric() {
		return avgPredictiveMetric;
	}
	
	/**
	 * Sets the average predictive metric.
	 *
	 * @param avgPredictiveMetric the avgPredictiveMetric to set
	 */
	public void setAvgPredictiveMetric(double avgPredictiveMetric) {
		this.avgPredictiveMetric = avgPredictiveMetric;
	}
	
	/**
	 * Gets the average real metric.
	 *
	 * @return the avgRealMetric
	 */
	public double getAvgRealMetric() {
		return avgRealMetric;
	}
	
	/**
	 * Sets the average real metric.
	 *
	 * @param avgPrealMetric the new average real metric
	 */
	public void setAvgRealMetric(double avgPrealMetric) {
		this.avgRealMetric = avgPrealMetric;
	}
	
	/**
	 * Gets the minimum predictive metric.
	 *
	 * @return the minPredictiveMetric
	 */
	public double getMinPredictiveMetric() {
		return minPredictiveMetric;
	}
	
	/**
	 * Sets the minimum predictive metric.
	 *
	 * @param minPredictiveMetric the minPredictiveMetric to set
	 */
	public void setMinPredictiveMetric(double minPredictiveMetric) {
		this.minPredictiveMetric = minPredictiveMetric;
	}
	
	/**
	 * Gets the minimum real metric.
	 *
	 * @return the minrealMetric
	 */
	public double getMinRealMetric() {
		return minRealMetric;
	}
	
	/**
	 * Sets the minimum real metric.
	 *
	 * @param minrealMetric the minrealMetric to set
	 */
	public void setMinRealMetric(double minrealMetric) {
		this.minRealMetric = minrealMetric;
	}
	
	/**
	 * Gets the max predictive metric.
	 *
	 * @return the maxPredictiveMetric
	 */
	public double getMaxPredictiveMetric() {
		return maxPredictiveMetric;
	}
	
	/**
	 * Sets the max predictive metric.
	 *
	 * @param maxPredictiveMetric the maxPredictiveMetric to set
	 */
	public void setMaxPredictiveMetric(double maxPredictiveMetric) {
		this.maxPredictiveMetric = maxPredictiveMetric;
	}
	
	/**
	 * Gets the max real metric.
	 *
	 * @return the maxRealMetric
	 */
	public double getMaxRealMetric() {
		return maxRealMetric;
	}
	
	/**
	 * Sets the max real metric.
	 *
	 * @param maxRealMetric the maxRealMetric to set
	 */
	public void setMaxRealMetric(double maxRealMetric) {
		this.maxRealMetric = maxRealMetric;
	}
}
