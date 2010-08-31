package game_of_life.setups;
import game_of_life.gui.GameOfLifeGUI;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.time.TimeModelStroke;
import application.Application;

public class SimulationServiceControllerAgent extends Agent { 
	
	//------------ Static variables -----------------------------------------------------
	private static final long serialVersionUID = 1L;
	
	//------------ Environment for Agents -----------------------------------------------
	private SimulationServiceHelper simHelper = null;
	public static HashMap<String, Integer> localEnvModel = new HashMap<String, Integer>();
	public static HashMap<String, Integer> localEnvModelNew = new HashMap<String, Integer>();
	private TimeModelStroke tmd = null;
	
	//------------ JInternalframe --------------------------------------------------------
	private GameOfLifeGUI gui;
	
	//------------- set GUI co-ordinates --------------------------------------------------
	private int cRow;
	private int cCol;
	
	// --------------- objects variables --------------------------------------------------
	private Object coordinate[];
	
	//------------- generation of simulation ----------------------------------------------
	public int generation;
	
	private JInternalFrame internalFrame;
	
	protected void setup() {
		
	} 

	private class ReceiveAndStepBehaviour extends CyclicBehaviour {

		private boolean doStep = true;
		private Integer msgBackSetPoint = localEnvModel.size();
		private Long lastMessage = new Long(0);
		
		public ReceiveAndStepBehaviour(Agent agent) {
			super(agent);
		}
		public void action() {
			
			if (doStep==true) {
				// --- Internal Counting -----------------------------------------------------
				generation++;  //considered as Generation increases
				gui.generationLabel.setText("Generation: " + generation);

				try {
					// --- Step the simulation one forward -----------------------------------
					// --- automatically informs all involved agents as well -----------------
					simHelper.stepTimeModel(); 

				} catch (ServiceException e) {
					e.printStackTrace();
				}
				doStep = false;
				// ---- Now, wait for the response of all other agents ----------------------
			} else {
				// --- Waiting for the reply of all Agents ----------------------------------
				ACLMessage msg = myAgent.receive();			
				if (msg!=null) {
					
					lastMessage = System.currentTimeMillis();
					
					// --- Am Modell arbeiten -----------------------------------------------					
					Integer newValueInteger = Integer.parseInt(msg.getContent());
					String AgentName = msg.getSender().getLocalName();
					localEnvModelNew.put(AgentName, newValueInteger);
					
					// --- Wenn die Liste der zu erwartenden Elemente fertig ist: ----------- 
					if (localEnvModelNew.size()==msgBackSetPoint) {
						try {
							localEnvModel = localEnvModelNew;
							gui.updateGUI(localEnvModel);
						
								block(gui.slider.getValue());	

							localEnvModelNew = new HashMap<String, Integer>();
							simHelper.setEnvironmentInstance(localEnvModel);
							
							doStep = true;
							
						} catch (ServiceException e) {
							e.printStackTrace();
						}
					}
				} else {
					if (System.currentTimeMillis()-lastMessage >= 3 * (1000)) {
						try {
							
							// --- erneut Notifyen ... --------------------------------------
							simHelper.notifySensors(SimulationService.SERVICE_UPDATE_TIME_STEP);
							
						} catch (ServiceException e) {
							e.printStackTrace();
						}						
					}
				}	
			}			
		}
	 }
	@Override
	protected void takeDown() {
		try {
		gui.dispose();
		gui = null;
		internalFrame.doDefaultCloseAction();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
} 
