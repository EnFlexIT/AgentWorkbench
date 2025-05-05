package de.enflexit.awb.ws.restapi.tools;

import de.enflexit.awb.ws.restapi.gen.model.PropertyEntry;
import de.enflexit.awb.ws.restapi.gen.model.ValueType;
import de.enflexit.common.properties.PropertyValue;

/**
 * The Class PropertyConverter converts between AWB properties and REST properties.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyConverter {

	/**
	 * Converts AWB properties to web application properties as specified in this REST API.
	 *
	 * @param awbProps the AWB properties to convert
	 * @return the de.enflexit.awb.ws.restapi.gen.model. properties
	 */
	public static de.enflexit.awb.ws.restapi.gen.model.Properties toWebRestProperties(de.enflexit.common.properties.Properties awbProps) {
		
		de.enflexit.awb.ws.restapi.gen.model.Properties restProps = new de.enflexit.awb.ws.restapi.gen.model.Properties();

		// --- Check each property ------------------------
		for (String identifier : awbProps.getIdentifierList()) {
			PropertyValue pValue = awbProps.getPropertyValue(identifier);
			
			PropertyEntry pEntry = createPropertyEntry(identifier, pValue) ;
			restProps.addPropertyEntriesItem(pEntry);
		}
		
		return restProps;
	}
	
	/**
	 * Creates the property entry.
	 *
	 * @param identifier the identifier
	 * @param pValue the value
	 * @return the property entry
	 */
	private static PropertyEntry createPropertyEntry(String identifier, PropertyValue pValue) {
		
		PropertyEntry pEntry = new PropertyEntry();
		pEntry.setKey(identifier);
		pEntry.setValue(pValue.getValueString());
		
		switch (pValue.getPropertyType()) {
		case Boolean:
			pEntry.setValueType(ValueType.BOOLEAN);
			break;
		case String:
			pEntry.setValueType(ValueType.STRING);
			break;
		case Double:
			pEntry.setValueType(ValueType.DOUBLE);
			break;
		case Float:
			pEntry.setValueType(ValueType.DOUBLE);
			break;
		case Integer:
			pEntry.setValueType(ValueType.INTEGER);
			break;
		case Long:
			pEntry.setValueType(ValueType.INTEGER);
			break;
		}		
		return pEntry;
	}
	
	
	/**
	 * Converts web application properties to AWB properties.
	 *
	 * @param awbProps the awb props
	 * @return the de.enflexit.common.properties. properties
	 */
	public static de.enflexit.common.properties.Properties toAwbProperties(de.enflexit.awb.ws.restapi.gen.model.Properties awbProps) {
		
		de.enflexit.common.properties.Properties webAppProps = new de.enflexit.common.properties.Properties();
		
		// TODO
		
		return webAppProps;
	}
	
	
}
