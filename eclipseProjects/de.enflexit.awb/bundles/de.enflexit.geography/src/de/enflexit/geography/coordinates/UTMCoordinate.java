package de.enflexit.geography.coordinates;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.coords.UTMCoord;

/**
 * The Class UTMCoordinate.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UTMCoordinate", propOrder = {
    "lngZone",
    "latZone",
    "easting",
    "northing"
})
public class UTMCoordinate extends AbstractGeoCoordinate {

	private static final long serialVersionUID = -938213615995403657L;

	public static final String POS_PREFIX = "UTM";
	
	private int lngZone;
	private String latZone;
	private double easting;
	private double northing;
	
	
	/**
	 * Instantiates a new UTM reference (default constructor).
	 */
	public UTMCoordinate() { }
	
	/**
	 * Instantiates a new UTM reference.
	 *
	 * @param lngZone the longitude zone
	 * @param latZone the latitude zone
	 * @param easting the easting
	 * @param northing the northing
	 */
	public UTMCoordinate(int lngZone, String latZone, double easting, double northing) {
		this.lngZone  = lngZone;
		this.latZone  = latZone;
		this.easting  = easting;
		this.northing = northing;
	}
	
	
	public int getLongitudeZone() {
		return lngZone;
	}
	public void setLongitudeZone(int lngZone) {
		this.lngZone = lngZone;
	}

	public String getLatitudeZone() {
		return latZone;
	}
	public void setLatitudeZone(String latZone) {
		this.latZone = latZone;
	}

	public double getEasting() {
		return easting;
	}
	public void setEasting(double easting) {
		this.easting = easting;
	}

	public double getNorthing() {
		return northing;
	}
	public void setNorthing(double northing) {
		this.northing = northing;
	}

	
	// ----------------------------------------------------
	// --- Methods from abstract class Point2D -- Start --- 
	// ----------------------------------------------------
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D.Double#getX()
	 */
	@Override
	public double getX() {
		return this.getEasting();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#getY()
	 */
	@Override
	public double getY() {
		return this.getNorthing();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#setLocation(double, double)
	 */
	@Override
	public void setLocation(double x, double y) {
		this.setEasting(x);
		this.setNorthing(y);
	}
	// ----------------------------------------------------
	// --- Methods from abstract class Point2D -- End ----- 
	// ----------------------------------------------------
	
	
	/**
	 * Transforms the current UTM longitude zone to the specified longitude zone.
	 * @param targetLongitudeZone the target longitude zone
	 */
	public void transformZone(int targetLongitudeZone) {
		new CoordinateConversion().utmTransformEastingByLongitudeZone(this, targetLongitudeZone);
	}
	
	/**
	 * Return the WGS84LatLngCoordinatefor the current UTMCoordinate.
	 * @return the corresponding WGS84LatLngCoordinatefor
	 */
	public WGS84LatLngCoordinate getWGS84LatLngCoordinate() {
		
		WGS84LatLngCoordinate wgs84 = null;
		
		boolean useNasaLib = false;
		if (useNasaLib==true) {
			// --- Usage of the NASA library ------------------------
			String hemisphere = new CoordinateConversion().getUTMHemisphere(this.getLatitudeZone());
			hemisphere = hemisphere.equals("N") ? AVKey.NORTH : AVKey.SOUTH;
			LatLon latLon = UTMCoord.locationFromUTMCoord(this.getLongitudeZone(), hemisphere, this.getEasting(), this.getNorthing());
			wgs84 = new WGS84LatLngCoordinate(latLon.getLatitude().getDegrees(), latLon.getLongitude().getDegrees());
		} else {
			// --- Usage of the old style conversion ---------------- 
			wgs84 = new CoordinateConversion().utm2LatLon(this);
		}
		return wgs84;
	}
	
	/**
	 * Returns the current UTM coordinate as {@link UTMCoord} as used by the NASA world wind.
	 * @return the UTMCoord from the UTMCoordinate
	 */
	protected UTMCoord getUTMCoord() {
		String hemisphere = new CoordinateConversion().getUTMHemisphere(this.getLatitudeZone());
		hemisphere = hemisphere.equals("N") ? AVKey.NORTH : AVKey.SOUTH;
		return UTMCoord.fromUTM(this.getLongitudeZone(), hemisphere, this.getEasting(), this.getNorthing());
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null) return false;
		if (! (compObj instanceof UTMCoordinate)) return false;

		UTMCoordinate utmComp = (UTMCoordinate) compObj;
		
		if (utmComp.getLongitudeZone()!=this.getLongitudeZone()) return false;
		if (utmComp.getLatitudeZone().equals(this.getLatitudeZone())==false) return false;
		return super.equals(compObj);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String longZoneDisplay = String.valueOf(this.lngZone);
		if (longZoneDisplay.length()==1) {
			longZoneDisplay = "0" + longZoneDisplay;
		}
		return POS_PREFIX + ": " + longZoneDisplay + this.latZone + " East: " + this.easting + ", North: " + this.northing;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractCoordinate#serialize()
	 */
	@Override
	public String serialize() {
		
		String[] utmParts = new String[5]; 
		utmParts[0] = POS_PREFIX;
		utmParts[1] = "" + this.getLongitudeZone();
		utmParts[2] = this.getLatitudeZone();
		utmParts[3] = "" + this.getEasting();
		utmParts[4] = "" + this.getNorthing();
		return String.join(":", utmParts);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractCoordinate#deserialize(java.lang.String)
	 */
	@Override
	public void deserialize(String coordinateString) throws NullPointerException, CoordinateParseException {

		if (coordinateString==null || coordinateString.isEmpty()==true) throw new NullPointerException("No string was specified to deserialize a coordinate");
		
		String[] utmParts = coordinateString.split(":");
		if (utmParts.length==5 && utmParts[0].equals(POS_PREFIX)==true) {
			this.setLongitudeZone(Integer.parseInt(utmParts[1]));
			this.setLatitudeZone(utmParts[2]);
			this.setEasting(java.lang.Double.parseDouble(utmParts[3]));
			this.setNorthing(java.lang.Double.parseDouble(utmParts[4]));
			return;
		}
		// --- Nothing parsed - throw an error -- 
		throw new CoordinateParseException("The specified coordinate '" + coordinateString + "' is not of type " + this.getClass().getSimpleName());
	}

}
