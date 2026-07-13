package de.enflexit.db.hibernate.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import org.hibernate.cfg.Configuration;

import de.enflexit.common.NumberHelper;
import de.enflexit.common.StringHelper;
import de.enflexit.db.dataSources.DatabaseDataSource;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;

/**
 * The Class DatabaseSettings represents a data type that describes possible
 * hibernate database settings in relation to a database system.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DatabaseSettings implements Serializable {

	private static final long serialVersionUID = -4296502128683511660L;

	private String databaseSystemName;
	private Properties hibernateDatabaseSettings;
	
	
	/**
	 * Default constructor for DatabaseSettings (for the JAXB context only).
	 */
	public DatabaseSettings() {	}
	/**
	 * Instantiates a new database settings.
	 *
	 * @param databaseSystemName the database system name
	 * @param hibernateDatabaseSettings the hibernate database settings as java properties
	 */
	public DatabaseSettings(String databaseSystemName, Properties hibernateDatabaseSettings) {
		this.setDatabaseSystemName(databaseSystemName);
		this.setHibernateDatabaseSettings(hibernateDatabaseSettings);
	}
	
	/**
	 * Instantiates a new database settings.
	 *
	 * @param databaseSystemName the database system name 
	 * @param hibernateConfiguration the hibernate configuration
	 */
	public DatabaseSettings(String databaseSystemName, Configuration hibernateConfiguration) {
		this.setDatabaseSystemName(databaseSystemName);
		this.setHibernateConfiguration(hibernateConfiguration);
	}
	
	/**
	 * Gets the database system name.
	 * @return the database system name
	 */
	public String getDatabaseSystemName() {
		return databaseSystemName;
	}
	/**
	 * Sets the database system name.
	 * @param databaseSystemName the new database system name
	 */
	public void setDatabaseSystemName(String databaseSystemName) {
		this.databaseSystemName = databaseSystemName;
	}
	
	/**
	 * Sets the hibernate configuration that will be translated into java properties.
	 * @param configuration the new configuration
	 */
	public void setHibernateConfiguration(Configuration configuration) {

		HibernateDatabaseService hds = HibernateUtilities.getDatabaseService(this.getDatabaseSystemName());
		if (hds!=null) {
			// -- Extract properties from hibernate configuration ---
			Properties props = new Properties();
			Vector<String> propNameVector = hds.getHibernateConfigurationPropertyNamesForDbCheckOnJDBC();
			for (int i = 0; i < propNameVector.size(); i++) {
				String propName = propNameVector.get(i);
				String propValue = configuration.getProperty(propName);
				if (propValue!=null) {
					props.setProperty(propName, propValue);
				}
			}
			this.setHibernateDatabaseSettings(props);
		}
	}
	
	/**
	 * Gets the hibernate database settings.
	 * @return the hibernate database settings
	 */
	public Properties getHibernateDatabaseSettings() {
		return hibernateDatabaseSettings;
	}
	/**
	 * Sets the hibernate database settings.
	 * @param hibernateDatabaseSettings the new hibernate database settings
	 */
	public void setHibernateDatabaseSettings(Properties hibernateDatabaseSettings) {
		this.hibernateDatabaseSettings = hibernateDatabaseSettings;
	}
	
	/**
	 * Checks if the current instance is empty.
	 * @return true, if the current settings instance is empty
	 */
	public boolean isEmpty() {
		boolean isAvailableDatabaseSystemName = (this.getDatabaseSystemName()!=null && this.getDatabaseSystemName().isBlank()==false);
		boolean hasDatabaseSettingEntries     = (this.hibernateDatabaseSettings!=null && this.hibernateDatabaseSettings.size()!=0);
		return isAvailableDatabaseSystemName==false & hasDatabaseSettingEntries==false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject instanceof DatabaseSettings) {
			// --- Cast the object to a DatabaseSettings instance and compare it --------
			DatabaseSettings compareSettings = (DatabaseSettings) compareObject;
			
			// --- Compare the database system name -------------------------------------
			String databaseSystemNameComp = compareSettings.getDatabaseSystemName();
			String databaseSystemNameLocal = this.getDatabaseSystemName();
			if ((databaseSystemNameComp==null && databaseSystemNameLocal!=null) || (databaseSystemNameComp!=null && databaseSystemNameLocal==null)) return false;
			if (databaseSystemNameComp!=null && databaseSystemNameLocal!=null && compareSettings.getDatabaseSystemName().equals(this.getDatabaseSystemName())==false) return false;
			
			// --- Compare the hibernate database settings ------------------------------
			Properties propertiesCompare = compareSettings.getHibernateDatabaseSettings();
			Properties propertiesLocal = this.getHibernateDatabaseSettings();
			if ((propertiesCompare==null && propertiesLocal!=null) || (propertiesCompare!=null && propertiesLocal==null)) return false;
			if (propertiesCompare==null && propertiesLocal==null) return true;		// If both are empty, they are equal
			if (compareSettings.getHibernateDatabaseSettings().size()!=this.getHibernateDatabaseSettings().size()) return false;

			// --- Compare each property and value --------------------------------------
			ArrayList<?> propKeys = new ArrayList<>(compareSettings.getHibernateDatabaseSettings().keySet());
			for (int i = 0; i < propKeys.size(); i++) {
				String comparePropKey = (String) propKeys.get(i);
				String comparePropValue = compareSettings.getHibernateDatabaseSettings().getProperty(comparePropKey);
				String localPropValue = this.getHibernateDatabaseSettings().getProperty(comparePropKey);
				if (StringHelper.isEqualString(comparePropValue, localPropValue)==false) {
					return false;
				}
			}
			return true;	
		}
		return false;
	}
	
	
	/**
	 * Converts the current settings to a {@link DatabaseDataSource} .
	 * @return the database data source
	 */
	public DatabaseDataSource toDataSource() {
		return toDataSource(this);
	}
	/**
	 * Converts the current settings to a {@link DatabaseDataSource} .
	 * @return the database data source
	 */
	public static DatabaseDataSource toDataSource(DatabaseSettings dbSettings) {
		
		if (dbSettings==null) return null;
		
		DatabaseDataSource dbDS = new DatabaseDataSource();
		
		dbDS.setDBMSName(dbSettings.getDatabaseSystemName());
		dbDS.setConnectionURL(dbSettings.getHibernateDatabaseSettings().getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_URL));
		dbDS.setDbName(dbSettings.getHibernateDatabaseSettings().getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog));
		
		dbDS.setUserName(dbSettings.getHibernateDatabaseSettings().getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName));
		dbDS.setPassword(dbSettings.getHibernateDatabaseSettings().getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Password));

		// --- Super class attributes -----------
		dbDS.setId(NumberHelper.parseInteger(dbSettings.getHibernateDatabaseSettings().getProperty(DatabaseDataSource.KEY_ID)));
		dbDS.setName(dbSettings.getHibernateDatabaseSettings().getProperty(DatabaseDataSource.KEY_NAME));
		dbDS.setDescription(dbSettings.getHibernateDatabaseSettings().getProperty(DatabaseDataSource.KEY_DESCRIPTION));
		Integer rowsPerPage = NumberHelper.parseInteger(dbSettings.getHibernateDatabaseSettings().getProperty(DatabaseDataSource.KEY_ROWS_PER_PAGE));
		if (rowsPerPage!=null) dbDS.setRowsPerPage(rowsPerPage);
		
		return dbDS;
	}
	/**
	 * Converts the specified {@link DatabaseDataSource} to {@link DatabaseSettings}.
	 * 
	 * @param dbDataSource the DatabaseDataSource to convert
	 * @return the database settings
	 */
	public static DatabaseSettings fromDataSource(DatabaseDataSource dbDataSource) {
		
		if (dbDataSource==null) return null;
		
		DatabaseSettings dbSettings = new DatabaseSettings();
		
		dbSettings.setDatabaseSystemName(dbDataSource.getDBMSName());
		dbSettings.setHibernateDatabaseSettings(new Properties());

		if (dbDataSource.getConnectionURL()!=null) 	dbSettings.getHibernateDatabaseSettings().setProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_URL, dbDataSource.getConnectionURL());
		if (dbDataSource.getDbName()!=null) 		dbSettings.getHibernateDatabaseSettings().setProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog, dbDataSource.getDbName());
		
		if (dbDataSource.getUserName()!=null) 		dbSettings.getHibernateDatabaseSettings().setProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName, dbDataSource.getUserName());
		if (dbDataSource.getPassword()!=null) 		dbSettings.getHibernateDatabaseSettings().setProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Password, dbDataSource.getPassword());
		
		// --- Super class attributes -----------
		dbSettings.getHibernateDatabaseSettings().setProperty(DatabaseDataSource.KEY_ID, dbDataSource.getId()==null ? "" : dbDataSource.getId().toString());
		dbSettings.getHibernateDatabaseSettings().setProperty(DatabaseDataSource.KEY_NAME, dbDataSource.getName());
		dbSettings.getHibernateDatabaseSettings().setProperty(DatabaseDataSource.KEY_DESCRIPTION, dbDataSource.getDescription());
		dbSettings.getHibernateDatabaseSettings().setProperty(DatabaseDataSource.KEY_ROWS_PER_PAGE, dbDataSource.getRowsPerPage() + "");
		
		if (dbSettings.isEmpty()==true) {
			return null;
		}
		return dbSettings;
	}
	
}
