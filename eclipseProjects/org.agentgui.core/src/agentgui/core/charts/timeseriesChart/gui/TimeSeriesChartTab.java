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
package agentgui.core.charts.timeseriesChart.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.time.TimeSeriesCollection;

import agentgui.core.charts.DataModel;
import agentgui.core.charts.gui.ChartTab;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;

/**
 * Time series chart visualization component
 * @author Nils
 *
 */
public class TimeSeriesChartTab extends ChartTab {

	private static final long serialVersionUID = -1998969136744482400L;
	
	/**
	 * Constructor
	 * @param model The data model for this time series
	 */
	public TimeSeriesChartTab(TimeSeriesDataModel model, TimeSeriesChartEditorJPanel parent){
		
		super(ChartFactory.createTimeSeriesChart(
				model.getTimeSeriesOntologyModel().getChartSettings().getChartTitle(), 
				model.getTimeSeriesOntologyModel().getChartSettings().getXAxisLabel(), 
				model.getTimeSeriesOntologyModel().getChartSettings().getYAxisLabel(), 
				model.getTimeSeriesChartModel().getTimeSeriesCollection(), 
				true, false, false
		), parent);
		
		this.dataModel = model;
		
		applySettings();
		
		model.getChartModel().addObserver(this);
	}

	@Override
	public void replaceModel(DataModel newModel) {
		this.dataModel = newModel;
		
//		this.chartPanel.setChart(ChartFactory.createTimeSeriesChart(
//				this.dataModel.getOntologyModel().getChartSettings().getChartTitle(), 
//				this.dataModel.getOntologyModel().getChartSettings().getXAxisLabel(), 
//				this.dataModel.getOntologyModel().getChartSettings().getYAxisLabel(), 
//				((TimeSeriesDataModel)this.dataModel).getTimeSeriesChartModel().getTimeSeriesCollection(), 
//				true, false, false
//		));
//		
//		applySettings();
		
		TimeSeriesCollection tsc = ((TimeSeriesDataModel)this.dataModel).getTimeSeriesChartModel().getTimeSeriesCollection();
		this.chartPanel.getChart().getXYPlot().setDataset(tsc);
		
		dataModel.getChartModel().addObserver(this);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartTab#applySettings()
	 */
	@Override
	protected void applySettings() {
		super.applySettings();
		DateAxis da = (DateAxis) this.chartPanel.getChart().getXYPlot().getDomainAxis();
		
		DateFormat dateFormat = new SimpleDateFormat(((TimeSeriesDataModel)dataModel).getTimeFormat());
		da.setDateFormatOverride(dateFormat);
	}
	
	/**
	 * Sets the time format for the time axis label ticks
	 * @param timeFormat
	 */
	void setTimeFormat(String timeFormat){
		DateFormat dateFormat = new SimpleDateFormat(timeFormat);
		DateAxis da = (DateAxis) this.chartPanel.getChart().getXYPlot().getDomainAxis();
		da.setDateFormatOverride(dateFormat);
	}
	
}