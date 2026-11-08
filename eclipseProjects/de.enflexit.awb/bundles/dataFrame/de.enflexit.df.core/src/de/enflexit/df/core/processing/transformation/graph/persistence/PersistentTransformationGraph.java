package de.enflexit.df.core.processing.transformation.graph.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.graph.DataTableNode;
import de.enflexit.df.core.processing.transformation.graph.DataTableNodeDataSource;
import de.enflexit.df.core.processing.transformation.graph.DataTableNodeTransformationResult;
import de.enflexit.df.core.processing.transformation.graph.DataTransformationEdge;
import de.enflexit.df.core.processing.transformation.graph.persistence.PersistentTableNode.NodeType;
import edu.uci.ics.jung.graph.Graph;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name="transformation_graphs")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformationGraph", propOrder = {
	"id",
    "dataSourceUUID",
    "tableNodes",
    "transformations"
})
public class PersistentTransformationGraph implements Serializable {
	
	private static final long serialVersionUID = 3221630447459025566L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_transformation_graph", nullable=false)
	private Integer id;
	
	@Column(name="uuid_data_source", nullable=false)
	private String dataSourceUUID;
	
	@OneToMany(
		    mappedBy = "transformationGraph",
		    targetEntity = PersistentTableNode.class,
		    cascade = CascadeType.ALL,
		    orphanRemoval = true
		)
	private List<PersistentTableNode> tableNodes;
	@OneToMany(
		    mappedBy = "transformationGraph",
		    targetEntity = PersistentTransformation.class,
		    cascade = CascadeType.ALL,
		    orphanRemoval = true
		)
	private List<PersistentTransformation> transformations;
	
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
	public List<PersistentTableNode> getTableNodes() {
		if (tableNodes==null) {
			tableNodes = new ArrayList<PersistentTableNode>();
		}
		return tableNodes;
	}
	
	/**
	 * Sets the table nodes.
	 * @param tableNodes the new table nodes
	 */
	public void setTableNodes(List<PersistentTableNode> tableNodes) {
		this.tableNodes = tableNodes;
	}
	
	/**
	 * Gets the transformations.
	 * @return the transformations
	 */
	public List<PersistentTransformation> getTransformations() {
		if (transformations==null) {
			transformations = new ArrayList<PersistentTransformation>();
		}
		return transformations;
	}
	/**
	 * Sets the transformations.
	 * @param transformations the new transformations
	 */
	public void setTransformations(List<PersistentTransformation> transformations) {
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
			persistentNode.setTransformationGraph(persistentGraph);
			persistentNode.setPosition(tableNode.getPosition());
			persistentNode.setDataSourceUUID(tableNode.getDataSourceUUID());
			
			persistentGraph.getTableNodes().add(persistentNode);
		}
		
		for (DataTransformationEdge transformationEdge : transformationGraph.getEdges()) {
			PersistentTransformation persistentEdge = new PersistentTransformation();
			AbstractDataTransformation transformation = transformationEdge.getDataTransformation();
			persistentEdge.setTransformationGraph(persistentGraph);
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
