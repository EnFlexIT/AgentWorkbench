package de.enflexit.common.dataSources;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class AbstractDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "data_source")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "source_type", discriminatorType = DiscriminatorType.STRING)

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "id",
    "name",
    "description"
})
@XmlSeeAlso({CsvDataSource.class, ExcelDataSource.class, DatabaseDataSource.class})
public abstract class AbstractDataSource implements Serializable {

	private static final long serialVersionUID = -1124708006041779794L;

	public static final String CHANGED_ID = "CHANGED_ID";
	public static final String CHANGED_NAME = "CHANGED_NAME";
	public static final String CHANGED_DESCRIPTION = "CHANGED_DESCRIPTION";
	
	@Id
	@GeneratedValue
	@Column(name="id_data_source", nullable=false)
	private Integer id;
	
	@Column(nullable=false)
	private String name;
	private String description;
	
	
	/**
	 * Returns the id.
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * Gets the name.
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
	
}
