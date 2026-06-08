package de.enflexit.awb.ws.core.util;

import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.server.AwbServer;

/**
 * The Class UpdateCheckStatus.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UpdateCheckStatusWebApp {
	
	private boolean isPending;
	private boolean isAvailable;
	private String newVersion;
	private String currentVersion;
	
	/**
	 * Checks if the update check is pending.
	 *
	 * @return true, if is pending
	 */
	public boolean isPending() {
		return isPending;
	}

	/**
	 * Sets the pending.
	 *
	 * @param pending true if the update check is pending
	 */
	public void setPending(boolean pending) {
		this.isPending = pending;
	}
	
	/**
	 * Checks if an update is available.
	 *
	 * @return true, if is update available
	 */
	public boolean isUpdateAvailable() {
		return isAvailable;
	}

	/**
	 * Sets wether an update is available.
	 *
	 * @param isAvailable true if an update is available
	 */
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	/**
	 * Returns the new version string.
	 *
	 * @return the new version
	 */
	public String getNewVersion() {
		return newVersion;
	}

	/**
	 * Sets the new version string.
	 *
	 * @param version the new version
	 */
	public void setNewVersion(String version) {
		this.newVersion = version;
	}

	/**
	 * Returns the date and time of the last update check.
	 *
	 * @return the last check
	 */
	public long getLastCheck() {
		return JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfiguration().getWebApplicationSettings().getUpdateLastCheck();
	}

	/**
	 * Sets the date and time of the last update check.
	 *
	 * @param lastCheck the new last check
	 */
	public void setLastCheck(long lastCheck) {
		JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfiguration().getWebApplicationSettings().setUpdateLastCheck(lastCheck);
		JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfiguration().save();
	}

	/**
	 * @return the currentVersion
	 */
	public String getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * @param currentVersion the currentVersion to set
	 */
	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}	
}