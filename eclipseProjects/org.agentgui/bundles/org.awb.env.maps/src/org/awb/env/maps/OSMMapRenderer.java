package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.event.MouseInputListener;

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

public class OSMMapRenderer extends BaseMapRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5410126677313772460L;

	/** The map canvas. */
	private JXMapViewerWrapper mapCanvas;

	/**
	 * Paint map.
	 *
	 * @param graphics the graphics
	 * @param geoCoord the geo coord
	 * @param dimension the dimension
	 * @param positions the positions
	 */
	@Override
	public void paintMap(Graphics graphics, WGS84LatLngCoordinate geoCoord, Rectangle dimension, List<WGS84LatLngCoordinate> positions) {
		super.paintMap(graphics, geoCoord, dimension, positions);
		System.out.println("Calling map rendering");
		if (this.mapCanvas == null) {
			this.mapCanvas = new JXMapViewerWrapper(dimension);
			this.mapCanvas.setBounds(dimension);
			TileFactory tileFactory = new DefaultTileFactory(new OSMTileFactoryInfo());
			tileFactory.addTileListener(this.tileLoadListener);
		} else {
			this.mapCanvas.setBounds(dimension);
		}
		graphics.setClip(0, 0, (int) dimension.getWidth(), (int) dimension.getHeight());
		
		//Paint nodes and zoom to best fit
		if(positions.size() > 0) {
			ArrayList<GeoPosition> nodeCoords = new ArrayList<GeoPosition>(); 
			HashSet<DefaultWaypoint> waypoints = new HashSet<DefaultWaypoint>();
			for (int i = 0; i < positions.size(); i++) {
				GeoPosition geoPosition = this.convertToGeoPosition(positions.get(i));
				nodeCoords.add(i, geoPosition);			
				waypoints.add(new DefaultWaypoint(geoPosition));
			}		
			WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
			waypointPainter.setWaypoints(waypoints);
			waypointPainter.setVisible(true);
	        this.mapCanvas.zoomToBestFit(new HashSet<GeoPosition>(nodeCoords), 0.7);
	        this.mapCanvas.setOverlayPainter(waypointPainter);
		}else {
			this.mapCanvas.setAddressLocation(this.convertToGeoPosition(geoCoord));
		}
		
		this.mapCanvas.paint(graphics);
	}

	@Override
	public void paint(Graphics g) {
		this.mapCanvas.paint(g);
	}

	private TileListener tileLoadListener = new TileListener() {
		public void tileLoaded(Tile tile) {
			if (tile.getZoom() == mapCanvas.getZoom()) {
				mapCanvas.repaint();
				repaint();
			}
		}
	};
	
	protected GeoPosition convertToGeoPosition(WGS84LatLngCoordinate wgs84coord) {
		return new GeoPosition(wgs84coord.getLatitude(), wgs84coord.getLongitude());
	}

}
