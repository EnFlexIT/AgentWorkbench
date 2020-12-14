package org.awb.env.networkModel.maps;

import java.awt.Dimension;
import java.awt.Graphics;

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
	 * Paint map at the specified geographical coordinate.
	 *
	 * @param graphics the graphics
	 * @param geoCoord the {@link WGS84LatLngCoordinate}
	 * @param dimension the dimension of the visualization
	 */
	public void paintMap(Graphics graphics, WGS84LatLngCoordinate geoCoord, Dimension dimension);
	
}
