package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapRendererSettings;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Tile;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileListener;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

public class OSMMapRenderer implements MapRenderer {

	private JXMapViewerWrapper mapCanvas;

	private Graphics2D graphics;
	private MapRendererSettings mapRendererSettings;
	
	private OSMScalingOperator scalingOperator;

	
	private boolean initialized = false;
	

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#paintMap(java.awt.Graphics2D, org.awb.env.networkModel.maps.MapRendererSettings)
	 */
	@Override
	public void paintMap(Graphics2D graphics, MapRendererSettings mapRendererSettings) {

		// --- Set current working instances ------------------------
		this.setGraphics2D(graphics);
		this.setMapRendererSettings(mapRendererSettings);
		
		ScalingOperator scalingOperator = getScalingOperator(mapRendererSettings);
		System.out.println("Calling map rendering " + mapRendererSettings.getCenterPostion() );
		System.out.println("Landscape width:"+ (mapRendererSettings.getLandscapeDimension().getWidth()));
		System.out.println("Landscape height:"+ (mapRendererSettings.getLandscapeDimension().getHeight()));
		System.out.println("Visualization width:"+ mapRendererSettings.getVisualizationDimension().getWidth());
		System.out.println("Visualization height:"+ mapRendererSettings.getVisualizationDimension().getHeight());
		System.out.println("Zoom level:"+scalingOperator.getZoomLevel());
		
		Dimension visDim = mapRendererSettings.getVisualizationDimension();
		graphics.setClip(0, 0, visDim.width, visDim.height);

		if (this.mapCanvas == null) {
			this.mapCanvas = new JXMapViewerWrapper(visDim);
			this.mapCanvas.setBounds(visDim);
			TileFactory tileFactory = new DefaultTileFactory(new OSMTileFactoryInfo());
			tileFactory.addTileListener(this.tileLoadListener);
//			this.mapCanvas.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
			this.mapCanvas.setTileFactory(tileFactory);
		
		} else {
			this.mapCanvas.setBounds(visDim);
		}
		
		if (initialized == false) {
            Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList(
                    new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getBottomLeftPosition())),
                    new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getBottomRightPosition())),
                    new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getTopLeftPosition())),
                    new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getTopRightPosition())),
                    new DefaultWaypoint(this.convertToGeoPosition(mapRendererSettings.getCenterPostion()))
            	));
        
            WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
            waypointPainter.setWaypoints(waypoints);
        
            this.mapCanvas.setOverlayPainter(waypointPainter);
            initialized = true;
        }
		this.mapCanvas.setZoom(scalingOperator.getZoomLevel());
		this.mapCanvas.setAddressLocation(this.convertToGeoPosition(mapRendererSettings.getCenterPostion()));
		this.mapCanvas.paint(graphics);
	}

	public void repaint() {
//		System.out.println("Calling map rendering " + mapRendererSettings.getCenterPostion() );
//		System.out.println("Landscape width:"+ (mapRendererSettings.getLandscapeDimension().getWidth()));
//		System.out.println("Landscape height:"+ (mapRendererSettings.getLandscapeDimension().getHeight()));
//		System.out.println("Visualization width"+ mapRendererSettings.getVisualizationDimension().getWidth());
//		System.out.println("Visualization height"+ mapRendererSettings.getVisualizationDimension().getHeight());
		GeoPosition centerPos = this.convertToGeoPosition(this.getMapRendererSettings().getCenterPostion());
		this.mapCanvas.setZoom(scalingOperator.getZoomLevel());
		this.mapCanvas.setAddressLocation(centerPos);
//		this.mapCanvas.paint(this.graphics);
		this.paintMap(this.getGraphics2D(), this.getMapRendererSettings());
		this.getMapRendererSettings().getVisualizationViewer().repaint();
	}
	
	public void repaint(int zoomLevel, MapRendererSettings mapRendererSettings) {
		this.mapCanvas.setZoom(zoomLevel);
		this.paintMap(this.getGraphics2D(), mapRendererSettings);
		this.getMapRendererSettings().getVisualizationViewer().repaint();
	}

	private TileListener tileLoadListener = new TileListener() {
		public void tileLoaded(Tile tile) {
			System.out.println("Tile loaded");
			if (tile.getZoom() == mapCanvas.getZoom()) {
				
				repaint();
			}
		}
	};
	
	/**
	 * Convert WSG84 coordinate to geo position.
	 *
	 * @param wgs84coord the wgs 84 coord
	 * @return the geo position
	 */	
	protected GeoPosition convertToGeoPosition(WGS84LatLngCoordinate wgs84coord) {
		return new GeoPosition(wgs84coord.getLatitude(), wgs84coord.getLongitude());
	}
	
	
//	/**
//	 * Gets the scaling operator.
//	 *
//	 * @return the scaling operator
//	 */
//	public OSMScalingOperator getScalingOperator() {
//		if(scalingOperator == null) {
//			MapRendererSettings mapRendererSettings = getMapRendererSettings();
//			if(mapRendererSettings == null) {
//				
//			}else {
//				this.scalingOperator = new OSMScalingOperator(mapRendererSettings.getLandscapeDimension(), mapRendererSettings.getVisualizationDimension(), mapRendererSettings.getCenterPostion());
//			}
//		}
//		return scalingOperator;
//	}
	
	/**
	 * Gets the scaling operator.
	 *
	 * @param mapRenderSettings the map render settings
	 * @return the scaling operator
	 */
	public OSMScalingOperator getScalingOperator(MapRendererSettings mapRenderSettings) {
		if(scalingOperator == null) {
			this.scalingOperator = new OSMScalingOperator(mapRenderSettings.getLandscapeDimension(), mapRenderSettings.getVisualizationDimension(), mapRenderSettings.getCenterPostion());
//			this.scalingOperator = new OSMAbsoluteScalingOperator(mapRenderSettings.getLandscapeDimension(), mapRenderSettings.getVisualizationDimension(), mapRenderSettings.getCenterPostion());

		}
		return scalingOperator;
	}
	
	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.MapRenderer#getScalingOperator()
	*/
	public OSMScalingOperator getScalingOperator(Rectangle2D landscapeDimension, Dimension visualizationDimension, WGS84LatLngCoordinate centerPosition) {
		if(scalingOperator == null) {
			this.scalingOperator = new OSMScalingOperator(landscapeDimension, visualizationDimension, centerPosition);
		}
		return scalingOperator;
	}


	public void setZoomLevel(int zoomLevel) {
		this.scalingOperator.setZoomLevel(zoomLevel);
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
