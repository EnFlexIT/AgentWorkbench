package de.enflexit.common.classLoadService;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
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
	
	private boolean debug = false;
	private boolean debugDetail = true;
	
	private BundleContext bundleContext;
	
	private HashMap<String, Vector<ClassLoadServiceElement>> clsElementsByServiceInterfaceName;
	private HashMap<String, T> clServicesByServiceAndClassName;
	
	
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
		
		// ----------------------------------------------------------
		// --- Default: Use the already known BaseClassLoadService -- 
		// ----------------------------------------------------------
		String requestKey = className + "@" + serviceBaseInterface.getName();
		T clsFoundDefault = this.getLocalClassLoadService();
		T clsFound = this.getClassLoadServicesByServiceAndClassName().get(requestKey);
		if (clsFound!=null) {
			return clsFound;
		
		} else {
			// --- Check if this is the required service ------------
			if (this.isRequiredClassLoadService(serviceBaseInterface, clsFoundDefault, className)==true) {
				// --- Remind this service for later calls --------------
				this.getClassLoadServicesByServiceAndClassName().put(requestKey, clsFoundDefault);
				return clsFoundDefault;
			}
		}
		
		// ----------------------------------------------------------
		// --- Try to find the required BaseClassLoadService --------
		// ----------------------------------------------------------
		
		boolean isDoDetailCheck = false;
		if (this.debugDetail 
//			&& serviceBaseInterface.getSimpleName().equals("BaseClassLoadService")==true
//			&& className.equals("energy.optionModel.TechnicalSystemState$UsageOfInterfaces")==true 
//			&& this.getClass().getName().equals("de.enflexit.common.classLoadService.BaseClassLoadServiceUtilityImpl")==true
			&& className.equals("hygrid.agent.photovoltaic.orientationBased.SolarPositionCalculation")==true 
			) {
				isDoDetailCheck = true; 
			}
		

		// --- Check all available ClassLoadServices ---------------- 
		Vector<ClassLoadServiceElement> clsElementsKnown = this.getClassLoadServiceVector(serviceBaseInterface);
		int nKnown = clsElementsKnown.size();
		for (ClassLoadServiceElement clsElement : clsElementsKnown) {
			// --- Filter from right filter type --------------------
			if (isDoDetailCheck) {
				System.out.println("Debug now!");
			}
			if (this.isRequiredClassLoadService(serviceBaseInterface, clsElement.getClassLoadServiceImpl(), className)==true) {
				this.getClassLoadServicesByServiceAndClassName().put(requestKey, clsElement.getClassLoadServiceImpl());
				clsFound = clsElement.getClassLoadServiceImpl();
				break;
			}	
		}

		// --- Check for new services if nothing was found ----------
		int nNew = 0;
		if (clsFound==null) {
			Vector<ClassLoadServiceElement> clsElementsNew = this.updateClassLoadServices();
			nNew = clsElementsNew.size();
			for (ClassLoadServiceElement clsElement : clsElementsNew) {
				// --- Filter from right filter type ----------------
				if (this.isRequiredClassLoadService(serviceBaseInterface, clsElement.getClassLoadServiceImpl(), className)==true) {
					this.getClassLoadServicesByServiceAndClassName().put(requestKey, clsElement.getClassLoadServiceImpl());
					clsFound = clsElement.getClassLoadServiceImpl();
					break;
				}	
			}
		}
		
		// --- Use default service, even if it throws exceptions ----
		if (clsFound==null) {
			if (this.debugDetail) System.err.println("No ClassLoadService found [known: " + nKnown + "| new: " + nNew + "] Searched by: " + this.getClass().getName() + ", Service '" + serviceBaseInterface.getSimpleName() + "' for: " + className + "");
			clsFound = clsFoundDefault;
		}
		return clsFound;
	}
	
	/**
	 * Checks if the specified {@link BaseClassLoadService} is the required one for the specified class.
	 *
	 * @param serviceBaseInterface the type of the service, specified by the interface
	 * @param clsToCheck the BaseClassLoadService found
	 * @param className the class name
	 * @return true, if is required class load service
	 */
	private boolean isRequiredClassLoadService(Class<?> serviceBaseInterface, T clsToCheck, String className) {
		if (serviceBaseInterface.isInstance(clsToCheck)==true) {
			try {
				clsToCheck.forName(className);
				return true;
			} catch (ClassNotFoundException | NoClassDefFoundError cnfe) {
				//cnfe.printStackTrace();
			}
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
	private HashMap<String, T> getClassLoadServicesByServiceAndClassName() {
		if (clServicesByServiceAndClassName==null) {
			clServicesByServiceAndClassName = new HashMap<>();
		}
		return clServicesByServiceAndClassName;
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
		if (unique) {
			// --- Unify the know elements --------------------------  
			HashSet<ClassLoadServiceElement> clsElements = new HashSet<>();
			for (Vector<ClassLoadServiceElement> servieVector : this.getClassLoadServiceElementsByServiceInterfaceName().values()) {
				clsElements.addAll(servieVector);
			}
			known = new Vector<>(clsElements);
			
		} else {
			// --- Just take the elements 'as is' -------------------
			for (Vector<ClassLoadServiceElement> servieVector : this.getClassLoadServiceElementsByServiceInterfaceName().values()) {
				known.addAll(servieVector);
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
					ComponentFactory compFactory = (ComponentFactory) this.getBundleContext().getService(serviceReferences[i]);
					String compFactoryName = compFactory.toString();
					
					// --- Create ComponentInstance and the actual implementation -------
					ComponentInstance compInstance = compFactory.newInstance(null);
					if (compInstance.getInstance() instanceof BaseClassLoadService) {
						// --- Get the instance of the Class load service impl ----------
						@SuppressWarnings("unchecked")
						T cls = (T) compInstance.getInstance();
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
							for (String affectedService : affectedServices) {
								// --- Get the vector of services -----------------------
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
				System.err.println("   org.eclipse.equinox.ds   - Start Level=2 - Auto-Start=true");
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
		if (debug) {
			
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
		for (ClassLoadServiceElement clsElement : clsElementVector) {
			System.out.println(clsElement.getComponentFactoryName() + " " + clsElement.getClassLoadServiceImpl());
		}
	}
	
	
	
	/**
	 * The Class ClassLoadServiceElement store the important elements belonging to a class load service.
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
	 */
	private class ClassLoadServiceElement implements Comparable<ClassLoadServiceElement> {
		
		private String componentFactoryName;
		private Bundle bundle;
		private T classLoadServiceImpl;
		private Vector<String> affectedServices;
		
		public ClassLoadServiceElement(String componentFactoryName, Bundle bundle, T classLoadServiceImpl) {
			this.setComponentFactoryName(componentFactoryName);
			this.setBundle(bundle);
			this.setClassLoadServiceImpl(classLoadServiceImpl);
		}
		public String getComponentFactoryName() {
			return componentFactoryName;
		}
		public void setComponentFactoryName(String componentFactoryName) {
			this.componentFactoryName = componentFactoryName;
		}
		
		public Bundle getBundle() {
			return bundle;
		}
		public void setBundle(Bundle bundle) {
			this.bundle = bundle;
		}
		
		public T getClassLoadServiceImpl() {
			return classLoadServiceImpl;
		}
		public void setClassLoadServiceImpl(T classLoadService) {
			this.classLoadServiceImpl = classLoadService;
		}
		
		public Vector<String> getAffectedServices() {
			if (affectedServices==null) {
				affectedServices = this.getAffectedClassLoadServiceInterfaces(this.getClassLoadServiceImpl());
			}
			return affectedServices;
		}
		private Vector<String> getAffectedClassLoadServiceInterfaces(T classLoadService) {
			Vector<String> affectedInterfaces = new Vector<>();
			Class<?>[] interfaceClasses = getSuperInterfaces(classLoadService.getClass().getInterfaces());
			for (int i = 0; i < interfaceClasses.length; i++) {
				affectedInterfaces.add(interfaceClasses[i].getName());
			}
			return affectedInterfaces;
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
			return clsElement.getComponentFactoryName().compareTo(this.getComponentFactoryName());
		}
		
	}
	
}
