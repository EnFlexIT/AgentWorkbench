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
package agentgui.simulationService.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Vector;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.sensoring.ServiceActuator;
import agentgui.simulationService.sensoring.ServiceSensor;
import agentgui.simulationService.sensoring.ServiceSensorInterface;
import agentgui.simulationService.sensoring.ServiceSensorListener;
import agentgui.simulationService.transaction.DisplayAgentNotification;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * The Class SimulationServiceBehaviour can be extended in order to use the
 * functionality of the {@link SimulationService} in a behaviour instead of
 * the usage through {@link SimulationAgent}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class SimulationServiceBehaviour extends Behaviour implements ServiceSensorInterface {

	private static final long serialVersionUID = 1782853782362543057L;

	private boolean passive = false;
	private boolean done = false; 
	
	/** The ServiceSensor of this agent. */
	protected ServiceSensor mySensor;
	/** The current EnvironmentModel. */
	protected EnvironmentModel myEnvironmentModel;
	
	private CyclicNotificationHandler notificationHandler = null;
	private Vector<EnvironmentNotification> notifications = new Vector<EnvironmentNotification>();

	private Vector<ServiceSensorListener> simulationServiceListeners = null;
	
	
	/**
	 * Instantiates a new SimulationServiceBehaviour as an active agent, which
	 * means that the current agent is asked to react on notifications or environment changes.
	 * @param agent the agent
	 */
	public SimulationServiceBehaviour(Agent agent) {
		super(agent);
	}
	
	/**
	 * Instantiates a new simulation agent as a passive agent.
	 * The agent will just listening to changes in the environment model
	 * but it is not expected, that it will react on it. This is used
	 * for example for agents, which are displaying the current environment.
	 *
	 * @param agent the agent
	 * @param passive the passive
	 */
	public SimulationServiceBehaviour(Agent agent, boolean passive) {
		super(agent);
		this.passive = passive;
	}
	
	/**
	 * Gets the current agent.
	 * @return the agent
	 */
	public Agent getAgent() {
		return myAgent;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		this.block(5000);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.sensoring.ServiceSensorInterface#getAID()
	 */
	public AID getAID() {
		return this.myAgent.getAID();
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.sensoring.ServiceSensorInterface#doDelete()
	 */
	@Override
	public void doDelete() {
		this.done=true;
		this.removeNotificationHandler();
		this.sensorPlugOut();
		this.myAgent.doDelete();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		return done;
	}
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#onStart()
	 */
	@Override
	public void onStart() {
		this.sensorPlugIn();
		this.addNotificationHandler();
	}
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#onEnd()
	 */
	@Override
	public int onEnd() {
		this.removeNotificationHandler();
		this.sensorPlugOut();
		return super.onEnd();
	}
	
	/**
	 * This Method plugs IN the service sensor.
	 */
	protected void sensorPlugIn() {
		// --- Start the ServiceSensor ------------------------------
		this.mySensor = new ServiceSensor(this);
		// --- Register the sensor to the SimulationService ---------
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) this.myAgent.getHelper(SimulationService.NAME);
			if (this.passive==true) {
				simHelper.sensorPlugIn(mySensor, true);
			} else {
				simHelper.sensorPlugIn(mySensor);	
			}
				
		} catch (ServiceException ae) {
			System.err.println("Agent '" + this.myAgent.getLocalName() + "': Could not plugin simulated sensor!");
//			se.printStackTrace();
		}
	}
	
	/**
	 * This Method plugs OUT the service sensor.
	 */
	protected void sensorPlugOut() {
		// --- plug-out the Sensor ----------------------------------
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) this.myAgent.getHelper(SimulationService.NAME);
			simHelper.sensorPlugOut(mySensor);	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		mySensor = null;
	}
	/**
	 * This Method checks if the environment changed in the meantime.
	 * If so, the method 'onEnvironmentStimulus' will be fired
	 */
	protected void checkAndActOnEnvironmentChanges() {
		// --- Has the EnvironmentModel changed? ----------------
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) this.myAgent.getHelper(SimulationService.NAME);
			EnvironmentModel tmpEnvMode =  simHelper.getEnvironmentModel();
			if (tmpEnvMode!=null) {
				if (tmpEnvMode.equals(myEnvironmentModel)==false) {
					this.setEnvironmentModel(tmpEnvMode, false);
				}				
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will be used by the ServiceActuator (class) to inform
	 * this agent about its new migration location.
	 *
	 * @param newLocation the new Location for the migration
	 */
	public abstract void setMigration(Location newLocation);
	
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
	public void setEnvironmentModel(EnvironmentModel envModel, boolean aSynchron) {
		myEnvironmentModel = envModel;
		if (aSynchron==true) {
			this.myAgent.addBehaviour(new ServiceStimulus());	
		} else {
			this.onEnvironmentStimulusIntern();	
		}		
	}
	
		
	/**
	 * This Behaviour is used to stimulate the agent from the outside 
	 * in a asynchronous way.
	 * 	 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class ServiceStimulus extends OneShotBehaviour {
		private static final long serialVersionUID = 1441989543791055996L;
		@Override
		public void action() {
			onEnvironmentStimulusIntern();
		}
	}
	/**
	 * This method is internally called if a stimulus from the outside reached this agent.
	 */
	private void onEnvironmentStimulusIntern() {
		this.onEnvironmentStimulus();
		for(ServiceSensorListener listener : this.getSimulationServiceListeners()) {
			listener.onEnvironmentStimulus();
		}
	}
	/**
	 * This method is called if a stimulus from the outside reached this agent.
	 * It can be overwritten in the child class to act on environment changes. 
	 */
	public abstract void onEnvironmentStimulus();
	
	
	/**
	 * This method sets the answer respectively the change of a single simulation agent
	 * back to the central simulation manager.
	 *
	 * @param myNextState the next state of this agent in the next instance of the environment model
	 */
	protected void setMyStimulusAnswer(Object myNextState) {
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) this.myAgent.getHelper(SimulationService.NAME);
			simHelper.setEnvironmentInstanceNextPart(this.myAgent.getAID(), myNextState);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method can be used to transfer any kind of information to the Manager of the current environment model.
	 *
	 * @param notification the notification
	 * @return true, if successful
	 */
	public boolean sendManagerNotification(Object notification) {
		boolean send = false;
		EnvironmentNotification myNotification = new EnvironmentNotification(this.myAgent.getAID(), false, notification);
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) this.myAgent.getHelper(SimulationService.NAME);
			send = simHelper.notifyManagerAgent(myNotification);	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return send;
	}
	
	/**
	 * This method can be used to transfer any kind of information to one member of the current environment model.
	 *
	 * @param receiverAID the AID of receiver agent
	 * @param notification the notification
	 * @return true, if successful
	 */
	public boolean sendAgentNotification(AID receiverAID, Object notification) {
		boolean send = false;
		EnvironmentNotification myNotification = new EnvironmentNotification(this.myAgent.getAID(), false, notification);
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) this.myAgent.getHelper(SimulationService.NAME);
			send = simHelper.notifySensorAgent(receiverAID, myNotification);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return send;
	}
	
	/**
	 * Notify display agents about changes with a {@link DisplayAgentNotification}.
	 * 
	 * @param displayAgentNotification the display agent message
	 */
	public void sendDisplayAgentNotification(DisplayAgentNotification displayAgentNotification) {
		try {
			EnvironmentNotification notification = new EnvironmentNotification(getAID(), true, displayAgentNotification);
			SimulationServiceHelper simHelper = (SimulationServiceHelper) this.myAgent.getHelper(SimulationService.NAME);
			simHelper.displayAgentNotification(notification);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method can be invoked from the simulation service, if
	 * a notification for the manager has to be delivered.
	 *
	 * @param notification the new notification
	 */
	public void setNotification(EnvironmentNotification notification) {
		// --- place the notification into the notification vector -------
		synchronized (this.notifications) {
			this.notifications.add(notification);	
		}
		// --- restart the CyclicNotificationHandler ---------------------
		if (this.notificationHandler==null) {
			this.addNotificationHandler();
		} else {
			this.notificationHandler.restart();	
		}
			
	}
	/**
	 * This method adds the CyclicNotificationHandler to this agent.
	 */
	private void addNotificationHandler() {
		if (this.notificationHandler==null) {
			this.notificationHandler = new CyclicNotificationHandler();	
		}		
		this.myAgent.addBehaviour(this.notificationHandler);
	}
	/**
	 * This method removes the CyclicNotificationHandler from this agent.
	 */
	private void removeNotificationHandler() {
		if (this.notificationHandler!=null) {
			this.myAgent.removeBehaviour(this.notificationHandler);	
		}
	}

	/**
	 * This CyclicBehaviour is used in order to act on the incoming notifications.
	 *  
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class CyclicNotificationHandler extends CyclicBehaviour {
		
		private static final long serialVersionUID = 4638681927192305608L;
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			
			EnvironmentNotification notification = null;
			boolean removeFirstElement = false;
			boolean moveAsLastElement = false;
			
			// --- Get the first element and work on it ------------------
			if (notifications.size()!=0) {
				notification = notifications.get(0);
				notification = onEnvironmentNotificationIntern(notification);
				if (notification.getProcessingInstruction().isDelete()) {
					removeFirstElement = true;	
					moveAsLastElement = false;
					
				} else if (notification.getProcessingInstruction().isBlock()) {
					removeFirstElement = false;
					moveAsLastElement = false;
					this.block(notification.getProcessingInstruction().getBlockPeriod());
					
				} 
				if (notification.getProcessingInstruction().isMoveLast()) {
					removeFirstElement = false;
					moveAsLastElement = true;
				}
			}
			
			// --- remove this element and control the notifications -----
			synchronized (notifications) {
				if (removeFirstElement==true) {
					notifications.remove(0);
				}
				if (moveAsLastElement==true) {
					if (notifications.size()>1) {
						notifications.remove(0);
						notifications.add(notification);
					} else {
						this.block(notification.getProcessingInstruction().getBlockPeriod());	
					}
				}
				if (notification!=null) {
					notification.resetProcessingInstruction();	
				}
				if (notifications.size()==0) {
					block();
				}
			}
			
		}
	}
	
	/**
	 * This method will be executed if a ManagerNotification arrives this agent.
	 * @param notification the notification
	 */
	private EnvironmentNotification onEnvironmentNotificationIntern(EnvironmentNotification notification){
		notification = this.onEnvironmentNotification(notification);
		for(ServiceSensorListener listener : this.getSimulationServiceListeners()) {
			notification = listener.onEnvironmentNotification(notification);
		}
		return notification;
	}
	/**
	 * This method will be executed if a ManagerNotification arrives this agent.
	 * @param notification the notification
	 */
	protected EnvironmentNotification onEnvironmentNotification(EnvironmentNotification notification){
		return notification;
	}

	
	/**
	 * Returns the current simulation service listeners.
	 * @return the simulation service listeners
	 */
	private Vector<ServiceSensorListener> getSimulationServiceListeners() {
		if (this.simulationServiceListeners==null) {
			this.simulationServiceListeners = new Vector<ServiceSensorListener>();
		}
		return this.simulationServiceListeners;
	}
	/**
	 * Adds the simulation service listener.
	 * @param simulationServiceListener the simulation service listener
	 */
	public void addSimulationServiceListener(ServiceSensorListener simulationServiceListener) {
		this.getSimulationServiceListeners().add(simulationServiceListener);
	}
	/**
	 * Removes the simulation service listener.
	 * @param simulationServiceListener the simulation service listener
	 */
	public void removeSimulationServiceListener(ServiceSensorListener simulationServiceListener) {
		this.getSimulationServiceListeners().remove(simulationServiceListener);
	}

	/**
	 * Registers a service for the agency to the DFService of JADE.
	 * @param type the type
	 * @param name the name
	 */
	protected void registerDFService(String type, String name, String ownership) {
		DFAgentDescription agentDescription = createAgentDescription(type, name, ownership);
		try {
			DFService.register(this.myAgent, agentDescription);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	/**
	 * Unregisters a service for the agency to the DFService of JADE.
	 */
	protected void deregisterDFService() {
		try {
			DFService.deregister(this.myAgent);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	/**
	 * Returns the Agent description for the DF-Service of JADE
	 * @param type the service type
	 * @param name AID address or name of the agent
	 * @param ownership the ownership
	 * @return the DFAgentDescription for the JADE DF 
	 */
	private DFAgentDescription createAgentDescription(String type, String name, String ownership) {
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType(type);
		serviceDescription.setName(name);
		serviceDescription.setOwnership(ownership);
		agentDescription.addServices(serviceDescription);
		
		return agentDescription;
	}
	
	/**
	 * Find and returns agents by a service type.
	 *
	 * @param serviceType the service type
	 * @return the dF agent description[]
	 */
	protected DFAgentDescription[] findAgentsByServiceType(String serviceType) {
	
		DFAgentDescription[] dfAgentDescriptions = null;
		
		try {
			DFAgentDescription agentDescription = new DFAgentDescription();
			ServiceDescription serviceDescription = new ServiceDescription();
			serviceDescription.setType(serviceType);
			agentDescription.addServices(serviceDescription);

			dfAgentDescriptions = DFService.search(this.myAgent, agentDescription);
			
		} catch (FIPAException fe) {
			//fe.printStackTrace();
			System.err.println("DFService: Error while requesting the DFService!");
		}
		return dfAgentDescriptions;
	}

	
}
