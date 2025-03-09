package gol.acl.agents;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.simulation.LoadService;
import de.enflexit.awb.simulation.LoadServiceHelper;
import gol.acl.gui.GameOfLifeGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JDesktopPane;


public class SimulationManagerAgent extends Agent { 
	
	private static final long serialVersionUID = 8465497357332714947L;

	//------------- set GUI co-ordinates ------------------------------------------------
	private Object agentArgs[];
	private int cRow;
	private int cCol;

	//------------ JInternalframe -------------------------------------------------------
	private GameOfLifeGUI gui;

	//------------ Environment for Agents -----------------------------------------------
	public static HashMap<String, Integer> localEnvModel = new HashMap<String, Integer>();
	public static HashMap<String, Integer> localEnvModelNew = new HashMap<String, Integer>();
	
	@SuppressWarnings("unchecked")
	protected void setup() {
		
		// ----- get the arguments/coordinates of gol.acl.agents --------------------------------
		agentArgs = this.getArguments();
		cRow = (Integer) agentArgs[0];
		cCol = (Integer) agentArgs[1];
		localEnvModel =  (HashMap<String, Integer>) agentArgs[2];
		
		// ---------- start and show GUI ------------------------------------------------
		gui = new GameOfLifeGUI(cRow, cCol, this);
		gui.bClear.setEnabled(false);
		gui.bPause.setEnabled(false);
		gui.bStart.setEnabled(false);
		gui.setResizable(true);
		gui.setMaximizable(true);
		
		JDesktopPane desptop = Application.getProjectFocused().getProjectDesktop();
		desptop.add(gui);
		desptop.getDesktopManager().maximizeFrame(gui);
		doWait(3000);
		
		// --- Start cyclic behaviour for this Manager Agent ---------------------------- 
		this.addBehaviour(new StepBehaviour());
		this.addBehaviour(new ReceiveBehaviour());
		gui.bClear.setEnabled(true);
		gui.bPause.setEnabled(false);
		gui.bStart.setEnabled(true);

	} 
	
	private class StepBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = -8141698642681739443L;
		private LoadServiceHelper simHelper = null;
		
		private StepBehaviour(){
			try {
				simHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
				simHelper.setSimulationCycleStartTimeStamp();
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
		}
		
		@Override
		public void action() {

			// --- Ggf. den Outgoing-Speicher der GUI lesen -----------------------------
			if (gui.localEnvModelOutput.size()!=0) {
				String[] fieldArray = new String[gui.localEnvModelOutput.size()];
				fieldArray = gui.localEnvModelOutput.keySet().toArray(fieldArray);
				for (int i = 0; i < fieldArray.length; i++) {
					String key = fieldArray[i];
					Integer value = gui.localEnvModelOutput.get(key);
					localEnvModel.put(key, value);					
				}
				gui.localEnvModelOutput.clear();
			}
			
			// --- Define this as start of the cycle ----------
			try {
				simHelper.setSimulationCycleStartTimeStamp();
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			
			// --- Send messages to all Agents ------------
			String[] fieldArray = new String[localEnvModel.size()];
			fieldArray = localEnvModel.keySet().toArray(fieldArray);
			for (int i = 0; i < fieldArray.length; i++) {
				String keyAndAgentName = fieldArray[i];
				Integer valueOfAgent = localEnvModel.get(keyAndAgentName);
				
				// --- Define Receiver --------------------
				AID receiver = new AID();
				receiver.setLocalName(keyAndAgentName);
				
				// --- Build Message ----------------------
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setSender(getAID());
				msg.addReceiver(receiver);
				try {
					msg.setContentObject(valueOfAgent);
					myAgent.send(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- S T A R T --------
	// -----------------------------------------------------
	private class ReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

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
				
				localEnvModelNew.put(sender.getLocalName(), (Integer) msgContent);
				if (localEnvModelNew.size()==localEnvModel.size()) {
					
					gui.updateGUI(localEnvModelNew);
					localEnvModel = localEnvModelNew;
					localEnvModelNew = new HashMap<String, Integer>();
					
					if (gui.slider.getValue()>0) {
						myAgent.doWait(gui.slider.getValue());
					}
					
					// --- Den n�chsten Schritt ausf�hren ----
					myAgent.addBehaviour(new StepBehaviour());
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
	
	@Override
	protected void takeDown() {
		try {
			gui.doDefaultCloseAction();
			gui.dispose();			
			gui = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
} 
