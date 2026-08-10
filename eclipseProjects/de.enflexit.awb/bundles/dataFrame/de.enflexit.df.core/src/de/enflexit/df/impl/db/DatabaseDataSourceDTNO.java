package de.enflexit.df.impl.db;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;

/**
 * The Class DatabaseDataSourceDTNO.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseDataSourceDTNO extends AbstractDataSourceDTNO<DatabaseDataSource> {
		
	/**
	 * Instantiates a new data tree node data source database.
	 * @param dsIntegration the current DatabaseDataSourceIntegration
	 */
	public DatabaseDataSourceDTNO(DatabaseDataSourceIntegration dsIntegration) {
		super(dsIntegration);
		if (this.getDataSource().getName()==null) {
			this.getDataSource().setName("New Database Connection");
		}
		this.setImageIcon(BundleHelper.getThemedIcon("DatabaseBlack.png", "DatabaseGrey.png"));
		this.setTooltipText("Please, configure the database connection ...");
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.treeNode.AbstractDTNO_DataSource#getPaginationDataLoader()
	 */
	@Override
	public AbstractPaginationDataLoader<DatabaseDataSource> getPaginationDataLoader() {
		return null;
	}

}
