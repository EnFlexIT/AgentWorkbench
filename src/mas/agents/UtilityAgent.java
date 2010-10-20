package mas.agents;

import jade.core.Agent;
import mas.Platform;
import mas.agents.behaviour.PlatformShutdownBehaviour;
import mas.agents.behaviour.ShowDFBehaviour;
import mas.agents.behaviour.ShowLoadMonitorBehaviour;

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
			this.addBehaviour(new ShowDFBehaviour());
			break;

		case Platform.UTIL_CMD_ShutdownPlatform:
			this.addBehaviour(new PlatformShutdownBehaviour());
			break;
			
		case Platform.UTIL_CMD_OpenLoadMonitor:
			this.addBehaviour(new ShowLoadMonitorBehaviour());
			break;
			
		default:
			this.doDelete();
		}
		
	}

}
