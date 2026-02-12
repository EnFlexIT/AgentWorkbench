package de.enflexit.db.userManagement;

import java.util.Vector;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;


public class UserManagementHelper {
	private static Bundle localBundle;
	
	private static final String MODEL_CLASSES_PACKAGE = "/de/enflexit/db/userManagement/dataModel"; 
	
	private static Bundle getLocalBundle() {
		if (localBundle==null) {
			localBundle = FrameworkUtil.getBundle(UserManagementHelper.class);
		}
		return localBundle;
	}
	
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
