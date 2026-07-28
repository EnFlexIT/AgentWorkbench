package de.enflexit.logging.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hibernate.cfg.Configuration;

import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;

/**
 * The Class LoggingDataSource.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class LoggingDataSource implements DataSource {

	private Connection connection;

	/**
	 * Returns the actual configuration.
	 * @return the actual configuration
	 */
	private Configuration getActualConfiguration() {
		return DatabaseConnectionManager.getInstance().getActualConfiguration(LoggingDatabaseConnectionService.SESSION_FACTORY_ID);
	}
	
	/* (non-Javadoc)
	* @see javax.sql.DataSource#getConnection()
	*/
	@Override
	public Connection getConnection() throws SQLException {
		return this.getConnection(null, null);
	}

	/* (non-Javadoc)
	* @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	*/
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		if (connection==null || connection.isClosed()==true || connection.isValid(0)==false) {
			
			Configuration config = this.getActualConfiguration();
			// --- Use driver class to determine the appropriate dbService --------------
			String driverClass = config.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass);
			HibernateDatabaseService dbService = HibernateUtilities.getDatabaseServiceByDriverClassName(driverClass);
			if (dbService==null) return null;
			
			Vector<String> msgVector = new Vector<>(); 
			if (connection!=null) connection.close();
			connection = dbService.getDatabaseConnection(config.getProperties(), msgVector, false, false);

			if (connection==null) {
				msgVector.forEach(msg -> System.err.println("[" + this.getClass().getSimpleName() + "] " + msg));
			}
		}
		return connection;
	}
	
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
	
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}
	
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

}