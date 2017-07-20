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

import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.core.Location;

/**
 * This is the class for the so called ServiceSensor, which is an attribute of
 * the {@link SimulationAgent}. It transfers information about new environment 
 * models, notifications in relation to the environment, the location where an
 * agent has to migrate and that a agent will be shut down.  
 * 
 * @see SimulationAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServiceSensor {

	private ServiceSensorInterface myServiceSensor;
	
	/**
	 * Instantiates a new service sensor.
	 * @param serviceSensor the service sensor
	 */
	public ServiceSensor(ServiceSensorInterface serviceSensor) {
		myServiceSensor = serviceSensor;		
	}
	
	/**
	 * Gets the current service sensor.
	 * @return the service sensor
	 */
	public ServiceSensorInterface getServiceSensor() {
		return myServiceSensor;
	}
	
	/**
	 * Puts a new environment model to the agent.
	 * @param environmentModel the environment model
	 * @param aSynchron true, if synchronously
	 */
	public void putEnvironmentModel(EnvironmentModel environmentModel, boolean aSynchron) {
		myServiceSensor.setEnvironmentModel(environmentModel, aSynchron);
	}
	
	/**
	 * Notifies an agent about things, taht happen in the environment.
	 * @param notification the EnvironmentNotification
	 */
	public void notifyAgent(EnvironmentNotification notification) {
		myServiceSensor.setNotification(notification);
	}

	/**
	 * Does the <code>doDelete()</code> method of the agent.
	 */
	public void doDelete() {
		myServiceSensor.doDelete();
	}
	
	/**
	 * Sets that the simulation is to be paused or not.
	 * @param isPauseSimulation the new pause simulation
	 */
	public void setPauseSimulation(boolean isPauseSimulation ) {
		myServiceSensor.setPauseSimulation(isPauseSimulation);
	}
	
	/**
	 * Puts the Location to the agent, where the agent has to migrate to.
	 * @param newLocation the new location
	 */
	public void putMigrationLocation(Location newLocation) {
		myServiceSensor.setMigration(newLocation);
	}

	
}
