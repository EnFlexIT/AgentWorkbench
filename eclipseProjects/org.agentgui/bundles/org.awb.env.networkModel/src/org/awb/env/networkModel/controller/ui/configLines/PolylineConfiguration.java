package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphNode;

/**
 * The Class PolyLineConfiguration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PolylineConfiguration extends GraphEdgeShapeConfiguration<GeneralPath> {

	private static final long serialVersionUID = -4470264048650428232L;
	
	private GeneralPath generalPath;
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getShape()
	 */
	@Override
	public GeneralPath getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo) {
		if (generalPath==null) {
			generalPath = new GeneralPath();
			// --- Set default parameter ----
			generalPath.moveTo(0.0d, 0.0d);
			generalPath.lineTo(0.2d, 0.3d);
			generalPath.lineTo(0.3d, 50.3d);
			generalPath.lineTo(0.5d, -50.5d);
			generalPath.lineTo(0.8d, 40.3d);
			generalPath.lineTo(1.0d, 0.0d);
		}
		return generalPath;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#setShape(java.awt.Shape)
	 */
	@Override
	public void setShape(GeneralPath shape) {
		this.generalPath = shape;
	}

	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getIntermediatePoints()
	 */
	@Override
	public List<Point2D> getIntermediatePoints() {
		
		List<Point2D> intPointList = new ArrayList<>();
		
		double[] coords = new double[6];
		for (PathIterator pIterator = this.getShape(null, null).getPathIterator(null); !pIterator.isDone(); pIterator.next()) {

			pIterator.currentSegment(coords);
			double xCoord = coords[0];
			double yCoord = coords[1];
			
			if (! (xCoord==0.0 && yCoord==0.0 || xCoord==1.0 && yCoord==0.0)) {
				intPointList.add(new Point2D.Double(xCoord, yCoord)); 
			}
		}
		return intPointList;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#setIntermediatePoints(java.util.List)
	 */
	@Override
	public void setIntermediatePoints(List<Point2D> intermediatePointList) {
		
		GeneralPath gp = new GeneralPath();
		gp.moveTo(0.0d,  0.0d);
		
		for (int i = 0; i < intermediatePointList.size(); i++) {
			Point2D intNodePoint = intermediatePointList.get(i);
			gp.lineTo(intNodePoint.getX(), intNodePoint.getY());
		}
		gp.lineTo(1.0d, 0.0d);
		this.setShape(gp);
		
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