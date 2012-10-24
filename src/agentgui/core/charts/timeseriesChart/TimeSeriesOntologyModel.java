package agentgui.core.charts.timeseriesChart;

import jade.util.leap.List;
import agentgui.core.charts.OntologyModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.DataSeries;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesAdditionalSettings;
import agentgui.ontology.TimeSeriesChart;

public class TimeSeriesOntologyModel extends OntologyModel{
	
	public TimeSeriesOntologyModel(TimeSeriesChart timeSeriesChart, TimeSeriesDataModel parent){
		this.chart = timeSeriesChart;
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
	public TimeSeries getSeries(int seriesIndex) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			TimeSeries series = (TimeSeries) ((TimeSeriesChart) chart).getTimeSeriesChartData().get(seriesIndex);
			return series;
		}else{
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
}
