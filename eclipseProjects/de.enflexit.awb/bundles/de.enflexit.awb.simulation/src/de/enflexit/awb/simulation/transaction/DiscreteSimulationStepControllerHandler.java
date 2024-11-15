package de.enflexit.awb.simulation.transaction;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.awb.environment.EnvironmentModel;
import de.enflexit.awb.environment.time.TimeModel;
import de.enflexit.awb.environment.time.TimeModelDiscrete;
import de.enflexit.awb.environment.time.TimeModelStroke;

/**
 * The Class DiscreteSimulationStepControllerHandler.
 *
 * @author Christian Derksen - SOFTEC - Paluno - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DiscreteSimulationStepControllerHandler {
	
	private static List<AbstractDiscreteSimulationStepController> registeredControllers;
	private static List<AbstractDiscreteSimulationStepController> controllersToWaitFor;
	
	private static boolean isInitialStep = true;
	
	/**
	 * Gets the registered controllers.
	 * @return the registered controllers
	 */
	private static List<AbstractDiscreteSimulationStepController> getRegisteredControllers() {
		if (registeredControllers==null) {
			registeredControllers = new ArrayList<>();
		}
		return registeredControllers;
	}
	
	/**
	 * Gets the controllers to wait for.
	 * @return the controllers to wait for
	 */
	private static List<AbstractDiscreteSimulationStepController> getControllersToWaitFor() {
		if (controllersToWaitFor==null) {
			controllersToWaitFor = new ArrayList<>();
		}
		return controllersToWaitFor;
	}
	
	/**
	 * Adds a new discrete simulation step controller to the list of known controllers.
	 * @param stepController the step controller
	 */
	public static void addDiscreteSimulationStepController(AbstractDiscreteSimulationStepController stepController) {
		getRegisteredControllers().add(stepController);
	}
	/**
	 * Removes a discrete simulation step controller from the list of known controllers.
	 * @param stepController the step controller
	 */
	public static void removeDiscreteSimulationStepController(AbstractDiscreteSimulationStepController stepController) {
		getRegisteredControllers().remove(stepController);
	}
	
	/**
	 * Checks if there are registered controllers that the simulaiton should wait for.
	 * @param envModel the environment model
	 */
	public static void checkDiscreteSimulationStepControllers(EnvironmentModel envModel) {
		
		if (getRegisteredControllers().size()==0) return;
		if (isControllerRelevantTimeModel(envModel.getTimeModel())==false) return;
		
		// --- Don't wait for the initial simulation step -----------
		if (isInitialStep==true) {
			isInitialStep=false;
			return;
		}
		
		// --- Check all registered controllers ---------------------
		for (AbstractDiscreteSimulationStepController stepController : getRegisteredControllers()) {
			try {
				
				// --- Notify that the current step is finished -----
				stepController.onSimulationStepDone();
				
				// --- Check if the controller must be waited for ---
				if (stepController.waitForNextSimulationStepInvocation()==true) {
					getControllersToWaitFor().add(stepController);
				}
				
			} catch (Exception ex) {
				System.err.println("Error executing onSimulationStepDone for " + stepController.getClass().getName());
				ex.printStackTrace();
			}
		}
		
		// --- Wait for all necessary controllers ------------------- 
		synchronized (getControllersToWaitFor()) {
			while (getControllersToWaitFor().size()>0) {
				try {
					getControllersToWaitFor().wait();
				} catch (InterruptedException e) {
					// --- The simulation was stopped, reset --------
					getControllersToWaitFor().clear();
					isInitialStep = true;
				}
			}
		}
		
		
	}
	
	/**
	 * Checks if is the current time model is compatible with a step controller.
	 * @param envModel the env model
	 * @return true, if is controller relevant time model
	 */
	private static boolean isControllerRelevantTimeModel(TimeModel envModel) {
		return (envModel instanceof TimeModelDiscrete || envModel instanceof TimeModelStroke);
	}
	
	/**
	 * Removes the controller from the wait list.
	 * @param stepController the step controller
	 */
	static void removeControllerFromWaitList(AbstractDiscreteSimulationStepController stepController) {
		synchronized (getControllersToWaitFor()) {
			getControllersToWaitFor().remove(stepController);
			getControllersToWaitFor().notify();
		}
	}
	
}
