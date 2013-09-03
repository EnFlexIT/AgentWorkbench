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

import jade.util.leap.List;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.OntologyModel;
import agentgui.ontology.ChartSettingsGeneral;
import agentgui.ontology.DataSeries;
import agentgui.ontology.XyChart;
import agentgui.ontology.XyDataSeries;

public class XyOntologyModel extends OntologyModel {
	
	public XyOntologyModel(XyChart chart, XyDataModel parent){
		if(chart != null){
			this.chart = chart;
		}else{
			this.chart = new XyChart();
			this.chart.setVisualizationSettings(new ChartSettingsGeneral());
		}
		
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
	@Override
	public void exchangeSeries(int seriesIndex, DataSeries dataSeries) throws NoSuchSeriesException {
		if (seriesIndex<getSeriesCount()) {
			XyChart xyc = (XyChart) this.chart;
			xyc.getXyChartData().remove(seriesIndex);
			xyc.getXyChartData().add(seriesIndex, dataSeries);
		} else {
			throw new NoSuchSeriesException();
		}
	}

}
