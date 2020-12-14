package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics;

import org.awb.env.networkModel.maps.MapRenderer;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

/**
 * The Class BaseMapRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BaseMapRenderer implements MapRenderer {

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#paintMap(java.awt.Graphics, de.enflexit.geography.coordinates.WGS84LatLngCoordinate, java.awt.Dimension)
	 */
	@Override
	public void paintMap(Graphics graphics, WGS84LatLngCoordinate geoCoord, Dimension dimension) {

		//System.out.println("[" + this.getClass().getSimpleName() + "] Angekommen");
		
	}

}
