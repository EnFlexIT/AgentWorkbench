package org.awb.env.networkModel.adapter;

import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphElementLayout;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;

/**
 * The Class DynamicGraphElementLayout enables to visualize a {@link NetworkComponent}
 * or a {@link GraphNode} based it data properties. It can be introduced wit the definition
 * of a {@link NetworkComponentAdapter} by overriding the method {@link NetworkComponentAdapter#getDynamicGraphElementLayout()}.
 * 
 * @see NetworkComponentAdapter#getDynamicGraphElementLayout()
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class DynamicGraphElementLayout extends GraphElementLayout {

	/**
	 * Instantiates a new dynamic graph element layout.
	 *
	 * @param graphElement the graph element
	 */
	public DynamicGraphElementLayout(GraphElement graphElement) {
		super(graphElement);
	}


}
