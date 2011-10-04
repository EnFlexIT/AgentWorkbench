package jade.debugging;

import jade.core.Agent;
import jade.debugging.components.JFrame4Consoles;
import jade.debugging.components.SysOutBoard;

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
