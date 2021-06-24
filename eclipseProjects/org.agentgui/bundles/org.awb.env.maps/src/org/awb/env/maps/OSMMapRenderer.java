package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.awb.env.maps.OSMZoomLevels.ZoomLevel;
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
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.transform.MutableAffineTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * The Class OSMMapRenderer.
 */
public class OSMMapRenderer implements MapRenderer {

	private boolean isDebug = true;
	
	private BaseMapService baseMapService;
	
	private JXMapViewerForAWB jxMapViewer;

	private Graphics2D graphics;
	private MapRendererSettings mapRendererSettings;
	
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
	 * Returns the current scaling control.
	 * @return the scaling control
	 */
	private OSMScalingControl getScalingControl() {
		return this.getZoomController().getScalingControl();
	}

	/**
	 * Returns the JX map viewer wrapper.
	 * @return the JX map viewer wrapper
	 */
	private JXMapViewerForAWB getJXMapViewerWrapper() {
		if (jxMapViewer==null) {
			jxMapViewer = new JXMapViewerForAWB();
			jxMapViewer.setDrawTileBorders(true);
			jxMapViewer.getTileFactory().addTileListener(new TileListener() {
				@Override
				public void tileLoaded(Tile tile) {
					OSMMapRenderer.this.repaint();
				}
			});
		}
		return jxMapViewer;
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
		
		
		// --- Get the mutable transformer for the graph ------------
		AffineTransform aTransGraphics = graphics.getTransform();
		System.out.println("Graphics2D AffineTransformer " + aTransGraphics.toString());
		
		MutableAffineTransformer mTransLayout = (MutableAffineTransformer) mapRendererSettings.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		AffineTransform atLayoutTrans  = mTransLayout.getTransform();
		AffineTransform atLayoutInvers = mTransLayout.getInverse();
		
		MutableAffineTransformer mTransView = (MutableAffineTransformer) mapRendererSettings.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
		AffineTransform atViewTrans  = mTransView.getTransform();
		AffineTransform atViewInvers = mTransView.getInverse();
		
		System.out.println("MutableAffineTransformer [Layout] " + mTransLayout.toString());
		System.out.println("MutableAffineTransformer [ View ] " + mTransView.toString());
		System.out.println();
		
		// --- Test area to manipulate the transformer --------------

		
		Dimension visDim = mapRendererSettings.getVisualizationDimension();
		graphics.setClip(0, 0, visDim.width, visDim.height);
	
		this.getJXMapViewerWrapper().setAddressLocation(this.convertToGeoPosition(mapRendererSettings.getCenterPostion()));
		this.getJXMapViewerWrapper().setBounds(visDim);
		this.getJXMapViewerWrapper().setZoom(this.getScalingControl().getZoomLevel().getJXMapViewerZoomLevel());
		
		if (isPrintOverlay==true) {
			this.getJXMapViewerWrapper().setOverlayPainter(this.getWaypointPainter(mapRendererSettings));
			
			mTransLayout.translate(100, 100);
			
			isPrintOverlay = false;
		}
		this.getJXMapViewerWrapper().paintComponent(graphics);
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
			
			// --- Get current scaling ------------------------------ 
			MutableTransformer layoutTransformer = this.getMapRendererSettings().getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
			MutableTransformer viewTransformer = this.getMapRendererSettings().getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
			double modelScale = layoutTransformer.getScale();
			double viewScale = viewTransformer.getScale();
			double scale = modelScale * viewScale;
			
			// --- Get the closest zoom level -----------------------
			ZoomLevel zl = OSMZoomLevels.getInstance().getClosestZoomLevelOfJungScaling(scale);
			
			// --- Check if the scaling needs to adjusted -----------
			double scaleDiff = Math.abs(scale - zl.getJungScaling());
			if (scaleDiff > 0.01) {
				
				requiresJungReScaling = true;
				
				if (this.isDebug==true) {
					String classNamePrefix = "[" + this.getClass().getSimpleName() + "] ";
					System.out.println(classNamePrefix + "Current Map Resolution. " + (1.0/scale) + " m/px" );
					System.out.println(classNamePrefix + "Current Scale: " + scale + " m/px" );
					System.out.println(classNamePrefix + "Jung re-scaling required ! - Try " + zl);
					System.out.println();
				}
				
				BasicGraphGuiVisViewer<?, ?> visViewer = this.getMapRendererSettings().getVisualizationViewer();
				Point2D scalePoint = null;
				Rectangle2D rectVis = visViewer.getVisibleRect();
				if (rectVis.isEmpty() == false) {
					scalePoint = new Point2D.Double(rectVis.getCenterX(), rectVis.getCenterY());
				}
				this.getScalingControl().scale(visViewer, zl, scalePoint);
			}
			
		} catch (Exception ex) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error while checking graph scaling:");
			ex.printStackTrace();
		}
		return requiresJungReScaling;
	}
	
	
	public void repaint() {
//		System.out.println("Calling map rendering " + mapRendererSettings.getCenterPostion() );
//		System.out.println("Landscape width:"+ (mapRendererSettings.getLandscapeDimension().getWidth()));
//		System.out.println("Landscape height:"+ (mapRendererSettings.getLandscapeDimension().getHeight()));
//		System.out.println("Visualization width"+ mapRendererSettings.getVisualizationDimension().getWidth());
//		System.out.println("Visualization height"+ mapRendererSettings.getVisualizationDimension().getHeight());

//		GeoPosition centerPos = this.convertToGeoPosition(this.getMapRendererSettings().getCenterPostion());
//		this.mapCanvas.setZoom(scalingOperator.getZoomLevel());
//		this.mapCanvas.setAddressLocation(centerPos);
		this.getJXMapViewerWrapper().paint(this.getGraphics2D());
		this.paintMap(this.getGraphics2D(), this.getMapRendererSettings());
		this.getMapRendererSettings().getVisualizationViewer().repaint();
	}
	
	public void repaint(int zoomLevel, MapRendererSettings mapRendererSettings) {
		this.jxMapViewer.setZoom(zoomLevel);
		this.paintMap(this.getGraphics2D(), mapRendererSettings);
		this.getMapRendererSettings().getVisualizationViewer().repaint();
	}
	
	
	private WaypointPainter<Waypoint> getWaypointPainter(MapRendererSettings mapRendererSettings) {
		
		Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList(
				new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getBottomLeftPosition())),
				new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getBottomRightPosition())),
				new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getTopLeftPosition())),
				new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getTopRightPosition())),
				new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getCenterPostion()))));

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
		this.mapRendererSettings = mapRendererSettings;
	}
	

}
