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
package agentgui.envModel.graph.components;

import agentgui.envModel.graph.controller.AddComponentDialog;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;

/**
 * The Class ComponentTypeListElement is used in the dialog {@link AddComponentDialog}
 * in order to display the available, user-defined network components in the according
 * component list. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ComponentTypeListElement {

	private String componentName = null;
	private ComponentTypeSettings componentTypeSettings; 
	
	/**
	 * Instantiates a new component type list element.
	 *
	 * @param componentName the component name
	 * @param componentTypeSettings the component type settings
	 */
	public ComponentTypeListElement(String componentName, ComponentTypeSettings componentTypeSettings) {
		this.setComponentName(componentName);
		this.setComponentTypeSettings(componentTypeSettings);
	}

	@Override
	public String toString() {
		String output = "(" + this.componentTypeSettings.getDomain() + ") " + this.componentName;
		if (this.componentTypeSettings.getGraphPrototype()==null) {
			output = "[M i s s i n g   GraphPrototype]: " + output;
		}
		return output;
	}

	/**
	 * Gets the domain.
	 * @return the domain
	 */
	public String getDomain() {
		return this.componentTypeSettings.getDomain();
	}
	
	/**
	 * Sets the component name.
	 * @param componentName the new component name
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	/**
	 * Gets the component name.
	 * @return the component name
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * Sets the component type settings.
	 * @param componentTypeSettings the new component type settings
	 */
	public void setComponentTypeSettings(ComponentTypeSettings componentTypeSettings) {
		this.componentTypeSettings = componentTypeSettings;
	}
	/**
	 * Gets the component type settings.
	 * @return the component type settings
	 */
	public ComponentTypeSettings getComponentTypeSettings() {
		return componentTypeSettings;
	}
	
}
