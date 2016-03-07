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
package agentgui.core.ontologies.reflection;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import agentgui.core.application.Application;
import agentgui.core.ontologies.OntologyClassTree;

/**
 * This extended ArrayList<String> is used for the search of classes within a 
 * project by using a specified package name. During the initialisation of 
 * this class, the search will start and fill the list with the classes found.   
 * 
 * @see OntologyClassTree
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ReflectClassFiles extends ArrayList<String> {

	private static final long serialVersionUID = -256361698681180954L;

	private String searchInPackage;
	private String[] searchINPathParts;

	
	/**
	 * Instantiates a new reflect class files that searches in sub packages also.
	 *
	 * @param searchInPackage the package reference in which the search has to be executed
	 */
	public ReflectClassFiles(String searchInPackage) {
		this.searchInPackage = searchInPackage;
		if (this.searchInPackage!=null) {
			this.searchINPathParts = this.searchInPackage.split("\\.");
		}
		this.setClasses();
	}
	
	/**
	 * Initial detection of the available classes by using the 'SearchReference'.
	 */
	private void setClasses() {
		
		String searchPath = null;
		List<File> dirs = new ArrayList<File>();
		
		// --- Try to find the resource of the given Reference ------
		try {
			dirs = this.getClassResources(this.searchInPackage);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// --- Look at the Result of the Resources-Search -----------
		for (int i = 0; i < dirs.size(); i++) {
			
			ArrayList<String> classesFound = null;			
			File directoryFile = dirs.get(i);
			if (directoryFile.exists()==true) {
				// --- Check the specified class resource ------------
				String pathOfFile = directoryFile.getAbsolutePath();
				String reference2JarFile = this.getJarReferenceFromPathOfFile(pathOfFile);
				//System.out.println("=> Reference to JarFile: From " + pathOfFile + " to  " + reference2JarFile);
				if (reference2JarFile!=null) {
					// --- Points to a jar file ---------------------
					if (pathOfFile.startsWith("file:") || pathOfFile.endsWith(".jar")) {
						// --- Path points to a jar-file ------------
						classesFound = this.getJARClasses(directoryFile);
						
					} else {
						// --- Path points to an external IDE-path --
						searchPath = reference2JarFile;
						classesFound = this.getIDEClasses(searchPath, searchPath);
					}
					
				} else {
					// --- Points to a bin folder of an IDE ---------
					searchPath = Application.getGlobalInfo().getPathBaseDirIDE_BIN();
					if (new File(searchPath).exists()==true) {
						// --- The Agent.GUI IDE --------------------
						classesFound = this.getIDEClasses(searchPath, searchPath);
					} else {
						// --- Another IDE bin folder --------------- 
						classesFound = this.getIDEClasses(pathOfFile, pathOfFile);
					}
				}
				
			} else {
				System.err.println("Reflect classes in '" + this.searchInPackage + "': Could not find " + directoryFile);
			}
			
			// --- Add classes found, if any ------------------------ 
			if (classesFound!=null && classesFound.size()!=0){
				this.addAll(classesFound);
			}
		}// --- end for ---
	}

	/**
	 * This Method checks if a given Path-Description
	 * is a description to an external jar-file.
	 *
	 * @param path2Reference the path2 reference
	 * @return the jar reference from path of file
	 */
	private String getJarReferenceFromPathOfFile(String path2Reference) {
		for (String classPathSingle : this.getClassPathExternalJars()) {
			if (path2Reference.contains(classPathSingle)==true) {
				return classPathSingle;
			}
		}
		return null;
	}
	
	/**
	 * This Method returns all jar-Files, which are part of the current ClassPath
	 * @return Returns a Vector<String> with all external jars
	 */
	private Vector<String> getClassPathExternalJars() {
		Vector<String> classPathExternalJars = new Vector<String>();
		String[] JCP_Files = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
		for (int i = 0; i < JCP_Files.length; i++) {
			String classPathEntry = JCP_Files[i];
			if (classPathEntry.endsWith(".jar")==true ){
				classPathExternalJars.add(classPathEntry);
			}
		}	
		return classPathExternalJars;
	}
	
	/**
	 * This method searches for the File-Reference to a given package (e.g. jade.tools.onto)
	 *
	 * @param packageName the package name
	 * @return the class resources
	 * @throws ClassNotFoundException the class not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private List<File> getClassResources(String packageName) throws ClassNotFoundException, IOException {
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null : "ClassLoader not found!";
		
		String path = packageName.replace('.', '/');
		List<File> dirs = new ArrayList<File>();

		Enumeration<URL> resources = classLoader.getResources(path);
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			String fileName = resource.getFile();
			String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
			if (fileNameDecoded.startsWith("file:")) {
				fileNameDecoded = fileNameDecoded.substring(5);
			}
			if (fileNameDecoded.contains(".jar!")) {
				fileNameDecoded = fileNameDecoded.substring(0, fileNameDecoded.indexOf(".jar!")+4);
			}
			dirs.add(new File(fileNameDecoded));
		}
		
		if (dirs.size()==0) {
			// --- In case that no Resources were found ---
			String pathSep = System.getProperty("path.separator");
			String classpath = System.getProperty("java.class.path");

			StringTokenizer st = new StringTokenizer(classpath, pathSep);
			while (st.hasMoreTokens()) {
				dirs.add(new File(st.nextToken()));
			}
		}
		return dirs;
	}
	    
	/**
	 * Reading the Classes from the inside of a jar-file.
	 *
	 * @param jarName the jar name
	 * @return the jar-classes
	 */
	private ArrayList<String> getJARClasses(File jarFileInstance) {
		
		ArrayList<String> classesFound = new ArrayList<String>();
		try {
			
			// --- Get URL for the jar file in order to open it -------------------------
			URL jar = jarFileInstance.toURI().toURL();
			jar = new URL("jar: " + jar.toExternalForm() + "!/");
			// --- Open JarURLConnection ------------------------------------------------
			JarURLConnection conn = (JarURLConnection) jar.openConnection();
			conn.setUseCaches(false);
			
			// --- Run through the list of jar files ------------------------------------
			JarFile jarFile = conn.getJarFile();
			Enumeration<JarEntry> enu = jarFile.entries();
			while (enu.hasMoreElements()) {
				// --- Get the next jar file entry --------------------------------------
				JarEntry jarEntry = enu.nextElement();
				if ((jarEntry.getName().endsWith(".class"))) {
					String className = jarEntry.getName().replaceAll("/", "\\.");
					className = className.substring(0, className.length() - (".class").length());
					// --- Add class to list ? ------------------------------------------
					if (this.searchInPackage==null) {
						classesFound.add(className);	
					} else {
						if (className.startsWith(this.searchInPackage) ) {
							// --- Check if class is in a sub package ---------------
							String checkName = className.replace(this.searchInPackage + ".", "");
							if (checkName.indexOf(".")==-1) {
								classesFound.add(className);
							}
						}		
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classesFound;
	}
	
	/**
	 * Reading all Classes from the IDE area by starting at 'BasePath'.
	 *
	 * @param basePath the base path
	 * @param searchPath the search path
	 * @return the iDE classes
	 */
	private ArrayList<String> getIDEClasses(String basePath, String searchPath) {

		ArrayList<String> fileList = new ArrayList<String>();
		
		int cutBegin = basePath.length();
		int cutEnd   = 0;
		
		File dir = new File(searchPath);
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				// --------------------------------------------------------------------
				// --------------------------------------------------------------------
				if (files[i].isDirectory()==true) {
					// ----------------------------------------------------------------
					// --- Search in sub folder ? -------------------------------------
					if (searchInPackage==null) {
						// ------------------------------------------------------------
						// --- In case that we're not looking for something specific -- 
						fileList.addAll(this.getIDEClasses(basePath, files[i].getAbsolutePath()));
						
					} else {
						// ------------------------------------------------------------
						// --- Only search there, where it is of interest -------------
						boolean moveDeeper = false;
						String searchINPath = null;
						// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						for (int j=0; j<searchINPathParts.length; j++) {
							if (searchINPath == null) {
								searchINPath = searchINPathParts[j];	
							} else {
								searchINPath = searchINPath +  File.separator + searchINPathParts[j];
							}								
							// --- Evaluate current searchINPath ----------------------
							if (files[i].getAbsolutePath().endsWith(searchINPath)) {
								moveDeeper = true;
								break;
							}						
						}
						// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

						if (moveDeeper==true) {
							// --- search one path level deeper -----------------------
							fileList.addAll(this.getIDEClasses(basePath, files[i].getAbsolutePath()) );	
						}
						// ------------------------------------------------------------
						// ------------------------------------------------------------
					}					 
					
				} else {
					// ----------------------------------------------------------------
					// --- Found file -------------------------------------------------
					String currClass = files[i].getAbsolutePath();
					if (currClass.endsWith(".class")) {
						// --- adapt name in currClass --------------------------------
						cutEnd    = currClass.length() - (".class").length();						
						currClass = currClass.substring(cutBegin, cutEnd);
						currClass = currClass.replace('/', '.').replace('\\', '.');						
						while(currClass.startsWith(".")) {
							currClass = currClass.substring(1, currClass.length());
						}

						// --- Add class to list ? ------------------------------------
						if (searchInPackage==null) {
							// --- Just take every class found ------------------------
							fileList.add(currClass);	
							
						} else {
							// --- Is currClass member of the specified package? ------
							if (currClass.startsWith(searchInPackage) ) {
								// --- Found a class of the package -------------------
								fileList.add(currClass);
								
							} else if (currClass.contains(".")==false) {
								// --- Just found a simple class file name ------------
								currClass = searchInPackage + "." + currClass;
								Class<?> clazzFound = null;
								try {
									clazzFound = Class.forName(currClass);
								} catch (ClassNotFoundException cnfe) {
									//cnfe.printStackTrace();
								}
								if (clazzFound!=null) {
									fileList.add(currClass);
								}
								
							}
						}
						// ------------------------------------------------------------
					}
				}
				// --------------------------------------------------------------------
				// --------------------------------------------------------------------
			}		
		}
		return fileList;
	}
	
	/**
	 * Returns the actual classes of the search result.
	 * @return the classes of result
	 */
	public ArrayList<Class<?>> getClassesOfResult() {
		
		ArrayList<Class<?>> classesFound = new ArrayList<>();
		for (String classReference : this) {
			Class<?> classFound = null;
			try {
				classFound = Class.forName(classReference);
			} catch (ClassNotFoundException cnfe) {
				//cnfe.printStackTrace();
			}
			if (classFound!=null) {
				classesFound.add(classFound);
			}
		}
		return classesFound;
	}
	
}
