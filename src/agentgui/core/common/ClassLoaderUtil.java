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
package agentgui.core.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This Singleton class was build to enable dynamic resource loading during the runtime of <b>Agent.GUI</b>.<br>
 * It is used in agent projects for adding external jar-files or binary-folders to the ClassPath.
 * 
 * @author Tim Lewen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassLoaderUtil {

	// Log object

	// Parameters
	private static final Class<? extends Object>[] parameters = new Class<?>[] { URL.class };

	/**
	 * This method returns a listing of all available packages, taking 
	 * into account the current settings of the ClassPath 
	 * 
	 * @param resourcesAlreadyThere
	 * @param fullProjectFolderPath
	 * @return Returns a Vector<String> containing the available packages 
	 * @throws Exception
	 */
	public static Vector<String> getPackageNames(Vector<String> resourcesAlreadyThere, String fullProjectFolderPath) throws Exception {

		Vector<String> result = new Vector<String>();		
		Vector<String> resources = new Vector<String>(resourcesAlreadyThere); // make a copy of this instance
		for (String resource : resources) {
			
			resource = ClassLoaderUtil.adjustPathForLoadIn(resource, fullProjectFolderPath);
			File file = new File(resource);
			
			if (file.exists()==false)  {
				// --- Do nothing in here -------
				
			} else if (file.isDirectory()) {
				// --- Search Folder ------------ 
				result.addAll(ClassLoaderUtil.getFolderStructure(file, file));
				
			} else {
				// --- search jar-file --------------------
				URL jar = file.toURI().toURL();
				jar = new URL("jar:" + jar.toExternalForm() + "!/");
				JarURLConnection conn = (JarURLConnection) jar.openConnection();
				conn.setUseCaches(false);
				JarFile jarFile = conn.getJarFile();
				Enumeration<JarEntry> e = jarFile.entries();

				while (e.hasMoreElements()) {
					JarEntry entry = e.nextElement();
					String entryName = entry.getName();
					if (!entryName.contains("META-INF")) {
						// --- package found ? ---    	
						String packpageName = ClassLoaderUtil.getPackageName(entryName);
						if (packpageName!=null && packpageName.length()!=0 && result.contains(packpageName)==false) {
							result.add(packpageName);	
						} 
					}
					
				}// end while
				jarFile.close();

			}
		}
		return result;
	}

	/**
	 * This method converts a String coming from the listing of (JarFile).entries()
	 * to a package String like 'agentgui.core.application' 
	 *  
	 * @param jarEntry
	 * @return Returns a usable String for package name
	 */
	private static String getPackageName(String jarEntry) {

		String packpageName = new String(jarEntry);
		String[] testDepthArray = packpageName.split("/");
		if (testDepthArray.length > 3) {
			return null;
		} else {
			int index = packpageName.lastIndexOf("/");
			if (index == -1) {
				return null;
			} else {
				packpageName = packpageName.substring(0, index);
				packpageName = packpageName.replaceAll("/", ".");
				return packpageName;
			}
		}
	}

	/**
	 * This method evaluates the folder structure recursively (top-down) 
	 * given by the adjustment of the File-variable 'searchIn'. 
	 * 
	 * @param searchIn
	 * @param startSearchAt
	 * @return Returns a list of folders in a Vector<String>.   
	 */
	private static Vector<String> getFolderStructure(File searchIn, File startSearchAt) {
		
		Vector<String> result = new Vector<String>();
		
		if (searchIn.getAbsolutePath().equals(startSearchAt.getAbsolutePath())==false) {
			// --- Single result found, add to list -------
			String startSearchAtPath = startSearchAt.getAbsolutePath();
			String singlePath = searchIn.getAbsolutePath().substring(startSearchAtPath.length()+1);
			singlePath = singlePath.replace(File.separator, ".");
			result.add(singlePath);
		}
		
		for (File file : searchIn.listFiles()) {
    		if (file.isDirectory()) {
    			// --- Deep search ------------------------
    			result.addAll(ClassLoaderUtil.getFolderStructure(file, startSearchAt));
	    	}
		}
		return result;
	}
	
	/**
	 * This method will print the current ClassPath settings
	 */
	public static void printClassPath() {
		System.out.println(System.getProperty("java.class.path"));
	}

	/**
	 * This method will add an external jar file to the current ClassPath settings
	 * @param newJarFile
	 */
	public static void addJarToClassPath(String newJarFile) {
		String cPath = System.getProperty("java.class.path")	+ System.getProperty("path.separator") + newJarFile;
		cPath = cPath.replace(";;", ";");
		System.setProperty("java.class.path", cPath);
	}

	/**
	 * This method will remove a jar file from the current ClassPath 
	 * settings in the JAVA system properties
	 *  
	 * @param jarFile2Remove
	 */
	private static void removeJarFromPropertyClassPath(String jarFile2Remove) {
		String path = System.getProperty("java.class.path");
		if ((path.indexOf(jarFile2Remove) + jarFile2Remove.length()) < path.length()) {
			path = path.replace(jarFile2Remove + System.getProperty("path.separator"),	System.getProperty("path.separator"));
		} else {
			path = path.replace(jarFile2Remove, "");
			path = path.trim();
		}
		path = path.replace(";;", ";");
		System.setProperty("java.class.path", path);
	}

	/**
	 * This method removes a jar-file entry from current ClassPath settings
	 *  
	 * @param jarFile
	 * @throws RuntimeException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static void removeFile(String jarFile) throws RuntimeException,	NoSuchFieldException, IllegalAccessException {
		
		URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

		Class<URLClassLoader> sysclass = URLClassLoader.class;
		java.lang.reflect.Field ucp = sysclass.getDeclaredField("ucp");
		ucp.setAccessible(true);

		Object sun_misc_URLClassPath = ucp.get(sysLoader);
		Class<? extends Object> clazz = sun_misc_URLClassPath.getClass();

		Field path = clazz.getDeclaredField("path");
		path.setAccessible(true);
		
		Field urlsField = clazz.getDeclaredField("urls");
		urlsField.setAccessible(true);
		
		Object tmpObject = new Object();
		tmpObject = path.get(sun_misc_URLClassPath);

		Object stack = urlsField.get(sun_misc_URLClassPath);
		Stack<?> myStack = (Stack<?>) stack;
		ArrayList<?> list = (ArrayList<?>) tmpObject;
		ArrayList<URL> urls = new ArrayList<URL>();
		for (int i = 0; i < list.size(); i++) {
			URL url = (URL) list.get(i);
			String jarFileTest = jarFile.replace("\\", "/");
			jarFileTest = "/" + jarFileTest;
			if (url.getPath().equals(jarFileTest)) {
				urls.add(url);
			}
		}

		for (URL url:urls) {
			list.remove(url);
			myStack.remove(url);
		}

		path.set(sun_misc_URLClassPath, list);
		ucp.set(ClassLoader.getSystemClassLoader(), sun_misc_URLClassPath);

		// --- Remove the file form the properties --------
		ClassLoaderUtil.removeJarFromPropertyClassPath(jarFile);
	}

	/**
	 * Adds a file or a folder to the ClassPath
	 * @param file  File object
	 * @throws IOException IOException
	 */
	public static void addFile(File file) throws IOException {
		addURL(file.toURI().toURL());
	}

	/**
	 * Adds a URL to CLASSPATH
	 * @param url URL
	 * @throws IOException IOException
	 */
	public static void addURL(URL url) throws IOException {

		URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[] { url });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader");
		}
	}
	
	/**
	 * Will adjust a folder specification for an external resource to 
	 * an usable resource link. This Path can be used in order to add
	 * it later on to the class path
	 *  
	 * @param selectedJar
	 * @param fullProjectFolderPath
	 * @return Returns an adjusted/corrected path for a jar resource 
	 */
	public static String adjustPathForLoadIn(String selectedJar, String fullProjectFolderPath) {
		
		String checkPath = selectedJar;
		File checkFile = new File(checkPath);
		if (checkFile.exists()) {
			// --- right path was given -----------------------------
			return checkPath;
			
		} else {
			// --- retry with the addition of the project folder ----
			checkPath = fullProjectFolderPath + checkPath;
			checkPath = checkPath.replace((File.separator + File.separator), File.separator);
			checkFile = new File(checkPath);
			if (checkFile.exists()) {
				return checkPath;
			} 
			
		}
		return selectedJar;
		
	}

	/**
	 * This method can be used in order to get a list of available classes inside of a jar-File
	 * 
	 * @param jarURL
	 * @return Returns a list of Classes available in the specified jar
	 * @throws IOException
	 */
	public static Vector<String> getClassNamesFromJar(URL jarURL) throws IOException {

		Vector<String> result = new Vector<String>();
		jarURL = new URL("jar:" + jarURL.toExternalForm() + "!/");
		JarURLConnection conn = (JarURLConnection) jarURL.openConnection();
		JarFile jarFile = conn.getJarFile();
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {

			JarEntry entry = e.nextElement();

			if (!entry.isDirectory()) {
				String entryName = entry.getName();
				if (entryName.contains(".class")) {
					entryName = entryName.replace("/", ".");
					entryName = entryName.replace(".class", " ");
					entryName = entryName.trim();

					result.add(entryName);
				}
			}

		}
		jarFile.close();
		return result;
	}

}
