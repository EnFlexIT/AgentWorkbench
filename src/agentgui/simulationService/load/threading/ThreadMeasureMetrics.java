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
import agentgui.core.project.AgentClassLoadMetrics;
import agentgui.core.project.AgentClassMetricDescription;
import agentgui.core.project.Project;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorage;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageAgent;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageMachine;

/**
 * The Class ThreadMeasureMetrics.
 * 
 * Calculates the "real" metrics for all (threadable) agents.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadMeasureMetrics {
	
	/**  The simulation initialization phase offset (t0) in milliseconds
	 *  after which metrics can be calculated, default 2min = 120000. */
	private final long SIMULATION_INIT_OFFSET_MS	    = 120000;
	
	/**  The sampling interval offset, default 10. 
	 *  depending on the recording interval, 
	 *  this defines the index of the first value (t0)*/
	private final int SAMPLING_INTERVAL_OFFSET_DEFAULT	= 10;
	
	/**  The simulation duration minimum in milliseconds
	 *  after which metrics can be calculated, default 5min = 300000. */
	public final double SIMULATION_DURATION_MIN 		= 300000 + SIMULATION_INIT_OFFSET_MS;

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
	
	/** The calculation type. */
	private String calcType;
	
	/** The base for calculation of metric. */
	private String metricBase;

	/** The thread info storage. */
	private ThreadInfoStorage threadInfoStorage;
	
	/** The simulation duration. */
	private double simulationDuration;
	
	/** The sampling interval. */
	private double samplingInterval;
	
	/** The sampling interval offset. */
	private int samplingIntervalOffset;
	
	/**  The map that holds the values (average, integral, last total), depending on calcType. */
	private HashMap<String, Double> calcTypeValueMap;
	
	/** The current project. */
	private Project currProject;
	
	/** The local machine name. */
	private String localMachineName;
	
	/**
	 * Instantiates a new thread measure metrics.
	 */
	public ThreadMeasureMetrics(){
		initialize();
	}
	
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
		currProject = Application.getProjectFocused();
		
		Iterator<Entry<String, ThreadInfoStorageMachine>> iteratorMachine = threadInfoStorage.getMapMachine().entrySet().iterator();
		localMachineName = iteratorMachine.next().getValue().getName();
	}
	

	/**
	 * Calculates the metrics for all agents.
	 */
	public void getMetrics(){
		
		if(isDataUsable() == true){
						
			Iterator<Entry<String, ThreadInfoStorageMachine>> iteratorMachine = threadInfoStorage.getMapMachine().entrySet().iterator();
			while (iteratorMachine.hasNext()){
				
				
				ThreadInfoStorageMachine actualMachine = iteratorMachine.next().getValue();
				XYSeries series = actualMachine.getXYSeriesMap().get(this.threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
				
				samplingIntervalOffset = getSamplingIntervalOffset(series);
				samplingInterval = getSamplingIntervalMilliSeconds(series,samplingIntervalOffset);
				
				if (getCalcType().equals(CALC_TYPE_INTEGRAL_DELTA)) {
					series = actualMachine.getXYSeriesMap().get(threadInfoStorage.DELTA_CPU_SYSTEM_TIME);
					calcTypeValueMap.put(actualMachine.getName(), getIntegralForTimeSeries(series, samplingIntervalOffset, series.getItemCount()-1));				
				} else if (getCalcType().equals(CALC_TYPE_INTEGRAL_TOTAL)) {
					series = actualMachine.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
					calcTypeValueMap.put(actualMachine.getName(), getIntegralForTimeSeries(series, samplingIntervalOffset, series.getItemCount()-1));
				} else if (getCalcType().equals(CALC_TYPE_LAST_TOTAL)) {
					series = actualMachine.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
					calcTypeValueMap.put(actualMachine.getName(), series.getMaxY());
				}
				
				
				Iterator<Entry<String, ThreadInfoStorageAgent>> iteratorAgent = threadInfoStorage.getMapAgent().entrySet().iterator();
				while (iteratorAgent.hasNext()){
					
					ThreadInfoStorageAgent actualAgent = iteratorAgent.next().getValue();
					if (actualAgent.getName().contains(actualMachine.getName())) {
						actualAgent.setRealMetric(getMetricForAgent(actualAgent, actualMachine, samplingIntervalOffset, samplingInterval));
					}
				}
			}
			addOrUpdateAgentClassRealMetrics();
		}
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
			if (actualAgent.isAgent() == true) {

				String className = actualAgent.getClassName();
				double actualMetric = actualAgent.getRealMetric();
				
				AgentClassMetricDescription agentClass = mapAgentClass.get(className);
				int noOfAgents = threadInfoStorage.getNoOfAgentsPerClass().get(className);
				
				if(agentClass == null){
					agentClass = new AgentClassMetricDescription();
					agentClass.setRealMetricMin(actualMetric);
					agentClass.setRealMetricMax(actualMetric);
					agentClass.setRealMetricAverage(actualMetric/noOfAgents);
					agentClass.setClassName(className);
					mapAgentClass.put(className, agentClass);

				}else{
					
					if(agentClass.getRealMetricMin() > actualMetric){
						agentClass.setRealMetricMin(actualMetric);
					}
					if(agentClass.getRealMetricMax()< actualMetric){
						agentClass.setRealMetricMax(actualMetric);
					}
					
					agentClass.setRealMetricAverage(agentClass.getRealMetricAverage() + (actualMetric/noOfAgents));	
				}
			}
		}
		
		AgentClassLoadMetrics aclm = currProject.getAgentClassLoadMetrics();
		
		aclm.clearTableModel();
		
		//update min, max and average real metrics for agent class in current project
		Iterator<Entry<String, AgentClassMetricDescription>> iteratorAgentMetricsMap = mapAgentClass.entrySet().iterator();
		while (iteratorAgentMetricsMap.hasNext()){
			AgentClassMetricDescription actualAgentClass = iteratorAgentMetricsMap.next().getValue();
			String className = actualAgentClass.getClassName();
			double min = actualAgentClass.getRealMetricMin();
			double max = actualAgentClass.getRealMetricMax();
			double avg = actualAgentClass.getRealMetricAverage();
			
			int index = aclm.getIndexOfAgentClassMetricDescription(className);
			if(index == -1){
				aclm.addAgentLoadDescription(className,1 , min, max, avg);
				aclm.addTableModelRow(new AgentClassMetricDescription(currProject,className,1 , min, max, avg));
			}else{
				aclm.getAgentClassMetricDescriptionVector().get(index).setRealMetricMin(min);
				aclm.getAgentClassMetricDescriptionVector().get(index).setRealMetricMax(max);
				aclm.getAgentClassMetricDescriptionVector().get(index).setRealMetricAverage(avg);
				
				aclm.addTableModelRow(aclm.getAgentClassMetricDescriptionVector().get(index));
			}
		}
		currProject.setAgentClassLoadMetrics(aclm);
	}

	/**
	 * Gets the metric for a single agent.
	 *
	 * @param agent the agent
	 * @param machine the machine
	 * @param samplingIntervalOffset the sampling interval offset
	 * @param samplingInterval the sampling interval
	 * @return the metric for agent
	 */
	private double getMetricForAgent(ThreadInfoStorageAgent agent,ThreadInfoStorageMachine machine, int samplingIntervalOffset, double samplingInterval){
		XYSeries series = new XYSeries("");
		double actualValue = 0.0d;
		
		if (getMetricBase().equals(METRIC_BASE_INDIVIDUAL)) {
			// --- calculate Integral based on data of individual agent ---
			
			if (getCalcType().equals(CALC_TYPE_INTEGRAL_DELTA)) {
				series = agent.getXYSeriesMap().get(threadInfoStorage.DELTA_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, samplingIntervalOffset, series.getItemCount()-1);				
			} else if (getCalcType().equals(CALC_TYPE_INTEGRAL_TOTAL)) {
				series = agent.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, samplingIntervalOffset, series.getItemCount()-1);
			} else if (getCalcType().equals(CALC_TYPE_LAST_TOTAL)) {
				series = agent.getXYSeriesMap().get(threadInfoStorage.TOTAL_CPU_SYSTEM_TIME);
				actualValue = series.getMaxY();
			}
			
		} else if (getMetricBase().equals(METRIC_BASE_CLASS)) {
			// --- calculate Integral based on data of the agent's class (average) ---
			
			if (getCalcType().equals(CALC_TYPE_INTEGRAL_DELTA)) {
				series = threadInfoStorage.getMapAgentClass().get(agent.getClassName()).getXYSeriesMap().get(threadInfoStorage.AVG_DELTA_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, samplingIntervalOffset, series.getItemCount()-1);				
			} else if (getCalcType().equals(CALC_TYPE_INTEGRAL_TOTAL)) {
				series = threadInfoStorage.getMapAgentClass().get(agent.getClassName()).getXYSeriesMap().get(threadInfoStorage.AVG_TOTAL_CPU_SYSTEM_TIME);
				actualValue = getIntegralForTimeSeries(series, samplingIntervalOffset, series.getItemCount()-1);				
			} else if (getCalcType().equals(CALC_TYPE_LAST_TOTAL)) {
				series = threadInfoStorage.getMapAgentClass().get(agent.getClassName()).getXYSeriesMap().get(threadInfoStorage.AVG_TOTAL_CPU_SYSTEM_TIME);
				actualValue = series.getMaxY();
			}
		}
		
		//--- rule of three ---
		return (getMetricTotal(machine) * actualValue)/calcTypeValueMap.get(machine.getName());
	}
	
	/**
	 * Checks if data/measurement is sufficient for determining metrics.
	 *
	 * @return true, if usable
	 */
	public boolean isDataUsable(){
		
		if (getSimulationDurationMilliSeconds() >= SIMULATION_DURATION_MIN){
			return true;
		}
		return false;
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
		return getIntegralForTimeSeries(series, samplingIntervalOffset, series.getItemCount()-1);
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
	 * @param start the start
	 * @param end the end
	 * @return the integral for time series
	 */
	private double getIntegralForTimeSeries(XYSeries series, int start, int end){
		//hint: all Values are stored in milliseconds
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
	 * Gets the simulation duration in milliseconds.
	 *
	 * @return the simulationDuration
	 */
	public double getSimulationDurationMilliSeconds() {
		double simulationDuration = 0;

		if(this.threadInfoStorage != null){
			double start = this.threadInfoStorage.getMapMachine().get(localMachineName).getXYSeriesMap().get(this.threadInfoStorage.TOTAL_CPU_SYSTEM_TIME).getMinX();
			double end   = this.threadInfoStorage.getMapMachine().get(localMachineName).getXYSeriesMap().get(this.threadInfoStorage.TOTAL_CPU_SYSTEM_TIME).getMaxX();
			simulationDuration = end - start;
		}
		return simulationDuration;
	}	
	
	/**
	 * Gets the sampling interval offset.
	 *
	 * @param series the series
	 * @return the sampling interval offset
	 */
	private int getSamplingIntervalOffset(XYSeries series){
		double start = series.getX(0).doubleValue();
		
		for(int i = 0; i < series.getItemCount(); i++){
			double actual = series.getX(i).doubleValue();
			if((actual-start)>=SIMULATION_INIT_OFFSET_MS){
				return i;
			}
		}
		return SAMPLING_INTERVAL_OFFSET_DEFAULT;
	}
	
	/**
	 * Gets the sampling interval in milliseconds.
	 *
	 * @param series the series
	 * @param samplingIntervalOffset the sampling interval offset
	 * @return the sampling interval
	 */
	private double getSamplingIntervalMilliSeconds(XYSeries series, int samplingIntervalOffset){
		
		double samplingInterval = this.simulationDuration;
		
		if(this.threadInfoStorage != null){
			Number start = series.getX(samplingIntervalOffset); //first value
			Number end   = series.getMaxX(); //last value
			samplingInterval = end.doubleValue() - start.doubleValue();
		}
		return samplingInterval;
	}
}
