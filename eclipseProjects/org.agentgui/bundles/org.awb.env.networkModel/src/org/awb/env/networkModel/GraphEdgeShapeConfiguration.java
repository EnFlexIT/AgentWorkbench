package org.awb.env.networkModel;

import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.configLines.ConfiguredLinePopupPlugin;
import org.awb.env.networkModel.controller.ui.configLines.IntermediatePointTransformer;
import org.awb.env.networkModel.controller.ui.configLines.OrthogonalConfiguration;
import org.awb.env.networkModel.controller.ui.configLines.PolylineConfiguration;
import org.awb.env.networkModel.controller.ui.configLines.QuadCurveConfiguration;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * The Class GraphEdgeShapeConfiguration serves as super class for specific edge shape configurations.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @param <T> the generic type
 */
public abstract class GraphEdgeShapeConfiguration<T extends Shape> implements Serializable {

	private static final long serialVersionUID = 7535546074273015170L;

	private enum ShapeConfigurationType {
		Orthogonal,
		QuadCurve,
		Polyline
	}


	
	/**
	 * Returns the actual edge shape.
	 * @return the shape
	 */
	public abstract T getShape();

	/**
	 * Allows to set the actual shape.
	 * @param shape the new shape
	 */
	public abstract void setShape(T shape);

	
	
	/**
	 * Returns a list of intermediate points between start and end point.
	 * @return the intermediate points
	 */
	public abstract List<Point2D> getIntermediatePoints();
	
	/**
	 * Allows to set all intermediate points.
	 *
	 * @param intermediatePointList the intermediate point list
	 * @return the intermediate points
	 */
	public abstract void setIntermediatePoints(List<Point2D> intermediatePointList);
	
	
	
	/**
	 * Enables to add individual menu items to the specified the popup menu that will be shown at the visualization viewer.
	 * To add an individual menu item, just add an action command to the menu item. An {@link ActionListener} will be added 
	 * automatically. Afterwards, the {@link #actionPerformed(String, BasicGraphGuiVisViewer, GraphEdge, GraphNode, Point2D)} will be invoked 
	 * and all required runtime variables will be provided.
	 * 
	 * @param visView the vis view
	 * @param popupMenu the JPopupMenu to which items can be added
	 * @param graphEdge the editing graph edge
	 * @param graphNode the editing graph node (can be <code>null</code>)
	 * @param mousePosition the current mouse position on the visualization viewer
	 * 
	 * @see #actionPerformed(String, BasicGraphGuiVisViewer, GraphEdge, GraphNode, Point2D)
	 */
	public abstract void addPopupMenuItems(JPopupMenu popupMenu, GraphEdge graphEdge, GraphNode graphNode);
	
	/**
	 * The action performed method will be invoked if one of the individual menu items were selected in the context menu.
	 *
	 * @param actionCommand the previously defined action command
	 * @param configuredLinePopupPlugin the popup plugin that enables access to further instances like the {@link GraphEnvironmentController}, the {@link BasicGraphGuiVisViewer} and other.
	 * @param currentGraphEdge the current graph edge
	 * @param currentGraphNode the current graph node
	 * @param currentMousePosition the current mouse position
	 */
	public abstract void actionPerformed(String actionCommand, ConfiguredLinePopupPlugin configuredLinePopupPlugin, GraphEdge currentGraphEdge, GraphNode currentGraphNode, Point2D currentMousePosition);

	
	
	/**
	 * Returns the configuration as consistent string.
	 * @return the configuration as string
	 */
	protected abstract String getConfigurationAsStringInternal();
	
	/**
	 * Sets the configuration from the specified string.
	 *
	 * @param stringConfiguration the configuration string 
	 * @return the graph edge shape configuration
	 */
	protected abstract void setConfigurationFromStringInternal(String stringConfiguration);

	
	/**
	 * Returns the current edge shape configuration as string.
	 * @return the configuration as string
	 */
	public String getConfigurationAsString() {
		return this.getShapeConfigurationTypeAsString() + "=>" + this.getConfigurationAsStringInternal();
	}
	
	/**
	 * Returns a GraphEdgeShapeConfiguration from the specified single config string.
	 *
	 * @param singleEdgeShapeConfigString the single config string
	 * @return the graph edge shape configuration
	 */
	public static GraphEdgeShapeConfiguration<? extends Shape> getGraphEdgeShapeConfiguration(String singleEdgeShapeConfigString) {
		
		if (singleEdgeShapeConfigString==null || singleEdgeShapeConfigString.isEmpty()==true) return null;
		
		GraphEdgeShapeConfiguration<? extends Shape> edgeShapeConfig = null;
		String[] edgeShapeConfigArry = singleEdgeShapeConfigString.split("=>");
		if (edgeShapeConfigArry.length==2) {
			
			String type = edgeShapeConfigArry[0];
			String config = edgeShapeConfigArry[1];

			switch (ShapeConfigurationType.valueOf(type)) {
			case Orthogonal:
				edgeShapeConfig = new OrthogonalConfiguration();
				break;
			case QuadCurve:
				edgeShapeConfig = new QuadCurveConfiguration();
				break;
			case Polyline:
				edgeShapeConfig = new PolylineConfiguration();
				break;
			} 
			
			if (edgeShapeConfig!=null) {
				edgeShapeConfig.setConfigurationFromStringInternal(config);
			}
			
		}
		return edgeShapeConfig;
	}
	
	/**
	 * Return the current shape configuration type as string.
	 * @return the shape configuration as string
	 */
	public String getShapeConfigurationTypeAsString() {
		
		String shapeConfigType = null;
		if (this instanceof OrthogonalConfiguration) {
			shapeConfigType = ShapeConfigurationType.Orthogonal.name();
		} else if (this instanceof QuadCurveConfiguration) {
			shapeConfigType = ShapeConfigurationType.QuadCurve.name();
		} else if (this instanceof PolylineConfiguration) {
			shapeConfigType = ShapeConfigurationType.Polyline.name();
		}
		return shapeConfigType;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(Object compareObject);
	
	/**
	 * Returns a copy of the current shape configuration.
	 * @return the copy of the edge shape
	 */
	public abstract GraphEdgeShapeConfiguration<T> getCopy();

	
	
	/**
	 * Returns a new IntermediatePointTransformer.
	 * @return the intermediate point transformer
	 */
	protected IntermediatePointTransformer getIntermediatePointTransformer() {
		return new IntermediatePointTransformer();
	}

	/**
	 * Returns the start and end graph node of the specified {@link GraphEdge}, where the first node 
	 * represents the start node and the second the end node.
	 *
	 * @param visualizationViewer the visualization viewer
	 * @param edge the edge
	 * @return the graph nodes
	 */
	protected List<GraphNode> getGraphNodesOfGraphEdge(BasicGraphGuiVisViewer<GraphNode, GraphEdge> visualizationViewer, GraphEdge edge) {
		
		List<GraphNode> graphNodeList = new ArrayList<>();
		if (edge!=null) {
			Graph<GraphNode, GraphEdge> graph = visualizationViewer.getGraphLayout().getGraph();
			if (graph.getEdgeType(edge)==EdgeType.DIRECTED) {
				graphNodeList.add(graph.getSource(edge));
				graphNodeList.add(graph.getDest(edge));
			} else {
				Pair<GraphNode> graphNodePair = graph.getEndpoints(edge);
				graphNodeList.add(graphNodePair.getFirst());
				graphNodeList.add(graphNodePair.getSecond());
			}
		}
		return graphNodeList;
	}
	
}
