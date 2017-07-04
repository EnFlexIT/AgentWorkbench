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
package agentgui.simulationService.load.monitoring;

import agentgui.core.application.Application;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

/**
 * The Class SingleAgentMonitor can be used to monitor any {@link Agent}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SingleAgentMonitor extends AbstractMonitoringTask {

	private MonitoringMeasureType monitoringMeasureType;
	private Runnable measure;

	private String agentName; 
	private String agentClass;
	private Object[] agentStartArguments;
	
	private ContainerController containerController;
	
	
	/**
	 * Instantiates a new agent monitor.
	 *
	 * @param agent the agent to monitor
	 * @param monitoringMeasureType the actual MonitoringMeasureType
	 */
	public SingleAgentMonitor(Agent agent, MonitoringMeasureType monitoringMeasureType) {
		this(agent, monitoringMeasureType, null);
	}
	/**
	 * Instantiates a new agent monitor.
	 *
	 * @param agent the agent to monitor
	 * @param monitoringMeasureType the actual MonitoringMeasureType
	 * @param runnable the runnable
	 */
	public SingleAgentMonitor(Agent agent, MonitoringMeasureType monitoringMeasureType, Runnable runnable) {
		if (agent==null) {
			throw new IllegalArgumentException("No agent was specified for the monitoring task.");
		}
		if (monitoringMeasureType==null) {
			throw new IllegalArgumentException("No measure were specified in case of a thread failure.");
		}
		if (monitoringMeasureType==MonitoringMeasureType.CUSTOM_MEASURE && runnable==null) {
			throw new IllegalArgumentException("No runnable was defined for the custom measure.");
		}
		
		this.agentName = agent.getLocalName();
		this.agentClass = agent.getClass().getName();
		this.agentStartArguments = agent.getArguments();
		this.containerController = agent.getContainerController();
		
		this.monitoringMeasureType = monitoringMeasureType;
		this.measure = runnable;
	}
	
	/**
	 * Checks if the platform is running.
	 * @return true, if is platform running
	 */
	protected boolean isPlatformRunning() {
		return Application.getJadePlatform().isMainContainerRunning();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#isFaultlessProcess()
	 */
	@Override
	public boolean isFaultlessProcess() {
		
		// --- 0. Is the platform still running? --------------------
		if (this.isPlatformRunning()==false) {
			this.unregisterTask();
			return false;
		}
		
		// --- 1. Try to find the agent in the container ------------
		AgentController agentController = null;
		try {
			agentController = this.containerController.getAgent(this.agentName);
			
		} catch (ControllerException ce) {
			//ce.printStackTrace();
			return false;
		}

		// --- 2. Try to get the agent's state ----------------------
		boolean isRunning;
		try {
			agentController.getState();
			isRunning = true;
		
		} catch (Exception ex) {
			// ex.printStackTrace();
			isRunning = false; 			
		}		
		return isRunning;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#getFaultMeasure()
	 */
	@Override
	public Runnable getFaultMeasure() {
		
		// --- 0. Is the platform still running? ----------
		if (this.isPlatformRunning()==false) {
			this.unregisterTask();
			return null;
		}
		
		// -- In case of an individual measure action -----
		if (this.measure!=null) return this.measure;

		// --- Only for the restart of the agent ----------
		Runnable failureMeasure = new Runnable() {
			@Override
			public void run() {
				try {
					containerController.createNewAgent(agentName, agentClass, agentStartArguments).start();
				} catch (StaleProxyException spex) {
					//spex.printStackTrace();
					System.err.println("[MonitoringEvent] Agent restart failed.");
					setMonitoringState(MonitoringState.FAULTY_PROCESS_FAULTY_MEASURE);
				}
			}
		};
		return failureMeasure;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#getMonitoringType()
	 */
	@Override
	public MonitoringType getMonitoringType() {
		return MonitoringType.THREAD_MONITORING;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#getMonitoringMeasureType()
	 */
	@Override
	public MonitoringMeasureType getMonitoringMeasureType() {
		return this.monitoringMeasureType;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#removeTaskAfterMeasure()
	 */
	@Override
	public boolean removeTaskAfterMeasure() {
		return false;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#getTaskDescription()
	 */
	@Override
	public String getTaskDescription() {
		return "Monitoring of agent " + this.agentName + "(" + this.agentClass + ")";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object objectToCompare) {
		if (objectToCompare instanceof SingleAgentMonitor) {
			SingleAgentMonitor sam = (SingleAgentMonitor) objectToCompare;
			return sam.getTaskDescription().equals(this.getTaskDescription());
		}
		return false;
	}
	
}
