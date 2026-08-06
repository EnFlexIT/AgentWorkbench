package de.enflexit.df.impl.db;

import java.io.Serializable;

import de.enflexit.common.NumberHelper;
import de.enflexit.common.StringHelper;

/**
 * The Class DatabaseQuery.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseQuery implements Serializable, Comparable<DatabaseQuery> {

	private static final long serialVersionUID = 6770803530373291509L;
	
	public static final String KEY_NUMBER = "no";
	public static final String KEY_NAME  = "name";
	public static final String KEY_SQL_STATEMENT  = "sql-statement";
	public static final String KEY_ROWS_PER_PAGE = "rows-per-page";
	
	private int number;
	private String name;
	private String sqlStatement;
	private int rowsPerPage;
	
	
	/**
	 * Instantiates a new database query.
	 */
	public DatabaseQuery() { }
	
	/**
	 * Instantiates a new database query.
	 *
	 * @param number the number of the database query
	 * @param name the name of the database query
	 * @param sqlStatement the actual SQL statement to be executed
	 */
	public DatabaseQuery(int number, String name, String sqlStatement) {
		this.number = number;
		this.name = name;
		this.sqlStatement = sqlStatement;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSqlStatement() {
		return sqlStatement;
	}
	public void setSqlStatement(String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null) return false;
		if (compObj==this) return true;
		
		if (compObj instanceof DatabaseQuery dbQueryComp) {

			if (this.getNumber()!=dbQueryComp.getNumber()) return false;
			if (StringHelper.isEqualString(this.getName(), dbQueryComp.getName()) == false) return false;
			if (StringHelper.isEqualString(this.getSqlStatement(), dbQueryComp.getSqlStatement()) == false) return false;
			if (this.getRowsPerPage()!=dbQueryComp.getRowsPerPage()) return false;
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DatabaseQuery dbQuery) {
		return Integer.compare(this.getNumber(), dbQuery.getNumber());
	}
	
	
	/**
	 * Converts the current instance into a configuration string.
	 * @return the configuration string
	 */
	public String toConfigurationString() {
		return toConfigurationString(this);
	}
	/**
	 * Converts the current instance into a configuration string.
	 *
	 * @param dbQuery the db query
	 * @return the configuration string
	 */
	public static String toConfigurationString(DatabaseQuery dbQuery) {
		
		String config = new String();
		
		config = DatabaseDataSource.addConfigValue(config, KEY_NUMBER, (dbQuery.getNumber() + ""));
		config = DatabaseDataSource.addConfigValue(config, KEY_NAME, dbQuery.getName());
		config = DatabaseDataSource.addConfigValue(config, KEY_SQL_STATEMENT, dbQuery.getSqlStatement());
		config = DatabaseDataSource.addConfigValue(config, KEY_ROWS_PER_PAGE, (dbQuery.getRowsPerPage() + ""));
		
		return config; 
	}
	/**
	 * Converts the specified configuration string into a {@link DatabaseQuery}.
	 * @param configuration the sub configuration
	 */
	public static DatabaseQuery fromConfigurationString(String config) {

		if (config==null || config.isBlank()==true) return null;
		
		String[] keyValuePairs = config.split("\\|");
		if (keyValuePairs.length==0) return null;
		
		// --- Create new instance ----------------------------------
		DatabaseQuery dbQuery = new DatabaseQuery();
		for (String keyValuePair : keyValuePairs) {
			
			int idxTagOpen  = keyValuePair.indexOf("[");
			int idxTagClose = keyValuePair.indexOf("]");
			
			String key   = keyValuePair.substring(0, idxTagOpen);
			String value = keyValuePair.substring(idxTagOpen + 1, idxTagClose);
			if (value.isBlank()==true) continue;
			
			switch (key) {
			case KEY_NUMBER:
				Integer qNumber = NumberHelper.parseInteger(value);
				dbQuery.setNumber(qNumber);
				break;
			case KEY_NAME:
				dbQuery.setName(value);
				break;
			case KEY_SQL_STATEMENT:
				dbQuery.setSqlStatement(value);
				break;
			case KEY_ROWS_PER_PAGE:
				Integer rpp = NumberHelper.parseInteger(value);
				dbQuery.setRowsPerPage(rpp);
				break;
			}
		} // end for
		return dbQuery;
	}

}
