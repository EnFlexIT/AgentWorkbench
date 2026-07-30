package de.enflexit.df.core.ui;

import java.util.List;

import javax.swing.JComponent;

/**
 * The Interface DataSourceConfigurationPanel.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface DataSourceConfigurationPanel {

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
	
	/**
	 * Has to reset the configuration panel.
	 */
	public void resetConfigurationPanel();
	
	
	
	/**
	 * Has to return a panel that is to be displayed in the detail area.
	 * @return the configuration panel for sub elements
	 */
	public JComponent getDetailViewPanel();
	
	/**
	 * Has to reset the panel that is to be displayed in the detail area.
	 */
	public void resetDetailViewPanel();
	
}
