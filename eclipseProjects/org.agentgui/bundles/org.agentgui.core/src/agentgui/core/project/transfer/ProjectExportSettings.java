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
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import agentgui.core.config.InstallationPackageFinder;
import agentgui.core.config.InstallationPackageFinder.InstallationPackageDescription;

/**
 * The Class ProjectExportSettings.
 * @author Nils Loose 
 */
public class ProjectExportSettings implements Serializable{
	
	private static final long serialVersionUID = -1048613565176959102L;

	private File targetFile;
	
	private boolean includeInstallationPackage;
	private transient InstallationPackageDescription installationPackage;
	private String targetOS;
	
	private List<String> simSetups;
	private List<File> bundleJarFilesToInclude;
	
	private List<Path> fileExcludeList;
	
	
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
		if (installationPackage==null) {
			new InstallationPackageFinder().getInstallationPackageForOperatingSystem(this.getTargetOS());
		}
		return installationPackage;
	}
	/**
	 * Sets the installation package.
	 * @param installationPackage the new installation package
	 */
	public void setInstallationPackage(InstallationPackageDescription installationPackage) {
		this.installationPackage = installationPackage;
		if (installationPackage!=null) {
			this.setTargetOS(installationPackage.getOperatingSystem());
		} else {
			this.setTargetOS(null);
		}
	}
	
	public String getTargetOS() {
		return targetOS;
	}
	public void setTargetOS(String targetOS) {
		this.targetOS = targetOS;
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
	 * Returns the file exclude list for the export.
	 * @return the file exclude list
	 */
	public List<Path> getFileExcludeList() {
		if (fileExcludeList==null) {
			fileExcludeList = new ArrayList<>();
		}
		return fileExcludeList;
	}
	/**
	 * Sets the file exclude list for the export.
	 * @param fileExcludeList the new file exclude list
	 */
	public void setFileExcludeList(List<Path> fileExcludeList) {
		this.fileExcludeList = fileExcludeList;
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
