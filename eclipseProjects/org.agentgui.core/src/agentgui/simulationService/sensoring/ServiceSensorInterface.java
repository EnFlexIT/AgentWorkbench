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
import jade.core.Location;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.behaviour.SimulationServiceBehaviour;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This interface is the super class for connections to the {@link ServiceSensor}.
 * It is used in the {@link SimulationAgent} or in the {@link SimulationServiceBehaviour} 
 * in order to connect to the {@link SimulationService} and its sensor system.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ServiceSensorInterface {

	/**
	 * This method will be used by the ServiceActuator (class) to inform
	 * this agent about its new migration location.
	 *
	 * @param newLocation the new Location for the migration
	 */
	public abstract void setMigration(Location newLocation);

	/**
	 * This method will be used by the ServiceActuator (class) to inform
	 * that the simulation is paused or not.
	 *
	 * @param isPauseSimulation the new pause simulation
	 */
	public abstract void setPauseSimulation(boolean isPauseSimulation);
	
	/**
	 * This method will be used by the {@link ServiceActuator} to inform
	 * this agent about changes in the environment. It can be either used
	 * to do this asynchronously or synchronously. It is highly recommended 
	 * to do this asynchronously, so that the agency can act parallel and not
	 * sequentially.
	 *
	 * @param envModel the current or new EnvironmentModel
	 * @param aSynchron true, if this should be done asynchronously
	 */
	public abstract void setEnvironmentModel(EnvironmentModel envModel, boolean aSynchron);

	/**
	 * This method can be invoked from the simulation service, if
	 * a notification for the manager has to be delivered.
	 *
	 * @param notification the new notification
	 */
	public abstract void setNotification(EnvironmentNotification notification);

	/**
	 * Will delete/kill the current agent.
	 */
	public abstract void doDelete();

	/**
	 * Returns the AID of the current Agent.
	 * @return the AID
	 */
	public abstract AID getAID();
	
}