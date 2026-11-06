package de.enflexit.df.core;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.common.ServiceFinder;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.PropertiesConfigurationPanel;

/**
 * The Class PropertiesConfigurationHelper.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertiesConfigurationHelper {

	/**
	 * Returns the list of available PropertiesConfigurationPanel.
	 * @return the properties configuration panel list
	 */
	public static List<PropertiesConfigurationPanel> getPropertiesConfigurationPanelList() {
		return ServiceFinder.findServices(PropertiesConfigurationPanel.class);
	}
	
	/**
	 * Returns the properties configuration panel by its title.
	 *
	 * @param searchTitle the search title
	 * @return the properties configuration panel by title
	 */
	public static PropertiesConfigurationPanel getPropertiesConfigurationPanelByTitle(String searchTitle) {
		
		List<PropertiesConfigurationPanel> pcpList = PropertiesConfigurationHelper.getPropertiesConfigurationPanelList();
		if (pcpList==null || pcpList.size()==0) return null;

		for (PropertiesConfigurationPanel pcp : pcpList) {
			if (pcp.getPropertiesTitle().equalsIgnoreCase(searchTitle.toLowerCase())==true) {
				return pcp;
			}
		}
		return null;
	}

	/**
	 * Returns the properties configuration panel list for the specified tree node.
	 *
	 * @param dc the {@link DataController} to consider
	 * @param treeNode the current tree node
	 * @return the properties configuration panel list for tree node
	 */
	public static List<PropertiesConfigurationPanel> getPropertiesConfigurationPanelListForTreeNode(DataController dc, DefaultMutableTreeNode treeNode) {
		
		List<PropertiesConfigurationPanel> pcpListAvailable = getPropertiesConfigurationPanelList();
		List<PropertiesConfigurationPanel> pcpListToUse = new ArrayList<>();
		for (PropertiesConfigurationPanel pcp : pcpListAvailable) {
			try {
				if (pcp.providesPropertiesConfiguration(dc, treeNode)==true) {
					pcpListToUse.add(pcp);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return pcpListToUse;
	}
	
	
}
