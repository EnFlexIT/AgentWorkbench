package agentgui.core.charts.timeseriesChart;

import java.awt.Color;

import agentgui.core.charts.ChartSettings;
import agentgui.core.charts.SeriesSettings;
import agentgui.ontology.Chart;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;

public class TimeSeriesChartSettings extends ChartSettings {

	public TimeSeriesChartSettings(Chart chart) {
		super(chart);
		TimeSeriesChart tsChart = (TimeSeriesChart) chart;
		for(int i=0; i< tsChart.getTimeSeriesChartData().size(); i++){
			TimeSeries series = (TimeSeries) tsChart.getTimeSeriesChartData().get(i);
			SeriesSettings settings = new SeriesSettings();
			settings.setLabel(series.getLabel());
			String colorString = (String) chart.getVisualizationSettings().getYAxisColors().get(i);
			settings.setColor(new Color(Integer.parseInt(colorString)));
			settings.setLineWIdth((Float) chart.getVisualizationSettings().getYAxisLineWidth().get(i));
		}
	}

}
