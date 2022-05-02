package de.enflexit.awb.ws.ui.server;

import javax.swing.Icon;

/**
 * The Class ServerTreeNodeRoot.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTreeNodeRoot extends ServerTreeNodeObject {

	@Override
	protected String getToolTipText() {
		return "Registered Server & Handler";
	}

	@Override
	protected Icon getNodeIcon() {
		return null;
	}

	@Override
	public String toString() {
		return "Registered Server & Handler";
	}

}
