package org.awb.env.networkModel.prototypes;

import java.util.Vector;

import org.awb.env.networkModel.networkModel.GraphNode;

public abstract class StarGraphElement extends GraphElementPrototype {

	/** The vector of outer nodes that forms the corners of the element. */
	protected Vector<GraphNode> outerNodes = new Vector<GraphNode>();

	protected Integer n;

	public StarGraphElement() {
		super();
	}

	@Override
	public boolean isDirected() {
		return false;
	}

	public Integer getN() {
		return n;
	}

}
