package de.enflexit.df.core.processing.transformation.graph.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


/**
 * This converter can be used store an ArrayList of Strings as a single CSV String.  
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
@Converter
public class StringsListConverter implements AttributeConverter<List<String>, String> {
	
	private static final String SEPARATOR = ";";
	
	/* (non-Javadoc)
	 * @see jakarta.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public String convertToDatabaseColumn(List<String> stringsList) {
		return stringsList==null ? null : stringsList.stream().collect(Collectors.joining(SEPARATOR));
	}

	/* (non-Javadoc)
	 * @see jakarta.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		if (dbData==null || dbData.isBlank()) return new ArrayList<String>();
		String[] uuidsArray = dbData.split(SEPARATOR);
		return new ArrayList<String>(Arrays.asList(uuidsArray));
	}

}
