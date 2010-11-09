package agentgui.core.sim.start;

import jade.core.Agent;

public class SimStartAgent extends Agent {

	private static final long serialVersionUID = 7768569966599564839L;

	public final static int BASE_ACTION_Start = 0; 
	public final static int BASE_ACTION_Pause = 1;
	public final static int BASE_ACTION_Stop = 2;
	
	@Override
	protected void setup() {
		super.setup();
		
		Object[] startArgs = getArguments();
		int startArg = (Integer) startArgs[0];
		
		switch (startArg) {
		case BASE_ACTION_Start:
			this.addBehaviour(new SimStartBehaviour());
			break;
		case BASE_ACTION_Pause:
			System.out.println("Pausiere die Simulation .....");
			break;
		case BASE_ACTION_Stop:
			System.out.println("Stop die Simulation .....");
			break;
		}
		
	}
	
	
	
	
	
}
