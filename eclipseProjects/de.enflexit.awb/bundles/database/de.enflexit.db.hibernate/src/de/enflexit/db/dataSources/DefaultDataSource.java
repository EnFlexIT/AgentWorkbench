package de.enflexit.db.dataSources;

import de.enflexit.common.NumberHelper;
import de.enflexit.common.StringHelper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class DefaultDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "data_sources")

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "id",
    "name",
    "description",
    "rowsPerPage",
    "storageConfiguration"
})
public class DefaultDataSource implements DataSource {

	private static final long serialVersionUID = -1124708006041779794L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_data_source", nullable=false)
	private int id;
	
	@Column(nullable=true)
	private String name;
	@Column(nullable=true)
	private String description;
	
	private int rowsPerPage = 0;
	
	@Column(name="storage_configuration", nullable=true, length = 10000)
	private String storageConfiguration;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#newInstance()
	 */
	@Override
	public DefaultDataSource newInstance() {
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#getDataSourceIdentifier()
	 */
	@Override
	public String getDataSourceIdentifier() {
		return this.getClass().getSimpleName();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#getId()
	 */
	@Override
	public int getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#setId(int)
	 */
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#getRowsPerPage()
	 */
	@Override
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#setRowsPerPage(int)
	 */
	@Override
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	
	
	/**
	 * Updates the storage configuration.
	 */
	public final void updateStorageConfiguration() {
		this.setStorageConfiguration(null);
		this.getStorageConfiguration();
	}
	/**
	 * Returns the storage configuration.
	 * @return the storage configuration
	 */
	public final String getStorageConfiguration() {
		if (storageConfiguration==null) {
			storageConfiguration = this.getDataSourceIdentifier() + "::" + this.toConfigurationString();
		}
		return storageConfiguration;
	}
	/**
	 * Sets the current StorageConfiguration.
	 * @param storageConfiguration the new storage configuration
	 */
	public final void setStorageConfiguration(String storageConfiguration) {
		this.storageConfiguration = storageConfiguration;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null) return false;
		if (compObj instanceof DefaultDataSource ==false) return false;
		
		if (compObj==this) return true;

		// --- Compare the abstract data source first ---------------
		DefaultDataSource adsComp = (DefaultDataSource) compObj;
		
		//if (NumberHelper.isEqualNumber(this.getId(), adsComp.getId())==false) return false;
		if (StringHelper.isEqualString(this.getName(), adsComp.getName())==false) return false;
		if (StringHelper.isEqualString(this.getDescription(), adsComp.getDescription())==false) return false;
		if (NumberHelper.isEqualNumber(this.getRowsPerPage(), adsComp.getRowsPerPage())==false) return false;
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getStorageConfiguration();
	}
	
	/**
	 * Adds a configuration key/value entry to the specified configuration String.
	 *
	 * @param configString the current configuration string
	 * @param key the value key
	 * @param value the value
	 */
	public static String addConfigValue(String configString, String key, String value) {
		
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

	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#toConfigurationString()
	 */
	@Override
	public String toConfigurationString() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#fromConfigurationString(java.lang.String)
	 */
	@Override
	public DataSource fromConfigurationString(String configurationString) {
		return null;
	}
	
}
