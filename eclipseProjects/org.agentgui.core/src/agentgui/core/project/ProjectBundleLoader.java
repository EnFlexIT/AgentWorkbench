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
import java.util.Vector;

import org.agentgui.bundle.BundleBuilder;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

import agentgui.core.application.Application;

/**
 * The Class ProjectBundleLoader.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ProjectBundleLoader {

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
	public boolean installAndStartBundles() {
		
		boolean bundleLoaded = false;
		if (this.getBundleDirectory()!=null) {

			try {
				// --- Load the directory bundle ------------------------------
				if (this.getBundleBuilder().getRegularJars()!=null) {
					
					// --- Check if the manifest file is available ------------
					if (this.getBundleBuilder().isAvailableManifest()==false) {
						
						// --- Check for files of the BaseClassLoadService --------
						this.getBundleBuilder().moveClassLoadServiceFiles();
						
						// --- Create the MANIFEST.MF file --------------------
						if (this.getBundleBuilder().createManifest()==false) {
							throw new RuntimeException("The file " + BundleBuilder.MANIFEST_MF + " could not be created!");
						}
					}
					this.installAndStartBundle("reference:file:" + this.getBundleDirectory());
				}
				
				// --- Load the independent bundles found ---------------------
				if (this.getBundleBuilder().getBundleJars()!=null) {
					for (File bundleFile : this.getBundleBuilder().getBundleJars()) {
						this.installAndStartBundle("reference:file:" + bundleFile.getAbsolutePath());
					}
				}
				bundleLoaded = true;
				
			} catch (BundleException bEx) {
				bEx.printStackTrace();
				bundleLoaded = false;
			}
		}
		return bundleLoaded;
	}
	
	/**
	 * Install and start bundle.
	 *
	 * @param locationID the location ID
	 * @throws BundleException the bundle exception
	 */
	public void installAndStartBundle(String locationID) throws BundleException {

		// --- Install and start bundle ---------
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		Bundle bundle = bundleContext.installBundle(locationID);
		bundle.start();
		
		// --- Remind this bundle ---------------
		this.getBundleVector().addElement(bundle);
		
	}
	/**
	 * Stops and un-installs the current bundle.
	 */
	public void stopAndUninstallBundles() {
		Vector<Bundle> bundlesToRemove = new Vector<>(this.getBundleVector());
		for (Bundle bundle: bundlesToRemove) {
			try {
				
				if (bundle.getState()==Bundle.ACTIVE) {
					bundle.stop();
					bundle.uninstall();
				}
				this.getBundleVector().remove(bundle);
				
			} catch (BundleException bEx) {
				bEx.printStackTrace();
			}
		}
	}
	
}
