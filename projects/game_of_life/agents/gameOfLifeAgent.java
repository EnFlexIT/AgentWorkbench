package game_of_life.agents;

import java.util.HashMap;
import java.util.Observable;
import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.sensoring.ServiceSensor;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.lang.acl.ACLMessage;

public class gameOfLifeAgent extends Agent implements ServiceSensor {

	private static final long serialVersionUID = 5814102967088301805L;
	// --------- create hash table -----------------------------------------
	HashMap<String, Integer> localEnvModel;
	// --------------- objects variables -----------------------------------
	private Object coordinate[];
	// ---------------- Instance variables ---------------------------------
	private int myCurrentState;
	private int myNextState;
	private int cRow, cCol;
	private int length;
	private int width;
	// ---------------- coding and decoding -------------------------------
	private Codec codec = new SLCodec();
	private AID simulationManager = null;
	SimulationServiceHelper simHelper = null;

	private String[] neighbours; // array for storing neighbour

	protected void setup() {
		// --------- ini hash table ---------------------------------------
		localEnvModel = new HashMap<String, Integer>();
		myCurrentState = 0;
		myNextState = 0;
		// ----- get the arguments of agents -------------------------------
		coordinate = getArguments();

		// ----- create coordinates of Agents ----------------------------
		cRow = (Integer) coordinate[0];
		cCol = (Integer) coordinate[1];

		// ---- length of the game of length -----------------------------
		length = (Integer) coordinate[2];
		width = (Integer)coordinate[3];

		// ------- create my neighbours -----------------------------------
		neighbours = createNeighbours(cRow, cCol, length, width);
		setNeighbours(neighbours);

		// ------- Set simulation values ----------------------------------
		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simulationManager = simHelper.getManagerAgent();
			simHelper.addSensor(this);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
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
		
	/*	System.out.println("AgentName " + getLocalName() + " Neighbours ");
		for (int i = 0; i < obj.length; i++) {
			System.out.print(" " + obj[i]);
		}
		System.out.println(" ");*/
		
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

	// ------ array with stored numer of neighbours -----------------------
	public void setNeighbours(String[] neighbours) {
		this.neighbours = neighbours;
	}

	// ------------ verify state of my neighbours -------------------------
	protected int checkStateOfMyNeighbours() {
		int counter = 0;
		// ------------ Look for state of the neighbours ------------------
		for (int i = 0; i < 8; i++) {
			if (neighbours[i] != null) {
				counter += localEnvModel.get(neighbours[i]);
			}
		}
		return counter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals(SimulationService.SERVICE_UPDATE_TIME_STEP)) {
			// --- Simulationsimpuls erhalten -----------------------------
			try {
				// ------- get Environment-Object-Instance ----------------
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				Object simInst = simHelper.getEnvironmentInstance();
				localEnvModel = (HashMap<String, Integer>) simInst;
				// -------- Refresh state of agent -----------------------
				myCurrentState = localEnvModel.get(this.getLocalName());
				// -------- Look at the neighbours ----------------------
				// controle the state of my neighbours
				int stateOfNeighbours = checkStateOfMyNeighbours();
				// -------- Decide what to do ---------------------------
				// -------- change my state depending on my neighbours --
				updateStateOfAgent(stateOfNeighbours);
				// --- Send a message to the main controler about your change of
				this.sendMessage2ManagerAgent(myCurrentState);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
	}

	// ------- Agent behavious ------------------------------------------
	private boolean sendMessage2ManagerAgent(Integer newValue) {
		// ------- Definition einer neuen 'Action' ----------------------
		// Action act = new Action();
		// act.setActor(getAID());
		// act.setAction(agentAction);
		// ------- Nachricht zusammenbauen und ...-----------------------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(getAID());
		msg.addReceiver(simulationManager);
		msg.setLanguage(codec.getName());
		// msg.setOntology(ontology.getName());
		msg.setContent(newValue.toString());
		// --- ... versenden --------------------------------------------
		// getContentManager().fillContent(msg, act);
		send(msg);
		return true;
	}
}