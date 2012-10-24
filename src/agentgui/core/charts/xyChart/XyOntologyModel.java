package agentgui.core.charts.xyChart;

import jade.util.leap.List;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.OntologyModel;
import agentgui.ontology.DataSeries;
import agentgui.ontology.XyChart;
import agentgui.ontology.XyDataSeries;

public class XyOntologyModel extends OntologyModel {
	
	public XyOntologyModel(XyChart chart, XyDataModel parent){
		this.chart = chart;
		this.parent = parent;
	}
	
	public XyChart getXyChart(){
		return (XyChart) chart;
	}

	@Override
	public List getSeriesData(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			XyDataSeries series = (XyDataSeries) ((XyChart) chart).getXyChartData().get(seriesIndex);
			return (List) series.getXyValuePairs();
		}else{
			throw new NoSuchSeriesException();
		}
	}

	@Override
	public void addSeries(DataSeries series) {
		((XyChart)chart).getXyChartData().add(series);

	}

	@Override
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			((XyChart) chart).getXyChartData().remove(seriesIndex);
			chart.getVisualizationSettings().getYAxisColors().remove(seriesIndex);
			chart.getVisualizationSettings().getYAxisLineWidth().remove(seriesIndex);
		}else{
			throw new NoSuchSeriesException();
		}
	}

	@Override
	public int getSeriesCount() {
		return ((XyChart) chart).getXyChartData().size();
	}

	@Override
	public List getChartData() {
		return ((XyChart) chart).getXyChartData();
	}

	@Override
	public XyDataSeries getSeries(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			XyDataSeries series = (XyDataSeries) ((XyChart) chart).getXyChartData().get(seriesIndex);
			return series;
		}else{
			throw new NoSuchSeriesException();
		}
	}

}
