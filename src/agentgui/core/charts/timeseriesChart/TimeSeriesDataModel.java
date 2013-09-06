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

import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.util.Date;

import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.gui.ChartTab;
import agentgui.ontology.DataSeries;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.Simple_Long;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesValuePair;
import agentgui.ontology.ValuePair;
import agentgui.simulationService.time.TimeModelDateBased;

/**
 * Container class managing the data models for the different time series representations.
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 *
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
	
	/** This will be the basic date for relative time values */
	private Date startDate;
	
	
	/**
	 * Constructor.
	 *
	 * @param timeSeriesChart The ontology representation of the series to be displayed
	 * @param defaultTimeFormat the default time format
	 */
	public TimeSeriesDataModel(TimeSeriesChart timeSeriesChart, String defaultTimeFormat){
		
		this.startDate = new Date(0);
		
		// Initialize the three sub models
		this.ontologyModel = new TimeSeriesOntologyModel(timeSeriesChart, this);
		this.chartModel = new TimeSeriesChartModel();
		this.tableModel = new TimeSeriesTableModel(this);
		
		TimeSeriesOntologyModel tsom = (TimeSeriesOntologyModel) this.ontologyModel;
		
		// TODO chart == null abfangen
		
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
			((TimeSeriesOntologyModel)this.ontologyModel).getAdditionalSettings().setTimeFormat(defaultTimeFormat);
			
		} else {
			
			// The ontology model contains data, add it to the other sub models 
			Iterator dataSeries = tsom.getTimeSeriesChart().getAllTimeSeriesChartData();
			while(dataSeries.hasNext()){
				
				TimeSeries nextSeries = (TimeSeries) dataSeries.next();
				
				// If there is no color specified for this series, use a default color
				if(seriesCount > this.ontologyModel.getChartSettings().getYAxisColors().size()){
					this.ontologyModel.getChartSettings().addYAxisColors(""+DEFAULT_COLORS[(seriesCount-1) % DEFAULT_COLORS.length].getRGB());
				}
				
				// If there is no line width specified for this series, use a default line width
				if(seriesCount > this.ontologyModel.getChartSettings().getYAxisLineWidth().size()){
					this.ontologyModel.getChartSettings().addYAxisLineWidth(DEFAULT_LINE_WIDTH);
				}
				chartModel.addSeries(nextSeries);
				tableModel.addSeries(nextSeries);
				seriesCount++;
			}
		}
		
		// Register for table model events
		tableModel.addTableModelListener(this);
	}

	/**
	 * @return the ontologyModel
	 */
	public TimeSeriesOntologyModel getTimeSeriesOntologyModel() {
		return (TimeSeriesOntologyModel) ontologyModel;
	}

	/**
	 * @param ontologyModel the ontologyModel to set
	 */
	public void setTimeSeriesOntologyModel(TimeSeriesOntologyModel ontologyModel) {
		this.ontologyModel = ontologyModel;
	}

	/**
	 * @return the chartModel
	 */
	public TimeSeriesChartModel getTimeSeriesChartModel() {
		return (TimeSeriesChartModel) chartModel;
	}

	/**
	 * @param chartModel the chartModel to set
	 */
	public void setTimeSeriesChartModel(TimeSeriesChartModel chartModel) {
		this.chartModel = chartModel;
	}

	/**
	 * @return the tableModel
	 */
	public TimeSeriesTableModel getTimeSeriesTableModel() {
		return (TimeSeriesTableModel) tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTimeSeriesTableModel(TimeSeriesTableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the timeFormat
	 */
	public String getTimeFormat() {
		if (((TimeSeriesOntologyModel)getOntologyModel()).getAdditionalSettings().getTimeFormat()==null) {
			((TimeSeriesOntologyModel)getOntologyModel()).getAdditionalSettings().setTimeFormat(TimeModelDateBased.DEFAULT_TIME_FORMAT);
		} 
		return ((TimeSeriesOntologyModel)getOntologyModel()).getAdditionalSettings().getTimeFormat();
	}

	/**
	 * @param timeFormat the timeFormat to set
	 */
	public void setTimeFormat(String timeFormat) {
		((TimeSeriesOntologyModel)getOntologyModel()).getAdditionalSettings().setTimeFormat(timeFormat);
	}

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
	public Number getKeyFromPair(ValuePair vp) {
		TimeSeriesValuePair tsvp = (TimeSeriesValuePair) vp;
		return tsvp.getTimestamp().getLongValue();
	}

	@Override
	public Number getValueFromPair(ValuePair vp) {
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

	@Override
	public String getDefaultSeriesLabel() {
		return DEFAULT_SERIES_LABEL+" "+(getSeriesCount()+1);
	}

	@Override
	public TimeSeries createNewDataSeries(String label) {
		TimeSeries newSeries = new TimeSeries();
		newSeries.setLabel(label);
		return newSeries;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesAddData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesAddData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		TimeSeries addTimeSeries = (TimeSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			TimeSeries localTimeSeries = (TimeSeries) this.ontologyModel.getSeriesData(targetDataSeriesIndex);
			TimeSeriesHelper localTimeSeriesHelper = new TimeSeriesHelper(localTimeSeries);
			localTimeSeriesHelper.addSeriesData(addTimeSeries);
		}
		// --- Edit chart model ---------------------------
		this.chartModel.editSeriesAddData(addTimeSeries, targetDataSeriesIndex);
		// --- Edit table model ---------------------------
		this.tableModel.editSeriesAddData(addTimeSeries, targetDataSeriesIndex);
		
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesAddOrExchangeData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesAddOrExchangeData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		TimeSeries addExTimeSeries = (TimeSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			TimeSeries localTimeSeries = (TimeSeries) this.ontologyModel.getSeriesData(targetDataSeriesIndex);
			TimeSeriesHelper localTimeSeriesHelper = new TimeSeriesHelper(localTimeSeries);
			localTimeSeriesHelper.addOrExchangeSeriesData(addExTimeSeries);
		}
		// --- Edit chart model ---------------------------
		this.chartModel.editSeriesAddOrExchangeData(addExTimeSeries, targetDataSeriesIndex);
		// --- Edit table model ---------------------------
		this.tableModel.editSeriesAddOrExchangeData(addExTimeSeries, targetDataSeriesIndex);
		
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesExchangeData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesExchangeData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		TimeSeries exTimeSeries = (TimeSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			TimeSeries localTimeSeries = (TimeSeries) this.ontologyModel.getSeriesData(targetDataSeriesIndex);
			TimeSeriesHelper localTimeSeriesHelper = new TimeSeriesHelper(localTimeSeries);
			localTimeSeriesHelper.exchangeSeriesData(exTimeSeries);
		}
		// --- Edit chart model ---------------------------
		
		// --- Edit table model ---------------------------
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesRemoveData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesRemoveData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		TimeSeries removeTimeSeries = (TimeSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			TimeSeries localTimeSeries = (TimeSeries) this.ontologyModel.getSeriesData(targetDataSeriesIndex);
			TimeSeriesHelper localTimeSeriesHelper = new TimeSeriesHelper(localTimeSeries);
			localTimeSeriesHelper.removeSeriesData(removeTimeSeries);
		}
		// --- Edit chart model ---------------------------
		
		// --- Edit table model ---------------------------
	}

}
