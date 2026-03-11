package de.enflexit.common.properties.bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.common.ServiceFinder;
import de.enflexit.common.properties.Properties;

/**
 * The Class ApplicationPropertyBus enables to exchange property settings for and from 
 * different parts of the current application area. Regardless, if you want to update 
 * database settings or if you want to change the AWB start options, the settings can
 * be packed within a performative- or topic-driven {@link Properties} instance.
 * 
 * To enable a specific information exchange, one can define and register an individual 
 * {@link PropertyBusService} that is intended to cover the necessary actions to set or get 
 * the context dependent setting information.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * 
 * @see PropertyBusService
 * 
 */
public class ApplicationPropertyBus {

	static Logger LOGGER = LoggerFactory.getLogger(ApplicationPropertyBus.class);
	
	private static ApplicationPropertyBus instance;
	/** Instantiates a new application property bus. */
	private ApplicationPropertyBus() { }
	/**
	 * Returns the single instance of ApplicationPropertyBus.
	 * @return single instance of ApplicationPropertyBus
	 */
	public static ApplicationPropertyBus getInstance() {
		if (instance==null) {
			instance = new ApplicationPropertyBus();
			instance.getPerformativeServieHashMap();
			instance.startPropertyBusServiceTracker();
		}
		return instance;
	}

	
	private PropertyBusServiceTracker pbsTracker;
	private HashMap<String, List<PropertyBusService>> performativeServieHashMap;
	
	
	/**
	 * Starts the local PropertyBusServiceTracker.
	 */
	private void startPropertyBusServiceTracker() {
		if (pbsTracker==null) {
			pbsTracker = new PropertyBusServiceTracker(FrameworkUtil.getBundle(this.getClass()).getBundleContext());
			pbsTracker.open();
		}
	}
	
	/**
	 * Returns the performative service HashMap.
	 * @return the performative HashMap
	 */
	private HashMap<String, List<PropertyBusService>> getPerformativeServieHashMap() {
		if (performativeServieHashMap==null) {
			performativeServieHashMap = new HashMap<>();
			this.updatePerformativeServiceHashMap();
		}
		return performativeServieHashMap;
	}
	/**
	 * Update performative service HashMap.
	 */
	private void updatePerformativeServiceHashMap() {
		
		LOGGER.info("Updateing map of registered PropertyBusServices!");
		
		// --- Get the registered services --------------------------
		List<PropertyBusService> perfServiceList = ServiceFinder.findServices(PropertyBusService.class);
		perfServiceList.forEach(pbs -> this.addPropertyBusService(pbs));
	}
	/**
	 * Adds the specified property PropertyBusService to the known services.
	 * @param perfService the PropertyBusService to add
	 */
	public void addPropertyBusService(PropertyBusService perfService) {
		
		if (perfService==null) return;
		
		String performative = perfService.getPerformativeNotNull();
		List<PropertyBusService> perfServiceListKnown = this.getPerformativeServieHashMap().get(performative);
		if (perfServiceListKnown==null) {
			// --- Create new list and remind service -----------
			perfServiceListKnown = new ArrayList<>();
			perfServiceListKnown.add(perfService);
			this.getPerformativeServieHashMap().put(performative, perfServiceListKnown);
		} else {
			// --- Add service, if not already in list ----------
			if (perfServiceListKnown.contains(perfService)==false) {
				// --- Update the list of services --------------
				perfServiceListKnown.add(perfService);
			}
		}
	}
	/**
	 * Removes the property bus service.
	 * @param perfService the PropertyBusService to remove
	 */
	public void removePropertyBusService(PropertyBusService perfService) {
		
		if (perfService==null) return;
		
		String performative = perfService.getPerformativeNotNull();
		List<PropertyBusService> perfServiceList = this.getPerformativeServieHashMap().get(performative);

		// --- No list, nothing to do ---------------------
		if (perfServiceList==null) return;

		// --- Remove from list ---------------------------
		if (perfServiceList.remove(perfService)==false) return;
		
		// --- Remove performative, if list is empty ------
		if (perfServiceList.size()==0) {
			this.getPerformativeServieHashMap().remove(performative);
		}
		
	}
	
	
	/**
	 * Returns all property bus services that were registered for the specified performative.
	 *
	 * @param performative the performative
	 * @return the property bus services
	 */
	public List<PropertyBusService> getPropertyBusServices(String performative) {
		
		if (performative==null || performative.isBlank()==true) return null;
		
		List<PropertyBusService> perfServiceListKnown = this.getPerformativeServieHashMap().get(performative.trim().toLowerCase());
		if (perfServiceListKnown==null || perfServiceListKnown.size()==0) {
			LOGGER.error("No PropertyBusService was found for the performative '" + performative + "'!");
		} else if (perfServiceListKnown.size()>1) {
			LOGGER.info(perfServiceListKnown.size() + " instead of one PropertyBusService were found for the performative '" + performative + "'!");
		}
		return perfServiceListKnown;
	}
	
	/**
	 * Sets the properties for the specified performative.
	 *
	 * @param performative the performative
	 * @param properties the properties
	 * @return true, if successfully set through the specific 
	 */
	public boolean setProperties(String performative, Properties properties) {
		
		if (performative==null || performative.isBlank()==true) return false;
		
		List<PropertyBusService> perfServiceListKnown = this.getPropertyBusServices(performative);
		if (perfServiceListKnown!=null) {
			boolean success = true;
			for (PropertyBusService pbs : perfServiceListKnown) {
				 success = success & pbs.setProperties(properties);
			}
			return success;
		}
		return false;
	}
	
	/**
	 * Returns the properties for the specified performative.
	 *
	 * @param performative the performative
	 * @return the properties
	 */
	public Properties getProperties(String performative) {
		
		if (performative==null || performative.isBlank()==true) return null;
		
		Properties properties = null;
		List<PropertyBusService> perfServiceListKnown = this.getPropertyBusServices(performative);
		if (perfServiceListKnown!=null) {
			for (PropertyBusService pbs : perfServiceListKnown) {
				properties = pbs.getProperties(properties);
			}
		}
		return properties;
	}
	
}
