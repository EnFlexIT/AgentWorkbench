package agentgui.graphEnvironment.controller.yedGraphml;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.jung.graph.Graph;
import agentgui.graphEnvironment.controller.GraphFileImporter;
import agentgui.graphEnvironment.controller.GraphEdge;
import agentgui.graphEnvironment.controller.GridModel;

public class YedGraphMLFileImporter implements GraphFileImporter {
	
	private String fileTypeExtension = "graphml";
	private String fileTypeDescription = "yEd GraphML";
	
	private Graph<TempNode, Object> tempGraph;
	private GridModel gridModel;
	
	@Override
	public GridModel loadGraphFromFile(File graphFile) {
		
		YedGraphMLParser parser = new YedGraphMLParser();
		Vector<TempNode> startNodesList = null;
		
		tempGraph = parser.getGraph(graphFile);
		if (tempGraph==null) {
			return null;
		} else {
			Iterator<TempNode> nodes = tempGraph.getVertices().iterator();
			startNodesList = new Vector<TempNode>();
			while(nodes.hasNext()){
				TempNode node = nodes.next();
				if(tempGraph.inDegree(node) == 0){
					startNodesList.add(node);
				}
			}	
		}
		
		if (startNodesList!=null) {
			Iterator<TempNode> startNodes = startNodesList.iterator();
			gridModel = new GridModel();
			while(startNodes.hasNext()){
				TempNode startNode = startNodes.next();
				addNode(startNode, null);
			}
			gridModel.fixDirections();	
		}		
		return gridModel;
	}
	
	private void addNode(TempNode node, String predecessorID){
		// Check if one of the nodes successors already exists in the GridModel
		GraphEdge existingSuccessor = null;
		Iterator<TempNode> successors = tempGraph.getSuccessors(node).iterator();
		while(successors.hasNext()){
			TempNode successor = successors.next();
			existingSuccessor = gridModel.getComponent(successor.getId());
		}
		
		// Generate new Component
		GraphEdge newComponent = new GraphEdge(node.getId(), node.getType());
		
		// No predecessor, no existing successor
		if(predecessorID == null && existingSuccessor == null){
			gridModel.addSimpleComponent(newComponent);
		// Predecessor, no existing successor -> add after predecessor
		}else if(predecessorID != null && existingSuccessor == null){
			gridModel.addSimpleComponentAfter(newComponent, predecessorID);
		// No predecessor, existing successor -> add before successor
		}else if(predecessorID == null && existingSuccessor != null){
			gridModel.addSimpleComponentBefore(newComponent, existingSuccessor.id());
		// Predecessor, existing successor -> add between both
		}else{
			gridModel.addSimpleComponentBetween(newComponent, predecessorID, existingSuccessor.id());
		}
		
		// Call recursively for successor nodes
		// New iterator 
		successors = tempGraph.getSuccessors(node).iterator();
		while(successors.hasNext()){
			TempNode successor = successors.next();
			addNode(successor, node.getId());
		}
	}
	
	

	@Override
	public String getGraphFileExtension() {
		return fileTypeExtension;
	}

	@Override
	public String getTypeString() {
		return fileTypeDescription;
	}

}
