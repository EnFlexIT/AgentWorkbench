package org.awb.env.networkModel.maps;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
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
	 * Will be invoked to initialize the individual MapRenderer. Here also the initial zoom level (and others) should be set
	 * to allow a correct calculation between geographical coordinate and position on screen.   
	 *
	 * @param visViewer the {@link BasicGraphGuiVisViewer} that visualizes the graph and call the current MapRenderer. Use this, e.g. to determine the current visualization size. 
	 * @param centerGeoCoordinate the current geographical location in the center of the visViewer
	 */
	public void initialize(BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer, WGS84LatLngCoordinate centerGeoCoordinate);

	
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

	
	/**
	 * Will be invoked to set the center geographical coordinate before {@link #paintMap(Graphics2D)} is called.
	 * @param centerWgsCoordinate the new geographical center coordinate
	 */
	public void setCenterGeoCoordinate(WGS84LatLngCoordinate centerWgsCoordinate);
	
	/**
	 * Paint map at the specified geographical coordinate.
	 * @param graphics2D the graphics2D instance to work on
	 */
	public void paintMap(Graphics2D graphics2D);
	
}
