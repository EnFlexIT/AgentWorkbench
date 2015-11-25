/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.simulationService.load.threading;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jfree.data.xy.XYSeries;

/**
 * The Class ThreadMeasureMetrics.
 * Calculate the "real" metric for agents
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadMeasureMetrics {
	

	/** The factor max machine load. */
	private final double FACTOR_MAX_MACHINE_LOAD 	= 0.95d;
	
	/** The simulation duration minimum in milliseconds, default 5min = 300000 */
	public final double SIMULATION_DURATION_MIN 	= 60000;
	
	/**  Calculation is based on integral of delta system times. */
	public final String CALC_TYPE_INTEGRAL_DELTA  		= "CALC_TYPE_INTEGRAL_DELTA";
	
	/**  Calculation is based on average system times. */
	public final String CALC_TYPE_INTEGRAL_TOTAL   			= "CALC_TYPE_INTEGRAL_TOTAL";
	
	/**  Calculation is based on the last value for CPU system times. */
	public final String CALC_TYPE_LAST_TOTAL   		= "CALC_TYPE_LAST_TOTAL";
	
	/** The metric calculation based on individual data. */
	public final String METRIC_BASE_INDIVIDUAL 		= "METRIC_BASE_INDIVIDUAL";
	
	/** The metric calculation based on class data. */
	public final String METRIC_BASE_CLASS			= "METRIC_BASE_CLASS";
	
	/** The calculation type. */
	private String calcType;
	
	/** The base for calculation of metric. */
	private String metricBase;

	/** The thread info storage. */
	private ThreadInfoStorage threadInfoStorage;
	
	/** The simulation duration. */
	private double simulationDuration;
	
	/** The map that holds the values (average, integral, last total), depending on calcType */
	private HashMap<String, Double> calcTypeValueMap;
	
	/**
	 * Instantiates a new thread measure metrics.
	 *
	 * @param threadInfoStorage the thread info storage
	 * @param calcType the calculation type
	 * @param metricBase the metric base
	 */
	public ThreadMeasureMetrics(ThreadInfoStorage threadInfoStorage, String calcType, String metricBase){
		this.threadInfoStorage = threadInfoStorage;
		this.calcType = calcType;
		this.metricBase = metricBase;
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize(){		
		calcTypeValueMap = new HashMap<String, Double>();
	}
	
	/**
	 * Calculates the metrics for all agents.
	 */
	public void calculateMetrics(){
		
		if(isDataUsable() == true){
						
			Iterator<Entry<String, ThreadInfoStorageMachine>> iteratorMachine = threadInfoStorage.getMapMachine().entrySet().iterator();
			while (iteratorMachine.hasNext()){
				
				XYSeries series;
				ThreadInfoStorageMachine actualMachine = iteratorMachine.next().getValue();
				
				
				if (getCalcType().equals(CALC_TYPE_INTEGRAL_DELTA)) {
					series = actualMachine.getXYSeriesMap().get(threadInfoStorage.DELTA_CPU_SYSTEM_TIME);
					calcTypeValueMap.put(actualMachine.getName(), getIntegralForTimeSeries(series, 0, series.getItemCount()-1));				
				} else if (getCalcType().equals(CALC_TYPE_INTEGRAL_TOTAL)) {
					series = actualMachine.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
					calcTypeValueMap.put(actualMachine.getName(), getIntegralForTimeSeries(series, 0, series.getItemCount()-1));
				} else if (getCalcType().equals(CALC_TYPE_LAST_TOTAL)) {
					series = actualMachine.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
					calcTypeValueMap.put(actualMachine.getName(), series.getMaxY());
				}
				
				
				Iterator<Entry<String, ThreadInfoStorageAgent>> iteratorAgent = threadInfoStorage.getMapAgent().entrySet().iterator();
				while (iteratorAgent.hasNext()){
					
					ThreadInfoStorageAgent actualAgent = iteratorAgent.next().getValue();
					if (//actualAgent.isAgent() == true && 
							actualAgent.getName().contains(actualMachine.getName())) {

						actualAgent.setRealMetricCPU(getMetricForAgent(
								actualAgent, actualMachine));
					}
				}
			}
		}
	}
	

	/**
	 * Gets the metric for agent.
	 *
	 * @param agent the agent
	 * @param machine the machine
	 * @return the metric for agent
	 */
	private double getMetricForAgent(ThreadInfoStorageAgent agent, ThreadInfoStorageMachine machine){
		XYSeries series = new XYSeries("");
		double actualValue = 0.0d;
		
		if (getMetricBase().equals(METRIC_BASE_INDIVIDUAL)) {
			// --- calculate Integral based on data of individual agent ---
			
			if (getCalcType().equals(CALC_TYPE_INTEGRAL_DELTA)) {
				series = agent.getXYSeriesMap().get(threadInfoStorage.DELTA_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, 0, series.getItemCount()-1);				
			} else if (getCalcType().equals(CALC_TYPE_INTEGRAL_TOTAL)) {
				series = agent.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, 0, series.getItemCount()-1);
			} else if (getCalcType().equals(CALC_TYPE_LAST_TOTAL)) {
				series = agent.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
				actualValue = series.getMaxY();
			}
			
		} else if (getMetricBase().equals(METRIC_BASE_CLASS)) {
			// --- calculate Integral based on data of the agent's class (average) ---
			
			if (getCalcType().equals(CALC_TYPE_INTEGRAL_DELTA)) {
				series = threadInfoStorage.getMapAgentClass().get(agent.getClassName()).getXYSeriesMap().get(threadInfoStorage.AVG_DELTA_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, 0, series.getItemCount()-1);				
			} else if (getCalcType().equals(CALC_TYPE_INTEGRAL_TOTAL)) {
				series = threadInfoStorage.getMapAgentClass().get(agent.getClassName()).getXYSeriesMap().get(threadInfoStorage.AVG_TOTAL_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, 0, series.getItemCount()-1);				
			} else if (getCalcType().equals(CALC_TYPE_LAST_TOTAL)) {
				series = threadInfoStorage.getMapAgentClass().get(agent.getClassName()).getXYSeriesMap().get(threadInfoStorage.AVG_TOTAL_CPU_SYSTEM_TIME);
				actualValue = series.getMaxY();
			}
		}
		//--- rule of three ---
		return (getMFLOPConsumptionReal(machine) * actualValue)/calcTypeValueMap.get(machine.getName());
	}
	
	/**
	 * Checks if data/measurement is usable for determining metrics.
	 *
	 * @return true, if usable
	 */
	public boolean isDataUsable(){
		
		if (getSimulationDuration() >= SIMULATION_DURATION_MIN){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets the real CPU consumption.
	 * (calculation of area)
	 *
	 * @param machine the machine
	 * @return the CPU consumption real
	 */
	private double getCPUConsumptionReal(ThreadInfoStorageMachine machine){
		
		XYSeries series = machine.getXYSeriesMap().get(threadInfoStorage.LOAD_CPU);
		return getIntegralForTimeSeries(series, 0, series.getItemCount()-1);
	}
	
	/**
	 * Gets the theoretical max. CPU consumption.
	 * (calculation of area)
	 * 
	 * @return the CPU consumption max
	 */
	private double getCPUConsumptionMax(){
		
		return 100 * FACTOR_MAX_MACHINE_LOAD * simulationDuration;
	}
	
	/**
	 * Gets the calculated real MFLOP.
	 *
	 * @param machine the machine
	 * @return the MFLOP consumption real
	 */
	private double getMFLOPConsumptionReal(ThreadInfoStorageMachine machine){
		//--- rule of three ---
		return (getMFLOPMax(machine) * getCPUConsumptionReal(machine))/getCPUConsumptionMax();
	}
	
	/**
	 * Gets the MFLOP max.
	 *
	 * @param machine the machine
	 * @return the MFLOP
	 */
	private double getMFLOPMax(ThreadInfoStorageMachine machine){
		
		return FACTOR_MAX_MACHINE_LOAD * machine.getMflops();
	}

	/**
	 * Gets the integral for time series.
	 *
	 * @param series the series
	 * @param start the start
	 * @param end the end
	 * @return the integral for time series
	 */
	private double getIntegralForTimeSeries(XYSeries series, int start, int end){
		double integral = 0.0d;
		
		if(end > series.getItemCount()-1){
			end = series.getItemCount()-1;
		}
		if(start > end){
			start = end;
		}
		
		//--- simple calculation of area ---
		for(int x = start; x < end; x++){
			//--- difference of X
			double xDiff = series.getX(x+1).doubleValue() - series.getX(x).doubleValue();
			// --- Y value for first and second measurement
			double yValue1 = series.getY(x).doubleValue();
			double yValue2 = series.getY(x+1).doubleValue();
			
			integral = integral + (xDiff * yValue2);
			
			// --- Subtract or add area of triangle
			double triangle = (xDiff * (Math.abs(yValue2 - yValue1)))/2;
			
			if(yValue2 > yValue1){
				integral = integral - triangle;
				
			}else if (yValue2 < yValue1){
				integral = integral + triangle;
			}
		}
		
		return integral;
	}
	
	/**
	 * Gets the calculation type.
	 *
	 * @return the calc type
	 */
	public String getCalcType() {
		if (calcType.equals(CALC_TYPE_INTEGRAL_DELTA) == false
				&& calcType.equals(CALC_TYPE_INTEGRAL_TOTAL) == false
				&& calcType.equals(CALC_TYPE_LAST_TOTAL) == false) {
			calcType = CALC_TYPE_LAST_TOTAL;
		}
		return calcType;
	}

	/**
	 * Sets the calculation type.
	 *
	 * @param calcType the new calc type
	 */
	public void setCalcType(String calcType) {
		this.calcType = calcType;
	}
	
	/**
	 * Gets the method on which the calculation of metric is based.
	 *
	 * @return the metric base
	 */
	public String getMetricBase() {
		if (metricBase.equals(METRIC_BASE_CLASS) == false
				&& metricBase.equals(METRIC_BASE_INDIVIDUAL) == false) {
			metricBase = METRIC_BASE_INDIVIDUAL;
		}
		return metricBase;
	}

	/**
	 * Sets the the method on which the calculation of metric is based.
	 *
	 * @param metricBase the new metric base
	 */
	public void setMetricBase(String metricBase) {
		this.metricBase = metricBase;
	}

	/**
	 * Gets the simulation duration.
	 *
	 * @return the simulationDuration
	 */
	public double getSimulationDuration() {
		if(threadInfoStorage != null){
			Number start = threadInfoStorage.getMapAgentClass().get(ThreadProperties.NON_AGENTS_CLASSNAME).getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME).getMinX();
			Number end   = threadInfoStorage.getMapAgentClass().get(ThreadProperties.NON_AGENTS_CLASSNAME).getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME).getMaxX();
			simulationDuration = end.doubleValue() - start.doubleValue();
		}
		return simulationDuration;
	}	
}
