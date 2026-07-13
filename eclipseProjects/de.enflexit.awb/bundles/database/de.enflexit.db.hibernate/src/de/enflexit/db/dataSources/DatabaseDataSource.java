package de.enflexit.common.dataSources;

import de.enflexit.common.NumberHelper;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class DatabaseDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-EssenS
 */
@Entity
@DiscriminatorValue("dbms")

@XmlRootElement(name = "DatabaseDataSource")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "dbmsName",
    "connectionURL",
    "dbName",
    "userName",
    "password"
})

public class DatabaseDataSource extends AbstractDataSource {

	private static final long serialVersionUID = 6704254616526361690L;

	public final static String KEY_DBMS_NAME = "DBMS-Name";
	public final static String KEY_CONNECTION_URL = "connectionURL";
	public final static String KEY_DB_NAME = "DB-Name";
	public final static String KEY_USER_NAME = "UserName";
	public final static String KEY_PASSWORD = "Password";
	
	
	@Column(name="dbms_name", nullable=false)
	private String dbmsName; 
	
	@Column(name="connection_url", nullable=false)
	private String connectionURL;
	
	@Column(name="db_name", nullable=false)
	private String dbName;
	
	@Column(name="user_name", nullable=false)
	private String userName;
	
	@Column(name="password", nullable=false)
	private String password;
	
	
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
	
	/**
	 * Convert the current DatabaseDataSource to a single configuration String.
	 * @return the string
	 */
	public String toConfigurationString() {
		return DatabaseDataSource.toConfigurationString(this);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.toConfigurationString();
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here static helper methods for the type DatabaseDataSource ----
	// ------------------------------------------------------------------------	
	/**
	 * Method to Convert from a DatabaseDataSource to a single configuration String.
	 *
	 * @param dbSource the DatabaseDataSource to convert
	 * @return the configuration string or <code>null</code>
	 */
	public static String toConfigurationString(DatabaseDataSource dbSource) {
		
		if (dbSource==null) return null;
		
		String config = new String();
		
		config = DatabaseDataSource.addConfigValue(config, KEY_ID, (dbSource.getId()==null || dbSource.getId()==0) ? null : dbSource.getId().toString());
		config = DatabaseDataSource.addConfigValue(config, KEY_NAME, dbSource.getName());
		config = DatabaseDataSource.addConfigValue(config, KEY_DESCRIPTION, dbSource.getDescription());
		config = DatabaseDataSource.addConfigValue(config, KEY_ROWS_PER_PAGE, dbSource.getRowsPerPage() + "");
		
		config = DatabaseDataSource.addConfigValue(config, KEY_DBMS_NAME, dbSource.getDBMSName());
		config = DatabaseDataSource.addConfigValue(config, KEY_CONNECTION_URL, dbSource.getConnectionURL());
		config = DatabaseDataSource.addConfigValue(config, KEY_DB_NAME, dbSource.getDbName());
		config = DatabaseDataSource.addConfigValue(config, KEY_USER_NAME, dbSource.getUserName());
		config = DatabaseDataSource.addConfigValue(config, KEY_PASSWORD, dbSource.getPassword());
		
		if (config.isBlank()==true) {
			config = null;
		}
		return config;
	}
	/**
	 * Adds a configuration value to the specified configuration String.
	 *
	 * @param configString the current configuration string
	 * @param key the value key
	 * @param value the value
	 */
	private static String addConfigValue(String configString, String key, String value) {
		
		if (key==null || key.isBlank()==true) return configString;
		if (value==null || value.isBlank()==true) return configString;
		
		if (configString==null) {
			configString = "";
		} else {
			if (configString.isBlank()==false) {
				configString += "|";	
			}
		}
		configString += key + "[" + value + "]";
		return configString;
	}
	
	
	/**
	 * Method to Convert from a single configuration String to a DatabaseDataSource.
	 *
	 * @param config the configuration string to convert
	 * @return the converted DatabaseDataSource or <code>null</code>
	 */
	public static DatabaseDataSource fromConfigurationString(String config) {
		
		if (config==null || config.isBlank()==true) return null;
		
		String[] keyValuePairs = config.split("\\|");
		if (keyValuePairs.length==0) return null;
		
		// --- Create new instance ----------------------------------
		DatabaseDataSource dbSource = new DatabaseDataSource();
		for (String keyValuePair : keyValuePairs) {
			
			int idxTagOpen  = keyValuePair.indexOf("[");
			int idxTagClose = keyValuePair.indexOf("]");
			
			String key   = keyValuePair.substring(0, idxTagOpen);
			String value = keyValuePair.substring(idxTagOpen + 1, idxTagClose);
			if (value.isBlank()==true) continue;
			
			switch (key) {
			case KEY_ID:
				dbSource.setId(NumberHelper.parseInteger(value));
				break;
			case KEY_NAME:
				dbSource.setName(value);
				break;
			case KEY_DESCRIPTION:
				dbSource.setDescription(value);
				break;
			case KEY_ROWS_PER_PAGE:
				Integer rowsPerPage = NumberHelper.parseInteger(value);
				if (rowsPerPage!=null) dbSource.setRowsPerPage(rowsPerPage);
				break;
				
			case KEY_DBMS_NAME:
				dbSource.setDBMSName(value);
				break;
			case KEY_CONNECTION_URL:
				dbSource.setConnectionURL(value);
				break;
			case KEY_DB_NAME:
				dbSource.setDbName(value);
				break;
			case KEY_USER_NAME:
				dbSource.setUserName(value);
				break;
			case KEY_PASSWORD:
				dbSource.setPassword(value);
				break;
			}
		} // end for
		return dbSource;
	}
}
