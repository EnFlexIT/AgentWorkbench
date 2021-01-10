package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.event.MouseInputListener;

import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapRendererSettings;
import org.awb.env.networkModel.maps.ScalingOperator;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
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

	private ScalingOperator scalingOperator;
	
	private MapRendererSettings mapRendererSettings;
	
	private Graphics graphics;
	
	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.MapRenderer#paintMap(java.awt.Graphics2D, org.awb.env.networkModel.maps.MapRendererSettings, java.awt.Dimension)
	*/
	@Override
	public void paintMap(Graphics2D graphics, MapRendererSettings mapRendererSettings, Dimension dimension) {
		this.graphics = graphics;
		this.mapRendererSettings = mapRendererSettings;
		this.scalingOperator = new OSMScalingOperator(mapRendererSettings.getLandscapeDimension(), mapRendererSettings.getVisualizationDimension(), mapRendererSettings.getCenterPostion());
		
		System.out.println("Calling map rendering " + mapRendererSettings.getCenterPostion() );
		
		if (this.mapCanvas == null) {
			this.mapCanvas = new JXMapViewerWrapper(dimension);
			this.mapCanvas.setBounds(dimension);
			this.mapCanvas.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
		} else {
			this.mapCanvas.setBounds(dimension);
		}
		
		graphics.setClip(0, 0, (int) dimension.getWidth(), (int) dimension.getHeight());
		
		//Paint nodes and zoom to best fit
		HashSet<GeoPosition> nodeCoords = new HashSet<GeoPosition>(); 
		nodeCoords.add(this.convertToGeoPosition(mapRendererSettings.getBottomLeftPosition()));
		nodeCoords.add(this.convertToGeoPosition(mapRendererSettings.getBottomRightPosition()));
		nodeCoords.add(this.convertToGeoPosition(mapRendererSettings.getTopLeftPosition()));
		nodeCoords.add(this.convertToGeoPosition(mapRendererSettings.getTopRightPosition()));


		this.mapCanvas.setAddressLocation(this.convertToGeoPosition(mapRendererSettings.getCenterPostion()));
		
		this.mapCanvas.paint(graphics);
	}

//	@Override
	public void repaint() {
		this.mapCanvas.paint(this.graphics);
	}

	private TileListener tileLoadListener = new TileListener() {
		public void tileLoaded(Tile tile) {
			if (tile.getZoom() == mapCanvas.getZoom()) {
				repaint();
			}
		}
	};
	
	protected GeoPosition convertToGeoPosition(WGS84LatLngCoordinate wgs84coord) {
		return new GeoPosition(wgs84coord.getLatitude(), wgs84coord.getLongitude());
	}
	
	public ScalingOperator getScalingOperator() {
		return this.scalingOperator;
	}

}
