/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
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
 * The Class ClassLoadServiceUtility extends the {@link DefaultClassLoadServiceUtility} 
 * and prepares the access to the {@link ClassLoadService} depending on the OSGI bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ClassLoadServiceUtilityImpl extends AbstractClassLoadServiceUtility {

	private boolean debug = false;
	
	private ClassLoadService localClassLoadService;
	
	private BundleContext bundleContext;
	
	private HashMap<String, ClassLoadService> clServicesByComponentFactory;
	private HashMap<String, ClassLoadService> clServicesBySymbolicBundleName;
	
	private HashMap<String, ClassLoadService> clServicesByClassName;
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.DefaultClassLoadServiceUtility#getClassLoadService(java.lang.String)
	 */
	@Override
	protected ClassLoadService getClassLoadService(String className) {

		// ----------------------------------------------------------
		// --- Default: Use the already known ClassLoadService ------ 
		// ----------------------------------------------------------
		ClassLoadService clsFound = this.getClassLoadServicesByClassName().get(className);
		if (clsFound!=null) {
			return clsFound;
		} else {
			// --- As backup, use the local ClassLoadService first --
			clsFound = this.getLocalClassLoadService(className);
		}
		
		// --- Check if this is the required ClassLoadService -------
		if (this.isRequiredClassLoadService(clsFound, className)==true) {
			// --- Remind this service for later calls --------------
			this.getClassLoadServicesByClassName().put(className, clsFound);
			return clsFound;
		} 
		
		// ----------------------------------------------------------
		// --- Try to find the required ClassLoadService ------------
		// ----------------------------------------------------------
		// --- Update the list of available ClassLoadServices -------
		this.updateClassLoadServices();
		// --- Check all available ClassLoadServices ---------------- 
		for (ClassLoadService cls : this.getClassLoadServiceVector()) {
			if (this.isRequiredClassLoadService(cls, className)==true) {
				this.getClassLoadServicesByClassName().put(className, cls);
				clsFound = cls;
				break;
			}
		}
		return clsFound;
	}
	/**
	 * Returns the local class load service.
	 *
	 * @param className the class name
	 * @return the local class load service
	 */
	private ClassLoadService getLocalClassLoadService(String className) {
		if (localClassLoadService==null) {
			localClassLoadService = new ClassLoadServiceImpl();
		}
		return localClassLoadService;
	}
	
	/**
	 * Checks if the specified {@link ClassLoadService} is the required one for the specified class.
	 *
	 * @param clsToCheck the ClassLoadService found
	 * @param className the class name
	 * @return true, if is required class load service
	 */
	private boolean isRequiredClassLoadService(ClassLoadService clsToCheck, String className) {
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
	public ClassLoadService getClassLoadServiceByBundle(String symbolicBundleName) {
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
	 * Returns the HashMap of {@link ClassLoadService}'s, where the key is given by the ComponentFactory name.
	 * @return the HashMap of available services
	 */
	private HashMap<String, ClassLoadService> getClassLoadServicesByComponentFactory() {
		if (clServicesByComponentFactory==null) {
			clServicesByComponentFactory = new HashMap<>();
		}
		return clServicesByComponentFactory;
	}
	/**
	 * Returns the component factory name by the symbolic bundle name.
	 * @return the component factory name by the symbolic bundle name
	 */
	private HashMap<String, ClassLoadService> getClassLoadServicesBySymbolicBundleName() {
		if (clServicesBySymbolicBundleName==null) {
			clServicesBySymbolicBundleName = new HashMap<>();
		}
		return clServicesBySymbolicBundleName;
	}
	/**
	 * Returns the already known ClassLoadServices by class name.
	 * @return the class load services by class name
	 */
	private HashMap<String, ClassLoadService> getClassLoadServicesByClassName() {
		if (clServicesByClassName==null) {
			clServicesByClassName = new HashMap<>();
		}
		return clServicesByClassName;
	}
	/**
	 * Returns all ClassLoadService's as a vector.
	 * @return the class load service vector
	 */
	private Vector<ClassLoadService> getClassLoadServiceVector() {
		return new Vector<>(this.getClassLoadServicesByComponentFactory().values());
	}
	
	/**
	 * Updates the currently registered {@link ClassLoadService}'s (Takes also into account that services can be removed dynamically).
	 */
	public void updateClassLoadServices() {
		
		Vector<String> deleteCandidatesComponentFactoryName = new Vector<>(this.getClassLoadServicesByComponentFactory().keySet()); 
		Vector<String> deleteCandidatesSymbolicBundleName = new Vector<>(this.getClassLoadServicesBySymbolicBundleName().keySet());
		
		// ------------------------------------------------------------------------------
		// --- Check the current service references -------------------------------------
		// ------------------------------------------------------------------------------
		try {
			// --- Check for the ServiceReference --------------------------------------- 
			ServiceReference<?>[] serviceReferences = this.getBundleContext().getServiceReferences(ComponentFactory.class.getName(), "(component.factory=org.agentgui.classLoadService)");
			for (int i = 0; i < serviceReferences.length; i++) {
				
				// --- Get the component factory and its name ---------------------------
				ComponentFactory compFactory = (ComponentFactory) this.getBundleContext().getService(serviceReferences[i]);
				String compFactoryName = compFactory.toString();
				String sourceBundleName = serviceReferences[i].getBundle().getSymbolicName();
				
				// --- Check if service is already available ----------------------------
				if (this.getClassLoadServicesByComponentFactory().get(compFactoryName)==null) {
					// --- Create ComponentInstance and the actual implementation -------
					ComponentInstance compInstance = compFactory.newInstance(null);
					if (compInstance.getInstance() instanceof ClassLoadService) {
						ClassLoadService cls = (ClassLoadService) compInstance.getInstance();
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
				ClassLoadService clService = this.getClassLoadServicesByComponentFactory().get(compFactoryName);
				System.out.println(compFactoryName + " " + clService);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException {
		return this.getClassLoadService(className).forName(className);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return this.getClassLoadService(className).newInstance(className);
	}
	
}
