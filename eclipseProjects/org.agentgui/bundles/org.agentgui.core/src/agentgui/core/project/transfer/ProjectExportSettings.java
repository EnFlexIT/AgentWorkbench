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

import java.util.List;

/**
 * The Class ProjectExportSettings.
 * @author Nils Loose 
 */
public class ProjectExportSettings {
	
	/**
	 * The Enum ProductVersion.
	 */
	public enum ProductVersion{
		Win32, Win64, Linux, MacOs;
	}
	
	/** The target path. */
	private String targetPath;
	
	/** The include product. */
	private boolean includeProduct;
	
	/** The product version. */
	private ProductVersion productVersion;
	
	/** Indicates if all simulation setups should be exported */
	private boolean includeAllSetups;
	
	/** List of simulation setups to be exported. If includeAllSetups == true, this list will be ignored. */
	private List<String> simSetups;
	
	/**
	 * Gets the target path.
	 *
	 * @return the target path
	 */
	public String getTargetPath() {
		return targetPath;
	}
	
	/**
	 * Sets the target path.
	 *
	 * @param targetPath the new target path
	 */
	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
	
	/**
	 * Checks if is include product.
	 *
	 * @return true, if is include product
	 */
	public boolean isIncludeProduct() {
		return includeProduct;
	}
	
	/**
	 * Sets the include product.
	 *
	 * @param includeProduct the new include product
	 */
	public void setIncludeProduct(boolean includeProduct) {
		this.includeProduct = includeProduct;
	}
	
	/**
	 * Gets the product version.
	 *
	 * @return the product version
	 */
	public ProductVersion getProductVersion() {
		return productVersion;
	}
	
	/**
	 * Sets the product version.
	 *
	 * @param productVersion the new product version
	 */
	public void setProductVersion(ProductVersion productVersion) {
		this.productVersion = productVersion;
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
