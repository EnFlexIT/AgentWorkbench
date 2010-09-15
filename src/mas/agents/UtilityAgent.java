package mas.agents;

import jade.core.Agent;
import mas.Platform;
import mas.agents.behaviour.PlatformShutdownBehaviour;
import mas.agents.behaviour.ShowDFBehaviour;

public class UtilityAgent extends Agent {

	private static final long serialVersionUID = 4018534357973603L;

	@Override
	protected void setup() {
		super.setup();
		
		Object[] args = getArguments();
		if (args==null || args.length==0) {
			this.doDelete();
		}
		
		Integer start4 = (Integer) args[0];
		switch (start4) {
		case Platform.UTIL_CMD_OpenDF:
			addBehaviour(new ShowDFBehaviour());
			break;

		case Platform.UTIL_CMD_ShutdownPlatform:
			addBehaviour(new PlatformShutdownBehaviour());
			break;
			
		default:
			break;
		}
		
	}

}
