package org.awb.env.networkModel.maps;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.Set;

import javax.swing.event.MouseInputListener;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

// TODO: Auto-generated Javadoc
/**
 * The Interface MapRenderer can be implemented to draw map images to the background of the 
 * {@link NetworkModel} that is displayed by the {@link BasicGraphGuiVisViewer} and the 
 * {@link MapPreRenderer}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface MapRenderer {

	/**
	 * Paint map at the specified geographical coordinate.
	 *
	 * @param graphics the graphics
	 * @param geoCoord the {@link WGS84LatLngCoordinate}
	 * @param dimension the dimension of the visualization
	 */
	public void paintMap(Graphics graphics, WGS84LatLngCoordinate geoCoord, Rectangle dimension);
	
	/**
	 * Paint map at the specified geographical coordinate.
	 *
	 * @param graphics the graphics
	 * @param geoCoord the geo coord
	 * @param dimension the dimension
	 * @param positions the positions
	 */
	public void paintMap(Graphics graphics, WGS84LatLngCoordinate geoCoord, Rectangle dimension, List<WGS84LatLngCoordinate> positions);
	
	
	/**
	 * Sets the mouse input listener.
	 *
	 * @param inputListener the new mouse input listener
	 */
	public void setMouseInputListener(MouseInputListener mouseListener);
	
	
	public void setMouseWheelListener(MouseWheelListener wheelListener);
	
}
