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
package agentgui.envModel.graph.maps;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import agentgui.envModel.graph.controller.BasicGraphGuiVisViewer;
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
        g2d.setTransform(at);

        Image mapImage = this.getMapImage();
        if (mapImage!=null) {
        	g2d.drawImage(mapImage, 0, 0, mapImage.getWidth(this.visViewer), mapImage.getWidth(this.visViewer), this.visViewer);	
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
	 * Returns the specified map image.
	 *
	 * @param imageURL the image url
	 * @return the map image
	 */
	private Image getMapImage() {
		
		String imageURL = "https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=11&size=612x612&scale=2&maptype=roadmap";
		// TODO
        System.out.println("Do something!");
        
		BufferedImage mapImage = null;
        try {
        	mapImage = ImageIO.read(new URL(imageURL));
        } catch(Exception ex) {
            System.err.println("Can't load \""+imageURL+"\"");
        }
        return mapImage;
	}
	
}
