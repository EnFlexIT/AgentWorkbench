package de.enflexit.common;

import javax.swing.JPanel;

/**
 * The Interface EomVisualizationHostContainer indicates that the visualization components 
 * of the EnergyOptionModel can be hosted here. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface EomVisualizationHostContainer {

	/**
	 * Has to exchange the visualization (e.g. the content pane) by the specified JPanel.
	 * @param newJPanelContent the new JPanel for the content
	 */
	public void exchangeJPanelContent(JPanel newJPanelContent);
	
}
