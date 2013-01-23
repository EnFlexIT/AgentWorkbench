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
import org.jfree.data.xy.XYDataset;

import agentgui.core.charts.DataModel;
import agentgui.core.charts.gui.ChartTab;
import agentgui.core.charts.xyChart.XyDataModel;

public class XyChartTab extends ChartTab {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 5373349334098916334L;
	/**
	 * These renderer types can be chosen for rendering the plots. For adding more renderers,
	 * a description must be added to this array, and the corresponding constructor call must 
	 * be added to the setRenderer method.
	 */

	public XyChartTab(XyDataModel model){
		super(ChartFactory.createXYLineChart(
				model.getOntologyModel().getChartSettings().getChartTitle(), 
				model.getOntologyModel().getChartSettings().getXAxisLabel(), 
				model.getOntologyModel().getChartSettings().getYAxisLabel(), 
				(XYDataset) model.getChartModel(), 
				PlotOrientation.VERTICAL, 
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
		
		this.setChart(ChartFactory.createXYLineChart(
				model.getOntologyModel().getChartSettings().getChartTitle(), 
				model.getOntologyModel().getChartSettings().getXAxisLabel(), 
				model.getOntologyModel().getChartSettings().getYAxisLabel(), 
				(XYDataset) model.getChartModel(), 
				PlotOrientation.VERTICAL, 
				true, false, false
		));
		
		
setRenderer(DEFAULT_RENDERER);	// Use step renderer by default
		
		applyColorSettings();
		applyLineWidthsSettings();
		
	}
}
