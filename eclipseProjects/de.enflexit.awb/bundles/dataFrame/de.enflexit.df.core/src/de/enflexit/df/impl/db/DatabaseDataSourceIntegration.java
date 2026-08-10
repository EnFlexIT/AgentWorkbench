package de.enflexit.df.impl.db;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class CsvDataSourceIntegration.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseDataSourceIntegration extends AbstractDataSourceIntegration<DatabaseDataSource> {

	private DatabaseDataSourceDTNO dtnoDBDataSource;
	private JPanelDataSourceConfigurationDatabase jPanelDataSourceConfigurationDatabase;
	private JPanelQueryConfiguration jPanelQueryConfiguration;
	
	private DatabaseConnection databaseConnection;
	
	/**
	 * Instantiates a new database data source integration.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public DatabaseDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook, DatabaseDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDataTreeNodeObject()
	 */
	@Override
	public AbstractDataSourceDTNO<DatabaseDataSource> getDTNO() {
		if (dtnoDBDataSource==null) {
			dtnoDBDataSource = new DatabaseDataSourceDTNO(this);
		}
		return dtnoDBDataSource;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return this.getConfigurationPanel().getConfigurationToolbarComponents();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JPanelDataSourceConfigurationDatabase getConfigurationPanel() {
		if (jPanelDataSourceConfigurationDatabase==null) {
			jPanelDataSourceConfigurationDatabase = new JPanelDataSourceConfigurationDatabase(this);
			this.getDataController().addPropertyChangeListener(jPanelDataSourceConfigurationDatabase);
		}
		return jPanelDataSourceConfigurationDatabase;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetConfigurationPanel()
	 */
	@Override
	public void resetConfigurationPanel() {
		this.getDataController().removePropertyChangeListener(this.jPanelDataSourceConfigurationDatabase);
		this.jPanelDataSourceConfigurationDatabase = null;
	}


	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getDetailViewPanel()
	 */
	@Override
	public JComponent getDetailViewPanel() {
		if (jPanelQueryConfiguration==null) {
			jPanelQueryConfiguration = new JPanelQueryConfiguration(this);
		}
		return jPanelQueryConfiguration;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetDetailViewPanel()
	 */
	@Override
	public void resetDetailViewPanel() {
		jPanelQueryConfiguration = null;
	}

	
	/**
	 * Returns the database connection.
	 * @return the database connection
	 */
	public synchronized DatabaseConnection getDatabaseConnection() {
		if (databaseConnection==null) {
			DatabaseConnection dbConnectionTest = new DatabaseConnection(this.getDataSource());
			if (dbConnectionTest.hasValidDatabaseSettings()==true) {
				databaseConnection = dbConnectionTest;
			}
		}
		return databaseConnection;
	}
	
	/**
	 * Close all connections and resources used.
	 */
	public void close() {
		
		try {
			this.resetConfigurationPanel();
			this.resetDetailViewPanel();
			if (this.databaseConnection!=null) {
				this.databaseConnection.close();
			}
			this.databaseConnection = null;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// ------------------------------------------------------------------------
	// --- From here handling of sub nodes ------------------------------------
	// ------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration#addDataTreeSubNodes()
	 */
	@Override
	public void addDataTreeSubNodes() {
		for (DatabaseQuery dbQuery : this.getDataSource().getDatabaseQueryList()) {
			this.addDataTreeSubNode(dbQuery);
		}
	}
	
	/**
	 * Adds a data tree sub node that corresponds to the specified {@link DatabaseQuery}.
	 * @param dbQueryToAdd the DatabaseQuery to add
	 */
	public void addDataTreeSubNode(DatabaseQuery dbQueryToAdd) {
		
		DatabaseDataSource4SQL sqlDS = new DatabaseDataSource4SQL(this.getDataSource(), dbQueryToAdd);
		DatabaseDataSourceIntegration4SQL dbSqlIntegration = new DatabaseDataSourceIntegration4SQL(this.getDataController(), this.getDataWorkbook(), sqlDS);
		DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(dbSqlIntegration.getDTNO());
		
		this.getDataTreeNode().add(subNode);
		this.getDataController().getDataTreeModel().informTreeNodeAdded(this.getDataTreeNode(), subNode);
	}
	/**
	 * Removes the data tree sub node that corresponds to the specified {@link DatabaseQuery}.
	 * @param dbQueryToRemove the DatabaseQuery to remove
	 */
	public void removeDataTreeSubNode(DatabaseQuery dbQueryToRemove) {
		
		DefaultMutableTreeNode subNodeToRemove = this.getDataTreeSubNode(dbQueryToRemove);
		if (subNodeToRemove==null) return;
		
		int idxOfSubNodeToRemove = this.getDataTreeNode().getIndex(subNodeToRemove);
		this.getDataTreeNode().remove(idxOfSubNodeToRemove);
		this.getDataController().getDataTreeModel().informTreeNodeRemoved(this.getDataTreeNode(), subNodeToRemove, idxOfSubNodeToRemove);
	}

	/**
	 * Move data tree sub node.
	 *
	 * @param dbQueryToMove the db query to move
	 * @param direction the direction
	 */
	public void moveDataTreeSubNode(DatabaseQuery dbQueryToMove, int direction) {
		
		DefaultMutableTreeNode treeNodeToMove = this.getDataTreeSubNode(dbQueryToMove);
		if (treeNodeToMove==null) return;
		
		int oldIndex = this.getDataTreeNode().getIndex(treeNodeToMove);
		int newIndex = oldIndex + direction;

		this.getDataTreeNode().remove(treeNodeToMove);
		this.getDataTreeNode().insert(treeNodeToMove, newIndex);
		
		int[] changedIndex = new int[2];
		changedIndex[0] = oldIndex;
		changedIndex[1] = newIndex;
		
		this.getDataController().getDataTreeModel().nodesChanged(this.getDataTreeNode(), changedIndex);
		for (int changed : changedIndex) {
			this.getDataController().getDataTreeModel().nodeChanged(this.getDataTreeNode().getChildAt(changed));
		}
	}
	
	/**
	 * Returns the data tree sub node.
	 *
	 * @param databaseQuery the database query
	 * @return the data tree sub node
	 */
	public DefaultMutableTreeNode getDataTreeSubNode(DatabaseQuery databaseQuery) {
		
		DefaultMutableTreeNode parentNode = this.getDataTreeNode();
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			DefaultMutableTreeNode subTreeNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			if (subTreeNode.getUserObject() instanceof DatabaseDataSourceDTNO4SQL dtnoSQL) {
				if (dtnoSQL.getDatabaseSqlIntegration().getDatabaseQuery().equals(databaseQuery)==true) {
					return subTreeNode;
				}
			}
		}
		return null;
	}
	/**
	 * Returns the data tree sub node.
	 *
	 * @param databaseQuery the database query
	 * @return the data tree sub node
	 */
	public DatabaseDataSourceDTNO4SQL getDataTreeSubNodeDTNO(DatabaseQuery databaseQuery) {

		DefaultMutableTreeNode parentNode = this.getDataTreeNode();
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			if (treeNode.getUserObject() instanceof DatabaseDataSourceDTNO4SQL dtnoSQL) {
				if (dtnoSQL.getDatabaseSqlIntegration().getDatabaseQuery().equals(databaseQuery)==true) {
					return dtnoSQL;
				}
			}
		}
		return null;
	}
	
}
