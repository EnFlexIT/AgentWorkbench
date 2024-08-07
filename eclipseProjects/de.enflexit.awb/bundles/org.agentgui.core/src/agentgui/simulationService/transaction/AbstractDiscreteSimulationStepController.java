package agentgui.simulationService.transaction;

/**
 * This class allows to intervene in the progress of a discrete simulation.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractDiscreteSimulationStepController {
	
	public AbstractDiscreteSimulationStepController() {
		this.init();
	}
	
	private void init() {
		DiscreteSimulationStepControllerHandler.addDiscreteSimulationStepController(this);
	}
	
	/**
	 * This method is invoked when the previous simulation step is finished, and everything is ready to proceed to the next step.
	 */
	public abstract void onSimulationStepDone();
	
	/**
	 * If this method returns true, the simulation will wait for an external stimulus before proceeding. If false, it will proceed immediately.
	 * @return true, if successful
	 */
	public abstract boolean waitForNextSimulationStepInvocation();
	
	/**
	 * Performs the next simulation step.
	 */
	public final void stepSimulation() {
		DiscreteSimulationStepControllerHandler.removeControllerFromWaitList(this);
	}
}
