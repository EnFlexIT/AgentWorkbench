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

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration#addDataTreeSubNodes()
	 */
	@Override
	public void addDataTreeSubNodes() {
		
		DefaultMutableTreeNode parentNode = this.getDataTreeNode();
		for (DatabaseQuery dbQuery : this.getDataSource().getDatabaseQueryList()) {
			
			DatabaseDataSourceIntegration4SQL dbSqlIntegration = new DatabaseDataSourceIntegration4SQL(this.getDataController(), this.getDataWorkbook(), this.getDataSource());
			dbSqlIntegration.setDatabaseQuery(dbQuery);
			
			DatabaseDataSourceDTNO4SQL dtnoSQL = new DatabaseDataSourceDTNO4SQL(dbSqlIntegration);
			DefaultMutableTreeNode newChildNode = new DefaultMutableTreeNode(dtnoSQL);
			parentNode.add(newChildNode);
		}
	}
	
	
}
