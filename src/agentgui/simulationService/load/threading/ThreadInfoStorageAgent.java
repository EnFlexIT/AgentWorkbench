package agentgui.simulationService.load.threading;

import org.jfree.data.xy.XYSeries;


/**
 * Protocol class for storing Thread-Load-Information of a single Agent
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageAgent extends ThreadInfoStorageSeries {
	
	private double predictMetricCPU;
	private double realMetricCPU;
	protected String className;
	private boolean isAgent;
	
	public final String TOTAL_CPU_USER_TIME   = "TOTAL_CPU_USER_TIME";
	public final String DELTA_CPU_USER_TIME   = "DELTA_CPU_USER_TIME";
	public final String TOTAL_CPU_SYSTEM_TIME = "TOTAL_CPU_SYSTEM_TIME";
	public final String DELTA_CPU_SYSTEM_TIME = "DELTA_CPU_SYSTEM_TIME";
//	public enum seriesAvailable {
//		TOTAL_CPU_USER_TIME, 
//		DELTA_CPU_USER_TIME, 
//		TOTAL_CPU_SYSTEM_TIME, 
//		DELTA_CPU_SYSTEM_TIME
//		};
	
	public ThreadInfoStorageAgent(String name, String className, boolean isAgent) {
		super(name);
		this.className = className;
		this.setAgent(isAgent);
//		for (seriesAvailable ser : seriesAvailable.values()) {
//			setXYSeries(ser.toString(),new XYSeries(name));
//		}
		setXYSeries(TOTAL_CPU_USER_TIME,new XYSeries(TOTAL_CPU_USER_TIME+DELIMITER+name));
		setXYSeries(DELTA_CPU_USER_TIME,new XYSeries(DELTA_CPU_USER_TIME+DELIMITER+name));
		setXYSeries(TOTAL_CPU_SYSTEM_TIME,new XYSeries(TOTAL_CPU_SYSTEM_TIME+DELIMITER+name));
		setXYSeries(DELTA_CPU_SYSTEM_TIME,new XYSeries(DELTA_CPU_SYSTEM_TIME+DELIMITER+name));
	}
	/**
	 * @return the predictMetricCPU
	 */
	public double getPredictMetricCPU() {
		return this.predictMetricCPU;
	}
	/**
	 * @param predictMetricCPU the predictMetricCPU to set
	 */
	public void setPredictMetricCPU(double predictMetricCPU) {
		this.predictMetricCPU = predictMetricCPU;
	}
	/**
	 * @return the realMetricCPU
	 */
	public double getRealMetricCPU() {
		return realMetricCPU;
	}
	/**
	 * @param realMetricCPU the realMetricCPU to set
	 */
	public void setRealMetricCPU(double realMetricCPU) {
		this.realMetricCPU = realMetricCPU;
	}
	
	/**
	 * @return the className
	 */
	public String getClassName() {
		return this.className;
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
