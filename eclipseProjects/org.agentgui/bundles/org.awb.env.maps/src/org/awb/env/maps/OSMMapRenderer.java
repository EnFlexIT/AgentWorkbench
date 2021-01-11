package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;

import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapRendererSettings;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Tile;
import org.jxmapviewer.viewer.TileListener;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

public class OSMMapRenderer implements MapRenderer {

	private JXMapViewerWrapper mapCanvas;

	private Graphics graphics;
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#paintMap(java.awt.Graphics2D, org.awb.env.networkModel.maps.MapRendererSettings)
	 */
	@Override
	public void paintMap(Graphics2D graphics, MapRendererSettings mapRendererSettings) {

		this.graphics = graphics;
		System.out.println("Calling map rendering " + mapRendererSettings.getCenterPostion() );
		
		Dimension visDim = mapRendererSettings.getVisualizationDimension();
		if (this.mapCanvas == null) {
			this.mapCanvas = new JXMapViewerWrapper(visDim);
			this.mapCanvas.setBounds(visDim);
//			TileFactory tileFactory = new DefaultTileFactory(new OSMTileFactoryInfo());
			this.mapCanvas.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
//			tileFactory.addTileListener(this.tileLoadListener);
		} else {
			this.mapCanvas.setBounds(visDim);
		}
		graphics.setClip(0, 0, visDim.width, visDim.height);
		
		//Paint nodes and zoom to best fit
		HashSet<GeoPosition> nodeCoords = new HashSet<GeoPosition>(); 
		nodeCoords.add(this.convertToGeoPosition(mapRendererSettings.getBottomLeftPosition()));
		nodeCoords.add(this.convertToGeoPosition(mapRendererSettings.getBottomRightPosition()));
		nodeCoords.add(this.convertToGeoPosition(mapRendererSettings.getTopLeftPosition()));
		nodeCoords.add(this.convertToGeoPosition(mapRendererSettings.getTopRightPosition()));

//		if(positions.size() > 0) {
//			ArrayList<GeoPosition> nodeCoords = new ArrayList<GeoPosition>(); 
//			HashSet<DefaultWaypoint> waypoints = new HashSet<DefaultWaypoint>();
//			for (int i = 0; i < positions.size(); i++) {
//				GeoPosition geoPosition = this.convertToGeoPosition(positions.get(i));
//				nodeCoords.add(i, geoPosition);			
//				waypoints.add(new DefaultWaypoint(geoPosition));
//			}		
//			WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
//			waypointPainter.setWaypoints(waypoints);
//			waypointPainter.setVisible(true);
	        this.mapCanvas.zoomToBestFit(nodeCoords, 0.99);
//	        this.mapCanvas.setOverlayPainter(waypointPainter);
//		}else {
//			this.mapCanvas.setAddressLocation(this.convertToGeoPosition(geoCoord));
//		}
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

}
