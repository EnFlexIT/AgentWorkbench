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
package agentgui.envModel.graph.networkModel;

import java.io.Serializable;

/**
 * The Class DomainSettings.
 */
public class DomainSettings implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 469880829860930598L;
	
	/** The node configuration stored as ComponentTypeSettings. */
	private ComponentTypeSettings nodeConfiguration = null; 
	
	
	/**
	 * Instantiates a new domain settings.
	 */
	public DomainSettings() {
	}
	
	/**
	 * Sets the node configuration.
	 * @param nodeConfiguration the new node configuration
	 */
	public void setNodeConfiguration(ComponentTypeSettings nodeConfiguration) {
		this.nodeConfiguration = nodeConfiguration;
	}
	/**
	 * Returns the node configuration.
	 * @return the node configuration
	 */
	public ComponentTypeSettings getNodeConfiguration() {
		return nodeConfiguration;
	}
	
	
}
