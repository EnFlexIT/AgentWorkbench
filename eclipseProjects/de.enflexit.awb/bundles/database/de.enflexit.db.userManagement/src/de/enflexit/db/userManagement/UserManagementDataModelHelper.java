package de.enflexit.db.userManagement;

import java.util.Vector;

import org.hibernate.cfg.Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

/**
 * This helper class provides static methods integrate the user management data model classes
 * in bigernate-based database bundles. You can either obtain a list of classes to process it 
 * yourself, or add the classes to your hibernate Configuration directly (recommended). 
 *   
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class UserManagementDataModelHelper {
	private static Bundle localBundle;
	
	private static final String MODEL_CLASSES_PACKAGE = "/de/enflexit/db/userManagement/dataModel"; 
	
	/**
	 * Gets the local bundle.
	 * @return the local bundle
	 */
	private static Bundle getLocalBundle() {
		if (localBundle==null) {
			localBundle = FrameworkUtil.getBundle(UserManagementDataModelHelper.class);
		}
		return localBundle;
	}
	
	/**
	 * Gets the data model classes list.
	 * @return the data model classes list
	 */
	public static Vector<Class<?>> getDataModelClassesList() {
		Vector<Class<?>> modelClassesList = new Vector<Class<?>>();
		
		Bundle bundle = getLocalBundle();
		
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		if (bundleWiring!=null) {
			Vector<String> modelClasses = new Vector<>(bundleWiring.listResources(MODEL_CLASSES_PACKAGE, "*.class", BundleWiring.LISTRESOURCES_LOCAL));
			for (int i = 0; i < modelClasses.size(); i++) {
				try {
					
					String modelClassName = modelClasses.get(i).replace("/", ".").replace(".class", "");
					Class<?> modelClass = Class.forName(modelClassName);
					modelClassesList.add(modelClass);
					
				} catch (ClassNotFoundException cnfEx) {
					cnfEx.printStackTrace();
				}
			}
		}
		
		return modelClassesList;
	}
	
	/**
	 * Adds the user management data model classes to the provided hibernate configuration.
	 * @param hibernateConfiguration the hibernate configuration
	 */
	public static void addUserManagementDataModelClasses(Configuration hibernateConfiguration) {
		Vector<Class<?>> userManagementModelClasses = UserManagementDataModelHelper.getDataModelClassesList();
		for (Class<?> umModelClass : userManagementModelClasses) {
			hibernateConfiguration.addAnnotatedClass(umModelClass);
		}
	}
}
