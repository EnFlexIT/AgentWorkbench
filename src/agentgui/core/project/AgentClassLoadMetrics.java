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
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The Class AgentClassLoadMetrics describes the predictive or real measured metrics of the.
 * agent classes within a project.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentClassLoadMetrics implements Serializable {

	private static final long serialVersionUID = 2251966049148701116L;

	private Project currProject;
	private boolean useRealLoadMetric = false;
	
	
	/** The table model. */
	private DefaultTableModel tableModel;
	
	@XmlElementWrapper(name = "agentClassMetricDescriptionVector")
	@XmlElement(name = "agentClass")
	private Vector<AgentClassMetricDescription> agentClassMetricDescriptionVector = null;
	
	/**
	 * Instantiates a new agent class load metrics.
	 */
	public AgentClassLoadMetrics() {
	}
	/**
	 * Instantiates a new agent load metrics.
	 * @param currProject the current {@link Project} instance
	 */
	public AgentClassLoadMetrics(Project currProject) {
		this.currProject = currProject;
	}
	/**
	 * Sets the current project instance.
	 * @param currProject the new project
	 */
	public void setProject(Project currProject) {
		this.currProject = currProject;
	}
	/**
	 * Returns the current project instance.
	 * @param currProject the new project
	 */

	@XmlTransient
	public Project getProject() {
		return this.currProject;
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
	 * Checks if is use real load metric.
	 * @return the useRealLoadMetric
	 */
	public boolean isUseRealLoadMetric() {
		return useRealLoadMetric;
	}
	/**
	 * Sets the use real load metric.
	 * @param useRealLoadMetric the useRealLoadMetric to set
	 */
	public void setUseRealLoadMetric(boolean useRealLoadMetric) {
		this.useRealLoadMetric = useRealLoadMetric;
		this.setProjectNotification(Project.AGENT_METRIC_ChangedDataSource);
	}
	/**
	 * Gets the agent class metric description vector.
	 * @return the agent load description vector
	 */
	
	public Vector<AgentClassMetricDescription> getAgentClassMetricDescriptionVector() {
		if (agentClassMetricDescriptionVector==null) {
			agentClassMetricDescriptionVector = new Vector<AgentClassMetricDescription>();
		}
		return agentClassMetricDescriptionVector;
	}
	/**
	 * Sets the agent class metric description vector.
	 * @param agentClassMetricDescriptionVector the new agent load description vector
	 */
	public void setAgentClassMetricDescription(Vector<AgentClassMetricDescription> agentClassMetricDescriptionVector) {
		this.agentClassMetricDescriptionVector = agentClassMetricDescriptionVector;
		this.setProjectNotification(Project.AGENT_METRIC_AgentDescriptionAdded);
	}

	/**
	 * Adds a new {@link AgentClassMetricDescription}.
	 *
	 * @param className the class name
	 * @param userPredictedMetric the user predictive load
	 * @param realMetricMin the real load min
	 * @param realMetricMax the real load max
	 * @param realLoadAverage the real load average
	 */
	public void addAgentLoadDescription(String className, double userPredictiveLoad, double realLoadMin, double realLoadMax, double realLoadAverage) {
		this.getAgentClassMetricDescriptionVector().addElement(new AgentClassMetricDescription(currProject,className, userPredictiveLoad, realLoadMin, realLoadMax, realLoadAverage));
		this.setProjectNotification(Project.AGENT_METRIC_AgentDescriptionAdded);
	}
	/**
	 * Removes the agent class metric description.
	 *
	 * @param className the class name
	 * @return the agent load description
	 */
	public AgentClassMetricDescription removeAgentClassMetricDescription(String className) {
		AgentClassMetricDescription removed = null;
		int elementToRemove = this.getIndexOfAgentClassMetricDescription(className);
		if (elementToRemove!=-1) {
			removed = this.getAgentClassMetricDescriptionVector().remove(elementToRemove);
			this.currProject.setChangedAndNotify(Project.AGENT_METRIC_AgentDescriptionRemoved);
		}
		return removed;
	}
	
	/**
	 * Gets the index of agent class metric description.
	 *
	 * @param className the class name
	 * @return the index of agent load description
	 */
	public int getIndexOfAgentClassMetricDescription(String className) {
		for (int i = 0; i < this.getAgentClassMetricDescriptionVector().size(); i++) {
			AgentClassMetricDescription ald =  this.getAgentClassMetricDescriptionVector().get(i);
			if (ald.getClassName().equals(className)) return i;
		}
		return -1;
	}
	/**
	 * Gets the agent class metric description.
	 *
	 * @param className the class name
	 * @return the agent load description
	 */
	
	public AgentClassMetricDescription getAgentClassMetricDescription(String className) {
		for(AgentClassMetricDescription ald : this.getAgentClassMetricDescriptionVector()) {
			if (ald.getClassName().equals(className)) {
				return ald;
			}
		}
		return null;
	}	
	
	/**
	 * Gets the table model for this {@link ThreadMeasureMetrics}.
	 * @return the table model
	 */
	public DefaultTableModel getTableModel() {
		
		if (tableModel==null) {
			
			Vector<String> header = new Vector<String>();
			header.add("Class");
			header.add("Predictive Metric");
			header.add("Real Metric Min");
			header.add("Real Metric Max");
			header.add("Real Metric Average");
			
			
			tableModel = new DefaultTableModel(null, header){

				private static final long serialVersionUID = 1L;

				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int column){
					if (column >= 0 && column <= getColumnCount()) {
						return getValueAt(0, column).getClass();
					} else {
						return Object.class;
					}
				}
			};
			
			// --- Necessary for preventing sorter from throwing error about empty row
			addTableModelRow(null);
		}
		return tableModel;
	}
	
	/**
	 * Adds a row to the table model.
	 *
	 * @param agentClass the agent class
	 */
	public void addTableModelRow(AgentClassMetricDescription agentClass) {
		if(agentClass == null){
			agentClass = new AgentClassMetricDescription();
		}
		Vector<Object> row = new Vector<Object>();
		row.add(agentClass);
		// ---Math.round.. remove decimal .000 ----
		row.add(Math.round(agentClass.getUserPredictedMetric()));
		row.add(Math.round(agentClass.getRealMetricMin()));
		row.add(Math.round(agentClass.getRealMetricMax()));
		row.add(Math.round(agentClass.getRealMetricAverage()));
		
		// --- Add row to table model ---
		getTableModel().addRow(row); 
	}
	
	/**
	 * Clears the table model.
	 */
	public void clearTableModel() {
		while (getTableModel().getRowCount()>0) {
			getTableModel().removeRow(0);
		}
	}
}
