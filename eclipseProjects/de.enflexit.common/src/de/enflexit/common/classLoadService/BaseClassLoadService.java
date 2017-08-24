package de.enflexit.common.classLoadService;


/**
 * The Interface for the BaseClassLoadService within an OSGI-bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface BaseClassLoadService {

	
	/**
	 * Has to return the class for the specified class name or reference.
	 *
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 */
	public Class<?> forName(String className) throws ClassNotFoundException;

	/**
	 * Has to return a new instance of the specified class.
	 *
	 * @param className the class name
	 * @return the object
	 * @throws ClassNotFoundException the class not found exception
	 */
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException;
	
	
	
}
