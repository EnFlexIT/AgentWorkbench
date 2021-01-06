package org.awb.env.maps;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;
import java.awt.Graphics2D;

import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapRendererSettings;

/**
 * The Class BaseMapRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class BaseMapRenderer implements MapRenderer {
	
	private static final long serialVersionUID = 6308436046951210047L;
	
	private Graphics graphics;

	@Override
	public void paintMap(Graphics2D graphics, MapRendererSettings mapRendererSettings, Dimension dimension) {
		//System.out.println("[" + this.getClass().getSimpleName() + "] Angekommen");
//		this.paintMap(graphics, geoCoord, dimension, new ArrayList<WGS84LatLngCoordinate>());
		
		
	}
	

}
