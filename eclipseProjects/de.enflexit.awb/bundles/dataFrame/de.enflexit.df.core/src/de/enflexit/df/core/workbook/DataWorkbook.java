package de.enflexit.df.core.workbook;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.enflexit.common.NumberHelper;
import de.enflexit.common.StringHelper;
import de.enflexit.db.dataSources.DefaultDataSource;
import de.enflexit.db.dataSources.DataSource;
import de.enflexit.db.dataSources.DataSourceHelper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class DataWorkbook.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "data_workbook")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataWorkbook", propOrder = {
    "id",
    "name",
    "description",
    "dataSources"
})
public abstract class DataWorkbook implements Serializable {

	private static final long serialVersionUID = 116362577147783546L;
	
	@Id
	@GeneratedValue
	@Column(name="id_data_workbook", nullable=false)
	private Integer id;
	@Column(nullable=false)
	private String name;
	private String description;
	
	@XmlElementWrapper(name = "dataSources")
	@XmlElement(name = "dataSource") 
	protected List<DefaultDataSource> dataSources;
	
	/**
	 * Instantiates a new data workbook.
	 */
	public DataWorkbook() {
		this(null, null, null, null);
	}
	/**
	 * Instantiates a new DataWorkbook.
	 *
	 * @param id the id
	 * @param name the name
	 * @param description the description
	 */
	public DataWorkbook(Integer id, String name, String description) {
		this(id, name, description, null);
	}
	/**
	 * Instantiates a new DataWorkbook.
	 *
	 * @param id the id
	 * @param name the name
	 * @param description the description
	 * @param dataSources the data sources
	 */
	public DataWorkbook(Integer id, String name, String description, List<DefaultDataSource> dataSources) {
		this.setID(id);
		this.setName(name);
		this.setDescription(description);
		this.setDataSources(dataSources);
	}

	/**
	 * Returns the id.
	 * @return the id
	 */
	public Integer getID() {
		if (id==null) {
			this.createRandomID();
		}
		return id;
	}
	/**
	 * Sets the id.
	 * @param id the new id
	 */
	public void setID(Integer id) {
		this.id = id;
	}
	/**
	 * Creates a random integer ID, if the ID is not already defined for the {@link DataWorkbook}.
	 * @return the ID of the DataWorkbook, the current or the newly defined ID
	 */
	private void createRandomID() {
		if (this.id==null) {
			this.id = NumberHelper.getRandomInteger(0, Integer.MAX_VALUE);
		}
	}
	
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name.
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Sets the description.
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the list of data sources or null, if nothing was initialized yet.
	 * @return the data sources allow null
	 */
	public List<DefaultDataSource> getDataSourcesAllowNull() {
		return dataSources;
	}
	/**
	 * Returns the data sources.
	 * @return the data sources
	 */
	public List<DefaultDataSource> getDataSources() {
		if (dataSources==null) {
			dataSources = new ArrayList<>();
		}
		return dataSources;
	}
	/**
	 * Sets the data sources.
	 * @param dataSources the new data sources
	 */
	public void setDataSources(List<DefaultDataSource> dataSources) {
		this.dataSources = dataSources;
	}

	/**
	 * Adds the specified DataSource to the list of data sources.
	 *
	 * @param dataSource the data source
	 * @return true, if successful
	 */
	public boolean addDataSource(DefaultDataSource dataSource) {
		if (dataSource==null || this.getDataSources().contains(dataSource)==true) return false; 
		return this.getDataSources().add(dataSource);
	}
	/**
	 * Removes the specified DataSource from the list of data sources.
	 *
	 * @param dataSource the data source
	 * @return true, if successful
	 */
	public boolean removeDataSource(DefaultDataSource dataSource) {
		if (dataSource==null) return false;
		return this.getDataSources().remove(dataSource);
	}
	
	/**
	 * Sets the {@link DataSource}s to its storage configuration.
	 */
	public void setDataSourcesToStorageConfiguration() {
 
		if (this.getDataSourcesAllowNull()!=null) {
			this.getDataSources().forEach(ds -> {
				try {
					ds.updateStorageConfiguration();	
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		}
	}
	/**
	 * Configures the {@link DataSource}s from the actual storage configurations.
	 */
	public void setDataSourcesFromStorageConfiguration() {
		
		List<DefaultDataSource> dsListToEdit = this.getDataSources();
		List<DefaultDataSource> deleteCandidate = new ArrayList<>();
		
		if (this.getDataSourcesAllowNull()!=null) {
			// --- Get the DataSource HashMap now, to avoid multiple access ---
			HashMap<String, DataSource> dsHashMap = DataSourceHelper.getDataSourceServiceHashMap(); 
			
			// --- Convert each data source to the correct instance -----------
			for (int i = 0; i < dsListToEdit.size(); i++) {
				DefaultDataSource absDS = dsListToEdit.get(i);
				DefaultDataSource actDS = DataSourceHelper.toSpecificDataSource(dsHashMap, absDS);
				if (actDS==null) {
					deleteCandidate.add(absDS);
				} else {
					dsListToEdit.set(i, actDS);
				}
			}
		}
		
		// --- Remove the unconvertible DataSources from the list -------------
		deleteCandidate.forEach(dsDelete -> dsListToEdit.remove(dsDelete));
	}
	
	
	/**
	 * Has to save the current data workbook.
	 * @return true, if successful
	 */
	public abstract boolean save();
	
	/**
	 * Has to close the current DataWorkbook.
	 */
	public abstract void close();
	
	/**
	 * Has to return the the data workbook file or <code>null</code>., if noc file are involved.
	 * @return the data workbook file
	 */
	public abstract File getDataWorkbookFile();

	/**
	 * Has to return the individual DataWorkbookLocation.
	 * @return the data workbook location
	 */
	public abstract DataWorkbookLocation getDataWorkbookLocation();
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null) return false;
		if (compObj instanceof DataWorkbook ==false) return false;
		
		if (compObj==this) return true;

		// --- Compare the DataWorkbook first -----------------------
		DataWorkbook dwComp = (DataWorkbook) compObj;
		
		// --- Compare ID, name and description ---------------------
		//if (NumberHelper.isEqualNumber(this.getID(), dwComp.getID())==false) return false;
		if (StringHelper.isEqualString(this.getName(), dwComp.getName())==false) return false;
		if (StringHelper.isEqualString(this.getDescription(), dwComp.getDescription())==false) return false;
		
		// --- Compare DataWorkbookLocation -------------------------
		if (this.getDataWorkbookLocation()==null && dwComp.getDataWorkbookLocation()==null) {
			// --- Nothing to do here ---
		} else if ((this.getDataWorkbookLocation()!=null && dwComp.getDataWorkbookLocation()==null) || (this.getDataWorkbookLocation()==null && dwComp.getDataWorkbookLocation()!=null)) {
			return false;
		} else {
			if (this.getDataWorkbookLocation().equals(dwComp.getDataWorkbookLocation())==false) return false;
		}
		
		// --- Compare workbook file --------------------------------
		if (this.getDataWorkbookFile()==null && dwComp.getDataWorkbookFile()==null) {
			// --- Nothing to do here ---
		} else if ((this.getDataWorkbookFile()!=null && dwComp.getDataWorkbookFile()==null) || (this.getDataWorkbookFile()==null && dwComp.getDataWorkbookFile()!=null)) {
			return false;
		} else {
			if (this.getDataWorkbookFile().equals(dwComp.getDataWorkbookFile())==false) return false;
		}
		
		// --- Compare data sources ---------------------------------
		if (this.getDataSources().equals(dwComp.getDataSources())==false) return false;
		
		return true;
	}
	
}
