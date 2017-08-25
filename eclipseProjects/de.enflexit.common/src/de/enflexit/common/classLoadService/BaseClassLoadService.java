package de.enflexit.common.classLoadService;

import java.lang.reflect.InvocationTargetException;

import jade.content.onto.Ontology;

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
	
	/**
	 * Has to return the ontology instance from the specified ontology class name.
	 *
	 * @param ontologyClassName the ontology class name
	 * @return the ontology instance
	 * @throws ClassNotFoundException the class not found exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws SecurityException the security exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public Ontology getOntologyInstance(String ontologyClassName) throws ClassNotFoundException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException;

	
}
