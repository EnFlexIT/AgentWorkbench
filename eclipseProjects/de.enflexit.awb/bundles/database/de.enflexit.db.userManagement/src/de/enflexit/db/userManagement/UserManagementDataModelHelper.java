package de.enflexit.db.userManagement;

import java.util.Vector;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

/**
 * This helper class provides a static method to obtain a list of relevant data model classes, to include them
 * in the schema generation of hibernate-based database bundles. To use it in a database bundle based on the AWB
 * Tools blueprint, add the following code snippet at the end of the addMappingFileResources()-method of your 
 * HibernateDatabaseConnectionService implementation:     
 *
 *	private void addMappingFileResources(Configuration conf) {
 *
 *		... default code from the blueprint
 *
 *  	Vector<Class<?>> userManagementModelClasses = UserManagementHelper.getDataModelClassesList();
 *  	for (Class<?> umModelClass : userManagementModelClasses) {
 *  		conf.addAnnotatedClass(umModelClass);
 *  	}
 *  }
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
}
