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

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import edu.uci.ics.jung.graph.Graph;

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
	/**
	 * Counting the total Number of nodes for generating unique node IDs 
	 */
	protected static int nodeCounter = 0;
	/**
	 * The id of the element represented by this GraphElementPrototype	
	 */
	protected String id;
	/**
	 * The type of the element represented by this GraphElementPrototype
	 */
	protected String type;
	/**
	 * Reference to the graph instance, required to get graph topology related information on nodes and edges 
	 */
	protected Graph<GraphNode, GraphEdge> graph;

	/**
	 * This method adds a GraphElementPrototype to a JUNG graph, with no connection to other graph elements.
	 * This method should be implemented, for new graph prototypes.
	 * @param graph The JUNG graph
	 * @return A HashSet containing the GraphElements representing this component, or null if adding failed  
	 */
	public abstract HashSet<GraphElement> addToGraph(Graph<GraphNode, GraphEdge> graph);
	/**
	 * This method adds a GraphElementPrototype to a JUNG graph after another element.
	 * This method is only used for importing the graph from the GraphML file.
	 * @param graph The JUNG graph
	 * @param predecessor The GraphElementPrototype's predecessor
	 * @return A HashSet containing the GraphElements representing this component, or null if adding failed
	 */
	public abstract HashSet<GraphElement> addAfter(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor);
	/**
	 * This method adds a GraphElementPrototype to a JUNG graph before another element.
	 * This method is only used for importing the graph from the GraphML file.
	 * @param graph The JUNG graph
	 * @param successor The GraphElementPrototype's successor
	 * @return A HashSet containing the GraphElements representing this component, or null if adding failed
	 */
	public abstract HashSet<GraphElement> addBefore(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype successor);
	/**
	 * This method adds a GraphElementPrototype to a JUNG graph between two other elements.
	 * This method is only used for importing the graph from the GraphML file.
	 * @param graph The JUNG graph
	 * @param predecessor The GraphElementPrototype's predecessor
	 * @param successor The GraphElementPrototype's successor
	 * @return A HashSet containing the GraphElements representing this component, or null if adding failed
	 */
	public abstract HashSet<GraphElement> addBetween(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor, GraphElementPrototype successor);
	
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
	 * This method returns a node in which a predecessor can be connected to this GraphElementPrototype 
	 * This method is only used for importing the graph from the GraphML file.
	 * @return The node
	 */
	public abstract GraphNode getFreeEntry();
	/**
	 * This method returns a node in which a successor can be connected to this GraphElementPrototype
	 * This method is only used for importing the graph from the GraphML file.
	 * @return The node
	 */
	public abstract GraphNode getFreeExit();
	/**
	 * @return True if directed, false if undirected
	 */
	public abstract boolean isDirected();
	
	//TODO think about this vs the current implementation based on the 
	//	constraint that a node can be in max two network components. (eg. Star) 
	//public abstract boolean isFreeConnectionPoint();
}
