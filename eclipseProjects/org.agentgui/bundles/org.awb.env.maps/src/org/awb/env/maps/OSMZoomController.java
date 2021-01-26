package org.awb.env.maps;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyVetoException;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiRootJSplitPane;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController;
import org.awb.env.networkModel.controller.ui.ZoomController;
import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.ScalingOperator;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbsoluteCrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;

@SuppressWarnings("unused")
public class OSMZoomController extends BasicGraphGuiZoomController implements ZoomController {

	ScalingOperator scalingOperator;

	GraphEnvironmentController graphController;

	VisualizationViewer<GraphNode, GraphEdge> visViewer;

	ScalingControl scalingControl;

	OSMMapRenderer osmMapRenderer;

	Point2D defaultScaleAtPoint;
	
	boolean initialized = false;


	public OSMZoomController(OSMMapRenderer osmMapRenderer) {
		this.osmMapRenderer = osmMapRenderer;
	}
	
	public void initialize() {
		if(initialized == false) {
        	OSMScalingOperator scalingOperator = osmMapRenderer.getScalingOperator();
        	float refinementScalingFactor = (float) scalingOperator.getRefinementScalingFactor();
        	System.out.println("initialize scaling and refine with:" + refinementScalingFactor);
        	VisualizationViewer<GraphNode, GraphEdge> visViewer = getVisualizationViewer();
        	ScalingControl scalingControl = getScalingControl();
        	scalingControl.scale(visViewer,
        			refinementScalingFactor ,
        			getDefaultScaleAtPoint());
        	this.initialized = true;
		}
	}


	@Override
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}

	@Override
	public void setVisualizationViewer(VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		this.visViewer = visViewer;
	}

	@Override
	public ScalingControl getScalingControl() {
		if (scalingControl == null) {
			CrossoverScalingControl crossoverScalingControl = new CrossoverScalingControl();
			crossoverScalingControl.setCrossover(1.0);
			scalingControl = crossoverScalingControl;
		}
		return scalingControl;
	}

	@Override
	public void zoomIn() {
		this.zoomIn(this.getDefaultScaleAtPoint());
	}

	@Override
	public void zoomIn(Point2D zoomAtPoint) {
		if(!isInitialized()) {
			initialize();
		}
		if(this.osmMapRenderer.getScalingOperator().increaseZoomLevel()) {
			System.out.println("Zooming in");
			this.osmMapRenderer.repaint();
		}
		double scalingFactor = this.osmMapRenderer.getScalingOperator().getActualScalingFactor();
		// --- Set selected frame to the parent internal frame ------
		BasicGraphGuiRootJSplitPane parentInternalFrame = this.getGraphEnvironmentController().getGraphEnvironmentControllerGUI().getBasicGraphGuiRootJSplitPane();
		if (parentInternalFrame.isSelected() == false) {
			try {
				parentInternalFrame.setSelected(true);
			} catch (PropertyVetoException pvEx) {
				pvEx.printStackTrace();
			}
		}

		// --- Scale the graph to the scale amount ------------------
		this.getScalingControl().scale(this.getVisualizationViewer(), (float) scalingFactor, getDefaultScaleAtPoint());
	}

	@Override
	public void zoomOut() {
		this.zoomOut(this.getDefaultScaleAtPoint());
	}

	@Override
	public void zoomOut(Point2D zoomAtPoint) {
		if(!isInitialized()) {
			initialize();
		}
		if(this.osmMapRenderer.getScalingOperator().decreaseZoomLevel()) {
			System.out.println("Zooming out");
			this.osmMapRenderer.repaint();
		}
		double scalingFactor = this.osmMapRenderer.getScalingOperator().getActualScalingFactor();
		// --- Set selected frame to the parent internal frame ------
		BasicGraphGuiRootJSplitPane parentInternalFrame = this.getGraphEnvironmentController().getGraphEnvironmentControllerGUI().getBasicGraphGuiRootJSplitPane();
		if (parentInternalFrame.isSelected() == false) {
			try {
				parentInternalFrame.setSelected(true);
			} catch (PropertyVetoException pvEx) {
				pvEx.printStackTrace();
			}
		}

		// --- Scale the graph to the scale amount ------------------
		this.getScalingControl().scale(this.getVisualizationViewer(), (float) scalingFactor, getDefaultScaleAtPoint());

	}

	@Override
	public void zoomOneToOneMoveFocus() {
		// TODO Auto-generated method stub
		super.zoomOneToOneMoveFocus();
	}

	@Override
	public void zoomToFitToWindow() {
		// TODO Auto-generated method stub
		super.zoomToFitToWindow();
	}

	@Override
	public void zoomToFitToWindow(VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		// TODO Auto-generated method stub
		//super.zoomToFitToWindow(visViewer);
		
		
		
		
	}

	@Override
	public void zoomToComponent() {
		// TODO Auto-generated method stub
		super.zoomToComponent();
	}

	/**
	 * Gets the default point to scale at for zooming.
	 * 
	 * @return the default scale at point
	 */
	private Point2D getDefaultScaleAtPoint() {
		Rectangle2D rectVis = this.getVisualizationViewer().getVisibleRect();
		if (rectVis.isEmpty() == false) {
			this.defaultScaleAtPoint = new Point2D.Double(rectVis.getCenterX(), rectVis.getCenterY());
		}
		return defaultScaleAtPoint;
	}

	/**
	 * @return the graphController
	 */
	public GraphEnvironmentController getGraphEnvironmentController() {
		return graphController;
	}

	/**
	 * @return the visViewer
	 */
	public VisualizationViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		return visViewer;
	}

	
	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}
	
//	/* (non-Javadoc)
//	* @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#getScalingFactorToZoomIn()
//	*/
//	@Override
//	public float getScalingFactorToZoomIn() {
//		// TODO Auto-generated method stub
//		return (float) this.osmMapRenderer.getScalingOperator().getScalingFactorToZoomIn();
//	}
//	
//	/* (non-Javadoc)
//	* @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#getScalingFactorToZoomOut()
//	*/
//	@Override
//	public float getScalingFactorToZoomOut() {
//		// TODO Auto-generated method stub
//		return (float) this.osmMapRenderer.getScalingOperator().getScalingFactorToZoomOut();
//	}
	
}
