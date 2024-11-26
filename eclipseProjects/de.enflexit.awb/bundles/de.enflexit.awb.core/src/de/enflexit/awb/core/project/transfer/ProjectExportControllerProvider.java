package de.enflexit.awb.core.project.transfer;

import java.lang.reflect.InvocationTargetException;

import de.enflexit.awb.core.classLoadService.ClassLoadServiceUtility;


/**
 * The Class ProjectExportControllerProvider provides the 
 *
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectExportControllerProvider {
	
	private static String projectExportControllerClassName;

	/**
	 * Sets a specialized project export controller class - must be a subclass of {@link DefaultProjectExportController}!
	 * @param projectExportControllerClassName the project export controller class name
	 */
	public static void setProjectExportControllerClass(String projectExportControllerClassName) {
		ProjectExportControllerProvider.projectExportControllerClassName = projectExportControllerClassName;
	}
	
	/**
	 * Unsets the project export controller class - the default class will be used.
	 */
	public static void unsetProjectExportControllerClass() {
		ProjectExportControllerProvider.projectExportControllerClassName = null;
	}
	
	/**
	 * Gets the project export controller. If a specialized subclass is set, an instance of that class
	 * will be returned, otherwise an instance of {@link DefaultProjectExportController} 
	 *
	 * @return the project export controller
	 */
	public static ProjectExportController getProjectExportController() {
		
		if (projectExportControllerClassName == null) {
			
			// --- Use the default implementation -------------
			return new DefaultProjectExportController();
			
		} else {
			
			// --- Use a specialized subclass ----------------- 
			try {
				return (ProjectExportController) ClassLoadServiceUtility.newInstance(projectExportControllerClassName);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoClassDefFoundError e) {
				System.err.println("Error getting the specialized ProjectExportController implementation - using the default one!");
				e.printStackTrace();
				return new DefaultProjectExportController();
			}
			
		}
		
	}
	
}
