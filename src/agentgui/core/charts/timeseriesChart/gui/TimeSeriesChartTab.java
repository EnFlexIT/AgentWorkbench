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

import org.jfree.chart.ChartFactory;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.gui.ChartTab;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;

/**
 * Time series chart visualization component
 * @author Nils
 *
 */
public class TimeSeriesChartTab extends ChartTab {
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -1998969136744482400L;
	
	/**
	 * Constructor
	 * @param model The data model for this time series
	 */
	public TimeSeriesChartTab(TimeSeriesDataModel model){
		super(ChartFactory.createTimeSeriesChart(
				model.getTimeSeriesOntologyModel().getChartSettings().getChartTitle(), 
				model.getTimeSeriesOntologyModel().getChartSettings().getXAxisLabel(), 
				model.getTimeSeriesOntologyModel().getChartSettings().getYAxisLabel(), 
				model.getTimeSeriesChartModel(), 
				true, false, false
		));
		
		this.model = model;
		
		setRenderer(DEFAULT_RENDERER);	// Use step renderer by default
		
		applyColorSettings();
		applyLineWidthsSettings();
	}

	@Override
	public void replaceModel(DataModel newModel) {
		this.model = newModel;
		
		this.setChart(ChartFactory.createTimeSeriesChart(
				this.model.getOntologyModel().getChartSettings().getChartTitle(), 
				this.model.getOntologyModel().getChartSettings().getXAxisLabel(), 
				this.model.getOntologyModel().getChartSettings().getYAxisLabel(), 
				((TimeSeriesDataModel)this.model).getTimeSeriesChartModel(), 
				true, false, false
		));
		
		setRenderer(DEFAULT_RENDERER);	// Use step renderer by default
		
		applyColorSettings();
		applyLineWidthsSettings();
	}
}
