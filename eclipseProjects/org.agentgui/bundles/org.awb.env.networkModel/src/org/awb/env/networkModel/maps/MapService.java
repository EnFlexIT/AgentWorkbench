package org.awb.env.networkModel.maps;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController;
import org.awb.env.networkModel.controller.ui.ZoomController;

/**
 * The Interface MapService can be used to register a map integration to the background  
 * of the {@link NetworkModel} that is displayed by the {@link BasicGraphGuiVisViewer}
 * and the {@link MapPreRenderer}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface MapService {

	/**
	 * Has to Returns the map service name.
	 * @return the map service name
	 */
	public String getMapServiceName();
	
	/**
	 * Has to return the image icon of the current MapService.
	 * @return the image icon of map service
	 */
	public ImageIcon getImageIconOfMapService();

	/**
	 * Has to return the {@link MapRenderer}.
	 * @return the map renderer
	 */
	public MapRenderer getMapRenderer();

	/**
	 * Has to return the {@link ZoomController} to act on zooming action within the visualization. If null will be  
	 * returned by this method, the default ZoomController (see {@link BasicGraphGuiZoomController}) will be used. 
	 *  
	 * @return the zoom controller
	 * @see BasicGraphGuiZoomController
	 */
	public ZoomController getZoomController();
	
	/**
	 * Has to return the components to configure the map visualization.
	 * @return the j components for map interaction
	 */
	public Vector<JComponent> getJComponentsForMapInteraction();

	/**
	 * Will be called to destroy map service instances after the deselection in the visualization in the {@link MapSettings}.
	 * Here, all instances within a MapServices should be destroyed in order to save memory. 
	 */
	public void destroyMapServiceInstances();
	
	
}
