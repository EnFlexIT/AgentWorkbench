package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.geom.Point2D;

import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.positioning.GraphNodePositionFactory;
import org.awb.env.networkModel.positioning.GraphNodePositionFactory.CoordinateType;

import de.enflexit.geography.coordinates.AbstractCoordinate;
import de.enflexit.geography.coordinates.UTMCoordinate;

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
		
		Point2D startPos = startNode.getPosition();
		Point2D endPos   = endNode.getPosition();
		
		// --- In case of geographical coordinates, always use UTM --
		boolean isUseGeographicalCoordinates = GraphNodePositionFactory.isGeographicalCoordinate(startPos) || GraphNodePositionFactory.isGeographicalCoordinate(endPos);
		if (isUseGeographicalCoordinates==true) {
			// --- Make coordinate types equal ----------------------
			startPos = GraphNodePositionFactory.convertToCoordinate((AbstractCoordinate) startPos, CoordinateType.UTM);
			endPos   = GraphNodePositionFactory.convertToCoordinate((AbstractCoordinate) endPos, CoordinateType.UTM);
		}
		
		
		// ----------------------------------------------------------
		// --- Linear function between start and end node -----------
		// ----------------------------------------------------------
		double xC2 = 0;
		double yC2 = 0;
		
		if (endPos.getX()==startPos.getX()) {
			double xCross = endPos.getX();
			double yCross = startPos.getY() + intCoordinate.getX() * (endPos.getY() - startPos.getY());
			double negativeYDiff = Math.signum(startPos.getY()-endPos.getY());
			xC2 = xCross + intCoordinate.getY() * negativeYDiff;
			yC2 = yCross;
			
		} else {
			double a1 = (endPos.getY() - startPos.getY()) / (endPos.getX() - startPos.getX());
			double b1 = startPos.getY() - (a1 * startPos.getX());
			double xCross = startPos.getX() + ((endPos.getX() - startPos.getX()) * intCoordinate.getX());  
			double yCross = a1 * xCross + b1;
			
			double baselineAngle = this.getBaselineAngle(startPos, endPos);
			double negativeGradient = Math.signum(endPos.getX() - startPos.getX());
			xC2 = xCross - intCoordinate.getY() * Math.sin(baselineAngle) * negativeGradient;
			yC2 = yCross + intCoordinate.getY() * Math.cos(baselineAngle) * negativeGradient;
		}

		
		// --- Prepare default return value -------------------------
		Point2D graphCoordinate = new Point2D.Double(xC2, yC2); 
		// --- Do we deal with geographical coordinates? ------------
		if (isUseGeographicalCoordinates==true) {
			// --- Define the calculated UTM coordinate -------------
			UTMCoordinate utmStart = (UTMCoordinate) startPos;
			graphCoordinate = new UTMCoordinate(utmStart.getLongitudeZone(), utmStart.getLatitudeZone(), xC2, yC2);
//			// --- Convert to start node format ---------------------
//			CoordinateType cTypeStart = GraphNodePositionFactory.getCoordinateType(startNode);
//			graphCoordinate = GraphNodePositionFactory.convertToCoordinate((AbstractCoordinate) graphCoordinate, cTypeStart);
		}
		return graphCoordinate;
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
		
		Point2D startPos = startNode.getPosition();
		Point2D endPos   = endNode.getPosition();
		
		// --- In case of geographical coordinates, always use UTM --
		if (GraphNodePositionFactory.isGeographicalCoordinate(startPos) || GraphNodePositionFactory.isGeographicalCoordinate(endPos)) {
			// --- Make coordinate types equal ----------------------
			startPos = GraphNodePositionFactory.convertToCoordinate((AbstractCoordinate) startPos, CoordinateType.UTM);
			endPos   = GraphNodePositionFactory.convertToCoordinate((AbstractCoordinate) endPos, CoordinateType.UTM);
			if (GraphNodePositionFactory.isGeographicalCoordinate(graphCoordinate)==true) {
				graphCoordinate = GraphNodePositionFactory.convertToCoordinate((AbstractCoordinate) graphCoordinate, CoordinateType.UTM);
			}
		}
		
		
		// ----------------------------------------------------------
		// --- Basically, we deal with two linear functions here! ---
		// ----------------------------------------------------------
		double x1 = startPos.getX();
		double y1 = startPos.getY();
		double x2 = endPos.getX();
		double y2 = endPos.getY();
		
		double xC1, yC1;
		if (x1==x2) {
			xC1 = (graphCoordinate.getY()-y1) / (y2-y1);
			yC1 = graphCoordinate.getX() - x1;
			if (y1<y2) {
				yC1 *=-1;
			}

		} else {
			// --- Calculate gradients ------------------------------
			double a1 = (y2 - y1) / (x2 - x1);
			double a2 = Math.tan(Math.atan(a1) + Math.PI /2.0);

			// --- Calculate absolute terms -------------------------
			double b1 = y1 - (a1 * x1); 
			double b2 = graphCoordinate.getY() - (a2 * graphCoordinate.getX());
			
			// --- Calculate their intersection ---------------------
			double xCross = (b2-b1) / (a1-a2);
			double yCross = a1 * xCross + b1;
			
			// --- Determine positive or negative direction ---------
			double xCrossDirection = Math.signum(xCross - x1);
			// --- Distance between start point and xCross ----------
			double c2LengthOfxCross = xCrossDirection * Math.sqrt(Math.pow(xCross - x1, 2) + Math.pow(yCross - y1, 2)); 
			// --- Get distance between start and end node ----------
			double c2LengthOfC1x = this.getC2LengthOfC1x(1.0, startPos, endPos);

			// --- Check y direction in C1 coordinate system -------- 
			double b1GraphCoordinate = graphCoordinate.getY() - (a1 * graphCoordinate.getX());
			double yC1Direction = Math.signum(b1GraphCoordinate - b1);

			// --- Calculate position in coordinate system C1 -------
			xC1 = c2LengthOfxCross / c2LengthOfC1x;
			yC1 = yC1Direction * Math.sqrt(Math.pow(graphCoordinate.getX() - xCross, 2) + Math.pow(graphCoordinate.getY() - yCross, 2));

			// --- x1 < x2 ??? --------------------------------------
			if (x1 > x2) {
				xC1 *=-1;
				yC1 *=-1;
			}
			
		}
		return new Point2D.Double(xC1, yC1);
	}
	
	
	/**
	 * Returns the length of C1 x in the coordinate system C2.
	 *
	 * @param xC1 the x C 1
	 * @param startPos the start position
	 * @param endPos the end position
	 * @return the c 2 length of C 1 x
	 */
	private double getC2LengthOfC1x(double xC1, Point2D startPos, Point2D endPos) {
		double gk = endPos.getY() - startPos.getY();
		double ak = endPos.getX() - startPos.getX();
		return xC1 * Math.sqrt(Math.pow(gk, 2) + Math.pow(ak, 2));
	}
	
	/**
	 * Returns the (baseline) gradient between start and end GraphNode.
	 *
	 * @param startPos the start position
	 * @param endPos the end position
	 * @return the baseline gradient
	 */
	private double getBaselineAngle(Point2D startPos, Point2D endPos) {
		double gk = endPos.getY() - startPos.getY();
		double ak = endPos.getX() - startPos.getX();
		double angle = Math.atan(gk/ak);
		if (angle<0) angle = angle + 2*Math.PI;
		return angle;
	}
	
}
