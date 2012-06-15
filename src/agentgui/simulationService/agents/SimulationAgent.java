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
package agentgui.simulationService.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

import java.util.Vector;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.sensoring.ServiceActuator;
import agentgui.simulationService.sensoring.ServiceSensor;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This agent class can be used for simulations based on agents 
 * that are using the <b>Agent.GUI</b> {@link SimulationService}.
 * 
 * @see SimulationService
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class SimulationAgent extends Agent {

	private static final long serialVersionUID = 1782853782362543057L;

	private boolean passive = false;
	
	/** The ServiceSensor of this agent. */
	protected ServiceSensor mySensor;
	/** The current EnvironmentModel. */
	protected EnvironmentModel myEnvironmentModel;
	/** The location, where the agent has to migrate to. */
	protected Location myNewLocation;
	
	private CyclicNotificationHandler notifyHandler = null;
	private Vector<EnvironmentNotification> notifications = new Vector<EnvironmentNotification>();

	/**
	 * Instantiates a new simulation agent as an active agent.
	 */
	public SimulationAgent() { }
	/**
	 * Instantiates a new simulation agent as a passive agent.
	 * The agent will just listening to changes in the environment model
	 * but it is not expected, that it will react on it. This is used 
	 * for example for agents, which are displaying the current environment. 
	 * 
	 * @param passive the passive
	 */
	public SimulationAgent(boolean passive) {
		this.passive = passive;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		this.sensorPlugIn();
		this.addNotificationHandler();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#beforeMove()
	 */
	@Override
	protected void beforeMove() {
		super.beforeMove();
		this.sensorPlugOut();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#afterMove()
	 */
	@Override
	protected void afterMove() {
		myNewLocation = null;
		super.afterMove();		
		this.sensorPlugIn();
		this.checkAndActOnEnvironmentChanges();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#beforeClone()
	 */
	@Override
	protected void beforeClone() {
		super.beforeClone();
		this.sensorPlugOut();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#afterClone()
	 */
	@Override
	protected void afterClone() {
		super.afterClone();
		this.sensorPlugIn();
		this.checkAndActOnEnvironmentChanges();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		super.takeDown();
		this.removeNotificationHandler();
		this.sensorPlugOut();
	}
	
	/**
	 * This Method plugs IN the service sensor.
	 */
	protected void sensorPlugIn() {
		// --- Start the ServiceSensor ------------------------------
		mySensor = new ServiceSensor(this);
		// --- Register the sensor to the SimulationService ---------
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			if (this.passive==true) {
				simHelper.sensorPlugIn(mySensor, true);
			} else {
				simHelper.sensorPlugIn(mySensor);	
			}
				
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This Method plugs OUT the service sensor.
	 */
	protected void sensorPlugOut() {
		// --- plug-out the Sensor ----------------------------------
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
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
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			EnvironmentModel tmpEnvMode =  simHelper.getEnvironmentModel();
			if (tmpEnvMode!=null) {
				if (tmpEnvMode.equals(myEnvironmentModel)==false) {
					this.onEnvironmentStimulus();	
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
	public void setMigration(Location newLocation) {
		myNewLocation = newLocation;
	}
	
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
			this.addBehaviour(new ServiceStimulus());	
		} else {
			this.onEnvironmentStimulus();	
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
			onEnvironmentStimulus();
		}
	}
	/**
	 * This method is called if a stimulus from the outside reached this agent.
	 * It can be overwritten in the child class to act on environment changes. 
	 */
	protected void onEnvironmentStimulus() {
	}
	
	/**
	 * This method sets the answer respectively the change of a single simulation agent
	 * back to the central simulation manager.
	 *
	 * @param myNextState the next state of this agent in the next instance of the environment model
	 */
	protected void setMyStimulusAnswer(Object myNextState) {
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.setEnvironmentInstanceNextPart(getAID(), myNextState);
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
		EnvironmentNotification myNotification = new EnvironmentNotification(this.getAID(), false, notification);
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
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
		EnvironmentNotification myNotification = new EnvironmentNotification(this.getAID(), false, notification);
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			send = simHelper.notifySensorAgent(receiverAID, myNotification);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return send;
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
		notifyHandler.restart();	
			
	}
	/**
	 * This method adds the CyclicNotificationHandler to this agent.
	 */
	private void addNotificationHandler() {
		if (this.notifyHandler==null) {
			this.notifyHandler = new CyclicNotificationHandler();	
		}		
		this.addBehaviour(this.notifyHandler);
	}
	/**
	 * This method removes the CyclicNotificationHandler from this agent.
	 */
	private void removeNotificationHandler() {
		if (this.notifyHandler!=null) {
			this.removeBehaviour(this.notifyHandler);	
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
			
			boolean removeFirstElement = false;
			
			// --- Get the first element and work on it ------------------
			if (notifications.size()!=0) {
				EnvironmentNotification notification = notifications.get(0);
				onEnvironmentNotification(notification);
				removeFirstElement = true;				
			}
			
			// --- remove this element and control the notifications -----
			synchronized (notifications) {
				if (removeFirstElement==true) {
					notifications.remove(0);
				}
				if (notifications.size()==0) {
					block();
				}
			}
		}
	}
	
	/**
	 * This method will be executed if a ManagerNotification arrives this agent.
	 *
	 * @param notification the notification
	 */
	protected void onEnvironmentNotification(EnvironmentNotification notification) {};
	
	
}
