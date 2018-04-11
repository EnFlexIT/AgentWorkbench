package de.enflexit.db.mySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import com.mysql.jdbc.Driver;

import de.enflexit.db.hibernate.HibernateDatabaseService;

/**
 * The Class DatabaseService provides the {@link HibernateDatabaseService}.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen 
 */
public class DatabaseService implements HibernateDatabaseService {

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getDriverClass()
	 */
	@Override
	public String getDriverClassName() {
		return Driver.class.getName();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getHibernateConfigurationPropertyNamesForDbCheckOnJDBC()
	 */
	@Override
	public Vector<String> getHibernateConfigurationPropertyNamesForDbCheckOnJDBC() {
		Vector<String> propertyName = new Vector<>();
		propertyName.add("hibernate.connection.driver_class");
		propertyName.add("hibernate.default_catalog");
		propertyName.add("hibernate.connection.url");
		propertyName.add("hibernate.connection.username");
		propertyName.add("hibernate.connection.password");
		return propertyName;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#isDatabaseAccessible(java.util.Properties)
	 */
	@Override
	public boolean isDatabaseAccessible(Properties hibernateProperties, boolean doSilentConnectionCheck) {

		String driverClass = hibernateProperties.getProperty("hibernate.connection.driver_class"); 
		String db = hibernateProperties.getProperty("hibernate.default_catalog");
		String url = hibernateProperties.getProperty("hibernate.connection.url");
		String user = hibernateProperties.getProperty("hibernate.connection.username");
		String pswd = hibernateProperties.getProperty("hibernate.connection.password");

		Connection conn = null;
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, user, pswd);
			if (db!=null && db.isEmpty()==false) {
				conn.setCatalog(db);
			}
			
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
		}
		
		// --- Space for further connection tests ---------
		if (conn!=null) {
			
			
		}
		return true;
	}

	

}
