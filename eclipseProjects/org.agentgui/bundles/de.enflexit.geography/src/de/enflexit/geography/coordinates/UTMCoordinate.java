package de.enflexit.geography.coordinates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

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
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String longZoneDisplay = String.valueOf(this.lngZone);
		if (longZoneDisplay.length()==1) {
			longZoneDisplay = "0" + longZoneDisplay;
		}
		return "UTM: " + longZoneDisplay + this.latZone + " " + this.easting + " " + this.northing;
	}


	
}
