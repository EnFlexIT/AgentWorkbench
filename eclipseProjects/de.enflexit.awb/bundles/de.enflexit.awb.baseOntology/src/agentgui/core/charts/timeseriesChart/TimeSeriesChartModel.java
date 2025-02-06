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

import java.util.TimeZone;

import org.jfree.data.general.SeriesException;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import agentgui.core.charts.ChartModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.DataSeries;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.Simple_Long;
import agentgui.ontology.TimeSeriesChartSettings;
import agentgui.ontology.TimeSeriesValuePair;

/**
 * Data model for the JFreeChart representation of a time series
 * 
 * @author Nils
 */
public class TimeSeriesChartModel extends ChartModel{
	
	/**
	 * The parent {@link TimeSeriesDataModel}
	 */
	private TimeSeriesDataModel parent;
	
	/**
	 * The JFreeChart data model for this chart
	 */
	private TimeSeriesCollection<String> timeSeriesCollection;

	
	/**
	 * Instantiates a new time series chart model.
	 * @param timeSeriesDataModel the current TimeSeriesDataModel
	 */
	public TimeSeriesChartModel(TimeSeriesDataModel timeSeriesDataModel){
		this.timeSeriesCollection = new TimeSeriesCollection<>();
		this.parent = timeSeriesDataModel;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#getChartSettings()
	 */
	@Override
	public TimeSeriesChartSettings getChartSettings() {
		return (TimeSeriesChartSettings)parent.getOntologyModel().getChartSettings();
	}
	
	
	/**
	 * Creates the time series.
	 *
	 * @param dataSeries the data series
	 * @param parentTimeSeriesDataModel the parent time series data model
	 * @return the org.jfree.data.time. time series
	 */
	private TimeSeries<String> createTimeSeries(DataSeries dataSeries, TimeSeriesDataModel parentTimeSeriesDataModel) {
		
		TimeSeries<String> newSeries = new TimeSeries<>(dataSeries.getLabel());
		
		// --- If the chart shows real time data, set length restrictions -----
		if (parentTimeSeriesDataModel.isRealTime()==true) {
			int maxStates = parentTimeSeriesDataModel.getLengthRestriction().getMaxNumberOfStates();
			if (maxStates>0) {
				newSeries.setMaximumItemCount(maxStates);
			}
			long maxAge = parentTimeSeriesDataModel.getLengthRestriction().getMaxDuration();
			if (maxAge>0) {
				newSeries.setMaximumItemAge(maxAge);
			}
			
		}
		
		// --- Add the value pairs --------------------------------------------
		List valuePairs = ((agentgui.ontology.TimeSeries)dataSeries).getTimeSeriesValuePairs();
		for (int i = 0; i < valuePairs.size(); i++) {
			
			TimeSeriesValuePair valuePair = (TimeSeriesValuePair) valuePairs.get(i);
			Simple_Long simpleLong = valuePair.getTimestamp();
			Simple_Float simpleFloat = valuePair.getValue();
			
			Long timeStampLong = simpleLong.getLongValue();
			Float floatValue = simpleFloat.getFloatValue();
			if (timeStampLong!=null) {
				TimeSeriesDataItem newItem = new TimeSeriesDataItem(new FixedMillisecond(timeStampLong), floatValue);
				newSeries.addOrUpdate(newItem);	
			}
		}
		return newSeries;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#addSeries(agentgui.ontology.DataSeries)
	 */
	@Override
	public void addSeries(DataSeries series){
		
		this.getTimeSeriesCollection().addSeries(this.createTimeSeries(series, this.parent));	
		this.setChanged();
		this.notifyObservers(ChartModel.EventType.SERIES_ADDED);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#exchangeSeries(int, agentgui.ontology.DataSeries)
	 */
	@Override
	public void exchangeSeries(int seriesIndex, DataSeries series) throws NoSuchSeriesException {
		
		if (seriesIndex < 0 ||  seriesIndex >= this.getSeriesCount()) {
			throw new NoSuchSeriesException();
		}
		// --- Create series that exchanges the specified series ----
		this.exchangeSeries(seriesIndex, this.createTimeSeries(series, parent), true);
	}
	/**
	 * Exchanges the specified TimeSeries in the local {@link #getTimeSeriesCollection()}.
	 *
	 * @param seriesIndex the series index to replace
	 * @param tsExchange the time series to exchange
	 * @param setDirtyAndNotify the set dirty and notify
	 * @throws NoSuchSeriesException the no such series exception
	 */
	private void exchangeSeries(int seriesIndex, TimeSeries<String> tsExchange, boolean setDirtyAndNotify) {
		
		// --- Create temporary TimeSeriesCollection ----------------
		TimeSeriesCollection<String> tscTmp = new TimeSeriesCollection<>();
		for (int i = 0; i < this.getTimeSeriesCollection().getSeriesCount(); i++) {
			org.jfree.data.time.TimeSeries<String> tsToAdd = this.getTimeSeriesCollection().getSeries(i);
			if (i==seriesIndex) {
				tsToAdd = tsExchange;
			}
			tscTmp.addSeries(tsToAdd);
		} 

		// --- Replace local TimeSeries by the new ordered one ------
		this.getTimeSeriesCollection().removeAllSeries();
		tscTmp.getSeries().forEach(ts -> this.getTimeSeriesCollection().addSeries(ts));
		
		if (setDirtyAndNotify==true) {
			this.setChanged();
			this.notifyObservers(ChartModel.EventType.SERIES_EXCHANGED);
		}
	}
	
	
	/**
	 * Adds or updates a value to/in a data series.
	 * @param seriesIndex The index of the series that should be changed
	 * @param key The key for the value pair to be added/updated
	 * @param value The new value for the value pair
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public void addOrUpdateValuePair(int seriesIndex, Number key, Number value) throws NoSuchSeriesException {
		if (seriesIndex < this.getSeriesCount()) {
			TimeSeries<String> series = this.getSeries(seriesIndex);
			series.addOrUpdate(new FixedMillisecond(key.longValue()), value.floatValue());
		} else {
			throw new NoSuchSeriesException();
		}
	}

	/**
	 * Update the time stamp in all series that contain it
	 * @param oldKey The old time stamp
	 * @param newKey The new time stamp
	 */
	public void updateKey(Number oldKey, Number newKey) {
		
		// --- Iterate over all series ------------------------------
		for (int i=0; i < this.getSeriesCount(); i++) {

			TimeSeries<String> series = this.getSeries(i);
			
			// Try to find a value pair with the old time stamp
			TimeSeriesDataItem oldValuePair = series.getDataItem(new FixedMillisecond(oldKey.longValue()));
			
			// If found, remove it and add a new one with the new time stamp and the old value
			if (oldValuePair!=null) {
				series.delete(new FixedMillisecond(oldKey.longValue()));
				series.addOrUpdate(new FixedMillisecond(newKey.longValue()), oldValuePair.getValue());
			}
		}
	}
	/**
	 * Removes the value pair with the given key from the series with the given index.
	 * @param seriesIndex The series index
	 * @param key The key
	 * @throws NoSuchSeriesException Thrown if there is no series with the specified index
	 */
	public void removeValuePair(int seriesIndex, Number key) throws NoSuchSeriesException {
		if(seriesIndex < this.getSeriesCount()){
			TimeSeries<String> series = this.getSeries(seriesIndex);
			series.delete(new FixedMillisecond(key.longValue()));
		}else{
			throw new NoSuchSeriesException();
		}
		
	}

	/**
	 * Edits the data series by adding data.
	 *
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public void editSeriesAddData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {

		if (targetDataSeriesIndex<=(this.getSeriesCount()-1)) {
			// --- Get the series -------------------------
			TimeSeries<String> addToSeries = (TimeSeries<String>) this.getSeries(targetDataSeriesIndex);
			List valuePairs = ((agentgui.ontology.TimeSeries)series).getTimeSeriesValuePairs();
			for (int i = 0; i < valuePairs.size(); i++) {
				TimeSeriesValuePair valuePair = (TimeSeriesValuePair) valuePairs.get(i);
				Simple_Long simpleLong = valuePair.getTimestamp();
				Simple_Float simpleFloat = valuePair.getValue();
				
				Long timeStampLong = simpleLong.getLongValue();
				Float floatValue = simpleFloat.getFloatValue();
				if (timeStampLong!=null) {
					TimeSeriesDataItem newItem = new TimeSeriesDataItem(new FixedMillisecond(timeStampLong), floatValue);
					addToSeries.addOrUpdate(newItem);
				}
			}
			
		} else {
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Edits the data series by adding or exchanging data.
	 *
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public void editSeriesAddOrExchangeData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {
		
		if (targetDataSeriesIndex<=(this.getSeriesCount()-1)) {
			TimeSeries<String> addToSeries = (TimeSeries<String>) this.getSeries(targetDataSeriesIndex);
			List valuePairs = ((agentgui.ontology.TimeSeries)series).getTimeSeriesValuePairs();
			for (int i = 0; i < valuePairs.size(); i++) {
				TimeSeriesValuePair valuePair = (TimeSeriesValuePair) valuePairs.get(i);
				Simple_Long simpleLong = valuePair.getTimestamp();
				Simple_Float simpleFloat = valuePair.getValue();
				
				Long timeStampLong = simpleLong.getLongValue();
				Float floatValue = simpleFloat.getFloatValue();
				if (timeStampLong!=null) {
					TimeSeriesDataItem newItem = new TimeSeriesDataItem(new FixedMillisecond(timeStampLong), floatValue);
					addToSeries.addOrUpdate(newItem);
				}
			}

		} else {
			throw new NoSuchSeriesException();			
		}
	}

	/**
	 * Edits the data series by exchanging data.
	 *
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public void editSeriesExchangeData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {

		if (targetDataSeriesIndex<=(this.getSeriesCount()-1)) {
			TimeSeries<String> exchangeSeries = (TimeSeries<String>) this.getSeries(targetDataSeriesIndex);
			List valuePairs = ((agentgui.ontology.TimeSeries)series).getTimeSeriesValuePairs();
			for (int i = 0; i < valuePairs.size(); i++) {
				TimeSeriesValuePair valuePair = (TimeSeriesValuePair) valuePairs.get(i);
				Simple_Long simpleLong = valuePair.getTimestamp();
				Simple_Float simpleFloat = valuePair.getValue();
				
				Long timeStampLong = simpleLong.getLongValue();
				Float floatValue = simpleFloat.getFloatValue();
				if (timeStampLong!=null) {
					try {
						exchangeSeries.update(new FixedMillisecond(timeStampLong), floatValue);
					} catch (SeriesException se) {
						// --- Nothing to do here, just take the next value ---
					}
				}
			}

		} else {
			throw new NoSuchSeriesException();			
		}
	}

	/**
	 * Edits the data series by remove data.
	 *
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public void editSeriesRemoveData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {

		if (targetDataSeriesIndex<=(this.getSeriesCount()-1)) {
			TimeSeries<String> removeSeries = (TimeSeries<String>) this.getSeries(targetDataSeriesIndex);
			List valuePairs = ((agentgui.ontology.TimeSeries)series).getTimeSeriesValuePairs();
			for (int i = 0; i < valuePairs.size(); i++) {
				TimeSeriesValuePair valuePair = (TimeSeriesValuePair) valuePairs.get(i);
				Simple_Long simpleLong = valuePair.getTimestamp();
				Long timeStampLong = simpleLong.getLongValue();
				if (timeStampLong!=null) {
					removeSeries.delete(new FixedMillisecond(timeStampLong));
				}
			}

		} else {
			throw new NoSuchSeriesException();			
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#getSeries(int)
	 */
	@Override
	public TimeSeries<String> getSeries(int seriesIndex) {
		return this.getTimeSeriesCollection().getSeries(seriesIndex);
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#getSeries(java.lang.String)
	 */
	@Override
	public TimeSeries<String> getSeries(String seriesLabel) {
		return this.getTimeSeriesCollection().getSeries(seriesLabel);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#removeSeries(int)
	 */
	@Override
	public void removeSeries(int seriesIndex) {
		this.getTimeSeriesCollection().removeSeries(seriesIndex);
		this.setChanged();
		this.notifyObservers(ChartModel.EventType.SERIES_REMOVED);
	}
	
	/**
	 * Gets the JFreeChart data model for this chart
	 * @return The JFreeChart data model for this chart
	 */
	public TimeSeriesCollection<String> getTimeSeriesCollection(){
		if (this.timeSeriesCollection == null) {
			this.timeSeriesCollection = new TimeSeriesCollection<>(TimeZone.getTimeZone("GMT"));
		}
		return this.timeSeriesCollection;
	}
	
	/**
	 * Returns the number of series in this chart.
	 * @return the number of series 
	 */
	public int getSeriesCount(){
		return this.getTimeSeriesCollection().getSeriesCount();
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#setSeriesLabel(int, java.lang.String)
	 */
	@Override
	public void setSeriesLabel(int seriesIndex, String newLabel) {
		
		// --- Try getting the current series -------------
		TimeSeries<String> oldSeries = this.getSeries(seriesIndex);
		if (oldSeries==null || oldSeries.getKey().equals(newLabel)==true) return;
		
		// --- Create a new series ------------------------
		TimeSeries<String> newSeries = new TimeSeries<>(newLabel);
		newSeries.setMaximumItemCount(oldSeries.getMaximumItemCount());
		newSeries.setMaximumItemAge(oldSeries.getMaximumItemAge());
		
		for (int i = 0; i < oldSeries.getItemCount(); i++) {
			newSeries.add(oldSeries.getDataItem(i));
		}
		// --- Exchange the series ------------------------
		this.exchangeSeries(seriesIndex, newSeries, false);

		// --- Notify -------------------------------------
		this.setChanged();
		this.notifyObservers(ChartModel.EventType.SERIES_RENAMED);
	}
	
}
