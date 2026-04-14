package de.enflexit.df.core.model;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;
import de.enflexit.df.core.model.treeNode.DataTreeNodeDataWorkbook;
import de.enflexit.df.core.model.treeNode.DataTreeNodeObjectBase;
import de.enflexit.df.core.ui.ConfigurationPanel;
import de.enflexit.df.core.workbook.DataWorkbook;

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
	 * Returns the currently selected DataTreeNodeObjectBase.
	 * @return the selected data tree node base
	 */
	public DataTreeNodeObjectBase getSelectedDataTreeNodeObjectBase() {
		
		TreePath treePath = this.getSelectedTreePath();
		if (treePath!=null) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
			if (treeNode.getUserObject() instanceof DataTreeNodeObjectBase) {
				return (DataTreeNodeObjectBase) treeNode.getUserObject();
			}
		}
		return null;
	}

	/**
	 * Returns the currently selected {@link DataTreeNodeDataWorkbook} if available.
	 * @return the selected data tree node data source
	 */
	public DataTreeNodeDataWorkbook getSelectedDataTreeNodeDataWorkbook() {
		
		TreePath tpSelected = this.getSelectedTreePath();
		if (tpSelected==null) return null;
		
		Object[] pathNodes = tpSelected.getPath();
		for (int i = pathNodes.length-1; i >=0 ; i--) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pathNodes[i];
			Object userObject = treeNode.getUserObject();
			if (userObject instanceof DataTreeNodeDataWorkbook) {
				return (DataTreeNodeDataWorkbook) userObject;
			}
		}
		return null;
	}
	/**
	 * Returns the currently selected DataWorkbook.
	 * @return the selected DataWorkbook
	 */
	public DataWorkbook getSelectedDataWorkbook() {
		
		DataTreeNodeDataWorkbook dtnoDW = this.getSelectedDataTreeNodeDataWorkbook();
		if (dtnoDW!=null) {
			return dtnoDW.getDataWorkbook();
		}
		return null;
	}
	
	/**
	 * Returns the currently selected {@link AbstractDataTreeNodeDataSource} if available.
	 * @return the selected data tree node data source
	 */
	public AbstractDataTreeNodeDataSource<?> getSelectedDataTreeNodeDataSource() {
		
		TreePath tpSelected = this.getSelectedTreePath();
		if (tpSelected==null) return null;
		
		Object[] pathNodes = tpSelected.getPath();
		for (int i = pathNodes.length-1; i >=0 ; i--) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pathNodes[i];
			Object userObject = treeNode.getUserObject();
			if (userObject instanceof AbstractDataTreeNodeDataSource<?>) {
				return (AbstractDataTreeNodeDataSource<?>) userObject;
			}
		}
		return null;
	}
	
	/**
	 * Returns the first instance of a ConfigurationPanel out of the current path selection.
	 * @return the selected data tree node data source
	 */
	public ConfigurationPanel getSelectedFirstConfigurationPanel() {
		
		TreePath tpSelected = this.getSelectedTreePath();
		if (tpSelected==null) return null;
		
		Object[] pathNodes = tpSelected.getPath();
		for (int i = pathNodes.length-1; i >=0 ; i--) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) pathNodes[i];
			Object userObject = treeNode.getUserObject();
			if (userObject instanceof ConfigurationPanel) {
				return (ConfigurationPanel) userObject;
			}
		}
		return null;
	}
	
}
