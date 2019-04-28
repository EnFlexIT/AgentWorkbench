package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import agentgui.core.application.Language;

/**
 * The Class PolyLineConfiguration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PolylineConfiguration extends GraphEdgeShapeConfiguration<GeneralPath> {

	private static final long serialVersionUID = -4470264048650428232L;
	
	private static final String ACTION_ADD_INTERMEDIATE = "AddIntermediatePoint";
	private static final String ACTION_REMOVE_INTERMEDIATE = "RemoveIntermediatePoint";
	
	private GeneralPath generalPath;
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getShape()
	 */
	@Override
	public GeneralPath getShape() {
		if (generalPath==null) {
			generalPath = new GeneralPath();
			generalPath.moveTo(0.0d, 0.0d);
			generalPath.lineTo(0.2d, 5.0d);
			generalPath.lineTo(0.4d, 15.0d);
			generalPath.lineTo(0.6d, 15.0d);
			generalPath.lineTo(0.8d, 5.0d);
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
		for (PathIterator pIterator = this.getShape().getPathIterator(null); !pIterator.isDone(); pIterator.next()) {

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
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==null || !(compareObject instanceof PolylineConfiguration)) return false;
		
		// --- Get the compare object ---------------------
		PolylineConfiguration comparePolyConfig = (PolylineConfiguration) compareObject;
		// --- Get intermediate points --------------------
		List<Point2D> compaIntPoints = comparePolyConfig.getIntermediatePoints();
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
	public GraphEdgeShapeConfiguration<GeneralPath> getCopy() {
		PolylineConfiguration copy = new PolylineConfiguration();
		copy.setShape((GeneralPath) this.getShape().clone());
		return copy;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#addPopupMenuItems(javax.swing.JPopupMenu, org.awb.env.networkModel.GraphEdge, org.awb.env.networkModel.GraphNode)
	 */
	@Override
	public void addPopupMenuItems(JPopupMenu popupMenu, GraphEdge graphEdge, GraphNode graphNode) {
		if (graphNode==null) {
			popupMenu.add(this.getJMenuItemAddPoint());
		} else {
			if (graphNode.getId().startsWith(ConfiguredLineMousePlugin.INTERMEDIATE_GRAPH_NODE_ID_PREFIX)) {
				popupMenu.add(this.getJMenuItemRemovePoint());
			}
		}
	}
	/**
	 * Returns the JMenuItem to add an intermediate point.
	 * @return the menu item
	 */
	private JMenuItem getJMenuItemAddPoint() {
		JMenuItem jmiAddPoint = new JMenuItem();
		jmiAddPoint.setText(Language.translate("Add intermediate point", Language.EN));
		jmiAddPoint.setIcon(GraphGlobals.getImageIcon("ListPlus.png"));
		jmiAddPoint.setActionCommand(ACTION_ADD_INTERMEDIATE);
		return jmiAddPoint;
	}
	/**
	 * Returns the JMenuItem to remove an intermediate point.
	 * @return the menu item 
	 */
	private JMenuItem getJMenuItemRemovePoint() {
		JMenuItem jmiRemovePoint = new JMenuItem();
		jmiRemovePoint.setText(Language.translate("Remove intermediate point", Language.EN));
		jmiRemovePoint.setIcon(GraphGlobals.getImageIcon("ListMinus.png"));
		jmiRemovePoint.setActionCommand(ACTION_REMOVE_INTERMEDIATE);
		return jmiRemovePoint;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#actionPerformed(java.lang.String, org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer, org.awb.env.networkModel.GraphEdge, org.awb.env.networkModel.GraphNode, java.awt.geom.Point2D)
	 */
	@Override
	public void actionPerformed(String actionCommand, ConfiguredLinePopupPlugin configuredLinePopupPlugin, GraphEdge currentGraphEdge, GraphNode currentGraphNode, Point2D currentMousePosition) {

		// --- Get the start and end node of the current edge -------
		BasicGraphGuiVisViewer<GraphNode, GraphEdge> visualizationViewer = configuredLinePopupPlugin.getVisualizationViewer();
		List<GraphNode> graphNodeList = this.getGraphNodesOfGraphEdge(visualizationViewer, currentGraphEdge);
		
		switch (actionCommand) {
		case ACTION_ADD_INTERMEDIATE:
	
			// --- Where to add the new intermediate node? ----------
			Point2D mousePosInGraph = visualizationViewer.getRenderContext().getMultiLayerTransformer().inverseTransform(currentMousePosition);
			
			// --- Add the new intermediate point -------------------
			this.setShape(this.addIntermediatePoint(this.getShape(), mousePosInGraph, graphNodeList.get(0), graphNodeList.get(1)));
			
			// --- Notify about the change ---------------------------
			configuredLinePopupPlugin.getGraphEnvironmentController().notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_IntermediateNodeChanged));
			break;

		case ACTION_REMOVE_INTERMEDIATE:
			
			// --- Remove the intermediate node ---------------------
			this.setShape(this.removeIntermediatePoint(this.getShape(), currentGraphNode.getPosition(), graphNodeList.get(0), graphNodeList.get(1)));

			// --- Notify about the change ---------------------------
			configuredLinePopupPlugin.getGraphEnvironmentController().notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_IntermediateNodeChanged));
			break;
		}
	}
	
	/**
	 * Adds the specified intermediate point from the source GeneralPath and returns an extended GenerlPath.
	 *
	 * @param srcGPath the source GeneralPath
	 * @param pointToAddGraph the point to add in graph coordinates
	 * @param startNode the start node
	 * @param endNode the end node
	 * @return the general path
	 */
	public GeneralPath addIntermediatePoint(GeneralPath srcGPath, Point2D pointToAddGraph, GraphNode startNode, GraphNode endNode) {
		
		GeneralPath gPathNew = new GeneralPath();

		IntermediatePointTransformer ipTrans = this.getIntermediatePointTransformer();
		Double xCoordGraphPrev = null;
		Double yCoordGraphPrev = null; 
		
		boolean alreadyAdded = false;
		int pointCounter = 0;

		double[] coords = new double[6];
		for (PathIterator pIterator = srcGPath.getPathIterator(null); !pIterator.isDone(); pIterator.next()) {

			pIterator.currentSegment(coords);
			double xCoordInt = coords[0]; 
			double yCoordInt = coords[1];
			
			// --- Get graph coordinates -------------- 
			Point2D graphPoint = ipTrans.transformToGraphCoordinate(new Point2D.Double(xCoordInt, yCoordInt), startNode, endNode);
			double xCoordGraph = graphPoint.getX(); 
			double yCoordGraph = graphPoint.getY();

			// --- Add a new intermediate point now? ------
			if (xCoordGraphPrev!=null && alreadyAdded==false) {
				
				// --- Calculate distances ----------------
				double distanceCurrToPrev = Point.distance(xCoordGraph, yCoordGraph, xCoordGraphPrev, yCoordGraphPrev);
				double distanceNewToCurr  = Point.distance(pointToAddGraph.getX(), pointToAddGraph.getY(), xCoordGraph, yCoordGraph);
				double distanceNewToPrev  = Point.distance(pointToAddGraph.getX(), pointToAddGraph.getY(), xCoordGraphPrev, yCoordGraphPrev);
				double checkZero = Math.abs(distanceNewToPrev + distanceNewToCurr - distanceCurrToPrev);
				
				// --- Check if new point is on the line -- 
				double threshold = 0.01;
				if (checkZero <= threshold) {
					Point2D pointToAddInt = ipTrans.transformToIntermediateCoordinate(pointToAddGraph, startNode, endNode);
					gPathNew.lineTo(pointToAddInt.getX(), pointToAddInt.getY());
					alreadyAdded = true;
				}
			}
			
			// --- Add intermediate node to GeneralPath ------------- 
			if (pointCounter==0) {
				gPathNew.moveTo(xCoordInt, yCoordInt);
			} else {
				gPathNew.lineTo(xCoordInt, yCoordInt); 
			}
			pointCounter++;
			
			// --- Remind position as previous node position --------
			xCoordGraphPrev = xCoordGraph;
			yCoordGraphPrev = yCoordGraph;
		}
		return gPathNew;
	}
	
	/**
	 * Removes the specified intermediate point from the source GeneralPath and returns a new reduced instance.
	 *
	 * @param srcGPath the source GeneralPath
	 * @param pointToRemoveGraph the point to remove
	 * @param startNode the start node
	 * @param endNode the end node
	 * @return the general path
	 */
	public GeneralPath removeIntermediatePoint(GeneralPath srcGPath, Point2D pointToRemoveGraph, GraphNode startNode, GraphNode endNode) {
		
		GeneralPath gPathNew = new GeneralPath();
		
		int pointCounter = 0;
		IntermediatePointTransformer ipTrans = this.getIntermediatePointTransformer();
		double selectionThreshold = 0.001;
		
		double[] coords = new double[6];
		for (PathIterator pIterator = srcGPath.getPathIterator(null); !pIterator.isDone(); pIterator.next()) {

			pIterator.currentSegment(coords);
			double xCoordInt = coords[0];
			double yCoordInt = coords[1];
			
			// --- Get int. point in graph coordinates ---- 
			Point2D graphPoint = ipTrans.transformToGraphCoordinate(new Point2D.Double(xCoordInt, yCoordInt), startNode, endNode);
			
			// --- Skip the current point? ----------------
			double selectionDistance = Point2D.distance(graphPoint.getX(), graphPoint.getY(), pointToRemoveGraph.getX(), pointToRemoveGraph.getY());
			if (selectionDistance > selectionThreshold) {
				if (pointCounter==0) {
					gPathNew.moveTo(xCoordInt, yCoordInt);
				} else {
					gPathNew.lineTo(xCoordInt, yCoordInt); 
				}
				pointCounter++;
			}
			
		}
		return gPathNew;
	}
	
	
}