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

import java.awt.Color;

import agentgui.core.charts.ChartSettings;
import agentgui.core.charts.SeriesSettings;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;

public class TimeSeriesChartSettings extends ChartSettings {
	
	private String timeFormat;

	public TimeSeriesChartSettings(TimeSeriesChart chart) {
		super(chart);
		this.timeFormat = chart.getTimeSeriesAdditionalSettings().getTimeFormat();
		
		// TODO In die Oberklasse?
		TimeSeriesChart tsChart = (TimeSeriesChart) chart;
		for(int i=0; i< tsChart.getTimeSeriesChartData().size(); i++){
			TimeSeries series = (TimeSeries) tsChart.getTimeSeriesChartData().get(i);
			if (series.isEmpty()==false) {
				SeriesSettings settings = new SeriesSettings();
				settings.setLabel(series.getLabel());
				String colorString = (String) chart.getVisualizationSettings().getYAxisColors().get(i);
				settings.setColor(new Color(Integer.parseInt(colorString)));
				settings.setLineWIdth((Float) chart.getVisualizationSettings().getYAxisLineWidth().get(i));
				
				this.addSeriesSettings(settings);
			}
		}
	}

	/**
	 * @return the timeFormat
	 */
	public String getTimeFormat() {
		return timeFormat;
	}

	/**
	 * @param timeFormat the timeFormat to set
	 */
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

}
