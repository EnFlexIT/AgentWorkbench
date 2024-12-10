package de.enflexit.awb.core.ui;

/**
 * The Interface AwbTrayIcon.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbTrayIcon {

	
	/**
	 * Has to dispose the tray icon. Typically, if the application is in shutdown.
	 */
	public void dispose();

	/**
	 * Has to refresh the appearance of the tray icon.
	 */
	public void refreshView();
	
	
}
