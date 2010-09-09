package game_of_life.agents;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.Observable;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.sensoring.ServiceSensor;

/**
 * @version 1.0
 */ 
public class GameOfLifeAgent extends Agent implements ServiceSensor { 

	private static final long serialVersionUID = 1L;
	
	// ---- Instance variables ------------------------------------------
	private int myCurrentState;
	private int myNextState;
	private int cRow, cCol;
	private int length;
	private int width;
	
	private Codec codec = new SLCodec();
	private AID simulationManager = null;
	
	// ---- objects variables ---------------------------------------------
	private Object coordinate[];
	
	// ---- Mobile Agent --------------------------------------------------
	   private AID controller;
	   private Location destination;
	   
	private String[] neighbours; // array for storing neighbour
		
	protected void setup() { 

		// ----- get the arguments of agents -------------------------------
		coordinate = getArguments();
		
		// ----- create coordinates of Agents ------------------------------
		cRow = (Integer) coordinate[0];
		cCol = (Integer) coordinate[1];

		// ---- length of the game of length -------------------------------
		length = (Integer) coordinate[2];
		width = (Integer)coordinate[3];
		
		// ---- needed for migration ---------------------------------------
		controller = (AID) coordinate[4];
		destination = here();

		init();
		
		// ------- create my neighbours ------------------------------------
		neighbours = createNeighbours(cRow, cCol, length, width);
		setNeighbours(neighbours);


		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simulationManager = simHelper.getManagerAgent();
			simHelper.addSensor(this);	
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		  // Program the main behaviour of this agent
	      addBehaviour(new ReceiveCommands(this));
	} 
	
	protected void beforeMove() {
		// -----------------------------
		//System.out.println("AgentName: "+getLocalName()+"  Moving now From location : " + destination.getName());
	}

	protected void afterMove() {
		// ----------------------------
		init();
		//System.out.println("AgentName: "+getLocalName()+"  Arrived at location : " + destination.getName());
	}

	
	void init() {
		
		// ---- Register language and ontology ------------------------------
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());

	}
	// ---------- Internal implementation methods --------------------------
	// ---------- Create Neighbours of Agents ------------------------------
	protected String[] createNeighbours(int r, int c, int length, int width) {
		String obj[] = new String[8];
		// --------- Add 8 Neighbours to every Agent -----------------------
		if (r != 0)
			obj[0] = (r - 1) + "&" + c; // North
		if (c != 0)
			obj[1] = r + "&" + (c - 1); // West
		if (c != 0 && r != length - 1)
			obj[2] = (r + 1) + "&" + (c - 1); // South West
		if (r != 0 && c != 0)
			obj[3] = (r - 1) + "&" + (c - 1); // North West
		if (r != 0 && c != width - 1)
			obj[4] = (r - 1) + "&" + (c + 1); // North East
		if (r != length - 1)
			obj[5] = (r + 1) + "&" + c; // South
		if (c != width - 1)
			obj[6] = r + "&" + (c + 1); // East
		if (r != length - 1 && c != width - 1)
			obj[7] = (r + 1) + "&" + (c + 1); // South East
		
		return obj;
	}

	// ---- update stateof Agent after controlling my neighbours ------------
	public void updateStateOfAgent(int stateOfNeibours) {
		myNextState = myCurrentState;
		if (myCurrentState == 1) { // if alive
			if (stateOfNeibours < 2) { // 1.Any live cell with fewer than two
				myNextState = 0; // live neighbours dies
			}
			if (stateOfNeibours > 3) { // 2.Any live cell with more than three
				myNextState = 0; // live neighbours dies
			}
		} else { // Agent is not Alive
			if (stateOfNeibours == 3) { // 4.Any dead cell with exactly three
										// live
				myNextState = 1; // neighbours becomes a live cell
			}
		}
		myCurrentState = myNextState;
	}

	public void setMyCurrentState(int myS) {
		this.myCurrentState = myS;
	}

	public int getMyCurrentState() {
		return myCurrentState;
	}

	public void setMyNextState(int myNS) {
		this.myNextState = myNS;
	}

	public int getMyNextState() {
		return myNextState;
	}

	// ------ array with stored numer of neighbours ------------------------
	public void setNeighbours(String[] neighbours) {
		this.neighbours = neighbours;
	}

	// ------------ verify state of my neighbours --------------------------
	private void checkStateOfMyNeighbours(HashMap<String, Integer> localEnvModel) {		
		int counter = 0;
		// ------------ Look for state of the neighbours -------------------
		for (int i = 0; i < 8; i++) {
			if (neighbours[i] != null) {
				counter += localEnvModel.get(neighbours[i]);
			}
		}
		updateStateOfAgent(counter);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		
		if (arg.equals( SimulationService.SERVICE_UPDATE_TIME_STEP )) {
			// --- Simulationsimpuls erhalten --------------------------------
			try {
				// --- get Environment-Object-Instance -----------------------
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				Object simInst = simHelper.getEnvironmentInstance();
				//System.out.println( "SSW => " + simInst.toString() );

				// --- Cast it to the right Type -----------------------------
				HashMap<String, Integer> localEnvModel = new HashMap<String, Integer>();
				localEnvModel = (HashMap<String, Integer>) simInst;

				// -------- Refresh state of agent ---------------------------
				myCurrentState = localEnvModel.get(this.getLocalName());
				
				// -------- Look at the neighbours ---------------------------
				// controle the state of my neighbours
				checkStateOfMyNeighbours(localEnvModel);
				
				// --- Send a message to the main controler ------------------
				this.sendMessage2ManagerAgent(myCurrentState);
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	private boolean sendMessage2ManagerAgent(Integer newValue) {
		
		// --- Definition einer neuen 'Action' --------
		//Action act = new Action();
		//act.setActor(getAID());
		//act.setAction(agentAction);

		// --- Nachricht zusammenbauen und ... --------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(getAID());
		msg.addReceiver(simulationManager);
		msg.setLanguage(codec.getName());
		// msg.setOntology(ontology.getName());
		msg.setContent(newValue.toString());

		// --- ... versenden --------------------------
		//getContentManager().fillContent(msg, act);
		send(msg);			
		return true;
		
	}
	

	/*
	* Receive all commands from the controller agent
	*/
	class ReceiveCommands extends CyclicBehaviour {
	//-----------------------------------------------
         
		public ReceiveCommands(Agent a) {
			
			super(a);
		}

		  public void action() {

	      ACLMessage msg = receive(MessageTemplate.MatchSender(controller));

	      if (msg == null) { block(); return; }

		     if (msg.getPerformative() == ACLMessage.REQUEST){

			    try {
				   ContentElement content = getContentManager().extractContent(msg);
				   Concept concept = ((Action)content).getAction();

				  if (concept instanceof MoveAction){

					  MoveAction ma = (MoveAction)concept;
					  Location l = ma.getMobileAgentDescription().getDestination();
					  if (l != null) doMove(destination = l);
				   }
				}
				catch (Exception ex) { ex.printStackTrace();
				System.out.println("Problems Migration");
				}
			 }
			 else { System.out.println("Unexpected msg from controller agent"); }
		  }
	}
	
} 
