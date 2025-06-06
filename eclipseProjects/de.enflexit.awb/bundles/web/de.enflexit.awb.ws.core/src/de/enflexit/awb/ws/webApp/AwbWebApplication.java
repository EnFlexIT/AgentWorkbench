package de.enflexit.awb.ws.webApp;

import java.util.List;

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
	 * Has to return the list of keys that are public available.
	 * @return the public property keys
	 */
	public List<String> getPublicPropertyKeys();
	
	/**
	 * Will be invoked to enable a check for default web application properties.
	 * @param webAppProperties the web app properties
	 */
	public void doCheckDefaultProperties(Properties webAppProperties);
	
	/**
	 * Will be invoked by the {@link AwbWebApplicationManager} to set the stored / overall properties.
	 * @param properties the new properties
	 */
	public void setProperties(Properties properties);

	/**
	 * Has to return the properties of the {@link AwbWebApplication}.
	 *
	 * @param typeOfProperty the type of property
	 * @return the properties
	 */
	public Properties getProperties(PropertyType typeOfProperty);


	
	/**
	 * Will be called to enable initializing the web application (e.g. to establish database connections and so on).
	 */
	public void initialize();

	
	
}
