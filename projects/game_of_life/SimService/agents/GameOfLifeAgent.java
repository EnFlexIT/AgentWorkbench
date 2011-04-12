package game_of_life.SimService.agents;

import java.util.HashMap;
import java.util.Vector;

import agentgui.simulationService.agents.SimulationAgent;


/**
 * @version 1.0
 */ 
public class GameOfLifeAgent extends SimulationAgent { 

	private static final long serialVersionUID = 1L;
	
	private Object[] startArgs;
	private Vector<String> myNeighbours = new Vector<String>();
	private Integer myCurrentState;
	private Integer myNextState;
	
	@SuppressWarnings("unchecked")
	protected void setup() { 
		super.setup();
		
		// ----- get the arguments of agents -------------------------------
		startArgs = getArguments();
		if (startArgs!=null && startArgs.length>0) {
			myNeighbours = (Vector<String>) startArgs[0];	
		}
	} 
	
	@SuppressWarnings("unchecked")
	public void onEnvironmentStimulus() {

		if (myEnvironmentModel!=null) {

			HashMap<String, Integer> localEnvModel = (HashMap<String, Integer>) myEnvironmentModel.getAbstractEnvironment();
			this.updateStateOfAgent(localEnvModel);
			this.setMyStimulusAnswer(myNextState);
			
			// ---- Test Notification ---------------------------------
//			if (this.getAID().getLocalName().equalsIgnoreCase("0&0")) {
//				// --- Random number ------
//				int maxNumber = localEnvModel.size();
//				int rndNumber = (int)(Math.random() * maxNumber + 1) - 1;
//				
//				Vector agents = new Vector(localEnvModel.keySet());
//				String agentLocalName = (String) agents.get(rndNumber);
//				
//				AID aid = new AID();
//				aid.setLocalName(agentLocalName);
//				String msg = "Hallo " + aid.getLocalName();
//				
//				try {
//					SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
//					System.out.println("Sende " + msg );
//					simHelper.notifySensorAgent(aid, msg);
//					
//				} catch (ServiceException e) {
//					e.printStackTrace();
//				}
//			}
			
		}
		// --- Maybe migrate, after you did your job --------
		if (myNewLocation!=null) {
			doMove(myNewLocation);
		}
	}
	
	// ---- update state of Agent after controlling my neighbours -----------
	public Integer updateStateOfAgent(HashMap<String, Integer> localEnvModel) {
		
		int stateOfNeibours = 0;

		// --- Look for state of the neighbours -----------------------------
		for (int i = 0; i < myNeighbours.size(); i++) {
			stateOfNeibours += localEnvModel.get(myNeighbours.get(i));
		}
		
		// -------- Refresh state of agent ---------------------------
		myCurrentState = localEnvModel.get(this.getLocalName());
		myNextState = myCurrentState;
		if (myCurrentState == 1) { 			// if alive
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
		return myNextState;
	}

	@Override
	protected void onNotification(Object arg0) {
		
		
	
	}
	
} 
