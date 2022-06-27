package org.awb.env.networkModel.controller.ui;

import java.awt.Shape;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.helper.GraphEdgeShapeTransformer;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;

import com.google.common.base.Function;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

/**
 * The Class EdgeShapeTransformer connects the edge settings from the {@link GeneralGraphSettings4MAS}
 * with the visualization of an edge with a JUNG {@link VisualizationViewer}. Therefore, it reads the current 
 * setup-settings and the current graph and transforms it to the corresponding shape. 
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class EdgeShapeTransformer implements Function<GraphEdge, Shape> {

	private VisualizationViewer<GraphNode, GraphEdge> visViewer;
	private GraphEnvironmentController graphController;
	
	private Graph<GraphNode, GraphEdge> graph;
	private EdgeShape<GraphNode, GraphEdge> edgeShapeBuilder;
	
	
	/**
	 * Instantiates a new edge shape transformer.
	 *
	 * @param graphController the current {@link GraphEnvironmentController}
	 * @param visViewer the VisualizationViewer
	 */
	public EdgeShapeTransformer(GraphEnvironmentController graphController, VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		this.graphController = graphController;
		this.visViewer = visViewer;
	}
	
	/**
	 * Returns the current graph.
	 * @return the graph
	 */
	public Graph<GraphNode, GraphEdge> getGraph() {
		if (graph==null) {
			graph = this.visViewer.getGraphLayout().getGraph();
		}
		return graph;
	}
	/**
	 * Return the current, locally used builder-{@link EdgeShape}.
	 * @return the edge shape builder
	 */
	private EdgeShape<GraphNode, GraphEdge> getEdgeShapeBuilder() {
		if (edgeShapeBuilder==null) {
			edgeShapeBuilder = new EdgeShape<GraphNode, GraphEdge>(this.getGraph());
		}
		return edgeShapeBuilder;
	}
	
	private void checkForGraphUpdate() {
		if (this.graph!=null) {
			if (this.visViewer.getGraphLayout().getGraph()!=this.graph) {
				// --- Reset local working variables ------
				this.graph = null;
				this.edgeShapeBuilder = null;
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public Shape apply(GraphEdge graphEdge) {

		// --- Check if the graph has changed in the 
		this.checkForGraphUpdate();
		
		// --- Get the edge-shape setting and its corresponding function ------
		Function<GraphEdge, Shape> shapeFunction = null;
		switch (this.graphController.getNetworkModel().getLayoutSettings().getEdgeShape()) {
		case BentLine:
			shapeFunction = this.getEdgeShapeBuilder().new BentLine();
			break;
		case Box:
			shapeFunction = this.getEdgeShapeBuilder().new Box();
			break;
		case ConfigurableLine:
			shapeFunction = new GraphEdgeShapeTransformer<GraphNode, GraphEdge>(this.getGraph());
			break;
		case CubicCurve:
			shapeFunction = this.getEdgeShapeBuilder().new CubicCurve();
			break;
		case Line:
			shapeFunction = this.getEdgeShapeBuilder().new Line();
			break;
		case Loop:
			shapeFunction = this.getEdgeShapeBuilder().new Loop();
			break;
		case Orthogonal:
			shapeFunction = this.getEdgeShapeBuilder().new Orthogonal();
			break;
		case QuadCurve:
			shapeFunction = this.getEdgeShapeBuilder().new QuadCurve();
			break;
		case SimpleLoop:
			shapeFunction = this.getEdgeShapeBuilder().new SimpleLoop();
			break;
		case Wedge:
			shapeFunction = this.getEdgeShapeBuilder().new Wedge(5);
			break;
		}
		
		// --- By the current function, transfer into a Shape ---------
		Shape shape = null;
		try {
			shape = shapeFunction.apply(graphEdge);	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return shape;
	}

}
