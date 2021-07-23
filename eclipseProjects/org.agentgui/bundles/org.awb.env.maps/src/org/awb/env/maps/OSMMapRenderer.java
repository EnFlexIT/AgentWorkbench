package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.awb.env.maps.OSMZoomLevels.ZoomLevel;
import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapRendererSettings;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Tile;
import org.jxmapviewer.viewer.TileListener;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * The Class OSMMapRenderer.
 */
public class OSMMapRenderer implements MapRenderer {

	private boolean isDebugJungRescalling = false;
	
	private BaseMapService baseMapService;
	
	private JXMapViewerForAWB jxMapViewer;
	private TileListener jxTileListener;

	private Graphics2D graphics;
	private MapRendererSettings mapRendererSettings;
	private boolean newMapRendererSettings;
	
	private boolean isPrintOverlay = true;
	
	
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
			jxMapViewer.setDrawTileBorders(true);
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
					OSMMapRenderer.this.getMapRendererSettings().getVisualizationViewer().paintComponentRenderGraph();
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
	 * @see org.awb.env.networkModel.maps.MapRenderer#setCenterGeoLocation(de.enflexit.geography.coordinates.WGS84LatLngCoordinate)
	 */
	@Override
	public void setCenterGeoCoordinate(WGS84LatLngCoordinate geoCoordinate) {
		this.getJXMapViewerWrapper().setAddressLocation(this.convertToGeoPosition(geoCoordinate));
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#getCenterGeoCoordinate()
	 */
	@Override
	public WGS84LatLngCoordinate getCenterGeoCoordinate() {
		GeoPosition jxGeoPosition = this.getJXMapViewerWrapper().getCenterPosition();
		if (jxGeoPosition!=null) {
			return new WGS84LatLngCoordinate(jxGeoPosition.getLatitude(), jxGeoPosition.getLongitude());
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#getPositionOnScreen(de.enflexit.geography.coordinates.WGS84LatLngCoordinate)
	 */
	@Override
	public Point2D getPositionOnScreen(WGS84LatLngCoordinate wgsCoordinate) {
		return this.getJXMapViewerWrapper().convertGeoPositionToPoint(new GeoPosition(wgsCoordinate.getLatitude(), wgsCoordinate.getLongitude()));
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

		// --- Set current working instances ------------------------
		this.setGraphics2D(graphics);
		this.setMapRendererSettings(mapRendererSettings);
		
		// --- Check if a Jung re-scaling is required and called ----
		if (this.isJungReScalingCalled()==true) {
			mapRendererSettings.getVisualizationViewer().repaint();
			return;
		}
		
		// --- Set clip and configure JXMapViewer -------------------
		Dimension visDim = mapRendererSettings.getVisualizationDimension();
		graphics.setClip(0, 0, visDim.width, visDim.height);
		
		GeoPosition geoPosCenter = this.convertToGeoPosition(mapRendererSettings.getCenterPostion());
		if (geoPosCenter!=null) {
			this.getJXMapViewerWrapper().setAddressLocation(geoPosCenter);
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] No center geo position was specified for the map representation.");
		}
		this.getJXMapViewerWrapper().setBounds(visDim);
		this.getJXMapViewerWrapper().setZoom(this.getZoomController().getZoomLevel().getJXMapViewerZoomLevel());
		
		if (isPrintOverlay==true) {
			this.getJXMapViewerWrapper().setOverlayPainter(this.getWaypointPainter(mapRendererSettings));
			isPrintOverlay = false;
		}
		
		// --- Paint to the specified graphics object ---------------
		this.getJXMapViewerWrapper().paintComponent(graphics);
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
	 * Checks if the Jung scaling needs to be adjusted. If so, the nearest {@link ZoomLevel} 
	 * will be determined out of the available {@link OSMZoomLevels}.  
	 *
	 * @return true, if the Jung visualization was called to scale to a known ZoomLevel
	 */
	private boolean isJungReScalingCalled() {
		
		boolean requiresJungReScaling = false;
		try {
			
			// --- Define an initial zoom level ? -------------------
			if (this.getZoomController().getZoomLevel()==null) {
				// -- Set the highest zoom level first --------------
				this.setZoomLevel(OSMZoomLevels.getInstance().getZoomLevel(19, this.getCenterGeoCoordinate().getLatitude()));
				return true;
			}
			
			// --- Get current scaling ------------------------------ 
			MutableTransformer layoutTransformer = this.getMapRendererSettings().getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
			MutableTransformer viewTransformer = this.getMapRendererSettings().getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
			double modelScale = layoutTransformer.getScale();
			double viewScale = viewTransformer.getScale();
			double scale = modelScale * viewScale;
			
			// --- Get the closest zoom level -----------------------
			ZoomLevel zl = OSMZoomLevels.getInstance().getClosestZoomLevelOfJungScaling(scale, this.getCenterGeoCoordinate().getLatitude());
			
			// --- Check if the scaling needs to adjusted -----------
			double scaleDiff = Math.abs(scale - zl.getJungScaling());
			if (scaleDiff > 0.01) {
				requiresJungReScaling = true;
				this.setZoomLevel(zl);
				if (this.isDebugJungRescalling==true) {
					String classNamePrefix = "[" + this.getClass().getSimpleName() + "] ";
					System.out.println(classNamePrefix + "Current Map Resolution. " + (1.0/scale) + " m/px" );
					System.out.println(classNamePrefix + "Current Scale: " + scale + " m/px" );
					System.out.println(classNamePrefix + "Jung re-scaling required ! - Try " + zl);
					System.out.println();
				}
			}
			
		} catch (Exception ex) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error while checking graph scaling:");
			ex.printStackTrace();
		}
		return requiresJungReScaling;
	}

	
	private WaypointPainter<Waypoint> getWaypointPainter(MapRendererSettings mapRendererSettings) {
		
		Dimension visViewerDim = mapRendererSettings.getVisualizationViewer().getSize();
		
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
	
	/**
	 * Convert WSG84 coordinate to geo position.
	 *
	 * @param wgs84coord the wgs 84 coord
	 * @return the geo position
	 */	
	protected GeoPosition convertToGeoPosition(WGS84LatLngCoordinate wgs84coord) {
		if (wgs84coord==null) return null;
		return new GeoPosition(wgs84coord.getLatitude(), wgs84coord.getLongitude());
	}
	
	
	/**
	 * Gets the graphics 2D instance.
	 * @return the graphics
	 */
	public Graphics2D getGraphics2D() {
		return graphics;
	}
	/**
	 * Sets the graphics 2D instance.
	 * @param graphics the new graphics 12
	 */
	public void setGraphics2D(Graphics2D graphics) {
		this.graphics = graphics;
	}
	
	/**
	 * Gets the map renderer settings.
	 * @return the mapRendererSettings
	 */
	public MapRendererSettings getMapRendererSettings() {
		return mapRendererSettings;
	}
	/**
	 * Sets the map renderer settings.
	 * @param mapRendererSettings the mapRendererSettings to set
	 */
	public void setMapRendererSettings(MapRendererSettings mapRendererSettings) {
		this.setNewMapRendererSettings(this.mapRendererSettings==null || mapRendererSettings!=this.mapRendererSettings);
		this.mapRendererSettings = mapRendererSettings;
	}
	
	/**
	 * Checks if is new map renderer settings.
	 * @return true, if is new map renderer settings
	 */
	public boolean isNewMapRendererSettings() {
		return this.newMapRendererSettings;
	}
	/**
	 * Sets the new map renderer settings.
	 * @param newMapRendererSettings the new new map renderer settings
	 */
	private void setNewMapRendererSettings(boolean newMapRendererSettings) {
		this.newMapRendererSettings = newMapRendererSettings;
	}

}
