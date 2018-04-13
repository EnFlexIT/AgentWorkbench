package de.enflexit.db.mySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import com.mysql.jdbc.Driver;

import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.gui.DatabaseSettingPanel;

/**
 * The Class MySQLDatabaseService provides the {@link HibernateDatabaseService}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen 
 */
public class MySQLDatabaseService implements HibernateDatabaseService {

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
		return "MySQL";
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
		defaultProps.setProperty(HIBERNATE_PROPERTY_URL, "jdbc:mysql://localhost:3306/agentWorkbench?createDatabaseIfNotExist=true");
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
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#isDatabaseAccessible(java.util.Properties)
	 */
	@Override
	public boolean isDatabaseAccessible(Properties hibernateProperties, boolean doSilentConnectionCheck) {

		String driverClass = hibernateProperties.getProperty(HIBERNATE_PROPERTY_DriverClass); 
		String db = hibernateProperties.getProperty(HIBERNATE_PROPERTY_Catalog);
		String url = hibernateProperties.getProperty(HIBERNATE_PROPERTY_URL);
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
			
			// --- Space for further connection tests ---------------
			
			
		} catch (NullPointerException npEx) {
			String configString = "Driver: " + driverClass + ", URL: " + url + ", DB: " + db + ", User: " + user + ", PSWD: " + pswd + "";
			if (doSilentConnectionCheck==false) System.err.println("[MySQL connection check] Failed throw NullPointerException with config: " + configString);
			return false;
			
		} catch (SQLException sqlEx) {
			String mySQLErr = "[MySQL connection check] Err. Code:" + sqlEx.getErrorCode() + " | State: " + sqlEx.getSQLState() + ": " + sqlEx.getMessage();
			mySQLErr = mySQLErr.replaceAll("(?m)^[ \t]*\r?\n", ""); // replace empty lines 
			if (doSilentConnectionCheck==false) System.err.println(mySQLErr);
			return false;
			
		} catch (ClassNotFoundException cnfEx) {
			if (doSilentConnectionCheck==false) System.err.println("[MySQL connection check] Failed throw: " + cnfEx.getMessage());
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

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getHibernateSettingPanel()
	 */
	@Override
	public DatabaseSettingPanel getHibernateSettingPanel() {
		return new MySQLSettingPanel();
	}

}
