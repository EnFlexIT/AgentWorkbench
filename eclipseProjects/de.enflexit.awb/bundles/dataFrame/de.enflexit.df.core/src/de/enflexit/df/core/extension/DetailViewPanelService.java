package de.enflexit.df.core.extension;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.DataTreeModel;


/**
 * The Interface DetailViewPanelService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface DetailViewPanelService {
	
	/**
	 * Has to return the answer to the question 'Do you provide one or more detail views for the current tree node of the {@link DataTreeModel}?'.
	 *
	 * @param dataController the data controller
	 * @param treeNode the currently handled tree node
	 * @return true, if a property configuration will be provided
	 */
	public boolean providesDetailView(DataController dataController, DefaultMutableTreeNode treeNode);
	
	/**
	 * Has to return the properties configuration panel.
	 *
	 * @param dataController the current data controller
	 * @param treeNode the tree node
	 * @return the properties panel
	 */
	public List<DetailViewTabComponent> getDetailViewPanels(DataController dataController, DefaultMutableTreeNode treeNode);

	
	/**
	 * The Class DetailViewTabComponent.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class DetailViewTabComponent {
		
		private String tabTitle;
		private JComponent tabComponent;
		
		/**
		 * Instantiates a new detail tab component.
		 *
		 * @param tabTitle the tab title
		 * @param tabComponent the tab component
		 */
		public DetailViewTabComponent(String tabTitle, JComponent tabComponent) {
			this.setTabTitle(tabTitle);
			this.setTabComponent(tabComponent);
		}

		public String getTabTitle() {
			return tabTitle;
		}
		public void setTabTitle(String tabTitle) {
			this.tabTitle = tabTitle;
		}

		public JComponent getTabComponent() {
			return tabComponent;
		}
		public void setTabComponent(JComponent tabComponent) {
			this.tabComponent = tabComponent;
		}
	}
	
}
