package de.enflexit.awb.core.update;

import de.enflexit.awb.core.Application;

/**
 * The Class UpdateCheckStatus.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UpdateCheckStatus {
	
	private boolean isPending;
	private boolean isAvailable;
	private String version;
	
	public boolean isPending() {
		return isPending;
	}

	public void setPending(boolean pending) {
		this.isPending = pending;
	}
	public boolean isUpdateAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getLastCheck() {
		return Application.getGlobalInfo().getUpdateDateLastChecked();
	}

	public void setLastCheck(long lastCheck) {
		Application.getGlobalInfo().setUpdateDateLastChecked(lastCheck);
	}	
}