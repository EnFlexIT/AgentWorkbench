package de.enflexit.df.core.model;

import de.enflexit.common.dataSources.DatabaseDataSource;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.ui.dataSource.JPanelDataSourceConfigurationDatabase;

/**
 * The Class DataTreeNodeDataSourceDatabase.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceDatabase extends DataTreeNodeDataSource<DatabaseDataSource> {

	private JPanelDataSourceConfigurationDatabase jPanelDataSourceConfigurationDatabase;
	
	/**
	 * Instantiates a new data tree node data source database.
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceDatabase(DatabaseDataSource dataSource) {
		super(dataSource);
		this.setImageIcon(BundleHelper.getThemedIcon("DatabaseBlack.png", "DatabaseGrey.png"));
		this.setCaption("New Database Connection");
		this.setTooltipText("Please, configure the database connection ...");
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeDataSource#getJPanelConfiguration()
	 */
	@Override
	public JPanelDataSourceConfigurationDatabase getJPanelConfiguration() {
		if (jPanelDataSourceConfigurationDatabase==null) {
			jPanelDataSourceConfigurationDatabase = new JPanelDataSourceConfigurationDatabase(this);
		}
		return jPanelDataSourceConfigurationDatabase;
	}

}
