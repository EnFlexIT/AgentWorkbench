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
public class ServiceActuator extends Thread {

	private enum Job {
		NotifySensors,
		NotifySensorAgentsDoDelete,
		NotifySensorPauseSimulation, 
		SetMigration
	}
	
	private Vector<ActuatorJob> jobVector;
	
	private boolean shutdownActuator;
	
	private Vector<ServiceSensor> serviceSensors;
	private Vector<ServiceSensor> serviceSensorsPassive;

	private ServiceSensor[] serviceSensorArray;
	private HashMap<String, ServiceSensor> sensorSearchHash;

	
	/**
	 * Instantiates a new service actuator.
	 * @param containerName the container name
	 */
	public ServiceActuator(String containerName) {
		this.setName(containerName + "Actuator");
		this.start();
	}
	
	/**
	 * Gets the job vector.
	 * @return the job vector
	 */
	private Vector<ActuatorJob> getJobVector() {
		if (jobVector==null) {
			jobVector = new Vector<ActuatorJob>();
		}
		return jobVector;
	}
	/**
	 * Adds a job to the local jobVector and interrupts the actuator thread.
	 * @param job the job
	 * @param arguments the arguments
	 */
	private void addJob(Job job, Object[] arguments) {
		this.addJob(new ActuatorJob(job, arguments));
	}
	/**
	 * Adds the specified job to the local jobVector and interrupts the actuator thread.
	 * @param actuatorJob the ActuatorJob to do
	 */
	private void addJob(ActuatorJob actuatorJob) {
		synchronized (this.getJobVector()) {
			this.getJobVector().add(actuatorJob);	
		}
		this.interrupt();
	}
	/**
	 * Removes a job from the local jobVector and returns it as the next job.
	 * @param job the job
	 */
	private ActuatorJob getNextJob() {
		synchronized (this.getJobVector()) {
			if (this.getJobVector().size()!=0) {
				return this.getJobVector().remove(0);	
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		while (this.shutdownActuator==false) {
		
			try {
				// --- Get the next job, if any -----------
				ActuatorJob actuatorJob = this.getNextJob();
				if (actuatorJob!=null) {
					// --- Get Job and arguments ----------
					Job job = actuatorJob.getJob();
					Object[] arg = actuatorJob.getArguments();
					// --- Job case separation ------------
					switch (job) {
					case NotifySensors:
						this.notifySensorsJob((EnvironmentModel)arg[0], (Boolean)arg[1]);
						break;

					case NotifySensorAgentsDoDelete:
						this.notifySensorAgentsDoDeleteJob();
						break;
						
					case NotifySensorPauseSimulation:
						this.notifySensorPauseSimulationJob((Boolean)arg[0]);
						break;
					
					case SetMigration:
						this.setMigrationJob((Vector<AID_Container>)arg[0]);
						break;
					}
					
				}
				Thread.sleep(1000 * 10);
				
			} catch (InterruptedException ie) {
				// ie.printStackTrace();
			}
			
		} // --- end while ----
		
	}
	/**
	 * Manages to shutdown the Thread of this ServiceActuator.
	 */
	public void shutdown() {
		this.shutdownActuator = true;
	}
	
	/**
	 * Returns the Vector of {@link ServiceSensor}.
	 * @return the service sensors
	 */
	private Vector<ServiceSensor> getServiceSensors() {
		if (serviceSensors==null) {
			serviceSensors = new Vector<ServiceSensor>();
		}
		return serviceSensors;
	}
	/**
	 * Returns the Vector of {@link ServiceSensor} that are just passively 
	 * listen to environment changes and which are not react.
	 * @return the service sensors passive
	 */
	private Vector<ServiceSensor> getServiceSensorsPassive() {
		if (serviceSensorsPassive==null) {
			serviceSensorsPassive = new Vector<ServiceSensor>();
		}
		return serviceSensorsPassive;
	}
	
	/**
	 * Method for agents to plug-in to this actuator.
	 * @param currSensor the ServiceSensor to plug-in
	 */
	public void plugIn(ServiceSensor currSensor) {
		this.getServiceSensors().addElement(currSensor);		
	}
	/**
	 * Method for agents to plug-in to this actuator, but it is not 
	 * expected that the connected agent will answer to the change
	 * of the environment.
	 * @param currSensor the ServiceSensor to plug-in
	 */
	public void plugInPassive(ServiceSensor currSensor) {
		this.getServiceSensors().addElement(currSensor);
		this.getServiceSensorsPassive().addElement(currSensor);
	}
	/**
	 * Method to plug-out from this actuator
	 * @param currSensor the ServiceSensor to plug-out
	 */
	public void plugOut(ServiceSensor currSensor) {
		this.getServiceSensors().removeElement(currSensor);
		if (this.getServiceSensorsPassive().contains(currSensor)) {
			this.getServiceSensorsPassive().removeElement(currSensor);
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
	private ServiceSensor[] getServiceSensorArray() {
		if (this.serviceSensorArray==null || this.getServiceSensors().size()!=serviceSensorArray.length) {
			this.serviceSensorArray = new ServiceSensor[this.getServiceSensors().size()];
			this.getServiceSensors().toArray(this.serviceSensorArray);
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
		return this.getServiceSensors().size() - this.getServiceSensorsPassive().size();
	}
	
	/**
	 * Notify an agent through the sensor.
	 *
	 * @param aid the aid
	 * @param notification the notification
	 * @return true, if successful
	 */
	public boolean notifySensorAgent(AID aid, EnvironmentNotification notification) {
		ServiceSensor sensor = this.getSensor(aid);
		if (sensor==null) {
			// --- Agent/Sensor was NOT found ---
			return false;
		} else {
			// --- Agent/Sensor was found -------
			sensor.notifyAgent(notification);
			return true;
		}		
	}
	
	// ------------------------------------------------------------------------
	// --- Thread Jobs ---------------------------------------------- Start ---
	// ------------------------------------------------------------------------
	/**
	 * This method informs all Sensors about the new environment model.
	 * It can be either used to do this asynchronously or asynchronously, but it.
	 * is highly recommended to do this asynchronously, so that the agency
	 * can act parallel and not sequential.
	 *
	 * @param environmentModel the new {@link EnvironmentModel}
	 * @param async set true, if this should be done asynchronously
	 */
	public void notifySensors(EnvironmentModel environmentModel, boolean async) {
		Object[] arg = new Object[2];
		arg[0] = environmentModel;
		arg[1] = async;
		this.addJob(Job.NotifySensors, arg);
	}
	/**
	 * Notify sensors job.
	 * @param environmentModel the new {@link EnvironmentModel}
	 * @param async set true, if this should be done asynchronously
	 * @see #notifySensors(EnvironmentModel, boolean)
	 */
	private void notifySensorsJob(EnvironmentModel environmentModel, boolean async) {
		ServiceSensor[] sensors = getServiceSensorArray();
		for (int i = sensors.length-1; i>=0; i--) {
			sensors[i].putEnvironmentModel(environmentModel, async);
		}
	}

	/**
	 * This method will kill all registered SimulationAgents
	 * to provide a faster(!) shutdown of the system.
	 */
	public void notifySensorAgentsDoDelete() {
		this.addJob(Job.NotifySensorAgentsDoDelete, null);
	}
	/**
	 * This method will kill all registered SimulationAgents
	 * to provide a faster(!) shutdown of the system.
	 */
	private void notifySensorAgentsDoDeleteJob() {
		ServiceSensor[] sensors = getServiceSensorArray();
		for (int i = sensors.length-1; i>=0; i--) {
			sensors[i].doDelete();
		}
	}
	
	/**
	 * Notify that simulation has to be paused or not.
	 * @param isPauseSimulation the is pause simulation
	 */
	public void notifySensorPauseSimulation(boolean isPauseSimulation) {
		Object[] arg = new Object[2];
		arg[0] = isPauseSimulation;
		this.addJob(Job.NotifySensorPauseSimulation, arg);
	}
	/**
	 * Notify that simulation has to be paused or not.
	 * @param isPauseSimulation the is pause simulation
	 */
	private void notifySensorPauseSimulationJob(boolean isPauseSimulation) {
		ServiceSensor[] sensors = getServiceSensorArray();
		for (int i = sensors.length-1; i>=0; i--) {
			sensors[i].setPauseSimulation(isPauseSimulation);
		}
	}
	
	/**
	 * This method will place a 'newLocation'-Object to every agent which is registered to this actuator.
	 * @param transferAgents the Vector of agents to migrate
	 */
	public void setMigration(Vector<AID_Container> transferAgents) {
		Object[] arg = new Object[2];
		arg[0] = transferAgents;
		this.addJob(Job.SetMigration, arg);
	}
	/**
	 * This method will place a 'newLocation'-Object to every agent which is registered to this actuator.
	 * @param transferAgents the Vector of agents to migrate
	 */
	public void setMigrationJob(Vector<AID_Container> transferAgents) {

		Object[] arrTransfer = transferAgents.toArray();
		for (int i = arrTransfer.length-1; i>=0; i--) {
			AID aid = ((AID_Container)arrTransfer[i]).getAID();
			Location newLocation = ((AID_Container)arrTransfer[i]).getNewLocation();
			ServiceSensor sensorAgent = this.getSensor(aid); 
			if (sensorAgent!=null) {
				sensorAgent.putMigrationLocation(newLocation);
			}
		}
	}

	// ------------------------------------------------------------------------
	// --- Internal sub class ActuatorJob --------------------------- Start ---
	// ------------------------------------------------------------------------
	/**
	 * The Class ActuatorJob describes a Job that has to be done by the {@link ServiceActuator}.
	 */
	private class ActuatorJob {
		
		private Job job;
		private Object[] arguments;
		
		public ActuatorJob(Job job, Object[] arguments) {
			this.setJob(job);
			this.setArguments(arguments);
		}
		public Job getJob() {
			return job;
		}
		public void setJob(Job job) {
			this.job = job;
		}
		public Object[] getArguments() {
			return arguments;
		}
		public void setArguments(Object[] arguments) {
			this.arguments = arguments;
		}
	}
	
}
