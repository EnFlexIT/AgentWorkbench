package agentgui.graphEnvironment.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.jung.algorithms.layout.Layout;

import agentgui.graphEnvironment.networkModel.ComponentTypeSettings;
import agentgui.graphEnvironment.networkModel.GraphEdge;
import agentgui.graphEnvironment.networkModel.GraphNode;
import agentgui.graphEnvironment.networkModel.NetworkModel;

/**
 * Classes that should work as import components for the GraphEnvironmentController must implement this interface. 
 * 
 * @see GraphEnvironmentController
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 *
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
