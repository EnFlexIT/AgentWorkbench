package org.awb.env.maps;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.awb.env.networkModel.maps.MapService;

/**
 * The Class BaseMapService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BaseMapService implements MapService {

	private OSMMapRenderer osmMapRenderer;
	private OSMZoomController osmZoomController;
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getMapServiceName()
	 */
	@Override
	public String getMapServiceName() {
		return "AWB-OpenStreetMaps";
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getImageIconOfMapService()
	 */
	@Override
	public ImageIcon getImageIconOfMapService() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getJComponentsForMapInteraction()
	 */
	@Override
	public Vector<JComponent> getJComponentsForMapInteraction() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#destroyMapServiceInstances()
	 */
	@Override
	public void destroyMapServiceInstances() {
		if (this.osmMapRenderer!=null) {
			this.osmMapRenderer.dispose();
		}
		this.osmMapRenderer = null;
		this.osmZoomController = null;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getMapRenderer()
	 */
	@Override
	public OSMMapRenderer getMapRenderer() {
		if (osmMapRenderer==null) {
			osmMapRenderer = new OSMMapRenderer(this);
		}
		return osmMapRenderer;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getZoomController()
	 */
	@Override
	public OSMZoomController getZoomController() {
		if (osmZoomController == null) {
			osmZoomController = new OSMZoomController(); 
		}
		return osmZoomController;
	}

	
}
