package game_of_life.agents;

import jade.core.Agent;
import jade.wrapper.StaleProxyException;

public class StartAgent extends Agent{

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		
		// --- Size of the Array for the Game of Life -----
		// --- good: 144 / 576 / 1296 / 2304 / 3600 / 5184
		int no_of_agents = 3600; // --- approx. depends on rounding ---
		int stretch_factor = (int) Math.round( Math.sqrt(no_of_agents/144) );
		
		// --- Think about 16:9-ratio of the monitor ------
//		int nbCol = 5;
//		int nbRow = 5;
		int nbCol = stretch_factor * 16;
		int nbRow = stretch_factor * 9;
			
		// --- Start-Arg. for the manager-agent ----------- 
		Object [] arg = new Object[2];
		arg[0] = nbRow;
		arg[1] = nbCol;

		// --- Start the 'simulationManagerAgent' --------- 
		try {
			this.getContainerController().createNewAgent("sim.manager", game_of_life.agents.SimulationManagerAgent.class.getName(), arg).start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		// --- Ready! This Agent can die now --------------
		this.doDelete();
	}
	
}