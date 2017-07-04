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
package agentgui.envModel.graph.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeRenderer;
import edu.uci.ics.jung.visualization.transform.LensTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

/**
 * The Class GraphEnvironmentEdgeRenderer.
 */
public class GraphEnvironmentEdgeRenderer extends BasicEdgeRenderer<GraphNode, GraphEdge> {

	private boolean markerShow = false;
	private float markerStrokeWidth = 0;
	private Color markerColor = new Color(255,0,0, 140);
	
	/**
	 * Checks if is show marker.
	 * @return true, if is show marker
	 */
	public boolean isShowMarker(GraphEdge edge) {
		return this.markerShow;
	}
	/**
	 * Returns the marker stroke width.
	 * @return the marker stroke width
	 */
	public float getMarkerStrokeWidth(GraphEdge edge) {
		return this.markerStrokeWidth;
	}
	/**
	 * Returns the marker color.
	 * @return the marker color
	 */
	public Color getMarkerColor(GraphEdge edge) {
		return this.markerColor;
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.renderers.BasicEdgeRenderer#drawSimpleEdge(edu.uci.ics.jung.visualization.RenderContext, edu.uci.ics.jung.algorithms.layout.Layout, java.lang.Object)
	 */
	@Override
	protected void drawSimpleEdge(RenderContext<GraphNode, GraphEdge> rc, Layout<GraphNode, GraphEdge> layout, GraphEdge e) {
		
		if (this.isShowMarker(e)) {
			
			// --- Background HAS to be shown ----------------------- 
			final GraphicsDecorator graDec = rc.getGraphicsContext();
			Graph<GraphNode, GraphEdge> graph = layout.getGraph();
			Pair<GraphNode> endpoints = graph.getEndpoints(e);
			
			GraphNode v1 = endpoints.getFirst();
			GraphNode v2 = endpoints.getSecond();
			Point2D p1 = layout.transform(v1);
			Point2D p2 = layout.transform(v2);
			p1 = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p1);
			p2 = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p2);
			float x1 = (float) p1.getX();
			float y1 = (float) p1.getY();
			float x2 = (float) p2.getX();
			float y2 = (float) p2.getY();

			boolean isLoop = v1.equals(v2);
			Shape s2 = rc.getVertexShapeTransformer().transform(v2);
			Shape edgeShape = rc.getEdgeShapeTransformer().transform(Context.<Graph<GraphNode, GraphEdge>, GraphEdge> getInstance(graph, e));

			boolean edgeHit = true;
			boolean arrowHit = true;
			Rectangle deviceRectangle = null;
			JComponent vv = rc.getScreenDevice();
			if (vv != null) {
				Dimension d = vv.getSize();
				deviceRectangle = new Rectangle(0, 0, d.width, d.height);
			}

			AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);
			if (isLoop) {
				// this is a self-loop. scale it is larger than the vertex
				// it decorates and translate it so that its nadir is
				// at the center of the vertex.
				Rectangle2D s2Bounds = s2.getBounds2D();
				xform.scale(s2Bounds.getWidth(), s2Bounds.getHeight());
				xform.translate(0, -edgeShape.getBounds2D().getWidth() / 2);
				
			} else if (rc.getEdgeShapeTransformer() instanceof EdgeShape.Orthogonal) {
				float dx = x2 - x1;
				float dy = y2 - y1;
				GeneralPath gp = new GeneralPath();
				gp.moveTo(0, 0);// the xform will do the translation to x1,y1
				if (x1 > x2) {
					if (y1 > y2) {
						gp.lineTo(dx, 0);
						gp.lineTo(dx, dy);
					} else {
						gp.lineTo(dx, 0);
						gp.lineTo(dx, dy);
					}
				} else {
					if (y1 > y2) {
						gp.lineTo(dx, 0);
						gp.lineTo(dx, dy);

					} else {
						gp.lineTo(dx, 0);
						gp.lineTo(dx, dy);
					}
				}
				edgeShape = gp;

			} else {
				// this is a normal edge. Rotate it to the angle between
				// vertex endpoints, then scale it to the distance between
				// the vertices
				float dx = x2 - x1;
				float dy = y2 - y1;
				float thetaRadians = (float) Math.atan2(dy, dx);
				xform.rotate(thetaRadians);
				float dist = (float) Math.sqrt(dx * dx + dy * dy);
				xform.scale(dist, 1.0);
			}
			edgeShape = xform.createTransformedShape(edgeShape);

