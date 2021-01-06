/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package org.awb.env.networkModel.maps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.event.MouseInputListener;

import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import com.sun.javafx.iio.ImageStorage.ImageType;

import de.enflexit.common.ServiceFinder;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;


/**
 * The Class MapPreRenderer is used to prepare the painting of the Graph by
 * downloading maps.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MapPreRenderer<V,E> implements VisualizationViewer.Paintable {

	private BasicGraphGuiVisViewer<V,E> visViewer;
	private MapService mapService;
	
	
	/**
	 * Instantiates a new MapPreRendererr.
	 * @param basicGraphGuiVisViewer the BasicGraphGuiVisViewer
	 */
	public MapPreRenderer(BasicGraphGuiVisViewer<V,E> basicGraphGuiVisViewer) {
		this.visViewer = basicGraphGuiVisViewer;
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.VisualizationServer.Paintable#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics graphics) {
		
		Graphics2D g2d = (Graphics2D) graphics;
    	AffineTransform oldXform = g2d.getTransform();
        AffineTransform lat = this.visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).getTransform();
        AffineTransform vat = this.visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).getTransform();
        AffineTransform at = new AffineTransform();
        at.concatenate(g2d.getTransform());
        at.concatenate(vat);
        at.concatenate(lat);
//        g2d.setTransform(at);
        
        // --- Call the current MapService ------------------------------------
        MapService ms = this.getMapService();
        WGS84LatLngCoordinate wgs84 = new WGS84LatLngCoordinate(51.464471, 7.005437);
        ArrayList<WGS84LatLngCoordinate> positions = new ArrayList<WGS84LatLngCoordinate>();
        positions.add(new WGS84LatLngCoordinate(52.08207410403694, 8.865038740841008)); // northernmost node
        positions.add(new WGS84LatLngCoordinate(52.02531892125302, 8.878751169158551)); // easternmost node
        positions.add(new WGS84LatLngCoordinate(52.011849789452484, 8.838464944855245)); // southernmost node
        positions.add(new WGS84LatLngCoordinate(52.033870658983595,8.807551251936445)); // westernmost node

        if (ms!=null) {
        	
        	MapRenderer mr = ms.getMapRenderer();
        	if (mr!=null) {
        		try {
        			mr.paintMap(g2d, wgs84, this.visViewer.getBounds(), positions);
//        			mr.setMouseInputListener((MouseInputListener) visViewer.getGraphMouse());
        			mr.setMouseWheelListener((MouseWheelListener) visViewer.getGraphMouse());
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
        		
        	} else {
        		System.err.println("[" + this.getClass().getSimpleName() + "] MapService '" + ms.getMapServiceName() + "' does not implement MapRenderer!");
        	}
        }

        g2d.setTransform(oldXform);
	}
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.VisualizationServer.Paintable#useTransform()
	 */
	@Override
	public boolean useTransform() {
		return false;
	}

	/**
	 * Returns the list of registered {@link MapService}s.
	 * @return the map service list
	 */
	private List<MapService> getMapServiceList() {
		
		List<MapService> mapServiceList = ServiceFinder.findServices(MapService.class);
		
		return mapServiceList;
	}
	/**
	 * Return the current map service.
	 * @return the map service
	 */
	private MapService getMapService() {
		if (mapService==null) {
			if (this.getMapServiceList().size()>0) {
				mapService = this.getMapServiceList().get(0);
			}
		}
		return mapService;
	}
}
