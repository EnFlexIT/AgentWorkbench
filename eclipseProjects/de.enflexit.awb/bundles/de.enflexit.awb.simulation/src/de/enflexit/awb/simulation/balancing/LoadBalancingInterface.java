package de.enflexit.awb.simulation.balancing;

import jade.core.Agent;

/**
 * This interface is to extend an agent with methods needed for load-balancing, 
 *  
 * @author Hanno Monschan - University of Duisburg - Essen
 * @see Agent
 */
public interface LoadBalancingInterface {
	
	/**
	 * Gets the predict metric cpu.
	 * @return the predict metric cpu
	 */
	public double getPredictiveMetricCPU();
	
	/**
	 * Sets the predictive metric cpu.
	 * @param predictiveMetricCPU the new predictive metric cpu
	 */
	public void setPredictiveMetricCPU(double predictiveMetricCPU) ;
	
	/**
	 * Gets the real metric cpu.
	 * @return the real metric cpu
	 */
	public double getRealMetricCPU() ;
	
	/**
	 * Sets the real metric cpu.
	 * @param realMetricCPU the new real metric cpu
	 */
	public void setRealMetricCPU(double realMetricCPU) ;

}
