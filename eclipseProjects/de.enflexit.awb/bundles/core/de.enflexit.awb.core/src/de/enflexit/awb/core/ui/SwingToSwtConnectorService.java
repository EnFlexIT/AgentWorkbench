package de.enflexit.awb.core.ui;

/**
 * The Interface SwingToSwtConnectorService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface SwingToSwtConnectorService {

	/**
	 * Has to show the eclipse workbench window.
	 */
	public void showEclipseWorkbench();
	
	/**
	 * Has to close and dispose the eclipse workbench window.
	 */
	public void disposeEclipseWorkbench();
	
	/**
	 * Has to show the eclipse preferences.
	 */
	public void showEclipsePreferences();

	/**
	 * Has to show the eclipse about.
	 */
	public void showEclipseAbout();

	/**
	 * Has to show the eclipse check for updates.
	 */
	public void showEclipseCheckForUpdates();
	
	/**
	 * Has to show the eclipse install new software dialog.
	 */
	public void showEclipseInstallNewSoftware();
	
}
