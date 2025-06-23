package de.enflexit.awb.ws.core.session;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hibernate.cfg.Configuration;

import de.enflexit.awb.ws.core.db.WebAppDatabaseConnectionService;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;


/**
 * The Class AwbDatabaseAdaptorDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbDatabaseAdaptorDataSource implements DataSource {

	private Connection dbConn;
	
	/**
	 * Returns the actual configuration.
	 * @return the actual configuration
	 */
	private Configuration getActualConfiguration() {
		return DatabaseConnectionManager.getInstance().getActualConfiguration(WebAppDatabaseConnectionService.SESSION_FACTORY_ID);
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
		if (dbConn==null || dbConn.isClosed()==true || dbConn.isValid(0)==false) {

			Configuration config = this.getActualConfiguration();
			
			String driverClass = config.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass);
			HibernateDatabaseService dbService = HibernateUtilities.getDatabaseServiceByDriverClassName(driverClass);
			if (dbService==null) return null;
			
			Vector<String> msgVector = new Vector<>(); 
			if (dbConn!=null) dbConn.close();
			dbConn = dbService.getDatabaseConnection(config.getProperties(), msgVector, false);
			if (dbConn==null) {
				msgVector.forEach(msg -> System.err.println("[" + this.getClass().getSimpleName() + "] " + msg));
			}
		}
		return dbConn;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}
	/* (non-Javadoc)
	 * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		
	}
	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
	
}
