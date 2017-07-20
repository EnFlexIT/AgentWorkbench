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
package agentgui.envModel.graph.prototypes;

import java.util.HashSet;

import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * Abstract super class for GraphElementPrototypes.
 * A GraphElementPrototype defines how a component in a graph/network environment will be represented
 * by nodes and edges in the graph defining the environment model. GraphElementPrototypes are used
 * during graph import only. Later the nodes and edges of the GridModel will be stored directly in
 * GraphML.
 *     
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 *
 */
public abstract class GraphElementPrototype {
	
	/** The id of the element represented by this GraphElementPrototype */
	protected String id;
	
	/** The type of the element represented by this GraphElementPrototype */
	protected String type;
	
	/**
	 * This method adds a GraphElementPrototype to a JUNG graph, with no connection to other graph elements.
	 * This method should be implemented, for new graph prototypes.
	 * @param networkModel the network model
	 * @return A HashSet containing the GraphElements representing this component, or null if adding failed
	 */
	public abstract HashSet<GraphElement> addToGraph(NetworkModel networkModel);
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return True if directed, false if undirected
	 */
	public abstract boolean isDirected();
	
}
