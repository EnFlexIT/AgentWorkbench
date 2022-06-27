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
import agentgui.ontology.Simple_Float;
import agentgui.ontology.XyChart;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XySeriesChartSettings;
import agentgui.ontology.XyValuePair;

/**
 * The Class XyOntologyModel.
 */
public class XyOntologyModel extends OntologyModel {
	
	private XyDataModel dataModel;
	private XyChart xyChart;
	
	/**
	 * Instantiates a new xy ontology model.
	 *
	 * @param chart the chart
	 * @param parent the parent
	 */
	public XyOntologyModel(XyChart chart, XyDataModel parent){
		if(chart != null){
			this.xyChart = chart;
		}else{
			this.xyChart = new XyChart();
			this.xyChart.setXySeriesVisualisationSettings(new XySeriesChartSettings());
		}
		this.dataModel = parent;
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getChartSettings()
	 */
	public ChartSettingsGeneral getChartSettings(){
		return this.xyChart.getXySeriesVisualisationSettings();
	}
	
	/**
	 * Gets the xy chart.
	 * @return the xy chart
	 */
	public XyChart getXyChart(){
		return xyChart;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getSeriesData(int)
	 */
	@Override
	public List getSeriesData(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			XyDataSeries series = (XyDataSeries) xyChart.getXyChartData().get(seriesIndex);
			return (List) series.getXyValuePairs();
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#addSeries(agentgui.ontology.DataSeries)
	 */
	@Override
	public void addSeries(DataSeries series) {
		this.xyChart.getXyChartData().add(series);

	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#exchangeSeries(int, agentgui.ontology.DataSeries)
	 */
	@Override
	public void exchangeSeries(int seriesIndex, DataSeries dataSeries) throws NoSuchSeriesException {
		if (seriesIndex>-1 && seriesIndex<getSeriesCount()) {
			this.xyChart.getXyChartData().remove(seriesIndex);
			this.xyChart.getXyChartData().add(seriesIndex, dataSeries);
		} else {
			throw new NoSuchSeriesException();
		}
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#removeSeries(int)
	 */
	@Override
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			this.xyChart.getXyChartData().remove(seriesIndex);
			this.xyChart.getXySeriesVisualisationSettings().getYAxisColors().remove(seriesIndex);
			this.xyChart.getXySeriesVisualisationSettings().getYAxisLineWidth().remove(seriesIndex);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getSeries(int)
	 */
	@Override
	public XyDataSeries getSeries(int seriesIndex) throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			XyDataSeries series = (XyDataSeries) xyChart.getXyChartData().get(seriesIndex);
			return series;
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Returns the specified XyValuePair.
	 *
	 * @param seriesIndex the series index
	 * @param indexPosition the index position
	 * @return the XyValuePair
	 */
	public XyValuePair getXyValuePair(int seriesIndex, int indexPosition) {
		XyValuePair vp = null;
		try {
			vp=(XyValuePair) this.getSeries(seriesIndex).getXyValuePairs().get(indexPosition);
		} catch (NoSuchSeriesException nsse) {
			nsse.printStackTrace();
		}
		return vp;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getSeriesCount()
	 */
	@Override
	public int getSeriesCount() {
		return xyChart.getXyChartData().size();
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.OntologyModel#getChartData()
	 */
	@Override
	public List getChartData() {
		return xyChart.getXyChartData();
	}

	/**
	 * Adds the specified values as XyValuePair to the specified position.
	 * @param seriesIndex the series index
	 * @param indexPosition the index position
	 * @param newXValue the new x value
	 * @param newYValue the new y value
	 */
	public void addXyValuePair(int seriesIndex, int indexPosition, float newXValue, float newYValue) {
		XyValuePair vp = (XyValuePair) this.dataModel.createNewValuePair(newXValue, newYValue);
		this.addXyValuePair(seriesIndex, indexPosition, vp);
	}
	/**
	 * Adds the specified values as XyValuePair to the specified position.
	 * @param seriesIndex the series index
	 * @param indexPosition the index position
	 * @param xyValuePair the xy value pair
	 */
	public void addXyValuePair(int seriesIndex, int indexPosition, XyValuePair xyValuePair) {
		try {
			this.getSeries(seriesIndex).getXyValuePairs().add(indexPosition, xyValuePair);
		} catch (NoSuchSeriesException nsse) {
			nsse.printStackTrace();
		}
	}
	
	/**
	 * Updates the specified XyValuePair with the given value.
	 * @param seriesIndex the series index
	 * @param indexPosition the index position
	 * @param newXValue the new x value
	 * @param newYValue the new y value
	 */
	public void updateXyValuePair(int seriesIndex, int indexPosition, float newXValue, float newYValue) {
		
		XyValuePair vp = this.getXyValuePair(seriesIndex, indexPosition);
		if (vp!=null) {
			Simple_Float xValue =  new Simple_Float();
			Simple_Float yValue =  new Simple_Float();
			xValue.setFloatValue(newXValue);
			yValue.setFloatValue(newYValue);
			vp.setXValue(xValue);
			vp.setYValue(yValue);
		}
		
	}

	/**
	 * Removes the specified XyValuePair from a series.
	 * @param seriesIndex the series index
	 * @param indexPosition the index position
	 * @return the removed XyValuePair
	 */
	public XyValuePair removeXyValuePair(int seriesIndex, int indexPosition) {
		XyValuePair vp = null;
		try {
			vp = (XyValuePair) this.getSeries(seriesIndex).getXyValuePairs().remove(indexPosition);
		} catch (NoSuchSeriesException nsse) {
			nsse.printStackTrace();
		}
		return vp;
	}

	
}
