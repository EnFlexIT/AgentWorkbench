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
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import agentgui.core.application.Application;
import agentgui.core.ontologies.OntologyClassTree;
import agentgui.core.project.Project;

/**
 * This extended ArrayList<String> is used for the search of classes 
 * within a project by using a specified package name.<br> 
 * During the instantiation, the search will start and fill the list with 
 * the found and available classes. It is used in the {@link OntologyClassTree}.  
 * 
 * @see OntologyClassTree
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ReflectClassFiles extends ArrayList<String> {

	private static final long serialVersionUID = -256361698681180954L;

	private Project currProject = null;
	
	private String searchINReference = null;
	private String[] searchINPathParts = null;

	private Vector<String> classPathExternalJars = null;
	
	/**
	 * Constructor of this class.
	 *
	 * @param project the project
	 * @param searchInPackage the search reference
	 */
	public ReflectClassFiles(Project project, String searchInPackage) {

		this.currProject = project;
		
		// --- Die aktuellen externen Ressourcen zusammenstellen -
		this.classPathExternalJars = Application.RunInfo.getClassPathJars();
		this.classPathExternalJars.addAll(currProject.getProjectResources());
		
		// --- Verzeichnis, in dem die Ontologie liegt auslesen ---
		this.searchINReference = searchInPackage;
		if ( !(searchINReference == null) ) {
			searchINPathParts = searchINReference.split("\\.");
		}
		this.setClasses();
	}
	
	/**
	 * Initial detection of the available classes by using the 'SearchReference'.
	 */
	private void setClasses() {
		
		String SearchPath = null;
		List<File> dirs = new ArrayList<File>();
		ArrayList<String> classList = null;
		//System.out.println("=> " + SearchINReference);
		
		// --- Try to find the resource of the given Reference ------
		try {
			dirs = getClassResources(searchINReference);
			//System.out.println( "=> " + dirs.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// --- Look at the Result of the Resources-Search ----------
		if (dirs.size()>0) {
			File directory = dirs.get(0);
			String PathOfFile = directory.toString();
			String reference2JarFile = this.getJarReferenceFromPathOfFile(PathOfFile);
			//System.out.println("=> " + directory.toString() + " <=> " + reference2JarFile); 
			
			if ( reference2JarFile!=null ) {
				if (PathOfFile.startsWith("file:")) {
					// --- Path points to a jar-file ----------------
					//System.out.println("Jar-Result: " + SearchINReference + " => " + reference2JarFile);
					reference2JarFile = PathOfFile.substring(0, PathOfFile.lastIndexOf(reference2JarFile)) + reference2JarFile;
					reference2JarFile = reference2JarFile.replace("file:\\", "");
					classList = getJARClasses( reference2JarFile );
					
				} else {
					// --- Path points to an external IDE-path ------
					SearchPath = reference2JarFile;
					classList = getIDEClasses( SearchPath, SearchPath );
				}
				
			} else {
				// --- Points to the Agent.GUI IDE-Environment ------
				SearchPath = Application.RunInfo.PathBaseDirIDE_BIN();
				classList = getIDEClasses( SearchPath, SearchPath );
			}
			
		}
		if(classList!=null){
			this.addAll( classList );
		}
	}

	/**
	 * This Method checks if a given Path-Description
	 * is a description to an external jar-file.
	 *
	 * @param path2Reference the path2 reference
	 * @return the jar reference from path of file
	 */
	private String getJarReferenceFromPathOfFile( String path2Reference ) {
		
		for (String classPathSingle : classPathExternalJars) {
			if (path2Reference.contains(classPathSingle)==true) {
				return classPathSingle;
			}
		}
		return null;
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
		
		String path = packageName.replace('.', '/');
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			String fileName = resource.getFile();
			String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
			dirs.add(new File(fileNameDecoded));
		}
		return dirs;
	}
	    
	/**
	 * Reading the Classes from the inside of a jar-file.
	 *
	 * @param jarName the jar name
	 * @return the jAR classes
	 */
	private ArrayList<String> getJARClasses(String jarName) {
		
		String CurrClass   = "";
		ArrayList<String> classes = new ArrayList<String>();
		
		try {
			
			File file = new File(jarName);
			URL jar = file.toURI().toURL();
			jar = new URL("jar:" + jar.toExternalForm() + "!/");
			
			JarURLConnection conn = (JarURLConnection) jar.openConnection();
			conn.setUseCaches(false);
			
			JarFile jarFile = conn.getJarFile();
			Enumeration<JarEntry> enu = jarFile.entries();
			while (enu.hasMoreElements()) {

				JarEntry jarEntry = enu.nextElement();

				if ((jarEntry.getName().endsWith(".class"))) {
					CurrClass = jarEntry.getName().replaceAll("/", "\\.");
					CurrClass = CurrClass.substring(0, CurrClass.length() - (".class").length());
					// --- Klasse in die Auflistung aufnehmen ? ---
					if (searchINReference == null) {
						classes.add( CurrClass );	
					} else {
						if (CurrClass.startsWith(searchINReference) ) {
							classes.add( CurrClass );
						}		
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	
	
	/**
	 * Reading all Classes from the IDE area by starting at 'BasePath'.
	 *
	 * @param BasePath the base path
	 * @param SearchPath the search path
	 * @return the iDE classes
	 */
	private ArrayList<String> getIDEClasses(String BasePath, String SearchPath) {

		ArrayList<String> FileList = new ArrayList<String>();
		
		int cutBegin = BasePath.toString().length();
		int cutEnd   = 0;
		String currClass = "";
		
		File dir = new File(SearchPath);
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				// --------------------------------------------------------------------
				// --------------------------------------------------------------------
				if ( files[i].isDirectory()==true ) {
					// ----------------------------------------------------------------
					// --- System.out.print(" (Unterordner)\n");
					if ( searchINReference == null ) {
						// ------------------------------------------------------------
						// --- Falls nach nichts konkretem gesucht wird, dann --------- 
						// --- alles in die Ergebnisliste aufnehmen 		  ---------
						FileList.addAll( getIDEClasses( BasePath, files[i].getAbsolutePath() ) );	
					} else {
						// ------------------------------------------------------------
						// --- Nur das durchsuchen, was auch wirklich interessiert ----
						boolean MoveDeeper = false;
						String SearchINPath = null;
						// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						for (int j=0; j<searchINPathParts.length; j++) {
							if ( SearchINPath == null ) {
								SearchINPath = searchINPathParts[j];	
							} else {
								SearchINPath = SearchINPath +  Application.RunInfo.AppPathSeparatorString() + searchINPathParts[j];
							}								
							// --- Aktuellen Pfad untersuchen / vergleichen -----------
							//System.out.println(files[i].getAbsolutePath());
							if ( files[i].getAbsolutePath().endsWith( SearchINPath) ) {
								MoveDeeper = true;
								break;
							}							
						}
						// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						if ( MoveDeeper == true ) {
							// --- eine Verzeichnisebene tiefer suchen ----------------
							FileList.addAll( getIDEClasses( BasePath, files[i].getAbsolutePath() ) );	
						}
						// ------------------------------------------------------------
						// ------------------------------------------------------------
						}					 
				} else {
					// ----------------------------------------------------------------
					// --- System.out.println("Datei: " + CurrClass );
					currClass = files[i].getAbsolutePath();
					if ( currClass.endsWith(".class") ) {
						// --- String der Klassendatei anpassen -----------------------
						cutEnd    = currClass.length() - (".class").length();						
						currClass = currClass.substring(cutBegin, cutEnd);
						currClass = currClass.replace('/', '.').replace('\\', '.');						
						while(currClass.startsWith(".")) {
							currClass = currClass.substring(1, currClass.length());
						}
						// --- Klasse in die Auflistung aufnehmen ? -------------------
						if ( searchINReference == null ) {
							FileList.add( currClass );	
						} else {
							if (currClass.startsWith( searchINReference ) ) {
								FileList.add( currClass );
							}		
						}
						// ------------------------------------------------------------
					}
				}
				// --------------------------------------------------------------------
				// --------------------------------------------------------------------
			}		
		}
		return FileList;
	}
	
}
