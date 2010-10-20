package game_of_life.agents;

import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import mas.service.SimulationAgent;
import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;

/**
 * @version 1.0
 */ 
public class GameOfLifeAgent extends SimulationAgent { 

	private static final long serialVersionUID = 1L;
	
	private Object startArgs[];
	
	private Vector<String> myNeighbours = new Vector<String>();
	
	private Integer myCurrentState;
	private Integer myNextState;
	
	@SuppressWarnings("unchecked")
	protected void setup() { 
		super.setup();
		
		// ----- get the arguments of agents -------------------------------
		startArgs = getArguments();
		myNeighbours = (Vector<String>) startArgs[0];

	} 
	
	@SuppressWarnings("unchecked")
	public void onEnvironmentStimulus() {

		if (myEnvironmentModel!=null) {

			HashMap<String, Integer> localEnvModel = (HashMap<String, Integer>) myEnvironmentModel.getEnvironmentInstance();
			this.updateStateOfAgent(localEnvModel);
			
			try {
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				simHelper.setEnvironmentInstanceNextPart(getAID(), myNextState);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	// ---- update state of Agent after controlling my neighbours -----------
	public Integer updateStateOfAgent(HashMap<String, Integer> localEnvModel) {
		
		int stateOfNeibours = 0;

		// --- Look for state of the neighbours -----------------------------
		Iterator<String> it = myNeighbours.iterator();
		while (it.hasNext()) {
			String neighbour = it.next();
			stateOfNeibours += localEnvModel.get(neighbour);
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

	
	
	class ReceiveSimulationCalls extends CyclicBehaviour {

		private static final long serialVersionUID = 5473235698882127521L;

		@Override
		public void action() {
		
			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
				
				// --- Simulationsimpuls erhalten --------------
				try {
					// --- get Environment-Object-Instance ------------
					SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
					Object simInst = simHelper.getEnvironmentModel();
					System.out.println( "=> " + simInst.toString() );

				
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				
				
			} else {
				block();
			}
			
		}
		
	}

} 
