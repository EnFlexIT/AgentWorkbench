package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JPopupMenu;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphNode;

/**
 * The Class OrthogonalConfiguration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OrthogonalConfiguration extends GraphEdgeShapeConfiguration<GeneralPath> {

	private static final long serialVersionUID = -8321682966274257112L;
	
	private GeneralPath orthLine;
	private boolean isFirstMoveInY; 
	
	
	/**
	 * Instantiates a new orthogonal configuration (default constructor).
	 */
	public OrthogonalConfiguration() { }
	
	/**
	 * Instantiates a new orthogonal configuration.
	 * @param isFirstMoveInY the indicator to move in the y-direction first
	 */
	public OrthogonalConfiguration(boolean isFirstMoveInY) { 
		this.isFirstMoveInY = isFirstMoveInY;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getShape()
	 */
	@Override
	public GeneralPath getShape() {
		if (orthLine==null) {
			orthLine = new GeneralPath();
			orthLine.moveTo(0.0, 0.0);
			orthLine.lineTo(1.0, 0.0);
		}
		return orthLine;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#setShape(java.awt.Shape)
	 */
	@Override
	public void setShape(GeneralPath shape) {
		this.orthLine = shape;
	}

	/**
	 * Checks if the orthogonal connection has at first to move in Y.
	 * @return true, if is first move in Y
	 */
	public boolean isFirstMoveInY() {
		return isFirstMoveInY;
	}
	/**
	 * Sets that the orthogonal connection has at first to move in Y (or not).
	 * @param isFirstMoveInY the new first move in Y
	 */
	public void setFirstMoveInY(boolean isFirstMoveInY) {
		this.isFirstMoveInY = isFirstMoveInY;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getIntermediatePoints()
	 */
	@Override
	public List<Point2D> getIntermediatePoints() {
		// --- Nothing to do here -----
		return null;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#setIntermediatePoints(java.util.List)
	 */
	@Override
	public void setIntermediatePoints(List<Point2D> intermediatePointList) {
		// --- Nothing to do here -----
	}


	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#addPopupMenuItems(javax.swing.JPopupMenu, org.awb.env.networkModel.GraphEdge, org.awb.env.networkModel.GraphNode)
	 */
	@Override
	public void addPopupMenuItems(JPopupMenu popupMenu, GraphEdge graphEdge, GraphNode graphNode) {
		// --- Nothing to do here ------
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#actionPerformed(java.lang.String, org.awb.env.networkModel.controller.ui.configLines.ConfiguredLinePopupPlugin, org.awb.env.networkModel.GraphEdge, org.awb.env.networkModel.GraphNode, java.awt.geom.Point2D)
	 */
	@Override
	public void actionPerformed(String actionCommand, ConfiguredLinePopupPlugin configuredLinePopupPlugin, GraphEdge currentGraphEdge, GraphNode currentGraphNode, Point2D currentMousePosition) {
		// --- Nothing to do here ------
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
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==null || !(compareObject instanceof OrthogonalConfiguration)) return false;
		
		// --- Get the compare object ---------------------
		OrthogonalConfiguration compareOrthConfig = (OrthogonalConfiguration) compareObject;
		if (compareOrthConfig.isFirstMoveInY()!=this.isFirstMoveInY()) return false;
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getCopy()
	 */
	@Override
	public GraphEdgeShapeConfiguration<GeneralPath> getCopy() {
		OrthogonalConfiguration copy = new OrthogonalConfiguration();
		copy.setShape((GeneralPath) this.getShape().clone());
		copy.setFirstMoveInY(this.isFirstMoveInY());
		return copy;
	}
	
}