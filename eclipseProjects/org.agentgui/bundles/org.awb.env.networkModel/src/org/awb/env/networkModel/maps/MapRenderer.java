package org.awb.env.networkModel.maps;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

/**
 * The Interface MapRenderer can be implemented to draw map images to the background of the 
 * {@link NetworkModel} that is displayed by the {@link BasicGraphGuiVisViewer} and the 
 * {@link MapPreRenderer}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface MapRenderer {

	
	/**
	 * Will be invoked to set the geographical center location.
	 * @param geoCoordinate the new geographical center location
	 */
	public void setCenterGeoLocation(WGS84LatLngCoordinate geoCoordinate);
	
	/**
	 * Paint map at the specified geographical coordinate.
	 *
	 * @param graphics2D the graphics2D instance to work on
	 * @param mapRendererSettings the current map render settings. This instance only will be produced if the visualization was changed somehow (thru scaling or movement). Otherwise, the last instance will be provided again. 
	 */
	public void paintMap(Graphics2D graphics2D, MapRendererSettings mapRendererSettings);

	/**
	 * Has to return the position on screen for the specified WGS84 coordinate.
	 *
	 * @param wgsCoordinate the WGS84 coordinate
	 * @return the position on screen
	 */
	public Point2D getPositionOnScreen(WGS84LatLngCoordinate wgsCoordinate);

	/**
	 * Has to return the {@link WGS84LatLngCoordinate} for the specified screen position.
	 * @param posOnScreen the position on screen
	 * @return the geographical coordinate as {@link WGS84LatLngCoordinate} instance
	 */
	public WGS84LatLngCoordinate getGeoCoordinate(Point2D posOnScreen);
	
}
