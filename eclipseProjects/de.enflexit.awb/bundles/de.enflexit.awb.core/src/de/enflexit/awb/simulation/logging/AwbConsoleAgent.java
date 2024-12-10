package de.enflexit.awb.simulation.logging;

import de.enflexit.awb.core.ui.AwbConsoleDialog;
import jade.core.Agent;

/**
 * The Class AwbConsoleAgent.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbConsoleAgent extends Agent {

	private static final long serialVersionUID = -1267455969879282083L;

	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
	
		AwbConsoleDialog consoleDialog = SysOutBoard.getConsoleDialog();
		if (consoleDialog!=null) {
			consoleDialog.setVisible(true);
		}
		this.doDelete();
	}
	
	
}
