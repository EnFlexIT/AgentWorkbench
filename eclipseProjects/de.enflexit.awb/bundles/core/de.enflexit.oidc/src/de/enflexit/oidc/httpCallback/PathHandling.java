package de.enflexit.oidc.httpCallback;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * The Class PathHandling provides the necessary foundation for paths and directories that are related 
 * to the Agent.Workbench logging system.<br>
 * Corresponding to the {@link ExecutionEnvironment} - that is an IDE or a product execution-  it provides:
 * <ul> 
 * 	<li>an application base path {@link #getApplicationBasePath(Class)}</li>
 * 	<li>an properties directory that is always <b>{basePath}/properties/</b> @see #</li>
 *  <li>depending on the operating system, the path for logging files</li>
 * </ul>
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PathHandling {

	public static final String SUB_PATH_PROPERTIES = "resources";
	
	/**
	 * Returns the location of the specified class (may be a *.jar file or a directory).
	 *
	 * @param clazz the class belonging to the bundle to resolve a corresponding application base path.
	 * @return the bundle file
	 */
	private static File getClassLocation(Class<?> clazz) {
		
		if (clazz==null) clazz = PathHandling.class;
		
		File thisFile = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
		if (thisFile.getAbsolutePath().contains("%20")==true) {
			try {
				thisFile = new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			} catch (URISyntaxException uriEx) {
				uriEx.printStackTrace();
			}
		}
		return thisFile;
	}
	/**
	 * Returns an application base path by determine the location of the specified class. 
	 * The class may be a located within a bundle (for *.jar-files) or in a directory (in a development environment).
	 *
	 * @param clazz the class belonging to the bundle to resolve a corresponding application base path.
	 * If the specified class is <code>null</code>, the current class PathHandling will be used
	 * @return the application base path
	 */
	static Path getApplicationBasePath(Class<?> clazz) {
		
		boolean debug = false;
		String baseDir = null;

		try {
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
				// --- Set base directory -------------------------------------
				int cutAt = pathFound.indexOf("plugins" + File.separator);
				baseDir = pathFound.substring(0, cutAt);
				
			} else {
				// --- IDE environment ----------------------------------------
				baseDir = thisFile + File.separator;
			}
			
			// --- Convert path to a canonical one ----------------------------
			File baseDirFile = new File(baseDir);
			baseDir = baseDirFile.getCanonicalPath() + File.separator;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// --------------------------------------------------------------------
		if (debug==true) {
			Bundle bundle = FrameworkUtil.getBundle(clazz);
			System.err.println("[" + PathHandling.class.getSimpleName() + "]  Base path for class out of bundle '" + bundle.getSymbolicName() + "' is '" + baseDir + "'");
		}
		return Path.of(baseDir);
	}
	
	/**
	 * This method can be invoked in order to get the path to the property folder 'properties\'.
	 * @param absolute set true if you want to get the full path to this 
	 * @return the path reference to the property folder
	 */
	static Path getPropertiesPath(boolean absolute){
		Path propPath = null;
		if (absolute==true) { 
			propPath =  PathHandling.getApplicationBasePath(null).resolve(SUB_PATH_PROPERTIES);
		} else {
			propPath = Path.of(SUB_PATH_PROPERTIES);	
		}
		// --- Create directory, if not already there -----
		PathHandling.createDirectoryIfNotExists(propPath);

		return propPath;
	}
	
	/**
	 * Creates the directory if not already there and thus required.
	 * @param path the path
	 */
	private static void createDirectoryIfNotExists(Path path) {
		File checkFile = path.toFile();
		if (checkFile.exists()==false) {
			checkFile.mkdir();
		}
	}
	
}
