package de.enflexit.df.core.processing.transformation.graph.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.graph.DataTableNode;
import de.enflexit.df.core.processing.transformation.graph.DataTableNodeDataSource;
import de.enflexit.df.core.processing.transformation.graph.DataTableNodeTransformationResult;
import de.enflexit.df.core.processing.transformation.graph.DataTransformationEdge;
import de.enflexit.df.core.processing.transformation.graph.persistence.PersistentTransformationNode.NodeType;
import edu.uci.ics.jung.graph.Graph;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * A reduced representation of the transformation graph for persisting.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */

@Entity
@Table(name="data_transformation_graphs")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformationGraph", propOrder = {
	"id",
    "dataSourceUUID",
    "tableNodes",
    "transformationEdges"
})
public class PersistentTransformationGraph implements Serializable {
	
	private static final long serialVersionUID = 3221630447459025566L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_transformation_graph", nullable=false)
	private Integer id;
	
	@Column(name="uuid_data_source", nullable=false)
	private String dataSourceUUID;
	
	@ElementCollection
	@CollectionTable(name = "data_transformation_node", joinColumns = @JoinColumn(name = "id_transformation_graph"))
	@OrderColumn(name = "node_order")
	private List<PersistentTransformationNode> tableNodes;
	
	@ElementCollection
	@CollectionTable(name = "data_transformation_edge", joinColumns = @JoinColumn(name = "id_transformation_graph"))
	@OrderColumn(name = "edge_order")
	private List<PersistentTransformationEdge> transformationEdges;
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * Sets the id.
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
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
	public List<PersistentTransformationNode> getTableNodes() {
		if (tableNodes==null) {
			tableNodes = new ArrayList<PersistentTransformationNode>();
		}
		return tableNodes;
	}
	
	/**
	 * Sets the table nodes.
	 * @param tableNodes the new table nodes
	 */
	public void setTableNodes(List<PersistentTransformationNode> tableNodes) {
		this.tableNodes = tableNodes;
	}
	
	/**
	 * Gets the transformations.
	 * @return the transformations
	 */
	public List<PersistentTransformationEdge> getTransformationEdges() {
		if (transformationEdges==null) {
			transformationEdges = new ArrayList<PersistentTransformationEdge>();
		}
		return transformationEdges;
	}
	/**
	 * Sets the transformations.
	 * @param transformationEdges the new transformations
	 */
	public void setTransformationEdges(List<PersistentTransformationEdge> transformationEdges) {
		this.transformationEdges = transformationEdges;
	}
	
	/**
	 * Create a persistent graph instance from the provided JUNGgraph.
	 * @param transformationGraph the transformation graph
	 * @return the persistent transformation graph
	 */
	public static PersistentTransformationGraph fromJungGraph(Graph<DataTableNode, DataTransformationEdge> transformationGraph) {
		
		PersistentTransformationGraph persistentGraph = new PersistentTransformationGraph();
		
		// --- Process table nodes ----------------------------------
		for (DataTableNode tableNode : transformationGraph.getVertices()) {
			PersistentTransformationNode persistentNode = new PersistentTransformationNode();
			
			// --- Determine node type ------------------------------
			if (tableNode instanceof DataTableNodeDataSource) {
				persistentNode.setNodeType(NodeType.DATA_SOURCE);
			} else if (tableNode instanceof DataTableNodeTransformationResult) {
				persistentNode.setNodeType(NodeType.TRANSFORMATION_RESULT);
			}
			
			persistentNode.setPosition(tableNode.getPosition());
			persistentNode.setDataSourceUUID(tableNode.getDataSourceUUID());
			
			persistentGraph.getTableNodes().add(persistentNode);
		}

		// --- Process transformation edges -------------------------
		ArrayList<AbstractDataTransformation> processedTransformations = new ArrayList<AbstractDataTransformation>();
		for (DataTransformationEdge transformationEdge : transformationGraph.getEdges()) {
			AbstractDataTransformation transformation = transformationEdge.getDataTransformation();
			
			// --- For transformations with multiple edges, skip the later ones
			if (processedTransformations.contains(transformation)) continue;
			
			// --- Collect transformation details -------------------
			PersistentTransformationEdge persistentEdge = new PersistentTransformationEdge();
			persistentEdge.setTransformationName(transformation.getTransformationName());
			persistentEdge.setTransformationParameters(transformation.getTransformationParameters());
			persistentEdge.setOutputNodeUUID(transformation.getOutputNode().getDataSourceUUID());
			
			// --- Collect all input nodes --------------------------
			for (DataTableNode inputNode : transformation.getInputNodes()) {
				persistentEdge.getInputNodeUUIDs().add(inputNode.getDataSourceUUID());
			}
			
			persistentGraph.getTransformationEdges().add(persistentEdge);
			
			// --- Remember this transformation was already processed 
			processedTransformations.add(transformation);
		}
		
		return persistentGraph;
	}
	
}
