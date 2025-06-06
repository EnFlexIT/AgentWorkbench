package de.enflexit.geography.coordinates;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.coords.UTMCoord;

/**
 * The Class WGS84LatLngCoordinate describes a coordinate in World Geodetic System 1984 (WGS 84) 
 * format  that is a decimal latitude longitude coordinate. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WGS84LatLngCoordinate", propOrder = {
    "latitude",
    "longitude"
})
public class WGS84LatLngCoordinate extends AbstractGeoCoordinate {
	
	private static final long serialVersionUID = -4543228960997341395L;
	
	public static final String POS_PREFIX = "WGS84";
	
	@XmlElement(name = "Latitude")
	private double latitude;
	@XmlElement(name = "Longitude")
	private double longitude;
	
	
	/**
	 * Instantiates a new WGS84 latitude longitude coordinate.
	 */
	public WGS84LatLngCoordinate() { }
	
	/**
	 * Instantiates a new WGS84 latitude longitude coordinate.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	public WGS84LatLngCoordinate(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.validate(this.latitude, this.longitude);
	}
	
	/**
	 * Gets the latitude.
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * Sets the latitude.
	 * @param latitude the new latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longitude.
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * Sets the longitude.
	 * @param longitude the new longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	// ----------------------------------------------------
	// --- Methods from abstract class Point2D -- Start --- 
	// ----------------------------------------------------
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D.Double#getX()
	 */
	@Override
	public double getX() {
		return this.getLatitude();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#getY()
	 */
	@Override
	public double getY() {
		return this.getLongitude();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#setLocation(double, double)
	 */
	@Override
	public void setLocation(double x, double y) {
		this.setLatitude(x);
		this.setLongitude(y);
	}
	// ----------------------------------------------------
	// --- Methods from abstract class Point2D -- End ----- 
	// ----------------------------------------------------
	
	
	
	/**
	 * Validates the current WGS84 coordinate.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	private void validate(double latitude, double longitude) {
		if (latitude < -90.0 || latitude > 90.0 || longitude < -180.0 || longitude >= 180.0) {
			throw new IllegalArgumentException("Legal ranges: latitude [-90,90], longitude [-180,180).");
		}
	}
	
	
	/**
	 * Gets the distance in km.
	 *
	 * @param pointNo2 the point no 2
	 * @return the distance in km 
	 */
	public double getDistanceInKm(WGS84LatLngCoordinate pointNo2) {

	  double er = 6366.707;

	  double latFrom = GeoUtils.deg2rad(this.getLatitude());
	  double latTo   = GeoUtils.deg2rad(pointNo2.getLatitude());
	  double lngFrom = GeoUtils.deg2rad(this.getLongitude());
	  double lngTo   = GeoUtils.deg2rad(pointNo2.getLongitude());

	  double x1 = er * Math.cos(lngFrom) * Math.sin(latFrom);
	  double y1 = er * Math.sin(lngFrom) * Math.sin(latFrom);
	  double z1 = er * Math.cos(latFrom);

	  double x2 = er * Math.cos(lngTo) * Math.sin(latTo);
	  double y2 = er * Math.sin(lngTo) * Math.sin(latTo);
	  double z2 = er * Math.cos(latTo);

	  double d = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)+(z1-z2)*(z1-z2));
	  return d;

	}
	
	/**
	 * Convert a latitude and longitude into an OSGB grid reference.
	 * @return the OSGB grid reference
	 */
	public OSGBCoordinate getOSGBCoordinate() {
		
		RefEll airy1830 = new RefEll(6377563.396, 6356256.909);
		double OSGB_F0  = 0.9996012717;
		double N0       = -100000.0;
		double E0       = 400000.0;
		double phi0     = GeoUtils.deg2rad(49.0);
		double lambda0  = GeoUtils.deg2rad(-2.0);
		double a        = airy1830.getMaj();
		double b        = airy1830.getMin();
		double eSquared = airy1830.getEcc();
		double phi 		= GeoUtils.deg2rad(this.latitude);
		double lambda 	= GeoUtils.deg2rad(this.longitude);
		double E = 0.0;
		double N = 0.0;
		
		double n = (a - b) / (a + b);
		double v = a * OSGB_F0 * Math.pow(1.0 - eSquared * GeoUtils.sinSquared(phi), -0.5);
		double rho = a * OSGB_F0 * (1.0 - eSquared) * Math.pow(1.0 - eSquared * GeoUtils.sinSquared(phi), -1.5);
		double etaSquared = (v / rho) - 1.0;
		
		double M = (b * OSGB_F0) 
				* (((1 + n + ((5.0 / 4.0) * n * n) + ((5.0 / 4.0) * n * n * n))
		        * (phi - phi0))
		        - (((3 * n) + (3 * n * n) + ((21.0 / 8.0) * n * n * n))
		          * Math.sin(phi - phi0)
		          * Math.cos(phi + phi0))
		        + ((((15.0 / 8.0) * n * n) + ((15.0 / 8.0) * n * n * n))
		          * Math.sin(2.0 * (phi - phi0))
		          * Math.cos(2.0 * (phi + phi0)))
		        - (((35.0 / 24.0) * n * n * n)
		          * Math.sin(3.0 * (phi - phi0))
		          * Math.cos(3.0 * (phi + phi0))));
	  
		double I = M + N0;
		double II = (v / 2.0) * Math.sin(phi) * Math.cos(phi);
		double III = (v / 24.0)
				  * Math.sin(phi)
				  * Math.pow(Math.cos(phi), 3.0)
				  * (5.0 - GeoUtils.tanSquared(phi) + (9.0 * etaSquared));
		double IIIA = (v / 720.0)
				  * Math.sin(phi)
				  * Math.pow(Math.cos(phi), 5.0)
				  * (61.0 - (58.0 * GeoUtils.tanSquared(phi)) + Math.pow(Math.tan(phi), 4.0));

		double IV = v * Math.cos(phi);
		double V = (v / 6.0) * Math.pow(Math.cos(phi), 3.0) * ((v / rho) - GeoUtils.tanSquared(phi));
		double VI = (v / 120.0)  
				* Math.pow(Math.cos(phi), 5.0)
				* (5.0
				- (18.0 * GeoUtils.tanSquared(phi))
				+ (Math.pow(Math.tan(phi), 4.0))
				+ (14 * etaSquared)
				- (58 * GeoUtils.tanSquared(phi) * etaSquared));

		N = I
	      + (II * Math.pow(lambda - lambda0, 2.0))
	      + (III * Math.pow(lambda - lambda0, 4.0))
	      + (IIIA * Math.pow(lambda - lambda0, 6.0));
	  
		E = E0
	      + (IV * (lambda - lambda0))
	      + (V * Math.pow(lambda - lambda0, 3.0))
	      + (VI * Math.pow(lambda - lambda0, 5.0));

	  return new OSGBCoordinate(E, N);
	}
	
	
	/**
	 * Return the UMT coordinate for the current WGS84 coordinate.
	 * @return the UMT coordinate
	 */
	public UTMCoordinate getUTMCoordinate() {
		
		UTMCoordinate utm = null;
		
		boolean useNasaLib = false;
		if (useNasaLib==true) {
			// --- Usage of the NASA library ------------------------
			UTMCoord utmCoord = UTMCoord.fromLatLon(Angle.fromDegrees(this.getLatitude()), Angle.fromDegrees(this.getLongitude()));
			// --- Conversion to own UTM coordinate ----------------- 
			String latZone = new CoordinateConversion().getUTMLatitudeZone(this.getLatitude());
			utm = new UTMCoordinate(utmCoord.getZone(), latZone, utmCoord.getEasting(), utmCoord.getNorthing());
		} else {
			// --- Usage of the old style conversion ---------------- 
			utm = new CoordinateConversion().latLon2UTM(this);
		}
		return utm;
	}
	
	/**
	 * Returns the UTM coordinate for the current WGS84 coordinate.
	 *
	 * @param targetLongitudeZone the target longitude zone
	 * @param targetLatitudeZone the target latitude zone
	 * @return the UTM coordinate
	 */
	public UTMCoordinate getUTMCoordinate(Integer targetLongitudeZone, String targetLatitudeZone) {
		UTMCoordinate utm = this.getUTMCoordinate();
		utm.transformZone(targetLongitudeZone);
		return utm;
	}

	
	/**
	 * Convert WGS 84 to OSGB 36.
	 */
	public void convertWGS84ToOSGB36() {
		
		RefEll wgs84 = new RefEll(6378137.000, 6356752.3141);
		double a = wgs84.getMaj();
		@SuppressWarnings("unused")
		double b = wgs84.getMin();
		double eSquared = wgs84.getEcc();
		double phi = GeoUtils.deg2rad(this.getLatitude());
		double lambda = GeoUtils.deg2rad(this.getLongitude());
		double v = a / (Math.sqrt(1 - eSquared * GeoUtils.sinSquared(phi)));
		double H = 0; // height
		double x = (v + H) * Math.cos(phi) * Math.cos(lambda);
		double y = (v + H) * Math.cos(phi) * Math.sin(lambda);
		double z = ((1 - eSquared) * v + H) * Math.sin(phi);

		double tx = -446.448;
		double ty = 124.157;
		double tz = -542.060;
		double s = 0.0000204894;
		double rx = GeoUtils.deg2rad(-0.00004172222);
		double ry = GeoUtils.deg2rad(-0.00006861111);
		double rz = GeoUtils.deg2rad(-0.00023391666);

		double xB = tx + (x * (1 + s)) + (-rx * y) + (ry * z);
		double yB = ty + (rz * x) + (y * (1 + s)) + (-rx * z);
		double zB = tz + (-ry * x) + (rx * y) + (z * (1 + s));

		RefEll airy1830 = new RefEll(6377563.396, 6356256.909);
		a = airy1830.getMaj();
		b = airy1830.getMin();
		eSquared = airy1830.getEcc();

		double lambdaB = GeoUtils.rad2deg(Math.atan(yB / xB));
		double p = Math.sqrt((xB * xB) + (yB * yB));
		double phiN = Math.atan(zB / (p * (1 - eSquared)));
		for (int i = 1; i < 10; i++) {
			v = a / (Math.sqrt(1 - eSquared * GeoUtils.sinSquared(phiN)));
			double phiN1 = Math.atan((zB + (eSquared * v * Math.sin(phiN))) / p);
			phiN = phiN1;
		}

		double phiB = GeoUtils.rad2deg(phiN);

		this.latitude = phiB;
		this.longitude = lambdaB;
	}
	
	/**
	 * Convert OSGB 36 to WGS 84.
	 */
	public void convertOSGB36ToWGS84() {
		
		RefEll airy1830 = new RefEll(6377563.396, 6356256.909);
		double a = airy1830.getMaj();
		@SuppressWarnings("unused")
		double b = airy1830.getMin();
		double eSquared = airy1830.getEcc();
		double phi = GeoUtils.deg2rad(this.getLatitude());
		double lambda = GeoUtils.deg2rad(this.getLongitude());
		double v = a / (Math.sqrt(1 - eSquared * GeoUtils.sinSquared(phi)));
		double H = 0; // height
		double x = (v + H) * Math.cos(phi) * Math.cos(lambda);
		double y = (v + H) * Math.cos(phi) * Math.sin(lambda);
		double z = ((1 - eSquared) * v + H) * Math.sin(phi);

		double tx = 446.448;
		double ty = -124.157;
		double tz = 542.060;
		double s = -0.0000204894;
		double rx = GeoUtils.deg2rad(0.00004172222);
		double ry = GeoUtils.deg2rad(0.00006861111);
		double rz = GeoUtils.deg2rad(0.00023391666);

		double xB = tx + (x * (1 + s)) + (-rx * y) + (ry * z);
		double yB = ty + (rz * x) + (y * (1 + s)) + (-rx * z);
		double zB = tz + (-ry * x) + (rx * y) + (z * (1 + s));

		RefEll wgs84 = new RefEll(6378137.000, 6356752.3141);
		a = wgs84.getMaj();
		b = wgs84.getMin();
		eSquared = wgs84.getEcc();

		double lambdaB = GeoUtils.rad2deg(Math.atan(yB / xB));
		double p = Math.sqrt((xB * xB) + (yB * yB));
		double phiN = Math.atan(zB / (p * (1 - eSquared)));
		for (int i = 1; i < 10; i++) {
			v = a / (Math.sqrt(1 - eSquared * GeoUtils.sinSquared(phiN)));
			double phiN1 = Math.atan((zB + (eSquared * v * Math.sin(phiN))) / p);
			phiN = phiN1;
		}

		double phiB = GeoUtils.rad2deg(phiN);

		this.latitude = phiB;
		this.longitude = lambdaB;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		if (compObj==null) return false;
		if (! (compObj instanceof WGS84LatLngCoordinate)) return false;
		return super.equals(compObj);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractGeoCoordinate#toString()
	 */
	public String toString() {
	  return POS_PREFIX + ": " + this.latitude + ", " + this.longitude;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractCoordinate#serialize()
	 */
	@Override
	public String serialize() {
		return POS_PREFIX + ":" + this.getLatitude() + ":" + this.getLongitude();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractCoordinate#deserialize(java.lang.String)
	 */
	@Override
	public void deserialize(String coordinateString) throws NullPointerException, CoordinateParseException {

		if (coordinateString==null || coordinateString.isEmpty()==true) throw new NullPointerException("No string was specified to deserialize a coordinate");
		
		String[] coords = coordinateString.split(":");
		if (coords.length == 3 && coords[0].equals(POS_PREFIX)==true) {
			this.setLatitude(java.lang.Double.parseDouble(coords[1]));
			this.setLongitude(java.lang.Double.parseDouble(coords[2]));
			return;
		}
		// --- Nothing parsed - throw an error -- 
		throw new CoordinateParseException("The specified coordinate '" + coordinateString + "' is not of type " + this.getClass().getSimpleName());
	}
	
}
