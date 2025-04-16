package de.enflexit.awb.ws.core.model;

import javax.swing.Icon;

/**
 * The Class ServerTreeNodeRoot.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTreeNodeRoot extends AbstractServerTreeNodeObject {

	@Override
	public String getToolTipText() {
		return "Registered Server & Handler";
	}

	@Override
	public Icon getNodeIcon() {
		return null;
	}

	@Override
	public String toString() {
		return "Registered Server & Handler";
	}

}
