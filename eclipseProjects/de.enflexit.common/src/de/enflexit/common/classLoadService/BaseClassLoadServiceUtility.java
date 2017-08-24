package de.enflexit.common.classLoadService;


/**
 * The Class BaseClassLoadServiceUtility provides static access to load classes or to initialize them.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BaseClassLoadServiceUtility {
	
	/**
	 * Return the current BaseClassLoadServiceUtility.
	 * @return the class load service utility
	 */
	private static BaseClassLoadServiceUtilityImpl getBaseClassLoadServiceUtility() {
		ClassLoadServiceUtilityImplManager clsu = ClassLoadServiceUtilityImplManager.getInstance();
		AbstractClassLoadServiceUtilityImpl<?> cls = clsu.getClassLoadServiceUtilityImpl(BaseClassLoadService.class);
		if (cls==null || !(cls instanceof BaseClassLoadService)) {
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
		return getBaseClassLoadServiceUtility().getClassLoadService(className);
	}
	
	/**
	 * Returns the class for the specified class name or reference.
	 *
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Class<?> forName(String className) throws ClassNotFoundException {
		return getBaseClassLoadServiceUtility().forName(className);
	}
	
	/**
	 * Returns a new instance of the specified class.
	 *
	 * @param className the class name
	 * @return the object
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return getBaseClassLoadServiceUtility().newInstance(className);
	}
	
}
