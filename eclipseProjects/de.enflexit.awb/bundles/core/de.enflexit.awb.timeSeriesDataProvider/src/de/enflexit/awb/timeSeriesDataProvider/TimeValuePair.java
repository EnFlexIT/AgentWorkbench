package de.enflexit.awb.timeSeriesDataProvider;

/**
 * This class represents a value assigned to a point in time-
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeValuePair {
	
	private long timestamp;
	private double value;
	
	/**
	 * Instantiates a new, uninitialized time value pair.
	 */
	public TimeValuePair() {}
	
	/**
	 * Instantiates a new time value pair, initialized with the provided values-.
	 * @param timestamp the timestamp
	 * @param value the value
	 */
	public TimeValuePair(long timestamp, double value) {
		this.timestamp = timestamp;
		this.value = value;
	}
	
	/**
	 * Gets the timestamp.
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	/**
	 * Sets the timestamp.
	 * @param timestamp the new timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * Gets the value.
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	/**
	 * Sets the value.
	 * @param value the new value
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
}
