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

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import agentgui.core.charts.ChartModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.DataSeries;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XySeriesChartSettings;
import agentgui.ontology.XyValuePair;

public class XyChartModel extends XYSeriesCollection implements ChartModel {

	private static final long serialVersionUID = 8270571170143064082L;

	private XyDataModel xyDataModel;
	
	/**
	 * Instantiates a new XYChartModel.
	 * @param chartSettings the chart settings
	 */
	public XyChartModel(XyDataModel xyDataModel) {
		this.xyDataModel = xyDataModel;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#getChartSettings()
	 */
	@Override
	public XySeriesChartSettings getChartSettings() {
		return (XySeriesChartSettings) xyDataModel.getOntologyModel().getChartSettings();
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#addSeries(agentgui.ontology.DataSeries)
	 */
	@Override
	public void addSeries(DataSeries series) {
		
		XyDataSeries xyDataSeries = (XyDataSeries) series;
		XYSeries newSeries = new XYSeries(series.getLabel(), xyDataSeries.getAutoSort(), !xyDataSeries.getAvoidDuplicateXValues());
		
		List valuePairs = xyDataSeries.getXyValuePairs();
		for (int i = 0; i < valuePairs.size(); i++) {
			XyValuePair valuePair = (XyValuePair) valuePairs.get(i);
			newSeries.add(valuePair.getXValue().getFloatValue(), valuePair.getYValue().getFloatValue());
		}
		this.addSeries(newSeries);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#exchangeSeries(int, agentgui.ontology.DataSeries)
	 */
	@Override
	public void exchangeSeries(int seriesIndex, DataSeries series) throws NoSuchSeriesException {
		if(seriesIndex < this.getSeriesCount()){
			// --- edit series ---
			XYSeries editSeries = (XYSeries) this.getSeries().get(seriesIndex);
			editSeries.clear();
			if (series.getLabel()!=null) {
				editSeries.setKey(series.getLabel());
			}
			
			List valuePairs = ((agentgui.ontology.XyDataSeries)series).getXyValuePairs();
			for (int i = 0; i < valuePairs.size(); i++) {
				XyValuePair valuePair = (XyValuePair) valuePairs.get(i);
				editSeries.add(valuePair.getXValue().getFloatValue(), valuePair.getYValue().getFloatValue());	
			}
			
		}else{
			throw new NoSuchSeriesException();
		}
	}

	/**
	 * Returns the specified theXYSeriess.
	 * @param seriesIndex the series index
	 * @return the XYSeries
	 */
	public XYSeries getXySeries(int seriesIndex) {
		return this.getSeries(seriesIndex);
	}
	/**
	 * Returns the XYDataItem.
	 *
	 * @param seriesIndex the series index
	 * @param indexPosition the index position
	 * @return the xY data item
	 */
	public XYDataItem getXYDataItem(int seriesIndex, int indexPosition) {
		return this.getXySeries(seriesIndex).getDataItem(indexPosition);
	}
	
	/**
	 * Sets a XY series according to the ontology model.
	 */
	public void setXYSeriesAccordingToOntologyModel(int seriesIndex) {
		
		try {
			List xyData = this.xyDataModel.getOntologyModel().getSeriesData(seriesIndex);
			XYSeries xySeries = this.getXySeries(seriesIndex);	
			xySeries.clear();
			
			for (int i = 0; i < xyData.size()-1; i++) {
				XyValuePair vp = (XyValuePair) xyData.get(i);
				xySeries.add(vp.getXValue().getFloatValue(), vp.getYValue().getFloatValue(), false);
			}
			XyValuePair vp = (XyValuePair) xyData.get(xyData.size()-1);
			xySeries.add(vp.getXValue().getFloatValue(), vp.getYValue().getFloatValue());
			
		} catch (NoSuchSeriesException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the specified values as XYDataItem to the specified position.
	 * @param seriesIndex the series index
	 * @param indexPosition the index position
	 * @param newXValue the new x value
	 * @param newYValue the new y value
	 */
	public void addXyDataItem(int seriesIndex, int indexPosition, float newXValue, float newYValue) {
		XYSeries xySeries = this.getXySeries(seriesIndex);
		if (xySeries.getAutoSort()==true) {
			// --- Add new value ------------------------------------
			xySeries.add(newXValue, newYValue);
		} else {
			// --- Reset this series in order to keep the order -----
			this.setXYSeriesAccordingToOntologyModel(seriesIndex);
		}
	}
	
	@Override
	public void editSeriesAddData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {
		// TODO Auto-generated method stub
	}
	@Override
	public void editSeriesAddOrExchangeData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {
		// TODO Auto-generated method stub
	}
	@Override
	public void editSeriesExchangeData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {
		// TODO Auto-generated method stub
	}
	@Override
	public void editSeriesRemoveData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {
		// TODO Auto-generated method stub
	}

	
}
