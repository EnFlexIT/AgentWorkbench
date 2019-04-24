package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.geom.Point2D;

import org.awb.env.networkModel.GraphNode;

/**
 * The Class IntermediatePointTransformer can be used to transform the position of an intermediate 
 * point in coordinate system no. 1 (C1) of a GraphEdge to the current graph coordinates that is
 * described in coordinate system no.2 (C2).
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class IntermediatePointTransformer {

	/**
	 * Transform to graph coordinate.
	 *
	 * @param intCoordinate the intermediate coordinate
	 * @param startNode the start node
	 * @param endNode the end node
	 * @return the point in the coordinate system no. 2 (C2)
	 */
	public Point2D transformToGraphCoordinate(Point2D intCoordinate, GraphNode startNode, GraphNode endNode) {
		
		// --- Linear function between start and end node -----------
		double a1 = (endNode.getPosition().getY() - startNode.getPosition().getY()) / (endNode.getPosition().getX() - startNode.getPosition().getX());
		double b1 = startNode.getPosition().getY() - (a1 * startNode.getPosition().getX());
		
		double xCross = startNode.getPosition().getX() + ((endNode.getPosition().getX() - startNode.getPosition().getX()) * intCoordinate.getX());  
		double yCross = a1 * xCross + b1;
		
		double baselineAngle = this.getBaselineAngle(startNode, endNode);
		double negativeGradient = Math.signum(endNode.getPosition().getX() - startNode.getPosition().getX());
		double xC2 = xCross - intCoordinate.getY() * Math.sin(baselineAngle) * negativeGradient;
		double yC2 = yCross + intCoordinate.getY() * Math.cos(baselineAngle) * negativeGradient;
		
		return new Point2D.Double(xC2, yC2);
	}
	
	
	/**
	 * Transform to intermediate coordinate.
	 *
	 * @param graphCoordinate the graph coordinate
	 * @param startNode the start node
	 * @param endNode the end node
	 * @return the point in coordinate system no. 1 (C1) intermediate coordinate
	 */
	public Point2D transformToIntermediateCoordinate(Point2D graphCoordinate, GraphNode startNode, GraphNode endNode) {
		
		// ----------------------------------------------------------
		// --- Basically, we deal with two linear functions here! ---
		// ----------------------------------------------------------
		
		// --- Calculate gradients ----------------------------------
		double a1 = (endNode.getPosition().getY() - startNode.getPosition().getY()) / (endNode.getPosition().getX() - startNode.getPosition().getX());
		double a2 = Math.tan(Math.atan(a1) + Math.PI /2.0);

		// --- Calculate absolute terms -----------------------------
		double b1 = startNode.getPosition().getY() - (a1 * startNode.getPosition().getX()); 
		double b2 = graphCoordinate.getY() - (a2 * graphCoordinate.getX());
		
		// --- Calculate their intersection -------------------------
		double xCross = (b2-b1) / (a1-a2);
		double yCross = a1 * xCross + b1;
		
		// --- Determine positive or negative direction -------------
		double xCrossDirection = Math.signum(xCross - startNode.getPosition().getX());
		// --- Calculate distance between start point and xCross ----
		double c2LengthOfxCross = xCrossDirection * Math.sqrt(Math.pow(xCross - startNode.getPosition().getX(), 2) + Math.pow(yCross - startNode.getPosition().getY(), 2)); 
		// --- Get distance between start and end node --------------
		double c2LengthOfC1x = this.getC2LengthOfC1x(1.0, startNode, endNode);

		// --- Check y direction in C1 coordinate system ------------ 
		double b1GraphCoordinate = graphCoordinate.getY() - (a1 * graphCoordinate.getX());
		double yC1Direction = Math.signum(b1GraphCoordinate - b1);

		// --- Calculate position in coordinate system C1 -----------
		double xC1 = c2LengthOfxCross / c2LengthOfC1x;
		double yC1 = yC1Direction * Math.sqrt(Math.pow(graphCoordinate.getX() - xCross, 2) + Math.pow(graphCoordinate.getY() - yCross, 2));
		
		return new Point2D.Double(xC1, yC1);
	}
	
	
	/**
	 * Returns the length of C1 x in the coordinate system C2.
	 *
	 * @param xC1 the x C 1
	 * @param startNode the start graph node
	 * @param endNode the end graph node
	 * @return the c 2 length of C 1 x
	 */
	private double getC2LengthOfC1x(double xC1, GraphNode startNode, GraphNode endNode) {
		double gk = endNode.getPosition().getY() - startNode.getPosition().getY();
		double ak = endNode.getPosition().getX() - startNode.getPosition().getX();
		return xC1 * Math.sqrt(Math.pow(gk, 2) + Math.pow(ak, 2));
	}
	
	/**
	 * Returns the (baseline) gradient between start and end GraphNode.
	 *
	 * @param startNode the start graph node
	 * @param endNode the end graph node
	 * @return the baseline gradient
	 */
	private double getBaselineAngle(GraphNode startNode, GraphNode endNode) {
		double gk = endNode.getPosition().getY() - startNode.getPosition().getY();
		double ak = endNode.getPosition().getX() - startNode.getPosition().getX();
		double angle = Math.atan(gk/ak);
		if (angle<0) angle = angle + 2*Math.PI;
		return angle;
	}
	
}
