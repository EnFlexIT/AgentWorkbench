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
package org.agentgui.bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentConstants;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import agentgui.core.application.Application;
import agentgui.core.project.ProjectBundleLoader;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;
import de.enflexit.common.transfer.FileCopier;

/**
 * The Class BundleBuilder is able to create OSGI bundles that can be loaded by the {@link ProjectBundleLoader}.<br><br>
 * Therefore, several use cases were considered for the builder:<br>
 * - the case that the a bin folder of a Java project will be linked as external resource<br>    
 * - the inclusion of external jar files (also in combination with the above case) or<br> 
 * - the case of already well defined bundles.<br>
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BundleBuilder {

	/** This Enumeration is used as return type for the method {@link BundleBuilder#getFileType(File)}. */
	private enum FileType {
		FILE_NOT_FOUND,
		DIRECTORY,
		NON_JAR_FILE,
		JAR_FILE,
		OSGI_BUNDLE
	}
	
	public static final String MANIFEST_MF = "MANIFEST.MF";
	public static final String CLASS_LOAD_SERVICE_JAR = "classLoadService.jar";
	public static final String CLASS_LOAD_SERVICE_XML = "classLoadService.xml";
	public static final String CLASS_LOAD_SERVICE_NAME = "BaseClassLoadServiceImpl";
	
	private ArrayList<String> allowedFileSuffixes;
	private ArrayList<String> allowedManifestBundlePrefixes;
	
	private String symbolicBundleName;
	private File bundleDirectory;
	
	private ArrayList<File> bundleJars;
	private ArrayList<File> regularJars;
	private HashMap<File, List<String>> regularJarsPackagesHashMap; 
	private HashMap<File, String> bundleJarsSymbolicBundleNames;
	
	/**
	 * Instantiates a new bundle builder.
	 *
	 * @param directoryToBundle the directory to bundle
	 * @param symbolicBundleName the symbolic bundle name
	 */
	public BundleBuilder(File directoryToBundle, String symbolicBundleName) {
		// --- Check the arguments first ------------------
		if (directoryToBundle==null) {
			throw new IllegalArgumentException("No file or directory was specified for the bundle build process!");
		} else if (symbolicBundleName==null || symbolicBundleName.equals("")) {
			throw new IllegalArgumentException("No symbolic bundle name was specified for the bundle build process!");
		}
		this.setBundleDirectory(directoryToBundle);
		this.setSymbolicBundleName(symbolicBundleName);
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
	 * Returns the symbolic bundle name.
	 * @return the symbolic bundle name
	 */
	public String getSymbolicBundleName() {
		return symbolicBundleName;
	}
	/**
	 * Sets the symbolic bundle name.
	 * @param symbolicBundleName the new symbolic bundle name
	 */
	public void setSymbolicBundleName(String symbolicBundleName) {
		this.symbolicBundleName = symbolicBundleName;
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
			for (File file2Check : jarFilesFound) {
				// --- Check the file type ----------------
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
		
		// --- Reset reminder of jar packages -------------
		this.getRegularJarsPackagesHashMap().clear();
		if (this.regularJars!=null) {
			// --- Remind packages in regular jars --------
			BundleEvaluator be = BundleEvaluator.getInstance();
			for (int i = 0; i < this.regularJars.size(); i++) {
				// --- Get the 
				File jarFile = this.regularJars.get(i);
				List<String> classesInJar = be.getJarClassReferences(null, jarFile);
				List<String> packages = be.getPackagesOfClassNames(classesInJar);
				// --- Remind packages for export ---------
				this.getRegularJarsPackagesHashMap().put(jarFile, packages);
			}
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
	 * Returns the reminder HashMap for packages in regular jars .
	 * @return the regular jars packages
	 */
	private HashMap<File, List<String>> getRegularJarsPackagesHashMap() {
		if (regularJarsPackagesHashMap==null) {
			regularJarsPackagesHashMap = new HashMap<>();
		}
		return regularJarsPackagesHashMap;
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
	// --- Methods for the MANIFEST file ------------------
	// ----------------------------------------------------
	/**
	 * Returns the manifest location, based on the specified bundle directory.
	 * @return the manifest location
	 */
	public String getManifestLocation() {
		String manifestLocation = this.getBundleDirectory().getAbsolutePath() + File.separator + "META-INF" + File.separator + MANIFEST_MF; 
		return manifestLocation;
	}
	/**
	 * Checks if is available manifest.
	 *
	 * @param directory the directory
	 * @return true, if is available manifest
	 */
	public boolean isAvailableManifest() {
		File manifestFile = new File(this.getManifestLocation()); 
		return manifestFile.exists();
	}

	/**
	 * Checks, if the MANIFEST file is available. If not, it will be created.
	 *
	 * @param overwriteExistingManifest set true, if the existing manifest has to be overwritten
	 * @return true, if the file is available or if it could successfully be created
	 */
	public boolean createManifest(boolean overwriteExistingManifest) {
		
		boolean created = false;
		
		// --- Check if MANIFEST already exists ---------------------
		File manifestFile = new File(this.getManifestLocation());
		if (overwriteExistingManifest==false && manifestFile.exists()==true) return true;
		
		// --- MANIFEST does not exists yet -------------------------
		FileOutputStream fOut = null;
		try {
			
			// ------------------------------------------------------
			// --- Create directory, if not already there -----------
			// ------------------------------------------------------
			File metaInfDir = manifestFile.getParentFile();
			if (metaInfDir.exists()==false) {
				metaInfDir.mkdirs();
			}
			
			// ------------------------------------------------------
			// --- Define Manifest ---------------------------------- 
			// ------------------------------------------------------
			Manifest manifest = new Manifest();
			Attributes att = manifest.getMainAttributes();
			att.putValue("Manifest-Version", "1.0");
			att.putValue(Constants.BUNDLE_MANIFESTVERSION, "2");
			att.putValue(Constants.BUNDLE_NAME, this.getSymbolicBundleName());
			att.putValue(Constants.BUNDLE_SYMBOLICNAME, this.getSymbolicBundleName());
			
			// --- Define the required bundles ----------------------
			Vector<String> bundleDescriptionVector = new Vector<>();
			Bundle[] bundles = BundleEvaluator.getInstance().getBundles();
			for (int i = 0; i < bundles.length; i++) {
				Bundle bundle = bundles[i];
				if (this.isAllowedManifestBundlesPrefix(bundle.getSymbolicName())) {
					bundleDescriptionVector.add(bundle.getSymbolicName() + ";bundle-version=\"" + bundle.getVersion().toString() + "\"");
				}
			}
			Collections.sort(bundleDescriptionVector);
			// --- Merge vector to MANIFEST entry -------------------
			String requireBundleString = "";
			for (int i = 0; i < bundleDescriptionVector.size(); i++) {
				if (requireBundleString.length()==0) {
					requireBundleString = bundleDescriptionVector.get(i);
				} else {
					requireBundleString += "," + bundleDescriptionVector.get(i);
				}	
			} 
			att.putValue(Constants.REQUIRE_BUNDLE, requireBundleString);
			
			// --- Define jar files to be included ------------------
			String bundleClassPathString = "";
			for (File jarFile : this.getRegularJars()) {
				String jarFilePath = this.getRequiredBundleEntry(jarFile);
				if (bundleClassPathString.length()==0) {
					bundleClassPathString = jarFilePath;
				} else {
					bundleClassPathString += "," + jarFilePath;
				}
			}
			att.putValue(Constants.BUNDLE_CLASSPATH, bundleClassPathString);
			
			// --- Define export packages --------------------------
			String exportPackageString = "";
			for (File jarFile : this.getRegularJars()) {
				// --- Get packages for each jar-file ---------------
				List<String> packageNames = this.getRegularJarsPackagesHashMap().get(jarFile);
				if (packageNames==null) continue;
				
				for (int i = 0; i < packageNames.size(); i++) {
					String packageName = packageNames.get(i);
					if (exportPackageString.length()==0) {
						exportPackageString = packageName;
					} else {
						exportPackageString += "," + packageName;
					}
				} 
			}
			att.putValue(Constants.EXPORT_PACKAGE, exportPackageString);
			
			// --- Define xml-file of BaseClassLoadService --------------
			att.putValue(ComponentConstants.SERVICE_COMPONENT, "classLoadService.xml");
			
			// ------------------------------------------------------
			// --- Write the  MANIFEST file -------------------------
			// ------------------------------------------------------
			fOut = new FileOutputStream(manifestFile);
			manifest.write(fOut);
			created = true;
					
		} catch (FileNotFoundException fnfEx) {
			fnfEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			try {
				if (fOut!=null) {
					fOut.close();
				}
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
		return created;
	}
	
	/**
	 * Returns a required bundle entry for the Manifest.mf.
	 *
	 * @param jarFile the jar file
	 * @return the required bundle entry
	 */
	private String getRequiredBundleEntry(File jarFile) {
		String jarFilePath = jarFile.getAbsolutePath().substring(this.getBundleDirectory().getAbsolutePath().length());
		while(jarFilePath.startsWith(File.separator)) {
			jarFilePath = jarFilePath.substring(1);	
		}
		return jarFilePath;
	}
	
	// ----------------------------------------------------
	// --- Methods for the MANIFEST file ------------------
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
			allowedFileSuffixes.add(".class");
			allowedFileSuffixes.add(".png");
			allowedFileSuffixes.add(".bmp");
			allowedFileSuffixes.add(".jpg");
			allowedFileSuffixes.add(".jepg");
			allowedFileSuffixes.add(".gif");
		}
		return allowedFileSuffixes;
	}
	/**
	 * Returns the allowed bundle prefixes for the MANIFEST.MF
	 * @return the positive file suffixes
	 */
	private ArrayList<String> getAllowedManifestBundlesPrefixex() {
		if (allowedManifestBundlePrefixes==null) {
			allowedManifestBundlePrefixes = new ArrayList<String>();
			allowedManifestBundlePrefixes.add("org.agentgui");
			allowedManifestBundlePrefixes.add("de.enflexit");
		}
		return allowedManifestBundlePrefixes;
	}
	/**
	 * Checks if the specified symbolic bundle name has an allowed prefix.
	 *
	 * @param symbolicBundleName the symbolic bundle name
	 * @return true, if is allowed manifest bundles prefix
	 */
	public boolean isAllowedManifestBundlesPrefix(String symbolicBundleName) {
		for (int i = 0; i < this.getAllowedManifestBundlesPrefixex().size(); i++) {
			String allowedPrefix = this.getAllowedManifestBundlesPrefixex().get(i);
			if (symbolicBundleName.startsWith(allowedPrefix)) {
				return true;
			}
		}
		return false;
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
				String symbolicName = (String) mainAttributes.getValue(Constants.BUNDLE_SYMBOLICNAME);
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
	// --- Methods for the search of files ----------------
	// ----------------------------------------------------

	// ----------------------------------------------------
	// --- Methods for moving files of ClassLoadService ---
	// ----------------------------------------------------
	/**
	 * Move class load service files.
	 * @param serviceName the service name for the BaseClassLoadService
	 */
	public void moveClassLoadServiceFiles() {
		
		String pathProps = Application.getGlobalInfo().getPathProperty(true);
		
		File sourceCLSjar = new File(pathProps + File.separator + CLASS_LOAD_SERVICE_JAR);
		File sourceCLSxml = new File(pathProps + File.separator + CLASS_LOAD_SERVICE_XML);
		
		File destinCLSjar = new File(this.getBundleDirectory() + File.separator + CLASS_LOAD_SERVICE_JAR);
		File destinCLSxml = new File(this.getBundleDirectory() + File.separator + CLASS_LOAD_SERVICE_XML);
		
		// --- Move the jar file ------------------------------------
		if (sourceCLSjar.exists()==true) {
			if (destinCLSjar.exists()==true) {
				destinCLSjar.delete();
			}
			new FileCopier().copyFile(sourceCLSjar.getAbsolutePath(), destinCLSjar.getAbsolutePath());
			if (this.getRegularJars().contains(destinCLSjar)==false) {
				this.getRegularJars().add(destinCLSjar);
			}
			
		} else {
			throw new RuntimeException("BaseClassLoadService-Files: Could not find file '" + sourceCLSjar + "'.");
		}
		
		// --- Move the xml file ------------------------------------
		if (sourceCLSxml.exists()==true) {
			if (destinCLSxml.exists()==true) {
				destinCLSxml.delete();
			}
			new FileCopier().copyFile(sourceCLSxml.getAbsolutePath(), destinCLSxml.getAbsolutePath());
			// --- Edit the destination service description --------- 
			this.setServiceDescriptionName(destinCLSxml);
			
		} else {
			throw new RuntimeException("BaseClassLoadService-Files: Could not find file '" + sourceCLSxml + "'.");
		}
		
	}
	/**
	 * Sets the service description name.
	 *
	 * @param serviceDescriptionFile the service description file
	 * @param serviceName the service name
	 */
	private void setServiceDescriptionName(File serviceDescriptionFile) {
		
		String newServiceName = this.getSymbolicBundleName() + "." + CLASS_LOAD_SERVICE_NAME;
		
		try {
			// --- Open the XML document ------------------
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(serviceDescriptionFile);
			
			// --- Get the XML root/component element -----
			Node component = doc.getFirstChild();
			NamedNodeMap componentAttr = component.getAttributes();
			Node nameAttr = componentAttr.getNamedItem("name");
			nameAttr.setTextContent(newServiceName);
			
			// --- Save document in XML file --------------	
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(serviceDescriptionFile);
			transformer.transform(source, result);
			
		} catch (ParserConfigurationException pcEx) {
			pcEx.printStackTrace();
		} catch (SAXException saxEx) {
			saxEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} catch (TransformerConfigurationException tcEx) {
			tcEx.printStackTrace();
		} catch (TransformerException tEx) {
			tEx.printStackTrace();
		}
		
	}
	// ----------------------------------------------------
	// --- Methods for moving files of ClassLoadService ---
	// ----------------------------------------------------

	
	
}
