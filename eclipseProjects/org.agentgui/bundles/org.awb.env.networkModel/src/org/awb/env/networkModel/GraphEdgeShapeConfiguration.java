package org.awb.env.networkModel;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;

/**
 * The Class GraphEdgeShapeConfiguration serves as super class for specific edge shape configurations.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @param <T> the generic type
 */
public abstract class GraphEdgeShapeConfiguration<T extends Shape> implements Serializable {

	private static final long serialVersionUID = 7535546074273015170L;

	
	/**
	 * Returns the actual edge shape.
	 *
	 * @param graphNodeFrom the graph node from
	 * @param graphNodeTo the graph node to
	 * @return the shape
	 */
	public abstract T getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo);

	/**
	 * Allows to set the actual shape.
	 * @param shape the new shape
	 */
	public abstract void setShape(T shape);
	

	
	/**
	 * Returns a list of intermediate points between start and end point.
	 * @return the intermediate points
	 */
	public abstract List<Point2D> getIntermediatePoints();
	
	/**
	 * Allows to set all intermediate points.
	 *
	 * @param intermediatePointList the intermediate point list
	 * @return the intermediate points
	 */
	public abstract void setIntermediatePoints(List<Point2D> intermediatePointList);
	
	
	
	/**
	 * Returns the configuration as consistent string.
	 * @return the configuration as string
	 */
	public abstract String getConfigurationAsString();
	
	/**
	 * Sets the configuration from the specified string.
	 *
	 * @param stringConfiguration the configuration string 
	 * @return the graph edge shape configuration
	 */
	public abstract void setConfigurationFromString(String stringConfiguration);

	
	
	/**
	 * Returns a copy of the current configuration.
	 * @return the copy
	 */
	public abstract GraphEdgeShapeConfiguration<T> getCopy();
	
	
}
