package de.enflexit.awb.core.update;


/**
 * The Class UpdateCheckStatus.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UpdateCheckStatus {
	
	private boolean isPending;
	private boolean isAvailable;
	private String version;
	private long lastCheck;
	
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
		return lastCheck;
	}

	public void setLastCheck(long lastCheck) {
		this.lastCheck = lastCheck;
	}	
}