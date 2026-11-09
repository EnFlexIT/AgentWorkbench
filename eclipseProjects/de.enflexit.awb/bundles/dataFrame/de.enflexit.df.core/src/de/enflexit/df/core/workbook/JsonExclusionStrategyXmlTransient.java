package de.enflexit.df.core.workbook;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import jakarta.xml.bind.annotation.XmlTransient;

/**
 * This {@link ExclusionStrategy} skips fields with an {@link XmlTransient} annotation also for GSON serialization.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JsonExclusionStrategyXmlTransient implements ExclusionStrategy{

	/* (non-Javadoc)
	 * @see com.google.gson.ExclusionStrategy#shouldSkipField(com.google.gson.FieldAttributes)
	 */
	@Override
	public boolean shouldSkipField(FieldAttributes field) {
		return field.getAnnotation(XmlTransient.class) != null;
	}

	/* (non-Javadoc)
	 * @see com.google.gson.ExclusionStrategy#shouldSkipClass(java.lang.Class)
	 */
	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}
	 
}
