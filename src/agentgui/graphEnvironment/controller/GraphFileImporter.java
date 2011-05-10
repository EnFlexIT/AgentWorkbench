package agentgui.graphEnvironment.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.jung.algorithms.layout.Layout;

import agentgui.graphEnvironment.environmentModel.GraphEdge;
import agentgui.graphEnvironment.environmentModel.ComponentTypeSettings;
import agentgui.graphEnvironment.environmentModel.GraphNode;
import agentgui.graphEnvironment.environmentModel.NetworkModel;

/**
 * Classes that should work as import components for the GraphEnvironmentController must implement this interface. 
 * @author Nils
 *
 */
public abstract class GraphFileImporter {
	
	protected HashMap<String, ComponentTypeSettings> componentTypeSettings = null;
	
	public GraphFileImporter(HashMap<String, ComponentTypeSettings> componentTypeSettings){
		this.componentTypeSettings = componentTypeSettings;
	}
	
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
