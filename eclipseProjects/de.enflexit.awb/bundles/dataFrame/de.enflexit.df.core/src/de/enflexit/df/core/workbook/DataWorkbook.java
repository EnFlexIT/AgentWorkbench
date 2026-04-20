package de.enflexit.df.core.workbook;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import de.enflexit.common.NumberHelper;
import de.enflexit.common.dataSources.AbstractDataSource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
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
	@XmlElementRef 
	private List<AbstractDataSource> dataSources;
	
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
	public DataWorkbook(Integer id, String name, String description, List<AbstractDataSource> dataSources) {
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
	public Integer createRandomID() {
		if (this.id==null) {
			this.id = NumberHelper.getRandomInteger(0, Integer.MAX_VALUE);
		}
		return this.getID();
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
	 * Returns the data sources.
	 * @return the data sources
	 */
	public List<AbstractDataSource> getDataSources() {
		if (dataSources==null) {
			dataSources = new ArrayList<>();
		}
		return dataSources;
	}
	/**
	 * Sets the data sources.
	 * @param dataSources the new data sources
	 */
	public void setDataSources(List<AbstractDataSource> dataSources) {
		this.dataSources = dataSources;
	}
	
	
	/**
	 * Has to save the current data workbook.
	 * @return true, if successful
	 */
	public abstract boolean save();
	
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
	
}
