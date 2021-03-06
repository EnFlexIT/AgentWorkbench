package org.awb.env.networkModel.maps;

import java.awt.Dimension;
import java.awt.Graphics2D;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

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
	 * @param graphics2D the graphics2D instance to work on
	 * @param mapRendererSettings the map render settings
	 * @param dimension the dimension of the visualization
	 */
	public void paintMap(Graphics2D graphics2D, MapRendererSettings mapRendererSettings, Dimension dimension);
	
}
