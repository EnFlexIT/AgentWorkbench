package gol.SimService.agents;

import gol.environment.GameOfLifeDataModel;

import jade.core.Location;

import java.util.HashMap;
import java.util.Vector;

import de.enflexit.awb.simulation.agents.SimulationAgent;



public class GameOfLifeAgent extends SimulationAgent { 

	private static final long serialVersionUID = -7628621869190239951L;

	private Location myNewLocation=null;
	
	private String myName = null;
	private Vector<String> myNeighbours = new Vector<String>();
	private int myState = 0;
	
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#setup()
	 */
	@SuppressWarnings("unchecked")
	protected void setup() { 
		super.setup();
		
		this.myName = this.getLocalName();
		
		// ----- get the arguments of gol.SimService.agents -------------------------------
		Object[] startArgs = getArguments();
		if (startArgs!=null && startArgs.length>0) {
			myNeighbours = (Vector<String>) startArgs[0];
			myState = (Integer) startArgs[1];
		}
	} 
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	public void onEnvironmentStimulus() {

		if (myEnvironmentModel!=null && myEnvironmentModel.getDisplayEnvironment()!=null) {
			GameOfLifeDataModel golModel = (GameOfLifeDataModel) myEnvironmentModel.getDisplayEnvironment();
			this.updateStateOfAgent(golModel.getGolHash());
			this.setMyStimulusAnswer(this.myState);
		}
		// --- Maybe migrate, after you did your job --------
		if (myNewLocation!=null) {
			doMove(myNewLocation);
		}
	}
	
	// ---- update state of Agent after controlling my neighbours -----------
	public void updateStateOfAgent(HashMap<String, Integer> localEnvModel) {
		
		int myNextState = 0;
		int stateOfNeibours = 0;
		
		Integer myNextStateInteger = localEnvModel.get(this.myName);
		if (myNextStateInteger==null) 
			myNextState = 0;
		else 
			myNextState = myNextStateInteger; 

		// --- Look for state of the neighbours -----------------------------
		for (int i = 0; i < myNeighbours.size(); i++) {
			Integer neibourState = localEnvModel.get(myNeighbours.get(i));
			if (neibourState!=null){
				stateOfNeibours += neibourState;	
			} 
		}
		
		// -------- Refresh state of agent ---------------------------
		if (myNextState == 1) { 			// if alive
			if (stateOfNeibours < 2) { 		// 1.Any live cell with fewer than two
				myNextState = 0; 			// live neighbours dies
			}
			if (stateOfNeibours > 3) { 		// 2.Any live cell with more than three
				myNextState = 0; 			// live neighbours dies
			}
		} else { // Agent is not Alive
			if (stateOfNeibours == 3) { 	// 4.Any dead cell with exactly three live
				myNextState = 1; 			// neighbours becomes a live cell
			}
		}
		this.myState = myNextState;
	}
	
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		// --- Nothing to do in case of the Game of Life, because --- 
		// --- the manager agent stops sending a new environment ----  
	}

	@Override
	public void setMigration(Location newLocation) {
		// --- Not implemented yet ----------------------------------
	}

} 
