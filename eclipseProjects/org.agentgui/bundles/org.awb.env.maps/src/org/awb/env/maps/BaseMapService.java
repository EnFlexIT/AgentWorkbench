package org.awb.env.maps;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.awb.env.networkModel.maps.MapRendererSettings;
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
		return "AWB-OpenStreetMaps Map Service";
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
	public OSMMapRenderer getMapRenderer() {
		if (osmMapRenderer==null) {
			osmMapRenderer = new OSMMapRenderer();
		}
		return osmMapRenderer;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getZoomController()
	 */
	@Override
	public OSMZoomController getZoomController() {
		if (osmZoomController == null) {
			osmZoomController = new OSMZoomController(this.getMapRenderer()); 
		}
		return osmZoomController;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.maps.MapService#getJComponentsForMapInteraction()
	 */
	@Override
	public Vector<JComponent> getJComponentsForMapInteraction() {
		return null;
	}
	
	
	/* (non-Javadoc)
	* @see org.awb.env.networkModel.maps.MapService#setMapRendererSettings(org.awb.env.networkModel.maps.MapRendererSettings)
	*/
	public void setMapRendererSettings(MapRendererSettings mapRendererSettings) {
		this.getMapRenderer().setMapRendererSettings(mapRendererSettings);
	}
	

	
	
}
