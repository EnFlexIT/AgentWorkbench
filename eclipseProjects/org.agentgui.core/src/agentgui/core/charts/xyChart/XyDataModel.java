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
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.gui.ChartTab;
import agentgui.core.charts.xyChart.gui.XyChartEditorJPanel;
import agentgui.ontology.Chart;
import agentgui.ontology.DataSeries;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.ValuePair;
import agentgui.ontology.XyChart;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XyValuePair;

/**
 * Container class managing the data models for the different XY data series representations.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class XyDataModel extends DataModel {
	
	/** This label with appended seriesCount+1 will be used for newly added series */
	public static final String DEFAULT_SERIES_LABEL = "Data Series";
	/** This title will be used for the chart if none is specified */
	public static final String DEFAULT_CHART_TITLE = "XY Chart";
	/** This x axis label will be used for the chart if none is specified */
	public static final String DEFAULT_X_AXIS_LABEL = "X Value";
	/** This y axis label will be used for the chart if none is specified */
	public static final String DEFAULT_Y_AXIS_LABEL = "Y Value";
	/** Duplicate X values are allowed by default */
	public static final boolean DEFAULT_ALLOW_DUPLICATE_X_VALUES = true;
	/** By default items will be sorted by the X values */
	public static final boolean DEFAULT_AUTOSORT_BY_X = true;
	
	private XyChartEditorJPanel myEditorJPanel = null;
	
	/**
	 * Instantiates a new XyDataModel.
	 */
	public XyDataModel(XyChartEditorJPanel myEditorJPanel){
		this.myEditorJPanel = myEditorJPanel;
	}
	/**
	 * Returns the current XyChartEditorJPanel.
	 * @return the current XyChartEditorJPanel
	 */
	public XyChartEditorJPanel getXyChartEditorJPanel() {
		return this.myEditorJPanel;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#setOntologyInstanceChart(agentgui.ontology.Chart)
	 */
	@Override
	public void setOntologyInstanceChart(Chart ontologyChart) {

		XyChart xyChart = (XyChart) ontologyChart;
		
		this.ontologyModel = new XyOntologyModel(xyChart, this);
		this.chartModel = new XyChartModel(this);
		if (this.tableModel==null) {
			this.tableModel = new XyTableModel(this.myEditorJPanel.getJTabelDataSeries(), this);
		} else {
			this.tableModel.rebuildTableModel();
		}
		this.chartSettingModel = null;
		this.seriesCount=0;
		
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
					this.ontologyModel.getChartSettings().addYAxisColors(""+DEFAULT_COLORS[(seriesCount-1) % DEFAULT_COLORS.length].getRGB());
				}
				
				// If there is no line width specified for this series, use a default line width
				if(seriesCount > this.ontologyModel.getChartSettings().getYAxisLineWidth().size()){
					this.ontologyModel.getChartSettings().addYAxisLineWidth(DEFAULT_LINE_WIDTH);
				}
				this.chartModel.addSeries(nextSeries);
				this.seriesCount++;
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#createNewDataSeries(java.lang.String)
	 */
	@Override
	public XyDataSeries createNewDataSeries(String label) {
		XyDataSeries newSeries = new XyDataSeries();
		newSeries.setLabel(label);
		newSeries.setAutoSort(false);
		newSeries.setAvoidDuplicateXValues(false);
		return newSeries;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#createNewValuePair(java.lang.Number, java.lang.Number)
	 */
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
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#getKeyFromPair(agentgui.ontology.ValuePair)
	 */
	@Override
	public Number getXValueFromPair(ValuePair vp) {
		return ((XyValuePair)vp).getXValue().getFloatValue();
	}

	@Override
	public Number getYValueFromValuePair(ValuePair vp) {
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

	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesAddData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesAddData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {

		XyDataSeries addXySeries = (XyDataSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			XyDataSeries localTimeSeries = (XyDataSeries) this.ontologyModel.getSeries(targetDataSeriesIndex);
			XySeriesHelper localXySeriesHelper = new XySeriesHelper(localTimeSeries);
			localXySeriesHelper.addSeriesData(addXySeries);
		}
		// --- Rebuild chart and table -------------------- 
		((XyChartModel)this.chartModel).exchangeSeries(targetDataSeriesIndex, this.ontologyModel.getSeries(targetDataSeriesIndex));
		((XyTableModel)this.tableModel).exchangeSeries(targetDataSeriesIndex, this.ontologyModel.getSeries(targetDataSeriesIndex));
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesAddOrExchangeData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesAddOrExchangeData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		XyDataSeries addExXySeries = (XyDataSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			XyDataSeries localTimeSeries = (XyDataSeries) this.ontologyModel.getSeries(targetDataSeriesIndex);
			XySeriesHelper localXySeriesHelper = new XySeriesHelper(localTimeSeries);
			localXySeriesHelper.addOrExchangeSeriesData(addExXySeries);
		}
		// --- Rebuild chart and table -------------------- 
		((XyChartModel)this.chartModel).exchangeSeries(targetDataSeriesIndex, this.ontologyModel.getSeries(targetDataSeriesIndex));
		((XyTableModel)this.tableModel).exchangeSeries(targetDataSeriesIndex, this.ontologyModel.getSeries(targetDataSeriesIndex));
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesExchangeData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesExchangeData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {

		XyDataSeries exchangeXySeries = (XyDataSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			XyDataSeries localTimeSeries = (XyDataSeries) this.ontologyModel.getSeries(targetDataSeriesIndex);
			XySeriesHelper localXySeriesHelper = new XySeriesHelper(localTimeSeries);
			localXySeriesHelper.exchangeSeriesData(exchangeXySeries);
		}
		// --- Rebuild chart and table -------------------- 
		((XyChartModel)this.chartModel).exchangeSeries(targetDataSeriesIndex, this.ontologyModel.getSeries(targetDataSeriesIndex));
		((XyTableModel)this.tableModel).exchangeSeries(targetDataSeriesIndex, this.ontologyModel.getSeries(targetDataSeriesIndex));
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.DataModel#editDataSeriesRemoveData(agentgui.ontology.DataSeries, int, boolean)
	 */
	@Override
	public void editDataSeriesRemoveData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException {
		
		XyDataSeries removeXySeries = (XyDataSeries) series;
		// --- Edit the ontology model first --------------
		if (editOntology==true) {
			XyDataSeries localTimeSeries = (XyDataSeries) this.ontologyModel.getSeries(targetDataSeriesIndex);
			XySeriesHelper localXySeriesHelper = new XySeriesHelper(localTimeSeries);
			localXySeriesHelper.removeSeriesData(removeXySeries);
		}
		// --- Rebuild chart and table -------------------- 
		((XyChartModel)this.chartModel).exchangeSeries(targetDataSeriesIndex, this.ontologyModel.getSeries(targetDataSeriesIndex));
		((XyTableModel)this.tableModel).exchangeSeries(targetDataSeriesIndex, this.ontologyModel.getSeries(targetDataSeriesIndex));
	}
	@Override
	public String getBaseStringForSeriesLabel() {
		return DEFAULT_SERIES_LABEL;
	}

}
