package agentgui.simulationService.agents;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.sensoring.ServiceSensor;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;

public class SimulationAgent extends Agent {

	private static final long serialVersionUID = 1782853782362543057L;

	protected ServiceSensor mySensor;
	protected EnvironmentModel myEnvironmentModel;
	protected Location myNewLocation;

	@Override
	protected void setup() {
		super.setup();
		this.sensorPlugIn();
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
	 * this agent about changes in the environmet. It can be either used
	 * to do this asynchron or synchron. It is highly recommended to do
	 * this asynchron, so that the agencie can act parallel and not
	 * sequently. 
	 * @param envModel
	 * @param aSynchron
	 */
	public void setEnvironmentModel(EnvironmentModel envModel, boolean aSynchron) {
		myEnvironmentModel = envModel;
		if (aSynchron == true) {
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
	
	
	
}
