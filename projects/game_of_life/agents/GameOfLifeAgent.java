package game_of_life.agents;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.domain.mobility.MobilityOntology;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.sensoring.ServiceSensor;

/**
 * @version 1.0
 */ 
public class GameOfLifeAgent extends Agent implements ServiceSensor { 

	private static final long serialVersionUID = 1L;
	
	// ---- objects variables ---------------------------------------------
	private Object startArgs[];
	
	// ---- Instance variables ------------------------------------------
	private Vector<String> myNeighbours = new Vector<String>();
	private Integer myCurrentState;
	private Integer myNextState;
		
	@SuppressWarnings("unchecked")
	protected void setup() { 

		// ----- get the arguments of agents -------------------------------
		startArgs = getArguments();
		myNeighbours = (Vector<String>) startArgs[0];
		
		// --- Register Codec and Ontology --------------------------------
		init();

		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.addSensor(this);	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	} 
	
	public void init() {
		// ---- Register language and ontology ------------------------------
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());
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

	
	// ---- update stateof Agent after controlling my neighbours ------------
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


	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		
		if (arg.equals( SimulationService.SERVICE_UPDATE_TIME_STEP )) {
			// --- Simulationsimpuls erhalten --------------------------------
			try {
				// --- get Environment-Object-Instance -----------------------
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				Object simEnvInst = simHelper.getEnvironmentInstance();
				//System.out.println( "SSW => " + simInst.toString() );

				// --- Cast it to the right Type -----------------------------
				HashMap<String, Integer> localEnvModel = (HashMap<String, Integer>) simEnvInst;

				// -------- Look at the neighbours ---------------------------
				myCurrentState = updateStateOfAgent(localEnvModel);
				
				// --- Send a message to the main controler ------------------
				simHelper.setEnvironmentInstanceNextPart(this.getAID(), myCurrentState);
				
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		
	}
	

	
} 
