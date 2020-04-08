package de.enflexit.common.classLoadService;

import java.util.HashMap;

/**
 * The Class ClassLoadServiceUtilityImplManager provides static access to load classes or to initialize them.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassLoadServiceUtilityImplManager {
	
	private static ClassLoadServiceUtilityImplManager thisInstance;
	private HashMap<Class<? extends BaseClassLoadService>, AbstractClassLoadServiceUtilityImpl<? extends BaseClassLoadService>> clsUtilityImplHashMap;

	
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
	 * Returns the HashMap of AbstractClassLoadServiceUtilityImpl.
	 * @return the HashMap 
	 */
	private HashMap<Class<? extends BaseClassLoadService>, AbstractClassLoadServiceUtilityImpl<? extends BaseClassLoadService>> getClassLoadServiceUtilityImplHashMap() {
		if (clsUtilityImplHashMap==null) {
			clsUtilityImplHashMap = new HashMap<>();
		}
		return clsUtilityImplHashMap;
	}
	/**
	 * Register a AbstractClassLoadServiceUtilityImpl for the specified BaseClassLoadService.
	 *
	 * @param clsClass the class of the extended BaseClassLoadService
	 * @param clsUtilityImpl the actual AbstractClassLoadServiceUtilityImpl for the BaseClassLoadService 
	 */
	public void registerClassLoadServiceUtilityImpl(Class<? extends BaseClassLoadService> clsClass, AbstractClassLoadServiceUtilityImpl<? extends BaseClassLoadService> clsUtilityImpl) {
		getClassLoadServiceUtilityImplHashMap().put(clsClass, clsUtilityImpl);
	}
	/**
	 * Returns the extended and registered {@link AbstractClassLoadServiceUtilityImpl} for the specified extended {@link BaseClassLoadService}.
	 *
	 * @param clsClass the actual BaseClassLoadService as description
	 * @return the class load service utility
	 */
	public AbstractClassLoadServiceUtilityImpl<? extends BaseClassLoadService> getClassLoadServiceUtilityImpl(Class<? extends BaseClassLoadService> clsClass) {
		return getClassLoadServiceUtilityImplHashMap().get(clsClass);
	}
	/**
	 * Removes a AbstractClassLoadServiceUtilityImpl for the specified BaseClassLoadService.
	 *
	 * @param clsClass the class of the extended BaseClassLoadService
	 */
	public void removeClassLoadServiceUtilityImpl(Class<? extends BaseClassLoadService> clsClass) {
		getClassLoadServiceUtilityImplHashMap().remove(clsClass);
	}
	
}
