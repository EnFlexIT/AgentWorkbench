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
package agentgui.simulationService.sensoring;

import jade.core.AID;

import java.util.Hashtable;

import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This is the class for the so called ServiceSensorManager, which is an attribute 
 * of the {@link SimulationManagerAgent}. It transfers information about environment 
 * changes caused by {@link SimulationAgent} and notifications in relation to the 
 * environment.  
 * 
 * @see SimulationManagerAgent 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServiceSensorManager {

	protected SimulationManagerAgent myManager;
	
	/**
	 * Instantiates a new service sensor manager.
	 * @param agent the agent
	 */
	public ServiceSensorManager(SimulationManagerAgent agent) {
		myManager = agent;		
	}
	
	/**
	 * Puts the agent answers to the manager.
	 *
	 * @param agentAnswers the agent answers
	 * @param aSynchron true, if synchronously
	 */
	public void putAgentAnswers(Hashtable<AID, Object> agentAnswers, boolean aSynchron) {
		myManager.putAgentAnswers(agentAnswers, aSynchron);
	}
	
	/**
	 * Notifies a manager agent about things that happen in the environment.
	 * @param notification the notification
	 */
	public void notifyManager(EnvironmentNotification notification) {
		myManager.setManagerNotification(notification);
	}

	/**
	 * Notifies the manager to pause the simulation.
	 * @param isPauseSimulation the is pause simulation
	 */
	public void notifyPauseSimulation(boolean isPauseSimulation) {
		myManager.setPauseSimulation(isPauseSimulation);
	}
	
}
