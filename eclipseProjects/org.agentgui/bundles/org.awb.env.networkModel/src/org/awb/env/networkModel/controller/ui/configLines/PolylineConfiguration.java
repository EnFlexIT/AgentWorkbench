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
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.environment.EnvironmentController;

/**
 * The Class PolyLineConfiguration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PolylineConfiguration extends GraphEdgeShapeConfiguration<GeneralPath> {

	private static final long serialVersionUID = -4470264048650428232L;
	
	private static final String COORD_HANDLING_ABSOLUTE = "Absolute";
	
	private static final String ACTION_ADD_INTERMEDIATE = "AddIntermediatePoint";
	private static final String ACTION_REMOVE_INTERMEDIATE = "RemoveIntermediatePoint";
	

	private List<Point2D> intermediatePoints;
	
	private GraphEdge graphEdge;
	private boolean useAbsoluteCoordinates;
	
	private GraphNode graphNodeStart;
	private GraphNode graphNodeEnd;
	
	
	/**
	 * Instantiates a new polyline configuration that uses RELATIVE intermediate positions.
	 * @param graphEdge the GraphEdge to edit
	 */
	public PolylineConfiguration(GraphEdge graphEdge) {
		this(graphEdge, false);
	}
	
	/**
	 * Instantiates a new polyline configuration that may use ABSOLUTE intermediate positions (see corresponding argument).
	 *
	 * @param graphEdge the GraphEdge to edit
	 * @param isUseAbsoluteCoordinates the indicator to use absolute intermediate coordinates
	 */
	public PolylineConfiguration(GraphEdge graphEdge, boolean isUseAbsoluteCoordinates) {
		if (graphEdge==null) {
			this.setUseAbsoluteCoordinates(false);
		} else {
			this.graphEdge = graphEdge;
			this.setUseAbsoluteCoordinates(isUseAbsoluteCoordinates);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#isUseAbsoluteCoordinates()
	 */
	@Override
	public boolean isUseAbsoluteCoordinates() {
		return useAbsoluteCoordinates;
	}
	/**
	 * Sets that absolute coordinates are to bes used for handling and saving.
	 * @param useAbsoluteCoordinates the new use absolute coordinates
	 */
	public void setUseAbsoluteCoordinates(boolean useAbsoluteCoordinates) {
		this.useAbsoluteCoordinates = useAbsoluteCoordinates;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getShape()
	 */
	@Override
	public GeneralPath getShape() {
		
		IntermediatePointTransformer intPointTransformer = this.getIntermediatePointTransformer();
		
		// ----------------------------------------------------------------------------------------
		// --- Always ensure that the return value is in relative coordinates for drawing ---------
		// ----------------------------------------------------------------------------------------
		GeneralPath gpReturn = new GeneralPath();
		gpReturn.moveTo(0.0d, 0.0d);
		List<Point2D> pointList = this.getIntermediatePoints();
		for (int i = 0; i < pointList.size(); i++) {
			Point2D point2D = pointList.get(i);
			if (this.isUseAbsoluteCoordinates()==true) {
				// --- Convert to relative coordinates for drawing -------------------------------- 
				point2D = intPointTransformer.transformToIntermediateCoordinate(point2D, this.getGraphNodeStart(), this.getGraphNodeEnd());
			}
			gpReturn.lineTo(point2D.getX(), point2D.getY());
		}
		gpReturn.lineTo(1.0, 0.0d);

		return gpReturn;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#setShape(java.awt.Shape)
	 */
	@Override
	public void setShape(GeneralPath shape) {
		
		int intPointIndex = -1;
		double[] coords = new double[6];
		
		List<Point2D> intPointPosList = new ArrayList<>();
		for (PathIterator pIterator = shape.getPathIterator(null); !pIterator.isDone(); pIterator.next()) {
			
			pIterator.currentSegment(coords);
			double xCoordInt = coords[0]; 
			double yCoordInt = coords[1];

			if (intPointIndex>=0) {
				intPointPosList.add(new Point2D.Double(xCoordInt, yCoordInt));
			}
			intPointIndex++;
		}
		
		// --- Remove the last point found (is end node) ------------
		intPointPosList.remove(intPointPosList.size()-1);
		this.setIntermediatePoints(intPointPosList);
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#getIntermediatePoints()
	 */
	@Override
	public List<Point2D> getIntermediatePoints() {
		if (intermediatePoints==null) {
			intermediatePoints = new ArrayList<>();
			if (this.isUseAbsoluteCoordinates()==true) {
				// --- Create absolute coordinates ------------------
				IntermediatePointTransformer intPointTrans = this.getIntermediatePointTransformer();
				intermediatePoints.add(intPointTrans.transformToGraphCoordinate(new Point2D.Double(0.2d, 5.0d),  this.getGraphNodeStart(), this.getGraphNodeEnd()));
				intermediatePoints.add(intPointTrans.transformToGraphCoordinate(new Point2D.Double(0.4d, 15.0d), this.getGraphNodeStart(), this.getGraphNodeEnd()));
				intermediatePoints.add(intPointTrans.transformToGraphCoordinate(new Point2D.Double(0.6d, 15.0d), this.getGraphNodeStart(), this.getGraphNodeEnd()));
				intermediatePoints.add(intPointTrans.transformToGraphCoordinate(new Point2D.Double(0.8d, 5.0d),  this.getGraphNodeStart(), this.getGraphNodeEnd()));
			} else {
				// --- Create relative coordinates ------------------
				intermediatePoints.add(new Point2D.Double(0.2d, 5.0d));
				intermediatePoints.add(new Point2D.Double(0.4d, 15.0d));
				intermediatePoints.add(new Point2D.Double(0.6d, 15.0d));
				intermediatePoints.add(new Point2D.Double(0.8d, 5.0d));	
			}
		}
		return this.intermediatePoints;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.GraphEdgeShapeConfiguration#setIntermediatePoints(java.util.List)
	 */
	@Override
	public void setIntermediatePoints(List<Point2D> intermediatePointList) {
		this.intermediatePoints = intermediatePointList;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#getConfigurationAsString()
	 */
	@Override
	protected String getConfigurationAsStringInternal() {
		
		String positions = null;
		List<Point2D> intPoints = this.getIntermediatePoints();
		
		// --- Get intermediate points as string --------------------
		for (int i = 0; i < intPoints.size(); i++) {
			Point2D pos = intPoints.get(i);
			String posString = GraphNode.getPositionAsString(pos);
			if (positions==null) {
				positions = posString; 
			} else {
				positions = positions + "|" + posString;
			}
		}
		
		// --- Set if is absolute position handling ----------------- 
		if (this.isUseAbsoluteCoordinates()==true) {
			positions = COORD_HANDLING_ABSOLUTE + "(" + positions + ")";
		}
		return positions;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphEdgeShapeConfiguration#setConfigurationFromString(java.lang.String)
	 */
	@Override
	protected void setConfigurationFromStringInternal(String stringConfiguration) {
		
		List<Point2D> intPointList = new ArrayList<>();
		
		// --- Determine if absolute or relative handling -----------
		if (stringConfiguration.startsWith(COORD_HANDLING_ABSOLUTE)) {
			this.setUseAbsoluteCoordinates(true);
		} else {
			this.setUseAbsoluteCoordinates(false);
		}
		stringConfiguration = this.getPostionConfiguration(stringConfiguration);
		
		// --- Set intermediate point positions ---------------------
		String[] posArray = stringConfiguration.split("\\|");
		for (int i = 0; i < posArray.length; i++) {
			Point2D pos = GraphNode.getPositionFromString(posArray[i]);
			if (pos!=null) {
				intPointList.add(pos);
			}
		}
		this.setIntermediatePoints(intPointList);
	}
	
	/**
	 * Removes the brackets and additional information to get just the position information.
	 *
	 * @param stringConfiguration the string configuration
	 * @return the intermediate positions as string
	 */
	private String getPostionConfiguration(String stringConfiguration) {
		
		String cleandConfiguration = stringConfiguration;

		if (stringConfiguration!=null && stringConfiguration.isEmpty()==false) {
			// --- Remove the absolute indicator, if used ---------------------
			int idxOpenBracket = stringConfiguration.indexOf("(");
			if (idxOpenBracket!=-1) {
				cleandConfiguration = stringConfiguration.substring(idxOpenBracket+1);
				int idxClosedBracket = cleandConfiguration.indexOf(")");
				cleandConfiguration = cleandConfiguration.substring(0, idxClosedBracket);
			}
		}
		return cleandConfiguration;
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

		PolylineConfiguration copy = new PolylineConfiguration(this.graphEdge, this.isUseAbsoluteCoordinates());
		
		List<Point2D> intPointListCopy = new ArrayList<>();
		for (int i = 0; i < this.getIntermediatePoints().size(); i++) {
			Point2D point2D = this.getIntermediatePoints().get(i);
			intPointListCopy.add(new Point2D.Double(point2D.getX(), point2D.getY()));
		}
		copy.setIntermediatePoints(intPointListCopy);
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
	public void actionPerformed(String actionCommand, ConfiguredLinePopupPlugin configuredLinePopupPlugin, GraphEdge currentGraphEdge, GraphNode currentGraphNode, Point2D currentMousePositionInGraph) {

		// --- Get the start and end node of the current edge -------
		BasicGraphGuiVisViewer<GraphNode, GraphEdge> visualizationViewer = configuredLinePopupPlugin.getVisualizationViewer();
		List<GraphNode> graphNodeList = this.getGraphNodesOfGraphEdge(visualizationViewer, currentGraphEdge);
		
		switch (actionCommand) {
		case ACTION_ADD_INTERMEDIATE:
			// --- Add the new intermediate point -------------------
			this.addIntermediatePoint(currentMousePositionInGraph, graphNodeList.get(0), graphNodeList.get(1));
			// --- Notify about the change ---------------------------
			configuredLinePopupPlugin.getGraphEnvironmentController().notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_IntermediateNodeChanged));
			break;

		case ACTION_REMOVE_INTERMEDIATE:
			// --- Remove the intermediate node ---------------------
			this.removeIntermediatePoint(currentGraphNode.getPosition(), graphNodeList.get(0), graphNodeList.get(1));
			// --- Notify about the change ---------------------------
			configuredLinePopupPlugin.getGraphEnvironmentController().notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_IntermediateNodeChanged));
			break;
		}
	}
	
	/**
	 * Adds the specified intermediate point from the source GeneralPath and returns an extended GeneralPath.
	 *
	 * @param pointToAddGraph the point to add in graph coordinates
	 * @param startNode the start node
	 * @param endNode the end node
	 */
	public void addIntermediatePoint(Point2D pointToAddGraph, GraphNode startNode, GraphNode endNode) {
		
		IntermediatePointTransformer ipTrans = this.getIntermediatePointTransformer();
		
		int intPointIndexForInsert = -1;

		double shortestDistance = Double.MAX_VALUE;
		double xCoordGraphPrev = 0.0;
		double yCoordGraphPrev = 0.0; 
		
		// --- Get complete list for all edge related nodes ---------
		List<Point2D> edgePoints = this.getIntermediatePoints();
		if (this.isUseAbsoluteCoordinates()==true) {
			edgePoints.add(0, new Point2D.Double(startNode.getPosition().getX(), startNode.getPosition().getY()));
			edgePoints.add(new Point2D.Double(endNode.getPosition().getX(), endNode.getPosition().getY()));
		} else {
			edgePoints.add(0, new Point2D.Double(0, 0));
			edgePoints.add(new Point2D.Double(1, 0));
		}
		
		// ----------------------------------------------------------
		// --- Find index position for adding a new node ------------
		// ----------------------------------------------------------
		for (int i = 0; i < edgePoints.size(); i++) {
			
			Point2D edgePoint = edgePoints.get(i);
			
			// --- Transform to graph coordinates -------------------
			Point2D graphPoint = edgePoint;
			if (this.isUseAbsoluteCoordinates()==false) {
				graphPoint = ipTrans.transformToGraphCoordinate(edgePoint, startNode, endNode);
			}
			
			double xCoordGraph = graphPoint.getX(); 
			double yCoordGraph = graphPoint.getY();

			// --- Is intermediate point? ---------------------------
			if (i>0) {
				// --- Calculate distances --------------------------
				double distanceCurrToPrev = Point.distance(xCoordGraph, yCoordGraph, xCoordGraphPrev, yCoordGraphPrev);
				double distanceNewToCurr  = Point.distance(pointToAddGraph.getX(), pointToAddGraph.getY(), xCoordGraph, yCoordGraph);
				double distanceNewToPrev  = Point.distance(pointToAddGraph.getX(), pointToAddGraph.getY(), xCoordGraphPrev, yCoordGraphPrev);
				double singleDistance = Math.abs(distanceNewToPrev + distanceNewToCurr - distanceCurrToPrev);
				if (singleDistance < shortestDistance) {
					shortestDistance = singleDistance;
					intPointIndexForInsert = i;
				}
			}
			
			// --- Remind position as previous node position --------
			xCoordGraphPrev = xCoordGraph;
			yCoordGraphPrev = yCoordGraph;
			
		}
		
		// ----------------------------------------------------------
		// --- Update complete edge point list ----------------------
		// ----------------------------------------------------------
		Point2D pointToAdd = pointToAddGraph;
		if (this.isUseAbsoluteCoordinates()==false) {
			pointToAdd = ipTrans.transformToIntermediateCoordinate(pointToAddGraph, startNode, endNode);
		}
		edgePoints.add(intPointIndexForInsert, pointToAdd);
		
		// --- Remove first and last position -----------------------
		edgePoints.remove(edgePoints.size()-1);
		edgePoints.remove(0);
		
		// --- Set new intermediate points --------------------------
		this.setIntermediatePoints(edgePoints);
	}
	
	/**
	 * Removes the specified intermediate point from the source GeneralPath and returns a new reduced instance.
	 *
	 * @param pointToRemoveGraph the point to remove
	 * @param startNode the start node
	 * @param endNode the end node
	 */
	public void removeIntermediatePoint(Point2D pointToRemoveGraph, GraphNode startNode, GraphNode endNode) {
		
		IntermediatePointTransformer ipTrans = this.getIntermediatePointTransformer();
		double selectionThreshold = 0.001;

		List<Point2D> intPointList = new ArrayList<>(this.getIntermediatePoints());
		for (int i = 0; i < intPointList.size(); i++) {
			
			Point2D point = intPointList.get(i);
			Point2D graphPoint = point;
			if (this.isUseAbsoluteCoordinates()==false) {
				graphPoint = ipTrans.transformToGraphCoordinate(point, startNode, endNode);
			}
			
			double selectionDistance = Point2D.distance(graphPoint.getX(), graphPoint.getY(), pointToRemoveGraph.getX(), pointToRemoveGraph.getY());
			if (selectionDistance <= selectionThreshold) {
				this.getIntermediatePoints().remove(i);
				break;
			}
		}
		
	}
	
	// ------------------------------------------------------------------------------------------------
	// --- From here help methods to get the corresponding GraphNodes and its positions can be found --
	// ------------------------------------------------------------------------------------------------
	/**
	 * Returns the start GraphNode.
	 * @return the start graph node 
	 */
	private GraphNode getGraphNodeStart() {
		if (graphNodeStart==null) {
			graphNodeStart = this.getNetworkModel().getGraph().getEndpoints(this.graphEdge).getFirst();
		}
		return graphNodeStart;
	}
	/**
	 * Returns the end GraphNode.
	 * @return the end graph node
	 */
	private GraphNode getGraphNodeEnd() {
		if (graphNodeEnd==null) {
			graphNodeEnd = this.getNetworkModel().getGraph().getEndpoints(this.graphEdge).getSecond();
		}
		return graphNodeEnd;
	}

	/**
	 * Returns the current NetworkModel.
	 * @return the network model
	 */
	private NetworkModel getNetworkModel() {
		NetworkModel networkModel = null;
		if (Application.getProjectFocused()!=null) {
			EnvironmentController envController = Application.getProjectFocused().getEnvironmentController();
			if (envController instanceof GraphEnvironmentController) {
				GraphEnvironmentController graphController = (GraphEnvironmentController) envController;
				networkModel = graphController.getNetworkModel();
			}
		}
		return networkModel;
	}
	
}