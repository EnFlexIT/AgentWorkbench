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
package agentgui.core.charts;

import java.awt.Color;
import java.util.List;
import java.util.Vector;

import agentgui.ontology.Chart;

/**
 * This class manages the chart settings.
 * @author Nils
 *
 */
public abstract class ChartSettings{
	private String chartTitle;
	private String xAxisLabel;
	private String yAxisLabel;
	private String rendererType;
	private List<SeriesSettings> seriesSettings;
	
	public ChartSettings(Chart chart){
		chartTitle = chart.getVisualizationSettings().getChartTitle();
		xAxisLabel = chart.getVisualizationSettings().getXAxisLabel();
		yAxisLabel = chart.getVisualizationSettings().getYAxisLabel();
		rendererType = chart.getVisualizationSettings().getRendererType();
		
		seriesSettings = new Vector<SeriesSettings>();
	}

	public void addSeriesSettings(SeriesSettings settings){
		seriesSettings.add(settings);
	}
	
	public void removeSeriesSettings(int seriesIndex){
		if(seriesIndex < seriesSettings.size()){
			seriesSettings.remove(seriesIndex);
		}
	}

	/**
	 * @return the chartTitle
	 */
	public String getChartTitle() {
		return chartTitle;
	}

	/**
	 * @param chartTitle the chartTitle to set
	 */
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	/**
	 * @return the xAxisLabel
	 */
	public String getxAxisLabel() {
		return xAxisLabel;
	}

	/**
	 * @param xAxisLabel the xAxisLabel to set
	 */
	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}

	/**
	 * @return the yAxisLabel
	 */
	public String getyAxisLabel() {
		return yAxisLabel;
	}

	/**
	 * @param yAxisLabel the yAxisLabel to set
	 */
	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}

	/**
	 * @return the rendererType
	 */
	public String getRendererType() {
		return rendererType;
	}

	/**
	 * @param rendererType the rendererType to set
	 */
	public void setRendererType(String rendererType) {
		this.rendererType = rendererType;
	}

	/**
	 * @return the seriesSettings
	 */
	public List<SeriesSettings> getSeriesSettings() {
		return seriesSettings;
	}
	
	public SeriesSettings getSeriesSettings(int seriesIndex) throws NoSuchSeriesException{
		if(seriesIndex < seriesSettings.size()){
			return seriesSettings.get(seriesIndex);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public void setSeriesLabel(int seriesIndex, String label) throws NoSuchSeriesException{
		if(seriesIndex < seriesSettings.size()){
			seriesSettings.get(seriesIndex).setLabel(label);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	public void setSeriesColor(int seriesIndex, Color color) throws NoSuchSeriesException{
		if(seriesIndex < seriesSettings.size()){
			seriesSettings.get(seriesIndex).setColor(color);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	public void setSeriesLineWidth(int seriesIndex, float lineWidth) throws NoSuchSeriesException{
		if(seriesIndex < seriesSettings.size()){
			seriesSettings.get(seriesIndex).setLineWIdth(lineWidth);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	
}
