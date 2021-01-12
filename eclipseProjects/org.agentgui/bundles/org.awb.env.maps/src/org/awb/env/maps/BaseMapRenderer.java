package org.awb.env.maps;

import java.awt.Graphics;
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
	public void paintMap(Graphics2D graphics, MapRendererSettings mapRendererSettings) {
		//System.out.println("[" + this.getClass().getSimpleName() + "] Angekommen");
//		this.paintMap(graphics, geoCoord, dimension, new ArrayList<WGS84LatLngCoordinate>());
		
		
	}
	

}
