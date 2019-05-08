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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.configLines.IntermediatePointTransformer;
import org.awb.env.networkModel.controller.ui.configLines.OrthogonalConfiguration;
import org.awb.env.networkModel.settings.LayoutSettings;
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemXDirection;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeIndexFunction;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.EdgeShape.IndexedRendering;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeRenderer;
import edu.uci.ics.jung.visualization.transform.LensTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

/**
 * The Class GraphEnvironmentEdgeRenderer.
 */
public class GraphEnvironmentEdgeRenderer extends BasicEdgeRenderer<GraphNode, GraphEdge> {

	private static IntermediatePointTransformer intPointTransformer;
	
	private GraphEnvironmentController graphController;
	
	private boolean markerShow = false;
	private float markerStrokeWidth = 0;
	private Color markerColor = new Color(255,0,0, 140);
	
	
	/**
	 * Instantiates a new graph environment edge renderer.
	 * @param graphController the current GraphEnvironmentController
	 */
	public GraphEnvironmentEdgeRenderer(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	
	/**
	 * Checks if is show marker.
	 *
	 * @param edge the GraphEdge
	 * @return true, if is show marker
	 */
	public boolean isShowMarker(GraphEdge edge) {
		return this.markerShow;
	}
	/**
	 * Returns the marker stroke width.
	 *
	 * @param edge the GraphEdge
	 * @return the marker stroke width
	 */
	public float getMarkerStrokeWidth(GraphEdge edge) {
		return this.markerStrokeWidth;
	}
	/**
	 * Returns the marker color.
	 *
	 * @param edge the GraphEdge
	 * @return the marker color
	 */
	public Color getMarkerColor(GraphEdge edge) {
		return this.markerColor;
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.renderers.BasicEdgeRenderer#drawSimpleEdge(edu.uci.ics.jung.visualization.RenderContext, edu.uci.ics.jung.algorithms.layout.Layout, java.lang.Object)
	 */
	@Override
	protected void drawSimpleEdge(RenderContext<GraphNode, GraphEdge> rc, Layout<GraphNode, GraphEdge> layout, GraphEdge edge) {
		
		// --- Draw a marked edge ? -----------------------
		if (this.isShowMarker(edge)==true) {
			this.drawSimpleEdgeAdjusted(rc, layout, edge, true);
		}
		// --- Do regular (but corrected) edge drawing ----
		this.drawSimpleEdgeAdjusted(rc, layout, edge, false);
	}
 	
	/**
	 * Draws the specified simple edge and represents basically the original but adjusted super method.
	 *
	 * @param rc the current RenderContext
	 * @param layout the layout
	 * @param edge the edge to draw
	 * @param isPaintMarker the is paint marker
	 */
	@SuppressWarnings("unchecked")
	private void drawSimpleEdgeAdjusted(RenderContext<GraphNode, GraphEdge> rc, Layout<GraphNode, GraphEdge> layout, GraphEdge edge, boolean isPaintMarker) {
	        
        GraphicsDecorator graphicsDecorator = rc.getGraphicsContext();
        Graph<GraphNode, GraphEdge> graph = layout.getGraph();
        Pair<GraphNode> endpoints = graph.getEndpoints(edge);
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
        
        Shape s2 = rc.getVertexShapeTransformer().transform(v2);
        Shape edgeShape = rc.getEdgeShapeTransformer().transform(Context.<Graph<GraphNode, GraphEdge>,GraphEdge>getInstance(graph, edge));
        
        boolean edgeHit = true;
        boolean arrowHit = true;
        Rectangle deviceRectangle = null;
        JComponent vv = rc.getScreenDevice();
        if (vv != null) {
            Dimension d = vv.getSize();
            deviceRectangle = new Rectangle(0,0,d.width,d.height);
        }

        // --- Do some layout checks --------------------------------------------------------------
        LayoutSettings ls = this.graphController.getNetworkModel().getLayoutSettings();
        
        boolean isLoop = v1.equals(v2);
        boolean isOrthogonalConfigurableLine = (ls.getEdgeShape()==org.awb.env.networkModel.settings.LayoutSettings.EdgeShape.ConfigurableLine && edge.getEdgeShapeConfiguration()!=null && edge.getEdgeShapeConfiguration() instanceof OrthogonalConfiguration);
        boolean isOrthogonal = rc.getEdgeShapeTransformer() instanceof EdgeShape.Orthogonal || isOrthogonalConfigurableLine;
        
        AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);
        if (isLoop==true) {
            // this is a self-loop. scale it is larger than the vertex
            // it decorates and translate it so that its nadir is
            // at the center of the vertex.
            Rectangle2D s2Bounds = s2.getBounds2D();
            xform.scale(s2Bounds.getWidth(),s2Bounds.getHeight());
            xform.translate(0, -edgeShape.getBounds2D().getWidth()/2);
            
        } else if (isOrthogonal==true) {
            edgeShape = getGeneralPathForOrthogonalConnection(rc, layout, edge, x1, y1, x2, y2, ls.getCoordinateSystemXDirection());
        	
        } else {
            // this is a normal edge. Rotate it to the angle between
            // vertex endpoints, then scale it to the distance between
            // the vertices
            float dx = x2-x1;
            float dy = y2-y1;
            float thetaRadians = (float) Math.atan2(dy, dx);
            xform.rotate(thetaRadians);
            float dist = (float) Math.sqrt(dx*dx + dy*dy);
            xform.scale(dist, 1.0);
        }
        
		// ----------------------------------------------------------------------------------------
		// => ORIGINAL: edgeShape = xform.createTransformedShape(edgeShape);
		// ----------------------------------------------------------------------------------------
        // => ADJUSTED FORM
        edgeShape = getTransformedShape(edgeShape, this.graphController, xform, rc, v1, v2, isOrthogonal);
		// ----------------------------------------------------------------------------------------
		
        MutableTransformer vt = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW);
        if(vt instanceof LensTransformer) {
        	vt = ((LensTransformer)vt).getDelegate();
        }
        edgeHit = vt.transform(edgeShape).intersects(deviceRectangle);

        if (edgeHit == true) {
            
            Paint oldPaint = graphicsDecorator.getPaint();
            
            // get Paints for filling and drawing
            // (filling is done first so that drawing and label use same Paint)
            Paint fill_paint = rc.getEdgeFillPaintTransformer().transform(edge); 
            Paint draw_paint = rc.getEdgeDrawPaintTransformer().transform(edge);
            if (isPaintMarker==true) {
            	// ----------------------------------------
            	// --- Paint the marker of the edge -------
            	// ----------------------------------------
            	// --- Define stroke / color --------------
				BasicStroke newStroke = null;
				BasicStroke oldStroke = (BasicStroke) graphicsDecorator.getStroke();
				float oldStrokeWidth = oldStroke.getLineWidth();

				if (this.getMarkerStrokeWidth(edge) <= oldStrokeWidth) {
					newStroke = new BasicStroke(oldStrokeWidth * 4);
				} else {
					newStroke = new BasicStroke(this.getMarkerStrokeWidth(edge));
				}
				graphicsDecorator.setPaint(this.getMarkerColor(edge));
				graphicsDecorator.setStroke(newStroke);
				graphicsDecorator.draw(edgeShape);
				graphicsDecorator.setStroke(oldStroke);

            } else {
            	// ----------------------------------------
            	// --- Regular paint ----------------------
            	// ----------------------------------------
				if (fill_paint != null) {
					graphicsDecorator.setPaint(fill_paint);
					graphicsDecorator.fill(edgeShape);
				}
				if (draw_paint != null) {
					graphicsDecorator.setPaint(draw_paint);
					graphicsDecorator.draw(edgeShape);
				}
            }
			
			// check for reverse edge that was already painted
			GraphEdge revEdge = graph.findEdge(v2, v1);
			boolean dontPaint = revEdge!=null;
			
			// get Paints for filling and drawing
			// (filling is done first so that drawing and label use same Paint)
			if (!dontPaint) {
				if (fill_paint != null) {
					graphicsDecorator.setPaint(fill_paint);
					graphicsDecorator.fill(edgeShape);
				}
				if (draw_paint != null) {
					graphicsDecorator.setPaint(draw_paint);
					graphicsDecorator.draw(edgeShape);
				}
			}
            
            float scalex = (float)graphicsDecorator.getTransform().getScaleX();
            float scaley = (float)graphicsDecorator.getTransform().getScaleY();
            // see if arrows are too small to bother drawing
            if(scalex < .3 || scaley < .3) return;
            
            if (rc.getEdgeArrowPredicate().evaluate(Context.<Graph<GraphNode, GraphEdge>, GraphEdge> getInstance(graph, edge))) {
            	
                Stroke new_stroke = rc.getEdgeArrowStrokeTransformer().transform(edge);
                Stroke old_stroke = graphicsDecorator.getStroke();
                if (new_stroke != null)
                    graphicsDecorator.setStroke(new_stroke);

                
                Shape destVertexShape = rc.getVertexShapeTransformer().transform(graph.getEndpoints(edge).getSecond());

                AffineTransform xf = AffineTransform.getTranslateInstance(x2, y2);
                destVertexShape = xf.createTransformedShape(destVertexShape);
                
                arrowHit = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW).transform(destVertexShape).intersects(deviceRectangle);
                if(arrowHit) {
                    
                    AffineTransform at = edgeArrowRenderingSupport.getArrowTransform(rc, edgeShape, destVertexShape);
                    if(at == null) return;
                    Shape arrow = rc.getEdgeArrowTransformer().transform(Context.<Graph<GraphNode, GraphEdge>, GraphEdge> getInstance(graph, edge));
                    arrow = at.createTransformedShape(arrow);
                    graphicsDecorator.setPaint(rc.getArrowFillPaintTransformer().transform(edge));
                    graphicsDecorator.fill(arrow);
                    graphicsDecorator.setPaint(rc.getArrowDrawPaintTransformer().transform(edge));
                    graphicsDecorator.draw(arrow);
                }
                if (graph.getEdgeType(edge) == EdgeType.UNDIRECTED) {
                    Shape vertexShape = 
                        rc.getVertexShapeTransformer().transform(graph.getEndpoints(edge).getFirst());
                    xf = AffineTransform.getTranslateInstance(x1, y1);
                    vertexShape = xf.createTransformedShape(vertexShape);
                    
                    arrowHit = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW).transform(vertexShape).intersects(deviceRectangle);
                    
                    if(arrowHit) {
                        AffineTransform at = edgeArrowRenderingSupport.getReverseArrowTransform(rc, edgeShape, vertexShape, !isLoop);
                        if(at == null) return;
                        Shape arrow = rc.getEdgeArrowTransformer().transform(Context.<Graph<GraphNode, GraphEdge>, GraphEdge> getInstance(graph, edge));
                        arrow = at.createTransformedShape(arrow);
                        graphicsDecorator.setPaint(rc.getArrowFillPaintTransformer().transform(edge));
                        graphicsDecorator.fill(arrow);
                        graphicsDecorator.setPaint(rc.getArrowDrawPaintTransformer().transform(edge));
                        graphicsDecorator.draw(arrow);
                    }
                }
                // restore paint and stroke
                if (new_stroke != null)
                    graphicsDecorator.setStroke(old_stroke);

            }
            
