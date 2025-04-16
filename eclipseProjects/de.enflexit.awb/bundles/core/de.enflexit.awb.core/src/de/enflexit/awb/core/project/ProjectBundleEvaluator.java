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
package de.enflexit.awb.core.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

/**
 * The Class ProjectBundleEvaluator searches for OSGI bundles and *.jar files in a specified directory.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectBundleEvaluator {

	/** This Enumeration is used as return type for the method {@link ProjectBundleEvaluator#getFileType(File)}. */
	private enum FileType {
		FILE_NOT_FOUND,
		DIRECTORY,
		NON_JAR_FILE,
		JAR_FILE,
		OSGI_BUNDLE
	}
	
	private ArrayList<String> allowedFileSuffixes;
	
	private File bundleDirectory;
	
	private ArrayList<File> bundleJars;
	private ArrayList<File> regularJars;
	private HashMap<File, String> bundleJarsSymbolicBundleNames;
	
	
	/**
	 * Instantiates a new bundle builder.
	 * @param directoryToEvaluate the directory to evaluate
	 */
	public ProjectBundleEvaluator(File directoryToEvaluate) {
		// --- Check the arguments first ------------------
		if (directoryToEvaluate==null) {
			throw new IllegalArgumentException("No file or directory was specified for the bundle search!");
		}
		this.setBundleDirectory(directoryToEvaluate);
	}

	/**
	 * Returns the bundle directory.
	 * @return the bundle directory
	 */
	public File getBundleDirectory() {
		return bundleDirectory;
	}
	/**
	 * Sets the current bundle directory.
	 * @param bundleFile the new bundle directory
	 */
	public void setBundleDirectory(File bundleFile) {
		this.bundleDirectory = bundleFile;
	}
	
	/**
	 * Gets the reminder for bundle jars and their symbolic bundle names.
	 * @return the bundle jars symbolic bundle names
	 */
	public HashMap<File, String> getBundleJarsSymbolicBundleNames() {
		if (bundleJarsSymbolicBundleNames==null) {
			bundleJarsSymbolicBundleNames = new HashMap<>();
		}
		return bundleJarsSymbolicBundleNames;
	}
	
	// ----------------------------------------------------
	// --- Methods for the directory evaluation -----------
	// ----------------------------------------------------
	/**
	 * Evaluates the specified directory.
	 */
	public void evaluateDirectory() {
		
		// --- Search for relevant files ------------------ 
		ArrayList<File> filesFound = this.findFiles(this.getBundleDirectory());

		// --- Filter for jar-files first -----------------
		ArrayList<File> jarFilesFound = this.filterForFileTypes(filesFound, "jar");
		ArrayList<File> regularJarFilesFound = new ArrayList<>(); 
		ArrayList<File> bundleJarFilesFound = new ArrayList<>();
		
		if (jarFilesFound!=null) {
			
			for (int i = 0; i < jarFilesFound.size(); i++) {
				// --- Check the file type ----------------
				File file2Check = jarFilesFound.get(i);
				FileType fileType= this.getFileType(file2Check);
				switch (fileType) {
				case JAR_FILE:
					regularJarFilesFound.add(file2Check);
					break;
				case OSGI_BUNDLE:
					bundleJarFilesFound.add(file2Check);
					break;
				default:
					break;
				}
			}
		}
		
		// --- Assign regular jars that were found --------
		if (regularJarFilesFound.size()>0) {
			this.regularJars = regularJarFilesFound;
		} else {
			this.regularJars = null;	
		}
		// --- Assign bundle jars that were found ---------
		if (bundleJarFilesFound.size()>0) {
			this.bundleJars = bundleJarFilesFound;
		} else {
			this.bundleJars = null;
		}
		
	}
	/**
	 * Returns the bundle jars.
	 * @return the bundle jars
	 */
	public ArrayList<File> getBundleJars() {
		return bundleJars;
	}
	/**
	 * Return the file of the specified project bundle.
	 *
	 * @param bundle the bundle
	 * @return the bundle file
	 */
	public File getBundleFile(Bundle bundle) {
		if (this.getBundleJars()!=null) {
			for (int i=0; i<this.getBundleJars().size(); i++) {
				File file = this.getBundleJars().get(i);
				String symBundleName = this.getBundleJarsSymbolicBundleNames().get(file);
				if (symBundleName.equals(bundle.getSymbolicName())) return file;
			}
		}
		return null;
	}
	/**
	 * Gets the regular jars.
	 * @return the regular jars
	 */
	public ArrayList<File> getRegularJars() {
		return regularJars;
	}
	
	/**
	 * Filters the specified file list for the specified file type.
	 *
	 * @param fileListToFilter the file list to filter
	 * @param fileType the file type
	 * @return the array list
	 */
	private ArrayList<File> filterForFileTypes(ArrayList<File> fileListToFilter, String fileType){
		
		ArrayList<File> filteredList = new ArrayList<>();
		for (int i = 0; i < fileListToFilter.size(); i++) {
			File fileToCheck = fileListToFilter.get(i);
			if (this.getFileExtension(fileToCheck).equals(fileType)) {
				filteredList.add(fileToCheck);
			}
		}
		
		if (filteredList.size()==0) {
			filteredList = null;
		}
		return filteredList;
	}
	/**
	 * Returns the file extension of the file.
	 *
	 * @param file the file
	 * @return the file extension
	 */
	private String getFileExtension(File file) {
		
		if (file==null) return null;
		if (file.isDirectory()==true) return null;
		
		String fileExtension = null;
		String fileName = file.getName();

		int i = fileName.lastIndexOf('.');
		if (i>0) {
			fileExtension = fileName.substring(i+1);
		}
		return fileExtension;
	}
	// ----------------------------------------------------
	// --- Methods for the directory evaluation -----------
	// ----------------------------------------------------
	
	
	// ----------------------------------------------------
	// --- Methods for the search of files ----------------
	// ----------------------------------------------------
	/**
	 * This method will find all available files specified by the find String.
	 *
	 * @param directory the directory
	 * @param allowedFileSuffixes the allowed file suffixes
	 */
	private ArrayList<File> findFiles(File directory) {

		File[] files = directory.listFiles();
		ArrayList<File> matches = new ArrayList<File> ();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					// --- Search sub-directories ---------
					matches.addAll(this.findFiles(files[i])); 
				} else {
					// --- filter single file for 'find' ------
					int lastDotPos = files[i].getName().lastIndexOf('.');
					if (lastDotPos>0) {
						String fileSuffix = files[i].getName().substring(lastDotPos);
					    if (this.getAllowedFileSuffixes().contains(fileSuffix)) {
					    	matches.add(files[i]);
					    }
					}
				}
			}
		}
		return matches;
	}
	/**
	 * Returns the allowed file suffixes for a *.jar-file to be created.
	 * @return the positive file suffixes
	 */
	private ArrayList<String> getAllowedFileSuffixes() {
		if (allowedFileSuffixes==null) {
			allowedFileSuffixes = new ArrayList<String>();
			allowedFileSuffixes.add(".jar");
			// --- Removed since regular jar files are not allowed anymore ----
			//allowedFileSuffixes.add(".class");
			//allowedFileSuffixes.add(".png");
			//allowedFileSuffixes.add(".bmp");
			//allowedFileSuffixes.add(".jpg");
			//allowedFileSuffixes.add(".jepg");
			//allowedFileSuffixes.add(".gif");
		}
		return allowedFileSuffixes;
	}
	
	/**
	 * Returns the {@link FileType} of the specified file object. If the specified 
	 * file object is null the method also return null. Otherwise, it checks and returns 
	 * the {@link FileType} of the specified file.
	 *
	 * @param file2Check the file object to check
	 * @return the file type
	 */
	private FileType getFileType(File file2Check) {
		
		if (file2Check==null) {
			return null;
		} else if(file2Check.exists()==false) {
			return FileType.FILE_NOT_FOUND;
		} else if (file2Check.isDirectory()==true) {
			return FileType.DIRECTORY;
		} 
		
		// --- Try to open the jar file -----------------------------
		FileType fileType = FileType.NON_JAR_FILE;
		JarFile jar = null;
		try {
			jar = new JarFile(file2Check);
			Manifest manifest = jar.getManifest();
			if (manifest!=null) {
				// --- At least we found a jar file -----------------
				fileType = FileType.JAR_FILE;

				// --- Check for an OSGI bundle ---------------------
				Attributes mainAttributes = manifest.getMainAttributes();
				String symbolicName = mainAttributes.getValue(Constants.BUNDLE_SYMBOLICNAME);
				if (symbolicName==null || symbolicName.isEmpty()) return fileType;
						
				symbolicName = (String) symbolicName.trim();
				int semicolonPosition = symbolicName.indexOf(";");
				if (semicolonPosition!=-1) {
					// --- Remove entries like ';singleton:=true' ---
					symbolicName = symbolicName.substring(0, semicolonPosition).trim();
				}
				if (symbolicName!=null && symbolicName.equals("")==false)  {
					fileType = FileType.OSGI_BUNDLE;
					// --- Remind the symbolic bundle name ----------
					this.getBundleJarsSymbolicBundleNames().put(file2Check, symbolicName);
				}
			}
			
		} catch (IOException ioe) {
			//ioe.printStackTrace();
			System.err.println(this.getClass().getSimpleName() + "#getFileType(File): " + ioe.getMessage());
			
		} finally {
			// --- Make sure that the file will be closed -----------
			try {
				if (jar!=null) jar.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return fileType;
	}
	
	// ----------------------------------------------------
	// --- Methods for the search of files ----------------
	// ----------------------------------------------------
	
}
