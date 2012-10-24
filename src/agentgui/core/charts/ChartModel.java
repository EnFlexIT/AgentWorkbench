package agentgui.core.charts;

import org.jfree.data.general.Series;

import agentgui.ontology.DataSeries;

public interface ChartModel {
	/**
	 * Adds or updates a value to/in a data series.
	 * @param seriesIndex The index of the series that should be changed
	 * @param key The key for the value pair to be added/updated
	 * @param value The new value for the value pair
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public void addOrUpdateValuePair(int seriesIndex, Number key, Number value) throws NoSuchSeriesException;
	public void addSeries(DataSeries series);
	/**
	 * Update the time stamp in all series that contain it
	 * @param oldKey The old time stamp
	 * @param newKey The new time stamp
	 */
	public void updateKey(Number oldKey, Number newKey);
	/**
	 * Removes the value pair with the given key from the series with the given index.
	 * @param seriesIndex The series index
	 * @param key The key
	 * @throws NoSuchSeriesException Thrown if there is no series with the specified index
	 */
	public void removeValuePair(int seriesIndex, Number key) throws NoSuchSeriesException;
	/**
	 * Removes the data series with the given index from the chart model
	 * @param seriesIndex The series index
	 */
	public void removeSeries(int seriesIndex);
	
	public abstract Series getSeries(int seriesIndex);
	
}
