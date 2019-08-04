package de.enflexit.common.classLoadService;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;

import jade.content.onto.Ontology;


/**
 * The Class ClassLoadServiceUtilityImplManager extends the {@link DefaultClassLoadServiceUtility} 
 * and prepares the access to the {@link BaseClassLoadService} depending on the OSGI bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public abstract class AbstractClassLoadServiceUtilityImpl<T extends BaseClassLoadService> implements BaseClassLoadService {

	public static final String SERVICE_REFERENCE_FILTER = "(component.factory=de.enflexit.common.classLoadService)";
	
	private static final String HOW_FOUND_ReminderHashMap = "In local reminder HashMap";
	private static final String HOW_FOUND_LocalServiceImplementation = "Through local service implementation.";
	private static final String HOW_FOUND_AlreadyKnownServices = "Found in already known service implementations.";
	private static final String HOW_FOUND_AfterUpdateOfServices = "Found after update of registered ClassLoadServices.";
	
	private boolean debugFailureDetail = false;
	private boolean isPrintServicesAfterServiceUpdate = false;
	private boolean isPrintServicesRequestResult = false;
	private HashSet<String> printedServicesRequestResult; 
	
	private BundleContext bundleContext;
	
	private HashMap<String, Vector<ClassLoadServiceElement>> clsElementsByServiceInterfaceName;
	private HashMap<String, ClassLoadServiceElement> clServicesElementByServiceAndClassName;
	
	
	/* (non-Javadoc)
	 * @see energy.classLoadService.AbstractClassLoadServiceUtility#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException, NoClassDefFoundError {
		return this.getClassLoadService(className, BaseClassLoadService.class).forName(className);
	}
	
	/* (non-Javadoc)
	 * @see energy.classLoadService.AbstractClassLoadServiceUtility#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return this.getClassLoadService(className, BaseClassLoadService.class).newInstance(className);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#getOntologyInstance(java.lang.String)
	 */
	@Override
	public Ontology getOntologyInstance(String ontologyClassName) throws ClassNotFoundException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		return this.getClassLoadService(ontologyClassName, BaseClassLoadService.class).getOntologyInstance(ontologyClassName);
	}
	
	/**
	 * Has to return the specific local class load service.
	 * @return the local class load service
	 */
	public abstract T getLocalClassLoadService();
	
	
	
	/**
	 * Return the class load service for the specified class.
	 *
	 * @param className the class name
	 * @param serviceBaseInterface the type of the service, specified by the interface 
	 * @return the class load service
	 */
	public T getClassLoadService(String className, Class<? extends BaseClassLoadService> serviceBaseInterface) {
		
		ClassLoadServiceElement clsElementFound = null;
		ClassLoadServiceElement clsElementLocal = null;
		
		String howFound = null;
		int nServicesKnown = 0;
		int nServicesNew = 0;
		
		// ----------------------------------------------------------
		// --- Nested classes are problematic - use hosting class --- 
		// ----------------------------------------------------------
		int dollarPos = className.indexOf("$");
		if (dollarPos!=-1) {
			className = className.substring(0, dollarPos);
		}
		
		
		// ----------------------------------------------------------
		// --- First trial: Use already known ClassLoadServices ----- 
		// ----------------------------------------------------------
		String requestKey = className + "@" + serviceBaseInterface.getName();
		clsElementFound = this.getClassLoadServiceElementsByServiceAndClassName().get(requestKey);
		if (clsElementFound!=null) { 
			if (clsElementFound.getBundle().getState()==Bundle.UNINSTALLED) {
				clsElementFound = null;
			} else {
				howFound = HOW_FOUND_ReminderHashMap;
			}
		}
		
		// ----------------------------------------------------------
		// --- Check if the local service is usable -----------------
		// ----------------------------------------------------------
		if (clsElementFound==null) {
			T clsFoundLocal = this.getLocalClassLoadService();
			if (clsFoundLocal!=null) {
				// --- Create 'local' ClassLoadServiceElement ------- 
				Bundle bundle = this.getBundleContext().getBundle();
				clsElementLocal = new ClassLoadServiceElement("LocalClassLoadService", bundle, clsFoundLocal);
				ClassLoadServiceUsability usability = this.getClassLoadServiceUsability(serviceBaseInterface, clsElementLocal, className, ClassLoadServiceUsability.CanLoadClass);
				if (usability==ClassLoadServiceUsability.CanLoadClass) {
					// --- Remind this service for later calls ------
					this.getClassLoadServiceElementsByServiceAndClassName().put(requestKey, clsElementLocal);
					clsElementFound = clsElementLocal;
					howFound = HOW_FOUND_LocalServiceImplementation;
				}
			}
		}
		
		// ----------------------------------------------------------
		// --- Try to find the required BaseClassLoadService --------
		// ----------------------------------------------------------
		if (clsElementFound==null) {
			// --- Check all available ClassLoadServices ------------
			Vector<ClassLoadServiceElement> clsElementsKnown = this.getClassLoadServiceVector(serviceBaseInterface);
			nServicesKnown = clsElementsKnown.size();
			Vector<ClassLoadServiceElementFound> clsElementsFound =  this.getUsableClassLoadServices(className, serviceBaseInterface, clsElementsKnown);
			if (clsElementsFound.size()>0) {
				clsElementFound = clsElementsFound.get(0).getClassLoadServiceElement();
				this.getClassLoadServiceElementsByServiceAndClassName().put(requestKey, clsElementFound);
				howFound = HOW_FOUND_AlreadyKnownServices;
			}
		}
		
		// --- Check for new services if nothing was found ----------
		if (clsElementFound==null) {
			Vector<ClassLoadServiceElement> clsElementsNew = this.updateClassLoadServices();
			nServicesNew = clsElementsNew.size();
			Vector<ClassLoadServiceElementFound> clsElementsFound =  this.getUsableClassLoadServices(className, serviceBaseInterface, clsElementsNew);
			if (clsElementsFound.size()>0) {
				clsElementFound = clsElementsFound.get(0).getClassLoadServiceElement();
				this.getClassLoadServiceElementsByServiceAndClassName().put(requestKey, clsElementFound);
				howFound = HOW_FOUND_AfterUpdateOfServices;
			}
		}

		// --- Fallback to local ClassLoadServiceElement? -----------
		if (clsElementFound==null) {
			// --- Use local service, even if it throws exceptions --
			if (this.debugFailureDetail) {
				System.err.println("No ClassLoadService found [known: " + nServicesKnown + "| new: " + nServicesNew + "] Searched by: " + this.getClass().getName() + ", Service '" + serviceBaseInterface.getSimpleName() + "' for: " + className + "");
			}
			clsElementFound = clsElementLocal;
		}

		// --- Prepare return value ---------------------------------
		T clsFound = null;
		if (clsElementFound!=null) {
			clsFound = clsElementFound.getClassLoadServiceImpl();
		} else {
			// --- Fallback to local ClassLoadService ---------------
			clsFound = this.getLocalClassLoadService();
		}
		
		// --- Print the service request detail ---------------------
		if (this.isPrintServicesRequestResult==true) {
			this.printServicesRequestResult(className, serviceBaseInterface, clsElementFound, clsFound, howFound);
		}
		return clsFound;
	}

	
	/**
	 * Returns a sorted Vector of usable ClassLoadServices that can answer the class request.
	 *
	 * @param clsElementVector the cls element vector
	 * @return the usable class load services
	 */
	private Vector<ClassLoadServiceElementFound> getUsableClassLoadServices(String className, Class<? extends BaseClassLoadService> serviceBaseInterface, Vector<ClassLoadServiceElement> clsElementVector) {
		
		Vector<ClassLoadServiceElementFound> usableServices = new Vector<>();
		for (int i=0; i < clsElementVector.size(); i++) {
			ClassLoadServiceElement clsElement = clsElementVector.get(i);
			ClassLoadServiceUsability clsUsability = this.getClassLoadServiceUsability(serviceBaseInterface, clsElement, className, ClassLoadServiceUsability.ContainsClass);
			if (clsUsability!=ClassLoadServiceUsability.NotUsable) {
				usableServices.add(new ClassLoadServiceElementFound(clsElement, clsUsability));
				if (clsUsability==ClassLoadServiceUsability.ContainsClass) {
					break;
				}
			}	
		}
		// --- Sort the result vector -----------
		Collections.sort(usableServices);
		return usableServices;
	}
	
	/**
	 * Returns the class load usability.
	 *
	 * @param serviceBaseInterface the type of the service, specified by the interface
	 * @param clsElement the ClassLoadServiceElement element
	 * @param className the class name
	 * @param desiredUsability the desired usability
	 * @return true, if is required class load service
	 */
	private ClassLoadServiceUsability getClassLoadServiceUsability(Class<?> serviceBaseInterface, ClassLoadServiceElement clsElement, String className, ClassLoadServiceUsability desiredUsability) {

		// --- First two checks ---------------------------
		boolean isActiveBundle = clsElement.getBundle().getState()!=Bundle.UNINSTALLED;
		boolean isRightServiceType = clsElement.isRequiredService(serviceBaseInterface);
		if (isActiveBundle==false || isRightServiceType==false) {
			return ClassLoadServiceUsability.NotUsable;	
		}
		
		// --- Check class load ability -------------------
		boolean isLoadingClass = this.isLoadingClass(clsElement.getBundle(), className);
		
		// --- Check class containment -------------------- 
		boolean isClassInBundle = false;
		if (isLoadingClass==true && (desiredUsability==null || desiredUsability==ClassLoadServiceUsability.ContainsClass)) {
			isClassInBundle = this.isClassInBundle(clsElement.getBundle(), className);
		}
		
		// ------------------------------------------------
		// --- Prepare return value -----------------------
		// ------------------------------------------------
		if (isActiveBundle==true && isRightServiceType==true && isLoadingClass==true) {
			if (isClassInBundle==true) {
				return ClassLoadServiceUsability.ContainsClass;
			} else {
				return ClassLoadServiceUsability.CanLoadClass;
			}
		}
		return ClassLoadServiceUsability.NotUsable;
	}
	/**
	 * Checks if the class can be loaded from the bundle.
	 *
	 * @param bundle the bundle
	 * @param className the class name
	 * @return true, if is required bundle
	 */
	private boolean isLoadingClass(Bundle bundle, String className) {

		try {
			Class<?> classInstance = bundle.loadClass(className);
			if (classInstance!=null) return true;
		
		} catch (ClassNotFoundException cnfEx) {
			//cnfEx.printStackTrace();
		}
		return false;
	}
	/**
	 * Checks if the specified class can be found in the bundle.
	 *
	 * @param bundle the bundle
	 * @param className the class name
	 * @return true, if is class in bundle
	 */
	private boolean isClassInBundle(Bundle bundle, String className) {
		
		// --- Try to check the resources of the bundle ---
		String simpleClassName = className.substring(className.lastIndexOf(".")+1);
		String packagePath = className;
		int lastDotIndex = className.lastIndexOf(".");
		if (lastDotIndex!=-1) {
			packagePath = className.substring(0, lastDotIndex);
		}
		packagePath = packagePath.replace(".", "/");
		if (packagePath.startsWith("/")==false) packagePath = "/" + packagePath;
		if (packagePath.endsWith("/")  ==false) packagePath = packagePath + "/";
		
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		Collection<String> resources = bundleWiring.listResources(packagePath, simpleClassName + ".class", BundleWiring.LISTRESOURCES_LOCAL);
		if (resources!=null && resources.size()>0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the HashMap of {@link ClassLoadServiceElement} vectors, where the key is given by the ComponentFactory name.
	 * @return the HashMap of available services
	 */
	private HashMap<String, Vector<ClassLoadServiceElement>> getClassLoadServiceElementsByServiceInterfaceName() {
		if (clsElementsByServiceInterfaceName==null) {
			clsElementsByServiceInterfaceName = new HashMap<>();
		}
		return clsElementsByServiceInterfaceName;
	}
	/**
	 * Returns the already known ClassLoadServices by the service name and class name.
	 * @return the class load services by class name
	 */
	private HashMap<String, ClassLoadServiceElement> getClassLoadServiceElementsByServiceAndClassName() {
		if (clServicesElementByServiceAndClassName==null) {
			clServicesElementByServiceAndClassName = new HashMap<>();
		}
		return clServicesElementByServiceAndClassName;
	}
	
	/**
	 * Returns all BaseClassLoadService's as a vector.
	 *
	 * @param serviceBaseInterface the service base interface
	 * @return the class load service vector
	 */
	private Vector<ClassLoadServiceElement> getClassLoadServiceVector(Class<? extends BaseClassLoadService> serviceBaseInterface) {
		
		Vector<ClassLoadServiceElement> clsElements = new Vector<>();
		
		Class<?>[] affectedinterFaceClasses = this.getSuperInterfaces(new Class[]{serviceBaseInterface});
		for (int i = 0; i < affectedinterFaceClasses.length; i++) {
			Vector<ClassLoadServiceElement> clsElementsPart = this.getClassLoadServiceElementsByServiceInterfaceName().get(affectedinterFaceClasses[i].getName());
			if (clsElementsPart!=null) {
				clsElements.addAll(clsElementsPart);
			}
		}
		return clsElements;
	}
	/**
	 * Return the super interfaces of the specified interfaces.
	 *
	 * @param childInterfaces the child interfaces
	 * @return the super interfaces
	 */
	private Class<?>[] getSuperInterfaces(Class<?>[] childInterfaces) {
    	ArrayList<Class<?>> allInterfaces = new ArrayList<>();
        for (int i = 0; i < childInterfaces.length; i++) {
            allInterfaces.add(childInterfaces[i]);
            allInterfaces.addAll(Arrays.asList(getSuperInterfaces(childInterfaces[i].getInterfaces())));
        }
        return allInterfaces.toArray(new Class<?>[allInterfaces.size()]);
    }	
	
	/**
	 * Returns the uniquely all know class load services.
	 * @return the all know class load services
	 */
	private Vector<ClassLoadServiceElement> getAllKnowClassLoadServices() {
		return this.getAllKnowClassLoadServices(true);
	}
	/**
	 * Returns the all know class load services.
	 * @return the all know class load services
	 */
	private Vector<ClassLoadServiceElement> getAllKnowClassLoadServices(boolean unique) {
		
		Vector<ClassLoadServiceElement> known = new Vector<>();
		Vector<Vector<ClassLoadServiceElement>> sebsiNameVector = new Vector<>(this.getClassLoadServiceElementsByServiceInterfaceName().values());
		if (unique==true) {
			// --- Unify the know elements --------------------------  
			HashSet<ClassLoadServiceElement> clsElements = new HashSet<>();
			for (int i = 0; i < sebsiNameVector.size(); i++) {
				clsElements.addAll(sebsiNameVector.get(i));
			}
			known = new Vector<>(clsElements);
			
		} else {
			// --- Just take the elements 'as is' -------------------
			for (int i = 0; i < sebsiNameVector.size(); i++) {
				known.addAll(sebsiNameVector.get(i));
			}
		}
		return known;
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
	 * Updates the currently registered {@link BaseClassLoadService}'s (Takes also into account that services can be removed dynamically).
	 */
	private Vector<ClassLoadServiceElement> updateClassLoadServices() {
		
		Vector<ClassLoadServiceElement> classLoadServiceElementsAddedNew = new Vector<>();
		Vector<ClassLoadServiceElement> classLoadServiceElementsToDelete = this.getAllKnowClassLoadServices(); 
		
		// ------------------------------------------------------------------------------
		// --- Check the current service references -------------------------------------
		// ------------------------------------------------------------------------------
		try {
			// --- Check for the ServiceReference ---------------------------------------
			ServiceReference<?>[] serviceReferences = this.getBundleContext().getServiceReferences(ComponentFactory.class.getName(), SERVICE_REFERENCE_FILTER);
			if (serviceReferences!=null) {
				for (int i = 0; i < serviceReferences.length; i++) {
					
					// --- Get the component factory and its name -----------------------
					ComponentFactory<?> compFactory = (ComponentFactory<?>) this.getBundleContext().getService(serviceReferences[i]);
					String compFactoryName = compFactory.toString();
					
					// --- Create ComponentInstance and the actual implementation -------
					ComponentInstance<?> compInstance = compFactory.newInstance(null);
					// --- Create a instance of the service -----------------------------
					Object serviceInstance = compInstance.getInstance();
					if (serviceInstance instanceof BaseClassLoadService) {
						// --- Get the instance of the Class load service impl ----------
						@SuppressWarnings("unchecked")
						T cls = (T) serviceInstance;
						// --------------------------------------------------------------
						// --- Create the reminder object with useful information -------
						// --------------------------------------------------------------
						ClassLoadServiceElement clsElement = new ClassLoadServiceElement(compFactoryName, serviceReferences[i].getBundle(), cls);
						if (classLoadServiceElementsToDelete.contains(clsElement)==false) {
							// --- Add to the list of newly added elements --------------
							classLoadServiceElementsAddedNew.add(clsElement);
							// ----------------------------------------------------------
							// --- Store the required information in the reminder -------
							// ----------------------------------------------------------
							Vector<String> affectedServices = clsElement.getAffectedServices();
							for (int j = 0; j < affectedServices.size(); j++) {
								// --- Get the vector of services -----------------------
								String affectedService = affectedServices.get(j);
								Vector<ClassLoadServiceElement> elementVector = this.getClassLoadServiceElementsByServiceInterfaceName().get(affectedService); 
								if (elementVector==null) {
									elementVector = new Vector<>();
									this.getClassLoadServiceElementsByServiceInterfaceName().put(affectedService, elementVector);
								}
								// --- Add the element clsElement the the vector --------
								if (elementVector.contains(clsElement)==false) {
									elementVector.add(clsElement);
								}
							}
							
						} else {
							// --- Remove from the list f delete candidates -----------------
							classLoadServiceElementsToDelete.remove(clsElement);
						}
					}
				}
				
			} else {
				System.err.println("=> BaseClassLoadServiceUtillity: Could not find any service for '" + SERVICE_REFERENCE_FILTER + "'.");
				System.err.println("   Ensure that the following bundles are configured in your start configuration:");
				System.err.println("   org.eclipse.core.runtime - Start Level=1 - Auto-Start=true");
				System.err.println("   org.apache.felix.scr     - Start Level=2 - Auto-Start=true");
			}
			
		} catch (InvalidSyntaxException isEx) {
			isEx.printStackTrace();
		}

		// ------------------------------------------------------------------------------
		// --- Remove services that are not available anymore ---------------------------
		// ------------------------------------------------------------------------------
		for (int i=0; i < classLoadServiceElementsToDelete.size(); i++) {
			// --- Remove the ClassLoadServiceElement from each service reminder --------
			ClassLoadServiceElement clsElement = classLoadServiceElementsToDelete.get(i);
			for (String sevice : clsElement.getAffectedServices()) {
				Vector<ClassLoadServiceElement> clsElementVector = this.getClassLoadServiceElementsByServiceInterfaceName().get(sevice);	
				if (clsElementVector!=null) {
					clsElementVector.remove(clsElement);
				}
			}
		}
		
		// --- Debug print the remaining services ---------------------------------------
		if (isPrintServicesAfterServiceUpdate) {
			
			int noOfServicesUnique = this.getAllKnowClassLoadServices(true).size();
			System.out.println("Number of unique services: " + noOfServicesUnique);
			
			int noOfServicesStored = this.getAllKnowClassLoadServices(true).size();
			System.out.println("Number of services as stored locally: " + noOfServicesStored);
			
			this.printClassLoadServiceElementVector(this.getAllKnowClassLoadServices());
		}
		return classLoadServiceElementsAddedNew;
	}

	/**
	 * Prints the class load service element vector.
	 * @param clsElementVector the cls element vector
	 */
	private void printClassLoadServiceElementVector(Vector<ClassLoadServiceElement> clsElementVector) {
		if (clsElementVector==null || clsElementVector.size()==0) return;
		Collections.sort(clsElementVector);
		for (int i = 0; i < clsElementVector.size(); i++) {
			ClassLoadServiceElement clsElement = clsElementVector.get(i);
			System.out.println(clsElement.getComponentFactoryName() + " " + clsElement.getClassLoadServiceImpl());
		}
	}
	
	/**
	 * Prints the services request result.
	 *
	 * @param clsElementFound the ClassLoadServiceElement found
	 * @param clsFound the ClassLoadService found
	 */
	private void printServicesRequestResult(String className, Class<? extends BaseClassLoadService> serviceBaseInterface, ClassLoadServiceElement clsElementFound, T clsFound, String howFound) {

		if (this.isPrintServicesRequestResult==false) return;
		try {
			if (howFound!=HOW_FOUND_ReminderHashMap && this.getPrintedServicesRequestResult().contains(className)==false) {
				if (clsElementFound==null) {
					System.out.println("[" + this.getClass().getSimpleName() + "] \t => Loaded class '" + className + "' via bundle '" + this.getBundleContext().getBundle().getSymbolicName() + "'. - Found: " + howFound);
				} else {
					System.out.println("[" + this.getClass().getSimpleName() + "] \t => Loaded class '" + className + "' via bundle '" + clsElementFound.getBundle().getSymbolicName() + "'. - Found: " + howFound);
				}
				this.getPrintedServicesRequestResult().add(className);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Return the already printed services request result.
	 * @return the printed services request result
	 */
	private HashSet<String> getPrintedServicesRequestResult() {
		if (printedServicesRequestResult==null) {
			printedServicesRequestResult = new HashSet<>();
		}
		return printedServicesRequestResult;
	}

	
	
	/**
	 * The Class ClassLoadServiceElement stores the important elements belonging to a class load service.
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
	 */
	private class ClassLoadServiceElement implements Comparable<ClassLoadServiceElement> {
		
		private String componentFactoryName;
		private Bundle bundle;
		private T classLoadServiceImpl;
		private Vector<String> affectedServices;
		
		/**
		 * Instantiates a new class load service element.
		 *
		 * @param componentFactoryName the component factory name
		 * @param bundle the bundle
		 * @param classLoadServiceImpl the class load service impl
		 */
		public ClassLoadServiceElement(String componentFactoryName, Bundle bundle, T classLoadServiceImpl) {
			this.setComponentFactoryName(componentFactoryName);
			this.setBundle(bundle);
			this.setClassLoadServiceImpl(classLoadServiceImpl);
		}
		
		/**
		 * Gets the component factory name.
		 * @return the component factory name
		 */
		public String getComponentFactoryName() {
			return componentFactoryName;
		}
		/**
		 * Sets the component factory name.
		 * @param componentFactoryName the new component factory name
		 */
		public void setComponentFactoryName(String componentFactoryName) {
			this.componentFactoryName = componentFactoryName;
		}
		
		/**
		 * Returns the bundle in which the service is located.
		 * @return the bundle
		 */
		public Bundle getBundle() {
			return bundle;
		}
		/**
		 * Sets the bundle in which the service is located.
		 * @param bundle the new bundle
		 */
		public void setBundle(Bundle bundle) {
			this.bundle = bundle;
		}
		
		/**
		 * Returns the actual instance of the class load service implementation.
		 * @return the class load service implementation
		 */
		public T getClassLoadServiceImpl() {
			return classLoadServiceImpl;
		}
		/**
		 * Sets the actual instance of the class load service implementation.
		 * @param classLoadService the new class load service impl
		 */
		public void setClassLoadServiceImpl(T classLoadService) {
			this.classLoadServiceImpl = classLoadService;
		}
		
		/**
		 * Returns the affected services.
		 * @return the affected services
		 */
		public Vector<String> getAffectedServices() {
			if (affectedServices==null) {
				affectedServices = this.getAffectedClassLoadServiceInterfaces();
			}
			return affectedServices;
		}
		/**
		 * Return the affected class load service interfaces.
		 *
		 * @param classLoadService the class load service
		 * @return the affected class load service interfaces
		 */
		private Vector<String> getAffectedClassLoadServiceInterfaces() {
			Vector<String> affectedInterfaces = new Vector<>();
			Class<?>[] interfaceClasses = getSuperInterfaces(this.getClassLoadServiceImpl().getClass().getInterfaces());
			for (int i = 0; i < interfaceClasses.length; i++) {
				affectedInterfaces.add(interfaceClasses[i].getName());
			}
			return affectedInterfaces;
		}
		/**
		 * Checks if this service implementation is the required one for the specified service base interface.
		 * @param serviceBaseInterface the service base interface
		 * @return true, if is required service
		 */
		public boolean isRequiredService(Class<?> serviceBaseInterface) {
			
			if (serviceBaseInterface.isInstance(this.classLoadServiceImpl)==true) return true;

			String serviceBaseInterfaceClassName = serviceBaseInterface.getClass().getName(); 
			for (int i = 0; i < this.getAffectedServices().size(); i++) {
				String affectedService = this.getAffectedServices().get(i);
				if (serviceBaseInterfaceClassName.equals(affectedService)) {
					return true;
				}
			}
			return false;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object compareObject) {
		
			if (compareObject==null) return false;
			if (compareObject==this) return true; 
			
			if (compareObject instanceof AbstractClassLoadServiceUtilityImpl.ClassLoadServiceElement) {
				
				@SuppressWarnings({ "unchecked" })
				AbstractClassLoadServiceUtilityImpl<T>.ClassLoadServiceElement compareElement = (ClassLoadServiceElement) compareObject;
				
				if (compareElement.getComponentFactoryName().equals(this.getComponentFactoryName())==true) {
					if (compareElement.getBundle().getSymbolicName().equals(this.getBundle().getSymbolicName())==true) {
						if (compareElement.getClassLoadServiceImpl().getClass().getName().equals(this.getClassLoadServiceImpl().getClass().getName())==true) {
							return true;
						}
					}
				}
			}
			return false;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(AbstractClassLoadServiceUtilityImpl<T>.ClassLoadServiceElement clsElement) {
			return this.getComponentFactoryName().compareTo(clsElement.getComponentFactoryName());
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			Object[] affServiceArray = this.getAffectedServices().toArray();
			String displayText = this.getBundle().getSymbolicName() + "; " + this.getClassLoadServiceImpl() + "; " + this.getComponentFactoryName() + ";" + affServiceArray.toString();
			return displayText;
		}
	}
	

	/**
	 * The enumeration ClassLoadServiceUsability.
	 */
	private enum ClassLoadServiceUsability {
		ContainsClass,
		CanLoadClass,
		NotUsable
	}
	/**
	 * The Class ClassLoadServiceElementFound specifies how a usable ClassLoadServiceElement is related to a class that is to be loaded by the ClassLoadService.
	 */
	private class ClassLoadServiceElementFound implements Comparable<ClassLoadServiceElementFound> {
		
		private ClassLoadServiceElement clsElement;
		private ClassLoadServiceUsability clsUsabillity;
		
		/**
		 * Instantiates a new description for a found ClassLoadServiceElement.
		 *
		 * @param clsElement the ClassLoadServiceElement
		 * @param clsUsability the cls usability
		 */
		public ClassLoadServiceElementFound(ClassLoadServiceElement clsElement, ClassLoadServiceUsability clsUsability) {
			this.setClassLoadServiceElement(clsElement);
			this.setClassLoadServiceUsabillity(clsUsability);
		}
		
		/**
		 * Sets the class load service element.
		 * @param clsElement the new class load service element
		 */
		public void setClassLoadServiceElement(ClassLoadServiceElement clsElement) {
			this.clsElement = clsElement;
		}
		/**
		 * Gets the class load service element.
		 * @return the class load service element
		 */
		public ClassLoadServiceElement getClassLoadServiceElement() {
			return clsElement;
		}
		
		/**
		 * Sets the class load service usability.
		 * @param clsUsability the new class load service usability
		 */
		public void setClassLoadServiceUsabillity(ClassLoadServiceUsability clsUsability) {
			this.clsUsabillity = clsUsability;
		}
		/**
		 * Gets the class load service usability.
		 * @return the class load service usability
		 */
		public ClassLoadServiceUsability getClassLoadServiceUsability() {
			return clsUsabillity;
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(ClassLoadServiceElementFound clsFoundToCompare) {
			if (this.getClassLoadServiceUsability()!=null && clsFoundToCompare!=null && clsFoundToCompare.getClassLoadServiceUsability()!=null && clsFoundToCompare.getClassLoadServiceUsability()!=this.getClassLoadServiceUsability()) {
				return this.getClassLoadServiceUsability().compareTo(clsFoundToCompare.getClassLoadServiceUsability());
			}
			return 0;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.getClassLoadServiceUsability().name() + " " + this.getClassLoadServiceElement().toString();
		}
	}
	
}
