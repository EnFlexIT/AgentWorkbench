package gol.acl.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;


/**
 * @version 1.0
 */ 
public class GameOfLifeAgent extends Agent { 

	private static final long serialVersionUID = 1L;
	
	private Object[] startArgs;
	private Vector<String> myNeighbours = new Vector<String>();
	private Integer myCurrentState = 0;
	private Integer myNextState = 0;
	
	@SuppressWarnings("unchecked")
	protected void setup() { 
		super.setup();
		
		// ----- get the arguments of gol.acl.agents -------------------------------
		startArgs = getArguments();
		if (startArgs!=null && startArgs.length>0) {
			myNeighbours = (Vector<String>) startArgs[0];	
		}
		this.addBehaviour(new ReceiveBehaviour());
	} 
	
	// ---- update state of Agent after controlling my neighbours -----------
	public Integer updateStateOfAgent(HashMap<String, Integer> localEnvModel) {
		
		int stateOfNeibours = 0;

		// --- Look for state of the neighbours -----------------------------
		for (int i = 0; i < myNeighbours.size(); i++) {
			stateOfNeibours += localEnvModel.get(myNeighbours.get(i));
		}
		
		// -------- Refresh state of agent ---------------------------
		myNextState = myCurrentState;		// Default
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

	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- S T A R T --------
	// -----------------------------------------------------
	private class ReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;
		
		private HashMap<String, Integer> localEnvModel = new HashMap<String, Integer>();
		
		@Override
		public void action() {
			
			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
				
				AID sender = msg.getSender();
				
				// ---- extract content -----------------------------
				Object msgContent = null;
				try {
					msgContent = msg.getContentObject();
				} catch (UnreadableException e) {
					msgContent = null;
				}
				
				// --- who is the sender of the message -------------
				if (sender.getLocalName().equalsIgnoreCase("sim.manager")) {
					// ----------------------------------------------
					// --- Message from the simulation manager ------
					myCurrentState = (Integer) msgContent;

					// ----------------------------------------------
					// ---Inform my neighbours about my state -------
					for (int i = 0; i < myNeighbours.size(); i++) {
						
						String aNeighbour = myNeighbours.get(i);
						// --- Send my state to sim.manager ---------
						AID receiver = new AID();
						receiver.setLocalName(aNeighbour);
						// --- Build Message ----------------------
						ACLMessage newState = new ACLMessage(ACLMessage.INFORM);
						newState.setSender(getAID());
						newState.addReceiver(receiver);
						try {
							newState.setContentObject(myCurrentState);
							myAgent.send(newState);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
					
				} else {
					// ----------------------------------------------
					// --- Message from the neigbours ---------------
					String neighbourName = sender.getLocalName();
					Integer neighbourState = (Integer) msgContent;
					localEnvModel.put(neighbourName, neighbourState);
					
					if (localEnvModel.size()==myNeighbours.size()) {
						// --- Calculate my new state ---------------
						myCurrentState = updateStateOfAgent(localEnvModel);
						
						// --- Send my state to sim.manager ---------
						AID receiver = new AID();
						receiver.setLocalName("sim.manager");
						
						// --- Build Message ----------------------
						ACLMessage newState = new ACLMessage(ACLMessage.INFORM);
						newState.setSender(getAID());
						newState.addReceiver(receiver);
						try {
							newState.setContentObject(myCurrentState);
							myAgent.send(newState);
						} catch (IOException e) {
							e.printStackTrace();
						}
				
						localEnvModel = new HashMap<String, Integer>();
						
					}
				}
				
			}
			else {
				block();
			}			
		}
	}
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- E N D ------------
	// -----------------------------------------------------
	
} 
