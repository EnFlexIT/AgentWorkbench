package gol.SimService.agents;

import gol.environment.GameOfLifeDataModel;
import jade.core.AID;
import jade.core.ServiceException;

import java.util.HashMap;
import java.util.Hashtable;

import de.enflexit.awb.simulation.agents.SimulationManagerAgent;
import de.enflexit.awb.simulation.environment.EnvironmentModel;
import de.enflexit.awb.simulation.environment.time.TimeModelContinuous;
import de.enflexit.awb.simulation.transaction.EnvironmentNotification;

public class SimulationManager extends SimulationManagerAgent {

	private static final long serialVersionUID = 670062003421753868L;

	private GameOfLifeDataModel golModel = null;
	private GameOfLifeDataModel golModelEdited = null;
	

	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationManagerAgent#setup()
	 */
	protected void setup() {
		super.setup();
		
		// --- Here the super class already grabbed the needed information 
		// --- from the SimulationSetup. They can be found by using 
		// --- the method 'this.getEnvironmentModel()' 

		
		// --- Get the displayable data model ---------------------------------
		this.golModel = (GameOfLifeDataModel) this.getDisplayEnvironment();
		this.setRunSimulation(true);
		
		// ---- In case of the corresponding time model -----------------------
		// !!! This is just for demonstration !!! -----------------------------
		if (this.getTimeModel() instanceof TimeModelContinuous) {
			((TimeModelContinuous) this.getTimeModel()).setTimeAskingAgent(this);
			((TimeModelContinuous) this.getTimeModel()).setExecuted(true);
		}
		// !!! This is just for demonstration !!! -----------------------------
	}
	
	/**
	 * Sets the run simulation.
	 * @param run the new run simulation
	 */
	public void setRunSimulation(boolean run) {
		if (run==true) {
			this.addSimulationBehaviour();
		} else {
			this.removeSimulationBehaviour();
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationManagerAgent#setPauseSimulation(boolean)
	 */
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		if (isPauseSimulation==true) {
			this.setRunSimulation(false);
		} else {
			this.setRunSimulation(true);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationManagerInterface#doSingleSimulationSequennce()
	 */
	@Override
	public void doSingleSimulationSequence() {

		// --- set the environment model ------------------
		if (this.getEnvironmentModel().getTimeModel()!=null) {
			this.getEnvironmentModel().getTimeModel().step();	
		}
		this.getEnvironmentModel().setDisplayEnvironment(this.golModel);
		
		// --- Do the next simulation step ----------------
		try {
			this.resetEnvironmentInstanceNextParts();
			this.stepSimulation(this.golModel.getGolHash().size());
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationManagerAgent#proceedAgentAnswers(java.util.Hashtable)
	 */
	@Override
	public void proceedAgentAnswers(Hashtable<AID, Object> agentAnswers) {

		// --- Define new display EnvironmentModel ----------------------------
		HashMap<String, Integer> localEnvModelNew = new HashMap<String, Integer>();
		AID[] agentArray = new AID[agentAnswers.size()];
		agentArray = agentAnswers.keySet().toArray(agentArray);
		for (int i = 0; i < agentArray.length; i++) {
			Integer value = (Integer) agentAnswers.get(agentArray[i]);  
			localEnvModelNew.put(agentArray[i].getLocalName(), value);
		}
		this.golModel.setGolHash(localEnvModelNew);
		
		// --- Did we've got edits from the visual representation ? -----------
		if (this.golModelEdited!=null) {
			this.golModel = this.golModelEdited;
			this.golModelEdited = null;
		}
		this.doNextSimulationStep();
		
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationManagerAgent#onManagerNotification(agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	protected void onManagerNotification(EnvironmentNotification notification) {
		
		EnvironmentModel envModelNotification = (EnvironmentModel) notification.getNotification();
		
		this.golModelEdited = (GameOfLifeDataModel) envModelNotification.getDisplayEnvironment();
		this.setTimeModel(envModelNotification.getTimeModel());
		
	}
	
}
