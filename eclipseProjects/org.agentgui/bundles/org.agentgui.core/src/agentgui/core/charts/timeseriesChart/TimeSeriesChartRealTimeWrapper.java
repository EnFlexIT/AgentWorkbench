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

import agentgui.ontology.Simple_Float;
import agentgui.ontology.Simple_Long;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesValuePair;

/**
 * This wrapper class provides methods to apply length restrictions 
 * on TimeSeriesChart ontology objects for real time data. 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeSeriesChartRealTimeWrapper {
	private TimeSeriesChart timeSeriesChart;
	private TimeSeriesLengthRestriction lengthRestriction;
	
	/**
	 * Instantiates a new time series chart real time wrapper.
	 *
	 * @param timeSeriesChart the time series chart
	 * @param lengthRestriction the length restriction
	 */
	public TimeSeriesChartRealTimeWrapper(TimeSeriesChart timeSeriesChart, TimeSeriesLengthRestriction lengthRestriction) {
		this.timeSeriesChart = timeSeriesChart;
		this.lengthRestriction = lengthRestriction;
	}
	
	/**
	 * Sets the time series chart.
	 * @param tsc the new time series chart
	 */
	public void setTimeSeriesChart(TimeSeriesChart tsc) {
		this.timeSeriesChart = tsc;
		this.timeSeriesChart.setRealTime(true);
	}
	/**
	 * Gets the time series chart.
	 * @return the time series chart
	 */
	public TimeSeriesChart getTimeSeriesChart() {
		return timeSeriesChart;
	}
	
	/**
	 * Gets the length restriction.
	 * @return the length restriction
	 */
	private TimeSeriesLengthRestriction getLengthRestriction() {
		if (this.lengthRestriction==null) {
			this.lengthRestriction = new TimeSeriesLengthRestriction();
			lengthRestriction.setMaxDuration(0);
			lengthRestriction.setMaxNumberOfStates(0);
		}
		return this.lengthRestriction;
	}

	/**
	 * Apply the length restriction on all time series.
	 */
	public void applyLengthRestriction() {
		for (int i=0; i<this.timeSeriesChart.getTimeSeriesChartData().size(); i++) {
			this.applyLengthRestriction(i);
		}
	}
	
	/**
	 * Apply the length restriction on a specific time series.
	 * @param timeSeriesIndex the time series index
	 */
	public void applyLengthRestriction(int timeSeriesIndex) {
		TimeSeries timeSeries = (TimeSeries) this.timeSeriesChart.getTimeSeriesChartData().get(timeSeriesIndex);
		this.applyLengthRestriction(timeSeries);
	}

	/**
	 * Apply the length restriction on a specific time series.
	 * @param timeSeries the time series
	 */
	public void applyLengthRestriction(TimeSeries timeSeries) {

		//TODO for debugging, remove when no longer needed
//		if (timeSeriesChart.getTimeSeriesVisualisationSettings().getChartTitle().equals("Current & Utilization for n87") && timeSeries.getLabel().equals("Current L1")) {
//			System.out.print(this.getClass().getSimpleName() + ": System n87, Series " + timeSeries.getLabel());
//			System.out.println(", " + timeSeries.getTimeSeriesValuePairs().size() + " states:");
//			for (int i=0; i<timeSeries.getTimeSeriesValuePairs().size(); i++) {
//				TimeSeriesValuePair tsvp = (TimeSeriesValuePair) timeSeries.getTimeSeriesValuePairs().get(i);
//				System.out.println(tsvp.getTimestamp().getStringLongValue());
//			}
//		}

		// ------------------------------------------------
		// --- Apply number of states restriction ---------
		
		if (this.getLengthRestriction().getMaxNumberOfStates()>0) {
			// --- Remove the first state while maxStates is exceeded -------------------
			while (timeSeries.getTimeSeriesValuePairs().size()>this.getLengthRestriction().getMaxNumberOfStates()) {
				timeSeries.getTimeSeriesValuePairs().remove(0);
			}
		}
		
		// ------------------------------------------------
		// --- Apply age restriction ----------------------
		
		if (this.getLengthRestriction().getMaxAge()>0) {
			// --- Determine the oldest time stamp that does not exceed maxAge ----------
			TimeSeriesValuePair lastValuePair = (TimeSeriesValuePair) timeSeries.getTimeSeriesValuePairs().get(timeSeries.getTimeSeriesValuePairs().size()-1);
			long oldestTimestampToKeep = lastValuePair.getTimestamp().getLongValue() - this.getLengthRestriction().getMaxAge();

			// --- Remove the first (=oldest) state while maxAge is exceeded ------------
			TimeSeriesValuePair firstValuePair = (TimeSeriesValuePair) timeSeries.getTimeSeriesValuePairs().get(0);
			
			while (firstValuePair.getTimestamp().getLongValue()<oldestTimestampToKeep) {
				timeSeries.getTimeSeriesValuePairs().remove(0);
			}
		}
	}
	 
	/**
	 * Adds a value pair to a time series. For real time charts, length restrictions will be applied.
	 * @param timeSeriesIndex the time series index
	 * @param timeStamp the time stamp
	 * @param value the value
	 */
	public void addValuePair(int timeSeriesIndex, long timeStamp, float value) {
		Simple_Long sl = new Simple_Long();
		sl.setLongValue(timeStamp);
		Simple_Float sf = new Simple_Float();
		sf.setFloatValue(value);
		
		TimeSeriesValuePair valuePair = new TimeSeriesValuePair();
		valuePair.setTimestamp(sl);
		valuePair.setValue(sf);
		
		this.addValuePair(timeSeriesIndex, valuePair);
	}
	
	/**
	 * Adds a value pair to a time series. For real time charts, length restrictions will be applied.
	 * @param timeSeriesIndex the time series index
	 * @param valuePair the value pair
	 */
	public void addValuePair(int timeSeriesIndex, TimeSeriesValuePair valuePair) {
		TimeSeries timeSeries = (TimeSeries) this.timeSeriesChart.getTimeSeriesChartData().get(timeSeriesIndex);
		if (timeSeries!=null) {

			// --- Check if there is already a value pair for this time stamp
			TimeSeriesValuePair existingValuePair = this.getValuePairForTimestamp(timeSeries, valuePair.getTimestamp().getLongValue());
			
			if (existingValuePair==null) {
				// --- Add a new value pair ---------------
				timeSeries.addTimeSeriesValuePairs(valuePair);
				if (timeSeriesChart.getRealTime()==true) {
					this.applyLengthRestriction(timeSeries);
				}
			} else {
				// --- Update an existing value pair ------
				existingValuePair.setValue(valuePair.getValue());
			}
		}
	}
	
	private TimeSeriesValuePair getValuePairForTimestamp(TimeSeries timeSeries, long timestamp) {
		for (int i=0; i<timeSeries.getTimeSeriesValuePairs().size(); i++) {
			TimeSeriesValuePair tsvp = (TimeSeriesValuePair) timeSeries.getTimeSeriesValuePairs().get(i);
			long tsvpTimestamp = tsvp.getTimestamp().getLongValue();
			if (tsvpTimestamp==timestamp) {
				return tsvp;
			}
		}
		return null;
	}
	
}