			MutableTransformer vt = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW);
			if (vt instanceof LensTransformer) {
				vt = ((LensTransformer) vt).getDelegate();
			}
			edgeHit = vt.transform(edgeShape).intersects(deviceRectangle);

			if (edgeHit == true) {

				Paint oldPaint = graDec.getPaint();

				Paint fill_paint = rc.getEdgeFillPaintTransformer().transform(e);
				Paint draw_paint = rc.getEdgeDrawPaintTransformer().transform(e);
				
				// --- Define stroke / color --------------
				BasicStroke newStroke = null;
				Stroke oldStroke = graDec.getStroke();
				float oldStrokeWidth = ((BasicStroke)oldStroke).getLineWidth();

				if (this.getMarkerStrokeWidth(e)<= oldStrokeWidth) {
					newStroke = new BasicStroke(oldStrokeWidth*4);					
				} else {
					newStroke = new BasicStroke(this.getMarkerStrokeWidth(e)); 
				}

				graDec.setPaint(this.getMarkerColor(e));
				graDec.setStroke(newStroke);
				graDec.draw(edgeShape);
				graDec.setStroke(oldStroke);
				
				// check for reverse edge that was already painted
				GraphEdge revEdge = graph.findEdge(v2, v1);
				boolean dontPaint = revEdge!=null;
				
				// get Paints for filling and drawing
				// (filling is done first so that drawing and label use same Paint)
				if (!dontPaint) {
					if (fill_paint != null) {
						graDec.setPaint(fill_paint);
						graDec.fill(edgeShape);
					}
					if (draw_paint != null) {
						graDec.setPaint(draw_paint);
						graDec.draw(edgeShape);
					}
				}

				float scalex = (float) graDec.getTransform().getScaleX();
				float scaley = (float) graDec.getTransform().getScaleY();
				// see if arrows are too small to bother drawing
				if (scalex < .3 || scaley < .3) return;

				// --- Adjust the Arrows to the background size -----
				if (rc.getEdgeArrowPredicate().evaluate(Context.<Graph<GraphNode, GraphEdge>, GraphEdge> getInstance(graph, e))) {
					
					Shape destVertexShape = rc.getVertexShapeTransformer().transform(graph.getEndpoints(e).getSecond());

					AffineTransform xf = AffineTransform.getTranslateInstance(x2, y2);
					destVertexShape = xf.createTransformedShape(destVertexShape);
					
					Paint arrowFillPaint = rc.getArrowFillPaintTransformer().transform(e);
					Paint arrowDrawPaint = rc.getArrowDrawPaintTransformer().transform(e);

					arrowHit = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW).transform(destVertexShape).intersects(deviceRectangle);
					if (arrowHit) {

						@SuppressWarnings("unchecked")
						AffineTransform at = getEdgeArrowRenderingSupport().getArrowTransform(rc, new GeneralPath(edgeShape), destVertexShape);
						if (at == null) return;
						
						Shape arrow = rc.getEdgeArrowTransformer().transform(Context.<Graph<GraphNode, GraphEdge>, GraphEdge> getInstance(graph, e));
						arrow = at.createTransformedShape(arrow);
						graDec.setPaint(arrowFillPaint);
						graDec.fill(arrow);
						graDec.setPaint(arrowDrawPaint);
						graDec.draw(arrow);
					}
					if (graph.getEdgeType(e) == EdgeType.UNDIRECTED) {
						Shape vertexShape = rc.getVertexShapeTransformer().transform(graph.getEndpoints(e).getFirst());
						xf = AffineTransform.getTranslateInstance(x1, y1);
						vertexShape = xf.createTransformedShape(vertexShape);

						arrowHit = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW).transform(vertexShape).intersects(deviceRectangle);

						if (arrowHit) {
							@SuppressWarnings("unchecked")
							AffineTransform at = getEdgeArrowRenderingSupport().getReverseArrowTransform(rc, new GeneralPath(edgeShape), vertexShape, !isLoop);
							if (at == null) return;
							
							Shape arrow = rc.getEdgeArrowTransformer().transform(Context.<Graph<GraphNode, GraphEdge>, GraphEdge> getInstance(graph, e));
							arrow = at.createTransformedShape(arrow);
							graDec.setPaint(arrowFillPaint);
							graDec.fill(arrow);
							graDec.setPaint(arrowDrawPaint);
							graDec.draw(arrow);
						}
					}
				}
				// restore old paint
				graDec.setPaint(oldPaint);
			}
			
		} // end if
		super.drawSimpleEdge(rc, layout, e);
	}
	
}
