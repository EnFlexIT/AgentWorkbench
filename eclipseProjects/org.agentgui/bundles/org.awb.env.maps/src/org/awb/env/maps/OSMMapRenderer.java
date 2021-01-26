package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapRendererSettings;
import org.awb.env.networkModel.maps.ScalingOperator;
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
	
	private OSMScalingOperator scalingOperator;

	private MapRendererSettings mapRendererSettings;
	
	private boolean initialized = false;


	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#paintMap(java.awt.Graphics2D, org.awb.env.networkModel.maps.MapRendererSettings)
	 */
	@Override
	public void paintMap(Graphics2D graphics, MapRendererSettings mapRendererSettings) {

		this.graphics = graphics;
		setMapRendererSettings(mapRendererSettings);
		ScalingOperator scalingOperator= getScalingOperator();
		System.out.println("Calling map rendering " + mapRendererSettings.getCenterPostion() );
		System.out.println("Landscape width:"+ (mapRendererSettings.getLandscapeDimension().getWidth()));
		System.out.println("Landscape height:"+ (mapRendererSettings.getLandscapeDimension().getHeight()));
		System.out.println("Visualization width"+ mapRendererSettings.getVisualizationDimension().getWidth());
		System.out.println("Visualization height"+ mapRendererSettings.getVisualizationDimension().getHeight());

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
		
		if(initialized == false) {
			
        	
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
		this.mapCanvas.setZoom(scalingOperator.getZoomLevel());
		this.mapCanvas.setAddressLocation(this.convertToGeoPosition(mapRendererSettings.getCenterPostion()));
		this.mapCanvas.paint(this.graphics);
//		this.paintMap(graphics, mapRendererSettings);
//		this.mapRendererSettings.getVisViewer().repaint();
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
	
	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.MapRenderer#getScalingOperator()
	*/
	public OSMScalingOperator getScalingOperator() {
		if(scalingOperator == null) {
			MapRendererSettings mapRendererSettings = getMapRendererSettings();
			if(mapRendererSettings == null) {
				
			}else {
				this.scalingOperator = new OSMScalingOperator(mapRendererSettings.getLandscapeDimension(), mapRendererSettings.getVisualizationDimension(), mapRendererSettings.getCenterPostion());
			}
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

	
	/**
	 * @return the mapRendererSettings
	 */
	public MapRendererSettings getMapRendererSettings() {
		return mapRendererSettings;
	}

	
	/**
	 * @param mapRendererSettings the mapRendererSettings to set
	 */
	public void setMapRendererSettings(MapRendererSettings mapRendererSettings) {
		this.mapRendererSettings = mapRendererSettings;
	}

}
