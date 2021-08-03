package org.awb.env.maps;

import java.awt.geom.Point2D;

import org.awb.env.maps.OSMZoomLevels.ZoomLevel;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController;

/**
 * The OSMZoomController extends the regular {@link BasicGraphGuiZoomController} 
 * and organizes the switching between different zoom level and the Jung scaling.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OSMZoomController extends BasicGraphGuiZoomController {

	private BaseMapService baseMapService;
	
	private OSMScalingControl scalingControl;
	private ZoomLevel zoomLevel;
	
	/**
	 * Instantiates a new OSM zoom controller.
	 * @param baseMapService the base map service
	 */
	public OSMZoomController(BaseMapService baseMapService) {
		this.baseMapService = baseMapService;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#getScalingControl()
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
	 * @return the zoom level
	 */
	public ZoomLevel getZoomLevel() {
		return this.zoomLevel;
	}
	/**
	 * Sets the zoom level.
	 * @param newZoomLevel the new zoom level
	 */
	public void setZoomLevel(ZoomLevel zoomLevel) {
		this.setZoomLevel(zoomLevel, null);
	}
	/**
	 * Sets the zoom level for the specified position on screen and will adjust the JUNG scaling accordingly.
	 *
	 * @param newZoomLevel the new zoom level
	 * @param scalePointOnScreen the scale point on screen
	 */
	public void setZoomLevel(ZoomLevel newZoomLevel, Point2D scalePointOnScreen) {
		if (this.getVisualizationViewer()!=null) {
			if (scalePointOnScreen==null) scalePointOnScreen = this.getVisualizationViewerCenter();
			this.setVisualizationViewerSelected();
			this.getScalingControl().scale(this.getVisualizationViewer(), newZoomLevel, scalePointOnScreen);
			this.zoomLevel = newZoomLevel;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomIn(java.awt.geom.Point2D)
	 */
	@Override
	public void zoomIn(Point2D pointOnScreen) {
		double currLatitude = this.baseMapService.getMapRenderer().getCenterGeoCoordinate().getLatitude();
		this.setZoomLevel(OSMZoomLevels.getInstance().getNextZoomLevel(this.getZoomLevel(), 1, currLatitude), pointOnScreen);
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomOut(java.awt.geom.Point2D)
	 */
	@Override
	public void zoomOut(Point2D pointOnScreen) {
		double currLatitude = this.baseMapService.getMapRenderer().getCenterGeoCoordinate().getLatitude();
		this.setZoomLevel(OSMZoomLevels.getInstance().getNextZoomLevel(this.getZoomLevel(), -1, currLatitude), pointOnScreen);
	}
	
}
