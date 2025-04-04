package de.enflexit.common.classLoadService;

import java.lang.reflect.InvocationTargetException;

import jade.content.onto.Ontology;

/**
 * The Class BaseClassLoadServiceUtility provides static access to load classes
 * or to initialize them.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BaseClassLoadServiceUtility {

	/**
	 * Return the current BaseClassLoadServiceUtility.
	 * 
	 * @return the class load service utility
	 */
	private static BaseClassLoadServiceUtilityImpl getBaseClassLoadServiceUtility() {
		ClassLoadServiceUtilityImplManager clsu = ClassLoadServiceUtilityImplManager.getInstance();
		AbstractClassLoadServiceUtilityImpl<?> cls = clsu.getClassLoadServiceUtilityImpl(BaseClassLoadService.class);
		if (cls == null || !(cls instanceof BaseClassLoadService)) {
			cls = new BaseClassLoadServiceUtilityImpl();
			clsu.registerClassLoadServiceUtilityImpl(BaseClassLoadService.class, cls);
		}
		return (BaseClassLoadServiceUtilityImpl) cls;
	}

	/**
	 * Returns the class load service that provides the actual implementations.
	 *
	 * @param className the class name
	 * @return the class load service
	 */
	public static BaseClassLoadService getBaseClassLoadService(String className) {
		return getBaseClassLoadServiceUtility().getClassLoadService(className, BaseClassLoadService.class);
	}

	/**
	 * Checks if the specified string represents a class name.
	 *
	 * @param classNameString the class name string
	 * @return true, if is class name
	 */
	public static boolean isClassAvailable(String classNameString) {
		
		if (classNameString==null || classNameString.isBlank()==true) return false;
		
		try {
			BaseClassLoadServiceUtility.forName(classNameString);
			return true;
		} catch (ClassNotFoundException | NoClassDefFoundError cnfEx) {
			//cnfEx.printStackTrace();
		}
		return false;
	}

	/**
	 * Returns the class for the specified class name or reference.
	 *
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Class<?> forName(String className) throws ClassNotFoundException, NoClassDefFoundError {
		return getBaseClassLoadServiceUtility().forName(className);
	}

	/**
	 * Returns a new instance of the specified class.
	 *
	 * @param className the class name
	 * @return the object
	 * @throws ClassNotFoundException    the class not found exception
	 * @throws InstantiationException    the instantiation exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws NoClassDefFoundError      class not found at runtime
	 * @throws SecurityException         classloader is not the same
	 * @throws NoSuchMethodException     if a matching method is not found.
	 * @throws InvocationTargetException if the underlying constructor throws an
	 *                                   exception
	 * @throws IllegalArgumentException  the illegal argument exceptionn
	 */
	public static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoClassDefFoundError {
		return getBaseClassLoadServiceUtility().newInstance(className);
	}

	/**
	 * Returns the ontology instance from the specified ontology class name.
	 *
	 * @param ontologyClassName the ontology class name
	 * @return the ontology instance
	 * @throws ClassNotFoundException    the class not found exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws SecurityException         the security exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public static Ontology getOntologyInstance(String ontologyClassName) throws ClassNotFoundException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		return getBaseClassLoadServiceUtility().getOntologyInstance(ontologyClassName);
	}

}
