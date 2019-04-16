package org.awb.env.networkModel;

import java.awt.Shape;
import java.io.Serializable;

/**
 * The Class GraphEdgeShapeConfiguration serves as super class for specific edge shape configurationss.
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
	 * Returns a copy of the current configuration.
	 * @return the copy
	 */
	public abstract GraphEdgeShapeConfiguration<?> getCopy();
	
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
	
}
