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

import java.util.HashMap;
import java.util.Vector;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This is the class for an actuator of the {@link SimulationService}, which is able to 
 * inform all connected {@link SimulationAgent} with an integrated {@link ServiceSensor} 
 * about a new {@link EnvironmentModel} or with an {@link EnvironmentNotification}.  
 * Furthermore it can inform the connected agents about the {@link Location}, where they
 * have to migrate, delegated by the load balancing process, or that they just have to die. 
 * <br><br>
 * The functionalities are not finalised yet.
 * 
 * @see SimulationService
 * @see SimulationAgent
 * @see ServiceSensor
 * @see EnvironmentModel
 * @see EnvironmentNotification
 * @see Location
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServiceActuator {

	private Vector<ServiceSensor> serviceSensors = new Vector<ServiceSensor>();
	private Vector<ServiceSensor> serviceSensorsPassive = new Vector<ServiceSensor>();

	private ServiceSensor[] serviceSensorArray = null;
	private HashMap<String, ServiceSensor> sensorSearchHash = null;
	
	
	/**
	 * Method for agents to plug-in to this actuator.
	 * @param currSensor the ServiceSensor to plug-in
	 */
	public void plugIn(ServiceSensor currSensor) {
		serviceSensors.addElement(currSensor);		
	}
	/**
	 * Method for agents to plug-in to this actuator, but it is not 
	 * expected that the connected agent will answer to the change
	 * of the environment.
	 * @param currSensor the ServiceSensor to plug-in
	 */
	public void plugInPassive(ServiceSensor currSensor) {
		serviceSensors.addElement(currSensor);
		serviceSensorsPassive.addElement(currSensor);
	}
	/**
	 * Method to plug-out from this actuator
	 * @param currSensor the ServiceSensor to plug-out
	 */
	public void plugOut(ServiceSensor currSensor) {
		serviceSensors.removeElement(currSensor);
		if (serviceSensorsPassive.contains(currSensor)) {
			serviceSensorsPassive.removeElement(currSensor);
		}
	}
	
	/**
	 * This method returns the instance of the ServiceSensor identified by the AID of the agent.
	 * @param aid the AID of the agent
	 * @return the ServiceSensor
	 */
	public ServiceSensor getSensor(AID aid) {
		String searchFor = aid.getLocalName();
		if (this.sensorSearchHash==null || this.sensorSearchHash.size()!=this.getServiceSensorArray().length) {
			this.sensorSearchHash = new HashMap<String, ServiceSensor>();
			ServiceSensor[] sensors = this.getServiceSensorArray();
			for (int i=0; i<sensors.length; i++) {
				this.sensorSearchHash.put(sensors[i].getServiceSensor().getAID().getLocalName(), sensors[i]);
			}
		}
		return this.sensorSearchHash.get(searchFor);
	}
	
	/**
	 * Returns all registered ServiceSensor arrays.
	 * @return the sensors
	 */
	public ServiceSensor[] getServiceSensorArray() {
		if (this.serviceSensorArray==null || this.serviceSensors.size()!=serviceSensorArray.length) {
			this.serviceSensorArray = new ServiceSensor[this.serviceSensors.size()];
			this.serviceSensors.toArray(this.serviceSensorArray);
		}
		return serviceSensorArray;
	}
	
	/**
	 * Returns all agents registered to this actuator by a sensor.
	 * @return the sensor agents
	 */
	public AID[] getSensorAgents() {
		ServiceSensor[] sensors = this.getServiceSensorArray();
		AID[] sensorAgents = new AID[sensors.length];
		for (int i = 0; i < sensors.length; i++) {
			sensorAgents[i] = sensors[i].getServiceSensor().getAID();
		}
		return sensorAgents;
	}
	
	/**
	 * Returns the number of simulation answers expected.
	 * @return the noOfSimulationAnswersExpected
	 */
	public int getNoOfSimulationAnswersExpected() {
		return serviceSensors.size() - serviceSensorsPassive.size();
	}
		
	/**
	 * This method informs all Sensors about the new environment model.
	 * It can be either used to do this asynchronously or asynchronously, but it.
	 * is highly recommended to do this asynchronously, so that the agency
	 * can act parallel and not sequential.
	 *
	 * @param currEnvironmentModel the current environment model
	 * @param aSynchron true, if this should be don asynchronously
	 */
	public void notifySensors(final EnvironmentModel currEnvironmentModel, final boolean aSynchron) {
		final ServiceSensor[] sensors = getServiceSensorArray();
		Runnable notifier = new Runnable() {
			@Override
			public void run() {
				for (int i = sensors.length-1; i>=0; i--) {
					sensors[i].putEnvironmentModel(currEnvironmentModel, aSynchron);
				}
			}
		};
		notifier.run();		
	}

	/**
	 * Notify an agent through the sensor.
	 *
	 * @param aid the aid
	 * @param notification the notification
	 * @return true, if successful
	 */
	public boolean notifySensorAgent(AID aid, EnvironmentNotification notification) {
		ServiceSensor sensor = getSensor(aid);
		if (sensor==null) {
			// --- Agent/Sensor was NOT found ---
			return false;
		} else {
			// --- Agent/Sensor was found -------
			sensor.notifyAgent(notification);
			return true;
		}		
	}

	/**
	 * This method will kill all registered SimulationAgents
	 * to provide a faster(!) shut-down of the system.
	 */
	public void notifySensorAgentsDoDelete() {
		final ServiceSensor[] sensors = getServiceSensorArray();
		Runnable notifier = new Runnable() {
			@Override
			public void run() {
				for (int i = sensors.length-1; i>=0; i--) {
					sensors[i].doDelete();
				}
			}
		};
		notifier.run();		
	}
	
	/**
	 * Notify that simulation has to be paused or not.
	 * @param isPauseSimulation the is pause simulation
	 */
	public void notifySensorPauseSimulation(final boolean isPauseSimulation) {
		final ServiceSensor[] sensors = getServiceSensorArray();
		Runnable notifier = new Runnable() {
			@Override
			public void run() {
				for (int i = sensors.length-1; i>=0; i--) {
					sensors[i].setPauseSimulation(isPauseSimulation);
				}
			}
		};
		notifier.run();		
	}
	
	/**
	 * This method will place a 'newLocation'-Object to every agent which is registered to this actuator.
	 * @param transferAgents the Vector of agents to migrate
	 */
	public void setMigration(final Vector<AID_Container> transferAgents) {

		Runnable notifier = new Runnable() {
			@Override
			public void run() {
				// --- Filter these agents, which are located here ----------
				Object[] arrTransfer = transferAgents.toArray();
				for (int i = arrTransfer.length-1; i>=0; i--) {
					AID aid = ((AID_Container)arrTransfer[i]).getAID();
					Location newLocation = ((AID_Container)arrTransfer[i]).getNewLocation();
					ServiceSensor sensorAgent = getSensor(aid); 
					if (sensorAgent!=null) {
						sensorAgent.putMigrationLocation(newLocation);
					}
				}
			}
		};
		notifier.run();	
	}

}
