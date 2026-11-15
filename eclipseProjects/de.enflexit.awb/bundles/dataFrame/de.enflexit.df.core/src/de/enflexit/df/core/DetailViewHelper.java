package de.enflexit.df.core;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.common.ServiceFinder;
import de.enflexit.df.core.extension.DetailViewPanelService;
import de.enflexit.df.core.extension.DetailViewPanelService.DetailViewTabComponent;
import de.enflexit.df.core.model.DataController;

/**
 * The Class DetailViewHelper.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DetailViewHelper {

	/**
	 * Returns the list of available DetailViewPanelService.
	 * @return the detail view panel list
	 */
	public static List<DetailViewPanelService> getDetailViewPanelServiceList() {
		return ServiceFinder.findServices(DetailViewPanelService.class);
	}
	/**
	 * Returns the list of DetailViewPanelService for the specified tree node.
	 *
	 * @param dc the {@link DataController} to consider
	 * @param treeNode the current tree node
	 * @return the detail view panel list for tree node
	 */
	public static List<DetailViewPanelService> getDetailViewPanelServiceListForTreeNode(DataController dc, DefaultMutableTreeNode treeNode) {
		
		List<DetailViewPanelService> dvListAvailable = DetailViewHelper.getDetailViewPanelServiceList();
		List<DetailViewPanelService> dvListToUse = new ArrayList<>();
		for (DetailViewPanelService dv : dvListAvailable) {
			try {
				if (dv.providesDetailView(dc, treeNode)==true) {
					dvListToUse.add(dv);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return dvListToUse;
	}

	/**
	 * Returns the detail tab component list.
	 *
	 * @param dc the current DataController
	 * @param treeNode the tree node
	 * @return the detail tab component list
	 */
	public static List<DetailViewTabComponent> getDetailViewTabComponentList(DataController dc, DefaultMutableTreeNode treeNode) {
		
		// --- Get the relevant services ----------------------------
		List<DetailViewPanelService> dvProviderList = DetailViewHelper.getDetailViewPanelServiceListForTreeNode(dc, treeNode);
		if (dvProviderList==null || dvProviderList.size()==0) return null;
		
		List<DetailViewTabComponent> dvTabList = new ArrayList<>();
		for (DetailViewPanelService dvProvider : dvProviderList) {
			try {
				if (dvProvider.providesDetailView(dc, treeNode)==false) continue;
				List<DetailViewTabComponent> dvTabListProvider = dvProvider.getDetailViewPanels(dc, treeNode);
				if (dvTabListProvider!=null && dvTabListProvider.size()>0) {
					dvTabList.addAll(dvTabListProvider);
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return dvTabList;
	}
	
}
