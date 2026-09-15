package de.enflexit.df.descriptionService.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * This class describes possible alternative IDs for a data column. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
@Entity
@Table(name="data_column_alternative_id")
public class DataColumnAlternativeID {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_alternative_identifier", nullable=false)
	private Integer id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="id_data_column")
	private DataColumnDescription dataColumn;
	
	@Column(name="alternative_identifier", nullable=false)
	private String alternativeIdentifier;

	/**
	 * Gets the id.
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * Sets the id.
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the measurement description.
	 * @return the measurement description
	 */
	public DataColumnDescription getDataColumn() {
		return dataColumn;
	}
	/**
	 * Sets the measurement description.
	 * @param measurementDescription the new measurement description
	 */
	public void setDataColumn(DataColumnDescription measurementDescription) {
		this.dataColumn = measurementDescription;
	}

	/**
	 * Gets the alternative identifier.
	 * @return the alternative identifier
	 */
	public String getAlternativeIdentifier() {
		return alternativeIdentifier;
	}
	/**
	 * Sets the alternative identifier.
	 * @param alternativeIdentifier the new alternative identifier
	 */
	public void setAlternativeIdentifier(String alternativeIdentifier) {
		this.alternativeIdentifier = alternativeIdentifier;
	}
	
}
