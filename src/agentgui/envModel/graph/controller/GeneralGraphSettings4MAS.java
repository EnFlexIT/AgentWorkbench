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
package agentgui.envModel.graph.controller;

import java.util.HashMap;

import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import jade.util.leap.Serializable;

/**
 * A custom user object encapsulating the required objects which can be placed in the Project object. 
 * You can add more attributes in this class if required, but be careful to cast and use the user object properly.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 *
 */
public class GeneralGraphSettings4MAS implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 7425147528482747232L;
	
	/**
	 * The component type settings used in the {@link GraphEnvironmentController}
	 */
	private HashMap<String, ComponentTypeSettings> currentCTS = null;

	
	/**
	 * Default constructor
	 */
	public GeneralGraphSettings4MAS() {
		currentCTS = new HashMap<String, ComponentTypeSettings>();
	}
	/**
	 * Creates a clone of the current instance
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the component type settings HashMap 
	 * @return the currentCTS
	 */
	public HashMap<String, ComponentTypeSettings> getCurrentCTS() {
		return currentCTS;
	}
	/**
	 * Set the component type settings hashmap
	 * @param currentCTS the currentCTS to set
	 */
	public void setCurrentCTS(HashMap<String, ComponentTypeSettings> currentCTS) {
		this.currentCTS = currentCTS;
	}
	
}
