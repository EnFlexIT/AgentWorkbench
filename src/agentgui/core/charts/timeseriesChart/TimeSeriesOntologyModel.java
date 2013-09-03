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
import agentgui.ontology.TimeSeriesAdditionalSettings;
import agentgui.ontology.TimeSeriesChart;

public class TimeSeriesOntologyModel extends OntologyModel{
	
	public TimeSeriesOntologyModel(TimeSeriesChart timeSeriesChart, TimeSeriesDataModel parent){
		if(timeSeriesChart != null){
			this.chart = timeSeriesChart;
		}else{
			this.chart = new TimeSeriesChart();
			this.chart.setVisualizationSettings(new ChartSettingsGeneral());
			((TimeSeriesChart)this.chart).setTimeSeriesAdditionalSettings(new TimeSeriesAdditionalSettings());
		}
		this.parent = parent;
	}

	/**
	 * Gets the complete ontology representation (data and visualization settings) for this chart.
	 * @return the timeSeriesChart
	 */
	public TimeSeriesChart getTimeSeriesChart() {
		return ((TimeSeriesChart) chart);
	}

	/**
	 * @param timeSeriesChart The timeSeriesChart to set
	 */
	public void setTimeSeriesChart(TimeSeriesChart timeSeriesChart) {
		this.chart = timeSeriesChart;
	}
	
	public TimeSeriesAdditionalSettings getAdditionalSettings(){
		return ((TimeSeriesChart) chart).getTimeSeriesAdditionalSettings();
	}
	@Override
	public void addSeries(DataSeries series){
		((TimeSeriesChart) chart).getTimeSeriesChartData().add(series);
	}
	@Override
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			((TimeSeriesChart) chart).getTimeSeriesChartData().remove(seriesIndex);
			chart.getVisualizationSettings().getYAxisColors().remove(seriesIndex);
			chart.getVisualizationSettings().getYAxisLineWidth().remove(seriesIndex);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	@Override
	public int getSeriesCount(){
		return ((TimeSeriesChart) chart).getTimeSeriesChartData().size();
	}
	
	@Override
	public List getChartData(){
		return ((TimeSeriesChart) chart).getTimeSeriesChartData();
	}
	
	@Override
	public TimeSeries getSeries(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			TimeSeries series = (TimeSeries) ((TimeSeriesChart) chart).getTimeSeriesChartData().get(seriesIndex);
			return series;
		}else{
			throw new NoSuchSeriesException();
		}
	}
	@Override
	public void exchangeSeries(int seriesIndex, DataSeries dataSeries) throws NoSuchSeriesException {
		if (seriesIndex<getSeriesCount()) {
			TimeSeriesChart tsc = (TimeSeriesChart) this.chart;
			tsc.getTimeSeriesChartData().remove(seriesIndex);
			tsc.getTimeSeriesChartData().add(seriesIndex, dataSeries);
		} else {
			throw new NoSuchSeriesException();
		}
	}
	
	@Override
	public List getSeriesData(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			TimeSeries series = (TimeSeries) ((TimeSeriesChart) chart).getTimeSeriesChartData().get(seriesIndex);
			return (List) series.getTimeSeriesValuePairs();
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Sets the time format in the settings object
	 * @param timeFormat
	 */
	void setTimeFormat(String timeFormat){
		getAdditionalSettings().setTimeFormat(timeFormat);
	}
}
