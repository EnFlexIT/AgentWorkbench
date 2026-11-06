package de.enflexit.df.core.processing.transformationGraph;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * This class holds all information that is required for saving and loading {@link DataTableNode}s.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class PersistentTableNode implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum NodeType {
		DATA_SOURCE, TRANSFORMATION_RESULT
	}

	private String dataSourceUUID;
	private NodeType nodeType;
	private double posX;
	private double posY;

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
