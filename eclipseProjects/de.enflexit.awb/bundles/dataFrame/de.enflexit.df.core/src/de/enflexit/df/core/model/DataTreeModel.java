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
import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;
import de.enflexit.df.core.model.treeNode.DataTreeNodeDataSourceCsv;
import de.enflexit.df.core.model.treeNode.DataTreeNodeDataSourceDatabase;
import de.enflexit.df.core.model.treeNode.DataTreeNodeDataSourceExcel;
import de.enflexit.df.core.model.treeNode.DataTreeNodeDataWorkbook;
import de.enflexit.df.core.model.treeNode.DataTreeNodeObjectBase;
import de.enflexit.df.core.workbook.DataWorkbook;

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
		super(new DefaultMutableTreeNode(new DataTreeNodeObjectBase("RootNode")));
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
	 * Returns a DataTreeModelSearchFilter for the specified {@link DataWorkbook}.
	 *
	 * @param dataWorkbook the data workbook
	 * @return the search filter 4 data workbook
	 */
	private DataTreeModelSearchFilter getSearchFilter4DataWorkbook(final DataWorkbook dataWorkbook) {
	
		return new DataTreeModelSearchFilter() {
			@Override
			public boolean matchesFilterCriteria(DefaultMutableTreeNode treeNode) {
				DataTreeNodeObjectBase dtno = (DataTreeNodeObjectBase) treeNode.getUserObject();
				if (dtno instanceof DataTreeNodeDataWorkbook) {
					DataTreeNodeDataWorkbook dtnoDW = (DataTreeNodeDataWorkbook) dtno;
					if (dtnoDW.getDataWorkbook() == dataWorkbook) {
						return true;
					}
				}
				return false;
			}
		};
	}
	/**
	 * Returns a DataTreeModelSearchFilter for the specified {@link AbstractDataSource}.
	 *
	 * @param dataSource the data source
	 * @return the search filter 4 data source
	 */
	private DataTreeModelSearchFilter getSearchFilter4DataSource(final AbstractDataSource dataSource) {
		
		return new DataTreeModelSearchFilter() {
			@Override
			public boolean matchesFilterCriteria(DefaultMutableTreeNode treeNode) {
				DataTreeNodeObjectBase dtno = (DataTreeNodeObjectBase) treeNode.getUserObject();
				if (dtno instanceof AbstractDataTreeNodeDataSource<?>) {
					AbstractDataTreeNodeDataSource<?> dtnoDataSource = (AbstractDataTreeNodeDataSource<?>) dtno;
					if (dtnoDataSource.getDataSource() == dataSource) {
						return true;
					}
				}
				return false;
			}
		};
	}
	
	/**
	 * Removes the specified list of tree nodes and fires events accordingly.
	 * @param treeNodeResultList the tree node result list
	 */
	private void removeTreeNodes(List<DefaultMutableTreeNode> treeNodeResultList) {
		if (treeNodeResultList==null) return;
		treeNodeResultList.forEach(tn -> this.removeTreeNode(tn));
	}
	/**
	 * Removes the specified tree node from the tree and fires events accordingly.
	 * @param treeNodeToRemove the tree node to remove
	 */
	private void removeTreeNode(DefaultMutableTreeNode treeNodeToRemove) {
		
		if (treeNodeToRemove==null) return;
		DefaultMutableTreeNode parentNode =  (DefaultMutableTreeNode) treeNodeToRemove.getParent();
		
		Object[] pathToParent = parentNode.getPath();
		int[] removedIndicies = {parentNode.getIndex(treeNodeToRemove)};
		Object[] removedChildren = {treeNodeToRemove}; 

		parentNode.remove(treeNodeToRemove);

		this.fireTreeNodesRemoved(this, pathToParent, removedIndicies, removedChildren);
	}
	
		
	/**
	 * Add the specified DataWorkbook as a tree node.
	 * @param dataWorkbook the data workbook
	 */
	private void addedDataWorkbook(DataWorkbook dataWorkbook) {

		DataTreeNodeDataWorkbook dtnoDW = new DataTreeNodeDataWorkbook(this.getDataController(), dataWorkbook);
		
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dtnoDW);
		this.getRootNode().add(newNode);
		
		Object[] pathToParent = this.getRootNode().getPath();
		int[] newIndicies = {this.getRootNode().getIndex(newNode)};
		Object[] newChildren = {newNode}; 
		this.fireTreeNodesInserted(this, pathToParent, newIndicies, newChildren);
	}
	
	/**
	 * React on an opened data workbook.
	 * @param dataWorkbook the data workbook
	 */
	private void openedDataWorkbook(DataWorkbook dataWorkbook) {
		
		List<DefaultMutableTreeNode> treeNodesFound = this.searchTreeNode(this.getSearchFilter4DataWorkbook(dataWorkbook));
		if (treeNodesFound==null) return;
		
		DefaultMutableTreeNode treeNodeWB = treeNodesFound.get(0);
		DataTreeNodeDataWorkbook dtnoWB =  (DataTreeNodeDataWorkbook) treeNodeWB.getUserObject();
		
		dtnoWB.setDataSourcesLoaded(true);
	}
	/**
	 * React on a closed data workbook.
	 * @param dataWorkbook the data workbook
	 */
	private void closedDataWorkbook(DataWorkbook dataWorkbook) {
		
		List<DefaultMutableTreeNode> treeNodesFound = this.searchTreeNode(this.getSearchFilter4DataWorkbook(dataWorkbook));
		if (treeNodesFound==null) return;
		
		DefaultMutableTreeNode treeNodeWB = treeNodesFound.get(0);
		DataTreeNodeDataWorkbook dtnoWB =  (DataTreeNodeDataWorkbook) treeNodeWB.getUserObject();
		
		dtnoWB.setDataSourcesLoaded(false);
	}
	/**
	 * Removes the specified DataWorkbook.
	 * @param dataWorkbook the data workbook
	 */
	private void removedDataWorkbook(DataWorkbook dataWorkbook) {
		
		// --- Define a search filter for the search traversal ----------------
		DataTreeModelSearchFilter searchFilter = this.getSearchFilter4DataWorkbook(dataWorkbook);
		
		// --- Search the tree and remove the corresponding nodes -------------
		this.removeTreeNodes(this.searchTreeNode(searchFilter));
	}
	
	
	/**
	 * Adds the specified data source as a tree node.
	 *
	 * @param dw the corresponding {@link DataWorkbook}
	 * @param dataSource the data source
	 */
	private void addedDataSource(DataWorkbook dw,  AbstractDataSource dataSource) {

		// --- Get the corresponding DataWorkbook first -------------
		List<DefaultMutableTreeNode> treeNodesFound = this.searchTreeNode(this.getSearchFilter4DataWorkbook(dw));
		if (treeNodesFound==null || treeNodesFound.size()>1) return;
		DefaultMutableTreeNode dwNode = treeNodesFound.getFirst();

		// --- Create new node according to data source -------------
		AbstractDataTreeNodeDataSource<?> ds = null;
		if (dataSource instanceof CsvDataSource) {
			ds = new DataTreeNodeDataSourceCsv(this.getDataController(), (CsvDataSource) dataSource);
		} else if (dataSource instanceof ExcelDataSource) {
			ds = new DataTreeNodeDataSourceExcel(this.getDataController(), (ExcelDataSource) dataSource);
		} else if (dataSource instanceof DatabaseDataSource) {
			ds = new DataTreeNodeDataSourceDatabase(this.getDataController(), (DatabaseDataSource) dataSource);
		}
		
		// --- Create new node and add to parent --------------------
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ds);
		dwNode.add(newNode);
		
		Object[] pathToParent = dwNode.getPath();
		int[] newIndicies = {dwNode.getIndex(newNode)};
		Object[] newChildren = {newNode}; 
		this.fireTreeNodesInserted(this, pathToParent, newIndicies, newChildren);
	}
	/**
	 * Removes the specified data source as tree node.
	 * 
	 * @param dw the corresponding {@link DataWorkbook}
	 * @param dataSource the data source
	 */
	private void removedDataSource(DataWorkbook dw, AbstractDataSource dataSource) {
		
		// --- Define a search filter for the search traversal ----------------
		DataTreeModelSearchFilter searchFilter = this.getSearchFilter4DataSource(dataSource);
		
		// --- Search the tree and remove the corresponding nodes -------------
		this.removeTreeNodes(this.searchTreeNode(searchFilter));
	}
	
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		AffectedDataObjects ado = null;
		switch (evt.getPropertyName()) {
		case DataController.DC_ADDED_DATA_WORKBOOK:
			ado = (AffectedDataObjects) evt.getNewValue();
			this.addedDataWorkbook(ado.getDataWorkbook());
			break;
		case DataController.DC_OPENED_DATA_WORKBOOK:
			ado = (AffectedDataObjects) evt.getNewValue();
			this.openedDataWorkbook(ado.getDataWorkbook());
			break;
		case DataController.DC_CLOSED_DATA_WORKBOOK:
			ado = (AffectedDataObjects) evt.getNewValue();
			this.closedDataWorkbook(ado.getDataWorkbook());
			break;
		case DataController.DC_REMOVED_DATA_WORKBOOK:
			ado = (AffectedDataObjects) evt.getOldValue();
			this.removedDataWorkbook(ado.getDataWorkbook());
			break;

		case DataController.DC_ADDED_DATA_SOURCE:
		case DataController.DC_OPENED_DATA_SOURCE:
			ado = (AffectedDataObjects) evt.getNewValue();
			this.addedDataSource(ado.getDataWorkbook(), ado.getDataSource());
			break;
		case DataController.DC_CLOSED_DATA_SOURCE:
		case DataController.DC_REMOVED_DATA_SOURCE:
			ado = (AffectedDataObjects) evt.getOldValue();
			this.removedDataSource(ado.getDataWorkbook(), ado.getDataSource());
			break;
		}
	}
	
}
