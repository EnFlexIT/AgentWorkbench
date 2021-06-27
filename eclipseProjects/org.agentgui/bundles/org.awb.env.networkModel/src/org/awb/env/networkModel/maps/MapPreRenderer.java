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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The Class MapPreRenderer is used to prepare the painting of the graph by downloading maps.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @param <V> the value type
 * @param <E> the element type
 */
public class MapPreRenderer<V,E> implements VisualizationViewer.Paintable {

	private BasicGraphGuiVisViewer<V,E> visViewer;
	private AffineTransform lastAffineTransform;
	
	private MapRendererSettings lastMapRendererSettings;
	
	/**
	 * Instantiates a new MapPreRendererr.
	 * @param basicGraphGuiVisViewer the BasicGraphGuiVisViewer
	 */
	public MapPreRenderer(BasicGraphGuiVisViewer<V,E> basicGraphGuiVisViewer) {
		this.visViewer = basicGraphGuiVisViewer;
	}

	
	/**
	 * Checks if the overall affine transform was changed.
	 *
	 * @param graphics the graphics
	 * @return true, if is changed overall affine transform
	 */
	private boolean isChangedOverallAffineTransform() {
		
		boolean isChanged = false; 
		
		// --- Get new, concatenated transformer ------------------------------
        AffineTransform currAffineTransform = this.visViewer.getOverallAffineTransform();
        if (this.lastAffineTransform==null) {
        	isChanged = true;
        	this.lastAffineTransform = currAffineTransform;
        } else {
        	isChanged = ! currAffineTransform.equals(this.lastAffineTransform);
        }
		return isChanged;
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
        			// --- Renew MapRendererSettings? -------------------------
        			MapRendererSettings mrs = this.lastMapRendererSettings;
        			if (mrs==null || this.isChangedOverallAffineTransform()==true) {
        				mrs = new MapRendererSettings(this.visViewer);
        				this.lastMapRendererSettings = mrs;
        				this.lastAffineTransform = this.visViewer.getOverallAffineTransform();
        			}
        			
        			// --- Invoke map tile integration ------------------------  
        			Graphics2D g2d = (Graphics2D) graphics;
        			mr.paintMap(g2d, mrs);
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
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.VisualizationServer.Paintable#useTransform()
	 */
	@Override
	public boolean useTransform() {
		return false;
	}

}
