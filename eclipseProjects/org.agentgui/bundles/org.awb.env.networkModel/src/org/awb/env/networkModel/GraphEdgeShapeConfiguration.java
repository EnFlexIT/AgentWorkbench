package org.awb.env.networkModel;

import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

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

	public static final String EDGE_SHAPE_CONFIGURATION_SEPERATOR = ";";
	
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
	 * @param intermediatePointList the intermediate point list
	 */
	public abstract void setIntermediatePoints(List<Point2D> intermediatePointList);
	
	
	/**
	 * Returns if absolute coordinates will be provided by the {@link GraphEdgeShapeConfiguration}.
	 * @return true, if is use absolute coordinates
	 */
	public abstract boolean isUseAbsoluteCoordinates();
	
	
	/**
	 * Enables to add individual menu items to the specified the popup menu that will be shown at the visualization viewer.
	 * To add an individual menu item, just add an action command to the menu item. An {@link ActionListener} will be added 
	 * automatically. Afterwards, the {@link #actionPerformed(String, ConfiguredLinePopupPlugin, GraphEdge, GraphNode, Point2D)} will be invoked 
	 * and all required runtime variables will be provided.
	 *
	 * @param popupMenu the JPopupMenu to which items can be added
	 * @param graphEdge the editing graph edge
	 * @param graphNode the editing graph node (can be <code>null</code>)
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
	 * @param currentMousePositionInGraph the current mouse position in Graph coordinates
	 */
	public abstract void actionPerformed(String actionCommand, ConfiguredLinePopupPlugin configuredLinePopupPlugin, GraphEdge currentGraphEdge, GraphNode currentGraphNode, Point2D currentMousePositionInGraph);

	
	
	/**
	 * Returns the configuration as consistent string.
	 * @return the configuration as string
	 */
	protected abstract String getConfigurationAsStringInternal();
	
	/**
	 * Sets the configuration from the specified string.
	 * @param stringConfiguration the configuration string 
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
	
	
	// ------------------------------------------------------------------------
	// --- From here, static methods can be found -----------------------------
	// ------------------------------------------------------------------------
	
	/**
	 * Gets the edge shape configuration tree map as string.
	 *
	 * @param allowedLayoutIDs the allowed layout ID's
	 * @return the edge configuration tree map as string
	 */
	public static String getGraphEdgeShapeConfigurationTreeMapAsString(TreeMap<String, GraphEdgeShapeConfiguration<? extends Shape>> configTreeMap, HashSet<String> allowedLayoutIDs) {
		
		
		if (configTreeMap==null || configTreeMap.size()==0) return null;
		
		// --- Convert to String ----------------------
		String configString = null;
		List<String> keys = new ArrayList<>(configTreeMap.keySet()); 
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			
			String layoutID = keys.get(i);
			if (allowedLayoutIDs!=null && allowedLayoutIDs.contains(layoutID)==false) continue;
			
			GraphEdgeShapeConfiguration<? extends Shape> shapeConfig = configTreeMap.get(layoutID);
			if (shapeConfig!=null) {
				String singleConfig = layoutID + ":=" + shapeConfig.getConfigurationAsString();
				if (configString==null) {
					configString = singleConfig;
				} else {
					configString = configString + EDGE_SHAPE_CONFIGURATION_SEPERATOR  + singleConfig;
				}
			}
		}
		return configString;
	}
	
	
	/**
	 * Returns TreeMap of {@link GraphEdgeShapeConfiguration}s from the specified configuration string.
	 *
	 * @param graphEdge the graph edge to be configured
	 * @param edgeShapeTreeMapString the edge shape tree map as string
	 * @return the graph edge shape configuration tree map
	 */
	public static TreeMap<String, GraphEdgeShapeConfiguration<? extends Shape>> getGraphEdgeShapeConfigurationTreeMap(GraphEdge graphEdge, String edgeShapeTreeMapString) {
		
		if (edgeShapeTreeMapString==null || edgeShapeTreeMapString.isEmpty()==true) return null;
		
		TreeMap<String, GraphEdgeShapeConfiguration<? extends Shape>> edgeShapeTreeMap = new TreeMap<>();
		
		String[] layoutShapeConfigurations = edgeShapeTreeMapString.split(EDGE_SHAPE_CONFIGURATION_SEPERATOR);
		for (int i = 0; i < layoutShapeConfigurations.length; i++) {
			
			String[] layoutConfigurationPair = layoutShapeConfigurations[i].split(":=");
			if (layoutConfigurationPair.length==2) {
				String layoutID = layoutConfigurationPair[0];
				GraphEdgeShapeConfiguration<? extends Shape> shapeConfig = GraphEdgeShapeConfiguration.getGraphEdgeShapeConfiguration(graphEdge, layoutConfigurationPair[1]);
				if (layoutID!=null && layoutID.isEmpty()==false && shapeConfig!=null) {
					edgeShapeTreeMap.put(layoutID, shapeConfig);
				}
			}
		}
		return edgeShapeTreeMap;
	}
	
	/**
	 * Returns a GraphEdgeShapeConfiguration from the specified single configuration string.
	 *
	 * @param graphEdge the graph edge to be configured
	 * @param singleEdgeShapeConfigString the single configuration string
	 * @return the graph edge shape configuration
	 */
	public static GraphEdgeShapeConfiguration<? extends Shape> getGraphEdgeShapeConfiguration(GraphEdge graphEdge, String singleEdgeShapeConfigString) {
		
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
				edgeShapeConfig = new PolylineConfiguration(graphEdge);
				break;
			} 
			
			if (edgeShapeConfig!=null) {
				edgeShapeConfig.setConfigurationFromStringInternal(config);
			}
			
		}
		return edgeShapeConfig;
	}
	
}
