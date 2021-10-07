package de.enflexit.geography.coordinates;

/**
 * The Class OSGBCoordinate represents a coordinate in ordnance survey Great Britain.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OSGBCoordinate extends AbstractGeoCoordinate {

	private static final long serialVersionUID = 2649016678473697000L;
	
	public static final String POS_PREFIX = "OSGB";
	
	private double easting;
	private double northing;
	
	/**
	 * Instantiates a new ordnance survey coordinate GB (default constructor).
	 */
	public OSGBCoordinate() { }
	/**
	 * Instantiates a new ordnance survey coordinate GB.
	 *
	 * @param easting the easting
	 * @param northing the northing
	 */
	public OSGBCoordinate(double easting, double northing) {
		this.easting  = easting;
		this.northing = northing;
	}
	/**
	 * Instantiates a new ordnance survey coordinate GB.
	 * @param sixFigureReference the six figure reference
	 */
	public OSGBCoordinate(String sixFigureReference) {
		this.setOSGBCoordinateFromSixFigureReference(sixFigureReference);
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
		return this.getNorthing();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#getY()
	 */
	@Override
	public double getY() {
		return this.getEasting();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#setLocation(double, double)
	 */
	@Override
	public void setLocation(double x, double y) {
		this.setNorthing(x);
		this.setEasting(y);
	}
	// ----------------------------------------------------
	// --- Methods from abstract class Point2D -- End ----- 
	// ----------------------------------------------------
	
	/**
	 * Return the WGS84LatLngCoordinate instance of this coordinate.
	 * @return the latitude longitude
	 */
	public WGS84LatLngCoordinate getWGS84LatLngCoordinate() {

		RefEll airy1830 = new RefEll(6377563.396, 6356256.909);
		double OSGB_F0 = 0.9996012717;
		double N0 = -100000.0;
		double E0 = 400000.0;
		double phi0 = GeoUtils.deg2rad(49.0);
		double lambda0 = GeoUtils.deg2rad(-2.0);
		double a = airy1830.getMaj();
		double b = airy1830.getMin();
		double eSquared = airy1830.getEcc();
		double phi = 0.0;
		double lambda = 0.0;
		double E = this.easting;
		double N = this.northing;
		double n = (a - b) / (a + b);
		double M = 0.0;
		double phiPrime = ((N - N0) / (a * OSGB_F0)) + phi0;
		
		do {
			M = (b * OSGB_F0) * (((1 + n + ((5.0 / 4.0) * n * n) + ((5.0 / 4.0) * n * n * n)) * (phiPrime - phi0))
					- (((3 * n) + (3 * n * n) + ((21.0 / 8.0) * n * n * n)) * Math.sin(phiPrime - phi0) * Math.cos(phiPrime + phi0))
					+ ((((15.0 / 8.0) * n * n) + ((15.0 / 8.0) * n * n * n)) * Math.sin(2.0 * (phiPrime - phi0)) * Math.cos(2.0 * (phiPrime + phi0)))
					- (((35.0 / 24.0) * n * n * n) * Math.sin(3.0 * (phiPrime - phi0)) * Math.cos(3.0 * (phiPrime + phi0))));
			phiPrime += (N - N0 - M) / (a * OSGB_F0);
		} while ((N - N0 - M) >= 0.001);
		
		double v = a * OSGB_F0 * Math.pow(1.0 - eSquared * GeoUtils.sinSquared(phiPrime), -0.5);
		double rho = a * OSGB_F0 * (1.0 - eSquared) * Math.pow(1.0 - eSquared * GeoUtils.sinSquared(phiPrime), -1.5);
		double etaSquared = (v / rho) - 1.0;
		double VII = Math.tan(phiPrime) / (2 * rho * v);
		double VIII = (Math.tan(phiPrime) / (24.0 * rho * Math.pow(v, 3.0))) * (5.0 + (3.0 * GeoUtils.tanSquared(phiPrime)) + etaSquared - (9.0 * GeoUtils.tanSquared(phiPrime) * etaSquared));
		double IX = (Math.tan(phiPrime) / (720.0 * rho * Math.pow(v, 5.0))) * (61.0 + (90.0 * GeoUtils.tanSquared(phiPrime)) + (45.0 * GeoUtils.tanSquared(phiPrime) * GeoUtils.tanSquared(phiPrime)));
		double X = GeoUtils.sec(phiPrime) / v;
		double XI = (GeoUtils.sec(phiPrime) / (6.0 * v * v * v)) * ((v / rho) + (2 * GeoUtils.tanSquared(phiPrime)));
		double XII = (GeoUtils.sec(phiPrime) / (120.0 * Math.pow(v, 5.0))) * (5.0 + (28.0 * GeoUtils.tanSquared(phiPrime)) + (24.0 * GeoUtils.tanSquared(phiPrime) * GeoUtils.tanSquared(phiPrime)));
		double XIIA = (GeoUtils.sec(phiPrime) / (5040.0 * Math.pow(v, 7.0))) * (61.0 + (662.0 * GeoUtils.tanSquared(phiPrime)) + (1320.0 * GeoUtils.tanSquared(phiPrime) * GeoUtils.tanSquared(phiPrime)) + (720.0 * GeoUtils.tanSquared(phiPrime) * GeoUtils.tanSquared(phiPrime) * GeoUtils.tanSquared(phiPrime)));
		
		phi = phiPrime - (VII * Math.pow(E - E0, 2.0)) + (VIII * Math.pow(E - E0, 4.0)) - (IX * Math.pow(E - E0, 6.0));
		lambda = lambda0 + (X * (E - E0)) - (XI * Math.pow(E - E0, 3.0)) + (XII * Math.pow(E - E0, 5.0)) - (XIIA * Math.pow(E - E0, 7.0));

		return new WGS84LatLngCoordinate(GeoUtils.rad2deg(phi), GeoUtils.rad2deg(lambda));
	}
	
	/**
	 * Gets the six figure string.
	 * @return the six figure string
	 */
	public String getSixFigureString() {

		int hundredkmE = (int) Math.floor(this.easting / 100000);
		int hundredkmN = (int) Math.floor(this.northing / 100000);
		String firstLetter = "";
		if (hundredkmN < 5) {
			if (hundredkmE < 5) {
				firstLetter = "S";
			} else {
				firstLetter = "T";
			}
		} else if (hundredkmN < 10) {
			if (hundredkmE < 5) {
				firstLetter = "N";
			} else {
				firstLetter = "O";
			}
		} else {
			firstLetter = "H";
		}

		String secondLetter = "";
		int index = 65 + ((4 - (hundredkmN % 5)) * 5) + (hundredkmE % 5);
		//int ti = index;
		if (index >= 73) index++;
		secondLetter = GeoUtils.chr(index);

		Integer e = (int) Math.floor((this.easting - (100000 * hundredkmE)) / 100);
		Integer n = (int) Math.floor((this.northing - (100000 * hundredkmN)) / 100);
		String es = e.toString();
		if (e < 100) es = "0" + es;
		if (e < 10)  es = "0" + es;
		String ns = n.toString();
		if (n < 100) ns = "0" + ns;
		if (n < 10)  ns = "0" + ns;

		return firstLetter + secondLetter + es + ns;
	}
	
	/**
	 * Gets the OSGB coordinate from six figure reference.
	 *
	 * @param sixFigureReference the six figure reference
	 * @return the OSGB coordinate from six figure reference
	 */
	private void setOSGBCoordinateFromSixFigureReference(String sixFigureReference) {
		String char1 = sixFigureReference.substring(0, 1);
		String char2 = sixFigureReference.substring(1, 2);
		// Thanks to Nick Holloway for pointing out the radix bug here
		int east = Integer.parseInt(sixFigureReference.substring(2, 5), 10) * 100;
		int north = Integer.parseInt(sixFigureReference.substring(5, 8), 10) * 100;
		if (char1.equalsIgnoreCase("H")) {
			north += 1000000;
		} else if (char1.equalsIgnoreCase("N")) {
			north += 500000;
		} else if (char1.equalsIgnoreCase("O")) {
			north += 500000;
			east += 500000;
		} else if (char1.equalsIgnoreCase("T")) {
			east += 500000;
		}
		int char2ord = GeoUtils.ord(char2);
		if (char2ord > 73) {
			char2ord--; // Adjust for no I
		}
		double nx = ((char2ord - 65) % 5) * 100000;
		double ny = (4 - Math.floor((char2ord - 65) / 5)) * 100000;
		
		this.easting  = east + nx;
		this.northing =  north + ny;
	}

	
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		if (compObj==null) return false;
		if (! (compObj instanceof OSGBCoordinate)) return false;
		return super.equals(compObj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return  POS_PREFIX + ": " + this.easting + ", " + this.northing + "";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractCoordinate#serialize()
	 */
	@Override
	public String serialize() {
		
		String[] utmParts = new String[3]; 
		utmParts[0] = POS_PREFIX;
		utmParts[1] = "" + this.getEasting();
		utmParts[2] = "" + this.getNorthing();
		return String.join(":", utmParts);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.geography.coordinates.AbstractCoordinate#deserialize(java.lang.String)
	 */
	@Override
	public void deserialize(String coordinateString) throws NullPointerException, CoordinateParseException {

		if (coordinateString==null || coordinateString.isEmpty()==true) throw new NullPointerException("No string was specified to deserialize a coordinate");
		
		String[] utmParts = coordinateString.split(":");
		if (utmParts.length==3 && utmParts[0].equals(POS_PREFIX)==true) {
			this.setEasting(java.lang.Double.parseDouble(utmParts[1]));
			this.setNorthing(java.lang.Double.parseDouble(utmParts[2]));
			return;
		}
		// --- Nothing parsed - throw an error -- 
		throw new CoordinateParseException("The specified coordinate '" + coordinateString + "' is not of type " + this.getClass().getSimpleName());
	}

	
}
