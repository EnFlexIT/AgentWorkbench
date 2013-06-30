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

import jade.util.leap.Iterator;
import jade.util.leap.List;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.gui.ChartTab;
import agentgui.ontology.DataSeries;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.ValuePair;
import agentgui.ontology.XyChart;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XyValuePair;

public class XyDataModel extends DataModel {
	
	/**
	 * This label with appended seriesCount+1 will be used for newly added series
	 */
	public static final String DEFAULT_SERIES_LABEL = "Data Series";
	/**
	 * This title will be used for the chart if none is specified
	 */
	public static final String DEFAULT_CHART_TITLE = "XY Chart";
	/**
	 * This x axis label will be used for the chart if none is specified
	 */
	public static final String DEFAULT_X_AXIS_LABEL = "X Value";
	/**
	 * This y axis label will be used for the chart if none is specified
	 */
	public static final String DEFAULT_Y_AXIS_LABEL = "Y Value";
	
	public XyDataModel(XyChart chart){
		this.ontologyModel = new XyOntologyModel(chart, this);
		chartModel = new XyChartModel();
		tableModel = new XyTableModel(this);
		
		XyOntologyModel xyom = (XyOntologyModel) this.ontologyModel;
		
		if(xyom.getXyChart().isEmpty()){
			
			// The ontology model is empty. Do some initialization work.
			
			// Remove the empty elements created by DynForm from the lists 
			xyom.getXyChart().getXyChartData().clear();
			this.ontologyModel.getChartSettings().getYAxisColors().clear();
			this.ontologyModel.getChartSettings().getYAxisLineWidth().clear();
			
			// Set some default values
			this.ontologyModel.getChartSettings().setChartTitle(DEFAULT_CHART_TITLE);
			this.ontologyModel.getChartSettings().setXAxisLabel(DEFAULT_X_AXIS_LABEL);
			this.ontologyModel.getChartSettings().setYAxisLabel(DEFAULT_Y_AXIS_LABEL);
			this.ontologyModel.getChartSettings().setRendererType(ChartTab.DEFAULT_RENDERER);
		
		}else{
			
			// The ontology model contains data, add it to the other sub models 
			Iterator dataSeries = xyom.getXyChart().getAllXyChartData();
			while(dataSeries.hasNext()){
				
				XyDataSeries nextSeries = (XyDataSeries) dataSeries.next();
				
				// If there is no color specified for this series, use a default color
				if(seriesCount > this.ontologyModel.getChartSettings().getYAxisColors().size()){
					this.ontologyModel.getChartSettings().addYAxisColors(""+DEFAULT_COLORS[seriesCount % DEFAULT_COLORS.length].getRGB());
				}
				
				// If there is no line width specified for this series, use a default line width
				if(seriesCount > this.ontologyModel.getChartSettings().getYAxisLineWidth().size()){
					this.ontologyModel.getChartSettings().addYAxisLineWidth(DEFAULT_LINE_WIDTH);
				}
				chartModel.addSeries(nextSeries);
				tableModel.addSeries(nextSeries);
				seriesCount++;
			}
		}
		
		// Register for table model events
		tableModel.addTableModelListener(this);
		
		this.chartSettings = new XyChartSettings(xyom.getXyChart());
	}

	@Override
	public ValuePair createNewValuePair(Number key, Number value) {
		XyValuePair valuePair = new XyValuePair();
		Simple_Float xValue = new Simple_Float();
		xValue.setFloatValue(key.floatValue());
		Simple_Float yValue = new Simple_Float();
		yValue.setFloatValue(value.floatValue());
		valuePair.setXValue(xValue);
		valuePair.setYValue(yValue);
		return valuePair;
	}

	@Override
	public Number getKeyFromPair(ValuePair vp) {
		return ((XyValuePair)vp).getXValue().getFloatValue();
	}

	@Override
	public Number getValueFromPair(ValuePair vp) {
		return ((XyValuePair)vp).getYValue().getFloatValue();
	}

	@Override
	public void setKeyForPair(Number key, ValuePair vp) {
		((XyValuePair)vp).getXValue().setFloatValue(key.floatValue());
	}

	@Override
	public void setValueForPair(Number value, ValuePair vp) {
		((XyValuePair)vp).getYValue().setFloatValue(value.floatValue());
	}

	@Override
	public List getValuePairsFromSeries(DataSeries series) {
		return ((XyDataSeries)series).getXyValuePairs();
	}

	@Override
	public String getDefaultSeriesLabel() {
		return DEFAULT_SERIES_LABEL+" "+(getSeriesCount()+1);
	}

	@Override
	public XyDataSeries createNewDataSeries(String label) {
		XyDataSeries newSeries = new XyDataSeries();
		newSeries.setLabel(label);
		return newSeries;
	}

}
