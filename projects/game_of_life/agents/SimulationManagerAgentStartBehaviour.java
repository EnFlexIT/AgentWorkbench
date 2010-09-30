package game_of_life.agents;

import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.StaleProxyException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class SimulationManagerAgentStartBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -2482874669126418389L;
	
	private HashMap<String, Integer> localEnvModel = null;
	
	private String agentGameOfLifeClass = game_of_life.agents.GameOfLifeAgent.class.getName();
	private Integer agents2Start = 0;
	private Integer agents2StartNotfyInterval = 0;
	private Integer agentsStarted = 0;
	private Integer agentsStartedOld = 0;
	
	public SimulationManagerAgentStartBehaviour(HashMap<String, Integer> envModel) {
		super();
		this.localEnvModel = envModel;
		this.agents2Start = envModel.size();
		this.agents2StartNotfyInterval = this.agents2Start / 10;
	}

	@Override
	public void action() {

		Vector<String> cellKeys = new Vector<String>( localEnvModel.keySet() );
		Collections.sort(cellKeys);
		Iterator<String> it = cellKeys.iterator();
		while (it.hasNext()) {
			// --- Namen des Agenten ermitteln ------------
			String agentName = it.next();
			// --- Startargument zusammenbauen ------------
			Object startArg[] = new Object[2];			
			// --- Die Nachbarn, die interessant sind -----
			startArg[0] = getNeighbourVector(agentName);
			// --- Agenten starten ------------------------ 
			try {
				this.myAgent.getContainerController().createNewAgent(agentName, agentGameOfLifeClass, startArg).start();
			} catch (StaleProxyException e) {
				System.out.println("Failure: Agents started: " + agentsStarted);
				e.printStackTrace();				
				return;
			}
			// --- Count the number of agents started -----
			agentsStarted++;
			if (agentsStarted>=agentsStartedOld+agents2StartNotfyInterval) {
				System.out.println(agentsStarted + " of  " + agents2Start + " started.");
				agentsStartedOld+=agents2StartNotfyInterval;
			}
		} // --- end while ---
		System.out.println("Ready: " + agentsStarted + " of  " + agents2Start + " started.");
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
					if (localEnvModel.containsKey(checkName)) {
						neighbour.add(checkName);
					}
				}
			}
		}
		return neighbour;
	}
	

}
