package de.enflexit.df.core.processing.transformationGraph;

import java.awt.geom.Point2D;

import tech.tablesaw.api.Table;

/**
 * A node in the transformation graph, that represents a data table which can be input and/or output of a transformation. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class DataTableNode {
	
	private Point2D position;

	/**
	 * Gets the position.
	 * @return the position
	 */
	public Point2D getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 * @param position the new position
	 */
	public void setPosition(Point2D position) {
		this.position = position;
	}
	
	/**
	 * Gets the data table.
	 * @return the data table
	 */
	public abstract Table getDataTable();
	
	/**
	 * Gets a text for the node label.
	 * @return the label text
	 */
	public abstract String getLabelText();
	
	/**
	 * Can be used to provide a longer text, e.g. for selection lists. 
	 * @return the long text
	 */
	public abstract String getLongText();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getLongText();
	}
	
	/**
	 * Gets the data source UUID.
	 * @return the data source UUID
	 */
	public abstract String getDataSourceUUID();
	
}
