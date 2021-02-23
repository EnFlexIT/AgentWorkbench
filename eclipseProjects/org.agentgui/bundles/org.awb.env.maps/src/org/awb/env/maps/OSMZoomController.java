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
import edu.uci.ics.jung.visualization.control.AbsoluteCrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;


public class OSMZoomController extends BasicGraphGuiZoomController implements ZoomController {

	private GraphEnvironmentController graphController;
	private VisualizationViewer<GraphNode, GraphEdge> visViewer;

	private ScalingControl scalingControl;
	private OSMScalingOperator scalingOperator;

	private OSMMapRenderer osmMapRenderer;

	private Point2D defaultScaleAtPoint;
	
	private boolean initialized = false;

	public OSMZoomController(OSMMapRenderer osmMapRenderer) {
		this.osmMapRenderer = osmMapRenderer;
	}
	
	public void init() {
		if(initialized == false) {
        	MapRendererSettings mapRendererSettings = getMapRendererSettings();
        	this.osmMapRenderer.setMapRendererSettings(mapRendererSettings);
        	OSMScalingOperator scalingOperator = this.osmMapRenderer.getScalingOperator(mapRendererSettings);
        	scalingOperator.calcRefinementScalingFactor(mapRendererSettings.getLandscapeDimension(), mapRendererSettings.getVisualizationDimension(), mapRendererSettings.getCenterPostion());
        	getScalingControl().scale(getVisualizationViewer(),(float) scalingOperator.getRefinementScalingFactor(), getDefaultScaleAtPoint());
        	initialized = true;
		}
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
		}
	}
	/**
	 * Gets the visualization viewer.
	 * @return the visViewer
	 */
	public VisualizationViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		return visViewer;
	}
	
	private MapRendererSettings getMapRendererSettings() {
		if (this.getVisualizationViewer()!=null) {
			return new MapRendererSettings((BasicGraphGuiVisViewer<?, ?>) this.getVisualizationViewer());
		}
		return null;
	}
	
	private OSMScalingOperator getScalingOperator() {
		if(scalingOperator == null) {
			scalingOperator = osmMapRenderer.getScalingOperator(getMapRendererSettings());
		}
		return scalingOperator;

	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#getScalingControl()
	 */
	@Override
	public ScalingControl getScalingControl() {
		if (this.scalingControl == null) {
//			OSMScalingControl crossoverScalingControl = new OSMScalingControl();
			CrossoverScalingControl crossoverScalingControl = new CrossoverScalingControl();
//			CrossoverScalingControl crossoverScalingControl = new AbsoluteCrossoverScalingControl();
			crossoverScalingControl.setCrossover(1.0);
			this.scalingControl = crossoverScalingControl;
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
		OSMScalingOperator scalingOperator = getScalingOperator();
		if( scalingOperator.zoomIn()) {
			int newZoomLevel = scalingOperator.getZoomLevel();
			System.out.println("Zoom level:"+newZoomLevel);
			System.out.println("Scaling factor:"+(float) scalingOperator.getScalingFactor());
			this.getScalingControl().scale(this.getVisualizationViewer(), (float) scalingOperator.getScalingFactor() , zoomAtPoint);
			scalingOperator.setZoomLevel(newZoomLevel);
			this.osmMapRenderer.repaint();
		}
		
//		if(this.osmMapRenderer.getScalingOperator(getMapRendererSettings()).increaseZoomLevel()) {
//			System.out.println("Zooming in");
//			this.osmMapRenderer.repaint();
//		}
//		double scalingFactor = this.osmMapRenderer.getScalingOperator(getMapRendererSettings()).getActualScalingFactor();
//		// --- Set selected frame to the parent internal frame ------
//		BasicGraphGuiRootJSplitPane parentInternalFrame = this.getGraphEnvironmentController().getGraphEnvironmentControllerGUI().getBasicGraphGuiRootJSplitPane();
//		if (parentInternalFrame.isSelected() == false) {
//			try {
//				parentInternalFrame.setSelected(true);
//			} catch (PropertyVetoException pvEx) {
//				pvEx.printStackTrace();
//			}
//		}
//
//		// --- Scale the graph to the scale amount ------------------
//		this.getScalingControl().scale(this.getVisualizationViewer(), (float) this.osmMapRenderer.getScalingOperator(getMapRendererSettings())., getDefaultScaleAtPoint());
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

		OSMScalingOperator scalingOperator = getScalingOperator();
		if( scalingOperator.zoomOut()) {
			int newZoomLevel = scalingOperator.getZoomLevel();
			System.out.println("Zoom level:"+newZoomLevel);
			System.out.println("Scaling factor:"+(float) scalingOperator.getScalingFactor());
			this.getScalingControl().scale(this.getVisualizationViewer(), (float) scalingOperator.getScalingFactor() , zoomAtPoint);
			this.osmMapRenderer.repaint();
		}
		
//		if(this.osmMapRenderer.getScalingOperator(getMapRendererSettings()).decreaseZoomLevel()) {
//			System.out.println("Zooming out");
//			this.osmMapRenderer.repaint();
//		}
//		double scalingFactor = this.osmMapRenderer.getScalingOperator(getMapRendererSettings()).getActualScalingFactor();
//		// --- Set selected frame to the parent internal frame ------
//		BasicGraphGuiRootJSplitPane parentInternalFrame = this.getGraphEnvironmentController().getGraphEnvironmentControllerGUI().getBasicGraphGuiRootJSplitPane();
//		if (parentInternalFrame.isSelected() == false) {
//			try {
//				parentInternalFrame.setSelected(true);
//			} catch (PropertyVetoException pvEx) {
//				pvEx.printStackTrace();
//			}
//		}
//
//		// --- Scale the graph to the scale amount ------------------
//		this.getScalingControl().scale(this.getVisualizationViewer(), this.osmMapRenderer.getScalingOperator(getMapRendererSettings()).getZoomLevel(), getDefaultScaleAtPoint());

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
//		MapRendererSettings mapRendererSettings = getMapRendererSettings();
//		OSMScalingOperator scalingOperator= this.osmMapRenderer.getScalingOperator(mapRendererSettings);
//		
//		int zoomLevel = scalingOperator.calcReqZoomLevelToFitVisualization(mapRendererSettings.getLandscapeDimension(), mapRendererSettings.getVisualizationDimension(), mapRendererSettings.getCenterPostion());
//		this.osmMapRenderer.setZoomLevel(zoomLevel);
//		this.getScalingControl().scale(getVisualizationViewer(), zoomLevel, getDefaultScaleAtPoint());
//		this.osmMapRenderer.repaint();
//		super.zoomToFitToWindow();
		this.zoomToFitToWindow(getVisualizationViewer());
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomToFitToWindow(edu.uci.ics.jung.visualization.VisualizationViewer)
	 */
	@Override
	public void zoomToFitToWindow(VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		MapRendererSettings mapRendererSettings = getMapRendererSettings();
		OSMScalingOperator scalingOperator= this.osmMapRenderer.getScalingOperator(mapRendererSettings);
		
		int zoomLevel = scalingOperator.calcReqZoomLevelToFitVisualization(mapRendererSettings.getLandscapeDimension(), mapRendererSettings.getVisualizationDimension(), mapRendererSettings.getCenterPostion());
		this.osmMapRenderer.setZoomLevel(zoomLevel);
		this.getScalingControl().scale(visViewer, zoomLevel, getDefaultScaleAtPoint());
		this.osmMapRenderer.repaint();
		super.zoomToFitToWindow();
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
			System.out.println(rectVis.getCenterX());
			System.out.println(rectVis.getCenterY());
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
