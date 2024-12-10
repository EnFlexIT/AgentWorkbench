package de.enflexit.awb.simulation.load.threading.storage;


/**
 * Protocol class for storing Thread-Load-Information of a single Agent
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageAgent extends ThreadInfoStorageXYSeries {
		
	private double predictiveMetric;
	private double realMetric;
	
	private String className;
	private boolean isAgent;
	
	/**
	 * Instantiates a new thread info storage agent.
	 * @param name the name
	 * @param className the class name
	 * @param isAgent the is agent
	 */
	public ThreadInfoStorageAgent(String name, String className, boolean isAgent) {
		super(name);
		this.className = className;
		this.isAgent = isAgent;
	}
	
	/**
	 * @return the predictiveMetric
	 */
	public double getPredictiveMetric() {
		return predictiveMetric;
	}
	/**
	 * @param predictiveMetric the predictiveMetric to set
	 */
	public void setPredictiveMetric(double predictiveMetric) {
		this.predictiveMetric = predictiveMetric;
	}
	/**
	 * @return the realMetric
	 */
	public double getRealMetric() {
		return realMetric;
	}
	/**
	 * @param realMetric the realMetric to set
	 */
	public void setRealMetric(double realMetric) {
		this.realMetric = realMetric;
	}
	
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @return the isAgent
	 */
	public boolean isAgent() {
		return isAgent;
	}
	/**
	 * @param isAgent the isAgent to set
	 */
	public void setAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}
}
