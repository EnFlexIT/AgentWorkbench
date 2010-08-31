package game_of_life.agents;
import game_of_life.LBC.CenterDynamicLoadBalancingCoodinator;
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
	public long timeCounterStart;
	public long timeCounterStop;
	public long totalCounterTime;
	
	private JInternalFrame internalFrame;
	
	protected void setup() {
		
		// ----- get the arguments of agents ----------------------------------------------
		coordinate = getArguments();
		
		// ----- create coordinates of Agents ---------------------------------------------
		cRow = (Integer) coordinate[0];
		cCol = (Integer) coordinate[1];
		
		// --------- Setup the Simulation with the Simulation-Service ---------------------
		tmd = new TimeModelStroke();
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.setManagerAgent(this.getAID());
			simHelper.setTimeModel(tmd);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		// ---------- startGUI and show GUI ------------------------------------------------
		gui = new GameOfLifeGUI(cRow, cCol, this);
		JInternalFrame internalFrame = gui;
		internalFrame.setResizable(true);
		internalFrame.setMaximizable(true);
		JDesktopPane dF = Application.ProjectCurr.ProjectDesktop;
		dF.add(internalFrame);
		dF.getDesktopManager().maximizeFrame(internalFrame);
				
		// --- Start the Agents which are in involved in this Experiment -------------------
		createAgents(cRow, cCol);
		
		// --- Setup the Simulation with the Simulation-Service ----------------------------
		try {
			simHelper.setEnvironmentInstance(localEnvModel);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		this.addBehaviour(new ReceiveAndStepBehaviour(this));
		if (gui.gameRunning == false) {
			this.doSuspend();
		}
	} 
	
	//----------- start agents for the game of life ----------------------------------------
	private void createAgents(int cRow, int cCOl){
		
		Object obj[] = new Object[2];
		obj[0] = cRow;
		obj[1] = cCol;
		
		Application.JadePlatform.jadeAgentStart("CDLBC", CenterDynamicLoadBalancingCoodinator.class.getName(), obj);
		
		for (int i = 0; i < cRow; i++) {
			for (int j = 0; j < cCOl; j++) {
				String agentName = i+"&"+j;	
				localEnvModel.put(agentName, 0);
			}
		}
	}
	private class ReceiveAndStepBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;
		private boolean doStep = true;
		private Integer msgBackSetPoint = localEnvModel.size();
		private Long lastMessage = new Long(0);
		
		public ReceiveAndStepBehaviour(Agent agent) {
			super(agent);
			generation = 0;
			timeCounterStart = 0;
			timeCounterStop = 0;
			totalCounterTime = 0;
		}
		@Override
		public void action() {
			if (doStep==true) {
				// --- Internal Counting -----------------------------------------------------
				generation++;  //considered as Generation increases
				timeCounterStart = System.currentTimeMillis();	//generation start time
				gui.generationLabel.setText("Generation: " + generation);
				try {
					// --- ggf. Outgoing-Speicher lesen --------------------------------------
					if (gui.localEnvModelOutput.size()!=0) {
						//----- lese outgoing-Speicher des GUIs --- 
						java.util.Iterator<String> it = gui.localEnvModelOutput.keySet().iterator();
						while (it.hasNext()) {
							String key = it.next();
							Integer value = gui.localEnvModelOutput.get(key);
							if (localEnvModel.containsKey(key)){
								localEnvModel.remove(key);
							}
							localEnvModel.put(key, value);
						}
						gui.localEnvModelOutput.clear();
						simHelper.setEnvironmentInstance(localEnvModel);
					}
					// --- Step the simulation one forward -----------------------------------
					// --- automatically informs all involved agents as well -----------------
					simHelper.stepTimeModel(); 
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				doStep = false;
				// ---- Now, wait for the response of all other agents ----------------------
			} else {
						// --- Waiting for the reply of all Agents --------------------------
				ACLMessage msg = myAgent.receive();			
				if (msg!=null) {
					
					//System.out.println(" Sender Name: "+msg.getSender().getLocalName());
					
					lastMessage = System.currentTimeMillis();
					// --- Am Modell arbeiten -----------------------------------------------					
					Integer newValueInteger = Integer.parseInt(msg.getContent());
					String AgentName = msg.getSender().getLocalName();
					if (localEnvModelNew.containsKey(AgentName)== false) {
						localEnvModelNew.put(AgentName, newValueInteger);
					}
					// --- Wenn die Liste der zu erwartenden Elemente fertig ist: ----------- 
					if (localEnvModelNew.size()==msgBackSetPoint) {
						try {
							localEnvModel = localEnvModelNew;
							gui.updateGUI(localEnvModel);
							if (gui.slider.getValue()>0) {
								block(gui.slider.getValue());
								//block(0);
							}
							doStep = true;
							localEnvModelNew = new HashMap<String, Integer>();
							simHelper.setEnvironmentInstance(localEnvModel);
							timeCounterStop = System.currentTimeMillis()-timeCounterStart;
							totalCounterTime += timeCounterStop;
							//System.out.println(" Generation : "+generation+" , time taken = "+timeCounterStop+" totalCounterTime = "+totalCounterTime);
							//System.out.println(generation);
							System.out.println(timeCounterStop);
							//System.out.println(totalCounterTime);
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
					block(200);
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
