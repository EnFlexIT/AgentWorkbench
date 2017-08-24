package de.enflexit.common.classLoadService;

import java.util.HashMap;

/**
 * The Class ClassLoadServiceUtilityImplManager provides static access to load classes or to initialize them.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassLoadServiceUtilityImplManager {
	
	private static ClassLoadServiceUtilityImplManager thisInstance;
	private HashMap<Class<? extends BaseClassLoadService>, BaseClassLoadServiceUtilityImpl<? extends BaseClassLoadService>> clsUtilityImplHashMap;

	
	/** Private singleton constructor. */
	private ClassLoadServiceUtilityImplManager() { }
	/**
	 * Return the singleton instance of ClassLoadServiceUtilityImplManager.
	 * @return single instance of ClassLoadServiceUtilityImplManager
	 */
	public static ClassLoadServiceUtilityImplManager getInstance() {
		if (thisInstance==null) {
			thisInstance = new ClassLoadServiceUtilityImplManager();
		}
		return thisInstance;
	}
	
	
	/**
	 * Returns the HashMap of BaseClassLoadServiceUtilityImpl.
	 * @return the HashMap 
	 */
	private HashMap<Class<? extends BaseClassLoadService>, BaseClassLoadServiceUtilityImpl<? extends BaseClassLoadService>> getClassLoadServiceUtilityImplHashMap() {
		if (clsUtilityImplHashMap==null) {
			clsUtilityImplHashMap = new HashMap<>();
		}
		return clsUtilityImplHashMap;
	}
	/**
	 * Register a BaseClassLoadServiceUtilityImpl for the specified BaseClassLoadService.
	 *
	 * @param clsClass the class of the extended BaseClassLoadService
	 * @param clsUtilityImpl the actual BaseClassLoadServiceUtilityImpl for the BaseClassLoadService 
	 */
	public void registerClassLoadServiceUtilityImpl(Class<? extends BaseClassLoadService> clsClass, BaseClassLoadServiceUtilityImpl<? extends BaseClassLoadService> clsUtilityImpl) {
		getClassLoadServiceUtilityImplHashMap().put(clsClass, clsUtilityImpl);
	}
	/**
	 * Returns the extended and registered {@link BaseClassLoadServiceUtilityImpl} for the specified extended {@link BaseClassLoadService}.
	 *
	 * @param clsClass the actual BaseClassLoadService as description
	 * @return the class load service utility
	 */
	public BaseClassLoadServiceUtilityImpl<? extends BaseClassLoadService> getClassLoadServiceUtilityImpl(Class<? extends BaseClassLoadService> clsClass) {
		return getClassLoadServiceUtilityImplHashMap().get(clsClass);
	}
	/**
	 * Removes a BaseClassLoadServiceUtilityImpl for the specified BaseClassLoadService.
	 *
	 * @param clsClass the class of the extended BaseClassLoadService
	 * @param clsUtilityImpl the actual BaseClassLoadServiceUtilityImpl for the BaseClassLoadService 
	 */
	public void removeClassLoadServiceUtilityImpl(Class<? extends BaseClassLoadService> clsClass) {
		getClassLoadServiceUtilityImplHashMap().remove(clsClass);
	}
	
}
