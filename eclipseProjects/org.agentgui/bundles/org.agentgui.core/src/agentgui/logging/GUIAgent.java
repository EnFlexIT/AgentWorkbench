package agentgui.logging;

import org.agentgui.gui.ConsoleDialog;

import agentgui.logging.components.SysOutBoard;
import jade.core.Agent;

public class GUIAgent extends Agent {

	private static final long serialVersionUID = -1267455969879282083L;

	@Override
	protected void setup() {
	
		ConsoleDialog consoleDialog = SysOutBoard.getConsoleDialog();
		if (consoleDialog!=null) {
			consoleDialog.setVisible(true);
		}
		this.doDelete();
	}
	
	
}
