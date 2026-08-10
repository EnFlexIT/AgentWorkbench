package de.enflexit.df.impl.db;

import java.util.List;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;

/**
 * The Class DatabaseDataSourceDTNO.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseDataSourceDTNO4SQL extends AbstractDataSourceDTNO<DatabaseDataSource4SQL> {

	private PaginationDataLoader4SQL paginationDataLoader4SQL;
	
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
	 * Returns the parent {@link DatabaseDataSourceIntegration}.
	 * @return the database integration
	 */
	private DatabaseDataSourceIntegration getDatabaseIntegration() {
		return (DatabaseDataSourceIntegration) this.getDatabaseSqlIntegration().getDataSource().getDatabaseDataSource().getDataSourceIntegration(this.getDataController(), this.getDataWorkbook());
	}
	/**
	 * Returns the current DatabaseDataSourceIntegration4SQL.
	 * @return the database SQL integration
	 */
	public DatabaseDataSourceIntegration4SQL getDatabaseSqlIntegration() {
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
	public AbstractPaginationDataLoader<DatabaseDataSource4SQL> getPaginationDataLoader() {
		if (paginationDataLoader4SQL==null) {
			paginationDataLoader4SQL = new PaginationDataLoader4SQL(this.getDatabaseSqlIntegration());
		}
		return paginationDataLoader4SQL;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO#getTableName(java.lang.String)
	 */
	@Override
	public String getTableName(String columName) {

		DatabaseConnection dbConnection = this.getDatabaseIntegration().getDatabaseConnection();
		if (dbConnection!=null) {
			// --- Check for the possible source table ---  
			List<String> tableNames = dbConnection.getTableDictionary().guessTable(this.getDatabaseSqlIntegration().getDatabaseQuery().getSqlStatement(), columName);
			if (tableNames!=null) {
				// --- Found something --------------------
				if (tableNames.size()==1) {
					return tableNames.get(0);
				} else {
					// --- n>1 ----------------------------
					if (tableNames.get(1)==null) {
						String mostProbableTable = tableNames.get(0); 
						tableNames.remove(1);
						tableNames.remove(0);
						return mostProbableTable + " (also available in: " + String.join(", ", tableNames) + ")"; 
					}
					return String.join(", ", tableNames);
				}
			}
		}
		return super.getTableName(columName);
	}
	
}
