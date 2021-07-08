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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import de.enflexit.geography.coordinates.UTMCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The Class MapPreRenderer is used to prepare the painting of the graph by downloading maps.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @param <V> the value type
 * @param <E> the element type
 */
public class MapPreRenderer implements VisualizationViewer.Paintable {

	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer;
	
	private MapSettings lastMapSettings;
	private AffineTransform lastAffineTransform;
	private Dimension lastVisViewerDimension;
	
	private MapRendererSettings lastMapRendererSettings;
	
	/**
	 * Instantiates a new MapPreRendererr.
	 * @param basicGraphGuiVisViewer the BasicGraphGuiVisViewer
	 */
	public MapPreRenderer(BasicGraphGuiVisViewer<GraphNode, GraphEdge> basicGraphGuiVisViewer) {
		this.visViewer = basicGraphGuiVisViewer;
		this.initializeMapRenderer();
	}
	/**
	 * Initialize map renderer.
	 */
	private void initializeMapRenderer() {

		MapRenderer mr = this.getMapRenderer();
		if (mr!=null) {

			// --- Get the center of the graph to show --------------
			Rectangle2D graphRect = GraphGlobals.getGraphSpreadDimension(this.visViewer.getGraphLayout().getGraph());
			
			// --- Get the center UTM / WGS84 coordinate ------------
			MapSettings mapSet = this.visViewer.getCoordinateSystemPositionTransformer().getMapSettings();
			UTMCoordinate centerUTMCoordinate = new UTMCoordinate(mapSet.getUTMLongitudeZone(), mapSet.getUTMLatitudeZone(), graphRect.getCenterX(), graphRect.getCenterY());
			WGS84LatLngCoordinate centerWGSCoordinate = centerUTMCoordinate.getWGS84LatLngCoordinate(); 
			
			// --- Set center coordinate to renderer ----------------
			mr.setCenterGeoLocation(centerWGSCoordinate);
		}
	}
	
	/**
	 * Returns the current {@link MapRenderer}.
	 * @return the map renderer
	 */
	private MapRenderer getMapRenderer() {
		MapService ms = this.visViewer.getMapService();
		if (ms!=null) {
			MapRenderer mr = ms.getMapRenderer();
        	if (mr!=null) {
        		return mr;
        	}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.VisualizationServer.Paintable#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics graphics) {
		
        // --- Call the current MapService ------------------------------------
        MapService ms = this.visViewer.getMapService();
        if (ms!=null) {
        	MapRenderer mr = ms.getMapRenderer();
        	if (mr!=null) {
        		try {
        			// --------------------------------------------------------
        			// --- Renew MapRendererSettings? -------------------------
        			MapRendererSettings mrs = this.lastMapRendererSettings;
        			if (this.isChangedMapVisualization()==true) {
        				mrs = new MapRendererSettings(this.visViewer);
        				this.lastMapRendererSettings = mrs;
        				this.lastAffineTransform = this.visViewer.getOverallAffineTransform();
        				this.lastVisViewerDimension = this.visViewer.getSize();
        				this.lastMapSettings = this.visViewer.getCoordinateSystemPositionTransformer().getMapSettings().clone();
        			}

        			// --------------------------------------------------------
        			// --- Invoke map tile integration ------------------------  
        			Graphics2D g2d = (Graphics2D) graphics;
        			mr.paintMap(g2d, mrs);
        			
        			// --- Just as a reminder, how map integration works ------
//			        Image mapImage = this.getMapImage();
//			        if (mapImage!=null) {
//			        	g2d.drawImage(mapImage, 0, 0, mapImage.getWidth(this.visViewer), mapImage.getWidth(this.visViewer), this.visViewer);	
//			        }
			        
				} catch (Exception ex) {
					ex.printStackTrace();
				}
        		
        	} else {
        		System.err.println("[" + this.getClass().getSimpleName() + "] MapService '" + ms.getMapServiceName() + "' does not implement MapRenderer!");
        	}
        }
	}
	/**
	 * Checks if the map visualization settings have changed.
	 *
	 * @param graphics the graphics
	 * @return true, if is changed overall affine transform
	 */
	private boolean isChangedMapVisualization() {
		
		if (this.lastMapRendererSettings!=null && this.lastAffineTransform!=null && this.lastMapSettings!=null && this.lastVisViewerDimension!=null) {
			// --- Check AffineTransform ------------------
			boolean isChangedAffineTransform = ! this.visViewer.getOverallAffineTransform().equals(this.lastAffineTransform);
			if (isChangedAffineTransform==true) return true;
			
			// --- Check VisViewer dimension --------------
			boolean isChangedVisViewerDimension = ! this.visViewer.getSize().equals(this.lastVisViewerDimension);
			if (isChangedVisViewerDimension==true) return true;
			
			// --- Check MapSettings ----------------------
			MapSettings currMapSettings = this.visViewer.getCoordinateSystemPositionTransformer().getMapSettings();
			boolean isChangedMapSetting = ! currMapSettings.equals(this.lastMapSettings); 
			
			return isChangedMapSetting;
		}
		return true;   
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.VisualizationServer.Paintable#useTransform()
	 */
	@Override
	public boolean useTransform() {
		return false;
	}

}
