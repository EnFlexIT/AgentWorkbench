package de.enflexit.df.impl.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.AddCellToColumnException;
import tech.tablesaw.io.ColumnIndexOutOfBoundsException;


/**
 * The Class PaginationDataLoader4SQL.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PaginationDataLoader4SQL extends AbstractPaginationDataLoader<DatabaseDataSource4SQL> {

	private DatabaseConnection databaseConnection;
	private HibernateDatabaseService dbService;
	
	private int lastOffset = 0;
	
	/**
	 * Instantiates a new pagination data loader 4 DB.
	 * @param dataSource the data source
	 */
	public PaginationDataLoader4SQL(DatabaseDataSourceIntegration4SQL dsIntegration) {
		super(dsIntegration);
	}
	/**
	 * Returns the DatabaseDataSourceIntegration (which is the parent integration).
	 * @return the database data source integration
	 */
	private DatabaseDataSourceIntegration getDatabaseDataSourceIntegration() {
		return (DatabaseDataSourceIntegration) this.getDatabaseDataSourceIntegration4SQL().getDataSource().getDatabaseDataSource().getDataSourceIntegration(getDatabaseDataSourceIntegration4SQL().getDataController(), getDatabaseDataSourceIntegration4SQL().getDataWorkbook());
	}
	/**
	 * Returns the current DatabaseDataSourceIntegration4SQL.
	 * @return the database SQL integration
	 */
	private DatabaseDataSourceIntegration4SQL getDatabaseDataSourceIntegration4SQL() {
		return (DatabaseDataSourceIntegration4SQL) this.getDataSourceIntegration();
	}
	
	/**
	 * Returns the {@link DatabaseQuery} on which this loader is working.
	 * @return the database query
	 */
	private DatabaseQuery getDatabaseQuery() {
		return this.getDatabaseDataSourceIntegration4SQL().getDatabaseQuery();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader#setRowsPerPage(int)
	 */
	@Override
	public void setRowsPerPage(int rowsPerPage) {
		this.getDatabaseQuery().setRowsPerPage(rowsPerPage);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader#getRowsPerPage()
	 */
	@Override
	public int getRowsPerPage() {
		return this.getDatabaseQuery().getRowsPerPage();
	}

	
	/**
	 * Returns the HibernateDatabaseService that is used to produce the {@link Connection}.
	 * @return the database service
	 */
	private HibernateDatabaseService getDatabaseService() {
		if (dbService==null) {
			dbService = HibernateUtilities.getDatabaseService(this.getDataSource().toDatabaseSettings().getDatabaseSystemName());
		}
		return dbService;
	}
	/**
	 * Returns the database connection.
	 * @return the database connection
	 */
	public DatabaseConnection getDatabaseConnection() {
		if (databaseConnection==null) {
			databaseConnection = new DatabaseConnection(this.getDataSource());
		}
		return databaseConnection;
	}
	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		this.getDatabaseConnection().close();
	}
	
	/**
	 * Reads the specified SQL statement from the database by using the current {@link Connection} .
	 *
	 * @param sqlStatement the SQL statement
	 * @param offset the offset
	 * @param limit the limit
	 * @return the table
	 * @see #getConnection()
	 */
	private Table readFromDatabase(String sqlStatement, int offset, int limit) {
		
		Table table = null;
		Statement statem;
		try {

			// --- Apply offset and limit to SQL statement? -------------------
			String sqlToExecute = sqlStatement;
			if (offset >= 0 && limit >= 0) {
				sqlToExecute = this.getDatabaseService().applyOffsetAndLimitToSqlStatement(sqlToExecute, offset, limit);
			}
			
			// --- Execute Query ----------------------------------------------
			statem = this.getDatabaseConnection().getConnection().createStatement();
			ResultSet resSet = statem.executeQuery(sqlToExecute);
			
			// --- Update the table dictionary according to the ResultSet ----- 
			this.getDatabaseDataSourceIntegration().getDatabaseConnection().getTableDictionary().updateDictionary(sqlStatement, resSet);

			// --- Translate into tablesaw table ------------------------------
			table = Table.read().db(resSet);
			this.setErrorMessage(null);
			
		} catch (SQLException sqlEx) {
			this.setErrorMessage(sqlEx.getMessage());
			//sqlEx.printStackTrace();
		}
		return table;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.data.PaginationDataLoader#loadNextPage()
	 */
	@Override
	public Table loadNextPage() {
		
		boolean isDebug = true;
		try {
			Table newPage = null;
			if (this.isPaginationActivated()==false) {
				// --------------------------------------------------
				// --- Read complete file ---------------------------
				// --------------------------------------------------
				newPage = this.readFromDatabase(this.getDatabaseQuery().getSqlStatement(), -1, -1);
				this.setPageNumberLoaded(1);
				this.lastOffset = 0;
				
			} else {
				// --------------------------------------------------
				// --- Read data page-wise --------------------------
				// --------------------------------------------------
				int useOffset = this.lastOffset;
				int useLimit  = this.getNumberOfRecordsPerPage(); 
				this.lastOffset += this.getNumberOfRecordsPerPage();
				
				// --- Get tablesaw table from SQL statement --------
				newPage = this.readFromDatabase(this.getDatabaseQuery().getSqlStatement(), useOffset, useLimit);
				this.setPageNumberLoaded(this.getPageNumberLoaded() + 1);
				this.setErrorMessage(null);
			}
			return newPage;
			
		} catch (AddCellToColumnException | ColumnIndexOutOfBoundsException | ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
			this.setErrorMessage(ex.getLocalizedMessage());
			if (isDebug==true) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
}
