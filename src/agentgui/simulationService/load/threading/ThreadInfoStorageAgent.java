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
	
	/**
 * Instantiates a new thread info storage agent.
 *
 * @param name the name
 * @param className the class name
 * @param isAgent the is agent
 */
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
		return predictMetricCPU;
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
