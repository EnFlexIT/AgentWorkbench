package de.enflexit.awb.simulation.agents;

import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.core.environment.EnvironmentPanel;
import de.enflexit.awb.simulation.SimulationService;
import de.enflexit.awb.simulation.SimulationServiceHelper;
import de.enflexit.awb.simulation.environment.EnvironmentModel;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.awb.simulation.environment.time.TimeModelContinuous;
import jade.core.ServiceException;

/**
 * The Class VisualisationAgent can be used in order to build agents
 * that are passively observing environment changes that have to be
 * displayed by an extended VisualisationAgent.<br>
 * If the SimulationService is running, the VisualisationAgent will 
 * register there as a displaying agent. 
 * 
 * @see SimulationAgent
 * @see EnvironmentModel
 * @see EnvironmentController
 * @see EnvironmentPanel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractDisplayAgent<VisualizationContainer> extends SimulationAgent {

	private static final long serialVersionUID = -6499021588257662334L;
	
	protected boolean isPausedSimulation = true;
	private boolean isAgentWorkbenchEmbedded = false;
	private EnvironmentController myEnvironmentController;
	
	protected VisualizationContainer visualizationContainer;

	/**
	 * Instantiates a new visualization agent for an EnvironmentModel.
	 */
	public AbstractDisplayAgent() {
		// --- Initialize this agent as a passive ---------
		// --- SimulationAgent (or observing agent) ------- 
		super(true);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.agents.SimulationAgent#setup()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setup() {
		super.setup();
		
		Object[] startArgs = getArguments();
		EnvironmentModel tmpEnvironmentModel = null;
			
		if (startArgs==null || startArgs.length==0) {
			// --- Started as independent display -------------------
			this.setAgentWorkbenchEmbedded(false);
			// --- Get environment from SimulationService -----------
			tmpEnvironmentModel = this.getEnvironmentModelFromSimulationService();
			
		} else {
			// --- Started from Agent.Workbench ---------------------
			this.setAgentWorkbenchEmbedded(true);
			// --- Get info from Agent.GUI configuration ------------
			this.visualizationContainer = (VisualizationContainer) startArgs[0];
			// --- Get environment from given controller ------------
			if (startArgs[1] instanceof EnvironmentController) {
				EnvironmentController envController = (EnvironmentController) startArgs[1];
				tmpEnvironmentModel = envController.getEnvironmentModel();
			} else if (startArgs[1] instanceof EnvironmentModel) {
				tmpEnvironmentModel = (EnvironmentModel) startArgs[1];
			}
			
		}
		// --- Set a copy of the EnvironmentModel to the local one --
		if (tmpEnvironmentModel!=null) {
			this.myEnvironmentModel = tmpEnvironmentModel.getCopy();
			this.getEnvironmentController().setEnvironmentModel(this.myEnvironmentModel);
		}
		
		// --- Build the visual components --------------------------
		this.buildEnvironmentModelVisualization();
		// --- Register as DisplayAgent at the SimulationService ----
		this.registerAsDisplayAgent();
		// --- Display the current TimeModel ------------------------
		this.displayTimeModel();
	}
	
	/**
	 * Returns a new environment controller that depends on the actual EnvironmentModel.<br> 
	 * As Example: In case of the 'Graph and Network Environment' a new  GraphEnvironmentController 
	 * will be created and returned.
	 * 
	 * @return the new EnvironmentController
	 */
	protected abstract EnvironmentController createNewEnvironmentController();
	
	/**
	 * Sets the current EnvironmentController of the agent. In case that the EnvironmentController 
	 * is not null, in turn this agent will be set as the controlling instance of the EnvironmentController.
	 * 
	 * @see EnvironmentController#setDisplayAgent(AbstractDisplayAgent)
	 * 
	 * @param newEnvironmentController the new environment controller
	 */
	protected void setEnvironmentController(EnvironmentController newEnvironmentController) {
		this.myEnvironmentController = newEnvironmentController;
		if (this.myEnvironmentController!=null) {
			this.myEnvironmentController.setDisplayAgent(this);	
		}
	}
	/**
	 * Returns the current EnvironmentController. In case that the EnvrionmentController is null, this
	 * method will invoke {@link #createNewEnvironmentController()} and will set this agent as the 
	 * controlling instance of the EnvironmentController.
	 * 
	 * @see #createNewEnvironmentController()
	 * @see EnvironmentController#setDisplayAgent(AbstractDisplayAgent)
	 * 
	 * @return the environment controller
	 */
	protected EnvironmentController getEnvironmentController() {
		if (this.myEnvironmentController==null) {
			this.myEnvironmentController = this.createNewEnvironmentController();
			if (this.myEnvironmentController!=null) {
				this.myEnvironmentController.setDisplayAgent(this);	
			}
		}
		return myEnvironmentController;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.agents.SimulationAgent#takeDown()
	 */
	@Override
	protected void takeDown() {
		this.destroyEnvironmentModelVisualization();
		this.unregisterAsDisplayAgent();
		super.takeDown();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.agents.SimulationAgent#beforeMove()
	 */
	@Override
	protected void beforeMove() {
		this.destroyEnvironmentModelVisualization();
		this.unregisterAsDisplayAgent();
		super.beforeMove();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.agents.SimulationAgent#afterMove()
	 */
	@Override
	protected void afterMove() {
		super.afterMove();
		this.myEnvironmentModel = this.getEnvironmentModelFromSimulationService();
		if (this.myEnvironmentModel!=null) {
			this.getEnvironmentController().setEnvironmentModel(this.myEnvironmentModel);	
		}
		this.buildEnvironmentModelVisualization();
		this.registerAsDisplayAgent();
		this.displayTimeModel();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.agents.SimulationAgent#afterClone()
	 */
	@Override
	protected void afterClone() {
		this.destroyEnvironmentModelVisualization();
		super.afterClone();
		this.registerAsDisplayAgent();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.agents.AbstractDisplayAgent#setPauseSimulation(boolean)
	 */
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		this.isPausedSimulation = isPauseSimulation;
		if (this.myEnvironmentModel.getTimeModel() instanceof TimeModelContinuous) {
			TimeModelContinuous tmc = (TimeModelContinuous)this.myEnvironmentModel.getTimeModel();
			tmc.setExecuted(!this.isPausedSimulation);
			this.setTimeModelDisplay(this.myEnvironmentModel.getTimeModel());	
		}
	}
	
	/**
	 * Register this agent as DisplayAgent at the SimulationServie.
	 */
	protected void registerAsDisplayAgent() {
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.displayAgentRegister(this);	
		} catch (ServiceException se) {
			se.printStackTrace();
		}
	}
	/**
	 * Unregister this agent as DisplayAgent at the SimulationServie.
	 */
	protected void unregisterAsDisplayAgent() {
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.displayAgentUnregister(this);	
		} catch (ServiceException se) {
			se.printStackTrace();
		}
	}
	
	/**
	 * Sets the Agent.GUI embedded.
	 * @param isAgentWorkbenchEmbedded the new agent gui embeded
	 */
	public void setAgentWorkbenchEmbedded(boolean isAgentGuiEmbedded) {
		this.isAgentWorkbenchEmbedded = isAgentGuiEmbedded;
	}
	/**
	 * Checks if is the current display is embedded into the Agent.GUI main window.
	 * @return true, if is embedded into Agent.GUI 
	 */
	public boolean isAgentWorkbenchEmbedded() {
		return isAgentWorkbenchEmbedded;
	}

	
	/**
	 * Has to build the visualization of the current {@link EnvironmentModel}.
	 */
	public abstract void buildEnvironmentModelVisualization();
	
	/**
	 * Has to destroy the visualization of the current {@link EnvironmentModel}.
	 */
	public abstract void destroyEnvironmentModelVisualization();
	
	/**
	 * Displays the current TimeModel, if available.
	 */
	private void displayTimeModel() {
		if (myEnvironmentModel!=null) {
			if (myEnvironmentModel.getTimeModel()!=null) {
				if (myEnvironmentModel.getTimeModel() instanceof TimeModelContinuous) {
					// --- Set this agent to the TimeModel in order to use ----
					// --- the synchronized platform time ---------------------
					((TimeModelContinuous)myEnvironmentModel.getTimeModel()).setTimeAskingAgent(this);
				}
				this.setTimeModelDisplay(myEnvironmentModel.getTimeModel());
			}
		}
	}
	
	/**
	 * Sets the time model display.
	 * @param timeModel the new time model display
	 */
	public abstract void setTimeModelDisplay(TimeModel timeModel);
	
}
