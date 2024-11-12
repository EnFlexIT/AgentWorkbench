package de.enflexit.awb.core.project.plugins;

import de.enflexit.awb.core.project.Project;

/**
 * This class is used within the observer pattern of projects. It informs
 * about changes regarding a plug-in.
 * 
 * @see Project
 * @see Project#setNotChangedButNotify(Object)
 * 
 * @see PlugIn#CHANGED
 * @see PlugIn#ADDED
 * @see PlugIn#REMOVED
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class PlugInNotification {

	private PlugIn plugIn = null;
	private int updateReason = 0; 
	
	/**
	 * Instantiates a new plug in notification.
	 *
	 * @param reason the reason
	 * @param plugIn the plug in
	 */
	public PlugInNotification(int reason, PlugIn plugIn) {
		this.setUpdateReason(reason);
		this.setPlugIn(plugIn);
	}
	
	/**
	 * Sets the update reason.
	 *
	 * @param updateReason the new update reason
	 */
	public void setUpdateReason(int updateReason) {
		this.updateReason = updateReason;
	}
	
	/**
	 * Gets the update reason.
	 *
	 * @return the update reason
	 */
	public int getUpdateReason() {
		return updateReason;
	}
	
	/**
	 * Sets the plug in.
	 *
	 * @param plugIn the new plug in
	 */
	public void setPlugIn(PlugIn plugIn) {
		this.plugIn = plugIn;
	}
	
	/**
	 * Gets the plug in.
	 *
	 * @return the plug in
	 */
	public PlugIn getPlugIn() {
		return plugIn;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PlugIn.CHANGED;
	}
	
}
