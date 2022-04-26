package de.enflexit.awb.ws.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class JettyAttribute.
 * @param <T> the generic type
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JettyAttribute", propOrder = {
    "key",
    "value"
})
public class JettyAttribute<T> {
	
	private String key;
	private T value;
	private transient T[] possibleValues;
	
	
	/**
	 * Empty default constructor for a JettyAttribute.
	 */
	public JettyAttribute() { }
	/**
	 * Instantiates a new jetty parameter value.
	 *
	 * @param parameterKey the parameter key
	 * @param value the value
	 * @param possibleValues the possible values
	 */
	public JettyAttribute(String parameterKey, T value, T[] possibleValues) {
		this.setKey(parameterKey);
		this.setValue(value);
		this.setPossibleValues(possibleValues);
	}
	
	/**
	 * Sets the parameter key.
	 * @param parameterKey the new parameter key
	 */
	public void setKey(String parameterKey) {
		this.key = parameterKey;
	}
	/**
	 * Gets the parameter key.
	 * @return the parameter key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Return the parameter class.
	 * @return the parameter class
	 */
	public Class<?> getType() {
		if (value==null) return null;
		return value.getClass();
	}
	
	/**
	 * Sets the value.
	 * @param value the new value
	 */
	public void setValue(T value) {
		this.value = value;
	}
	/**
	 * Gets the value.
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Sets the possible values.
	 * @param possibleValues the new possible value
	 */
	public void setPossibleValues(T[] possibleValue) {
		this.possibleValues = possibleValue;
	}
	/**
	 * Returns the possible value or null.
	 * @return the possible value
	 */
	public T[] getPossibleValues() {
		return possibleValues;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getValue().toString();
	}
	
}