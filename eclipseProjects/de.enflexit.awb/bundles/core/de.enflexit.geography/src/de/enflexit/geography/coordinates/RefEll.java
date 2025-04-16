package de.enflexit.geography.coordinates;

/**
 * The Class RefEll.
 */
public class RefEll {
	
	private double maj;
	private double min;
	private double ecc;
	  
	/**
	 * Instantiates a new GeoUtils.
	 *
	 * @param maj the maj
	 * @param min the min
	 */
	public RefEll(double maj, double min) {
		this.maj = maj;
		this.min = min;
		this.ecc = ((maj * maj) - (min * min)) / (maj * maj);
	}
	
	/**
	 * Gets the maj.
	 * @return the maj
	 */
	public double getMaj() {
		return maj;
	}
	/**
	 * Gets the min.
	 * @return the min
	 */
	public double getMin() {
		return min;
	}
	/**
	 * Gets the ecc.
	 * @return the ecc
	 */
	public double getEcc() {
		return ecc;
	}
}
