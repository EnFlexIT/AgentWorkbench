package org.awb.env.networkModel.positioning;

import org.awb.env.networkModel.NetworkModel;
import de.enflexit.geography.coordinates.AbstractCoordinate;

/**
 * The Class GraphNodePosition represents the general class for graph node positions if no 
 * geographical coordinate is used in the graph of the current {@link NetworkModel}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class GraphNodePosition extends AbstractCoordinate {

	private static final long serialVersionUID = -234248032288643611L;
	
    public double x;
    public double y;

    /**
	 * Instantiates a new graph node position.
	 */
	public GraphNodePosition() { }
	
	/**
	 * Instantiates a new graph node position.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public GraphNodePosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
    
    /* (non-Javadoc)
     * @see java.awt.geom.Point2D#getX()
     */
	@Override
    public double getX() {
        return x;
    }
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#getY()
	 */
	@Override
    public double getY() {
        return y;
    }
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#setLocation(double, double)
	 */
	@Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
	
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null) return false;
		if (! (compObj instanceof GraphNodePosition)) return false;
		return super.equals(compObj);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractGeoCoordinate#toString()
	 */
	@Override
	public String toString() {
		return "Pos: x=" + this.getX() + ", y=" + this.getY();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractCoordinate#serialize()
	 */
	@Override
	public String serialize() {
		return this.getX() + ":" + this.getY();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractCoordinate#deserialize(java.lang.String)
	 */
	@Override
	public void deserialize(String coordinateString) throws NullPointerException, CoordinateParseException {
		
		if (coordinateString==null || coordinateString.isEmpty()==true) throw new NullPointerException("No string was specified to deserialize a coordinate");
			
		String[] coords = coordinateString.split(":");
		if (coords.length == 2) {
			this.x = java.lang.Double.parseDouble(coords[0]);
			this.y = java.lang.Double.parseDouble(coords[1]);
			return;
		}
		// --- Nothing parsed - throw an error -- 
		throw new CoordinateParseException("The specified coordinate '" + coordinateString + "' is not of type " + this.getClass().getSimpleName());
	}
	
}
