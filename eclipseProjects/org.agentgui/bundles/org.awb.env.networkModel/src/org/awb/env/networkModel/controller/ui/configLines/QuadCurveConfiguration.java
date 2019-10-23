package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import agentgui.core.application.Language;

/**
 * The Class QuadCurveConfiguration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class QuadCurveConfiguration extends GraphEdgeShapeConfiguration<QuadCurve2D> {

	private static final long serialVersionUID = 5665040228749007437L;
	
	private static final String ACTION_CENTER_INTERMEDIATE = "CenterIntermediate";
	
	private QuadCurve2D quadCurve2D;
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getShape()
	 */
	@Override
	public QuadCurve2D getShape() {
		if (quadCurve2D==null) {
			quadCurve2D = new QuadCurve2D.Double(0.0, 0.0, 0.5, 50.0, 1.0, 0.0);
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
		QuadCurve2D quadCurve = this.getShape();
		List<Point2D> interPointList = new ArrayList<>();
		interPointList.add(new Point2D.Double(quadCurve.getCtrlX(), quadCurve.getCtrlY()));
		return interPointList;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getIntermediatePoints(java.util.List)
	 */
	@Override
	public void setIntermediatePoints(List<Point2D> intermediatePointList) {
		if (intermediatePointList!=null && intermediatePointList.size()>0) {
			Point2D cp = intermediatePointList.get(0);
			this.setIntermediatePoint(cp);
		}
	}
	/**
	 * Sets the intermediate (control) point.
	 * @param controlPoint the new intermediate point
	 */
	private void setIntermediatePoint(Point2D controlPoint) {
		if (controlPoint!=null) {
			QuadCurve2D quadCurve = this.getShape(); 
			quadCurve.setCurve(quadCurve.getP1(), controlPoint, quadCurve.getP2());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#isUseAbsoluteCoordinates()
	 */
	@Override
	public boolean isUseAbsoluteCoordinates() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getConfigurationAsString()
	 */
	@Override
	protected String getConfigurationAsStringInternal() {
		return GraphNode.getPositionAsString(this.getShape().getCtrlPt());
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#setConfigurationFromString(java.lang.String)
	 */
	@Override
	protected void setConfigurationFromStringInternal(String stringConfiguration) {
		this.setIntermediatePoint(GraphNode.getPositionFromString(stringConfiguration));
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==null || !(compareObject instanceof QuadCurveConfiguration)) return false;
		
		// --- Get the compare object ---------------------
		QuadCurveConfiguration compareCurveConfig = (QuadCurveConfiguration) compareObject;
		// --- Get intermediate points --------------------
		List<Point2D> compaIntPoints = compareCurveConfig.getIntermediatePoints();
		List<Point2D> localIntPoints = this.getIntermediatePoints();
		// --- Check number of intermediate points --------
		if (compaIntPoints.size()!=localIntPoints.size()) return false;
		
		for (int i = 0; i < localIntPoints.size(); i++) {
			Point2D localPoint2D = localIntPoints.get(i);
			Point2D compaPoint2D = compaIntPoints.get(i);
			if (localPoint2D.equals(compaPoint2D)==false) return false;
		}
		return true;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getCopy()
	 */
	@Override
	public GraphEdgeShapeConfiguration<QuadCurve2D> getCopy() {

		QuadCurve2D curveCopy = new QuadCurve2D.Double();
		curveCopy.setCurve(this.getShape().getX1(), this.getShape().getY1(), this.getShape().getCtrlX(), this.getShape().getCtrlY(), this.getShape().getX2(), this.getShape().getY2());

		QuadCurveConfiguration copy = new QuadCurveConfiguration();
		copy.setShape(curveCopy);
		return copy;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#addPopupMenuItems(javax.swing.JPopupMenu, org.awb.env.networkModel.GraphEdge, org.awb.env.networkModel.GraphNode)
	 */
	@Override
	public void addPopupMenuItems(JPopupMenu popupMenu, GraphEdge graphEdge, GraphNode graphNode) {
		if (graphNode!=null) {
			popupMenu.add(this.getJMenuItemCenter());
		}
	}
	/**
	 * Returns the JMenuItem to center the intermediate point.
	 * @return the j menu item center
	 */
	private JMenuItem getJMenuItemCenter() {
		JMenuItem jmiCenter = new JMenuItem();
		jmiCenter.setText(Language.translate("Center between start and end point", Language.EN));
		jmiCenter.setIcon(GraphGlobals.getImageIcon("EdgeQuadCurveCenter.png"));
		jmiCenter.setActionCommand(ACTION_CENTER_INTERMEDIATE);
		return jmiCenter;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#actionPerformed(java.lang.String, org.awb.env.networkModel.controller.ui.configLines.ConfiguredLinePopupPlugin, org.awb.env.networkModel.GraphEdge, org.awb.env.networkModel.GraphNode, java.awt.geom.Point2D)
	 */
	@Override
	public void actionPerformed(String actionCommand, ConfiguredLinePopupPlugin configuredLinePopupPlugin, GraphEdge currentGraphEdge, GraphNode currentGraphNode, Point2D currentMousePositionInGraph) {

		switch (actionCommand) {
		case ACTION_CENTER_INTERMEDIATE:
			// --- Set the shape control point position ---
			QuadCurve2D curve = QuadCurveConfiguration.this.getShape();
			curve.setCurve(curve.getX1(), curve.getY1(), 0.5, curve.getCtrlY(), curve.getX2(), curve.getY2());
			
			BasicGraphGuiVisViewer<GraphNode, GraphEdge> visualizationViewer = configuredLinePopupPlugin.getVisualizationViewer();
			List<GraphNode> edgeNodes = this.getGraphNodesOfGraphEdge(visualizationViewer, currentGraphEdge);
			Point2D newPositionGraph = QuadCurveConfiguration.this.getIntermediatePointTransformer().transformToGraphCoordinate(curve.getCtrlPt(), edgeNodes.get(0), edgeNodes.get(1));
			Point2D newPositionPanel = configuredLinePopupPlugin.getBasicGraphGui().getCoordinateSystemPositionTransformer().transform(newPositionGraph);
			visualizationViewer.getGraphLayout().setLocation(currentGraphNode, newPositionPanel);
			visualizationViewer.repaint();
			break;

		default:
			break;
		}
	}
	
}