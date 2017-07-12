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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

import agentgui.core.application.Application;
import agentgui.core.project.Project;

/**
 * The Class BundleLoader.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class BundleLoader {

	private String bundleDirectory;
	private Bundle bundle;

	/**
	 * Instantiates a new bundle loader.
	 * @param bundleDirectory the bundle directory
	 */
	public BundleLoader(String bundleDirectory) {
		this.bundleDirectory = bundleDirectory;
	}
	/**
	 * Instantiates a new bundle loader.
	 * @param project the project to be handled as a bundle
	 */
	public BundleLoader(Project project) {
		this(project.getProjectFolderFullPath());
	}
	
	/**
	 * Sets the current bundle directory.
	 * @param bundleDirectory the new bundle directory
	 */
	public void setBundleDirectory(String bundleDirectory) {
		this.bundleDirectory = bundleDirectory;
	}
	/**
	 * Returns the bundle directory.
	 * @return the bundle directory
	 */
	public String getBundleDirectory() {
		return bundleDirectory;
	}
	
	/**
	 * Returns the manifest location, based on the specified bundle directory.
	 * @return the manifest location
	 */
	public String getManifestLocation() {
		String manifestLocation = this.getBundleDirectory() + "META-INF" + File.separator + "MANIFEST.MF";
		return manifestLocation;
	}
	/**
	 * Checks if is available manifest.
	 *
	 * @param directory the directory
	 * @return true, if is available manifest
	 */
	private boolean isAvailableManifest() {
		File manifestFile = new File(this.getManifestLocation()); 
		return manifestFile.exists();
	}
	
	/**
	 * Returns the symbolic bundle name to be loaded or already loaded.
	 * @return the symbolic bundle name
	 */
	private String getSymbolicBundleName() {
		
		if (this.getBundle()!=null) return this.getBundle().getSymbolicName();
		
		// --- Bundle was not yet loaded - create a proposal -------- 
		String bundleName = this.getBundleDirectory();
		String appPath = Application.getGlobalInfo().getPathBaseDir();
		if (bundleName.contains(appPath)==true) {
			// --- Get path difference ------------------------------
			bundleName = bundleName.substring(appPath.length());
			
		} else {
			// --- Take the last two sub folder --------------------- 
			String[] pathParts = bundleName.split("\\" + File.separator);
			if (pathParts.length>=2) {
				bundleName = pathParts[pathParts.length-2] + "." + pathParts[pathParts.length-1];
			} else if (pathParts.length==1) {
				bundleName = "tmp.bundle." + pathParts[pathParts.length-1];
			} else {
				bundleName = "tmp.bundle.dir" + bundleName.hashCode();
			}
		}
		
		// --- Do some final adjustments ----------------------------
		bundleName = bundleName.replace("\\", ".");
		while (bundleName.startsWith(".")) {
			bundleName = bundleName.substring(1);
		}
		while (bundleName.endsWith(".")) {
			bundleName = bundleName.substring(0, bundleName.length()-1);
		}
		return bundleName;
	}
	
	/**
	 * Sets the current bundle.
	 * @param bundle the new bundle
	 */
	private void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	/**
	 * Returns the current bundle.
	 * @return the bundle
	 */
	public Bundle getBundle() {
		return this.bundle;
	}
	
	/**
	 * Load the bundle of the current project.
	 * @return true, if successful
	 */
	public boolean installAndStartBundle() {
		boolean bundleLoaded = false;
		if (this.getBundleDirectory()!=null) {
			try {
				// --- Check if the manifest file is available ------
				if (this.isAvailableManifest()==false) {
					// --- Create the MANIFEST.MF file --------------
					BundleBuilder bBuilder = new BundleBuilder(new File(this.getBundleDirectory()), this.getSymbolicBundleName());
					bBuilder.build();
					
				}
				// --- Load the bundle ------------------------------
				String projectPath = "file:" + this.getBundleDirectory();
				this.installAndStartBundle(projectPath);
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
	 * @param bundleName the bundle name
	 * @throws BundleException the bundle exception
	 */
	public void installAndStartBundle(String locationID) throws BundleException {
//		Bundle b = bundleContext.installBundle("reference:file:./test.ServiceA");
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		this.bundle = bundleContext.installBundle(locationID);
		this.bundle.start();
		
	}
	
	/**
	 * Stop and uninstalls the current project bundle.
	 */
	public void stopAndUninstallProjectBundle() {
		try {
			this.stopAndUninstallBundle();
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Stops and uninstalls the current bundle.
	 * @throws BundleException 
	 */
	public void stopAndUninstallBundle() throws BundleException {
		if (this.getBundle()!=null) {
			this.getBundle().stop();
			this.getBundle().uninstall();
			this.setBundle(null);
		}
	}
	
}
