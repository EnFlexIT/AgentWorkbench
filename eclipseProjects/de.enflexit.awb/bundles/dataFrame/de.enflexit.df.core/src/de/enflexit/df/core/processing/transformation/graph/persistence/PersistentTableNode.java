package de.enflexit.df.core.processing.transformation.graph.persistence;

import java.awt.geom.Point2D;
import java.io.Serializable;

import de.enflexit.df.core.processing.transformation.graph.DataTableNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * This class holds all information that is required for saving and loading {@link DataTableNode}s.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
@Entity
@Table(name="data_table_node")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersistentTableNode implements Serializable {
	
	private static final long serialVersionUID = -690331646007738930L;
	
	public enum NodeType {
		DATA_SOURCE, TRANSFORMATION_RESULT
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_table_node", nullable=false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = PersistentTransformationGraph.class)
	@JoinColumn(name = "id_transformation_graph", nullable = false)
	@XmlTransient
	private PersistentTransformationGraph transformationGraph;
	
	@Column(name="uuid_data_source", nullable=false)
	private String dataSourceUUID;
	
	@Column(name="node_type", nullable=false)
	@Enumerated(EnumType.STRING)
	private NodeType nodeType;
	
	@Column(name="pos_x", nullable=false)
	private double posX;
	@Column(name="pos_y", nullable=false)
	private double posY;

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
	 * Gets the transformation graph.
	 * @return the transformation graph
	 */
	public PersistentTransformationGraph getTransformationGraph() {
		return transformationGraph;
	}

	/**
	 * Sets the transformation graph.
	 * @param transformationGraph the new transformation graph
	 */
	public void setTransformationGraph(PersistentTransformationGraph transformationGraph) {
		this.transformationGraph = transformationGraph;
	}

	/**
	 * Sets the position.
	 * @param position the new position
	 */
	public void setPosition(Point2D position) {
		this.setPosX(position.getX());
		this.setPosY(position.getY());
	}
	
	/**
	 * Gets the pos X.
	 * @return the pos X
	 */
	public double getPosX() {
		return posX;
	}
	
	/**
	 * Sets the pos X.
	 * @param posX the new pos X
	 */
	public void setPosX(double posX) {
		this.posX = posX;
	}
	
	/**
	 * Gets the pos Y.
	 * @return the pos Y
	 */
	public double getPosY() {
		return posY;
	}
	/**
	 * Sets the pos Y.
	 * @param posY the new pos Y
	 */
	public void setPosY(double posY) {
		this.posY = posY;
	}
	

	/**
	 * Gets the node type.
	 * @return the node type
	 */
	public NodeType getNodeType() {
		return nodeType;
	}
	/**
	 * Sets the node type.
	 * @param nodeType the new node type
	 */
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
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
	
}
