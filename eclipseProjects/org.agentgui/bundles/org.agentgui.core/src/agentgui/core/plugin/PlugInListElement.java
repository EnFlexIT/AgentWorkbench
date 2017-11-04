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
package agentgui.core.plugin;

import org.agentgui.gui.swing.project.ProjectResources;

/**
 * This class is used as a displayable element in JLists, as for example
 * in the project tab {@link ProjectResources}.
 * 
 * @see ProjectResources
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PlugInListElement {

	private String plugInClassReference = null;
	private String plugInName = null;
	
	/**
	 * Constructor that needs the Name of a PlugIn and the
	 * reference to its class.
	 *
	 * @param name the name
	 * @param classReference2PlugIn the ref
	 */
	public PlugInListElement(String name, String classReference2PlugIn) {
		this.plugInClassReference = classReference2PlugIn;
		this.plugInName = name;
	}

	/**
	 * Returns the String representation for a plugIn.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return plugInName + " [" + plugInClassReference + "]"; 
	}
	
	/**
	 * Will do the object comparison.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		try {
			PlugInListElement pile = (PlugInListElement) obj;
			if (this.getPlugInName().equals(pile.getPlugInName()) && this.getPlugInClassReference().equals(pile.getPlugInClassReference())) {
				return true;
			}
			
		} catch (Exception err) {
			err.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Sets the plug in class reference.
	 *
	 * @param classReference2PlugIn the plugInClassReference to set
	 */
	public void setPlugInClassReference(String classReference2PlugIn) {
		this.plugInClassReference = classReference2PlugIn;
	}

	/**
	 * Gets the plug in class reference.
	 *
	 * @return the plugInClassReference
	 */
	public String getPlugInClassReference() {
		return plugInClassReference;
	}

	/**
	 * Sets the plug in name.
	 *
	 * @param plugInName the plugInName to set
	 */
	public void setPlugInName(String plugInName) {
		this.plugInName = plugInName;
	}

	/**
	 * Gets the plug in name.
	 *
	 * @return the plugInName
	 */
	public String getPlugInName() {
		return plugInName;
	}
}
