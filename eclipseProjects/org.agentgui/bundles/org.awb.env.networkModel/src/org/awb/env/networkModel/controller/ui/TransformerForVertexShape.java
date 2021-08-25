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
import org.awb.env.networkModel.maps.MapSettings;
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
	
	private Transformer<GraphNode, Integer> nodeSizeTransformer;

	private HashMap<String, Shape> imageShapeHashMap;
	private AffineTransform shapeScaleTransformer;

	
	/**
	 * Instantiates a new transformer for the vertex shape and size.
	 * @param graphController the current {@link GraphEnvironmentController}
	 */
	public TransformerForVertexShape(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		this.setSizeTransformer(this.getNodeSizeTransformer());
	}
	/**
	 * Returns the scale multiplier that is used during a map visualization.
	 * @return the scale multiplier
	 */
	private int getScaleMultiplier() {
		int scaleMultiplier = 1;
		MapSettings ms = TransformerForVertexShape.this.graphController.getNetworkModel().getMapSettings();
		if (ms!=null) {
			scaleMultiplier = ms.getMapScale().getScaleMultiplier();
		}
		return scaleMultiplier;
	}

	/**
	 * Returns the shape hash map.
	 * @return the shape hash map
	 */
	public Map<String, Shape> getImageShapeHashMap() {
		if (imageShapeHashMap==null) {
			imageShapeHashMap = new HashMap<>();
		}
		return imageShapeHashMap;
	}
	/**
	 * Returns the local shape scale transformer.
	 * @return the shape scale transformer
	 */
	private AffineTransform getShapeScaleTransformer() {
		if (shapeScaleTransformer==null) {
			shapeScaleTransformer = new AffineTransform();
		}
		return shapeScaleTransformer;
	}
	
	/**
	 * Gets the node size transformer.
	 * @return the node size transformer
	 */
	private Transformer<GraphNode, Integer> getNodeSizeTransformer() {
		if (nodeSizeTransformer==null) {
			nodeSizeTransformer = new Transformer<GraphNode, Integer>() {
				@Override
				public Integer transform(GraphNode graphNode) {
					return TransformerForVertexShape.this.getScaleMultiplier() * (int) graphNode.getGraphElementLayout(graphController.getNetworkModel()).getSize();
				}
			};
		}
		return nodeSizeTransformer;
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
			
			boolean isdebugPrintShapeScaleResults = false;
			int scaleMultiplier = this.getScaleMultiplier();
			String imageRef = node.getGraphElementLayout(this.graphController.getNetworkModel()).getImageReference();
			String hashKey = scaleMultiplier + "|" + imageRef;
			
			Shape imageShape = this.getImageShapeHashMap().get(hashKey);
			if (imageShape==null) {
				// --- Get the image shape for the reference ------------------
				ImageIcon imageIcon = GraphGlobals.getImageIcon(imageRef);
				if (imageIcon != null) {
					Image image = imageIcon.getImage();
					imageShape = FourPassImageShaper.getShape(image, 30);
					// --- Cache the image shape, if not zero-sized ----------- 
					if (imageShape!=null && imageShape.getBounds().getWidth()>0 && imageShape.getBounds().getHeight()>0) {
						
						int width = image.getWidth(null);
						int height = image.getHeight(null);
						AffineTransform transform = AffineTransform.getTranslateInstance(-width / 2, -height / 2);
						imageShape = transform.createTransformedShape(imageShape);
						
						// --- Scale the image shape --------------------------
						if (scaleMultiplier>1) {
							this.getShapeScaleTransformer().scale(this.getScaleMultiplier(), this.getScaleMultiplier());
							this.getShapeScaleTransformer().createTransformedShape(imageShape);
						}
						// --- Remind in local HashMap ------------------------
						this.getImageShapeHashMap().put(hashKey, imageShape);
						shape = imageShape;
						
						// --- Print shape scale results to console -----------
						if (isdebugPrintShapeScaleResults==true) {
							width  = (int) imageShape.getBounds().getWidth();
							height = (int) imageShape.getBounds().getHeight();
							System.out.println("[" + imageRef + "] Image-size: w=" + image.getWidth(null) + ", h=" + image.getHeight(null) + " - Shape-size: w=" + width + ", h=" + height);
						}
					}
				
				} else {
					if (imageRef!=null) {
						System.err.println("[" + this.getClass().getSimpleName() + "] Could not find node image '" + imageRef + "'");
					}
				}
			}
		}
		return shape;
	}
}
