package de.enflexit.common.classLoadService;

/**
 * The Interface ObjectInputStreamObjectFilter can be used as object filter 
 * in an instance of {@link ObjectInputStreamForClassLoadService} and enables
 * to filter or replace specific objects during a deserialization process.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ObjectInputStreamObjectFilter {

	/**
	 * The method may either simply return the specified object, edit the specified object or return <code>null</code>.
	 *
	 * @param checkObject the check object
	 * @return the object
	 */
	public Object filterObject(Object checkObject);
	
}
