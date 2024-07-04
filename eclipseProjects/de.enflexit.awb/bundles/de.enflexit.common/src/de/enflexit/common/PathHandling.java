package de.enflexit.common;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


/**
 * The Class PathHandling provides the foundation for paths and directories that are related 
 * to Agent.Workbench and their involved bundles.<br>
 * Corresponding to the {@link ExecutionEnvironment} - that is an IDE or a product execution-  it provides:
 * <ul> 
 * 	<li>an application base path {@link #getApplicationBasePath(Class)}</li>
 * 	<li>an properties directory that is always <b>{basePath}/properties/</b> @see #</li>
 *  <li>depending on the operating system, the path for logging files</li>
 * <ul>
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PathHandling {

	public static final String SUB_PATH_PROPERTIES = de.enflexit.logging.PathHandling.SUB_PATH_PROPERTIES;
	
	/**
	 * Returns the location of the specified class (may be a *.jar file or a directory).
	 *
	 * @param clazz the class belonging to the bundle to resolve a corresponding application base path.
	 * @return the bundle file
	 */
	static File getClassLocation(Class<?> clazz) {
		
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
	public static Path getApplicationBasePath(Class<?> clazz) {
		
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
	 * Returns the default path for logging files.
	 * @return the default base path for logging files
	 */
	public static Path getLoggingFilesBasePathDefault() {
		return de.enflexit.logging.PathHandling.getLoggingFilesBasePathDefault() ;
	}
	
	
	/**
	 * This method can be invoked in order to get the path to a directory named 'properties'.
	 * If such directory does not exists, it will be created automatically.
	 *
	 * @param clazz the class belonging to the bundle to resolve a corresponding application base path.
	 * @param absolute set true if you want to get the full path to this directory
	 * @return the path instance to the property folder
	 */
	public static Path getPropertiesPath(Class<?> clazz, boolean absolute) {
		return getPropertiesPath(clazz, absolute, true);
	}
	/**
	 * This method can be invoked in order to get the path to a directory named 'properties'.
	 *
	 * @param clazz the class belonging to the bundle to resolve a corresponding application base path.
	 * @param absolute set true if you want to get the full path to this directory
	 * @param isCreateIfNotExists the indicator to create the directory if not exists
	 * @return the path instance to the property folder
	 */
	public static Path getPropertiesPath(Class<?> clazz, boolean absolute, boolean isCreateIfNotExists){

		Path propPath = null;
		if (absolute==true) { 
			propPath =  PathHandling.getApplicationBasePath(clazz).resolve(SUB_PATH_PROPERTIES);
		} else {
			propPath = Path.of(SUB_PATH_PROPERTIES);	
		}

		if (isCreateIfNotExists==true) {
			PathHandling.createDirectoryIfNotExists(propPath);
		}
		return propPath;
	}
	
	
	/**
	 * Creates the specified directory if it not already exists.
	 *
	 * @param pathString the path string
	 * @return true, if successful
	 */
	public static boolean createDirectoryIfNotExists(String pathString) {
		return createDirectoryIfNotExists(Path.of(pathString));
	}
	/**
	 * Creates the specified directory if it not already exists.
	 *
	 * @param path the path
	 * @return true, if successful
	 */
	public static boolean createDirectoryIfNotExists(Path path) {
		return createDirectoryIfNotExists(path.toFile());
	}
	/**
	 * Creates the specified directory if it not already exists.
	 *
	 * @param file the file
	 * @return true, if successful
	 */
	public static boolean createDirectoryIfNotExists(File file) {
		if (file!=null && file.exists()==false) {
			return file.mkdir();
		}
		return false;
	}
	
	
	
	/**
	 * Will translate a given path name conform to the local system (Win, Mac, Linux).
	 *
	 * @param fileName the file name
	 * @return the path name for local system
	 */
	public static String getPathName4LocalOS(String fileName) {
		
		if (fileName==null) return null;
		
		// --- Put slash into right direction -------------
		String corrected = "";
		for (int i = 0; i < fileName.length(); i++) {

			boolean skipChar = false;
			String	chara = Character.toString(fileName.charAt(i));
			if (chara.equals("\\") || chara.equals("/")) {
				chara = File.separator;
				if (corrected.isEmpty()==false && corrected.substring(corrected.length()-1).equals(File.separator)) {
					skipChar=true;
				}
			}
			if (skipChar==false) corrected = corrected + chara;
		}
		return corrected;
	}
	
	
	/**
	 * Returns a file name suggestion for the specified initial file name proposal.
	 * E.g German umlauts will be replaced as well as doubled spaces and so on.
	 *
	 * @param initialFileNameProposal the initial file name proposal
	 * @return the file name suggestion
	 */
	public static String getFileNameSuggestion(String initialFileNameProposal) {
		
		String regExp = "[A-Z;a-z;\\-;_;0-9]";
		String suggest = initialFileNameProposal.trim();
		
		// --- Preparations ---------------------
		suggest = suggest.replaceAll("( )+", " ");
		suggest = suggest.replace(" ", "_");
		suggest = suggest.replace("ä", "ae");
		suggest = suggest.replace("ö", "oe");
		suggest = suggest.replace("ü", "ue");
		
		// --- Examine all characters -----------
		String suggestNew = "";
		for (int i = 0; i < suggest.length(); i++) {
			String sinlgeChar = "" + suggest.charAt(i);
			if (sinlgeChar.matches(regExp)==true) {
				suggestNew = suggestNew + sinlgeChar;	
			}						
	    }
		suggest = suggestNew;
		suggest = suggest.replaceAll("(_)+", "_");
		suggest = suggest.replaceAll("(-)+", "-");
		return suggest;
	}
	
}
