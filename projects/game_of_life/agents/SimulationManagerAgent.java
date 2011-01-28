package game_of_life.agents;

import game_of_life.gui.GameOfLifeGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;

import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JDesktopPane;

import agentgui.core.application.Application;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

public class SimulationManagerAgent extends Agent { 
	
	private static final long serialVersionUID = 8465497357332714947L;

	//------------- set GUI co-ordinates ------------------------------------------------
	private Object agentArgs[];
	private int cRow;
	private int cCol;

	//------------ JInternalframe -------------------------------------------------------
	private GameOfLifeGUI gui;

	//------------ Environment for Agents -----------------------------------------------
	private SimulationServiceHelper simHelper = null;
	public static HashMap<String, Integer> localEnvModel = new HashMap<String, Integer>();
	public static HashMap<String, Integer> localEnvModelNew = new HashMap<String, Integer>();
	
	private EnvironmentModel globalEnvModel = new EnvironmentModel();
	
	protected void setup() {
		
		// ----- get the arguments/coordinates of agents --------------------------------
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
		
		JDesktopPane desptop = Application.ProjectCurr.projectDesktop;
		desptop.add(gui);
		desptop.getDesktopManager().maximizeFrame(gui);
				
		// --------- Setup the Simulation with the Simulation-Service -------------------
		globalEnvModel.setAbstractEnvironment(localEnvModel);
		
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.setManagerAgent(this.getAID());			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
				
		// --- Start cyclic behaviour for this Manager Agent ---------------------------- 
		this.addBehaviour(new ReceiveAndStepBehaviour(this));
		gui.bClear.setEnabled(true);
		gui.bPause.setEnabled(false);
		gui.bStart.setEnabled(true);

	} 
	
	private class ReceiveAndStepBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = 6411030398040354159L;

		private long timeStepStart;
		private long timeStepStop;
		private long timeStepTotal;
		@SuppressWarnings("unused")
		private double timeStepTotal_ms;
		
		private Hashtable<AID, Object> agentAnswers = null;
		
		public ReceiveAndStepBehaviour(Agent agent) {
			super(agent);
			if (gui.gameRunning == false) {
				this.myAgent.doSuspend();
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
			
			// --- Simulationsschritt vorbereiten und durchführen -----------------------
			try {
				timeStepStart = System.nanoTime();	
				simHelper.resetEnvironmentInstanceNextParts();
				simHelper.stepSimulation(globalEnvModel,true);

				// --- Now wait for all answers -----------------------------------
				while (simHelper.getEnvironmentInstanceNextParts().size() != localEnvModel.size() ) {
					doWait(5);					
				}
				timeStepStop = System.nanoTime();
				timeStepTotal = (timeStepStop - timeStepStart);
				timeStepTotal_ms = timeStepTotal * Math.pow(10, -6);
//				System.out.println("Step-Execution-Time: " + timeStepTotal_ms );
				
				// --- Neues Umgebungsmodell definieren ---------------------------
				localEnvModelNew = new HashMap<String, Integer>();
				agentAnswers = simHelper.getEnvironmentInstanceNextParts();
				
				AID[] agentArray = new AID[agentAnswers.size()];
				agentArray = agentAnswers.keySet().toArray(agentArray);
				for (int i = 0; i < agentArray.length; i++) {
					Integer value = (Integer) agentAnswers.get(agentArray[i]);  
					localEnvModelNew.put(agentArray[i].getLocalName(), value);
				}
				gui.updateGUI(localEnvModelNew);
				localEnvModel = localEnvModelNew;
				globalEnvModel.setAbstractEnvironment(localEnvModel);
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			if (gui.slider.getValue()>0) {
				block(gui.slider.getValue());
			}
					
		}
	 }

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
