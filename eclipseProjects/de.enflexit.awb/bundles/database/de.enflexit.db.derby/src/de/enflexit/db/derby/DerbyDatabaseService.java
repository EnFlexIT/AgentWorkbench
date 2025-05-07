package de.enflexit.db.derby;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.derby.jdbc.ClientDriver;

import de.enflexit.db.hibernate.AbstractDatabaseService;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.gui.AbstractDatabaseSettingsPanel;

/**
 * The Class DerbyDatabaseService provides the {@link HibernateDatabaseService}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen 
 */
public class DerbyDatabaseService extends AbstractDatabaseService {

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getDatabaseSystemName()
	 */
	@Override
	public String getDatabaseSystemName() {
		return "Apache Derby (Network Server)";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getDriverClass()
	 */
	@Override
	public String getDriverClassName() {
		return DerbyDatabaseService.getDatabaseDriverClassName();
	}
	/**
	 * Returns the JDBC driver class name of Derby.
	 * @return the JDBC driver class name
	 */
	public static String getDatabaseDriverClassName() {
		return ClientDriver.class.getName();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.AbstractDatabaseService#getMessagePrefix()
	 */
	@Override
	protected String getMessagePrefix() {
		return "Apache Derby - connection check";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getHibernateDefaultPropertySettings()
	 */
	@Override
	public Properties getHibernateDefaultPropertySettings() {
		Properties defaultProps = new Properties();
		defaultProps.setProperty(HIBERNATE_PROPERTY_DriverClass, this.getDriverClassName());
		defaultProps.setProperty(HIBERNATE_PROPERTY_Catalog, "agentWorkbench");
		defaultProps.setProperty(HIBERNATE_PROPERTY_URL, "jdbc:derby://localhost:1527/agentWorkbench;create=true");
		defaultProps.setProperty(HIBERNATE_PROPERTY_UserName, "derby");
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
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#createDatabase(java.sql.Connection, java.lang.String)
	 */
	@Override
	public boolean createDatabase(Connection connection, String dbToCreate) {
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE DATABASE \"" + dbToCreate + "\"");
			return true;
			
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			if (statement!=null) {
				try {
					statement.close();
				} catch (SQLException sqlEx) {
					sqlEx.printStackTrace();
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#dropDatabase(java.sql.Connection, java.lang.String)
	 */
	@Override
	public boolean dropDatabase(Connection connection, String dbToDrop) {
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DROP DATABASE \"" + dbToDrop + "\"");
			return true;
			
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			if (statement!=null) {
				try {
					statement.close();
				} catch (SQLException sqlEx) {
					sqlEx.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getDatabaseList(java.sql.Connection)
	 */
	@Override
	public List<String> getDatabaseList(Connection connection) {
		
		List<String> dbList = new ArrayList<>();
		
		Path dbPath = Path.of(System.getProperty("derby.system.home"));
		File dbDirectory = dbPath.toFile();
		for (File checkFile : dbDirectory.listFiles()) {
			if (checkFile.isDirectory()==true) {
				dbList.add(checkFile.getName());
			}
		}
		return dbList;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.HibernateDatabaseService#getHibernateSettingPanel()
	 */
	@Override
	public AbstractDatabaseSettingsPanel getHibernateSettingsPanel() {
		return new DerbySettingsPanel();
	}

}
