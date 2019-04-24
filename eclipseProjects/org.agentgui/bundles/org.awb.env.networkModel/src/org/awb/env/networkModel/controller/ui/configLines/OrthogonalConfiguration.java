package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.List;

import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphNode;

/**
 * The Class OrthogonalConfiguration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OrthogonalConfiguration extends GraphEdgeShapeConfiguration<GeneralPath> {

	private static final long serialVersionUID = -8321682966274257112L;
	
	private GeneralPath line;
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getShape()
	 */
	@Override
	public GeneralPath getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo) {
		if (line==null) {
			line = new GeneralPath();
			line.moveTo(0.0, 0.0);
			line.lineTo(1.0, 0.0);
		}
		return line;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#setShape(java.awt.Shape)
	 */
	@Override
	public void setShape(GeneralPath shape) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getIntermediatePoints()
	 */
	@Override
	public List<Point2D> getIntermediatePoints() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#setIntermediatePoints(java.util.List)
	 */
	@Override
	public void setIntermediatePoints(List<Point2D> intermediatePointList) {
		// TODO Auto-generated method stub
		
	}
	
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
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getCopy()
	 */
	@Override
	public GraphEdgeShapeConfiguration<GeneralPath> getCopy() {
		// TODO Auto-generated method stub
		return null;
	}
	
}