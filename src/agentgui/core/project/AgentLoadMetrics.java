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

import javax.xml.bind.annotation.XmlTransient;

/**
 * The Class AgentLoadMetrics describes the predictive or real measured load of the.
 * agent classes within a project.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentLoadMetrics implements Serializable {

	private static final long serialVersionUID = 2251966049148701116L;

	private Project currProject;
	
	boolean useRealLoadMetric = false;
	private Vector<AgentLoadDescription> agentLoadDescriptionVector;
	
	
	/**
	 * Instantiates a new agent load metrics.
	 * @param currProject the current {@link Project} instance
	 */
	public AgentLoadMetrics(Project currProject) {
		this.currProject = currProject;
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
	 * Gets the agent load description vector.
	 * @return the agent load description vector
	 */
	@XmlTransient
	public Vector<AgentLoadDescription> getAgentLoadDescriptionVector() {
		if (agentLoadDescriptionVector==null) {
			agentLoadDescriptionVector = new Vector<AgentLoadMetrics.AgentLoadDescription>();
		}
		return agentLoadDescriptionVector;
	}
	/**
	 * Sets the agent load description vector.
	 * @param agentLoadDescriptionVector the new agent load description vector
	 */
	public void setAgentLoadDescriptionVector(Vector<AgentLoadDescription> agentLoadDescriptionVector) {
		this.agentLoadDescriptionVector = agentLoadDescriptionVector;
	}

	/**
	 * Adds a new {@link AgentLoadDescription}.
	 *
	 * @param className the class name
	 * @param userPredictiveLoad the user predictive load
	 * @param realLoadMin the real load min
	 * @param realLoadMax the real load max
	 * @param realLoadAverage the real load average
	 */
	public void addAgentLoadDescription(String className, double userPredictiveLoad, double realLoadMin, double realLoadMax, double realLoadAverage) {
		this.getAgentLoadDescriptionVector().addElement(new AgentLoadDescription(className, userPredictiveLoad, realLoadMin, realLoadMax, realLoadAverage));
		this.setProjectNotification(Project.AGENT_METRIC_AgentDescriptionAdded);
	}
	/**
	 * Removes the agent load description.
	 *
	 * @param className the class name
	 * @return the agent load description
	 */
	public AgentLoadDescription removeAgentLoadDescription(String className) {
		AgentLoadDescription removed = null;
		int elementToRemove = this.getIndexOfAgentLoadDescription(className);
		if (elementToRemove!=-1) {
			removed = this.getAgentLoadDescriptionVector().remove(elementToRemove);
			this.currProject.setChangedAndNotify(Project.AGENT_METRIC_AgentDescriptionRemoved);
		}
		return removed;
	}
	
	/**
	 * Gets the index of agent load description.
	 *
	 * @param className the class name
	 * @return the index of agent load description
	 */
	public int getIndexOfAgentLoadDescription(String className) {
		for (int i = 0; i < this.getAgentLoadDescriptionVector().size(); i++) {
			AgentLoadDescription ald =  this.getAgentLoadDescriptionVector().get(i);
			if (ald.getClassName().equals(className)) return i;
		}
		return -1;
	}
	/**
	 * Gets the agent load description.
	 *
	 * @param className the class name
	 * @return the agent load description
	 */
	public AgentLoadDescription getAgentLoadDescription(String className) {
		for(AgentLoadDescription ald : this.getAgentLoadDescriptionVector()) {
			if (ald.getClassName().equals(className)) {
				return ald;
			}
		}
		return null;
	}

	
	
	/**
	 * The Class AgentLoadDescription.
	 */
	public class AgentLoadDescription {
		
		private String className; 
		
		private double userPredictiveLoad = 5;
		
		private double realLoadAverage;		
		private double realLoadMin;
		private double realLoadMax;
		
		/**
		 * Instantiates a new agent load description.
		 *
		 * @param className the class name of the agent
		 * @param userPredictiveLoad the user predictive load
		 * @param realLoadMin the minimum real load 
		 * @param realLoadMax the maximum real load max
		 * @param realLoadAverage the average real load 
		 */
		public AgentLoadDescription(String className, double userPredictiveLoad, double realLoadMin, double realLoadMax, double realLoadAverage) {
			this.setClassName(className);
			this.setUserPredictiveLoad(userPredictiveLoad);
			this.setRealLoadMin(realLoadMin);
			this.setRealLoadMax(realLoadMax);
			this.setRealLoadAverage(realLoadAverage);
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
		 * Gets the user predictive load.
		 * @return the userPredictiveLoad
		 */
		public double getUserPredictiveLoad() {
			return userPredictiveLoad;
		}
		/**
		 * Sets the user predictive load.
		 * @param userPredictiveLoad the userPredictiveLoad to set
		 */
		public void setUserPredictiveLoad(double userPredictiveLoad) {
			this.userPredictiveLoad = userPredictiveLoad;
			setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
		}
		
		/**
		 * Gets the real load average.
		 * @return the realLoadAverage
		 */
		public double getRealLoadAverage() {
			return realLoadAverage;
		}
		/**
		 * Sets the real load average.
		 * @param realLoadAverage the realLoadAverage to set
		 */
		public void setRealLoadAverage(double realLoadAverage) {
			this.realLoadAverage = realLoadAverage;
			setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
		}
		
		/**
		 * Gets the real load min.
		 * @return the realLoadMin
		 */
		public double getRealLoadMin() {
			return realLoadMin;
		}
		/**
		 * Sets the real load min.
		 * @param realLoadMin the realLoadMin to set
		 */
		public void setRealLoadMin(double realLoadMin) {
			this.realLoadMin = realLoadMin;
			setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
		}
		
		/**
		 * Gets the real load max.
		 * @return the realLoadMax
		 */
		public double getRealLoadMax() {
			return realLoadMax;
		}
		/**
		 * Sets the real load max.
		 * @param realLoadMax the realLoadMax to set
		 */
		public void setRealLoadMax(double realLoadMax) {
			this.realLoadMax = realLoadMax;
			setProjectNotification(Project.AGENT_METRIC_AgentDescriptionEdited);
		}
	} // --- end sub class ----
	
}
