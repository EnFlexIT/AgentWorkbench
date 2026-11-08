package de.enflexit.df.core.processing.transformation.graph.persistence;

import com.google.gson.Gson;

import de.enflexit.common.properties.Properties;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * This converter can be used to store a {@link Properties} instance as a JSON string.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
@Converter
public class PropertiesConverter implements AttributeConverter<Properties, String>{
	
	private final Gson gson = new Gson();

	/* (non-Javadoc)
	 * @see jakarta.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public String convertToDatabaseColumn(Properties properties) {
		return properties == null ? null : gson.toJson(properties);
	}

	/* (non-Javadoc)
	 * @see jakarta.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public Properties convertToEntityAttribute(String dbData) {
		return dbData == null ? null : gson.fromJson(dbData, Properties.class);
	}

}
