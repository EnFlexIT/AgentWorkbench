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
			// --- Get the property value ---------------------------
			PropertyValue pValue =  this.appProperties.getPropertyValue(identifier);
			
			// --- Check which type of properties is requested ------
			boolean addToRequestedProperties = false;
			switch (typeOfProperty) {
			case AllProperties:
				addToRequestedProperties = true;
				break;
			case PublicProperties:
				addToRequestedProperties = (this.isPublicProperty(identifier)==true);
				break;
			case PrivateProperties:
				addToRequestedProperties = (this.isPublicProperty(identifier)==false);
				break;
			}

			// --- Add to result list if this applies ---------------
			if (addToRequestedProperties==true) {
				props.setValue(identifier, pValue);
			}
		}
		return props;
	}
	/**
	 * Checks if a property is public.
	 *
	 * @param identifier the identifier
	 * @return true, if is public property
	 */
	private boolean isPublicProperty(String identifier) {
		if (this.getPublicPropertyKeys()!=null && this.getPublicPropertyKeys().contains(identifier)==true) {
			return true;
		}
		return false;
	}
	
}
