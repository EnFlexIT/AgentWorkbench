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
package agentgui.core.charts.xyChart;

import java.awt.Color;

import agentgui.core.charts.ChartSettings;
import agentgui.core.charts.SeriesSettings;
import agentgui.ontology.Chart;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.XyChart;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XyValuePair;

/**
 * Settings handler class for XY charts. No additional functionality needed, 
 * this classes only purpose is to provide class symmetry between the different 
 * chart types, to avoid confusion for the developer. 
 * 
 * @author Nils
 *
 */
public class XyChartSettings extends ChartSettings {

	public XyChartSettings(Chart chart) {
		super(chart);
		
		// TODO In die Oberklasse?
		XyChart xyChart = (XyChart) chart;
		for(int i=0; i< xyChart.getXyChartData().size(); i++){
			
			XyDataSeries series = (XyDataSeries) xyChart.getXyChartData().get(i);
			if (this.isValidXyDataSeries(series)==true) {

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
	 * Checks if is valid xy data series.
	 *
	 * @param series the series
	 * @return true, if is valid xy data series
	 */
	private boolean isValidXyDataSeries(XyDataSeries series) {
	
		boolean isXyDataSeries = true;
		// --- Test if this is a real XY series -----------
		int seriesSize = series.getXyValuePairs().size();
		if (seriesSize==0) {
			return false;
			
		}
//		else  if (seriesSize==1) {
//			XyValuePair valuePair = (XyValuePair) series.getXyValuePairs().get(0);
//			Simple_Float x = valuePair.getXValue();
//			Simple_Float y = valuePair.getXValue();
//			
//			if (x.getFloatValue()==0.0 && y.getFloatValue()==0.0) {
//				return false;
//			}
//			
//		}
		return isXyDataSeries;
	}
	
}
