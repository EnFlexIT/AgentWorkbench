package agentgui.simulationService.sensoring;

import jade.core.AID;
import jade.core.Location;

import java.util.Vector;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;


public class ServiceActuator {

	private Vector<ServiceSensor> serviceSensors = new Vector<ServiceSensor>();
	private int noOfSimulationAnswersExpected = 0;
	
	private MigrationVector migrationList = new MigrationVector();
	private MigrationVector migrationListInMovement = new MigrationVector();
	private int migrationsParallelMax = 25;
	private int migrationsRestartAt = 10;
	
	
	/**
	 * returns all agents registered to this actuator by a sensor 
	 * @return
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
	 * Opportunity for the agents to plugIn to this service
	 * @param currSensor
	 */
	public void plugIn(ServiceSensor currSensor) {
		serviceSensors.addElement(currSensor);		
	}
	/**
	 * Method to plugOut from this actuator
	 * @param currSensor
	 */
	public void plugOut(ServiceSensor currSensor) {
		serviceSensors.removeElement(currSensor);
	}
	
	/**
	 * This method informs all Sensors about the new environment model.
	 * It can be either used to do this asynchron or synchron, but it. 
	 * is highly recommended to do this asynchron, so that the agencie 
	 * can act parallel and not sequently. 
	 * @param currEnvironmentModel
	 * @param aSynchron
	 */
	public void notifySensors(EnvironmentModel currEnvironmentModel, boolean aSynchron) {
		Object[] arrLocal = serviceSensors.toArray();
		this.noOfSimulationAnswersExpected = arrLocal.length;
		for (int i = this.noOfSimulationAnswersExpected-1; i>=0; i--) {
			((ServiceSensor)arrLocal[i]).putEnvironmentModel(currEnvironmentModel, aSynchron);
		}
	}
	/**
	 * @param noOfSimulationAnswersExpected the noOfSimulationAnswersExpected to set
	 */
	public void setNoOfSimulationAnswersExpected(
			int noOfSimulationAnswersExpected) {
		this.noOfSimulationAnswersExpected = noOfSimulationAnswersExpected;
	}
	/**
	 * @return the noOfSimulationAnswersExpected
	 */
	public int getNoOfSimulationAnswersExpected() {
		return noOfSimulationAnswersExpected;
	}

	/**
	 * This method returns the ServiceSensor-Instance identified by the AID of the agent
	 * @param aid
	 * @return
	 */
	public ServiceSensor getSensor(AID aid) {
		
		Object[] arrLocal = serviceSensors.toArray();
		for (int i = arrLocal.length-1; i>=0; i--) {
			ServiceSensor sensor = ((ServiceSensor)arrLocal[i]);
			AID sensorAgentAID = sensor.myAgent.getAID();
			if (sensorAgentAID.equals(aid)) {
				return sensor;				
			}
		}
		return null;
	}
	
	/**
	 * This method will place a 'newLocation'-Object to every agent 
	 * which is registered to this actuator 
	 * @param transferAgents
	 */
	public void setMigration(Vector<AID_Container> transferAgents) {

		// --- Filter these agents, which are located here ----------
		Object[] arrTransfer = transferAgents.toArray();
		for (int i = arrTransfer.length-1; i>=0; i--) {
			AID aid = ((AID_Container)arrTransfer[i]).getAID();
			Location newLocation = ((AID_Container)arrTransfer[i]).getNewLocation();
			ServiceSensor sensorAgent = this.getSensor(aid); 
			if (sensorAgent!=null) {
				sensorAgent.putMigrationInfo(newLocation);
				
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
			migrationList.get(0).sensor.putMigrationInfo(migrationList.get(0).getSensorAgentsNewLocation());
			migrationList.remove(0);
		}
	}
	
	/**
	 * If the migration of an agent is completed, the SimulationsService informs
	 * the local ServiceActuator by using this method about the 
	 * @param aidMigrated
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
