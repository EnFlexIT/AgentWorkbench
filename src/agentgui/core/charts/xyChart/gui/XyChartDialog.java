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

import java.awt.BorderLayout;
import java.awt.Window;

import agentgui.core.charts.gui.ChartDialog;
import agentgui.core.charts.xyChart.XyDataModel;
import agentgui.ontology.XyChart;

public class XyChartDialog extends ChartDialog {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -4780446063163497998L;

	public XyChartDialog(Window owner, XyChart chart) {
		super(owner, chart);
		this.setModal(true);
		
		setSize(600, 450);
		
		this.model = new XyDataModel(chart);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getToolBar(), BorderLayout.NORTH);
		getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
		getContentPane().add(getButtonPane(), BorderLayout.SOUTH);
	}

	@Override
	protected XyChartTab getChartTab() {
		if(chartTab == null){
			chartTab = new XyChartTab((XyDataModel) model);
		}
		return (XyChartTab) chartTab;
	}

	@Override
	protected XyTableTab getTableTab() {
		if(tableTab == null){
			tableTab = new XyTableTab((XyDataModel) model);
		}
		return (XyTableTab) tableTab;
	}

	@Override
	protected Number parseKey(String key) {
		return Float.parseFloat(key);
	}

	@Override
	protected Number parseValue(String value) {
		return Float.parseFloat(value);
	}

}
