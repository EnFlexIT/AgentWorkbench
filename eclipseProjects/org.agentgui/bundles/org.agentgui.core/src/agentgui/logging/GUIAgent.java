package agentgui.logging;

import org.agentgui.gui.AwbConsoleDialog;

import agentgui.logging.components.SysOutBoard;
import jade.core.Agent;

public class GUIAgent extends Agent {

	private static final long serialVersionUID = -1267455969879282083L;

	@Override
	protected void setup() {
	
		AwbConsoleDialog consoleDialog = SysOutBoard.getConsoleDialog();
		if (consoleDialog!=null) {
			consoleDialog.setVisible(true);
		}
		this.doDelete();
	}
	
	
}
