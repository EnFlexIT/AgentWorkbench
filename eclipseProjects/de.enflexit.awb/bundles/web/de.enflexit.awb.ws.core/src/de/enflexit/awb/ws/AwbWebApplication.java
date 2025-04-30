package de.enflexit.awb.ws;

import de.enflexit.common.properties.Properties;

/**
 * The Interface AwbWebApplication.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbWebApplication {

	public enum PropertyType {
		PrivateProperties,
		PublicProperties,
		AllProperties
	}

	/**
	 * Has to return the application name.
	 * @return the application name
	 */
	public String getApplicationName();

	/**
	 * Has to return the textual application description.
	 * @return the application description
	 */
	public String getApplicationDescription();
	
	
	/**
	 * Will be called to enable initializing the web application (e.g. to establish database connections and so on).
	 */
	public void initialize();
	
	/**
	 * Has to return the properties of the {@link AwbWebApplication}.
	 *
	 * @param typeOfProperty the type of property
	 * @return the properties
	 */
	public Properties getProperties(PropertyType typeOfProperty);

	
	
}
