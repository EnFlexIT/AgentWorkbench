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

import agentgui.core.application.Application;
import agentgui.core.project.AgentClassLoadMetricsTable;
import agentgui.core.project.AgentClassMetricDescription;
import agentgui.core.project.Project;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorage;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageAgent;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageMachine;

/**
 * The Class ThreadCalculateMetrics.
 * 
 * Calculates the "real" metrics for all (threadable) agents.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadCalculateMetrics {

	/** The factor max machine load. */
	public final static double FACTOR_MAX_MACHINE_LOAD 	= 0.95d;
	
	/**  Calculation is based on integral of delta system times. */
	public final String CALC_TYPE_INTEGRAL_DELTA  		= "CALC_TYPE_INTEGRAL_DELTA";
	
	/**  Calculation is based on average system times. */
	public final String CALC_TYPE_INTEGRAL_TOTAL   		= "CALC_TYPE_INTEGRAL_TOTAL";
	
	/**  Calculation is based on the last value for CPU system times. */
	public final String CALC_TYPE_LAST_TOTAL   			= "CALC_TYPE_LAST_TOTAL";
	
	/** The metric calculation based on individual data. */
	public final String METRIC_BASE_INDIVIDUAL 			= "METRIC_BASE_INDIVIDUAL";
	
	/** The metric calculation based on class data. */
	public final String METRIC_BASE_CLASS				= "METRIC_BASE_CLASS";
	
	private String calcType;
	private String metricBase;
	private ThreadInfoStorage threadInfoStorage;
	private double samplingInterval;
	private int samplingIntervalOffsetStart, samplingIntervalOffsetEnd;
	
	/**  The map that holds the values (average, integral, last total), depending on calcType. */
	private HashMap<String, Double> calcTypeValueMap;
	
	/** The current project. */
	private Project currProject;
	
	/**
	 * Instantiates a new thread measure metrics.
	 */
	public ThreadCalculateMetrics(){
		initialize();
	}
	
	/**
	 * Instantiates a new thread measure metrics.
	 *
	 * @param threadInfoStorage the thread info storage
	 * @param calcType the calculation type
	 * @param metricBase the metric base
	 */
	public ThreadCalculateMetrics(ThreadInfoStorage threadInfoStorage, String calcType, String metricBase){
		
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
		currProject = Application.getProjectFocused();
	}
	

	/**
	 * Calculates the metrics for all agents.
	 */
	public void getMetrics(){
							
		Iterator<Entry<String, ThreadInfoStorageMachine>> iteratorMachine = threadInfoStorage.getMapMachine().entrySet().iterator();
		while (iteratorMachine.hasNext()){			
			
			ThreadInfoStorageMachine actualMachine = iteratorMachine.next().getValue();
			XYSeries series = actualMachine.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
			
			samplingIntervalOffsetStart = 0; //(t0 start)
			samplingIntervalOffsetEnd = series.getItemCount()-1; //(t1 end)
			samplingInterval = getSamplingIntervalMilliSeconds(series, samplingIntervalOffsetStart, samplingIntervalOffsetEnd);
			
			if (getCalcType().equals(CALC_TYPE_INTEGRAL_DELTA)) {
				series = actualMachine.getXYSeriesMap().get(threadInfoStorage.DELTA_CPU_SYSTEM_TIME);
				calcTypeValueMap.put(actualMachine.getName(), getIntegralForTimeSeries(series, samplingIntervalOffsetStart, samplingIntervalOffsetEnd ));				
			} else if (getCalcType().equals(CALC_TYPE_INTEGRAL_TOTAL)) {
				series = actualMachine.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
				calcTypeValueMap.put(actualMachine.getName(), getIntegralForTimeSeries(series, samplingIntervalOffsetStart, samplingIntervalOffsetEnd));
			} else if (getCalcType().equals(CALC_TYPE_LAST_TOTAL)) {
				series = actualMachine.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
				calcTypeValueMap.put(actualMachine.getName(), series.getMaxY());
//				calcTypeValueMap.put(actualMachine.getName(), (series.getY(samplingIntervalOffsetEnd).doubleValue() - series.getY(samplingIntervalOffsetStart).doubleValue()));
			}
			
			
			Iterator<Entry<String, ThreadInfoStorageAgent>> iteratorAgent = threadInfoStorage.getMapAgent().entrySet().iterator();
			while (iteratorAgent.hasNext()){
				
				ThreadInfoStorageAgent actualAgent = iteratorAgent.next().getValue();
				if (actualAgent.getName().contains(actualMachine.getName())) {
					actualAgent.setRealMetric(getMetricForAgent(actualAgent, actualMachine, samplingIntervalOffsetStart, samplingIntervalOffsetEnd));
				}
			}
		}
		addOrUpdateAgentClassRealMetrics();
	}
	
	
	/**
	 * Adds or updates the agent class real metrics.
	 * 
	 * 
	 */
	private void addOrUpdateAgentClassRealMetrics(){
		
		HashMap<String, AgentClassMetricDescription> mapAgentClass = new HashMap<String, AgentClassMetricDescription>();
		
		Iterator<Entry<String, ThreadInfoStorageAgent>> iteratorAgent = threadInfoStorage.getMapAgent().entrySet().iterator();
		while (iteratorAgent.hasNext()){
			
			ThreadInfoStorageAgent actualAgent = iteratorAgent.next().getValue();
//			if (actualAgent.isAgent() == true) {//do not calculate for unknown agents

				String className = actualAgent.getClassName();
				double actualMetric = actualAgent.getRealMetric();
				
				AgentClassMetricDescription agentClass = mapAgentClass.get(className);
				int noOfAgents = threadInfoStorage.getNoOfAgentsPerClass().get(className);
				
				if (agentClass == null){
					agentClass = new AgentClassMetricDescription();
					agentClass.setRealMetricMin(actualMetric);
					agentClass.setRealMetricMax(actualMetric);
					agentClass.setRealMetricAverage(actualMetric/noOfAgents);
					agentClass.setClassName(className);
					mapAgentClass.put(className, agentClass);

				} else {
					
					if(agentClass.getRealMetricMin() > actualMetric){
						agentClass.setRealMetricMin(actualMetric);
					}
					if(agentClass.getRealMetricMax()< actualMetric){
						agentClass.setRealMetricMax(actualMetric);
					}
					
					agentClass.setRealMetricAverage(agentClass.getRealMetricAverage() + (actualMetric/noOfAgents));	
				}
//			}
		}
		
		// --- Update min, max and average real metrics for agent class in current project --------
		if (this.currProject!=null) {
			
			AgentClassLoadMetricsTable aclm = this.currProject.getAgentClassLoadMetrics();
			aclm.clearTableModel();
			
			Iterator<Entry<String, AgentClassMetricDescription>> iteratorAgentMetricsMap = mapAgentClass.entrySet().iterator();
			while (iteratorAgentMetricsMap.hasNext()){
				AgentClassMetricDescription actualAgentClass = iteratorAgentMetricsMap.next().getValue();
				String className = actualAgentClass.getClassName();
				double min = actualAgentClass.getRealMetricMin();
				double max = actualAgentClass.getRealMetricMax();
				double avg = actualAgentClass.getRealMetricAverage();
				
				int index = aclm.getIndexOfAgentClassMetricDescription(className);
				if(index == -1){
					aclm.addAgentLoadDescription(className,1 , 1, min, max, avg);
					aclm.addTableModelRow(new AgentClassMetricDescription(this.currProject,className,1 , 1,  min, max, avg));
				}else{
					aclm.getAgentClassMetricDescriptionVector().get(index).setRealMetricMin(min);
					aclm.getAgentClassMetricDescriptionVector().get(index).setRealMetricMax(max);
					aclm.getAgentClassMetricDescriptionVector().get(index).setRealMetricAverage(avg);
					
					aclm.addTableModelRow(aclm.getAgentClassMetricDescriptionVector().get(index));
				}
			}
			this.currProject.setAgentClassLoadMetrics(aclm);
		}
		
	}
	
	/**
	 * Gets the metric for a single agent.
	 *
	 * @param agent the agent
	 * @param machine the machine
	 * @param offsetFrom the sampling interval offset start value
	 * @param offsetTo the sampling interval offset end value
	 * @return the metric for agent
	 */
	private double getMetricForAgent(ThreadInfoStorageAgent agent,ThreadInfoStorageMachine machine, int offsetFrom, int offsetTo){
		XYSeries series = new XYSeries("");
		double actualValue = 0.0d;
		
		if (getMetricBase().equals(METRIC_BASE_INDIVIDUAL)) {
			// --- calculate Integral based on data of individual agent ---
			
			if (getCalcType().equals(CALC_TYPE_INTEGRAL_DELTA)) {
				series = agent.getXYSeriesMap().get(threadInfoStorage.DELTA_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, offsetFrom, offsetTo);				
			} else if (getCalcType().equals(CALC_TYPE_INTEGRAL_TOTAL)) {
				series = agent.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, offsetFrom, offsetTo);
			} else if (getCalcType().equals(CALC_TYPE_LAST_TOTAL)) {
				series = agent.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
				actualValue = series.getMaxY();
//				actualValue = series.getY(offsetTo).doubleValue() - series.getY(offsetFrom).doubleValue();
			}
			
		} else if (getMetricBase().equals(METRIC_BASE_CLASS)) {
			// --- calculate Integral based on data of the agent's class (average) ---
			
			if (getCalcType().equals(CALC_TYPE_INTEGRAL_DELTA)) {
				series = threadInfoStorage.getMapAgentClass().get(agent.getClassName()).getXYSeriesMap().get(threadInfoStorage.AVG_DELTA_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, offsetFrom, offsetTo);				
			} else if (getCalcType().equals(CALC_TYPE_INTEGRAL_TOTAL)) {
				series = threadInfoStorage.getMapAgentClass().get(agent.getClassName()).getXYSeriesMap().get(threadInfoStorage.AVG_TOTAL_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, offsetFrom, offsetTo);				
			} else if (getCalcType().equals(CALC_TYPE_LAST_TOTAL)) {
				series = threadInfoStorage.getMapAgentClass().get(agent.getClassName()).getXYSeriesMap().get(threadInfoStorage.AVG_TOTAL_CPU_SYSTEM_TIME);
				actualValue = series.getMaxY();
//				actualValue = series.getY(offsetTo).doubleValue() - series.getY(offsetFrom).doubleValue();
			}
		}
		
		//--- rule of three ---
		return (getMetricTotal(machine) * actualValue)/calcTypeValueMap.get(machine.getName());
	}
	
	/**
	 * Gets the total CPU utilization.
	 * (calculation of area)
	 *
	 * @param machine the machine
	 * @return the CPU total CPU utilization of machine
	 */
	private double getCPUTotal(ThreadInfoStorageMachine machine){
		XYSeries series = machine.getXYSeriesMap().get(threadInfoStorage.LOAD_CPU);
		return getIntegralForTimeSeries(series, samplingIntervalOffsetStart, samplingIntervalOffsetEnd);
	}
	
	/**
	 * Gets the theoretical max. CPU utilization within interval.
	 * (calculation of area)
	 *
	 * @param machine the machine
	 * @return the CPU max
	 */
	private double getCPUMax(ThreadInfoStorageMachine machine){
		return  (100 * FACTOR_MAX_MACHINE_LOAD * samplingInterval);
	}
	
	/**
	 * Gets the average CPU percentage.
	 *
	 * @param machine the machine
	 * @return the average CPU percentage
	 */
	public double getAverageCPUPercentage(ThreadInfoStorageMachine machine){
		return (getCPUTotal(machine) * 100) / getCPUMax(machine);
	}
	
	/**
	 * Gets the maximum metric of machine in MFLOP / millisecond
	 *
	 * @param machine the machine
	 * @return the MFLOPs
	 */
	private double getMetricMax(ThreadInfoStorageMachine machine){
		return FACTOR_MAX_MACHINE_LOAD * machine.getMflops();
	}
	
	/**
	 * Gets the total metric (MFLOP) of all agents, machine.
	 *
	 * @param machine the machine
	 * @return the total MFLOP consumption
	 */
	private double getMetricTotal(ThreadInfoStorageMachine machine){
		//--- rule of three ---
		return (getMetricMax(machine) * getCPUTotal(machine))/getCPUMax(machine);
	}

	/**
	 * Gets the integral for time series.
	 *
	 * @param series the series
	 * @param from the start
	 * @param to the end
	 * @return the integral for time series
	 */
	private double getIntegralForTimeSeries(XYSeries series, int from, int to){
		//hint: all Values are stored in milliseconds
		double integral = 0.0d;
		
		if(to > series.getItemCount()-1 || to < 0){
			to = series.getItemCount()-1;
		}
		
		if(!(from < to)){
			from = to;
		}
		
		//--- simple calculation of area ---
		for(int x = from; x < to; x++){
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
	 * Gets the simulation duration in milliseconds.
	 *
	 * @return the simulationDuration
	 */
	public double getSimulationDurationMilliSeconds() {
		double simulationDuration = 0;
		
		if(this.threadInfoStorage != null){
			//get the first series and its CPU data
			Iterator<Entry<String, ThreadInfoStorageMachine>> iteratorMachine = threadInfoStorage.getMapMachine().entrySet().iterator();
			String machineName = iteratorMachine.next().getValue().getName();
			double start = this.threadInfoStorage.getMapMachine().get(machineName).getXYSeriesMap().get(this.threadInfoStorage.TOTAL_CPU_SYSTEM_TIME).getMinX();
			double end   = this.threadInfoStorage.getMapMachine().get(machineName).getXYSeriesMap().get(this.threadInfoStorage.TOTAL_CPU_SYSTEM_TIME).getMaxX();
			simulationDuration = end - start;
		}
		return simulationDuration;
	}
	
	/**
	 * Gets the sampling interval in milliseconds.
	 *
	 * @param series the series
	 * @param from the sampling interval offset start value
	 * @param to the the sampling interval offset end value
	 * @return the sampling interval
	 */
	private double getSamplingIntervalMilliSeconds(XYSeries series, int from, int to){
		double samplingInterval = this.getSimulationDurationMilliSeconds();
		
		if(to > series.getItemCount()-1 || to < 0){
			to = series.getItemCount()-1;
		}
		
		if(!(from < to)){
			from = to;
		}
		
		if(this.threadInfoStorage != null && series.getItemCount() > from){
			Number start = series.getX(from); //first value
			Number end   = series.getX(to); //last value
			samplingInterval = end.doubleValue() - start.doubleValue();
		}
		return samplingInterval;
	}
}
