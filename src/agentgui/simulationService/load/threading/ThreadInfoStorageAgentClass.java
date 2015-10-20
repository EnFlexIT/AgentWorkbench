package agentgui.simulationService.load.threading;

import org.jfree.data.xy.XYSeries;


/**
 * Protocol class for storing Thread-Load-Information of an Agent-Class
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageAgentClass extends ThreadInfoStorageSeries{
	private double AvgPredictiveMetric;
	private double AvgrealMetric;
	private double MinPredictiveMetric;
	private double MinRealMetric;
	private double MaxPredictiveMetric;
	private double MaxRealMetric;
	
	public final String AVG_TOTAL_CPU_USER_TIME   = "AVG_TOTAL_CPU_USER_TIME";
	public final String AVG_TOTAL_CPU_SYSTEM_TIME = "AVG_TOTAL_CPU_SYSTEM_TIME";
	public final String AVG_DELTA_CPU_USER_TIME   = "AVG_DELTA_CPU_USER_TIME";
	public final String AVG_DELTA_CPU_SYSTEM_TIME = "AVG_DELTA_CPU_SYSTEM_TIME";
	public final String MAX_TOTAL_CPU_USER_TIME   = "MAX_TOTAL_CPU_USER_TIME";
	public final String MAX_TOTAL_CPU_SYSTEM_TIME = "MAX_TOTAL_CPU_SYSTEM_TIME";
	public final String MAX_DELTA_CPU_USER_TIME   = "MAX_DELTA_CPU_USER_TIME";
	public final String MAX_DELTA_CPU_SYSTEM_TIME = "MAX_DELTA_CPU_SYSTEM_TIME";
	
	public ThreadInfoStorageAgentClass(String name) {
		super(name);
		setXYSeries(AVG_TOTAL_CPU_USER_TIME,new XYSeries(AVG_TOTAL_CPU_USER_TIME+DELIMITER+name));
		setXYSeries(AVG_DELTA_CPU_USER_TIME,new XYSeries(AVG_DELTA_CPU_USER_TIME+DELIMITER+name));
		setXYSeries(AVG_TOTAL_CPU_SYSTEM_TIME,new XYSeries(AVG_TOTAL_CPU_SYSTEM_TIME+DELIMITER+name));
		setXYSeries(AVG_DELTA_CPU_SYSTEM_TIME,new XYSeries(AVG_DELTA_CPU_SYSTEM_TIME+DELIMITER+name));
		setXYSeries(MAX_TOTAL_CPU_USER_TIME,new XYSeries(MAX_TOTAL_CPU_USER_TIME+DELIMITER+name));
		setXYSeries(MAX_DELTA_CPU_USER_TIME,new XYSeries(MAX_DELTA_CPU_USER_TIME+DELIMITER+name));
		setXYSeries(MAX_TOTAL_CPU_SYSTEM_TIME,new XYSeries(MAX_TOTAL_CPU_SYSTEM_TIME+DELIMITER+name));
		setXYSeries(MAX_DELTA_CPU_SYSTEM_TIME,new XYSeries(MAX_DELTA_CPU_SYSTEM_TIME+DELIMITER+name));
		
		this.AvgPredictiveMetric 	= 0;
		this.AvgrealMetric 			= 0;
		this.MinPredictiveMetric 	= 0;
		this.MinRealMetric 			= 0;
		this.MaxPredictiveMetric 	= 0;
		this.MaxRealMetric 			= 0;
		
	}
	/**
	 * @return the avgPredictiveMetric
	 */
	public double getAvgPredictiveMetric() {
		return AvgPredictiveMetric;
	}
	/**
	 * @param avgPredictiveMetric the avgPredictiveMetric to set
	 */
	public void setAvgPredictiveMetric(double avgPredictiveMetric) {
		AvgPredictiveMetric = avgPredictiveMetric;
	}
	/**
	 * @return the avgPrealMetric
	 */
	public double getAvgRealMetric() {
		return AvgrealMetric;
	}
	/**
	 * @param avgRealMetric the avgRealMetric to set
	 */
	public void setAvgRealMetric(double avgPrealMetric) {
		AvgrealMetric = avgPrealMetric;
	}
	/**
	 * @return the minPredictiveMetric
	 */
	public double getMinPredictiveMetric() {
		return MinPredictiveMetric;
	}
	/**
	 * @param minPredictiveMetric the minPredictiveMetric to set
	 */
	public void setMinPredictiveMetric(double minPredictiveMetric) {
		MinPredictiveMetric = minPredictiveMetric;
	}
	/**
	 * @return the minrealMetric
	 */
	public double getMinRealMetric() {
		return MinRealMetric;
	}
	/**
	 * @param minrealMetric the minrealMetric to set
	 */
	public void setMinRealMetric(double minrealMetric) {
		MinRealMetric = minrealMetric;
	}
	/**
	 * @return the maxPredictiveMetric
	 */
	public double getMaxPredictiveMetric() {
		return MaxPredictiveMetric;
	}
	/**
	 * @param maxPredictiveMetric the maxPredictiveMetric to set
	 */
	public void setMaxPredictiveMetric(double maxPredictiveMetric) {
		MaxPredictiveMetric = maxPredictiveMetric;
	}
	/**
	 * @return the maxRealMetric
	 */
	public double getMaxRealMetric() {
		return MaxRealMetric;
	}
	/**
	 * @param maxRealMetric the maxRealMetric to set
	 */
	public void setMaxRealMetric(double maxRealMetric) {
		MaxRealMetric = maxRealMetric;
	}
}
