package de.enflexit.df.impl.db;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;

/**
 * The Class DatabaseDataSourceDTNO.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseDataSourceDTNO4SQL extends AbstractDataSourceDTNO<DatabaseDataSource> {

	private PaginationDataLoader4DB paginationDataLoader4DB;
	
	/**
	 * Instantiates a new database data source sql DTNO.
	 *
	 * @param dsIntegration the ds integration
	 * @param dbQuery the DatabaseQuery to handle
	 */
	public DatabaseDataSourceDTNO4SQL(DatabaseDataSourceIntegration4SQL dsIntegration) {
		super(dsIntegration);
		this.setImageIcon(BundleHelper.getThemedIcon("MBtable.png"));
		this.setTooltipText("Please, configure the corresponding SQL statement ...");
	}
	/**
	 * Returns the current DatabaseDataSourceIntegration4SQL.
	 * @return the database SQL integration
	 */
	private DatabaseDataSourceIntegration4SQL getDatabaseSqlIntegration() {
		return (DatabaseDataSourceIntegration4SQL) this.getDataSourceIntegration();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO#getCaption()
	 */
	@Override
	public String getCaption() {
		DatabaseQuery dbQuery = this.getDatabaseSqlIntegration().getDatabaseQuery();
		return dbQuery.getNumber() + ") " + dbQuery.getName();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.treeNode.AbstractDTNO_DataSource#getPaginationDataLoader()
	 */
	@Override
	public AbstractPaginationDataLoader<DatabaseDataSource> getPaginationDataLoader() {
		if (paginationDataLoader4DB==null) {
			// TODO
			paginationDataLoader4DB = new PaginationDataLoader4DB(this.getDataSource());
		}
		return paginationDataLoader4DB;
	}

}
