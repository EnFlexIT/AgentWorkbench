package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;

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
	
	private double xControlPoint = 0.5;
	private double yControlPoint = 50.0;
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getShape()
	 */
	@Override
	public QuadCurve2D getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo) {
		if (quadCurve2D==null) {
			quadCurve2D = new QuadCurve2D.Double();
			// --- Set default parameter ----
			quadCurve2D.setCurve(0.0, 0.0, this.xControlPoint, this.yControlPoint, 1.0, 0.0);
		}
		return quadCurve2D;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#setShape(java.awt.Shape)
	 */
	@Override
	public void setShape(QuadCurve2D shape) {
		this.quadCurve2D = shape;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getIntermediatePoints()
	 */
	@Override
	public List<Point2D> getIntermediatePoints() {
		List<Point2D> interPointList = new ArrayList<>();
		interPointList.add(new Point2D.Double(this.xControlPoint, this.yControlPoint));
		return interPointList;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getIntermediatePoints(java.util.List)
	 */
	@Override
	public void setIntermediatePoints(List<Point2D> intermediatePointList) {
		if (intermediatePointList!=null && intermediatePointList.size()>0) {
			Point2D cp = intermediatePointList.get(0);
			QuadCurve2D quadCurve = this.getShape(null, null); 
			quadCurve.setCurve(quadCurve.getP1(), cp, quadCurve.getP2());
		}
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
	public GraphEdgeShapeConfiguration<QuadCurve2D> getCopy() {
		QuadCurveConfiguration copy = new QuadCurveConfiguration();
		copy.setShape((QuadCurve2D) this.getShape(null, null).clone());
		return copy;
	}
	

}