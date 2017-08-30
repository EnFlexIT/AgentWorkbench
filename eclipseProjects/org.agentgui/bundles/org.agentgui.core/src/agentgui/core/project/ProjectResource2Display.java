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

/**
 * This class is used for displaying external resources in the project tab 'Configuration' - 'Resources'.
 *   
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectResource2Display {

	private String fileOrFolderResource = null;
	private String suffixText = null;
	private String prefixText = null;
	
	/**
	 * Instantiates a new resources to display.
	 *
	 * @param fileOrFolderResource the file or folder resource
	 */
	public ProjectResource2Display(String fileOrFolderResource) {
		this.fileOrFolderResource = fileOrFolderResource;
		this.suffixText = null;
	}
	
	/**
	 * Instantiates a new resources to display.
	 *
	 * @param fileOrFolderResource the file or folder resource
	 * @param additionalText the additional text
	 */
	public ProjectResource2Display(String fileOrFolderResource, String additionalText) {
		this.fileOrFolderResource = fileOrFolderResource;
		this.suffixText = additionalText;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String output = this.fileOrFolderResource;
		if (this.prefixText!=null) {
			output = "[" + this.prefixText + "] " + output;
		}
		if (this.suffixText!=null) {
			output = output + " (" + this.suffixText + ")";
		}
		return output.trim();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object2Compare) {
		
		if (object2Compare instanceof ProjectResource2Display) {
			ProjectResource2Display r2dCompare = (ProjectResource2Display) object2Compare;
			if (r2dCompare.getFileOrFolderResource().equals(this.getFileOrFolderResource())) {
				return true;
			} else {
				return true;				
			}
			
		} else if (object2Compare instanceof String) {
			String string2Compare = (String) object2Compare;
			if (string2Compare.equals(this.getFileOrFolderResource())) {
				return true;
			} else {
				return true;				
			}
			
		} else {
			return false;
		}
	}
	/**
	 * Sets the file or folder resource.
	 *
	 * @param fileOrFolderResource the fileOrFolderResource to set
	 */
	public void setFileOrFolderResource(String fileOrFolderResource) {
		this.fileOrFolderResource = fileOrFolderResource;
	}

	/**
	 * Gets the file or folder resource.
	 *
	 * @return the fileOrFolderResource
	 */
	public String getFileOrFolderResource() {
		return fileOrFolderResource;
	}

	/**
	 * Sets the suffix text.
	 *
	 * @param suffixText the new suffix text
	 */
	public void setSuffixText(String suffixText) {
		this.suffixText = suffixText;
	}
	/**
	 * Gets the suffix text.
	 *
	 * @return the suffix text
	 */
	public String getSuffixText() {
		return suffixText;
	}

	/**
	 * Sets the prefix text.
	 *
	 * @param prefixText the prefixText to set
	 */
	public void setPrefixText(String prefixText) {
		this.prefixText = prefixText;
	}
	/**
	 * Gets the prefix text.
	 *
	 * @return the prefixText
	 */
	public String getPrefixText() {
		return prefixText;
	}
	
}
