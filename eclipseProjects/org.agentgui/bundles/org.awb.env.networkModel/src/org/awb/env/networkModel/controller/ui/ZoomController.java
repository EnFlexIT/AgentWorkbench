package org.awb.env.networkModel.controller.ui;

import java.awt.geom.Point2D;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;

/**
 * The Interface ZoomController.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface ZoomController {

	/**
	 * Sets the graph environment controller.
	 * @param graphController the new graph environment controller
	 */
	public void setGraphEnvironmentController(GraphEnvironmentController graphController);

	/**
	 * Sets the visualization viewer.
	 * @param visViewer the vis viewer
	 */
	public void setVisualizationViewer(BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer);

	
	/**
	 * Has to return the currently used JUNG scaling control.
	 * @return the scaling control
	 */
	public ScalingControl getScalingControl();
	
	
	/**
	 * Zooms in.
	 */
	public void zoomIn();
	/**
	 * Zooms in at the specified point.
	 * @param zoomAtPoint the point to zoom at (e.g. the current mouse position on screen)
	 */
	public void zoomIn(Point2D zoomAtPoint);

	/**
	 * Zooms out.
	 */
	public void zoomOut();
	/**
	 * Zooms out at the specified point.
	 * @param zoomAtPoint the zoom at point (e.g. the current mouse position on screen)
	 */
	public void zoomOut(Point2D zoomAtPoint);

	/**
	 * Zoom one to one and move the focus according to the coordinate system source.
	 */
	public void zoomOneToOneMoveFocus();

	/**
	 * Zooms that the graph fits to the window.
	 */
	public void zoomToFitToWindow();

	/**
	 * Zooms to fit the graph to the window. Here the specified visualization viewer can also be the satellite view.
	 * @param visViewer the {@link VisualizationViewer} to adjust
	 */
	public void zoomToFitToWindow(VisualizationViewer<GraphNode, GraphEdge> visViewer);
	
	
	/**
	 * Zoom to the currently selected component.
	 */
	public void zoomToComponent();

}