package org.awb.env.networkModel.positioning;

import java.awt.geom.Point2D;
import java.util.TreeMap;

import de.enflexit.geography.coordinates.AbstractCoordinate;

/**
 * The Class GraphNodePositionTreeMap.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class GraphNodePositionTreeMap extends TreeMap<String, AbstractCoordinate> {

	private static final long serialVersionUID = 6566506851970092587L;

	/**
	 * Will convert the specified Point2D into a layout depending coordinate (see layoutID).
	 * If successful converted the coordinate will be added to the local TreeMap 
	 *
	 * @param layoutID the layout ID
	 * @param point2D the point 2 D
	 * @return the abstract coordinate
	 */
	public AbstractCoordinate put(String layoutID, Point2D point2D) {
		
		if (layoutID==null || point2D==null) return null;
		
		AbstractCoordinate coordinate = GraphNodePositionFactory.convertToCoordinate(layoutID, point2D);
		if (coordinate!=null) {
			coordinate = this.put(layoutID, coordinate);
		}
		return coordinate;
	}
	
}
