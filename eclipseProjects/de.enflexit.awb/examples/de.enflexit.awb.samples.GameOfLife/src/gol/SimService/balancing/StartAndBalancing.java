package gol.SimService.balancing;

import gol.environment.GameOfLifeDataModel;
import gol.environment.SquaredEnvironmentController;
import jade.core.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import de.enflexit.awb.simulation.agents.LoadExecutionAgent;
import de.enflexit.awb.simulation.balancing.StaticLoadBalancingBase;


public class StartAndBalancing extends StaticLoadBalancingBase {

	private static final long serialVersionUID = 1040818132230231302L;

	private final String context = "Game Of Life";
	private String agentGameOfLifeClass = gol.SimService.agents.GameOfLifeAgent.class.getName();
	private String agentManagerAgent = gol.SimService.agents.SimulationManager.class.getName();
	
	private HashMap<String, Integer> environmentModel = null;
	private int nAgents = 0;
	private int nColumn = 0;
	private int nRows = 0;
	
	private Integer agents2StartNotfyInterval = 0;
	private Integer agentsStarted = 0;
	private Integer agentsStartedOld = 0;
	
	
	public StartAndBalancing(LoadExecutionAgent agent) {
		super(agent);
	}


	@Override
	public void doBalancing() {
	
		System.out.println("Starting 'Game Of Life' ...");
		SquaredEnvironmentController golController = (SquaredEnvironmentController) currProject.getEnvironmentController();
		GameOfLifeDataModel golModel = (GameOfLifeDataModel) golController.getDisplayEnvironmentModelCopy();		
		
		nColumn = golModel.getNumberOfColumns();
		nRows   = golModel.getNumberOfRows();
		nAgents = golModel.getNumberOfAgents();
		
		
		// -----------------------------------------------------
		// --- read start configuration ------------------------
		if (nAgents==0) {
			JOptionPane.showMessageDialog(null, "Please set the number of agents !", context + ": Missing Option", JOptionPane.OK_OPTION);
			return;
		}
		if (currNumberOfContainer==0) {
			JOptionPane.showMessageDialog(null, "Please set the number of containers !", context + ": Missing Option", JOptionPane.OK_OPTION);
			return;
		}
		
		// -----------------------------------------------------
		// --- start the needed remote-container ---------------
		Vector<String> locationNames = null;
		int cont4DisMax = 0;
		int cont4DisI = 0;
		Hashtable<String, Location> newContainerLocations = this.startNumberOfRemoteContainer(currNumberOfContainer - 1, true, null);
		if (newContainerLocations!=null) {
			locationNames = new Vector<String>(newContainerLocations.keySet());
			cont4DisMax = newContainerLocations.size();
		}
		
		// -----------------------------------------------------
		// --- calculate number of rows and columns ------------
		agents2StartNotfyInterval = nAgents / 10;
		
		// -----------------------------------------------------		
		// --- Build the environment model of the agency -------
		// -----------------------------------------------------
		environmentModel = new HashMap<String, Integer>();	
		// --- Run through the columns -------------------------
		for(int stCol=0; stCol<nColumn;stCol++) {
			// --- Run through the rows ------------------------			
			for(int stRow=0; stRow<nRows;stRow++) {
				String agentName = (stRow+"&"+stCol);
				environmentModel.put(agentName, 0);
				if (golModel.getGolHash().get(agentName)!=null) {
					environmentModel.put(agentName, golModel.getGolHash().get(agentName));
				}
			}		
		}
		golModel.setGolHash(environmentModel);
		
		// -----------------------------------------------------
		// --- Start and distribute the Agents -----------------
		// -----------------------------------------------------
		Vector<String> cellKeys = new Vector<String>(environmentModel.keySet());
		Collections.sort(cellKeys);
		Iterator<String> it = cellKeys.iterator();
		while (it.hasNext()) {
			// --- Namen des Agenten ermitteln ------------
			String agentName = it.next();
			Integer agentState = environmentModel.get(agentName);
			
			// --- Set the location for the agent --------------------
			String containerName = locationNames.get(cont4DisI);
			Location location = newContainerLocations.get(containerName);
			cont4DisI++;
			if (cont4DisI>=cont4DisMax) {
				cont4DisI=0;
			}

			// --- Startargument zusammenbauen ------------
			// --- Die Nachbarn, die interessant sind -----
			Object startArg[] = new Object[2];
			startArg[0] = this.getNeighbourVector(agentName);
			startArg[1] = agentState;
			
			// --- Agenten starten ------------------------ 
			this.startAgent(agentName, agentGameOfLifeClass, startArg, location);
			
			// --- Count the number of agents started -----
			agentsStarted++;
			if (agentsStarted>=agentsStartedOld+agents2StartNotfyInterval) {
				System.out.println(agentsStarted + " of  " + nAgents + " started.");
				agentsStartedOld+=agents2StartNotfyInterval;
			}
		} // --- end while ---

		// -----------------------------------------------------
		// --- Start the GUI and the simulation-manager --------
		// -----------------------------------------------------		
		this.startAgent("sim.manager", agentManagerAgent, null);
	}
	
	private Vector<String> getNeighbourVector(String currAgentName) {
		
		Vector<String> neighbour = new Vector<String>();
		
		String[] myPos = currAgentName.split("&");
		Integer myPosR = Integer.parseInt(myPos[0]); 
		Integer myPosC = Integer.parseInt(myPos[1]);
		
		// --- Scan der aktuellen 'Umgebung' --------------
		for (Integer r=myPosR-1; r<myPosR+2; r++) {
			for (Integer c=myPosC-1; c<myPosC+2; c++) {
				// --- Bin ich gerade bei mir selbst? -----
				if ( ! (r==myPosR && c==myPosC) ) {
					// --- Im Umgebungsmodell zu finden? --
					String checkName = r+"&"+c;
					if (environmentModel.containsKey(checkName)) {
						neighbour.add(checkName);
					}
				}
			}
		}
		return neighbour;
	}


	
	
}
