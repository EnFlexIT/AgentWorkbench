package de.enflexit.common;

import java.io.File;

import org.eclipse.core.runtime.Platform;

/**
 * The enumeration ExecutionEnvironment specifies the possible 
 * execution environments of the AWB toolchain that is:
 * <ul>
 * <li>a: a development environment, using an IDE and,</li>
 * <li>b: an execution of a product built</li>
 * </ul>
 * Further, the enumeration provides a static method that return the current {@link ExecutionEnvironment}.<br><br>
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public enum ExecutionEnvironment {
	ExecutedOverIDE,
	ExecutedOverProduct;
	
	private static ExecutionEnvironment executionEnvironment;
	
	/**
	 * Determines and returns the current the execution environment.
	 *
	 * @param clazz the class to be used to resolve a bundle location 
	 * @return the execution environment
	 */
	public static ExecutionEnvironment getExecutionEnvironment(Class<?> clazz) {
		if (executionEnvironment==null) {
			
			// ----------------------------------------------------------------			
			// --- Get initial base directory by checking this class location -
			// ----------------------------------------------------------------
			if (clazz==null) clazz = PathHandling.class;
			File thisFile = PathHandling.getClassLocation(clazz);
			
			// ----------------------------------------------------------------
			// --- Examine the path reference found --------------------------
			// ----------------------------------------------------------------
			String pathFound = thisFile.getAbsolutePath();
			if (pathFound.endsWith(".jar") && pathFound.contains(File.separator + "plugins" + File.separator)) {
				// ------------------------------------------------------------
				// --- Can be product or IDE with Target platform -------------
				// ------------------------------------------------------------
				
				// --- Get the eclipse configuration location -----------------
				String configurationFile = Platform.getConfigurationLocation().getURL().getFile();
				if (configurationFile.contains("plugins/org.eclipse.pde.core/")==true) {
					// --- This is an IDE environment -------------------------
					executionEnvironment = ExecutionEnvironment.ExecutedOverIDE;
				} else {
					// --- Product OSGI environment ---------------------------
					executionEnvironment = ExecutionEnvironment.ExecutedOverProduct;
				}
				
			} else {
				// --- IDE environment ----------------------------------------
				executionEnvironment = ExecutionEnvironment.ExecutedOverIDE;
			}
		
		}
		return executionEnvironment;
	}
	
}
