package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.awb.env.maps.OSMZoomLevels.ZoomLevel;
import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapRendererSettings;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Tile;
import org.jxmapviewer.viewer.TileListener;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

/**
 * The Class OSMMapRenderer.
 */
public class OSMMapRenderer implements MapRenderer {

	private boolean isDebugJungRescalling = false;
	private boolean isPrintWaypointOverlay = false;
	
	private BaseMapService baseMapService;
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer;
	
	private JXMapViewerForAWB jxMapViewer;
	private TileListener jxTileListener;

	
	/**
	 * Instantiates a new OSM map renderer.
	 * @param baseMapService the parent base map service
	 */
	public OSMMapRenderer(BaseMapService baseMapService) {
		this.baseMapService = baseMapService;
	}

	/**
	 * Returns the zoom controller.
	 * @return the zoom controller
	 */
	private OSMZoomController getZoomController() {
		return this.baseMapService.getZoomController();
	}

	
	/**
	 * Returns the JX map viewer wrapper.
	 * @return the JX map viewer wrapper
	 */
	private JXMapViewerForAWB getJXMapViewerWrapper() {
		if (jxMapViewer==null) {
			jxMapViewer = new JXMapViewerForAWB();
			jxMapViewer.setDrawTileBorders(false);
			jxMapViewer.getTileFactory().addTileListener(this.getJXTileListener());
		}
		return jxMapViewer;
	}
	/**
	 * Returns the local TileListener of the JXMapViewerForAWB.
	 * @return the tile listener
	 */
	private TileListener getJXTileListener() {
		if (jxTileListener==null) {
			jxTileListener = new TileListener() {
				@Override
				public void tileLoaded(Tile tile) {
					OSMMapRenderer.this.visViewer.paintComponentRenderGraph();
				}
			};
		}
		return jxTileListener;
	}
	/**
	 * Disposes the connection between renderer and tile factory of the JXMapViewer.
	 */
	public void dispose() {
		this.getJXMapViewerWrapper().getTileFactory().removeTileListener(this.getJXTileListener());
		this.getJXMapViewerWrapper().getTileFactory().dispose();
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#initialize(org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer, de.enflexit.geography.coordinates.WGS84LatLngCoordinate)
	 */
	@Override
	public void initialize(BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer, WGS84LatLngCoordinate centerGeoCoordinate) {
		this.visViewer = visViewer;
		this.getJXMapViewerWrapper().setAddressLocation(this.convertToGeoPosition(centerGeoCoordinate));
		this.isJungReScalingCalled();
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#getPositionOnScreen(de.enflexit.geography.coordinates.WGS84LatLngCoordinate)
	 */
	@Override
	public Point2D getPositionOnScreen(WGS84LatLngCoordinate wgsCoordinate) {
        return this.getJXMapViewerWrapper().convertGeoPositionToPoint(this.convertToGeoPosition(wgsCoordinate));
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#getGeoCoordinate(java.awt.geom.Point2D)
	 */
	@Override
	public WGS84LatLngCoordinate getGeoCoordinate(Point2D posOnScreen) {
		GeoPosition jxGeoPosition = this.getJXMapViewerWrapper().convertPointToGeoPosition(posOnScreen);
		if (jxGeoPosition!=null) {
			return new WGS84LatLngCoordinate(jxGeoPosition.getLatitude(), jxGeoPosition.getLongitude());
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#paintMap(java.awt.Graphics2D, org.awb.env.networkModel.maps.MapRendererSettings)
	 */
	@Override
	public void paintMap(Graphics2D graphics, MapRendererSettings mapRendererSettings) {

		// --- Check if a JUNG re-scaling is required and called ----
		if (this.isJungReScalingCalled()==true) {
			OSMMapRenderer.this.visViewer.repaint();
			return;
		}
		
		// --- Configure JXMapViewer --------------------------------
		GeoPosition geoPosCenter = this.convertToGeoPosition(mapRendererSettings.getCenterPostion());
		if (geoPosCenter!=null) {
			this.getJXMapViewerWrapper().setAddressLocation(geoPosCenter);
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] No center geo position was specified for the map representation.");
		}
		this.getJXMapViewerWrapper().setBounds(this.visViewer.getSize());
		this.getJXMapViewerWrapper().setZoom(this.getZoomController().getZoomLevel().getJXMapViewerZoomLevel());
		
		// --- Just for debugging purposes --------------------------
		if (this.isPrintWaypointOverlay==true) {
			this.getJXMapViewerWrapper().setOverlayPainter(this.getWaypointPainter(mapRendererSettings));
			this.isPrintWaypointOverlay = false;
		}
		
		// --- Paint map tiles to the specified graphics object -----
		this.getJXMapViewerWrapper().paintComponent(graphics);
	}
	
	/**
	 * Return the center GeoPosition of the map shown.
	 * @return the center GeoPosition
	 */
	public GeoPosition getCenterGeoCoordinate() {
		return this.getJXMapViewerWrapper().getCenterPosition();
	}
	
	/**
	 * Converts a WSG84 coordinate to a JXMapViewer {@link GeoPosition}.
	 *
	 * @param wgs84coord the WGS84 coordinate
	 * @return the Geo Position
	 */	
	public GeoPosition convertToGeoPosition(WGS84LatLngCoordinate wgs84coord) {
		if (wgs84coord==null) return null;
		return new GeoPosition(wgs84coord.getLatitude(), wgs84coord.getLongitude());
	}
	
	/**
	 * Sets the zoom level.
	 * @param zoomLevel the new zoom level
	 */
	private void setZoomLevel(ZoomLevel zoomLevel) {
		this.getJXMapViewerWrapper().setZoom(zoomLevel.getJXMapViewerZoomLevel());
		this.getZoomController().setZoomLevel(zoomLevel);
	}
	
	/**
	 * Checks if the JUNG scaling needs to be adjusted. If so, the nearest {@link ZoomLevel} 
	 * will be determined out of the available {@link OSMZoomLevels}.  
	 *
	 * @return true, if the JUNG visualization was called to re-scale to a new ZoomLevel (scale)
	 */
	private boolean isJungReScalingCalled() {
		
		try {
			
			// --- Get current scaling ------------------------------ 
			double scale = this.visViewer.getOverallScale();;

			// --- Get the closest zoom level -----------------------
			ZoomLevel zl = OSMZoomLevels.getInstance().getClosestZoomLevelOfJungScaling(scale, this.getCenterGeoCoordinate().getLatitude());

			// --- Define an initial zoom level ? -------------------
			if (this.getZoomController().getZoomLevel()==null) {
				this.setZoomLevel(zl);
				return true;
			}
			
			// --- Check if the scaling needs to be adjusted --------
			double scaleDiff = Math.abs(scale - zl.getJungScaling());
			if (scaleDiff > 0.01) {
				if (this.isDebugJungRescalling==true) {
					String classNamePrefix = "[" + this.getClass().getSimpleName() + "] ";
					System.out.println(classNamePrefix + "Current Map Resolution. " + (1.0/scale) + " m/px" );
					System.out.println(classNamePrefix + "Current Scale: " + scale + " m/px" );
					System.out.println(classNamePrefix + "Jung re-scaling required ! - Try " + zl);
					System.out.println();
				}
				// --- Set the new zoom level -----------------------
				this.setZoomLevel(zl);
				return true;
			}
			
		} catch (Exception ex) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error while checking graph scaling:");
			ex.printStackTrace();
		}
		return false;
	}

	
	/**
	 * Returns the waypoint painter that will generate and print some points directly on the map.
	 *
	 * @param mapRendererSettings the map renderer settings
	 * @return the waypoint painter
	 */
	private WaypointPainter<Waypoint> getWaypointPainter(MapRendererSettings mapRendererSettings) {
		
		Dimension visViewerDim = this.visViewer.getSize();
		Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList(
				new DefaultWaypoint(this.getJXMapViewerWrapper().convertPointToGeoPosition(new Point2D.Double(0, 0))),
				new DefaultWaypoint(this.getJXMapViewerWrapper().convertPointToGeoPosition(new Point2D.Double(0, visViewerDim.getHeight()))),
				new DefaultWaypoint(this.getJXMapViewerWrapper().convertPointToGeoPosition(new Point2D.Double(visViewerDim.getWidth(), 0))),
				new DefaultWaypoint(this.getJXMapViewerWrapper().convertPointToGeoPosition(new Point2D.Double(visViewerDim.getWidth(), visViewerDim.getHeight()))),
				new DefaultWaypoint(this.getJXMapViewerWrapper().convertPointToGeoPosition(new Point2D.Double(visViewerDim.getWidth()/2.0, visViewerDim.getHeight()/2.0))))
				);

		WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
		waypointPainter.setWaypoints(waypoints);
		return waypointPainter;
	}
	
}
