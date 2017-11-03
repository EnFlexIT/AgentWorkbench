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
	
	/**
	 * Gets the target path.
	 *
	 * @return the target path
	 */
	public File getTargetFile() {
		return targetFile;
	}
	
	/**
	 * Sets the target path.
	 *
	 * @param targetPath the new target path
	 */
	public void setTargetFile(File targetPath) {
		this.targetFile = targetPath;
	}
	
	/**
	 * Checks if is include product.
	 *
	 * @return true, if is include product
	 */
	public boolean isIncludeInstallationPackage() {
		return includeInstallationPackage;
	}
	
	/**
	 * Sets the include product.
	 *
	 * @param includeProduct the new include product
	 */
	public void setIncludeInstallationPackage(boolean includeProduct) {
		this.includeInstallationPackage = includeProduct;
	}
	
	/**
	 * Gets the product version.
	 *
	 * @return the product version
	 */
	public InstallationPackageDescription getInstallationPackage() {
		return installationPackage;
	}
	
	/**
	 * Sets the product version.
	 *
	 * @param productVersion the new product version
	 */
	public void setInstallationPackage(InstallationPackageDescription productVersion) {
		this.installationPackage = productVersion;
	}
	
	/**
	 * Checks if is include all setups.
	 *
	 * @return true, if is include all setups
	 */
	public boolean isIncludeAllSetups() {
		return includeAllSetups;
	}
	
	/**
	 * Sets the include all setups.
	 *
	 * @param includeAllSetups the new include all setups
	 */
	public void setIncludeAllSetups(boolean includeAllSetups) {
		this.includeAllSetups = includeAllSetups;
	}
	
	/**
	 * Gets the sim setups.
	 *
	 * @return the sim setups
	 */
	public List<String> getSimSetups() {
		return simSetups;
	}
	
	/**
	 * Sets the sim setups.
	 *
	 * @param simSetups the new sim setups
	 */
	public void setSimSetups(List<String> simSetups) {
		this.simSetups = simSetups;
	}
	
}
