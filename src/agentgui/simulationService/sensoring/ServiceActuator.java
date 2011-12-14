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
	
	private MigrationVector migrationList = new MigrationVector();
	private MigrationVector migrationListInMovement = new MigrationVector();
	private int migrationsParallelMax = 25;
	private int migrationsRestartAt = 10;
	
	
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
	}
	
	/**
	 * This method returns the instance of the ServiceSensor identified by the AID of the agent.
	 * @param aid the AID of the agent
	 * @return the ServiceSensor
	 */
	public ServiceSensor getSensor(AID aid) {
		
		Object[] arrLocal = serviceSensors.toArray();
		for (int i = arrLocal.length-1; i>=0; i--) {
			ServiceSensor sensor = (ServiceSensor)arrLocal[i];
			AID sensorAgentAID = sensor.myAgent.getAID();
			if (sensorAgentAID.equals(aid)) {
				return sensor;				
			}
		}
		return null;
	}
	
	/**
	 * Returns all agents registered to this actuator by a sensor.
	 * @return the sensor agents
	 */
	public AID[] getSensorAgents() {
		AID[] sensorAgents = new AID[serviceSensors.size()];
		Object[] arrLocal = serviceSensors.toArray();
		for (int i = arrLocal.length-1; i>=0; i--) {
			AID aid = ((ServiceSensor)arrLocal[i]).myAgent.getAID();
			sensorAgents[i] = aid;
		}
		return sensorAgents;
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
		
		Runnable notifier = new Runnable() {
			@Override
			public void run() {
				Object[] arrLocal = serviceSensors.toArray();
				for (int i = arrLocal.length-1; i>=0; i--) {
					((ServiceSensor)arrLocal[i]).putEnvironmentModel(currEnvironmentModel, aSynchron);
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
		
		Runnable notifier = new Runnable() {
			@Override
			public void run() {
				Object[] arrLocal = serviceSensors.toArray();
				for (int i = arrLocal.length-1; i>=0; i--) {
					((ServiceSensor)arrLocal[i]).doDelete();
				}
			}
		};
		notifier.run();		
	}
	
	/**
	 * Returns the number of simulation answers expected.
	 * @return the noOfSimulationAnswersExpected
	 */
	public int getNoOfSimulationAnswersExpected() {
		return serviceSensors.size() - serviceSensorsPassive.size();
	}

	/**
	 * This method will place a 'newLocation'-Object to every agent which is registered to this actuator.
	 * @param transferAgents the Vector of agents to migrate
	 */
	public void setMigration(Vector<AID_Container> transferAgents) {

		// --- Filter these agents, which are located here ----------
		Object[] arrTransfer = transferAgents.toArray();
		for (int i = arrTransfer.length-1; i>=0; i--) {
			AID aid = ((AID_Container)arrTransfer[i]).getAID();
			Location newLocation = ((AID_Container)arrTransfer[i]).getNewLocation();
			ServiceSensor sensorAgent = this.getSensor(aid); 
			if (sensorAgent!=null) {
				sensorAgent.putMigrationLocation(newLocation);
				
				//MigrationReminder migRem = new MigrationReminder(sensorAgent, newLocation);
				//migrationList.addElement(migRem);
			}
		}
		
		// --- Provide the migration information to the first agents, bounded to
		// --- the number, defined through 'migrationsParallelMax'
		//this.putMigration();
	}

	/**
	 * Provide the migration information to the first agents, bounded to
	 * the number, defined through 'migrationsParallelMax'
	 */
	public void putMigration() {
		
		// --- Fill InMovement-list with Sensors from the 'migrationStore' ----
		while (this.migrationListInMovement.size() < this.migrationsParallelMax) {
			
			if (migrationList.size()==0) {
				break;
			}
			migrationListInMovement.addElement(migrationList.get(0));
			migrationList.get(0).sensor.putMigrationLocation(migrationList.get(0).getSensorAgentsNewLocation());
			migrationList.remove(0);
		}
	}
	
	/**
	 * If the migration of an agent is completed, the SimulationsService informs
	 * the local ServiceActuator by using this method about the.
	 * @param aidMigrated the new agent migrated
	 */
	public void setAgentMigrated(AID aidMigrated) {
		
//		MigrationReminder sensorAgentMigrated = migrationListInMovement.getMigrationReminder(aidMigrated);
//		if (sensorAgentMigrated!=null) {
//			migrationListInMovement.removeElement(sensorAgentMigrated);
//			System.out.println(aidMigrated.getLocalName() + " found! " + sensorAgentMigrated.toString() + " removed! Rest: " + migrationListInMovement.size());
//			sensorAgentMigrated = null;
//		}
//		
//		if (migrationListInMovement.size()<=migrationsRestartAt) {
//			this.putMigration();
//		}
	}
	
	
	
	private class MigrationVector extends Vector<MigrationReminder> {
		
		private static final long serialVersionUID = 1794425375640471823L;
		
		public MigrationReminder getMigrationReminder(AID searchAID) {
			
			for(int i=0; i<this.size();i++) {
				MigrationReminder migRem = this.get(i);
				AID agentsAID = migRem.getSensor().myAgent.getAID();
				if (agentsAID.getLocalName().equals(searchAID.getLocalName())) {
					return this.get(i);
				}
			}
			return null;
		}
	}
	
	private class MigrationReminder {
		
		private ServiceSensor sensor = null;
		private Location sensorAgentsNewLocation = null;
		
		public MigrationReminder(ServiceSensor sensor, Location newLocation) {
			this.setSensor(sensor);
			this.setSensorAgentsNewLocation(newLocation);
		}
		/**
		 * @param sensorAgent the sensorAgent to set
		 */
		public void setSensor(ServiceSensor sensor) {
			this.sensor = sensor;
		}
		/**
		 * @return the sensorAgent
		 */
		public ServiceSensor getSensor() {
			return sensor;
		}
		/**
		 * @param sensorAgentsNewLocation the sensorAgentsNewLocation to set
		 */
		public void setSensorAgentsNewLocation(Location sensorAgentsNewLocation) {
			this.sensorAgentsNewLocation = sensorAgentsNewLocation;
		}
		/**
		 * @return the sensorAgentsNewLocation
		 */
		public Location getSensorAgentsNewLocation() {
			return sensorAgentsNewLocation;
		}
	}

}
