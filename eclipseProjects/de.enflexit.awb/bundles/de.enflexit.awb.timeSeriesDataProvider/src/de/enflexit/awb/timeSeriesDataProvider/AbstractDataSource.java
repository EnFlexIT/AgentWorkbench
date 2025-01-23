package de.enflexit.awb.timeSeriesDataProvider;

import java.util.List;

/**
 * Abstract superclass for data sources.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractDataSource {
	
	/**
	 * Gets a value for the specified timestamp from the specified series. May be null if either there
	 * is no series with that name, or the series cannot provide a value for the specified time.
	 * data source is not able to provide a value for the specified time.
	 * @param seriesName the series name
	 * @param timestamp the timestamp
	 * @return the value
	 */
	public abstract Double getValue(String seriesName, long timestamp);
	
	/**
	 * Gets all values from the specified data series.
	 * @param seriesName the series name
	 * @return the data series values
	 */
	public abstract List<Double> getDataSeriesValues(String seriesName);
}
