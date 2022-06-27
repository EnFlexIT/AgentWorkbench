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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

/**
 * The Class ProjectBundleLoader.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ProjectBundleLoader {

	private boolean debug = false;
	
	private Project project;
	
	private ProjectBundleEvaluator projectBundleEvaluator;
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
	 * Returns the instance of the {@link ProjectBundleEvaluator} for this project.
	 * @return the bundle builder
	 */
	private ProjectBundleEvaluator getProjectBundleEvaluator() {
		if (projectBundleEvaluator==null) {
			projectBundleEvaluator = new ProjectBundleEvaluator(new File(this.getBundleDirectory()));
		}
		return projectBundleEvaluator;
	}
	/**
	 * Returns the bundle jars.
	 * @return the bundle jars
	 */
	public ArrayList<File> getBundleJars() {
		return this.getProjectBundleEvaluator().getBundleJars();
	}
	/**
	 * Return the file of the specified project bundle.
	 * @param bundle the bundle
	 * @return the bundle file
	 */
	public File getBundleFile(Bundle bundle) {
		return this.getProjectBundleEvaluator().getBundleFile(bundle);
	}
	/**
	 * Gets the regular jars.
	 * @return the regular jars
	 */
	public ArrayList<File> getRegularJars() {
		return this.getProjectBundleEvaluator().getRegularJars();
	}
	
	/**
	 * Returns the regular jar file resources as list model.
	 * @return the regular jar file resources
	 */
	public DefaultListModel<String> getRegularJarsListModel() {
		return this.getListModelFromFilesFound(this.getProjectBundleEvaluator().getRegularJars());
	}
	/**
	 * Returns the bundle jar file resources as list model.
	 * @return the bundle jar file resources
	 */
	public DefaultListModel<String> getBundleJarsListModel() {
		return this.getListModelFromFilesFound(this.getProjectBundleEvaluator().getBundleJars());
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
	 */
	public void installAndStartBundles() {
		
		if (this.getBundleDirectory()==null) return;
			
		// --- Evaluate the directory -------------------------------
		this.getProjectBundleEvaluator().evaluateDirectory();
		
		// --- Install the independent bundles found ----------------
		if (this.getProjectBundleEvaluator().getBundleJars()!=null) {
			this.installBundles(this.getProjectBundleEvaluator().getBundleJars());
		}
		
		// --- Finally, start all known bundles ---------------------
		this.startBundles(this.getBundleVector());
		
	}
	

	/**
	 * Installs the specified list of jar bundled and adds their bundle instances to the local bundle vector {@link #getBundleVector()}.
	 * @param fileList the list of bundle files
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
		String sbn = this.getProjectBundleEvaluator().getBundleJarsSymbolicBundleNames().get(bundleJarFile);
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
