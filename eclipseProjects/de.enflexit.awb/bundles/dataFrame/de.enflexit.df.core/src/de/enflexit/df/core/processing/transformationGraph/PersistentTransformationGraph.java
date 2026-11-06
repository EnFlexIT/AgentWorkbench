package de.enflexit.df.core.processing.transformationGraph;

import java.io.Serializable;
import java.util.ArrayList;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformationGraph.PersistentTableNode.NodeType;
import edu.uci.ics.jung.graph.Graph;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * A reduced representation of the transformation graph for persisting.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformationGraph", propOrder = {
    "dataSourceUUID",
    "tableNodes",
    "transformations"
})
public class PersistentTransformationGraph implements Serializable {
	
	private static final long serialVersionUID = 3221630447459025566L;
	
	private String dataSourceUUID;
	private ArrayList<PersistentTableNode> tableNodes;
	private ArrayList<PersistentTransformation> transformations;

	/**
	 * Gets the data source UUID.
	 * @return the data source UUID
	 */
	public String getDataSourceUUID() {
		return dataSourceUUID;
	}
	/**
	 * Sets the data source UUID.
	 * @param dataSourceUUID the new data source UUID
	 */
	public void setDataSourceUUID(String dataSourceUUID) {
		this.dataSourceUUID = dataSourceUUID;
	}

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
	 * Gets the transformations.
	 * @return the transformations
	 */
	public ArrayList<PersistentTransformation> getTransformations() {
		if (transformations==null) {
			transformations = new ArrayList<PersistentTransformation>();
		}
		return transformations;
	}
	/**
	 * Sets the transformations.
	 * @param transformations the new transformations
	 */
	public void setTransformations(ArrayList<PersistentTransformation> transformations) {
		this.transformations = transformations;
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
			
			persistentGraph.getTransformations().add(persistentEdge);
		}
		
		return persistentGraph;
	}
	
}
