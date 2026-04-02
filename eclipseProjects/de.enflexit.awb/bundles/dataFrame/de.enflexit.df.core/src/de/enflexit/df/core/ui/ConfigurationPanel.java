package de.enflexit.df.core.ui;

import java.util.List;

import javax.swing.JComponent;

/**
 * The Interface ConfigurationPanel.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface ConfigurationPanel {

	/**
	 * Has to return the configuration toolbar components.
	 * @return the configuration toolbar components to show
	 */
	public List<JComponent> getConfigurationToolbarComponents();
	
	/**
	 * Has to return the main configuration panel.
	 * @return the configuration panel
	 */
	public JComponent getConfigurationPanel();
	
}
