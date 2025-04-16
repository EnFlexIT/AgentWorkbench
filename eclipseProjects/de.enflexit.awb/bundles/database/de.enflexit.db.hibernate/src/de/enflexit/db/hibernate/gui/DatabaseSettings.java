package de.enflexit.db.hibernate.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import org.hibernate.cfg.Configuration;

import de.enflexit.common.StringHelper;
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
	@Deprecated
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
	
}
