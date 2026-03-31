package de.enflexit.df.core.model;

import de.enflexit.common.dataSources.DatabaseDataSource;
import de.enflexit.df.core.BundleHelper;

/**
 * The Class DataTreeNodeDataSourceDatabase.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceDatabase extends DataTreeNodeDataSource<DatabaseDataSource> {

	/**
	 * Instantiates a new data tree node data source database.
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceDatabase(DatabaseDataSource dataSource) {
		super(dataSource);
		this.setImageIcon(BundleHelper.getThemedIcon("", ""));
		this.setCaption("New Database Connection");
		this.setTooltipText("Please, configure the database connection ...");
	}

}
