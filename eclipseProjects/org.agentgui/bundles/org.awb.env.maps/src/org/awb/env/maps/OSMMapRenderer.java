package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapRendererSettings;
import org.awb.env.networkModel.maps.ScalingOperator;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Tile;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileListener;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

public class OSMMapRenderer implements MapRenderer {

	private JXMapViewerWrapper mapCanvas;

	private Graphics graphics;
	
	private ScalingOperator scalingOperator;

	private MapRendererSettings mapRendererSettings;

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#paintMap(java.awt.Graphics2D, org.awb.env.networkModel.maps.MapRendererSettings)
	 */
	@Override
	public void paintMap(Graphics2D graphics, MapRendererSettings mapRendererSettings) {

		this.graphics = graphics;
		this.mapRendererSettings = mapRendererSettings;
		System.out.println("Calling map rendering " + mapRendererSettings.getCenterPostion() );
		
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
		this.mapCanvas.setZoom(getScalingOperator().getZoomLevel());
		this.mapCanvas.setAddressLocation(this.convertToGeoPosition(mapRendererSettings.getCenterPostion()));
		this.mapCanvas.paint(graphics);
	}

	public void repaint() {
		this.mapCanvas.setZoom(scalingOperator.getZoomLevel());
		this.mapCanvas.setAddressLocation(this.convertToGeoPosition(mapRendererSettings.getCenterPostion()));
		this.mapCanvas.paint(this.graphics);
	}

	private TileListener tileLoadListener = new TileListener() {
		public void tileLoaded(Tile tile) {
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
	public ScalingOperator getScalingOperator() {
		if(scalingOperator == null) {
			this.scalingOperator = new OSMScalingOperator(mapRendererSettings.getLandscapeDimension(), mapRendererSettings.getVisualizationDimension(), mapRendererSettings.getCenterPostion());
		}
		return scalingOperator;
	}

		

}
