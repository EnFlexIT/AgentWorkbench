/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.charts.timeseriesChart;

import jade.util.leap.List;
import agentgui.core.charts.OntologyModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.ChartSettingsGeneral;
import agentgui.ontology.DataSeries;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesChartSettings;
import agentgui.ontology.ValuePair;

/**
 * The Class TimeSeriesOntologyModel.
 */
public class TimeSeriesOntologyModel extends OntologyModel{
	
	private TimeSeriesDataModel dataModel;
	private TimeSeriesChart timeSeriesChart;
	
	/**
	 * Instantiates a new time series ontology model.
	 *
	 * @param timeSeriesChart the time series chart
	 * @param parent the parent
	 */
	public TimeSeriesOntologyModel(TimeSeriesChart timeSeriesChart, TimeSeriesDataModel parent){
		if(timeSeriesChart!=null){
			this.timeSeriesChart = timeSeriesChart;
		}else{
			this.timeSeriesChart = new TimeSeriesChart();
			this.timeSeriesChart.setTimeSeriesVisualisationSettings(new TimeSeriesChartSettings());
		}
		this.dataModel = parent;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getChartSettings()
	 */
	public ChartSettingsGeneral getChartSettings(){
		return this.timeSeriesChart.getTimeSeriesVisualisationSettings();
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
	
	/**
	 * Gets the index of the value pair with the given key of the series with the given index.
	 * @param seriesIndex The series index
	 * @param key The key
	 * @return The value pair's index
	 * @throws NoSuchSeriesException Thrown if there is no series with the specified index
	 */
	protected int getIndexByKey(int seriesIndex, Number key) throws NoSuchSeriesException{
		
		List seriesData = getSeriesData(seriesIndex);
		
		for(int i=0; i<seriesData.size(); i++){
			ValuePair valuePair = (ValuePair) seriesData.get(i);
			if(dataModel.getXValueFromPair(valuePair).doubleValue() == key.doubleValue()){
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * Removes a value pair.
	 *
	 * @param seriesIndex the series index
	 * @param key the key
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public void removeValuePair(int seriesIndex, Number key) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			List seriesData = getSeriesData(seriesIndex);
			int index = getIndexByKey(seriesIndex, key);
			if(index >= 0){
				seriesData.remove(index);
			}
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Adds or updates a value pair.
	 *
	 * @param seriesIndex the series index
	 * @param key the key
	 * @param value the value
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public void addOrUpdateValuePair(int seriesIndex, Number key, Number value) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			List seriesData = getSeriesData(seriesIndex);
			int valueIndex = getIndexByKey(seriesIndex, key);
			if(valueIndex >= 0){
				ValuePair valuePairToChange = (ValuePair) seriesData.get(valueIndex);
				dataModel.setValueForPair(value, valuePairToChange);
			}else{
				ValuePair newValuePair = dataModel.createNewValuePair(key, value);
				seriesData.add(newValuePair);
			}
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Exchanges one key in all series that contain it
	 * @param oldKey The old key
	 * @param newKey The new key
	 */
	public void updateKey(Number oldKey, Number newKey){
		
		for(int i=0; i<getSeriesCount(); i++){
			try {
				
				int vpIndex = getIndexByKey(i, oldKey); 
				if( vpIndex >= 0){
					// There is a pair with this key
					List seriesData = getSeriesData(i);
					ValuePair vp2update = (ValuePair) seriesData.get(vpIndex);
					dataModel.setKeyForPair(newKey, vp2update);
				}
				
				
			} catch (NoSuchSeriesException e) {
				// Should never happen, as i cannot be > number of series
				System.err.println("Error accessing series "+i);
				e.printStackTrace();
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#addSeries(agentgui.ontology.DataSeries)
	 */
	@Override
	public void addSeries(DataSeries series){
		timeSeriesChart.getTimeSeriesChartData().add(series);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#removeSeries(int)
	 */
	@Override
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			timeSeriesChart.getTimeSeriesChartData().remove(seriesIndex);
			timeSeriesChart.getTimeSeriesVisualisationSettings().getYAxisColors().remove(seriesIndex);
			timeSeriesChart.getTimeSeriesVisualisationSettings().getYAxisLineWidth().remove(seriesIndex);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getSeriesCount()
	 */
	@Override
	public int getSeriesCount(){
		return timeSeriesChart.getTimeSeriesChartData().size();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getChartData()
	 */
	@Override
	public List getChartData(){
		return timeSeriesChart.getTimeSeriesChartData();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getSeries(int)
	 */
	@Override
	public TimeSeries getSeries(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			TimeSeries series = (TimeSeries) timeSeriesChart.getTimeSeriesChartData().get(seriesIndex);
			return series;
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#exchangeSeries(int, agentgui.ontology.DataSeries)
	 */
	@Override
	public void exchangeSeries(int seriesIndex, DataSeries dataSeries) throws NoSuchSeriesException {
		if (seriesIndex<getSeriesCount()) {
			this.timeSeriesChart.getTimeSeriesChartData().remove(seriesIndex);
			this.timeSeriesChart.getTimeSeriesChartData().add(seriesIndex, dataSeries);
		} else {
			throw new NoSuchSeriesException();
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getSeriesData(int)
	 */
	@Override
	public List getSeriesData(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			TimeSeries series = (TimeSeries) timeSeriesChart.getTimeSeriesChartData().get(seriesIndex);
			return (List) series.getTimeSeriesValuePairs();
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Sets the time format in the settings object.
	 * @param timeFormat the new time format
	 */
	public void setTimeFormat(String timeFormat){
		this.timeSeriesChart.getTimeSeriesVisualisationSettings().setTimeFormat(timeFormat);
	}
	
}
