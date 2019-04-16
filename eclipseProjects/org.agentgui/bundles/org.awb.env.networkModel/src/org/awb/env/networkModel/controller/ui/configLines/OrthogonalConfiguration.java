package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.geom.Line2D;

import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphNode;

/**
 * The Class OrthogonalConfiguration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OrthogonalConfiguration extends GraphEdgeShapeConfiguration<Line2D> {

	private static final long serialVersionUID = -8321682966274257112L;
	
	private Line2D line;
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getConfigurationAsString()
	 */
	@Override
	public String getConfigurationAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#setConfigurationFromString(java.lang.String)
	 */
	@Override
	public void setConfigurationFromString(String stringConfiguration) {
		// TODO Auto-generated method stub
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getShape()
	 */
	@Override
	public Line2D getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo) {
		if (line==null) {
			line = new Line2D.Double(0.0, 0.0, 1.0, 0.0);
		}
		return line;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getCopy()
	 */
	@Override
	public GraphEdgeShapeConfiguration<?> getCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}