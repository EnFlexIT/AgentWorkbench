package de.enflexit.common.properties;

public class PropertiesEvent {

	public static final int PROPERTY_ADDED = 0;
	public static final int PROPERTY_REMOVED = 1;
	public static final int PROPERTY_UPDATE = 2;
	
	public enum Action {
		PropertyAdded,
		PropertyRemoved,
		PropertyUpdate,
		PropertiesCleared
	}
	
	private Action action;
	private String identifier;
	private PropertyValue propertyValue;
	
	
	/**
	 * Instantiates a new properties event.
	 *
	 * @param action the action
	 * @param identifier the identifier
	 * @param propertyValue the property value
	 */
	public PropertiesEvent(Action action, String identifier, PropertyValue propertyValue) {
		this.setAction(action);
		this.setIdentifier(identifier);
		this.setPropertyValue(propertyValue);
	}

	/**
	 * Gets the identifier.
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * Sets the identifier.
	 * @param identifier the new identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the property value.
	 * @return the property value
	 */
	public PropertyValue getPropertyValue() {
		return propertyValue;
	}
	/**
	 * Sets the property value.
	 * @param propertyValue the new property value
	 */
	public void setPropertyValue(PropertyValue propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * Gets the action.
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}
	/**
	 * Sets the action.
	 * @param action the new action
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	
}
