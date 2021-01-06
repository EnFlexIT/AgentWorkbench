package org.awb.env.maps;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.awb.env.networkModel.maps.MapRenderer;
import org.awb.env.networkModel.maps.MapService;

/**
 * The Class BaseMapService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BaseMapService implements MapService {

	private MapRenderer mapRenderer;
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getMapServiceName()
	 */
	@Override
	public String getMapServiceName() {
		return "AWB-Base Map Service";
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getImageIconOfMapService()
	 */
	@Override
	public ImageIcon getImageIconOfMapService() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getMapRenderer()
	 */
	@Override
	public MapRenderer getMapRenderer() {
		if (mapRenderer==null) {
			mapRenderer = new OSMMapRenderer();
//			mapRenderer = new TestMapRenderer();

		}
		return mapRenderer;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getJComponentsForMapInteraction()
	 */
	@Override
	public Vector<JComponent> getJComponentsForMapInteraction() {
		return null;
	}

}
