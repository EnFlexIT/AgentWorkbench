package agentgui.logging;

import agentgui.logging.components.JFrame4Consoles;
import agentgui.logging.components.SysOutBoard;
import jade.core.Agent;

public class GUIAgent extends Agent {

	private static final long serialVersionUID = -1267455969879282083L;

	@Override
	protected void setup() {
	
		JFrame4Consoles displayFrame = SysOutBoard.getJFrame4Display();
		if (displayFrame!=null) {
			displayFrame.setVisible(true);
		}
		this.doDelete();
	}
	
	
}
