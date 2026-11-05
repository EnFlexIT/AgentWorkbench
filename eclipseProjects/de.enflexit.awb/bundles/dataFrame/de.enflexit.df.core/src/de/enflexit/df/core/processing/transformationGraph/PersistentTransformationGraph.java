package de.enflexit.df.core.processing.transformationGraph;

import java.util.ArrayList;
import com.google.gson.Gson;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformationGraph.PersistentTableNode.NodeType;
import edu.uci.ics.jung.graph.Graph;

/**
 * A reduced representation of the transformation graph for persisting.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class PersistentTransformationGraph {
	
	private ArrayList<PersistentTableNode> tableNodes;
	private ArrayList<PersistentTransformation> transformationEdges;
	
	/**
	 * Gets the table nodes.
	 * @return the table nodes
	 */
	public ArrayList<PersistentTableNode> getTableNodes() {
		if (tableNodes==null) {
			tableNodes = new ArrayList<PersistentTableNode>();
		}
		return tableNodes;
	}
	
	/**
	 * Sets the table nodes.
	 * @param tableNodes the new table nodes
	 */
	public void setTableNodes(ArrayList<PersistentTableNode> tableNodes) {
		this.tableNodes = tableNodes;
	}
	
	/**
	 * Gets the transformation edges.
	 * @return the transformation edges
	 */
	public ArrayList<PersistentTransformation> getTransformationEdges() {
		if (transformationEdges==null) {
			transformationEdges = new ArrayList<PersistentTransformation>();
		}
		return transformationEdges;
	}
	/**
	 * Sets the transformation edges.
	 * @param transformationEdges the new transformation edges
	 */
	public void setTransformationEdges(ArrayList<PersistentTransformation> transformationEdges) {
		this.transformationEdges = transformationEdges;
	}
	
	/**
	 * Create a persistent graph instance from the provided JUNGgraph.
	 * @param transformationGraph the transformation graph
	 * @return the persistent transformation graph
	 */
	public static PersistentTransformationGraph fromJungGraph(Graph<DataTableNode, DataTransformationEdge> transformationGraph) {
		
		PersistentTransformationGraph persistentGraph = new PersistentTransformationGraph();
		
		for (DataTableNode tableNode : transformationGraph.getVertices()) {
			PersistentTableNode persistentNode = new PersistentTableNode();
			if (tableNode instanceof DataTableNodeDataSource) {
				persistentNode.setNodeType(NodeType.DATA_SOURCE);
			} else if (tableNode instanceof DataTableNodeTransformationResult) {
				persistentNode.setNodeType(NodeType.TRANSFORMATION_RESULT);
			}
			persistentNode.setPosition(tableNode.getPosition());
			persistentNode.setDataSourceUUID(tableNode.getDataSourceUUID());
			
			persistentGraph.getTableNodes().add(persistentNode);
		}
		
		for (DataTransformationEdge transformationEdge : transformationGraph.getEdges()) {
			PersistentTransformation persistentEdge = new PersistentTransformation();
			AbstractDataTransformation transformation = transformationEdge.getDataTransformation();
			persistentEdge.setTransformationName(transformation.getTransformationName());
			persistentEdge.setTransformationParameters(transformation.getTransformationParameters());
			for (DataTableNode inputNode : transformation.getInputNodes()) {
				persistentEdge.getInputNodeUUIDs().add(inputNode.getDataSourceUUID());
			}
			persistentEdge.setOutputNodeUUID(transformation.getOutputNode().getDataSourceUUID());
			
			persistentGraph.getTransformationEdges().add(persistentEdge);
		}
		
		return persistentGraph;
	}
	
	/**
	 * Converts this {@link PersistentTransformationGraph} to a JSON string.
	 * @return the JSON string
	 */
	public String toJsonString() {
		return new Gson().toJson(this);
	}
	
	/**
	 * Creates a {@link PersistentTransformationGraph} from the provided JSON string.
	 * @param jsonString the json string
	 * @return the persistent transformation graph
	 */
	public static PersistentTransformationGraph fromJsonString(String jsonString) {
		return new Gson().fromJson(jsonString, PersistentTransformationGraph.class);
	}
}
