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
package agentgui.core.charts.xyChart.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.gui.ChartTab;
import agentgui.core.charts.xyChart.XyChartModel;
import agentgui.core.charts.xyChart.XyDataModel;

public class XyChartTab extends ChartTab {
	
	private static final long serialVersionUID = 5373349334098916334L;

	
	/**
	 * Instantiates a new XyChartTab.
	 * @param model the model
	 */
	public XyChartTab(XyDataModel model, XyChartEditorJPanel parent){
		super(ChartFactory.createXYLineChart(
				model.getOntologyModel().getChartSettings().getChartTitle(), 
				model.getOntologyModel().getChartSettings().getXAxisLabel(), 
				model.getOntologyModel().getChartSettings().getYAxisLabel(), 
				((XyChartModel)model.getChartModel()).getXySeriesCollection(), 
				PlotOrientation.VERTICAL, 
				true, false, false
		), parent);
		
		this.dataModel = model;
		this.applySettings();
		
		this.dataModel.getChartModel().addObserver(this);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartTab#replaceModel(agentgui.core.charts.DataModel)
	 */
	@Override
	public void replaceModel(DataModel newModel) {
		this.dataModel = newModel;
		
		this.chartPanel.setChart(ChartFactory.createXYLineChart(
				dataModel.getOntologyModel().getChartSettings().getChartTitle(), 
				dataModel.getOntologyModel().getChartSettings().getXAxisLabel(), 
				dataModel.getOntologyModel().getChartSettings().getYAxisLabel(), 
				((XyChartModel)dataModel.getChartModel()).getXySeriesCollection(), 
				PlotOrientation.VERTICAL, 
				true, false, false
		));
		
		this.applySettings();
		
		this.dataModel.getChartModel().addObserver(this);
	}
	
}
