package gol.acl.balancing;

import jade.core.Location;
import jade.core.ServiceException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import agentgui.simulationService.agents.LoadExecutionAgent;
import agentgui.simulationService.balancing.StaticLoadBalancingBase;

public class StartAndBalancing extends StaticLoadBalancingBase {

	private static final long serialVersionUID = 1040818132230231302L;

	private final String context = "Game Of Life";
	private String agentGameOfLifeClass = gol.acl.agents.GameOfLifeAgent.class.getName();
	private String agentManagerAgent = gol.acl.agents.SimulationManagerAgent.class.getName();
	
	private HashMap<String, Integer> environmentModel = null;
	private int nbCol = 0;
	private int nbRow = 0;
	
	private Integer agents2Start = 0;
	private Integer agents2StartNotfyInterval = 0;
	private Integer agentsStarted = 0;
	private Integer agentsStartedOld = 0;
	

	public StartAndBalancing(LoadExecutionAgent agent) {
		super(agent);
	}
	

	@Override
	public void doBalancing() {

		System.out.println("Starting 'Game Of Life' ...");
		
		// -----------------------------------------------------
		// --- read start configuration ------------------------
		if (currNumberOfAgents==0) {
			JOptionPane.showMessageDialog(null, "Please set the number of gol.SimService.agents !", context + ": Missing Option", JOptionPane.OK_OPTION);
			return;
		}
		if (currNumberOfContainer==0) {
			JOptionPane.showMessageDialog(null, "Please set the number of containers !", context + ": Missing Option", JOptionPane.OK_OPTION);
			return;
		}
		
		// -----------------------------------------------------
		// --- Display the system load -------------------------
		this.openMonitorAgents();
		
		// -----------------------------------------------------
		// --- start the needed remote-container ---------------
		Vector<String> locationNames = null;
		int cont4DisMax = 0;
		int cont4DisI = 0;
		Hashtable<String, Location> newContainerLocations = this.startNumberOfRemoteContainer(currNumberOfContainer - 1,true, null);
		if (newContainerLocations!=null) {
			locationNames = new Vector<String>(newContainerLocations.keySet());
			cont4DisMax = newContainerLocations.size();
		}
		
		// -----------------------------------------------------
		// --- calculate number of rows and columns ------------
		int stretch_factor = (int) Math.round( Math.sqrt(currNumberOfAgents/2) );
		nbCol = stretch_factor * 2;
		nbRow = stretch_factor * 1;
		
		agents2Start = nbCol * nbRow;
		agents2StartNotfyInterval = agents2Start / 10;
		
		// -----------------------------------------------------		
		// --- Build the gol.environment model of the ageny --------
		// -----------------------------------------------------
		environmentModel = new HashMap<String, Integer>();
		// --- Run through the columns -------------------------
		for(int stCol=0; stCol<nbCol;stCol++) {
			// --- Run through the rows ------------------------			
			for(int stRow=0; stRow<nbRow;stRow++) {
				String agentName = (stRow+"&"+stCol);
				environmentModel.put(agentName, 0);
			}		
		}

		// -----------------------------------------------------
		// --- Start and distribute the Agents -----------------
		// -----------------------------------------------------
		Vector<String> cellKeys = new Vector<String>( environmentModel.keySet() );
		Collections.sort(cellKeys);
		Iterator<String> it = cellKeys.iterator();
		while (it.hasNext()) {
			// --- Namen des Agenten ermitteln ------------
			String agentName = it.next();
			         
			// --- Set the location for the agent --------------------
			String containerName = locationNames.get(cont4DisI);
			Location location = newContainerLocations.get(containerName);
			cont4DisI++;
			if (cont4DisI>=cont4DisMax) {
				cont4DisI=0;
			}

			// --- Startargument zusammenbauen ------------
			// --- Die Nachbarn, die interessant sind -----
			Object startArg[] = new Object[1];
			startArg[0] = getNeighbourVector(agentName);

			// --- Agenten starten ------------------------ 
			this.startAgent(agentName, agentGameOfLifeClass, startArg, location);
			
			// --- Count the number of gol.SimService.agents started -----
			agentsStarted++;
			if (agentsStarted>=agentsStartedOld+agents2StartNotfyInterval) {
				System.out.println(agentsStarted + " of  " + agents2Start + " started.");
				agentsStartedOld+=agents2StartNotfyInterval;
			}
		} // --- end while ---

		// -----------------------------------------------------
		// --- Start the GUI and the simulation-manager --------
		// -----------------------------------------------------		
		Object [] arg = new Object[3];
		arg[0] = nbRow;
		arg[1] = nbCol;
		arg[2] = environmentModel;
		Location loc = null;
		try {
			
			loc = loadHelper.getContainerLocation("Main-Container");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		this.startAgent("sim.manager", agentManagerAgent, arg, loc);
		
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
