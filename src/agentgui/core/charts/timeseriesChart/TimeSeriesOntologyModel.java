package agentgui.core.charts.timeseriesChart;

import jade.util.leap.Iterator;
import jade.util.leap.List;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.ChartSettingsGeneral;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.Simple_Long;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesAdditionalSettings;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesValuePair;

public class TimeSeriesOntologyModel {
	private TimeSeriesChart timeSeriesChart;
	
	public TimeSeriesOntologyModel(TimeSeriesChart timeSeriesChart){
		this.timeSeriesChart = timeSeriesChart;
	}

	/**
	 * Gets the complete ontology representation (data and visualization settings) for this chart.
	 * @return the timeSeriesChart
	 */
	public TimeSeriesChart getTimeSeriesChart() {
		return timeSeriesChart;
	}

	/**
	 * @param timeSeriesChart The timeSeriesChart to set
	 */
	public void setTimeSeriesChart(TimeSeriesChart timeSeriesChart) {
		this.timeSeriesChart = timeSeriesChart;
	}
	
	public ChartSettingsGeneral getGeneralSettings(){
		return timeSeriesChart.getVisualizationSettings();
	}
	
	public TimeSeriesAdditionalSettings getAdditionalSettings(){
		return timeSeriesChart.getTimeSeriesAdditionalSettings();
	}
	
	public void addSeries(TimeSeries series){
		timeSeriesChart.getTimeSeriesChartData().add(series);
	}
	
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			timeSeriesChart.getTimeSeriesChartData().remove(seriesIndex);
			timeSeriesChart.getVisualizationSettings().getYAxisColors().remove(seriesIndex);
			timeSeriesChart.getVisualizationSettings().getYAxisLineWidth().remove(seriesIndex);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public void removeValuePair(int seriesIndex, long timestamp) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			TimeSeries series = (TimeSeries) getChartData().get(seriesIndex);
			int index = getIndexByTimestamp(series, timestamp);
			series.getTimeSeriesValuePairs().remove(index);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public void addOrUpdateValuePair(int seriesIndex, TimeSeriesValuePair valuePair) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			TimeSeries series = (TimeSeries) timeSeriesChart.getTimeSeriesChartData().get(seriesIndex);
			int valueIndex = getIndexByTimestamp(series, valuePair.getTimestamp().getLongValue());
			if(valueIndex >= 0){
				TimeSeriesValuePair valuePairToChange = (TimeSeriesValuePair) series.getTimeSeriesValuePairs().get(valueIndex);
				valuePairToChange.setValue(valuePair.getValue());
			}else{
				series.getTimeSeriesValuePairs().add(valuePair);
			}
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public void addOrUpdateValuePair(int seriesIndex, long timeStamp, float value) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			TimeSeries series = (TimeSeries) timeSeriesChart.getTimeSeriesChartData().get(seriesIndex);
			int valueIndex = getIndexByTimestamp(series, timeStamp);
			if(valueIndex >= 0){
				TimeSeriesValuePair valuePairToChange = (TimeSeriesValuePair) series.getTimeSeriesValuePairs().get(valueIndex);
				Simple_Float newValue = new Simple_Float();
				newValue.setFloatValue(value);
				valuePairToChange.setValue(newValue);
			}else{
				TimeSeriesValuePair newValuePair = new TimeSeriesValuePair();
				Simple_Long newTimeStamp = new Simple_Long();
				newTimeStamp.setLongValue(timeStamp);
				Simple_Float newValue = new Simple_Float();
				newValue.setFloatValue(value);
				newValuePair.setTimestamp(newTimeStamp);
				newValuePair.setValue(newValue);
				series.getTimeSeriesValuePairs().add(newValuePair);
			}
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public int getSeriesCount(){
		return timeSeriesChart.getTimeSeriesChartData().size();
	}
	
	/**
	 * Update the time stamp in all series that contain it
	 * @param oldTimeStamp The old time stamp
	 * @param newTimeStamp The new time stamp
	 */
	public void updateTimeStamp(long oldTimeStamp, long newTimeStamp){
		
		// Iterate over all series
		Iterator allSeries = timeSeriesChart.getAllTimeSeriesChartData();
		while(allSeries.hasNext()){
			
			TimeSeries series = (TimeSeries) allSeries.next();
			
			// Iterate over all value pairs
			Iterator valuePairs = series.getAllTimeSeriesValuePairs();
			while(valuePairs.hasNext()){
				
				TimeSeriesValuePair valuePair = (TimeSeriesValuePair) valuePairs.next();
				
				// If the value pair's time stamp matches oldTimeStamp, update it 
				if(valuePair.getTimestamp().getLongValue() == oldTimeStamp){
					valuePair.getTimestamp().setLongValue(newTimeStamp);
				}
			}
		}
	}
	
	private int getIndexByTimestamp(TimeSeries series, long timeStamp){
		for(int i=0; i<series.getTimeSeriesValuePairs().size(); i++){
			TimeSeriesValuePair valuePair = (TimeSeriesValuePair) series.getTimeSeriesValuePairs().get(i);
			if(valuePair.getTimestamp().getLongValue() == timeStamp){
				return i;
			}
		}
		return -1;
	}
	
	public List getChartData(){
		return timeSeriesChart.getTimeSeriesChartData();
	}
	
	public TimeSeries getSeries(int seriesIndex) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			TimeSeries series = (TimeSeries) timeSeriesChart.getTimeSeriesChartData().get(seriesIndex);
			return series;
		}else{
			throw new NoSuchSeriesException();
		}
	}
}
