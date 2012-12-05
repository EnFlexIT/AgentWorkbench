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

import java.awt.BorderLayout;
import java.awt.Window;
import agentgui.core.charts.gui.ChartDialog;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.ontology.TimeSeriesChart;

public class TimeSeriesChartDialog extends ChartDialog{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2712116890492352158L;
	
	/**
	 * Create the dialog.
	 */
	public TimeSeriesChartDialog(Window owner, TimeSeriesChart chart) {
		
		super(owner, chart);
		this.setModal(true);
		
		setSize(600, 450);
		
		this.model = new TimeSeriesDataModel(chart);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getToolBar(), BorderLayout.NORTH);
		getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
		getContentPane().add(getButtonPane(), BorderLayout.SOUTH);
	}
	
	protected TimeSeriesChartTab getChartTab(){
		if(chartTab == null){
			chartTab = new TimeSeriesChartTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesChartTab) chartTab;
	}
	
	protected TimeSeriesTableTab getTableTab(){
		if(tableTab == null){
			tableTab = new TimeSeriesTableTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesTableTab) tableTab;
	}
	
	@Override
	protected Number parseKey(String key) {
		// TODO This implementation expects time to be in minutes, should be made configurable
		int minutes = Integer.parseInt(key);
		Long timestamp = ((TimeSeriesDataModel)model).getStartDate().getTime() + minutes * 60000;
		return timestamp;
	}

	@Override
	protected Number parseValue(String value) {
		return Float.parseFloat(value);
	}

	

}
