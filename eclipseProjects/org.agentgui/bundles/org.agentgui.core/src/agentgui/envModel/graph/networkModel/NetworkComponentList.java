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

import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper class encapsulating a HashMap of NetworkComponents. Its' only 
 * purpose is saving a list of NetworkComponents via JAXB.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 */
@XmlRootElement
public class NetworkComponentList {

	private TreeMap<String, NetworkComponent> componentList;
	
	
	/**
	 * Default constructor, required for JAXB
	 */
	public NetworkComponentList() { }
	/**
	 * Constructor
	 * @param componentList The componentList
	 */
	public NetworkComponentList(TreeMap<String, NetworkComponent> componentList){
		this.componentList = componentList;
	}

	
	/**
	 * Gets the component list.
	 * @return the componentList
	 */
	public TreeMap<String, NetworkComponent> getComponentList() {
		return componentList;
	}
	/**
	 * Sets the component list.
	 * @param componentList the componentList to set
	 */
	public void setComponentList(TreeMap<String, NetworkComponent> componentList) {
		this.componentList = componentList;
	}
	
}
