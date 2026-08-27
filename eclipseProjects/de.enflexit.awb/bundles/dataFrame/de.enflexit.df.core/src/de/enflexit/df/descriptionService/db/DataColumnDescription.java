package de.enflexit.df.descriptionService.db;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="data_column_description")
public class DataColumnDescription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_data_column", nullable=false)
	private Integer id;
	
	@Column(name="data_source_name")
	private String dataSourceName;
	@Column(name="table_name")
	private String tableName;
	@Column(name="column_name")
	private String columnName;
	
	private String name;
	
	@OneToMany(mappedBy="dataColumn", cascade=CascadeType.ALL)
	private Set<DataColumnAlternativeID> alternativeIDs;
	
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name="data_type")
	private DataType dataType;
	
	private String unit;
	
	@Column(name="min_value")
	private Double minValue;
	@Column(name="max_value")
	private Double maxValue;
	
	
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
	 * Gets the table name.
	 * @return the table name
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * Sets the table name.
	 * @param tableName the new table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * Gets the column name.
	 * @return the column name
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * Sets the column name.
	 * @param columnName the new column name
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
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
	 * Gets the description.
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
	 * Gets the data type.
	 * @return the data type
	 */
	public DataType getDataType() {
		return dataType;
	}
	/**
	 * Sets the data type.
	 * @param dataType the new data type
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * Gets the unit.
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * Sets the unit.
	 * @param unit the new unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	/**
	 * Gets the min value.
	 * @return the min value
	 */
	public Double getMinValue() {
		return minValue;
	}
	/**
	 * Sets the min value.
	 * @param minValue the new min value
	 */
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}
	
	/**
	 * Gets the max value.
	 * @return the max value
	 */
	public Double getMaxValue() {
		return maxValue;
	}
	/**
	 * Sets the max value.
	 * @param maxValue the new max value
	 */
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	
	/**
	 * Gets the alternative IDs.
	 * @return the alternative IDs
	 */
	public Set<DataColumnAlternativeID> getAlternativeIDs() {
		return alternativeIDs;
	}
	/**
	 * Sets the alternative IDs.
	 * @param alternativeIDs the new alternative IDs
	 */
	public void setAlternativeIDs(Set<DataColumnAlternativeID> alternativeIDs) {
		this.alternativeIDs = alternativeIDs;
	}
	
	/**
	 * Adds an alternative identifier to this column description.
	 * @param alternativeIdentifier the alternative identifier
	 */
	public void addAlternativeIdentifier(DataColumnAlternativeID alternativeIdentifier) {
		alternativeIdentifier.setDataColumn(this);
		this.getAlternativeIDs().add(alternativeIdentifier);
	}
}
