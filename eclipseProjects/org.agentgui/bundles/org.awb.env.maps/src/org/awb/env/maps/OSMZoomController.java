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
import org.awb.env.networkModel.maps.MapRendererSettings;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;


public class OSMZoomController extends BasicGraphGuiZoomController implements ZoomController {

	private GraphEnvironmentController graphController;
	private VisualizationViewer<GraphNode, GraphEdge> visViewer;

	private ScalingControl scalingControl;
	private ScalingOperator scalingOperator;

	private OSMMapRenderer osmMapRenderer;

	private Point2D defaultScaleAtPoint;
	


	public OSMZoomController(OSMMapRenderer osmMapRenderer) {
		this.osmMapRenderer = osmMapRenderer;
	}
	

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#setGraphEnvironmentController(org.awb.env.networkModel.controller.GraphEnvironmentController)
	 */
	@Override
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	/**
	 * Gets the graph environment controller.
	 * @return the graphController
	 */
	public GraphEnvironmentController getGraphEnvironmentController() {
		return graphController;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#setVisualizationViewer(edu.uci.ics.jung.visualization.VisualizationViewer)
	 */
	@Override
	public void setVisualizationViewer(VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		if (this.visViewer==null) {
			this.visViewer = visViewer;
			this.initialize();
		}
	}
	/**
	 * Gets the visualization viewer.
	 * @return the visViewer
	 */
	public VisualizationViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		return visViewer;
	}
	/**
	 * Initialize.
	 */
	public void initialize() {
		
    	OSMScalingOperator scalingOperator = osmMapRenderer.getScalingOperator();
    	float refinementScalingFactor = (float) scalingOperator.getRefinementScalingFactor();
    	
    	System.out.println("initialize scaling and refine with:" + refinementScalingFactor);
    	this.getScalingControl().scale(this.getVisualizationViewer(), refinementScalingFactor, getDefaultScaleAtPoint());
        	
	}
	
	private MapRendererSettings getMapRendererSettings() {
		if (this.getVisualizationViewer()!=null) {
			return new MapRendererSettings((BasicGraphGuiVisViewer<?, ?>) this.getVisualizationViewer());
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#getScalingControl()
	 */
	@Override
	public ScalingControl getScalingControl() {
		if (scalingControl == null) {
			OSMScalingControl crossoverScalingControl = new OSMScalingControl();
			crossoverScalingControl.setCrossover(1.0);
			scalingControl = crossoverScalingControl;
		}
		return scalingControl;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomIn()
	 */
	@Override
	public void zoomIn() {
		this.zoomIn(this.getDefaultScaleAtPoint());
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomIn(java.awt.geom.Point2D)
	 */
	@Override
	public void zoomIn(Point2D zoomAtPoint) {
		
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
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomOut()
	 */
	@Override
	public void zoomOut() {
		this.zoomOut(this.getDefaultScaleAtPoint());
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomOut(java.awt.geom.Point2D)
	 */
	@Override
	public void zoomOut(Point2D zoomAtPoint) {

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

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomOneToOneMoveFocus()
	 */
	@Override
	public void zoomOneToOneMoveFocus() {
		// TODO Auto-generated method stub
		super.zoomOneToOneMoveFocus();
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomToFitToWindow()
	 */
	@Override
	public void zoomToFitToWindow() {
		// TODO Auto-generated method stub
		super.zoomToFitToWindow();
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomToFitToWindow(edu.uci.ics.jung.visualization.VisualizationViewer)
	 */
	@Override
	public void zoomToFitToWindow(VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		// TODO Auto-generated method stub
		//super.zoomToFitToWindow(visViewer);
		
		this.getScalingControl().scale(visViewer, 1.f, this.getDefaultScaleAtPoint());
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomToComponent()
	 */
	@Override
	public void zoomToComponent() {
		// TODO Auto-generated method stub
		super.zoomToComponent();
	}

	/**
	 * Gets the default point to scale at for zooming.
	 * @return the default scale at point
	 */
	private Point2D getDefaultScaleAtPoint() {
		Rectangle2D rectVis = this.getVisualizationViewer().getVisibleRect();
		if (rectVis.isEmpty() == false) {
			this.defaultScaleAtPoint = new Point2D.Double(rectVis.getCenterX(), rectVis.getCenterY());
		}
		return defaultScaleAtPoint;
	}

	

	// -------------------------------------------------------------------
	// -------------------------------------------------------------------
	// --- >> Koordiaten Transformationen << ----------------------------- 
	// -------------------------------------------------------------------
	// -------------------------------------------------------------------
	// --- => User <= --------------
	// -------------------------------------------------------------------
	// --- Visualization (VisualizationViewer / VisualizationServer) -----
	// -------------------------------------------------------------------
	// --- Graph-Coordinates ---------------------------------------------
	// -------------------------------------------------------------------
	// --- Geo-Coordinate (UTM => WGS84) ---------------------------------
	// -------------------------------------------------------------------
	// --- Map ----------------------------------------------------------- (Scaling: m / pixel)
	// -------------------------------------------------------------------
	
	
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
