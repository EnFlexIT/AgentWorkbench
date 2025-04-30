package de.enflexit.awb.ws.webApp;

import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertyValue;

/**
 * The Class DefaultWebApplication.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class DefaultWebApplication implements AwbWebApplication {

	private Properties appProperties;

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.webApp.AwbWebApplication#setProperties(de.enflexit.common.properties.Properties)
	 */
	@Override
	public void setProperties(Properties properties) {
		this.appProperties = properties;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.webApp.AwbWebApplication#getProperties(de.enflexit.awb.ws.webApp.AwbWebApplication.PropertyType)
	 */
	@Override
	public Properties getProperties(PropertyType typeOfProperty) {
		
		if (this.appProperties==null) return null;
		
		Properties props = new Properties();
		for (String identifier : this.appProperties.getIdentifierList()) {
			PropertyValue pValue =  this.appProperties.getPropertyValue(identifier);
			
			props.setValue(identifier, pValue);
		}
		return props;
		
	}

}
