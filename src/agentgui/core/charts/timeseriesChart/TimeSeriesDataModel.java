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

import java.util.Observable;

import agentgui.core.charts.ChartSettingModel;
import agentgui.core.charts.ChartSettingModel.ChartSettingsUpdateNotification;
import agentgui.core.charts.ChartSettingModel.EventType;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.gui.ChartTab;
import agentgui.ontology.Chart;
import agentgui.ontology.DataSeries;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.Simple_Long;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesChartSettings;
import agentgui.ontology.TimeSeriesValuePair;
import agentgui.ontology.ValuePair;
import agentgui.simulationService.time.TimeModelDateBased;
import jade.util.leap.List;

/**
 * Container class managing the data models for the different time series representations.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class TimeSeriesDataModel extends DataModel {
	
	/** This label with appended seriesCount+1 will be used for newly added series */
	public static final String DEFAULT_SERIES_LABEL = "Time Series";
	/** This title will be used for the chart if none is specified */
	public static final String DEFAULT_CHART_TITLE = "Time Series Chart";
	/** This x axis label will be used for the chart if none is specified */
	public static final String DEFAULT_X_AXIS_LABEL = "Time";
	/** This y axis label will be used for the chart if none is specified */
	public static final String DEFAULT_Y_AXIS_LABEL = "Value";
	
	private String defaultTimeFormat;

	
	/** Instantiates a new TimeSeriesDataModel. */
	public TimeSeriesDataModel() {
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#setOntologyInstanceChart(agentgui.ontology.Chart)
	 */
	@Override
	public void setOntologyInstanceChart(Chart ontologyChart) {

		TimeSeriesChart timeSeriesChartNew = (TimeSeriesChart) ontologyChart;
		
		this.ontologyModel = new TimeSeriesOntologyModel(timeSeriesChartNew, this);
		this.chartModel = new TimeSeriesChartModel(this);
		if (this.tableModel==null) {
			this.tableModel = new TimeSeriesTableModel(this);	
		} else {
			this.tableModel.rebuildTableModel();
		}
		this.chartSettingModel = null;
		this.seriesCount=0;
		
		TimeSeriesOntologyModel tsom = (TimeSeriesOntologyModel) this.ontologyModel;
		if(tsom.getTimeSeriesChart().isEmpty()){
			
			// The ontology model is empty. Do some initialization work.
			
			// Remove the empty elements created by DynForm from the lists 
			tsom.getTimeSeriesChart().getTimeSeriesChartData().clear();
			this.ontologyModel.getChartSettings().getYAxisColors().clear();
			this.ontologyModel.getChartSettings().getYAxisLineWidth().clear();
			
			// Set some default values
			this.ontologyModel.getChartSettings().setChartTitle(DEFAULT_CHART_TITLE);
			this.ontologyModel.getChartSettings().setXAxisLabel(DEFAULT_X_AXIS_LABEL);
			this.ontologyModel.getChartSettings().setYAxisLabel(DEFAULT_Y_AXIS_LABEL);
			this.ontologyModel.getChartSettings().setRendererType(ChartTab.DEFAULT_RENDERER);
			((TimeSeriesChartSettings)((TimeSeriesOntologyModel)this.ontologyModel).getChartSettings()).setTimeFormat(this.getDefaultTimeFormat());
			
		} else {
			
			// The ontology model contains data, add it to the other sub models
			for (int i = 0; i < tsom.getTimeSeriesChart().getTimeSeriesChartData().size(); i++) {

				TimeSeries nextSeries = (TimeSeries) tsom.getTimeSeriesChart().getTimeSeriesChartData().get(i);
				
				// If there is no color specified for this series, use a default color
				if (seriesCount > this.ontologyModel.getChartSettings().getYAxisColors().size()) {
					this.ontologyModel.getChartSettings().addYAxisColors(""+DEFAULT_COLORS[(seriesCount-1) % DEFAULT_COLORS.length].getRGB());
				}
				
				// If there is no line width specified for this series, use a default line width
				if (seriesCount > this.ontologyModel.getChartSettings().getYAxisLineWidth().size()) {
					this.ontologyModel.getChartSettings().addYAxisLineWidth(DEFAULT_LINE_WIDTH);
				}
				this.chartModel.addSeries(nextSeries);
				this.seriesCount++;
			}
		}
		
	}
	
	/**
	 * Returns the current time series ontology model.
	 * @return the ontologyModel
	 */
	public TimeSeriesOntologyModel getTimeSeriesOntologyModel() {
		return (TimeSeriesOntologyModel) ontologyModel;
	}
	/**
	 * Returns the current time series chart model.
	 * @return the chartModel
	 */
	public TimeSeriesChartModel getTimeSeriesChartModel() {
		return (TimeSeriesChartModel) chartModel;
	}
	/**
	 * Returns the current time series table model.
	 * @return the time series table model
	 */
	public TimeSeriesTableModel getTimeSeriesTableModel() {
		return (TimeSeriesTableModel) this.tableModel;
	}
	
	/**
	 * Sets the default time format.
	 * @param defaultTimeFormat the new default time format
	 */
	public void setDefaultTimeFormat(String defaultTimeFormat) {
		this.defaultTimeFormat = defaultTimeFormat;
	}
	/**
	 * Returns the default time format.
	 * @return the default time format
	 */
	public String getDefaultTimeFormat() {
		return defaultTimeFormat;
	}
	
	/**
	 * @return the timeFormat
	 */
	public String getTimeFormat() {
		TimeSeriesChartSettings tscs = (TimeSeriesChartSettings)((TimeSeriesOntologyModel)this.ontologyModel).getChartSettings();
		if (tscs.getTimeFormat()==null) {
			tscs.setTimeFormat(TimeModelDateBased.DEFAULT_TIME_FORMAT);
		} 
		return tscs.getTimeFormat();
	}
	/**
	 * @param timeFormat the timeFormat to set
	 */
	public void setTimeFormat(String timeFormat) {
		TimeSeriesChartSettings tscs = (TimeSeriesChartSettings)((TimeSeriesOntologyModel)this.ontologyModel).getChartSettings();
		tscs.setTimeFormat(timeFormat);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#createNewDataSeries(java.lang.String)
	 */
	@Override
	public TimeSeries createNewDataSeries(String label) {
		TimeSeries newSeries = new TimeSeries();
		newSeries.setLabel(label);
		return newSeries;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#createNewValuePair(java.lang.Number, java.lang.Number)
	 */
	@Override
	public ValuePair createNewValuePair(Number key, Number value) {
		
		long lTimestamp = key.longValue();
		float fValue = value.floatValue();
		
		TimeSeriesValuePair tsvp = new TimeSeriesValuePair();
		Simple_Long slTimestamp = new Simple_Long();
		slTimestamp.setLongValue(lTimestamp);
		Simple_Float sfValue = new Simple_Float();
		sfValue.setFloatValue(fValue);
		tsvp.setTimestamp(slTimestamp);
		tsvp.setValue(sfValue);
		
		return tsvp;
	}

	@Override
	public Number getXValueFromPair(ValuePair vp) {
		TimeSeriesValuePair tsvp = (TimeSeriesValuePair) vp;
		return tsvp.getTimestamp().getLongValue();
	}

	@Override
	public Number getYValueFromValuePair(ValuePair vp) {
		TimeSeriesValuePair tsvp = (TimeSeriesValuePair) vp;
		return tsvp.getValue().getFloatValue();
	}

	@Override
	public List getValuePairsFromSeries(DataSeries series) {
		TimeSeries ts = (TimeSeries) series;
		return ts.getTimeSeriesValuePairs();
	}

	@Override
	public void setKeyForPair(Number key, ValuePair vp) {
		TimeSeriesValuePair tsvp = (TimeSeriesValuePair) vp;
		Simple_Long newTimeStamp = new Simple_Long();
		newTimeStamp.setLongValue(key.longValue());
		tsvp.setTimestamp(newTimeStamp);
	}

	@Override
	public void setValueForPair(Number value, ValuePair vp) {
		TimeSeriesValuePair tsvp = (TimeSeriesValuePair) vp;
		Simple_Float newValue = new Simple_Float();
		newValue.setFloatValue(value.floatValue());
		tsvp.setValue(newValue);
	}

	/**
	 * Removes the value pair with the given time stamp from every series that contains one. 
	 * @param key The time stamp
	 */
	public void removeValuePairsFromAllSeries(Number key){
		
		TimeSeriesOntologyModel ontoModel = (TimeSeriesOntologyModel) this.ontologyModel;
		TimeSeriesChartModel chartModel = (TimeSeriesChartModel) this.chartModel;
		
		for (int i=0; i<getSeriesCount(); i++) {
			try {
				ontoModel.removeValuePair(i, key);
				chartModel.removeValuePair(i, key);

			} catch (NoSuchSeriesException e) {
				System.err.println("Trying to remove value pair from non-existant series "+i);
				e.printStackTrace();
			}
		}
		tableModel.removeRowByKey(key);
	}
	
	@Override
	public String getDefaultSeriesLabel() {
		
		// Build a series label from the default label and the number of series.
		// If the resulting label already exists, increase the number.
		
		String seriesLabel;
		int suffix = this.getSeriesCount();
		do{
			suffix++;
			seriesLabel = DEFAULT_SERIES_LABEL+" "+suffix;
		}while(this.getChartModel().getSeries(seriesLabel) != null);
		
		return seriesLabel;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesAddData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesAddData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		TimeSeries addTimeSeries = (TimeSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			TimeSeries localTimeSeries = (TimeSeries) this.ontologyModel.getSeries(targetDataSeriesIndex);
			TimeSeriesHelper localTimeSeriesHelper = new TimeSeriesHelper(localTimeSeries);
			localTimeSeriesHelper.addSeriesData(addTimeSeries);
		}
		// --- Edit chart model ---------------------------
		this.getTimeSeriesChartModel().editSeriesAddData(addTimeSeries, targetDataSeriesIndex);
		// --- Edit table model ---------------------------
		this.getTimeSeriesTableModel().editSeriesAddData(addTimeSeries, targetDataSeriesIndex);
		
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesAddOrExchangeData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesAddOrExchangeData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		TimeSeries addExTimeSeries = (TimeSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			TimeSeries localTimeSeries = (TimeSeries) this.ontologyModel.getSeries(targetDataSeriesIndex);
			TimeSeriesHelper localTimeSeriesHelper = new TimeSeriesHelper(localTimeSeries);
			localTimeSeriesHelper.addOrExchangeSeriesData(addExTimeSeries);
		}
		// --- Edit chart model ---------------------------
		this.getTimeSeriesChartModel().editSeriesAddOrExchangeData(addExTimeSeries, targetDataSeriesIndex);
		// --- Edit table model ---------------------------
		this.getTimeSeriesTableModel().editSeriesAddOrExchangeData(addExTimeSeries, targetDataSeriesIndex);
		
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesExchangeData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesExchangeData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		TimeSeries exTimeSeries = (TimeSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			TimeSeries localTimeSeries = (TimeSeries) this.ontologyModel.getSeries(targetDataSeriesIndex);
			TimeSeriesHelper localTimeSeriesHelper = new TimeSeriesHelper(localTimeSeries);
			localTimeSeriesHelper.exchangeSeriesData(exTimeSeries);
		}
		// --- Edit chart model ---------------------------
		this.getTimeSeriesChartModel().editSeriesExchangeData(exTimeSeries, targetDataSeriesIndex);
		// --- Edit table model ---------------------------
		this.getTimeSeriesTableModel().editSeriesExchangeData(exTimeSeries, targetDataSeriesIndex);
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesRemoveData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesRemoveData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		TimeSeries removeTimeSeries = (TimeSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			TimeSeries localTimeSeries = (TimeSeries) this.ontologyModel.getSeries(targetDataSeriesIndex);
			TimeSeriesHelper localTimeSeriesHelper = new TimeSeriesHelper(localTimeSeries);
			localTimeSeriesHelper.removeSeriesData(removeTimeSeries);
		}
		// --- Edit chart model ---------------------------
		this.getTimeSeriesChartModel().editSeriesRemoveData(removeTimeSeries, targetDataSeriesIndex);
		// --- Edit table model ---------------------------
		this.getTimeSeriesTableModel().editSeriesRemoveData(removeTimeSeries, targetDataSeriesIndex);
	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
		
		if(o instanceof ChartSettingModel){
			ChartSettingsUpdateNotification notification = (ChartSettingsUpdateNotification) arg;
			if(notification.getEventType() == EventType.TIME_FORMAT_CHANGED){
				String newTimeFormat = (String) notification.getNewValue();
				this.setTimeFormat(newTimeFormat);
			}
		}
	}

	@Override
	public String getBaseStringForSeriesLabel() {
		return DEFAULT_SERIES_LABEL;
	}
	
}
