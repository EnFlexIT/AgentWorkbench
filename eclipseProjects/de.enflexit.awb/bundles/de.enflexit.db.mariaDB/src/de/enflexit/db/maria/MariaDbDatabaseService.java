package de.enflexit.db.maria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import org.mariadb.jdbc.Driver;

import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.gui.AbstractDatabaseSettingsPanel;

/**
 * The Class MariaDbDatabaseService provides the {@link HibernateDatabaseService}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen 
 */
public class MariaDbDatabaseService implements HibernateDatabaseService {

	public static final String HIBERNATE_PROPERTY_DriverClass = "hibernate.connection.driver_class";
	public static final String HIBERNATE_PROPERTY_Catalog = "hibernate.default_catalog";
	public static final String HIBERNATE_PROPERTY_URL = "hibernate.connection.url";
	public static final String HIBERNATE_PROPERTY_UserName = "hibernate.connection.username";
	public static final String HIBERNATE_PROPERTY_Password = "hibernate.connection.password";
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getDatabaseSystemName()
	 */
	@Override
	public String getDatabaseSystemName() {
		return "MariaDB";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getDriverClass()
	 */
	@Override
	public String getDriverClassName() {
		return Driver.class.getName();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getHibernateDefaultPropertySettings()
	 */
	@Override
	public Properties getHibernateDefaultPropertySettings() {
		Properties defaultProps = new Properties();
		defaultProps.setProperty(HIBERNATE_PROPERTY_DriverClass, this.getDriverClassName());
		defaultProps.setProperty(HIBERNATE_PROPERTY_Catalog, "agentWorkbench");
		defaultProps.setProperty(HIBERNATE_PROPERTY_URL, "jdbc:mariadb://localhost:3306/agentWorkbench?createDatabaseIfNotExist=true");
		defaultProps.setProperty(HIBERNATE_PROPERTY_UserName, "root");
		defaultProps.setProperty(HIBERNATE_PROPERTY_Password, "");
		return defaultProps;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getHibernateConfigurationPropertyNamesForDbCheckOnJDBC()
	 */
	@Override
	public Vector<String> getHibernateConfigurationPropertyNamesForDbCheckOnJDBC() {
		Vector<String> propertyName = new Vector<>();
		propertyName.add(HIBERNATE_PROPERTY_DriverClass);
		propertyName.add(HIBERNATE_PROPERTY_Catalog);
		propertyName.add(HIBERNATE_PROPERTY_URL);
		propertyName.add(HIBERNATE_PROPERTY_UserName);
		propertyName.add(HIBERNATE_PROPERTY_Password);
		return propertyName;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#isDatabaseAccessible(java.util.Properties, java.util.Vector, boolean)
	 */
	@Override
	public boolean isDatabaseAccessible(Properties hibernateProperties, Vector<String> userMessageVector, boolean isPrintToConole) {

		String message = null;
		if (hibernateProperties==null) {
			message = "[MySQL connection check] no properties were specified for the connection test!";
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			return false;
		}
		
		String driverClass = hibernateProperties.getProperty(HIBERNATE_PROPERTY_DriverClass); 
		String url = hibernateProperties.getProperty(HIBERNATE_PROPERTY_URL);
		String db = hibernateProperties.getProperty(HIBERNATE_PROPERTY_Catalog);
		String user = hibernateProperties.getProperty(HIBERNATE_PROPERTY_UserName);
		String pswd = hibernateProperties.getProperty(HIBERNATE_PROPERTY_Password);

		Connection conn = null;
		try {
			// --- Try to establish the MySQL connection ------------
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, user, pswd);
			if (db!=null && db.isEmpty()==false) {
				conn.setCatalog(db);
			}
			
			// --- Check if the database exists ---------------------
			if (this.databaseExists(conn, db)==false) {
				message = "[MariaDB connection check] Database '" + db + "' does not exists (yet)!";
				userMessageVector.addElement(message);
				if (isPrintToConole) System.err.println(message);
			}
			
		} catch (NullPointerException npEx) {
			String configString = "Driver: " + driverClass + ", URL: " + url + ", DB: " + db + ", User: " + user + ", PSWD: " + pswd + "";
			message = "[MariaDB connection check] Failed throw NullPointerException with config: " + configString;
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			return false;
			
		} catch (SQLException sqlEx) {
			String mySQLErr = "[MariaDB connection check] Err. Code:" + sqlEx.getErrorCode() + " | State: " + sqlEx.getSQLState() + ": " + sqlEx.getMessage();
			mySQLErr = mySQLErr.replaceAll("(?m)^[ \t]*\r?\n", ""); // replace empty lines
			userMessageVector.addElement(mySQLErr);
			if (isPrintToConole) System.err.println(mySQLErr);
			return false;
			
		} catch (ClassNotFoundException cnfEx) {
			message = "[MariaDB connection check] Failed throw: " + cnfEx.getMessage();
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			return false;
		
		} finally {
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException sqlEx) {
					sqlEx.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the specified database exists.
	 *
	 * @param connection the connection
	 * @param dbNameToCheck the db name to check
	 * @return true, if successful
	 */
	private boolean databaseExists(Connection connection, String dbNameToCheck) {
		
		boolean dbExists = true;
		ResultSet resultSet = null;
		try {
			resultSet = connection.getMetaData().getCatalogs();
			while (resultSet.next()) {
				String databaseName = resultSet.getString(1);
				if (databaseName.equalsIgnoreCase(dbNameToCheck)) {
					return true;
				}
			}
			
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			dbExists = false;
		} finally {
			if (resultSet!=null) {
				try {
					resultSet.close();
				} catch (SQLException sqlEx) {
					sqlEx.printStackTrace();
				}
			}
		}
		return dbExists;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getHibernateSettingPanel()
	 */
	@Override
	public AbstractDatabaseSettingsPanel getHibernateSettingsPanel() {
		return new MariaDbSQLSettingsPanel();
	}

}
