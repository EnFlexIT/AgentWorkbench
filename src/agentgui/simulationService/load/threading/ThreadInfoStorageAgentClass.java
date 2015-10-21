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

import java.awt.Color;

import org.jfree.data.xy.XYSeries;

/**
 * Protocol class for storing Thread-Load-Information of an Agent-Class
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageAgentClass extends ThreadInfoStorageSeries{
	/**
	* The available series keys as constants
	*/
	public final String AVG_TOTAL_CPU_USER_TIME   = "AVG_TOTAL_CPU_USER_TIME";
	public final String AVG_TOTAL_CPU_SYSTEM_TIME = "AVG_TOTAL_CPU_SYSTEM_TIME";
	public final String AVG_DELTA_CPU_USER_TIME   = "AVG_DELTA_CPU_USER_TIME";
	public final String AVG_DELTA_CPU_SYSTEM_TIME = "AVG_DELTA_CPU_SYSTEM_TIME";
	public final String MAX_TOTAL_CPU_USER_TIME   = "MAX_TOTAL_CPU_USER_TIME";
	public final String MAX_TOTAL_CPU_SYSTEM_TIME = "MAX_TOTAL_CPU_SYSTEM_TIME";
	public final String MAX_DELTA_CPU_USER_TIME   = "MAX_DELTA_CPU_USER_TIME";
	public final String MAX_DELTA_CPU_SYSTEM_TIME = "MAX_DELTA_CPU_SYSTEM_TIME";
	
	private double avgPredictiveMetric;
	private double avgrealMetric;
	private double minPredictiveMetric;
	private double minRealMetric;
	private double maxPredictiveMetric;
	private double maxRealMetric;
	
	/**
	 * Instantiates a new thread info storage agent class.
	 * @param name the name
	 */
	public ThreadInfoStorageAgentClass(String name) {
		super(name);
		
		this.avgPredictiveMetric 	= 0;
		this.avgrealMetric 			= 0;
		this.minPredictiveMetric 	= 0;
		this.minRealMetric 			= 0;
		this.maxPredictiveMetric 	= 0;
		this.maxRealMetric 			= 0;
		
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		getXYSeriesMap().put(AVG_TOTAL_CPU_SYSTEM_TIME,new XYSeries(AVG_TOTAL_CPU_SYSTEM_TIME+DELIMITER+name));
		getXYSeriesMap().put(AVG_TOTAL_CPU_USER_TIME,new XYSeries(AVG_TOTAL_CPU_USER_TIME+DELIMITER+name));
		getXYSeriesMap().put(MAX_TOTAL_CPU_SYSTEM_TIME,new XYSeries(MAX_TOTAL_CPU_SYSTEM_TIME+DELIMITER+name));
		getXYSeriesMap().put(MAX_TOTAL_CPU_USER_TIME,new XYSeries(MAX_TOTAL_CPU_USER_TIME+DELIMITER+name));
		
		getXYSeriesMap().put(AVG_DELTA_CPU_SYSTEM_TIME,new XYSeries(AVG_DELTA_CPU_SYSTEM_TIME+DELIMITER+name));
		getXYSeriesMap().put(AVG_DELTA_CPU_USER_TIME,new XYSeries(AVG_DELTA_CPU_USER_TIME+DELIMITER+name));	
		getXYSeriesMap().put(MAX_DELTA_CPU_SYSTEM_TIME,new XYSeries(MAX_DELTA_CPU_SYSTEM_TIME+DELIMITER+name));	
		getXYSeriesMap().put(MAX_DELTA_CPU_USER_TIME,new XYSeries(MAX_DELTA_CPU_USER_TIME+DELIMITER+name));			
		
		// --- default: total series RED, delta series BLACK ---
		getXySeriesLineColorMap().put(AVG_TOTAL_CPU_USER_TIME+DELIMITER+name, Color.magenta);
		getXySeriesLineColorMap().put(AVG_DELTA_CPU_USER_TIME+DELIMITER+name, Color.gray);
		getXySeriesLineColorMap().put(AVG_TOTAL_CPU_SYSTEM_TIME+DELIMITER+name, Color.magenta);
		getXySeriesLineColorMap().put(AVG_DELTA_CPU_SYSTEM_TIME+DELIMITER+name, Color.gray);
		
		getXySeriesLineColorMap().put(MAX_TOTAL_CPU_USER_TIME+DELIMITER+name, Color.RED);
		getXySeriesLineColorMap().put(MAX_DELTA_CPU_USER_TIME+DELIMITER+name, Color.BLACK);
		getXySeriesLineColorMap().put(MAX_TOTAL_CPU_SYSTEM_TIME+DELIMITER+name, Color.RED);
		getXySeriesLineColorMap().put(MAX_DELTA_CPU_SYSTEM_TIME+DELIMITER+name, Color.BLACK);
	}
	/**
	 * @return the avgPredictiveMetric
	 */
	public double getAvgPredictiveMetric() {
		return avgPredictiveMetric;
	}
	/**
	 * @param avgPredictiveMetric the avgPredictiveMetric to set
	 */
	public void setAvgPredictiveMetric(double avgPredictiveMetric) {
		this.avgPredictiveMetric = avgPredictiveMetric;
	}
	/**
	 * @return the avgPrealMetric
	 */
	public double getAvgRealMetric() {
		return avgrealMetric;
	}
	/**
	 * @param avgRealMetric the avgRealMetric to set
	 */
	public void setAvgRealMetric(double avgPrealMetric) {
		this.avgrealMetric = avgPrealMetric;
	}
	/**
	 * @return the minPredictiveMetric
	 */
	public double getMinPredictiveMetric() {
		return minPredictiveMetric;
	}
	/**
	 * @param minPredictiveMetric the minPredictiveMetric to set
	 */
	public void setMinPredictiveMetric(double minPredictiveMetric) {
		this.minPredictiveMetric = minPredictiveMetric;
	}
	/**
	 * @return the minrealMetric
	 */
	public double getMinRealMetric() {
		return minRealMetric;
	}
	/**
	 * @param minrealMetric the minrealMetric to set
	 */
	public void setMinRealMetric(double minrealMetric) {
		this.minRealMetric = minrealMetric;
	}
	/**
	 * @return the maxPredictiveMetric
	 */
	public double getMaxPredictiveMetric() {
		return maxPredictiveMetric;
	}
	/**
	 * @param maxPredictiveMetric the maxPredictiveMetric to set
	 */
	public void setMaxPredictiveMetric(double maxPredictiveMetric) {
		this.maxPredictiveMetric = maxPredictiveMetric;
	}
	/**
	 * @return the maxRealMetric
	 */
	public double getMaxRealMetric() {
		return maxRealMetric;
	}
	/**
	 * @param maxRealMetric the maxRealMetric to set
	 */
	public void setMaxRealMetric(double maxRealMetric) {
		this.maxRealMetric = maxRealMetric;
	}
}