            // restore old paint
            graphicsDecorator.setPaint(oldPaint);
        }
    }
	
	
	/**
	 * Returns the transformed shape.
	 *
	 * @param shape the shape to transform
	 * @param graphController the graph controller
	 * @param affineTransform the (original) affine transform
	 * @param rc the current RenderContext
	 * @param startNode the start node
	 * @param endNode the end node
	 * @param isOrthogonal the indicator if an orthogonal edge is to be drawn
	 * @return the transformed shape
	 */
	public static Shape getTransformedShape(Shape shape, GraphEnvironmentController graphController, AffineTransform affineTransform, RenderContext<GraphNode, GraphEdge> rc, GraphNode startNode, GraphNode endNode, boolean isOrthogonal) {
		
		Shape newShape = null;
		
		TransformerForGraphNodePosition<GraphNode, GraphEdge> csTransformer = new TransformerForGraphNodePosition<>(graphController); 
		
		if (isOrthogonal==false && shape instanceof GeneralPath) {
			// --------------------------------------------
			// --- The Polyline transformation ------------
			// --------------------------------------------
			GeneralPath gPath = (GeneralPath) shape;
			GeneralPath gPathNew = new GeneralPath();

			// --- Add the start graph node ---------------
			Point2D graphPoint = csTransformer.transform(startNode);
			Point2D panelPoint = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, graphPoint);
			gPathNew.moveTo(panelPoint.getX(), panelPoint.getY());
			
			// --- Add the intermediate points ------------
			double[] coords = new double[6];
			for (PathIterator pIterator = gPath.getPathIterator(null); !pIterator.isDone(); pIterator.next()) {

				pIterator.currentSegment(coords);
				double xCoord = coords[0];
				double yCoord = coords[1];
				
				if (! (xCoord==0.0 && yCoord==0.0 || xCoord==1.0 && yCoord==0.0)) {
					Point2D scaledPoint = getIntermediatePointTransformer().transformToGraphCoordinate(new Point2D.Double(xCoord, yCoord), startNode, endNode);
					graphPoint = csTransformer.transform(scaledPoint);
					panelPoint = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, graphPoint);
					gPathNew.lineTo(panelPoint.getX(), panelPoint.getY());
				}
			}
			
			// --- Add the end graph node -----------------
			graphPoint = csTransformer.transform(endNode);
			panelPoint = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, graphPoint);
			gPathNew.lineTo(panelPoint.getX(), panelPoint.getY());
			newShape = gPathNew;
			
		} else if (shape instanceof QuadCurve2D) {
			// --------------------------------------------
			// --- The QuadCurve2D transformation --------- 
			// --------------------------------------------
			QuadCurve2D quadCurve = (QuadCurve2D) shape;
			QuadCurve2D quadCurveNew = new QuadCurve2D.Double();
			
			Point2D graphStartPoint = csTransformer.transform(startNode);
			Point2D panelStartPoint = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, graphStartPoint);
			
			Point2D graphEndPoint = csTransformer.transform(endNode); 
			Point2D panelEndPoint = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, graphEndPoint);
			
			Point2D scaledControlPoint = getIntermediatePointTransformer().transformToGraphCoordinate(quadCurve.getCtrlPt(), startNode, endNode);
			Point2D graphControlPoint = csTransformer.transform(scaledControlPoint);
			Point2D panelControlPoint = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, graphControlPoint); 
			
			quadCurveNew.setCurve(panelStartPoint, panelControlPoint, panelEndPoint);
			newShape = quadCurveNew;
			
		} else {
			// --------------------------------------------
			// --- The regular shape transformation -------
			// --------------------------------------------
			newShape = affineTransform.createTransformedShape(shape);

		}
		return newShape;
	}
	/**
	 * Returns the intermediate point transformer.
	 * @return the intermediate point transformer
	 */
	private static IntermediatePointTransformer getIntermediatePointTransformer() {
		if (intPointTransformer==null) {
			intPointTransformer = new IntermediatePointTransformer(); 
		}
		return intPointTransformer;
	}
	
	
	/**
	 * Gets a GeneralPath for an orthogonal connection.
	 *
	 * @param rc the RenderContext
	 * @param layout the layout
	 * @param edge the edge
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @param xDirection the x direction
	 * @return the general path for orthogonal connection
	 */
	public static GeneralPath getGeneralPathForOrthogonalConnection(RenderContext<GraphNode, GraphEdge> rc, Layout<GraphNode, GraphEdge> layout, GraphEdge edge, float x1, float y1, float x2, float y2, CoordinateSystemXDirection xDirection) {
		
        Graph<GraphNode, GraphEdge> graph = layout.getGraph();
        
        int index = 0;
        if (rc.getEdgeShapeTransformer() instanceof IndexedRendering) {
        	@SuppressWarnings("unchecked")
			EdgeIndexFunction<GraphNode, GraphEdge> peif = ((IndexedRendering<GraphNode, GraphEdge>)rc.getEdgeShapeTransformer()).getEdgeIndexFunction();
        	index = peif.getIndex(graph, edge);
        	index *= 20;
        }

        float dx = x2-x1;
        float dy = y2-y1;

        GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);// the xform will do the translation to x1,y1
        
        // --- Check to which direction we have to move first ------- 
        boolean isFirstMoveInY = false;
        if (edge.getEdgeShapeConfiguration()!=null && edge.getEdgeShapeConfiguration() instanceof OrthogonalConfiguration) {
        	OrthogonalConfiguration orthConfig = (OrthogonalConfiguration) edge.getEdgeShapeConfiguration();
        	isFirstMoveInY = orthConfig.isFirstMoveInY();
        }
        // --- Invert the direction indicator? ----------------------
        if (xDirection==CoordinateSystemXDirection.North || xDirection==CoordinateSystemXDirection.South) {
        	isFirstMoveInY = !isFirstMoveInY;
        }
        
        if (isFirstMoveInY==false) {
        	// ------------------------------------------------------
        	// --- The regular case to first move in x-direction ----
        	// ------------------------------------------------------
        	if(x1 > x2) {
            	if(y1 > y2) {
            		gp.lineTo(0, index);
            		gp.lineTo(dx-index, index);
            		gp.lineTo(dx-index, dy);
            		gp.lineTo(dx, dy);
            	} else {
            		gp.lineTo(0, -index);
            		gp.lineTo(dx-index, -index);
            		gp.lineTo(dx-index, dy);
            		gp.lineTo(dx, dy);
            	}

            } else {
            	if(y1 > y2) {
            		gp.lineTo(0, index);
            		gp.lineTo(dx+index, index);
            		gp.lineTo(dx+index, dy);
            		gp.lineTo(dx, dy);
            		
            	} else {
            		gp.lineTo(0, -index);
            		gp.lineTo(dx+index, -index);
            		gp.lineTo(dx+index, dy);
            		gp.lineTo(dx, dy);
            	}
            }

        } else {
        	// ------------------------------------------------------
        	// --- The case to first move in y-direction ------------
        	// ------------------------------------------------------
        	if(x1 > x2) {
            	if(y1 > y2) {
            		gp.lineTo(index, 0);
            		gp.lineTo(index, dy-index);
            		gp.lineTo(dx, dy-index);
            		gp.lineTo(dx, dy);
            	} else {
            		gp.lineTo(-index, 0);
            		gp.lineTo(index, dy-index);
            		gp.lineTo(dx, dy-index);
            		gp.lineTo(dx, dy);
            	}

            } else {
            	if(y1 > y2) {
            		gp.lineTo(index, 0);
            		gp.lineTo(index, dy+index);
            		gp.lineTo(dx, dy+index);
            		gp.lineTo(dx, dy);
            		
            	} else {
            		gp.lineTo(-index, 0);
            		gp.lineTo(-index, dy+index);
            		gp.lineTo(dx, dy+index);
            		gp.lineTo(dx, dy);
            	}
            }
        }
        return gp;
	}
	
}
