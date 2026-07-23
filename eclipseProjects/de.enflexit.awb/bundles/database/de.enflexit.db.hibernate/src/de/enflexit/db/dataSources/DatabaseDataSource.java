package de.enflexit.db.dataSources;

import de.enflexit.common.NumberHelper;

/**
 * The Class DatabaseDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-EssenS
 */
public class DatabaseDataSource extends DefaultDataSource {

	private static final long serialVersionUID = 6704254616526361690L;

	public final static String KEY_DBMS_NAME = "DBMS-Name";
	public final static String KEY_CONNECTION_URL = "connectionURL";
	public final static String KEY_DB_NAME = "DB-Name";
	public final static String KEY_USER_NAME = "UserName";
	public final static String KEY_PASSWORD = "Password";
	
	
	private String dbmsName; 
	private String connectionURL;
	private String dbName;
	private String userName;
	private String password;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#newInstance()
	 */
	@Override
	public DatabaseDataSource newInstance() {
		return new DatabaseDataSource();
	}
	
	
	/**
	 * Returns the DBMS name (e.g. MariaDB or PostGres).
	 * @return the DBMS name
	 */
	public String getDBMSName() {
		return dbmsName;
	}
	/**
	 * Sets the DBMS name.
	 * @param dbmsName the new DBMS name
	 */
	public void setDBMSName(String dbmsName) {
		this.dbmsName = dbmsName;
	}

	/**
	 * Returns the host or IP.
	 * @return the host or IP
	 */
	public String getConnectionURL() {
		return connectionURL;
	}
	/**
	 * Sets the connection URL.
	 * @param connectionURL the new connection URL
	 */
	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	/**
	 * Returns the database name.
	 * @return the database name
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * Sets the database name.
	 * @param dbName the new database name
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * Returns the database user name.
	 * @return the database user name
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * Sets the database user name.
	 * @param userName the new database user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Returns the database password.
	 * @return the database password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Sets the database password.
	 * @param databasePassword the new database password
	 */
	public void setPassword(String databasePassword) {
		this.password = databasePassword;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#toConfigurationString()
	 */
	@Override
	public String toConfigurationString() {
		
		String config = new String();
		
		config = DatabaseDataSource.addConfigValue(config, KEY_ID, (this.getId() + ""));
		config = DatabaseDataSource.addConfigValue(config, KEY_NAME, this.getName());
		config = DatabaseDataSource.addConfigValue(config, KEY_DESCRIPTION, this.getDescription());
		config = DatabaseDataSource.addConfigValue(config, KEY_ROWS_PER_PAGE, this.getRowsPerPage() + "");
		
		config = DatabaseDataSource.addConfigValue(config, KEY_DBMS_NAME, this.getDBMSName());
		config = DatabaseDataSource.addConfigValue(config, KEY_CONNECTION_URL, this.getConnectionURL());
		config = DatabaseDataSource.addConfigValue(config, KEY_DB_NAME, this.getDbName());
		config = DatabaseDataSource.addConfigValue(config, KEY_USER_NAME, this.getUserName());
		config = DatabaseDataSource.addConfigValue(config, KEY_PASSWORD, this.getPassword());
		
		if (config.isBlank()==true) {
			config = null;
		}
		return config;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#fromConfigurationString(java.lang.String)
	 */
	@Override
	public DatabaseDataSource fromConfigurationString(String config) {
		
		if (config==null || config.isBlank()==true) return this;
		
		String[] keyValuePairs = config.split("\\|");
		if (keyValuePairs.length==0) return this;
		
		// --- Create new instance ----------------------------------
		for (String keyValuePair : keyValuePairs) {
			
			int idxTagOpen  = keyValuePair.indexOf("[");
			int idxTagClose = keyValuePair.indexOf("]");
			
			String key   = keyValuePair.substring(0, idxTagOpen);
			String value = keyValuePair.substring(idxTagOpen + 1, idxTagClose);
			if (value.isBlank()==true) continue;
			
			switch (key) {
			case KEY_ID:
				this.setId(NumberHelper.parseInteger(value));
				break;
			case KEY_NAME:
				this.setName(value);
				break;
			case KEY_DESCRIPTION:
				this.setDescription(value);
				break;
			case KEY_ROWS_PER_PAGE:
				Integer rowsPerPage = NumberHelper.parseInteger(value);
				if (rowsPerPage!=null) this.setRowsPerPage(rowsPerPage);
				break;
				
			case KEY_DBMS_NAME:
				this.setDBMSName(value);
				break;
			case KEY_CONNECTION_URL:
				this.setConnectionURL(value);
				break;
			case KEY_DB_NAME:
				this.setDbName(value);
				break;
			case KEY_USER_NAME:
				this.setUserName(value);
				break;
			case KEY_PASSWORD:
				this.setPassword(value);
				break;
			}
		} // end for
		return this;
	}
	
}
