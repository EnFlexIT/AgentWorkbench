package org.awb.env.networkModel;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;

import de.enflexit.geography.coordinates.AbstractCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;
import edu.uci.ics.jung.graph.Graph;

/**
 * The Class GraphRectangle2D provides some help methods to investigate the size
 * and the position of a {@link Graph} or a set of {@link GraphNode}s.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class GraphRectangle2D extends Rectangle2D.Double {

    private static final long serialVersionUID = -915850470947212797L;

    public static final int TOP_LEFT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_LEFT = 3;
    public static final int BOTTOM_RIGHT = 4;

    private GraphNode[] graphNodeArray;

    /**
     * Instantiates a new 2D graph rectangle.
     */
    public GraphRectangle2D() {
    }

    /**
     * Instantiates a new 2D graph rectangle.
     * 
     * @param graph the graph with it GraphNodes that define the rectangle
     */
    public GraphRectangle2D(Graph<GraphNode, GraphEdge> graph) {
	this(graph.getVertices());
    }

    /**
     * Instantiates a new 2D graph rectangle.
     * 
     * @param graphNodeCollection the graph nodes collection
     */
    public GraphRectangle2D(Collection<GraphNode> graphNodeCollection) {
	this.setGraphNodeArray(graphNodeCollection);
    }

    /**
     * Instantiates a new 2D graph rectangle.
     * 
     * @param graphNodes array with its GraphNodes that define the rectangle
     */
    public GraphRectangle2D(GraphNode[] graphNodes) {
	this.setGraphNodeArray(graphNodes);
    }

    /**
     * Sets the local graph node array based on the specified collection.
     * 
     * @param graphNodeCollection the graph node collection
     */
    public void setGraphNodeArray(Collection<GraphNode> graphNodeCollection) {
	this.setGraphNodeArray(graphNodeCollection.toArray(new GraphNode[graphNodeCollection.size()]));
    }

    /**
     * Sets the graph node array.
     * 
     * @param graphNodeArray the new graph node array
     */
    public void setGraphNodeArray(GraphNode[] graphNodeArray) {
	this.graphNodeArray = graphNodeArray;
	this.initialize();
    }

    /**
     * Initialize this rectangle.
     */
    private void initialize() {

	boolean firstNodeAdded = false;
	for (int i = 0; i < this.graphNodeArray.length; i++) {

	    try {
		// --- Check if the coordinate needs to be converted ----------
		AbstractCoordinate coordToUse = null;
		AbstractCoordinate coord = this.graphNodeArray[i].getCoordinate();
		if (coord instanceof WGS84LatLngCoordinate) {
		    coordToUse = ((WGS84LatLngCoordinate) coord).getUTMCoordinate();
		} else {
		    coordToUse = coord;
		}

		// --- Get x and y to be integrated in the result -------------
		double x = coordToUse.getX();
		double y = coordToUse.getY();
		if (firstNodeAdded == false) {
		    this.setRect(x, y, 0, 0);
		    firstNodeAdded = true;
		}
		this.add(x, y);

	    } catch (Exception ex) {
		ex.printStackTrace();
	    }

	}
    }

    /**
     * Returns the center point of the current rectangle.
     * 
     * @return the center
     */
    public Point2D getCenter() {
	return new Point2D.Double(this.getCenterX(), this.getCenterY());
    }

    /**
     * Return the specified corner point, where the method parameter can be of the
     * local constant TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT or BOTTOM_RIGHT.
     *
     * @param corner the corner
     * @return the corner point
     */
    public Point2D getCornerPoint(int corner) {

	Point2D cornerPoint = null;
	switch (corner) {
	case TOP_LEFT:
	    cornerPoint = new Point2D.Double(this.getMinX(), this.getMinY());
	    break;
	case TOP_RIGHT:
	    cornerPoint = new Point2D.Double(this.getMinX(), this.getMaxY());
	    break;
	case BOTTOM_LEFT:
	    cornerPoint = new Point2D.Double(this.getMaxX(), this.getMinY());
	    break;
	case BOTTOM_RIGHT:
	    cornerPoint = new Point2D.Double(this.getMaxX(), this.getMaxY());
	    break;
	}
	return cornerPoint;
    }

    /**
     * Returns the corner point list of the current rectangle.
     * 
     * @return the corner point list
     */
    public List<Point2D> getCornerPointList() {

	ArrayList<Point2D> cornerList = new ArrayList<>();
	cornerList.add(this.getCornerPoint(TOP_LEFT));
	cornerList.add(this.getCornerPoint(TOP_RIGHT));
	cornerList.add(this.getCornerPoint(BOTTOM_LEFT));
	cornerList.add(this.getCornerPoint(BOTTOM_RIGHT));
	return cornerList;
    }

    /**
     * Returns the corner point list in transformed JUNG coordinates.
     * 
     * @param transformer the transformer
     * @return the corner point list in jung coordinates
     */
    public List<Point2D> getCornerPointListInJungCoordinates(TransformerForGraphNodePosition transformer) {
	return transformer.getJungPositionList(this.getCornerPointList());
    }

}
