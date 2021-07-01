package org.awb.env.networkModel.controller.ui;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.settings.LayoutSettings;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;

/**
 * The Class BasicGraphGuiStaticLayout provides the default layout for the visualization
 * of AWB graph and network models.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BasicGraphGuiStaticLayout extends StaticLayout<GraphNode, GraphEdge> {

	private GraphEnvironmentController graphController;
	private TransformerForGraphNodePosition coordinateSystemNodePositionTransformer;
	
	/**
	 * Instantiates a static layout.
	 *
	 * @param graphController the graph controller
	 * @param graph the graph
	 */
	public BasicGraphGuiStaticLayout(GraphEnvironmentController graphController, Graph<GraphNode, GraphEdge> graph) {
		super(graph);
		this.graphController = graphController;
		this.doInitialConfigurations();
	}
	/**
	 * Does the initial configurations.
	 */
	private void doInitialConfigurations() {

		// --- Set the initializer / the coordinate transformer for the layout ----------
		this.setInitializer(this.getCoordinateSystemPositionTransformer());

		// --- Set the size of the layout -----------------------------------------------
		Rectangle2D graphDimension = GraphGlobals.getGraphSpreadDimension(this.getGraph());
		this.setSize(new Dimension((int) (graphDimension.getWidth() + 2 * BasicGraphGui.graphMargin), (int) (graphDimension.getHeight() + 2 * BasicGraphGui.graphMargin)));
	}

	/**
	 * Returns the position transformer that considers the directions of the defined coordinate system.
	 * @return the TransformerForGraphNodePosition
	 * 
	 * @see LayoutSettings
	 */
	public TransformerForGraphNodePosition getCoordinateSystemPositionTransformer() {
		if (coordinateSystemNodePositionTransformer==null) {
			coordinateSystemNodePositionTransformer = new TransformerForGraphNodePosition(this.graphController);
		}
		return coordinateSystemNodePositionTransformer;
	}
	
}
