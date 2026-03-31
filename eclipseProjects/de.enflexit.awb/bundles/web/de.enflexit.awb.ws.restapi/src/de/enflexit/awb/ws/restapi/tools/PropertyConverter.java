package de.enflexit.awb.ws.restapi.tools;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.awb.ws.restapi.gen.model.PropertyEntry;
import de.enflexit.awb.ws.restapi.gen.model.ValueType;
import de.enflexit.common.properties.PropertyValue;

/**
 * The Class PropertyConverter converts between AWB properties and REST
 * properties.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyConverter {

	/**
	 * Converts AWB properties to web application properties as specified in this
	 * REST API.
	 *
	 * @param awbProps the AWB properties to convert
	 * @return the de.enflexit.awb.ws.restapi.gen.model. properties
	 */
	public static de.enflexit.awb.ws.restapi.gen.model.Properties toWebRestProperties(de.enflexit.common.properties.Properties awbProps) {

		de.enflexit.awb.ws.restapi.gen.model.Properties restProps = new de.enflexit.awb.ws.restapi.gen.model.Properties();

		if (awbProps != null) {
			// --- Check each property ----------------------------------
			for (String identifier : awbProps.getIdentifierList()) {
				// --- Get property -------------------------------------
				PropertyValue pValue = awbProps.getPropertyValue(identifier);
				// --- Create REST 'PropertyEntry' ----------------------
				PropertyEntry pEntry = createPropertyEntry(identifier, pValue);
				pEntry.setValueOptions(PropertyConverter.toWebValueOptionList(pValue.getValueOptionsString()));
				pEntry.setValueOptionsOnly(pValue.isValueOptionsOnly());

				// --- Add to property listing --------------------------
				restProps.addPropertyEntriesItem(pEntry);
			}
		}
		return restProps;
	}

	/**
	 * Converts the specified string array to a value option list for the REST
	 * 'PropertyEntry'.
	 *
	 * @param optionArray the option array
	 * @return the value option list
	 */
	private static List<String> toWebValueOptionList(String[] optionArray) {

		if (optionArray == null || optionArray.length == 0)
			return null;

		List<String> optionList = new ArrayList<>();
		for (String option : optionArray) {
			optionList.add(option);
		}
		return optionList;
	}

	/**
	 * Creates the property entry.
	 *
	 * @param identifier the identifier
	 * @param pValue     the value
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
	 * @param de.enflexit.awb.ws.restapi.gen.model.Properties
	 * @return the de.enflexit.common.properties.properties
	 */
	public static de.enflexit.common.properties.Properties toAwbProperties(de.enflexit.awb.ws.restapi.gen.model.Properties webAppProps) {

		de.enflexit.common.properties.Properties awbProps = new de.enflexit.common.properties.Properties();
		for (PropertyEntry pEntry : webAppProps.getPropertyEntries()) {
			PropertyConverter.setAwbPropertyValue(pEntry, awbProps);
		}
		return awbProps;
	}
	/**
	 * Sets the awb property value.
	 *
	 * @param pEntry   the webApp PropertyEntry to convert
	 * @param awbProps the awb properties to insert the entry into
	 */
	private static void setAwbPropertyValue(PropertyEntry pEntry, de.enflexit.common.properties.Properties awbProps) {
		
		String propertyKey = pEntry.getKey();
		// --- find the type and set it ----------
		switch (pEntry.getValueType()) {
		case ValueType.BOOLEAN:
			awbProps.setBooleanValue(propertyKey, PropertyConverter.parseBoolean(pEntry.getValue()));
			break;
		case ValueType.STRING:
			awbProps.setStringValue(propertyKey, pEntry.getValue());
			break;
		case ValueType.INTEGER:
			awbProps.setIntegerValue(propertyKey, PropertyConverter.parseInteger(pEntry.getValue()));
			break;
		case ValueType.LONG:
			awbProps.setLongValue(propertyKey, PropertyConverter.parseLong(pEntry.getValue()));
			break;
		case ValueType.DOUBLE:
			awbProps.setDoubleValue(propertyKey, PropertyConverter.parseDouble(pEntry.getValue()));
			break;
		}
	}

	/**
	 * Parses the boolean.
	 *
	 * @param stringValue the string value
	 * @return the boolean
	 */
	private static Boolean parseBoolean(String stringValue) {
		
		if (stringValue == null || stringValue.isBlank() == true) return null;
		
		Boolean value = null;
		try {
			value = Boolean.parseBoolean(stringValue);
		} catch (Exception e) {}
		
		return value;
	}
	/**
	 * Parses the Integer.
	 *
	 * @param stringValue the string value
	 * @return the Integer
	 */
	private static Integer parseInteger(String stringValue) {
		
		if (stringValue == null || stringValue.isBlank() == true) return null;
		
		Integer value = null;
		try {
			value = Integer.parseInt(stringValue);
		} catch (Exception e) {}
		
		return value;
	}
	/**
	 * Parses the long.
	 *
	 * @param stringValue the string value
	 * @return the Long
	 */
	private static Long parseLong(String stringValue) {
		
		if (stringValue == null || stringValue.isBlank() == true) return null;
		
		Long value = null;
		try {
			value = Long.parseLong(stringValue);
		} catch (Exception e) {}
		
		return value;
	}
	/**
	 * Parses the double.
	 *
	 * @param stringValue the string value
	 * @return the double
	 */
	private static Double parseDouble(String stringValue) {
		
		if (stringValue == null || stringValue.isBlank() == true) return null;
		
		Double value = null;
		try {
			value = Double.parseDouble(stringValue);
		} catch (Exception e) {}
		
		return value;
	}

}
