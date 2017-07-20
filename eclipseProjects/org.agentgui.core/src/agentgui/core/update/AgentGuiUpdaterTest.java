package agentgui.core.update;

public class AgentGuiUpdaterTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AgentGuiUpdater(true, "http://preupdate.agentgui.org/?key=xml").start();
//		new AgentGuiUpdater().start();
	}

}
