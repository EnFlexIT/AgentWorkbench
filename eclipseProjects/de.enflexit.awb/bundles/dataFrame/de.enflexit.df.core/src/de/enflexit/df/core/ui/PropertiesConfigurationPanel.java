package de.enflexit.df.core.ui;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.df.core.model.DataController;


/**
 * The Interface PropertiesConfigurationPanel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface PropertiesConfigurationPanel {
	
	/**
	 * has to return the title of the properties configuration panel.
	 * @return the title
	 */
	public String getPropertiesTitle();

	/**
	 * Has to return the answer to the question 'Do you provide properties for the current tree node?'.
	 *
	 * @param dataController the data controller
	 * @param treeNode the currently handled tree node
	 * @return true, if a property configuration will be provided
	 */
	public boolean providesPropertiesConfiguration(DataController dataController, DefaultMutableTreeNode treeNode);
	
	/**
	 * Has to return the properties configuration panel.
	 *
	 * @param dataController the current data controller
	 * @param treeNode the tree node
	 * @return the properties panel
	 */
	public JComponent getPropertiesConfigurationPanel(DataController dataController, DefaultMutableTreeNode treeNode);
	
}
