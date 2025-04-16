package de.enflexit.common.properties;

/**
 * The listener interface for receiving properties events.
 * The class that is interested in processing a properties
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addPropertiesListener<code> method. When
 * the properties event occurs, that object's appropriate
 * method is invoked.
 *
 * @see PropertiesEvent
 */
public interface PropertiesListener {

	/**
	 * On properties event.
	 * @param propertiesEvent the properties event
	 */
	public void onPropertiesEvent(PropertiesEvent propertiesEvent);
	
}
