package de.enflexit.df.impl.db;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class CsvDataSourceIntegration.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseDataSourceIntegration4SQL extends AbstractDataSourceIntegration<DatabaseDataSource4SQL> {

	private DatabaseDataSourceDTNO4SQL dtnoDBDataSourceSQL;
	
	/**
	 * Instantiates a new database data source integration.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public DatabaseDataSourceIntegration4SQL(DataController dataController, DataWorkbook dataWorkbook, DatabaseDataSource4SQL dataSource) {
		super(dataController, dataWorkbook, dataSource);
	}

	/**
	 * Returns the database query.
	 * @return the database query
	 */
	public DatabaseQuery getDatabaseQuery() {
		return this.getDataSource().getDatabaseQuery();
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDataTreeNodeObject()
	 */
	@Override
	public DatabaseDataSourceDTNO4SQL getDTNO() {
		if (dtnoDBDataSourceSQL==null) {
			dtnoDBDataSourceSQL = new DatabaseDataSourceDTNO4SQL(this);
		}
		return dtnoDBDataSourceSQL;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JPanelDataSourceConfigurationDatabase getConfigurationPanel() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetConfigurationPanel()
	 */
	@Override
	public void resetConfigurationPanel() { }
		

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getDetailViewPanel()
	 */
	@Override
	public JComponent getDetailViewPanel() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetDetailViewPanel()
	 */
	@Override
	public void resetDetailViewPanel() { }
	
}