package de.enflexit.awb.ws.ui;

/**
 * The Interface WsConfigurationInterface.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface WsConfigurationInterface {

	/**
	 * Checks for unsaved changes.
	 * @return true, if there are unsaved changes
	 */
	public boolean hasUnsavedChanges();
	
	/**
	 * Has to return, if the user confirmed to change the current view or to close the current dialog.
	 * @return true, if the user agreed to change the view
	 */
	public boolean userConfirmedToChangeView();
	
}
