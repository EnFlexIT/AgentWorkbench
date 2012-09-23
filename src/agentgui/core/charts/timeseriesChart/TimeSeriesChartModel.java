package agentgui.core.charts.timeseriesChart;

import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.TimeSeriesValuePair;

/**
 * Data model for the JFreeChart representation of a time series
 * @author Nils
 *
 */
public class TimeSeriesChartModel extends TimeSeriesCollection {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 2315036788757231999L;
	
	/**
	 * Adds or updates a value pair to/in a time series.
	 * If there is no entry for the valuePairs time stamp in the series, a new entry will be added.
	 * If there is one, the value for this time stamp will be updated
	 * @param seriesIndex The index of the that should be changed 
	 * @param valuePair The valuePair to be added/updated
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public void addOrUpdateValuePair(int seriesIndex, TimeSeriesValuePair valuePair) throws NoSuchSeriesException{
		if(seriesIndex < this.getSeriesCount()){
			org.jfree.data.time.TimeSeries series = this.getSeries(seriesIndex);
			series.addOrUpdate(new FixedMillisecond(valuePair.getTimestamp().getLongValue()), valuePair.getValue().getFloatValue());
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Adds or updates a value to/in a time series.
	 * @param seriesIndex The index of the that should be changed
	 * @param timeStamp The time stamp for the value to be added/updated
	 * @param value The new value for the time stamp
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public void addOrUpdateValuePair(int seriesIndex, long timeStamp, float value) throws NoSuchSeriesException{
		if(seriesIndex < this.getSeriesCount()){
			org.jfree.data.time.TimeSeries series = this.getSeries(seriesIndex);
			series.addOrUpdate(new FixedMillisecond(timeStamp), value);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	
	public void addSeries(agentgui.ontology.TimeSeries series){
		org.jfree.data.time.TimeSeries newSeries = new org.jfree.data.time.TimeSeries(series.getLabel());
		
		jade.util.leap.Iterator valuePairs = series.getAllTimeSeriesValuePairs();
		while(valuePairs.hasNext()){
			TimeSeriesValuePair valuePair = (TimeSeriesValuePair) valuePairs.next();
			TimeSeriesDataItem newItem = new TimeSeriesDataItem(new FixedMillisecond(valuePair.getTimestamp().getLongValue()), valuePair.getValue().getFloatValue());
			newSeries.add(newItem);
		}
		
		this.addSeries(newSeries);
	}
	
	/**
	 * Update the time stamp in all series that contain it
	 * @param oldTimeStamp The old time stamp
	 * @param newTimeStamp The new time stamp
	 */
	public void updateTimeStamp(long oldTimeStamp, long newTimeStamp){
		@SuppressWarnings("unchecked")
		java.util.Iterator<org.jfree.data.time.TimeSeries> allSeries = getSeries().iterator();
		
		// Iterate over all series
		while(allSeries.hasNext()){
			org.jfree.data.time.TimeSeries series = allSeries.next();
			
			// Try to find a value pair with the old time stamp
			TimeSeriesDataItem oldValuePair = series.getDataItem(new FixedMillisecond(oldTimeStamp));
			
			// If found, remove it and add a new one with the new time stamp and the old value
			if(oldValuePair != null){
				
				series.delete(new FixedMillisecond(oldTimeStamp));
				series.addOrUpdate(new FixedMillisecond(newTimeStamp), oldValuePair.getValue());
				
			}
		}
	}
	
	public void removeValuePair(int seriesIndex, long timeStamp) throws NoSuchSeriesException{
		if(seriesIndex < this.getSeriesCount()){
			org.jfree.data.time.TimeSeries series = (TimeSeries) getSeries().get(seriesIndex);
			series.delete(new FixedMillisecond(timeStamp));
		}else{
			throw new NoSuchSeriesException();
		}
	}

}
