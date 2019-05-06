package de.enflexit.geography.coordinates;

/**
 * The Class UTMCoordinate.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class UTMCoordinate extends AbstractGeoCoordinate {

	private static final long serialVersionUID = -938213615995403657L;

	private double easting;
	private double northing;
	private String latZone;
	private int lngZone;
	
	
	/**
	 * Instantiates a new UTM reference.
	 *
	 * @param easting the easting
	 * @param northing the northing
	 * @param latZone the latitude zone
	 * @param lngZone the longitude zone
	 */
	public UTMCoordinate(double easting, double northing, String latZone, int lngZone) {
		this.easting  = easting;
		this.northing = northing;
		this.latZone  = latZone;
		this.lngZone  = lngZone;
	}
	
	/**
	 * Gets the WGS 84 lat lng coordinate.
	 * @return the WGS 84 lat lng coordinate
	 */
	public WGS84LatLngCoordinate getWGS84LatLngCoordinate() {
		
		RefEll wgs84 = new RefEll(6378137, 6356752.314);
		double UTM_F0 = 0.9996;
		double a = wgs84.getMaj();
		double eSquared = wgs84.getEcc();
		double ePrimeSquared = eSquared / (1.0 - eSquared);
		double e1 = (1 - Math.sqrt(1 - eSquared)) / (1 + Math.sqrt(1 - eSquared));
		double x = this.easting - 500000.0;
		double y = this.northing;
		double zoneNumber = this.lngZone;
		String zoneLetter = this.latZone;

		double longitudeOrigin = (zoneNumber - 1.0) * 6.0 - 180.0 + 3.0;

		// Correct y for southern hemisphere
		if ((GeoUtils.ord(zoneLetter) - GeoUtils.ord("N")) < 0) {
			y -= 10000000.0;
		}

		double m = y / UTM_F0;
		double mu = m / (a * (1.0 - eSquared / 4.0 - 3.0 * eSquared * eSquared / 64.0 - 5.0 * Math.pow(eSquared, 3.0) / 256.0));

		double phi1Rad = mu + (3.0 * e1 / 2.0 - 27.0 * Math.pow(e1, 3.0) / 32.0) * Math.sin(2.0 * mu)
				+ (21.0 * e1 * e1 / 16.0 - 55.0 * Math.pow(e1, 4.0) / 32.0) * Math.sin(4.0 * mu)
				+ (151.0 * Math.pow(e1, 3.0) / 96.0) * Math.sin(6.0 * mu);

		double n = a / Math.sqrt(1.0 - eSquared * Math.sin(phi1Rad) * Math.sin(phi1Rad));
		double t = Math.tan(phi1Rad) * Math.tan(phi1Rad);
		double c = ePrimeSquared * Math.cos(phi1Rad) * Math.cos(phi1Rad);
		double r = a * (1.0 - eSquared) / Math.pow(1.0 - eSquared * Math.sin(phi1Rad) * Math.sin(phi1Rad), 1.5);
		double d = x / (n * UTM_F0);

		double latitude = (phi1Rad - (n * Math.tan(phi1Rad) / r) * (d * d / 2.0
				- (5.0 + (3.0 * t) + (10.0 * c) - (4.0 * c * c) - (9.0 * ePrimeSquared)) * Math.pow(d, 4.0) / 24.0
				+ (61.0 + (90.0 * t) + (298.0 * c) + (45.0 * t * t) - (252.0 * ePrimeSquared) - (3.0 * c * c)) * Math.pow(d, 6.0) / 720.0))
				* (180.0 / Math.PI);

		double longitude = longitudeOrigin + ((d - (1.0 + 2.0 * t + c) * Math.pow(d, 3.0) / 6.0
				+ (5.0 - (2.0 * c) + (28.0 * t) - (3.0 * c * c) + (8.0 * ePrimeSquared) + (24.0 * t * t)) * Math.pow(d, 5.0) / 120.0)  / Math.cos(phi1Rad)) * (180.0 / Math.PI);

		return new WGS84LatLngCoordinate(latitude, longitude);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.lngZone + this.latZone + " " + this.easting + " " + this.northing;
	}
	
}
