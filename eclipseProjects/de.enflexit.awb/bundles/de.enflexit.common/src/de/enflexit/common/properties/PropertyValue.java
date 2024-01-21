package de.enflexit.common.properties;

import java.io.Serializable;
import java.util.Comparator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import de.enflexit.common.properties.Properties.PropertyType;

/**
 * The Class PropertyValue serves as container to save and restore property values.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PropertyValue", propOrder = {
	"valueClass", 
	"valueString"
})
public class PropertyValue implements Serializable {
	
	private static final long serialVersionUID = 7862806542306066372L;
	
	private transient Object value;
	private String valueClass;
	private String valueString;
	
	private transient String errorMessage;
	
	/**
	 * Instantiates a new property value.
	 * ()Default constructor)
	 */
	public PropertyValue() { }
	/**
	 * Instantiates a new property value.
	 * @param value the value
	 */
	public PropertyValue(Object value) {
		this.setValue(value);
		this.setValueClass(value);
		this.setValueString(value.toString());
	}
	
	/**
	 * Returns the property type of the current value.
	 * @return the property type
	 */
	public PropertyType getPropertyType() {
		
		PropertyType pt = null;
		try {
			if (this.getValueClass()!=null && this.getValueClass().isBlank()==false) {
				pt = PropertyType.valueOf(this.getValueClass());
			}
		} catch (Exception ex) {
		}
		return pt;			
	}
	
	/**
	 * Sets the current value.
	 * @param value the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Returns the current value.
	 * @return the value
	 */
	public Object getValue() {
		return this.getValue(true);
	}
	/**
	 * Returns the current value.
	 *
	 * @param isPrintError the indicator to print errors to console or not
	 * @return the value
	 */
	public Object getValue(boolean isPrintError) {
		
		if (value==null && this.getValueString()!=null && this.getValueClass()!=null) {

			String valueString = this.getValueString();
			PropertyType propertyType = this.getPropertyType();
			if (propertyType== null) {
				this.setErrorMessage("Unknown property type '" + this.getValueClass() + "'!", isPrintError);
			} else {
				
				try {
					// --- Parse according to property type -
					switch (propertyType) {
					case String:
						value = valueString;
						break;
					case Boolean:
						value = Boolean.parseBoolean(valueString);
						break;
					case Integer:
						value = Integer.parseInt(valueString);
						break;
					case Long:
						value = Long.parseLong(valueString);
						break;
					case Float:
						value = Float.parseFloat(valueString);
						break;
					case Double:
						value = Double.parseDouble(valueString);
						break;
					}

				} catch (Exception ex) {
					this.setErrorMessage("Error while trying to convert value string '" + valueString + "' to actual value instance of type '" + propertyType + "'", isPrintError);
					if (isPrintError == true) {
						ex.printStackTrace();
					}
				}
			}
		}
		return value;
	}
	/**
	 * Returns the value as string if the instance is of that type.
	 * @return the string value or <code>null</code>;
	 */
	public String getStringValue() {
		Object value = this.getValue();
		if (value instanceof String) {
			return (String) value;
		}
		this.setErrorMessage("Value is not of type String!", true);
		return null;
	}
	/**
	 * Returns the value as boolean if the instance is of that type.
	 * @return the boolean value or <code>null</code>;
	 */
	public Boolean getBooleanValue() {
		Object value = this.getValue();
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		this.setErrorMessage("Value is not of type Boolean!", true);
		return null;
	}
	/**
	 * Returns the value as integer if the instance is of that type.
	 * @return the integer value or <code>null</code>;
	 */
	public Integer getIntegerValue() {
		Object value = this.getValue();
		if (value instanceof Integer) {
			return (Integer) value;
		}
		this.setErrorMessage("Value is not of type Integer!", true);
		return null;
	}
	/**
	 * Returns the value as long if the instance is of that type.
	 * @return the long value or <code>null</code>;
	 */
	public Long getLongValue() {
		Object value = this.getValue();
		if (value instanceof Long) {
			return (Long) value;
		}
		this.setErrorMessage("Value is not of type Long!", true);
		return null;
	}
	/**
	 * Returns the value as float if the instance is of that type.
	 * @return the float value or <code>null</code>;
	 */
	public Float getFloatValue() {
		Object value = this.getValue();
		if (value instanceof Float) {
			return (Float) value;
		}
		this.setErrorMessage("Value is not of type Float!", true);
		return null;
	}
	/**
	 * Returns the value as double if the instance is of that type.
	 * @return the double value or <code>null</code>;
	 */
	public Double getDoubleValue() {
		Object value = this.getValue();
		if (value instanceof Double) {
			return (Double) value;
		}
		this.setErrorMessage("Value is not of type Double!", true);
		return null;
	}
	
	
	/**
	 * Sets the value class.
	 * @param value the new value class
	 */
	private void setValueClass(Object value) {
		
		if (value instanceof String) {
			this.valueClass = PropertyType.String.name();
		} else if (value instanceof Boolean) {
			this.valueClass = PropertyType.Boolean.name();
		} else if (value instanceof Integer) {
			this.valueClass = PropertyType.Integer.name();
		} else if (value instanceof Long) {
			this.valueClass = PropertyType.Long.name();
		} else if (value instanceof Float) {
			this.valueClass = PropertyType.Float.name();
		} else if (value instanceof Double) {
			this.valueClass = PropertyType.Double.name();
		} else {
			this.valueClass = value.getClass().getName();
		}
	}
	/**
	 * Sets the value class.
	 * @param valueClass the new value class
	 */
	public void setValueClass(String valueClass) {
		this.valueClass = valueClass;
	}
	/**
	 * Returns the value class.
	 * @return the value class
	 */
	public String getValueClass() {
		return valueClass;
	}

	
	/**
	 * Sets the value string.
	 * @param valueString the new value string
	 */
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	/**
	 * Returns the value string.
	 * @return the value string
	 */
	public String getValueString() {
		return valueString;
	}
	
	/**
	 * Internally sets the error message.
	 *
	 * @param errorMessage the new error message
	 * @param doPrint the indicator to print the error to the console
	 */
	private void setErrorMessage(String errorMessage, boolean doPrint) {
		this.errorMessage = errorMessage;
		if (doPrint==true) {
			this.printErrMessage();
		}
	}
	/**
	 * Returns the current error message for this instance.
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * Prints the current error message.
	 */
	public void printErrMessage() {
		if (this.getErrorMessage()!=null) {
			System.err.println("[" + this.getClass().getSimpleName() + "] " + this.getErrorMessage());
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObject) {

		if (compObject==null || !(compObject instanceof PropertyValue)) return false;
		if (compObject==this) return true;
		
		PropertyValue propValueComp = (PropertyValue) compObject;
		
		PropertyType pTypeComp = propValueComp.getPropertyType();
		PropertyType pTypeThis = propValueComp.getPropertyType();
		if (pTypeComp==null && pTypeThis==null) {
			// --- Nothing to do here -----------------
		} else if ((pTypeComp==null & pTypeThis!=null) || (pTypeComp!=null & pTypeThis==null)) {
			return false;
		} else {
			if (pTypeComp.equals(pTypeThis)==false) return false;
		}
		
		if (propValueComp.getValue()!=this.getValue()) return false;
		
		return true;
	}
	

	/**
	 * Produces and returns a comparator for a {@link PropertyValue}.
	 *
	 * @param attributeIndex the attribute index (0=value class, 1=value string)
	 * @return the comparator
	 */
	public static Comparator<PropertyValue> getComparator(final int attributeIndex) {
		
		Comparator<PropertyValue> comparator = new Comparator<PropertyValue>() {
			@Override
			public int compare(PropertyValue pv1, PropertyValue pv2) {
				
				// --- Catch null cases -------------------
				if (pv1==null && pv2==null) {
					return 0;
				} else if (pv1!=null && pv2==null) {
					return 1;
				} else if (pv1==null && pv2!=null) {
					return -1;
				} 
				
				// --- Catch regular cases ----------------
				String compStr1 = null;
				String compStr2 = null;
				switch (attributeIndex) {
				case 0:
					compStr1 = pv1.getValueClass();
					compStr2 = pv2.getValueClass();
					break;
				case 1:
					compStr1 = pv1.getValueString();
					compStr2 = pv2.getValueString();
					break;
				default:
					return 0;
				}
				return compStr1.compareTo(compStr2);
			}
		};
		return comparator;
		
	}
	
}