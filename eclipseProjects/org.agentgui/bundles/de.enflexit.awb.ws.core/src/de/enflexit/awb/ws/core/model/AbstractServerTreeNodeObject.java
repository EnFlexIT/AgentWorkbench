package de.enflexit.awb.ws.core.model;

import javax.swing.Icon;

/**
 * The Class AbstractServerTreeNodeObject.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractServerTreeNodeObject {

	/**
	 * Returns the tool tip text.
	 * @return the tool tip text
	 */
	public abstract String getToolTipText();
	
	/**
	 * Returns the node icon.
	 * @return the node icon
	 */
	public abstract Icon getNodeIcon();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();

}
