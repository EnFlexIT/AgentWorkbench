package de.enflexit.charts;

import java.util.Vector;

import org.hibernate.cfg.Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;


public class ChartDataModelHelper {
	
	private static final String MODEL_CLASSES_PACKAGE = "/de/enflexit/charts/model"; 
	private static Bundle localBundle;
	
	
	/**
	 * Gets the local bundle.
	 * @return the local bundle
	 */
	private static Bundle getLocalBundle() {
		if (localBundle==null) {
			localBundle = FrameworkUtil.getBundle(ChartDataModelHelper.class);
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
		Vector<Class<?>> userManagementModelClasses = ChartDataModelHelper.getDataModelClassesList();
		for (Class<?> umModelClass : userManagementModelClasses) {
			hibernateConfiguration.addAnnotatedClass(umModelClass);
		}
	}
}