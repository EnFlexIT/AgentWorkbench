package de.enflexit.awb.ws.ui.server;

import javax.swing.Icon;

/**
 * The Class ServerTreeNodeObject.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class ServerTreeNodeObject {

	/**
	 * Returns the tool tip text.
	 * @return the tool tip text
	 */
	protected abstract String getToolTipText();
	
	/**
	 * Returns the node icon.
	 * @return the node icon
	 */
	protected abstract Icon getNodeIcon();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();

}
