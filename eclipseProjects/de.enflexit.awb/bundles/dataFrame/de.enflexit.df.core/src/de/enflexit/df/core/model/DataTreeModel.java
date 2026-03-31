package de.enflexit.df.core.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.enflexit.common.dataSources.AbstractDataSource;
import de.enflexit.common.dataSources.CsvDataSource;
import de.enflexit.common.dataSources.DatabaseDataSource;
import de.enflexit.common.dataSources.ExcelDataSource;

/**
 * The Class DataTreeModel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeModel extends DefaultTreeModel implements PropertyChangeListener {

	private static final long serialVersionUID = 3017762238004711124L;

	private DataController dataController;
	
	/**
	 * Instantiates a new data tree node model.
	 * @param root the root
	 */
	public DataTreeModel() {
		this(null);
	}
	/**
	 * Instantiates a new data tree node model.
	 * @param root the root
	 */
	public DataTreeModel(DataController dataController) {
		super(new DefaultMutableTreeNode(new DataTreeNodeBase("RootNode")));
		this.setDataController(dataController);
	}
	
	/**
	 * Returns the root node.
	 * @return the root node
	 */
	public DefaultMutableTreeNode getRootNode() {
		return (DefaultMutableTreeNode) this.getRoot();
	}
	
	/**
	 * Returns the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		if (this.dataController!=null) {
			this.dataController.removePropertyChangeListener(this);
		}
		this.dataController = dataController;
		if (this.dataController!=null) this.dataController.addPropertyChangeListener(this);
	}
	
	/**
	 * Searches for tree nodes that matches the specified search filter.
	 *
	 * @param searchFilter the search filter
	 * @return the default mutable tree node
	 */
	public List<DefaultMutableTreeNode> searchTreeNode(DataTreeModelSearchFilter searchFilter) {
		return this.searchTreeNode(this.getRootNode(), searchFilter);
	}
	/**
	 * Searches for tree nodes that matches the specified search filter.
	 *
	 * @param treeNode the tree node
	 * @param searchFilter the search filter
	 * @return the default mutable tree node
	 */
	public List<DefaultMutableTreeNode> searchTreeNode(DefaultMutableTreeNode treeNode, DataTreeModelSearchFilter searchFilter) {
		
		// --- Define result list -----------------------------------
		List<DefaultMutableTreeNode> resultList = new ArrayList<>();
		
		// --- Check the specified node -----------------------------
		if (searchFilter.matchesFilterCriteria(treeNode)==true) {
			resultList.add(treeNode);
		}
		
		// --- Check for child nodes --------------------------------
		if (treeNode.getChildCount()>0) {
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				// --- Get the child nodes of the current node ------
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) treeNode.getChildAt(i);
				List<DefaultMutableTreeNode> resultListChild = this.searchTreeNode(childNode, searchFilter);
				if (resultListChild!=null && resultListChild.size()>0) {
					resultList.addAll(resultListChild);
				}
				
			}
		}

		// --- Prepare result value ---------------------------------
		if (resultList.size()==0) {
			return null;
		}
		return resultList;
	}
	
	
	/**
	 * Adds the specified data source as a tree node.
	 * @param dataSource the data source
	 */
	private void addedDataSource(AbstractDataSource dataSource) {
		
		DataTreeNodeDataSource<?> ds = null;
		if (dataSource instanceof CsvDataSource) {
			ds = new DataTreeNodeDataSourceCsv((CsvDataSource) dataSource);
		} else if (dataSource instanceof ExcelDataSource) {
			ds = new DataTreeNodeDataSourceExcel((ExcelDataSource) dataSource);
		} else if (dataSource instanceof DatabaseDataSource) {
			ds = new DataTreeNodeDataSourceDatabase((DatabaseDataSource) dataSource);
		}
		
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ds);
		this.getRootNode().add(newNode);
		
		Object[] pathToParent = this.getRootNode().getPath();
		int[] newIndicies = {this.getRootNode().getIndex(newNode)};
		Object[] newChildren = {newNode}; 
		this.fireTreeNodesInserted(this, pathToParent, newIndicies, newChildren);
	}
	
	/**
	 * Removes the data source.
	 * @param dataSource the data source
	 */
	private void removedDataSource(final AbstractDataSource dataSource) {
		
		// --- Define a search filter for the search traversal ----------------
		DataTreeModelSearchFilter searchFilter = new DataTreeModelSearchFilter() {
			@Override
			public boolean matchesFilterCriteria(DefaultMutableTreeNode treeNode) {
				
				DataTreeNodeBase dtno = (DataTreeNodeBase) treeNode.getUserObject();
				if (dtno instanceof DataTreeNodeDataSource<?>) {
					
					DataTreeNodeDataSource<?> dtnoDataSource = (DataTreeNodeDataSource<?>) dtno;
					if (dtnoDataSource.getDataSource() == dataSource) {
						return true;
					}
				}
				return false;
			}
		};
		// --- Search the tree for the data source ----------------------------
		List<DefaultMutableTreeNode> treeNodeResultList = this.searchTreeNode(searchFilter);
		if (treeNodeResultList==null) return;
		
		// --- Remove the nodes found -----------------------------------------
		for (DefaultMutableTreeNode treeNode : treeNodeResultList) {
			
			DefaultMutableTreeNode parentNode =  (DefaultMutableTreeNode) treeNode.getParent();
			
			Object[] pathToParent = parentNode.getPath();
			int[] removedIndicies = {parentNode.getIndex(treeNode)};
			Object[] removedChildren = {treeNode}; 

			parentNode.remove(treeNode);

			this.fireTreeNodesRemoved(this, pathToParent, removedIndicies, removedChildren);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		AbstractDataSource ds = null;
		switch (evt.getPropertyName()) {
		case DataController.DC_ADDED_DATA_SOURCE:
			ds = (AbstractDataSource) evt.getNewValue();
			this.addedDataSource(ds);
			break;
			
		case DataController.DC_REMOVED_DATA_SOURCE:
			ds = (AbstractDataSource) evt.getOldValue();
			this.removedDataSource(ds);
			break;
		}
	}
	
}
