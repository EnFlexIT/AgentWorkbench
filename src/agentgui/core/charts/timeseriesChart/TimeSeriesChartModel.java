package agentgui.core.charts.timeseriesChart;

import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import agentgui.core.charts.ChartModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.TimeSeriesValuePair;

/**
 * Data model for the JFreeChart representation of a time series
 * @author Nils
 *
 */
public class TimeSeriesChartModel extends TimeSeriesCollection implements ChartModel{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 2315036788757231999L;
	
	public void addSeries(agentgui.ontology.DataSeries series){
		org.jfree.data.time.TimeSeries newSeries = new org.jfree.data.time.TimeSeries(series.getLabel());
		
		jade.util.leap.Iterator valuePairs = ((agentgui.ontology.TimeSeries)series).getAllTimeSeriesValuePairs();
		while(valuePairs.hasNext()){
			TimeSeriesValuePair valuePair = (TimeSeriesValuePair) valuePairs.next();
			TimeSeriesDataItem newItem = new TimeSeriesDataItem(new FixedMillisecond(valuePair.getTimestamp().getLongValue()), valuePair.getValue().getFloatValue());
			newSeries.add(newItem);
		}
		
		this.addSeries(newSeries);
	}
	
	@Override
	public void addOrUpdateValuePair(int seriesIndex, Number key, Number value)
			throws NoSuchSeriesException {
		if(seriesIndex < this.getSeriesCount()){
			org.jfree.data.time.TimeSeries series = this.getSeries(seriesIndex);
			series.addOrUpdate(new FixedMillisecond(key.longValue()), value.floatValue());
		}else{
			throw new NoSuchSeriesException();
		}
		
	}


	@Override
	public void updateKey(Number oldKey, Number newKey) {
		@SuppressWarnings("unchecked")
		java.util.Iterator<org.jfree.data.time.TimeSeries> allSeries = getSeries().iterator();
		
		// Iterate over all series
		while(allSeries.hasNext()){
			org.jfree.data.time.TimeSeries series = allSeries.next();
			
			// Try to find a value pair with the old time stamp
			TimeSeriesDataItem oldValuePair = series.getDataItem(new FixedMillisecond(oldKey.longValue()));
			
			// If found, remove it and add a new one with the new time stamp and the old value
			if(oldValuePair != null){
				
				series.delete(new FixedMillisecond(oldKey.longValue()));
				series.addOrUpdate(new FixedMillisecond(newKey.longValue()), oldValuePair.getValue());
				
			}
		}
		
	}


	@Override
	public void removeValuePair(int seriesIndex, Number key)
			throws NoSuchSeriesException {
		if(seriesIndex < this.getSeriesCount()){
			org.jfree.data.time.TimeSeries series = (TimeSeries) getSeries().get(seriesIndex);
			series.delete(new FixedMillisecond(key.longValue()));
		}else{
			throw new NoSuchSeriesException();
		}
		
	}

}
