package org.awb.env.networkModel.controller.ui;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.ui.configLines.OrthogonalConfiguration;
import org.awb.env.networkModel.settings.LayoutSettings;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;

/**
 * The Class GraphEnvironmentShapePickSupport extends the Jung ShapePickSupport and
 * overwrites {@link #getEdge(Layout, double, double)} method to capture orthogonal
 * and polyline edges (In the original internal method 'getTransformedEdgeShape()'
 * lacks of these two edge types).
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphEnvironmentShapePickSupport extends ShapePickSupport<GraphNode, GraphEdge> {

	/**
	 * Instantiates a new graph environment shape pick support.
	 * @param basicGraphGui the current {@link BasicGraphGui}
	 */
	public GraphEnvironmentShapePickSupport(BasicGraphGuiVisViewer<GraphNode,GraphEdge> visViewer) {
		super(visViewer);
	}
	/**
	 * Instantiates a new graph environment shape pick support.
	 *
	 * @param basicGraphGui the current {@link BasicGraphGui}
	 * @param pickSize the pick size
	 */
	public GraphEnvironmentShapePickSupport(BasicGraphGuiVisViewer<GraphNode,GraphEdge> visViewer, float pickSize) {
		super(visViewer, pickSize);
	}
	/**
	 * Returns the current {@link BasicGraphGuiVisViewer}.
	 * @return the visualization viewer
	 */
	private BasicGraphGuiVisViewer<GraphNode,GraphEdge> getVisualizationViewer() {
		return (BasicGraphGuiVisViewer<GraphNode, GraphEdge>) this.vv;
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.picking.ShapePickSupport#getEdge(edu.uci.ics.jung.algorithms.layout.Layout, double, double)
	 */
	@Override
	public GraphEdge getEdge(Layout<GraphNode, GraphEdge> layout, double x, double y) {
		
		Point2D ip = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.VIEW, new Point2D.Double(x,y));
        x = ip.getX();
        y = ip.getY();

        // as a Line has no area, we can't always use edgeshape.contains(point) so we
        // make a small rectangular pickArea around the point and check if the
        // edgeshape.intersects(pickArea)
        Rectangle2D pickArea = new Rectangle2D.Float((float)x-pickSize/2,(float)y-pickSize/2,pickSize,pickSize);
        GraphEdge closest = null;
        double minDistance = Double.MAX_VALUE;
        while(true) {
            try {
                for(GraphEdge e : getFilteredEdges(layout)) {

                    Shape edgeShape = getTransformedEdgeShape(layout, e);
                    if (edgeShape == null)
                    	continue;

                    // because of the transform, the edgeShape is now a GeneralPath
                    // see if this edge is the closest of any that intersect
                    if(edgeShape.intersects(pickArea)) {
                        float cx=0;
                        float cy=0;
                        float[] f = new float[6];
                        PathIterator pi = new GeneralPath(edgeShape).getPathIterator(null);
                        if(pi.isDone()==false) {
                            pi.next();
                            pi.currentSegment(f);
                            cx = f[0];
                            cy = f[1];
                            if(pi.isDone()==false) {
                                pi.currentSegment(f);
                                cx = f[0];
                                cy = f[1];
                            }
                        }
                        float dx = (float) (cx - x);
                        float dy = (float) (cy - y);
                        float dist = dx * dx + dy * dy;
                        if (dist < minDistance) {
                            minDistance = dist;
                            closest = e;
                        }
                    }
		        }
		        break;
		    } catch(ConcurrentModificationException cme) {}
		}
		return closest;
	}
	
	
	 /**
     * Retrieves the shape template for <code>e</code> and
     * transforms it according to the positions of its endpoints
     * in <code>layout</code>.
     * @param layout the <code>Layout</code> which specifies
     * <code>e</code>'s endpoints' positions
     * @param edge the edge whose shape is to be returned
     * @return
     */
	private Shape getTransformedEdgeShape(Layout<GraphNode, GraphEdge> layout, GraphEdge edge) {
		
		Pair<GraphNode> pair = layout.getGraph().getEndpoints(edge);
		GraphNode v1 = pair.getFirst();
		GraphNode v2 = pair.getSecond();
		
		Point2D p1 = vv.getRenderContext().getMultiLayerTransformer().transform(Layer.LAYOUT, layout.transform(v1));
		Point2D p2 = vv.getRenderContext().getMultiLayerTransformer().transform(Layer.LAYOUT, layout.transform(v2));
        if (p1==null || p2==null) return null;
        
		float x1 = (float) p1.getX();
		float y1 = (float) p1.getY();
		float x2 = (float) p2.getX();
		float y2 = (float) p2.getY();
		
        boolean isLoop = v1.equals(v2);
        boolean isOrthogonal = vv.getRenderContext().getEdgeShapeTransformer() instanceof EdgeShape.Orthogonal || (edge.getEdgeShapeConfiguration()!=null && edge.getEdgeShapeConfiguration() instanceof OrthogonalConfiguration);

		// translate the edge to the starting vertex
		AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);

		Shape edgeShape = vv.getRenderContext().getEdgeShapeTransformer().transform(Context.<Graph<GraphNode, GraphEdge>, GraphEdge>getInstance(vv.getGraphLayout().getGraph(), edge));
		if (isLoop) {
		    // make the loops proportional to the size of the vertex
		    Shape s2 = vv.getRenderContext().getVertexShapeTransformer().transform(v2);
		    Rectangle2D s2Bounds = s2.getBounds2D();
		    xform.scale(s2Bounds.getWidth(),s2Bounds.getHeight());
		    // move the loop so that the nadir is centered in the vertex
		    xform.translate(0, -edgeShape.getBounds2D().getHeight()/2);
		    
        } else if (isOrthogonal==true) {
        	LayoutSettings ls = this.getVisualizationViewer().getCoordinateSystemPositionTransformer().getLayoutSettings();
        	edgeShape = GraphEnvironmentEdgeRenderer.getGeneralPathForOrthogonalConnection(vv.getRenderContext(), layout, edge, x1, y1, x2, y2, ls.getCoordinateSystemXDirection());
		    
		} else {
		    float dx = x2 - x1;
		    float dy = y2 - y1;
		    // rotate the edge to the angle between the vertices
		    double theta = Math.atan2(dy,dx);
		    xform.rotate(theta);
		    // stretch the edge to span the distance between the vertices
		    float dist = (float) Math.sqrt(dx*dx + dy*dy);
		    xform.scale(dist, 1.0f);
		}

		// ----------------------------------------------------------------------------------------
		// => ORIGINAL: edgeShape = xform.createTransformedShape(edgeShape);
		// ----------------------------------------------------------------------------------------
		// => ADJUSTED FORM
		edgeShape = GraphEnvironmentEdgeRenderer.getTransformedShape(edgeShape, this.getVisualizationViewer().getCoordinateSystemPositionTransformer(), xform, vv.getRenderContext(), v1, v2, isOrthogonal); 
		// ----------------------------------------------------------------------------------------		
		return edgeShape;
	}
	
}
