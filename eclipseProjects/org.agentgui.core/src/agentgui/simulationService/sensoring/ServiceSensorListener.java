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

import java.io.Serializable;

import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.behaviour.SimulationServiceBehaviour;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This Sensor Listener can be used with the {@link SimulationAgent} or the {@link SimulationServiceBehaviour} class
 * and enables to 
 * 
 * @see SimulationAgent#addSimulationServiceListener(ServiceSensorListener)
 * @see SimulationAgent#removeSimulationServiceListener(ServiceSensorListener)
 * or
 * @see SimulationServiceBehaviour#addSimulationServiceListener(ServiceSensorListener)
 * @see SimulationServiceBehaviour#removeSimulationServiceListener(ServiceSensorListener)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ServiceSensorListener extends Serializable {

	/**
	 * Will be invoked, if the environment (model) changed.
	 */
	public void onEnvironmentStimulus();

	/**
	 * If an environment notification arrives the Agent, this method will be invoked.
	 *
	 * @param notification the notification
	 * @return the environment notification
	 */
	public EnvironmentNotification onEnvironmentNotification(EnvironmentNotification notification);
	
	
}
