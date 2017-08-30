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
package agentgui.core.project;

import java.io.Serializable;

/**
 * The Class AgentClassMetricDescription.
 * 
 * Describes all information concerning metrics associated with an agent-class
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentClassMetricDescription implements Serializable{
	
	private static final long serialVersionUID = 4739306168348986176L;
	private Project currProject;
	private String className; 
	private double userPredictedMetric = 5;
	private double realMetric = 0.1;	
	private double realMetricAverage = 0.1;
	private double realMetricMin = 0.1;
	private double realMetricMax = 0.1;
	
	/**
	 * Instantiates a new agent class metric description.
	 */
	public AgentClassMetricDescription(){
	}
	/**
	 * Instantiates a new agent class metric description.
	 */
	public AgentClassMetricDescription(Project currProject){
		this.currProject = currProject;
	}
	
	/**
	 * Instantiates a new agent class metric description.
	 *
	 * @param className the class name of the agent
	 * @param userPredictedMetric the user predictive load
	 * @param realMetricMin the minimum real load
	 * @param realMetricMax the maximum real load max
	 * @param realMetricAverage the real metric average
	 */
	public AgentClassMetricDescription(Project currProject,String className, double userPredictedMetric, double realMetric, double realMetricMin, double realMetricMax, double realMetricAverage) {
		this.currProject = currProject;
		this.setClassName(className);
		this.setUserPredictedMetric(userPredictedMetric);
		this.setRealMetric(realMetric);
		this.setRealMetricMin(realMetricMin);
		this.setRealMetricMax(realMetricMax);
		this.setRealMetricAverage(realMetricAverage);
	}
	/**
	 * Sets the project notification for changes within this class.
	 * @param projectNotication the new project notification
	 */
	private void setProjectNotification(String projectNotication) {
		if (this.currProject!=null) {
			this.currProject.setChangedAndNotify(projectNotication);
		}
	}
	/**
	 * Gets the class name.
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * Sets the class name.
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
		setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
	}
	
	/**
	 * Gets the user predicted metric.
	 * @return the user predicted metric
	 */
	public double getUserPredictedMetric() {
		return userPredictedMetric;
	}
	
	/**
	 * Sets the user predicted metric.
	 * @param userPredictedMetric the new user predicted metric
	 */
	public void setUserPredictedMetric(double userPredictedMetric) {
		this.userPredictedMetric = userPredictedMetric;
		setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
	}
	
	/**
	 * Gets the real metric.
	 * @return the real metric 
	 */
	public double getRealMetric() {
		return realMetric;
	}
	
	/**
	 * Sets the real metric.
	 * @param realMetric the new real metric
	 */
	public void setRealMetric(double realMetric) {
		this.realMetric = realMetric;
		setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
	}
	/**
	 * Gets the real metric average.
	 * @return the real metric average
	 */
	public double getRealMetricAverage() {
		return realMetricAverage;
	}
	
	/**
	 * Sets the real metric average.
	 * @param realMetricAverage the new real metric average
	 */
	public void setRealMetricAverage(double realMetricAverage) {
		this.realMetricAverage = realMetricAverage;
		setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
	}
	
	/**
	 * Gets the real metric minimum.
	 * @return the real metric minimum
	 */
	public double getRealMetricMin() {
		return realMetricMin;
	}
	/**
	 * Sets the real metric min.
	 * @param realMetricMin the realMetricMin to set
	 */
	public void setRealMetricMin(double realMetricMin) {
		this.realMetricMin = realMetricMin;
		setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
	}
	
	/**
	 * Gets the real metric max.
	 * @return the realMetricMax
	 */
	public double getRealMetricMax() {
		return realMetricMax;
	}
	/**
	 * Sets the real metric max.
	 * @param realMetricMax the realMetricMax to set
	 */
	public void setRealMetricMax(double realMetricMax) {
		this.realMetricMax = realMetricMax;
		setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
	}
	@Override
	public String toString(){
		return className;
	}
}
