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
package agentgui.core.project.transfer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import agentgui.core.config.InstallationPackageFinder.InstallationPackageDescription;

/**
 * The Class ProjectExportSettings.
 * @author Nils Loose 
 */
public class ProjectExportSettings {
	
	private File targetFile;
	
	private boolean includeInstallationPackage;
	private InstallationPackageDescription installationPackage;
	
	private boolean includeAllSetups;
	private List<String> simSetups;
	private List<File> bundleJarFilesToInclude;
	
	
	/**
	 * Returns the target file.
	 * @return the target path
	 */
	public File getTargetFile() {
		return targetFile;
	}
	/**
	 * Sets the target file.
	 * @param targetPath the new target path
	 */
	public void setTargetFile(File targetPath) {
		this.targetFile = targetPath;
	}
	
	/**
	 * Checks if is include product.
	 * @return true, if is include product
	 */
	public boolean isIncludeInstallationPackage() {
		return includeInstallationPackage;
	}
	/**
	 * Sets the include product.
	 * @param includeProduct the new include product
	 */
	public void setIncludeInstallationPackage(boolean includeProduct) {
		this.includeInstallationPackage = includeProduct;
	}
	
	/**
	 * Returns the installation package.
	 * @return the installation package
	 */
	public InstallationPackageDescription getInstallationPackage() {
		return installationPackage;
	}
	/**
	 * Sets the installation package.
	 * @param productVersion the new installation package
	 */
	public void setInstallationPackage(InstallationPackageDescription productVersion) {
		this.installationPackage = productVersion;
	}
	
	/**
	 * Checks if is include all setups.
	 * @return true, if is include all setups
	 */
	public boolean isIncludeAllSetups() {
		return includeAllSetups;
	}
	/**
	 * Sets the include all setups.
	 * @param includeAllSetups the new include all setups
	 */
	public void setIncludeAllSetups(boolean includeAllSetups) {
		this.includeAllSetups = includeAllSetups;
	}
	
	/**
	 * Returns the simulation setup names.
	 * @return the simulation setup names
	 */
	public List<String> getSimSetups() {
		if (simSetups==null) {
			simSetups = new ArrayList<>();
		}
		return simSetups;
	}
	/**
	 * Sets the simulation setup name to exports.
	 * @param simSetups the new simulation setup names 
	 */
	public void setSimSetups(List<String> simSetups) {
		this.simSetups = simSetups;
	}

	
	/**
	 * Gets the bundle jar files to include.
	 * @return the bundle jar files to include
	 */
	public List<File> getBundleJarFilesToInclude() {
		if (bundleJarFilesToInclude==null) {
			bundleJarFilesToInclude = new ArrayList<>();
		}
		return bundleJarFilesToInclude;
	}
	/**
	 * Sets the bundle jar files to include.
	 * @param bundleJarFilesToInclude the new bundle jar files to include
	 */
	public void setBundleJarFilesToInclude(List<File> bundleJarFilesToInclude) {
		this.bundleJarFilesToInclude = bundleJarFilesToInclude;
	}
	
}
