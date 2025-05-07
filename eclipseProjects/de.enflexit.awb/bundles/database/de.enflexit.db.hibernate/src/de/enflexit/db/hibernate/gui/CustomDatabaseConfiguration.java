package de.enflexit.db.hibernate.gui;

import java.awt.Component;

/**
 * The Interface CustomDatabaseConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface CustomDatabaseConfiguration {

	/**
	 * Has to return the name of the configuration (will also be used as tab header).
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Has to return the UI component.
	 * @return the UI component
	 */
	public Component getUIComponent();

	/**
	 * Has to dispose the custom UI component.
	 */
	public void disposeUIComponent();
	
	/**
	 * Has to answer the question, if changes were made in the configuration.
	 * @return true, if changes were made
	 */
	public boolean hasChangedConfiguration();
	
	/**
	 * Has to save the current configuration.
	 * @return true, if successful
	 */
	public boolean saveConfiguration();
	
}
