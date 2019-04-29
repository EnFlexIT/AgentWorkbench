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
package org.awb.env.networkModel.controller.ui;

import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.collections15.Transformer;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;

import edu.uci.ics.jung.visualization.FourPassImageShaper;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;

/**
 * The Class TransformerForVertexShapeSizeAspectc controls the shape, size, and 
 * aspect ratio for each vertex.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TransformerForVertexShape<V, E> extends AbstractVertexShapeTransformer<GraphNode> implements Transformer<GraphNode, Shape> {

	private GraphEnvironmentController graphController;
	private Map<String, Shape> shapeMap = new HashMap<String, Shape>();

	
	/**
	 * Instantiates a new transformer for vertex shape size aspect.
	 * @param graphController the current {@link GraphEnvironmentController}
	 */
	public TransformerForVertexShape(GraphEnvironmentController graphController) {
		
		this.graphController = graphController;
		this.setSizeTransformer(new Transformer<GraphNode, Integer>() {
			@Override
			public Integer transform(GraphNode node) {
				return (int) node.getGraphElementLayout(graphController.getNetworkModel()).getSize();
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
	 */
	@Override
	public Shape transform(GraphNode node) {

		Shape shape = factory.getEllipse(node); // DEFAULT
		
		String shapeForm = node.getGraphElementLayout(graphController.getNetworkModel()).getShapeForm();
		if (shapeForm==null) {
			// --- nothing to do here ----
		} else  if (shapeForm.equals(GeneralGraphSettings4MAS.SHAPE_RECTANGLE)) {
			shape = factory.getRectangle(node);
			
		} else if (shapeForm.equals(GeneralGraphSettings4MAS.SHAPE_ROUND_RECTANGLE)) {
			shape = factory.getRoundRectangle(node);
			
		} else if (shapeForm.equals(GeneralGraphSettings4MAS.SHAPE_REGULAR_POLYGON)) {
			shape = factory.getRegularPolygon(node, 6);
			
		} else if (shapeForm.equals(GeneralGraphSettings4MAS.SHAPE_REGULAR_STAR)) {
			shape = factory.getRegularStar(node, 6);
			
		} else if (shapeForm.equals(GeneralGraphSettings4MAS.SHAPE_IMAGE_SHAPE)) {
			
			String imageRef = node.getGraphElementLayout(graphController.getNetworkModel()).getImageReference();

			//TODO only rebuild if changed
			//shape = shapeMap.get(imageRef);
			Shape imageShape = null;
			ImageIcon imageIcon = GraphGlobals.getImageIcon(imageRef);
			if (imageIcon != null) {
				Image image = imageIcon.getImage();
				imageShape = FourPassImageShaper.getShape(image, 30);
				if (imageShape .getBounds().getWidth() > 0 && imageShape .getBounds().getHeight() > 0) {
					// don't cache a zero-sized shape, wait for the image to be ready
					int width = image.getWidth(null);
					int height = image.getHeight(null);
					AffineTransform transform = AffineTransform.getTranslateInstance(-width / 2, -height / 2);
					imageShape = transform.createTransformedShape(imageShape );
					this.shapeMap.put(imageRef, imageShape );
				}
			
			} else {
				if (imageRef!=null) {
					System.err.println("Could not find node image '" + imageRef + "'");
				}
			}
			
			if (imageShape!=null) shape = imageShape;
			
		}
		return shape;
	}
}
