package agentgui.simulationService.agents;

import java.util.Vector;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.sensoring.ServiceSensor;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class SimulationAgent extends Agent {

	private static final long serialVersionUID = 1782853782362543057L;

	protected ServiceSensor mySensor;
	protected EnvironmentModel myEnvironmentModel;
	protected Location myNewLocation;
	
	private CyclicNotificationHandler notifyHandler = null;
	private Vector<EnvironmentNotification> notifications = new Vector<EnvironmentNotification>();

	@Override
	protected void setup() {
		super.setup();
		this.sensorPlugIn();
		this.addNotificationHandler();
	}
	@Override
	protected void beforeMove() {
		super.beforeMove();
		this.sensorPlugOut();
	}
	@Override
	protected void afterMove() {
		myNewLocation = null;
		super.afterMove();		
		this.sensorPlugIn();
		this.checkAndActOnEnvironmentChanges();
	}
	@Override
	protected void beforeClone() {
		super.beforeClone();
		this.sensorPlugOut();
	}
	@Override
	protected void afterClone() {
		super.afterClone();
		this.sensorPlugIn();
		this.checkAndActOnEnvironmentChanges();
	}
	@Override
	protected void takeDown() {
		super.takeDown();
		this.removeNotificationHandler();
		this.sensorPlugOut();
	}
	
	/**
	 * This Method plugs IN the service sensor
	 */
	protected void sensorPlugIn() {
		// --- Start the ServiceSensor ------------------------------
		mySensor = new ServiceSensor(this);
		// --- Register the sensor to the SimulationService ---------
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.sensorPlugIn(mySensor);	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This Method plugs OUT the service sensor
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
	 * @param newLocation
	 */
	public void setMigration(Location newLocation) {
		myNewLocation = newLocation;
	}
	
	/**
	 * This method will be used by the ServiceActuator (class) to inform
	 * this agent about changes in the environment. It can be either used
	 * to do this asynchron or synchron. It is highly recommended to do
	 * this asynchron, so that the agencie can act parallel and not
	 * sequently. 
	 * @param envModel
	 * @param aSynchron
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
	 * @author derksen
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
	 * back to the central simulation manager
	 * @param answer
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
	 * This method can be used to transfer any kind of information to the Manager of the current environment model  
	 * @param notification
	 * @return
	 */
	protected boolean sendManagerNotification(Object notification) {
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
	 * This method can be used to transfer any kind of information to one member of the current environment model  
	 * @param notification
	 * @return
	 */
	protected boolean sendAgentNotification(AID receiverAID, Object notification) {
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
	 * a notification for the manager has to be delivered 
	 * @param notification
	 * @param aSynchron
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
	 * This method adds the CyclicNotificationHandler to this agent
	 */
	private void addNotificationHandler() {
		if (this.notifyHandler==null) {
			this.notifyHandler = new CyclicNotificationHandler();	
		}		
		this.addBehaviour(this.notifyHandler);
	}
	/**
	 * This method removes the CyclicNotificationHandler from this agent
	 */
	private void removeNotificationHandler() {
		this.removeBehaviour(this.notifyHandler);
	}

	private class CyclicNotificationHandler extends CyclicBehaviour {
		private static final long serialVersionUID = 4638681927192305608L;
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
	 * This mehtod will be executed if a ManagerNotification arrives this agent
	 * @param notification
	 */
	protected void onEnvironmentNotification(EnvironmentNotification notification) {};
	
	
}
