package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.geom.QuadCurve2D;

import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphNode;

/**
 * The Class QuadCurveConfiguration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class QuadCurveConfiguration extends GraphEdgeShapeConfiguration<QuadCurve2D> {

	private static final long serialVersionUID = 5665040228749007437L;
	
	private QuadCurve2D quadCurve2D;
	
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
	public QuadCurve2D getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo) {
		if (quadCurve2D==null) {
			quadCurve2D = new QuadCurve2D.Double();
			// --- Set default parameter ----
			quadCurve2D.setCurve(0.0, 0.0, 0.5, 50.0, 1.0, 0.0);
		}
		return quadCurve2D;
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