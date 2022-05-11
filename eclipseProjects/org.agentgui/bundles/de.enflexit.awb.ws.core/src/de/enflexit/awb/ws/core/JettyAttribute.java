package de.enflexit.awb.ws.core;

import java.io.Serializable;

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
public class JettyAttribute<T> implements Serializable, Comparable<JettyAttribute<T>> {
	
	private static final long serialVersionUID = -4251350036140530195L;

	private String key;
	private T value;
	
	
	/**
	 * Empty default constructor for a JettyAttribute.
	 */
	public JettyAttribute() { }

	
	/**
	 * Instantiates a new jetty parameter with a default value.
	 * @param jettyConstant the jetty constant
	 */
	public JettyAttribute(JettyConstants jettyConstant) {
		this(jettyConstant, jettyConstant.getDefaultValue());
	}
	/**
	 * Instantiates a new jetty parameter value.
	 *
	 * @param jettyConstant the jetty constant
	 * @param value the value
	 */
	public JettyAttribute(JettyConstants jettyConstant, Object value) {
		this.setKey(jettyConstant.getJettyKey());
		this.setValue(value);
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
	 * Returns the {@link JettyConstants} of the current key.
	 * @return the jetty constant
	 */
	public JettyConstants getJettyConstant() {
		return JettyConstants.valueofJettyKey(this.getKey());
	}
	
	/**
	 * Sets the value.
	 * @param value the new value
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		this.value = (T) value;
	}
	/**
	 * Gets the value.
	 * @return the value
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * Returns the possible value or null.
	 * @return the possible value
	 */
	public Object[] getPossibleValues() {
		return this.getJettyConstant().getPossibleValues();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getKey() + " = " + this.getValue().toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null || (! (compObj instanceof JettyAttribute<?>))) return false;
		if (compObj==this) return true;

		JettyAttribute<?> jaComp = (JettyAttribute<?>) compObj;
		if (jaComp.getValue()==null && this.getValue()==null) return true;
		
		JettyConstants jConst = JettyConstants.valueofJettyKey(this.getKey());
		if (jConst.getTypeClass().equals(Boolean.class)) {
			boolean boolComp = (Boolean) jaComp.getValue();
			boolean boolThis = (Boolean) this.getValue();
			if (boolComp!=boolThis) return false;
			
		} else if (jConst.getTypeClass().equals(Integer.class)) {
			Integer intComp = (Integer) jaComp.getValue();
			Integer intThis = (Integer) this.getValue();
			if (intComp.equals(intThis)==false) return false;
			
		} else if (jConst.getTypeClass().equals(String.class)) {
			String stringComp = (String) jaComp.getValue();
			String stringThis = (String) this.getValue();
			if (stringComp.equals(stringThis)==false) return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(JettyAttribute<T> ja2) {
		Integer ja1OrderPos = this.getJettyConstant().getOrderPos();
		Integer ja2OrderPos = ja2.getJettyConstant().getOrderPos();
		return ja1OrderPos.compareTo(ja2OrderPos);
	}
	
}