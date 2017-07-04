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
 * The Class AgentClassLoadMetricsTable describes the predictive or real measured metrics of the.
 * agent classes within a project.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentClassLoadMetricsTable implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2251966049148701116L;

	/** The curr project. */
	private Project currProject;
	
	/** The use real load metric. */
	private boolean useRealLoadMetric = false;
	
	
	/** The table model. */
	private DefaultTableModel tableModel;
	
	/** The agent class metric description vector. */
	@XmlElementWrapper(name = "agentClassMetricDescriptionVector")
	@XmlElement(name = "agentClass")
	private Vector<AgentClassMetricDescription> agentClassMetricDescriptionVector = null;
	
	/**
	 * Instantiates a new agent class load metrics.
	 */
	public AgentClassLoadMetricsTable() {
	}
	/**
	 * Instantiates a new agent load metrics.
	 * @param currProject the current {@link Project} instance
	 */
	public AgentClassLoadMetricsTable(Project currProject) {
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
	 *
	 * @return the project
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
	 * Adds the agent load description.
	 *
	 * @param className the class name
	 * @param userPredictiveLoad the user predictive metric
	 * @param realMetricMin the real metric min
	 * @param realMetricMax the real metric max
	 * @param realMetricAverage the real metric average
	 */
	public void addAgentLoadDescription(String className, double userPredictiveLoad, double realMetric, double realMetricMin, double realMetricMax, double realMetricAverage) {
		this.getAgentClassMetricDescriptionVector().addElement(new AgentClassMetricDescription(currProject, className, userPredictiveLoad, realMetric, realMetricMin, realMetricMax, realMetricAverage));
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
	 * Copy real metrics average to real metrics.
	 */
	public void copyRealMetricsAverage2RealMetrics(){		
		for(int i = 0; i < this.agentClassMetricDescriptionVector.size(); i++){
			this.agentClassMetricDescriptionVector.get(i).setRealMetric(this.agentClassMetricDescriptionVector.get(i).getRealMetricAverage());
		}
	}
	
	/**
	 * Load metrics settings from the current project.
	 */
	public void loadMetricsFromProject() {
		this.clearTableModel();
		if(this.agentClassMetricDescriptionVector != null){
			for(int i = 0; i < this.agentClassMetricDescriptionVector.size(); i++){
				this.addTableModelRow(this.agentClassMetricDescriptionVector.get(i));
			}
		}
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
	 * Gets the table model.
	 * @return the table model
	 */
	public DefaultTableModel getTableModel() {
		
		if (tableModel==null) {
			
			Vector<String> header = new Vector<String>();
			header.add("Class");
			header.add("Predictive Metric");
			header.add("Real Metric");
			header.add("Real Metric Min");
			header.add("Real Metric Max");
			header.add("Real Metric Average");
			
			
			tableModel = new DefaultTableModel(null, header){

				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isCellEditable(int row, int column) {
					if(column == 1){
						return true;
					}
					return false;
				}

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
		row.add(Math.round(agentClass.getRealMetric()));
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
