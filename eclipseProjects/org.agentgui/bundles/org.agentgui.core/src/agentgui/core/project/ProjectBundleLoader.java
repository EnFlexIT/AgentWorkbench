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
package agentgui.core.project;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;

import org.agentgui.bundle.BundleBuilder;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import de.enflexit.common.PathHandling;

/**
 * The Class ProjectBundleLoader.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ProjectBundleLoader {

	private boolean debug = false;
	
	private Project project;
	
	private BundleBuilder bundleBuilder;
	private Vector<Bundle> bundleVector;
	
	/**
	 * Instantiates a new bundle loader.
	 * @param project the project
	 */
	public ProjectBundleLoader(Project project) {
		this.project = project;
	}
	/**
	 * Returns the bundle directory.
	 * @return the bundle directory
	 */
	public String getBundleDirectory() {
		return this.project.getProjectFolderFullPath();
	}
	
	/**
	 * Returns the symbolic bundle name for the current project. In case that the
	 * bundle was not loaded yet, the method return a name suggestion. In case that
	 * the bundle was already loaded, the method returns its name.
	 *  
	 * @return the symbolic bundle name of the projects bundle
	 */
	private String getSymbolicBundleName() {
		
		// --- Return the first bundle name -------------------------
		if (this.getBundleVector().size()>0 && this.getBundleVector().get(0)!=null) {
			return this.getBundleVector().get(0).getSymbolicName();
		}
		
		// --- Bundle was not yet loaded - create name proposal ----- 
		String bundleName = this.getBundleDirectory();
		String appPath = Application.getGlobalInfo().getPathBaseDir();
		if (bundleName.contains(appPath)==true) {
			// --- Get path difference ------------------------------
			bundleName = bundleName.substring(appPath.length());
			String[] pathParts = bundleName.split("\\" + File.separator);
			bundleName = pathParts[pathParts.length-1];
			
		} else {
			// --- Take the last two sub folder --------------------- 
			String[] pathParts = bundleName.split("\\" + File.separator);
			if (pathParts.length>1) {
				bundleName = pathParts[pathParts.length-1];
			} else if (pathParts.length==1) {
				bundleName = pathParts[0];
			} else {
				bundleName = "tmp" + bundleName.hashCode();
			}
		}
		// --- Add a suffix to the bundle name ----------------------
		bundleName = "agentProject." + bundleName;
		
		// --- Do some final adjustments ----------------------------
		while (bundleName.contains("..")) {
			bundleName = bundleName.replace("..", ".");
		}
		while (bundleName.startsWith(".")) {
			bundleName = bundleName.substring(1);
		}
		while (bundleName.endsWith(".")) {
			bundleName = bundleName.substring(0, bundleName.length()-1);
		}
		return bundleName;
	}
	
	
	/**
	 * Returns the instance of the {@link BundleBuilder} for this project.
	 * @return the bundle builder
	 */
	private BundleBuilder getBundleBuilder() {
		if (bundleBuilder==null) {
			bundleBuilder = new BundleBuilder(new File(this.getBundleDirectory()), this.getSymbolicBundleName());
		}
		return bundleBuilder;
	}
	/**
	 * Returns the bundle jars.
	 * @return the bundle jars
	 */
	public ArrayList<File> getBundleJars() {
		return this.getBundleBuilder().getBundleJars();
	}
	/**
	 * Return the file of the specified project bundle.
	 * @param bundle the bundle
	 * @return the bundle file
	 */
	public File getBundleFile(Bundle bundle) {
		return this.getBundleBuilder().getBundleFile(bundle);
	}
	/**
	 * Gets the regular jars.
	 * @return the regular jars
	 */
	public ArrayList<File> getRegularJars() {
		return this.getBundleBuilder().getRegularJars();
	}
	
	/**
	 * Returns the regular jar file resources as list model.
	 * @return the regular jar file resources
	 */
	public DefaultListModel<String> getRegularJarsListModel() {
		return this.getListModelFromFilesFound(this.getBundleBuilder().getRegularJars());
	}
	/**
	 * Returns the bundle jar file resources as list model.
	 * @return the bundle jar file resources
	 */
	public DefaultListModel<String> getBundleJarsListModel() {
		return this.getListModelFromFilesFound(this.getBundleBuilder().getBundleJars());
	}
	/**
	 * Returns a list model out of the specified list of files.
	 *
	 * @param filesFound the files found as ArrayList
	 * @return the list model from the files found
	 */
	private DefaultListModel<String> getListModelFromFilesFound(ArrayList<File> filesFound) {
		
		String projectDir = this.project.getProjectFolderFullPath();
		DefaultListModel<String> listModel = new DefaultListModel<>();
		if (filesFound!=null) {
			for (int i = 0; i < filesFound.size(); i++) {
				File jarFile = filesFound.get(i);
				String relPathToFile = jarFile.getAbsolutePath().substring(projectDir.length());
				relPathToFile = relPathToFile.replace("\\", "/");
				listModel.addElement(relPathToFile);
			}
		}
		return listModel;
	}
	
	
	/**
	 * Returns the bundle vector that contains all bundles loaded by the current project. 
	 * @return the bundle vector
	 */
	public Vector<Bundle> getBundleVector() {
		if (bundleVector==null) {
			bundleVector = new Vector<>();
		}
		return bundleVector;
	}
	
	/**
	 * Load the bundle of the current project.
	 * @return true, if successful
	 */
	public void installAndStartBundles() {
		
		if (this.getBundleDirectory()==null) return;
			
		// --- Check configured project resources for bin folder ----
		this.createTemporaryJarsFromBinResources();
		
		// --- Evaluate the directory -------------------------------
		this.getBundleBuilder().evaluateDirectory();
		
		// --- Load the directory bundle ----------------------------
		if (this.getBundleBuilder().getRegularJars()!=null) {
			
			// --- Check if the manifest file is available ----------
			if (this.project.isReCreateProjectManifest()==true || this.getBundleBuilder().isAvailableManifest()==false) {
				
				// --- Check for files of the ClassLoadService ------
				this.getBundleBuilder().moveClassLoadServiceFiles();
				
				// --- Create the MANIFEST.MF file ------------------
				if (this.getBundleBuilder().createManifest(this.project.isReCreateProjectManifest())==false) {
					throw new RuntimeException("The file " + BundleBuilder.MANIFEST_MF + " could not be created!");
				}
			}
			this.installBundle("reference:file:" + this.getBundleDirectory());
		}
		
		// --- Install the independent bundles found ----------------
		if (this.getBundleBuilder().getBundleJars()!=null) {
			this.installBundles(this.getBundleBuilder().getBundleJars());
		}
		
		// --- Finally, start all known bundles ---------------------
		this.startBundles(this.getBundleVector());
		
	}
	
	/**
	 * Creates the temporary jars from the specified bin resources.
	 */
	private void createTemporaryJarsFromBinResources() {
		
		String projectDir = this.project.getProjectTempFolderFullPath();
		ProjectResourceVector projectResourcesVector = this.project.getProjectResources();
		
		for (int i=0; i<projectResourcesVector.size(); i++) {
			
			String resource = projectResourcesVector.get(i);
			resource = PathHandling.getPathName4LocalOS(resource);
			
			String prefixText = null;
			String suffixText = null;
			try {
				
				// --- Adjust path with respect to the project directory ------
				String jarFileCorrected = this.adjustPathForForProjectEnvironment(resource, projectDir);
				File file = new File(jarFileCorrected);
				if (file.exists()==false && jarFileCorrected.toLowerCase().endsWith("jar")==false) {
					// --- Try to find / retrieve bin resource ----------------
					String jarFileCorrectedNew = this.retrievBinResourceFromPath(jarFileCorrected);
					if (jarFileCorrectedNew.equals(jarFileCorrected)==false) {
						// --- Found new bin resource -------------------------
						System.out.println("=> Retrieved new location for resource path '" + jarFileCorrected + "'");
						System.out.println("=> Corrected it to '" + jarFileCorrectedNew + "'");
						projectResourcesVector.set(i, jarFileCorrectedNew);
						
						resource = jarFileCorrectedNew;
						jarFileCorrected = jarFileCorrectedNew;
						file = new File(jarFileCorrected);	
					}
				}
				
				if (file.isDirectory()) {
					// --- Folder found: Build a temporary *.jar --------------
					prefixText = "";
					suffixText = "proceeding started";
					
					// --- Prepare the path variables ---------------
					String pathBin = file.getAbsolutePath();
					String pathBinHash = ((Integer)Math.abs(pathBin.hashCode())).toString();
					String jarArchiveFileName = "BIN_DUMP_" + pathBinHash + ".jar";
					String jarArchivePath = projectDir + jarArchiveFileName;
					
					// --- Create the jar-file ----------------------
					JarFileCreator jarCreator = new JarFileCreator(pathBin);
					File jarArchiveFile = new File(jarArchivePath);
					jarCreator.createJarArchive(jarArchiveFile);
					jarArchiveFile.deleteOnExit();
					
					// --- Prepare the notification -----------------
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
					String dateText = dateFormat.format(new Date());
					
					prefixText = "OK";
					suffixText = Language.translate("Verzeichnis gepackt zu") + " '" + File.separator + "~tmp" + File.separator + jarArchiveFileName + "' " + Language.translate("um") + " " + dateText;
				} 
				
			} catch (Exception ioEx) {
				ioEx.printStackTrace();
				prefixText = "ERROR";
				suffixText = ioEx.getMessage();
			}
			
			// --- On Error print it to console -------------------------------
			if (prefixText!=null && prefixText.equals("ERROR")==true) {
				System.err.println("=> " + suffixText + " - " + resource);
			}
			// --- Set the additional text ------------------------------------
			projectResourcesVector.setPrefixText(resource, prefixText);
			projectResourcesVector.setSuffixText(resource, suffixText);
		}
		
	}
	/**
	 * Tries to retrieve a bin-folder location given form an external resource to load for a project.
	 * @param resourcePath the resource path
	 * @return the same string if nothing is found, otherwise the new resource path
	 */
	private String retrievBinResourceFromPath(String resourcePath) {
		
		// --- Get Agent.GUI base directory and walk up to parent folders ----
		File searchDir = new File(Application.getGlobalInfo().getPathBaseDir());
		for (int i=0; i<2; i++) {
			if (searchDir!=null) {
				searchDir = searchDir.getParentFile();
			} else {
				break;
			}
		}
		
		// --- Directory found? -----------------------------------------------
		if (searchDir!=null) {
			// --- Set the string for the search path -------------------------
			String searchPath = searchDir.getAbsolutePath() + File.separator;
			// --- Examine the give resource path -----------------------------			
			String checkAddition = null;
			String[] jarFileCorrectedFragments = resourcePath.split("\\"+File.separator);
			// --- Try to rebuild the resource path ---------------------------
			for (int i=(jarFileCorrectedFragments.length-1); i>0; i--) {
				String fragment = jarFileCorrectedFragments[i];
				if (fragment.equals("")==false) {
					if (i==(jarFileCorrectedFragments.length-1)) {
						checkAddition = fragment;	
					} else {
						checkAddition = fragment + File.separator + checkAddition;
					}
					 
					File checkPath = new File(searchPath + checkAddition);	
					if (checkPath.exists()==true) {
						// --- Path found ! -----------------------------------
						return checkPath.getAbsolutePath();
					}	
				}
			}
		}
		return resourcePath;
	}
	/**
	 * Will check (and maybe adjust) a resource path with respect to the project directory.
	 *
	 * @param resourcePath the resource
	 * @param projectDir the project directory
	 * @return a checked or adjusted path for a resource
	 */
	private String adjustPathForForProjectEnvironment(String resourcePath, String projectDir) {
		
		String checkPath = resourcePath;
		File checkFile = new File(checkPath);
		if (checkFile.exists()) {
			// --- right path was given -----------------------------
			return checkPath;
			
		} else {
			// --- retry with the addition of the project folder ----
			checkPath = projectDir + checkPath;
			checkPath = checkPath.replace((File.separator + File.separator), File.separator);
			checkFile = new File(checkPath);
			if (checkFile.exists()) {
				return checkPath;
			} 
			
		}
		return resourcePath;
	}
	

	/**
	 * Installs the specified list of jar bundled and adds their bundle instances to the local bundle vector {@link #getBundleVector()}.
	 * @param bundleJarFile the bundle jar file
	 */
	public void installBundles(List<File> fileList) {
		for (int i = 0; i < fileList.size(); i++) {
			this.installBundle(fileList.get(i));
		}
	}
	/**
	 * Installs the specified jar bundle and adds the Bundle instance to the local bundle vector {@link #getBundleVector()}.
	 * @param bundleJarFile the bundle jar file
	 */
	public void installBundle(File bundleJarFile) {
		
		// --- Check the symbolic bundle name of the jar to load ----
		String sbn = this.getBundleBuilder().getBundleJarsSymbolicBundleNames().get(bundleJarFile);
		if (sbn!=null && sbn.isEmpty()==false) {
			if (Platform.getBundle(sbn)!=null) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Bundle '" + sbn + "' is already installed, skip installation of jar file!");
				return;
			}
		}
		// --- INstall the local bundle -----------------------------
		this.installBundle("reference:file:" + bundleJarFile.getAbsolutePath());
	}
	/**
	 * Installs the specified jar bundle and adds the Bundle instance to the local bundle vector {@link #getBundleVector()}.
	 * @param bundleJarFilePath the bundle jar file path
	 */
	public void installBundle(String bundleJarFilePath) {
		Bundle bundle = null;
		try {
			BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
			bundle = bundleContext.installBundle(bundleJarFilePath);
			if (this.debug) System.out.println("=> + " + bundle.getSymbolicName() + " installed.");
			
		} catch (BundleException bEx) {
			bEx.printStackTrace();
		}
		// --- Remind this bundle ---------------
		if (bundle!=null) this.getBundleVector().addElement(bundle);
	}
	
	
	/**
	 * Tries to start all specified bundles.
	 * @param bundleVector the bundle vector
	 */
	public void startBundles(Vector<Bundle> bundleVector) {
		for (int i = 0; i < bundleVector.size(); i++) {
			this.startBundle(bundleVector.get(i));
		}
	}
	/**
	 * Starts the specified bundle and return true if successful.
	 *
	 * @param bundle the bundle
	 * @return true, if successful
	 */
	public boolean startBundle(Bundle bundle) {
		boolean bundleStarted = false;
		try {
			bundle.start();
			bundleStarted = true;
			if (this.debug) System.out.println("=> ! " + bundle.getSymbolicName() + " started");
			
		} catch (BundleException bEx) {
			bEx.printStackTrace();
		}
		return bundleStarted;
	}
	
	/**
	 * Stops and un-installs the current bundle.
	 */
	public void stopAndUninstallBundles() {
		
		// --- Get a copy of loaded bundles -----
		Vector<Bundle> bundlesToRemove = new Vector<>(this.getBundleVector());
		for (Bundle bundle: bundlesToRemove) {
			try {
				// --- Remove, if active --------
				if (bundle.getState()==Bundle.ACTIVE) {
					bundle.stop();
					bundle.uninstall();
				}
				// --- Remove from vector -------
				this.getBundleVector().remove(bundle);
				if (this.debug) System.out.println("=> - " + bundle.getSymbolicName() + " stoped & uninstalled");
				
			} catch (BundleException bEx) {
				bEx.printStackTrace();
			}
		}
	}
	
}
