package de.enflexit.awb.timeSeriesDataProvider;

import java.util.List;

/**
 * Abstract super class for data series.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractDataSeries {
	
	/**
	 * Gets the name of this data series.
	 * @return the name
	 */
	public abstract String getName();
	
	/**
	 * Gets the first time stamp from the data series.
	 * @return the first time stamp
	 */
	public abstract long getFirstTimeStamp();
	
	/**
	 * Gets the last time stamp from the data series.
	 * @return the last time stamp
	 */
	public abstract long getLastTimeStamp();
	
	/**
	 * Gets the number of entries from the data series.
	 * @return the number of entries
	 */
	public abstract int getNumberOfEntries();
	
	/**
	 * Gets the value for the specified timestamp.
	 * @param timestamp the timestamp
	 * @return the value, may be null if no value exists for this timestamp
	 */
	public abstract TimeValuePair getValueForTime(long timestamp);
	
	/**
	 * Gets the values for the specified time range.
	 * @param timestampFrom the beginning of the time range
	 * @param timestampTo the end of the time range
	 * @return the values
	 */
	public abstract List<TimeValuePair> getValuesForTimeRange(long timestampFrom, long timestampTo);
}
