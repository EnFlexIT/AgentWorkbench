package org.awb.env.maps;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

import org.awb.env.maps.OSMZoomLevels.ZoomLevel;
import org.awb.env.networkModel.GraphRectangle2D;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;

/**
 * The OSMZoomController extends the regular {@link BasicGraphGuiZoomController}
 * and organizes the switching between different zoom level and the Jung
 * scaling.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OSMZoomController extends BasicGraphGuiZoomController {

    private BaseMapService baseMapService;

    private OSMScalingControl scalingControl;
    private ZoomLevel zoomLevel;

    /**
     * Instantiates a new OSM zoom controller.
     * 
     * @param baseMapService the base map service
     */
    public OSMZoomController(BaseMapService baseMapService) {
	this.baseMapService = baseMapService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#
     * getScalingControl()
     */
    @Override
    public OSMScalingControl getScalingControl() {
	if (this.scalingControl == null) {
	    OSMScalingControl crossoverScalingControl = new OSMScalingControl();
	    crossoverScalingControl.setCrossover(1.0);
	    this.scalingControl = crossoverScalingControl;
	}
	return scalingControl;
    }

    /**
     * Returns the current zoom level of the {@link OSMScalingControl}.
     * 
     * @return the zoom level
     */
    public ZoomLevel getZoomLevel() {
	return this.zoomLevel;
    }

    /**
     * Sets the zoom level.
     * 
     * @param zoomLevel the zoom level
     */
    public void setZoomLevel(ZoomLevel zoomLevel) {
	this.setZoomLevel(zoomLevel, null);
    }

    /**
     * Sets the zoom level for the specified position on screen and will adjust the
     * JUNG scaling accordingly.
     *
     * @param newZoomLevel       the new zoom level
     * @param scalePointOnScreen the scale point on screen
     */
    public void setZoomLevel(ZoomLevel newZoomLevel, Point2D scalePointOnScreen) {
	if (this.getVisualizationViewer() != null) {
	    if (scalePointOnScreen == null)
		scalePointOnScreen = this.getVisualizationViewerCenter();
	    this.setVisualizationViewerSelected();
	    this.getVisualizationViewer().getBasicGraphGuiStaticLayout().setRefreshGraphNodePositionPaused(true);
	    this.getScalingControl().scale(this.getVisualizationViewer(), newZoomLevel, scalePointOnScreen);
	    this.getVisualizationViewer().getBasicGraphGuiStaticLayout().setRefreshGraphNodePositionPaused(false);
	    this.zoomLevel = newZoomLevel;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomIn(
     * java.awt.geom.Point2D)
     */
    @Override
    public void zoomIn(Point2D pointOnScreen) {
	double currLatitude = this.baseMapService.getMapRenderer().getCenterGeoCoordinate().getLatitude();
	this.setZoomLevel(OSMZoomLevels.getInstance().getNextZoomLevel(this.getZoomLevel(), 1, currLatitude),
		pointOnScreen);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomOut(
     * java.awt.geom.Point2D)
     */
    @Override
    public void zoomOut(Point2D pointOnScreen) {
	double currLatitude = this.baseMapService.getMapRenderer().getCenterGeoCoordinate().getLatitude();
	this.setZoomLevel(OSMZoomLevels.getInstance().getNextZoomLevel(this.getZoomLevel(), -1, currLatitude),
		pointOnScreen);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#
     * zoomOneToOne()
     */
    @Override
    public void zoomOneToOne() {

	// --- Use super method first -----------------------------------------
	super.zoomOneToOne();
	// --- Do ZoomLevel adjustment / re-scale -----------------------------
	if (this.baseMapService.getMapRenderer().isJungReScalingCalled() == true) {
	    double scaleNew = this.getVisualizationViewer().getOverallScale();
	    if (scaleNew > 1.0) {
		this.zoomOut();
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#
     * zoomToFitToWindow()
     */
    @Override
    public void zoomToFitToWindow() {

	// --- Use super method first -----------------------------------------
	super.zoomToFitToWindow();
	// --- Do ZoomLevel adjustment / re-scale -----------------------------
	if (this.baseMapService.getMapRenderer().isJungReScalingCalled() == true) {

	    // --- Check if each corner point is in the visualization area ----
	    GraphRectangle2D graphRectangle2D = this.getLastGraphRectangle2D();
	    Rectangle visViewerRect = this.getVisualizationViewer().getVisibleRect();

	    this.getVisualizationViewer().updateMapRendererCenterGeoLocation();
	    TransformerForGraphNodePosition gnpTransformer = this.getVisualizationViewer()
		    .getCoordinateSystemPositionTransformer();
	    AffineTransform at = this.getVisualizationViewer().getOverallAffineTransform();

	    List<Point2D> cpList = graphRectangle2D.getCornerPointList();
	    for (int i = 0; i < cpList.size(); i++) {
		Point2D cp = cpList.get(i);
		Point2D cpJung = gnpTransformer.apply(cp);
		Point2D cpView = at.transform(cpJung, null);
		if (visViewerRect.contains(cpView.getX(), cpView.getY()) == false) {
		    this.zoomOut();
		    break;
		}
	    }

	}
    }

}
