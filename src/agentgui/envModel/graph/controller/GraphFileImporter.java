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

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.jung.algorithms.layout.Layout;

import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * Classes that should work as import components for the GraphEnvironmentController must implement this interface. 
 * 
 * @see GraphEnvironmentController
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 */
public abstract class GraphFileImporter {
	
	/**
	 * The component type definitions
	 */
	protected HashMap<String, ComponentTypeSettings> componentTypeSettings = null;
	
	/**
	 * Constructor
	 * @param componentTypeSettings	The component type definitions
	 */
	public GraphFileImporter(HashMap<String, ComponentTypeSettings> componentTypeSettings){
		this.componentTypeSettings = componentTypeSettings;
	}
	
	/**
	 * Initialize the node positions according to a specified layout
	 * @param network The NetworkModel containing the graph
	 * @param layout The initial layout
	 */
	public void initPosition(NetworkModel network, Layout<GraphNode, GraphEdge> layout){
		Iterator<GraphNode> nodes = network.getGraph().getVertices().iterator();
		while(nodes.hasNext()){
			GraphNode node = nodes.next();
			node.setPosition(layout.transform(node));
		}
		
	}
	
	/**
	 * This method loads the graph graph from the file and translates it into a JUNG graph. 
	 * @param graphFile The file containing the graph definition.
	 * @return The JUNG graph.
	 */
	public abstract NetworkModel importGraphFromFile(File graphFile);
	/**
	 * Returns the extension of the file type the GraphFileLoader can handle
	 * @return The file extension
	 */
	public abstract String getGraphFileExtension();
	/**
	 * Returns a type string used for GraphFileLoader selection
	 * @return The type String
	 */
	public abstract String getTypeString();
}
