package de.enflexit.common.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class Properties serves as base class to store individual properties 
 * within superior classes. For this, it also provides the required XML tags.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Properties", propOrder = {
	"propertyMap"
})
public class Properties implements Serializable {

	private static final long serialVersionUID = -6532853465062835481L;

	public enum PropertyType {
		String,
		Booelan,
		Integer,
		Long,
		Float,
		Double
	}
	
	@XmlElement(name="Properties")
	private TreeMap<String, PropertyValue> propertyMap;
	
	
	/**
	 * Returns the list of identifier in an alphabetic order.
	 * @return the identifier list
	 */
	public List<String> getIdentifierList() {
		List<String> idList = new ArrayList<>(this.getPropertyMap().keySet());
		Collections.sort(idList);
		return idList;
	}
	
	/**
	 * Returns the property map.
	 * @return the property map
	 */
	private TreeMap<String, PropertyValue> getPropertyMap() {
		if (propertyMap==null) {
			propertyMap = new TreeMap<>();
		}
		return propertyMap;
	}
	/**
	 * Puts the specified value.
	 *
	 * @param identifier the identifier
	 * @param newValue the new value
	 * @return the previous PropertyValue instance
	 */
	public PropertyValue setValue(String identifier, Object newValue) {
		if (identifier==null) return null;
		if (newValue==null) {
			return this.getPropertyMap().put(identifier, null);
		}
		return this.getPropertyMap().put(identifier, new PropertyValue(newValue));
	}
	/**
	 * Removes the specified property.
	 *
	 * @param identifier the identifier
	 * @return the property value
	 */
	public PropertyValue remove(String identifier) {
		return this.getPropertyMap().remove(identifier);
	}
	/**
	 * Return the specified property value.
	 *
	 * @param identifier the identifier
	 * @return the property value or <code>null</code>
	 */
	public PropertyValue getPropertyValue(String identifier) {
		return this.getPropertyMap().get(identifier);
	}
	
	
	
	public void setStringValue(String identifier, String value) {
		this.setValue(identifier, value);
	}
	public void setBooleanValue(String identifier, boolean value) {
		this.setValue(identifier, value);
	}
	public void setIntegerValue(String identifier, int value) {
		this.setValue(identifier, value);
	}
	public void setLongValue(String identifier, long value) {
		this.setValue(identifier, value);
	}
	public void setFloatValue(String identifier, float value) {
		this.setValue(identifier, value);
	}
	public void setDoubleValue(String identifier, double value) {
		this.setValue(identifier, value);
	}
	
	
	public String getStringValue(String identifier) {
		PropertyValue pv = this.getPropertyValue(identifier);
		return pv==null ? null : pv.getStringValue();
	}
	public Boolean getBooleanValue(String identifier) {
		PropertyValue pv = this.getPropertyValue(identifier);
		return pv==null ? null : pv.getBooleanValue();
	}
	public Integer getIntegerValue(String identifier) {
		PropertyValue pv = this.getPropertyValue(identifier);
		return pv==null ? null : pv.getIntegerValue();
	}
	public Long getLongValue(String identifier) {
		PropertyValue pv = this.getPropertyValue(identifier);
		return pv==null ? null : pv.getLongValue();
	}
	public Float getFloatValue(String identifier) {
		PropertyValue pv = this.getPropertyValue(identifier);
		return pv==null ? null : pv.getFloatValue();
	}
	public Double getDoubleValue(String identifier) {
		PropertyValue pv = this.getPropertyValue(identifier);
		return pv==null ? null : pv.getDoubleValue();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObject) {
		
		if (compObject==null || !(compObject instanceof Properties)) return false;
		if (compObject==this) return true;
		
		Properties propsComp = (Properties) compObject;
		if (propsComp.getPropertyMap().equals(this.getPropertyMap())==false) return false;
		
		return true;
	}

	/**
	 * Fill the current property instance with some test data.
	 */
	public void fillWithTestData() {
		
		this.setStringValue(null, null);
		this.setStringValue("String 1", null);
		this.setStringValue("String 2", "");
		this.setStringValue("String 3", "Test-String");
		
		this.setBooleanValue("Boolean 1", true);
		this.setBooleanValue("Boolean 2", false);
		
		this.setIntegerValue("Integer 1", 0);
		this.setIntegerValue("Integer 2", 4711);
		
		this.setLongValue("Long 1", 0);
		this.setLongValue("Long 2", -100);
		this.setLongValue("Long 2", 987654321);
		
		this.setFloatValue("Float 1", 0.0f);
		this.setFloatValue("Flaot 2", 1.6f);
		
		this.setDoubleValue("Double 1", 0.0);
		this.setDoubleValue("Double 2", -3.6);
	}

}
