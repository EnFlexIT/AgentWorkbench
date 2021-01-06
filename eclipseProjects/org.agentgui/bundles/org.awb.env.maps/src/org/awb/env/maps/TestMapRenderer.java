package org.awb.env.maps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

public class TestMapRenderer extends BaseMapRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5034982367237986086L;

	public TestMapRenderer() {

	}
	
	@Override
	public void paintMap(Graphics graphics, WGS84LatLngCoordinate geoCoord, Rectangle dimension) {
		super.paintMap(graphics, geoCoord, dimension);
		graphics.setColor(Color.CYAN);
		System.out.println("Calling map renderer");
		graphics.drawRect(0, 0, (int) dimension.getWidth(),(int) dimension.getHeight());
		
	}

}
