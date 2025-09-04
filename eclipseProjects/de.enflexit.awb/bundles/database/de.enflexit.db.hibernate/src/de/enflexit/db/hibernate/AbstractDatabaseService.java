package de.enflexit.db.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * The Class AbstractDatabaseService provides a default way to get {@link Connection} or
 * to check if a database is available.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractDatabaseService implements HibernateDatabaseService {

	/**
	 * Has to return the prefix for messages that are to be written to a message vector or to the console.
	 * @return the message prefix
	 */
	protected abstract String getMessagePrefix();
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getDriver()
	 */
	@Override
	public Driver getDriver(Properties hibernateProperties) {

		String message = null;
		if (hibernateProperties==null) {
			message = "[" + this.getMessagePrefix() +"] No properties were specified for the connection!";
			System.err.println(message);
			return null;
		}
		
		Driver driver = null;
		try {
			String driverClassName = hibernateProperties.getProperty(HIBERNATE_PROPERTY_DriverClass);
			if (driverClassName!=null && driverClassName.isBlank()==false) {
				// --- Try getting the driver instance ------------------------
				Class<?> driverClass = Class.forName(driverClassName);
				Object driverObject = driverClass.getDeclaredConstructor().newInstance();
				if (driverObject instanceof Driver) {
					driver = (Driver) driverObject; 
				}
			}

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return driver;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getDatabaseConnection(java.util.Properties, java.util.Vector, boolean)
	 */
	@Override
	public Connection getDatabaseConnection(Properties hibernateProperties, Vector<String> userMessageVector, boolean isPrintToConole) {
		return this.getDatabaseConnection(hibernateProperties, userMessageVector, isPrintToConole, true);
	}
		
	public Connection getDatabaseConnection(Properties hibernateProperties, Vector<String> userMessageVector, boolean isPrintToConole, boolean removeDbFromUrl) {

		String message = null;
		if (hibernateProperties==null) {
			message = "[" + this.getMessagePrefix() +"] No properties were specified for the connection!";
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			return null;
		}
		
		String driverClass = hibernateProperties.getProperty(HIBERNATE_PROPERTY_DriverClass); 
		String url = hibernateProperties.getProperty(HIBERNATE_PROPERTY_URL);
		String db = hibernateProperties.getProperty(HIBERNATE_PROPERTY_Catalog);
		String user = hibernateProperties.getProperty(HIBERNATE_PROPERTY_UserName);
		String pswd = hibernateProperties.getProperty(HIBERNATE_PROPERTY_Password);

		int lastSlash = url.lastIndexOf("/") + 1;
		if (driverClass.startsWith("org.apache.derby")==true) lastSlash=-1;
		
		if (removeDbFromUrl==true) {
			url = lastSlash!=-1 ? url.substring(0, lastSlash) : url;
		}
		
		try {
			// --- Check for user name and password -----------------
			boolean isUserSpecified = user!=null && user.isBlank()==false;
			boolean isPswdSpecified = pswd!=null && pswd.isBlank()==false;

			// --- Try to establish the Database connection ---------
			Connection conn = null;
			Class.forName(driverClass);
			if (isUserSpecified==true && isPswdSpecified==true) {
				conn = DriverManager.getConnection(url, user, pswd);
			} else {
				conn = DriverManager.getConnection(url);
			}
			return conn;
			
		} catch (NullPointerException npEx) {
			String configString = "Driver: " + driverClass + ", URL: " + url + ", DB: " + db + ", User: " + user + ", PSWD: " + pswd + "";
			message = "[" + this.getMessagePrefix() +"] Failed throw NullPointerException with config: " + configString;
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			
		} catch (SQLException sqlEx) {
			String mySQLErr = "[" + this.getMessagePrefix() +"] Err. Code:" + sqlEx.getErrorCode() + " | State: " + sqlEx.getSQLState() + ": " + sqlEx.getMessage();
			mySQLErr = mySQLErr.replaceAll("(?m)^[ \t]*\r?\n", ""); // replace empty lines
			userMessageVector.addElement(mySQLErr);
			if (isPrintToConole) System.err.println(mySQLErr);
			
		} catch (ClassNotFoundException cnfEx) {
			message = "[" + this.getMessagePrefix() +"] Failed throw: " + cnfEx.getMessage();
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#isDatabaseAccessible(java.util.Properties, java.util.Vector, boolean)
	 */
	@Override
	public boolean isDatabaseAccessible(Properties hibernateProperties, Vector<String> userMessageVector, boolean isPrintToConole) {

		String message = null;
		if (hibernateProperties==null) {
			message = "[" + this.getMessagePrefix() +"] No properties were specified for the connection test!";
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			return false;
		}
		
		String driverClass = hibernateProperties.getProperty(HIBERNATE_PROPERTY_DriverClass); 
		String url = hibernateProperties.getProperty(HIBERNATE_PROPERTY_URL);
		String db = hibernateProperties.getProperty(HIBERNATE_PROPERTY_Catalog);
		String user = hibernateProperties.getProperty(HIBERNATE_PROPERTY_UserName);
		String pswd = hibernateProperties.getProperty(HIBERNATE_PROPERTY_Password);

		if (db==null || db.isEmpty()==true || db.isBlank()==true) {
			message = "[" + this.getMessagePrefix() +"] No Database was specified for the connection test!";
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			return false;	
		}
		
		Connection conn = null;
		try {
			// --- Check for user name and password -----------------
			boolean isUserSpecified = user!=null && user.isBlank()==false;
			boolean isPswdSpecified = pswd!=null && pswd.isBlank()==false;
			
			// --- Try to establish the Database connection ---------
			Class.forName(driverClass);
			if (isUserSpecified==true && isPswdSpecified==true) {
				conn = DriverManager.getConnection(url, user, pswd);
			} else {
				conn = DriverManager.getConnection(url);
			}
			if (conn==null) return false;
			
			// --- Check if the database exists ---------------------
			if (this.databaseExists(conn, db)==false) {
				message = "[" + this.getMessagePrefix() +"] Database '" + db + "' does not exists (yet)!";
				userMessageVector.addElement(message);
				if (isPrintToConole) System.err.println(message);
				return false;
			} else {
				conn.setCatalog(db);
			}
			
		} catch (NullPointerException npEx) {
			String configString = "Driver: " + driverClass + ", URL: " + url + ", DB: " + db + ", User: " + user + ", PSWD: " + pswd + "";
			message = "[" + this.getMessagePrefix() +"] Failed throw NullPointerException with config: " + configString;
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			//npEx.printStackTrace();
			return false;
			
		} catch (SQLException sqlEx) {
			String mySQLErr = "[" + this.getMessagePrefix() +"] Err. Code:" + sqlEx.getErrorCode() + " | State: " + sqlEx.getSQLState() + ": " + sqlEx.getMessage();
			mySQLErr = mySQLErr.replaceAll("(?m)^[ \t]*\r?\n", ""); // replace empty lines
			userMessageVector.addElement(mySQLErr);
			if (isPrintToConole) System.err.println(mySQLErr);
			//sqlEx.printStackTrace();
			return false;
			
		} catch (ClassNotFoundException cnfEx) {
			message = "[" + this.getMessagePrefix() +"] Failed throw: " + cnfEx.getMessage();
			userMessageVector.addElement(message);
			if (isPrintToConole) System.err.println(message);
			//cnfEx.printStackTrace();
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
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#databaseExists(java.sql.Connection, java.lang.String)
	 */
	@Override
	public boolean databaseExists(Connection connection, String dbNameToCheck) {
		List<String> dbList = this.getDatabaseList(connection);
		if (dbList.contains(dbNameToCheck)==true) {
			return true;
		} else {
			// --- Do a non-case sensitive check ----------
			for (String dbName : dbList) {
				if (dbName.equalsIgnoreCase(dbNameToCheck)==true) {
					return true;
				}
			}
		}
		return false;
	}
	
}
