package de.enflexit.common.classLoadService;

import java.util.HashMap;
import java.util.Vector;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;


/**
 * The Class ClassLoadServiceUtilityImplManager extends the {@link DefaultClassLoadServiceUtility} 
 * and prepares the access to the {@link BaseClassLoadService} depending on the OSGI bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public abstract class AbstractClassLoadServiceUtilityImpl<T extends BaseClassLoadService> implements BaseClassLoadService {

	private boolean debug = false;
	
	private BundleContext bundleContext;
	
	private HashMap<String, T> clServicesByComponentFactory;
	private HashMap<String, T> clServicesBySymbolicBundleName;
	
	private HashMap<String, T> clServicesByClassName;
	
	
	
	/**
	 * Has to return the specific service reference filter.
	 * @return the service reference filter
	 */
	public abstract String getServiceReferenceFilter();
	
	/**
	 * Has to return the specific local class load service.
	 * @return the local class load service
	 */
	public abstract T getLocalClassLoadService();
	
	
	/**
	 * Gets the class load service.
	 *
	 * @param className the class name
	 * @return the class load service
	 */
	public T getClassLoadService(String className) {

		// ----------------------------------------------------------
		// --- Default: Use the already known BaseClassLoadService ------ 
		// ----------------------------------------------------------
		T clsFound = this.getClassLoadServicesByClassName().get(className);
		if (clsFound!=null) {
			return clsFound;
		} else {
			// --- As backup, use the local BaseClassLoadService first --
			clsFound = (T) this.getLocalClassLoadService();
		}
		
		// --- Check if this is the required BaseClassLoadService -------
		if (this.isRequiredClassLoadService(clsFound, className)==true) {
			// --- Remind this service for later calls --------------
			this.getClassLoadServicesByClassName().put(className, clsFound);
			return clsFound;
		} 
		
		// ----------------------------------------------------------
		// --- Try to find the required BaseClassLoadService ------------
		// ----------------------------------------------------------
		// --- Update the list of available ClassLoadServices -------
		this.updateClassLoadServices();
		// --- Check all available ClassLoadServices ---------------- 
		for (T cls : this.getClassLoadServiceVector()) {
			if (this.isRequiredClassLoadService(cls, className)==true) {
				this.getClassLoadServicesByClassName().put(className, cls);
				clsFound = cls;
				break;
			}
		}
		return clsFound;
	}
	
	/**
	 * Checks if the specified {@link BaseClassLoadService} is the required one for the specified class.
	 *
	 * @param clsToCheck the BaseClassLoadService found
	 * @param className the class name
	 * @return true, if is required class load service
	 */
	private boolean isRequiredClassLoadService(T clsToCheck, String className) {
		try {
			clsToCheck.forName(className);
			return true;
		} catch (ClassNotFoundException cnfe) {
			//cnfe.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Returns the class load service by the symbolic bundle name.
	 *
	 * @param symbolicBundleName the symbolic bundle name
	 * @return the class load service by bundle
	 */
	public T getClassLoadServiceByBundle(String symbolicBundleName) {
		this.updateClassLoadServices();
		return this.getClassLoadServicesBySymbolicBundleName().get(symbolicBundleName);
	}
	
	/**
	 * Returns the current bundle context.
	 * @return the bundle context
	 */
	private BundleContext getBundleContext() {
		if (bundleContext==null) {
			bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		}
		return bundleContext;
	}
	/**
	 * Returns the HashMap of {@link BaseClassLoadService}'s, where the key is given by the ComponentFactory name.
	 * @return the HashMap of available services
	 */
	private HashMap<String, T> getClassLoadServicesByComponentFactory() {
		if (clServicesByComponentFactory==null) {
			clServicesByComponentFactory = new HashMap<>();
		}
		return clServicesByComponentFactory;
	}
	/**
	 * Returns the component factory name by the symbolic bundle name.
	 * @return the component factory name by the symbolic bundle name
	 */
	private HashMap<String, T> getClassLoadServicesBySymbolicBundleName() {
		if (clServicesBySymbolicBundleName==null) {
			clServicesBySymbolicBundleName = new HashMap<>();
		}
		return clServicesBySymbolicBundleName;
	}
	/**
	 * Returns the already known ClassLoadServices by class name.
	 * @return the class load services by class name
	 */
	private HashMap<String, T> getClassLoadServicesByClassName() {
		if (clServicesByClassName==null) {
			clServicesByClassName = new HashMap<>();
		}
		return clServicesByClassName;
	}
	/**
	 * Returns all BaseClassLoadService's as a vector.
	 * @return the class load service vector
	 */
	private Vector<T> getClassLoadServiceVector() {
		return new Vector<>(this.getClassLoadServicesByComponentFactory().values());
	}
	
	/**
	 * Updates the currently registered {@link BaseClassLoadService}'s (Takes also into account that services can be removed dynamically).
	 */
	private void updateClassLoadServices() {
		
		Vector<String> deleteCandidatesComponentFactoryName = new Vector<>(this.getClassLoadServicesByComponentFactory().keySet()); 
		Vector<String> deleteCandidatesSymbolicBundleName = new Vector<>(this.getClassLoadServicesBySymbolicBundleName().keySet());
		
		// ------------------------------------------------------------------------------
		// --- Check the current service references -------------------------------------
		// ------------------------------------------------------------------------------
		try {
			// --- Check for the ServiceReference --------------------------------------- 
			ServiceReference<?>[] serviceReferences = this.getBundleContext().getServiceReferences(ComponentFactory.class.getName(), this.getServiceReferenceFilter());
			for (int i = 0; i < serviceReferences.length; i++) {
				
				// --- Get the component factory and its name ---------------------------
				ComponentFactory compFactory = (ComponentFactory) this.getBundleContext().getService(serviceReferences[i]);
				String compFactoryName = compFactory.toString();
				String sourceBundleName = serviceReferences[i].getBundle().getSymbolicName();
				
				// --- Check if service is already available ----------------------------
				if (this.getClassLoadServicesByComponentFactory().get(compFactoryName)==null) {
					// --- Create ComponentInstance and the actual implementation -------
					ComponentInstance compInstance = compFactory.newInstance(null);
					if (compInstance.getInstance() instanceof BaseClassLoadService) {
						@SuppressWarnings("unchecked")
						T cls = (T) compInstance.getInstance();
						this.getClassLoadServicesByComponentFactory().put(compFactoryName, cls);
						this.getClassLoadServicesBySymbolicBundleName().put(sourceBundleName, cls);
					}
					
				} else {
					// --- Remove from the list f delete candidates ---------------------
					deleteCandidatesComponentFactoryName.remove(compFactoryName);
					deleteCandidatesSymbolicBundleName.remove(sourceBundleName);
				}
			}
			
		} catch (InvalidSyntaxException isEx) {
			isEx.printStackTrace();
		}

		// ------------------------------------------------------------------------------
		// --- Remove services that are not available anymore ---------------------------
		// ------------------------------------------------------------------------------
		for (int i = 0; i < deleteCandidatesComponentFactoryName.size(); i++) {
			this.getClassLoadServicesByComponentFactory().remove(deleteCandidatesComponentFactoryName.get(i));
		}
		for (int i = 0; i < deleteCandidatesSymbolicBundleName.size(); i++) {
			this.getClassLoadServicesBySymbolicBundleName().remove(deleteCandidatesSymbolicBundleName.get(i));
		}
		
		// --- Debug print the remaining services ---------------------------------------
		if (debug) {
			Vector<String> compFactoryNameVector = new Vector<>(this.getClassLoadServicesByComponentFactory().keySet());
			for (String compFactoryName : compFactoryNameVector) {
				BaseClassLoadService clService = this.getClassLoadServicesByComponentFactory().get(compFactoryName);
				System.out.println(compFactoryName + " " + clService);
			}
		}
	}

	/* (non-Javadoc)
	 * @see energy.classLoadService.AbstractClassLoadServiceUtility#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException {
		return this.getClassLoadService(className).forName(className);
	}
	
	/* (non-Javadoc)
	 * @see energy.classLoadService.AbstractClassLoadServiceUtility#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return this.getClassLoadService(className).newInstance(className);
	}
	
}
