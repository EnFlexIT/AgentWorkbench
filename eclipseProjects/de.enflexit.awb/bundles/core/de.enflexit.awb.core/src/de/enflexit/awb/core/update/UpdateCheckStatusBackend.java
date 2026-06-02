package de.enflexit.awb.core.update;

import de.enflexit.awb.core.Application;

/**
 * The Class UpdateCheckStatusBackend
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UpdateCheckStatusBackend {
	
	private boolean isPending;
	private boolean isAvailable;
	
	/**
	 * Returns whether the update check is pending.
	 *
	 * @return true, if is pending
	 */
	public boolean isPending() {
		return isPending;
	}

	/**
	 * Set whether the update check is pending.
	 *
	 * @param pending true if the update check is pending
	 */
	public void setPending(boolean pending) {
		this.isPending = pending;
	}
	
	/**
	 * Returns if an update is available.
	 *
	 * @return true, if is update available
	 */
	public boolean isUpdateAvailable() {
		return isAvailable;
	}

	/**
	 * Set whether an update is available.
	 *
	 * @param isAvailable true if there is an update available
	 */
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	/**
	 * Returns the date and time of the last update check.
	 *
	 * @return the last check
	 */
	public long getLastCheck() {
		return Application.getGlobalInfo().getUpdateDateLastChecked();
	}

	/**
	 * Sets the date and time of the last update check.
	 *
	 * @param lastCheck the new last check
	 */
	public void setLastCheck(long lastCheck) {
		Application.getGlobalInfo().setUpdateDateLastChecked(lastCheck);
		Application.getGlobalInfo().doSaveConfiguration();
	}	
}