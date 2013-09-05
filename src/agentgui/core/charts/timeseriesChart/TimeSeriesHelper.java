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

import java.util.TreeMap;

import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesValuePair;

/**
 * The Class TimeSeriesHelper provides some methods to work with TimeSeries.
 */
public class TimeSeriesHelper extends TreeMap<Long, TimeSeriesValuePair>{

	private static final long serialVersionUID = 5313715263015405675L;

	private TimeSeries timeSeries = null;
	
	
	/**
	 * Instantiates a new time series helper.
	 */
	public TimeSeriesHelper() {	}
	/**
	 * Instantiates a new time series helper.
	 * @param timeSeries the current time series
	 */
	public TimeSeriesHelper(TimeSeries timeSeries) {
		this.setTimeSeries(timeSeries);	
	}
	
	/**
	 * Sets the current time series.
	 * @param timeSeries the new time series
	 */
	public void setTimeSeries(TimeSeries timeSeries) {
		this.timeSeries = timeSeries;
		this.putToTreeMap();
	}
	/**
	 * Gets the current time series.
	 * @return the time series
	 */
	public TimeSeries getTimeSeries() {
		if (this.timeSeries==null) {
			this.timeSeries = new TimeSeries();
			this.timeSeries.setLabel("New TimeSeries");
		}
		return timeSeries;
	}
	/**
	 * Puts the current TimeSeries into the local TreeMap.
	 */
	private void putToTreeMap() {
		this.clear();
		List valuePairs = this.timeSeries.getTimeSeriesValuePairs();
		for (int i = 0; i < valuePairs.size(); i++) {
			TimeSeriesValuePair tsvp = (TimeSeriesValuePair) valuePairs.get(i);
			Long timeStamp = tsvp.getTimestamp().getLongValue();
			this.put(timeStamp, tsvp);
		}
	}
	
	/**
	 * Resets the current time series from local TreeMap.
	 */
	private void resetTimeSeriesFromTreeMap() {
		
		// --- Clear current TimeSeries ---------
		this.getTimeSeries().getTimeSeriesValuePairs().clear();
		
		// --- rebuild  TimeSeries --------------
		Long[] keys = new Long[this.size()];
		this.keySet().toArray(keys);
		for (int i = 0; i < keys.length; i++) {
			this.getTimeSeries().getTimeSeriesValuePairs().add(this.get(keys[i]));
		}
		
	}
	
	/**
	 * Adds new series data to the current TimeSeries, if the concrete data is new.
	 * If the data (time stamp) is already there, it will not be overwritten.
	 * @param additionalTimeSeries the TimeSereis containing new data 
	 */
	public void addSeriesData(TimeSeries additionalTimeSeries) {
		this.addSeriesData(additionalTimeSeries.getTimeSeriesValuePairs());
	}
	/**
	 * Adds new series data to the current TimeSeries, if the concrete data is new.
	 * If the data (time stamp) is already there, it will not be overwritten.
	 * @param listOfTimeSeriesValuePairs the list of time series value pairs
	 */
	public void addSeriesData(List listOfTimeSeriesValuePairs) {
		for (int i = 0; i < listOfTimeSeriesValuePairs.size(); i++) {
			TimeSeriesValuePair tsvp = (TimeSeriesValuePair) listOfTimeSeriesValuePairs.get(i);
			Long timeStamp = tsvp.getTimestamp().getLongValue();
			if (this.containsKey(timeStamp)==false) {
				this.getTimeSeries().getTimeSeriesValuePairs().add(tsvp);
				this.put(timeStamp, tsvp);
			}
		}
	}
	
	/**
	 * Adds the or exchanges series data with the data of the specified TimeSeries.
	 * @param additionalTimeSeries the additional time series
	 */
	public void addOrExchangeSeriesData(TimeSeries additionalTimeSeries) {
		this.addOrExchangeSeriesData(additionalTimeSeries.getTimeSeriesValuePairs());
	}
	/**
	 * Adds the or exchanges series data with the specified .
	 * @param listOfTimeSeriesValuePairs the list of time series value pairs
	 */
	public void addOrExchangeSeriesData(List listOfTimeSeriesValuePairs) {
		for (int i = 0; i < listOfTimeSeriesValuePairs.size(); i++) {
			TimeSeriesValuePair tsvp = (TimeSeriesValuePair) listOfTimeSeriesValuePairs.get(i);
			Long timeStamp = tsvp.getTimestamp().getLongValue();
			this.put(timeStamp, tsvp);
		}
		this.resetTimeSeriesFromTreeMap();
	}

	/**
	 * Exchanges series data, if the concrete time stamps are available.
	 * @param additionalTimeSeries the additional time series
	 */
	public void exchangeSeriesData(TimeSeries additionalTimeSeries) {
		this.exchangeSeriesData(additionalTimeSeries.getTimeSeriesValuePairs());
	}
	/**
	 * Exchanges series data, if the concrete time stamps are available.
	 * @param listOfTimeSeriesValuePairs the list of time series value pairs
	 */
	public void exchangeSeriesData(List listOfTimeSeriesValuePairs) {
		for (int i = 0; i < listOfTimeSeriesValuePairs.size(); i++) {
			TimeSeriesValuePair tsvp = (TimeSeriesValuePair) listOfTimeSeriesValuePairs.get(i);
			Long timeStamp = tsvp.getTimestamp().getLongValue();
			if (this.containsKey(timeStamp)) {
				this.put(timeStamp, tsvp);	
			}
		}
		this.resetTimeSeriesFromTreeMap();
	}
	
	/**
	 * Removes the series data specified by the time stamps.
	 * @param additionalTimeSeries the additional time series
	 */
	public void removeSeriesData(TimeSeries additionalTimeSeries) {
		this.removeSeriesData(additionalTimeSeries.getTimeSeriesValuePairs());
	}
	/**
	 * Removes the series data specified by the time stamps.
	 * @param listOfTimeSeriesValuePairs the list of time series value pairs
	 */
	public void removeSeriesData(List listOfTimeSeriesValuePairs) {
		for (int i = 0; i < listOfTimeSeriesValuePairs.size(); i++) {
			TimeSeriesValuePair tsvp = (TimeSeriesValuePair) listOfTimeSeriesValuePairs.get(i);
			Long timeStamp = tsvp.getTimestamp().getLongValue();
			this.remove(timeStamp);
		}
		this.resetTimeSeriesFromTreeMap();
	}
	

}
