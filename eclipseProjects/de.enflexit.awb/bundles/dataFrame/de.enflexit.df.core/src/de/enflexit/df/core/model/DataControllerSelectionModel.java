package de.enflexit.df.core.model;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * The Class DataControllerSelectionModel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataControllerSelectionModel {

	private DataController dataController;
	
	private TreePath selectedTreePath;
	
	/**
	 * Instantiates a new data controller selection model.
	 * @param dataController the data controller
	 */
	public DataControllerSelectionModel(DataController dataController) {
		this.setDataController(dataController);
	}
	
	/**
	 * Returns the data controller.
	 * @return the data controller
	 */
	private DataController getDataController() {
		return dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	private void setDataController(DataController dataController) {
		this.dataController = dataController;
	}

	
	/**
	 * Returns the currently selected tree path.
	 * @return the tree path selected
	 */
	public TreePath getSelectedTreePath() {
		return selectedTreePath;
	}
	/**
	 * Sets the currently selected tree path.
	 * @param newSelectedTreePath the new tree path selected
	 */
	public void setSelectedTreePath(TreePath newSelectedTreePath) {

		TreePath oldSelectedTreePath = null;
		if (this.selectedTreePath!=null) {
			oldSelectedTreePath = this.selectedTreePath;
		}
		this.selectedTreePath = newSelectedTreePath;
		this.getDataController().firePropertyChange(DataController.DC_NEW_TREE_PATH_SELECTED, oldSelectedTreePath, newSelectedTreePath);
	}

	/**
	 * Returns the currently selected {@link DataTreeNodeDataSource} if available.
	 * @return the selected data tree node data source
	 */
	public DataTreeNodeDataSource<?> getSelectedDataTreeNodeDataSource() {
		
		TreePath tpSelected = this.getSelectedTreePath();
		if (tpSelected==null) return null;
		
		Object[] pathNodes = tpSelected.getPath();
		for (int i = pathNodes.length-1; i >=0 ; i--) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pathNodes[i];
			Object userObject = treeNode.getUserObject();
			if (userObject instanceof DataTreeNodeDataSource<?>) {
				return (DataTreeNodeDataSource<?>) userObject;
			}
		}
		return null;
	}
	
	
	
}
