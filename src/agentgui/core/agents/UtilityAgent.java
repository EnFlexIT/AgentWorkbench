package agentgui.core.agents;

import agentgui.core.agents.behaviour.PlatformShutdownBehaviour;
import agentgui.core.agents.behaviour.ShowDFBehaviour;
import agentgui.core.agents.behaviour.ShowLoadMonitorBehaviour;
import agentgui.core.jade.Platform;
import jade.core.Agent;

public class UtilityAgent extends Agent {

	private static final long serialVersionUID = 4018534357973603L;

	@Override
	protected void setup() {
		super.setup();
		
		Object[] args = getArguments();
		if (args==null || args.length==0) {
			this.doDelete();
			return;
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
