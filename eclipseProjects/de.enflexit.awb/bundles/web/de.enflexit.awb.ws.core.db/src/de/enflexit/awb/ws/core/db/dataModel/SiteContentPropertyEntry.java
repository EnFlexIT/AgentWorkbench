package de.enflexit.awb.ws.core.db.dataModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The Class SiteContentProperties.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "site_content_property_entry")
public class SiteContentPropertyEntry {

	public enum PropertyValueType {
		BOOLEAN,
		STRING,
		INTEGER,
		LONG,
		DOUBLE
	}
	
	@Id
	@ManyToOne
	@JoinColumn(name = "id_site_content", referencedColumnName = "id_site_content", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_SITE_CONTENT) REFERENCES SITE_CONTENT(ID_SITE_CONTENT) ON DELETE CASCADE"))
	private SiteContentProperties siteContentProperties;

	@Id
	@Column(name = "property_key")
	private String propertyKey;
	@Column(name = "property_value")
	private String propertyValue;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "property_value_type")
	private PropertyValueType propertyValueType;
	
	
	/**
	 * Returns the propertyKey.
	 * @return the propertyKey
	 */
	public String getPropertyKey() {
		return propertyKey;
	}
	/**
	 * Sets the propertyKey.
	 * @param propertyKey the propertyKey to set
	 */
	public void setPropertyKey(String key) {
		this.propertyKey = key;
	}

	/**
	 * Returns the propertyValue.
	 * @return the propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}
	/**
	 * Sets the propertyValue.
	 * @param propertyValue the property Value to set
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * Returns the propertyValue type.
	 * @return the propertyValueType
	 */
	public PropertyValueType getValueType() {
		return propertyValueType;
	}
	/**
	 * Sets the propertyValue type.
	 * @param propertyValueType the propertyValueType to set
	 */
	public void setValueType(PropertyValueType propertyValueType) {
		this.propertyValueType = propertyValueType;
	}
	
}
