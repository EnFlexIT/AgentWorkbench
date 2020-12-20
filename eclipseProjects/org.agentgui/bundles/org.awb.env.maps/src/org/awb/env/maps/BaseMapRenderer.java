package org.awb.env.maps;

import java.awt.Dimension;
import java.awt.Graphics2D;

import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapRendererSettings;

/**
 * The Class BaseMapRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BaseMapRenderer implements MapRenderer {

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapRenderer#paintMap(java.awt.Graphics2D, org.awb.env.networkModel.maps.MapRendererSettings, java.awt.Dimension)
	 */
	@Override
	public void paintMap(Graphics2D graphics, MapRendererSettings mapRendererSettings, Dimension dimension) {

		//System.out.println("[" + this.getClass().getSimpleName() + "] Angekommen");
		
	}

}
