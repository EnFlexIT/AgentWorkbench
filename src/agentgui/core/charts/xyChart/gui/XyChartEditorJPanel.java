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

import javax.swing.JToolBar;

import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.charts.xyChart.XyDataModel;
import agentgui.core.charts.xyChart.XyOntologyModel;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.ontology.XyChart;
/**
 * Implementation of OntologyClassEditorJPanel for XyCharts
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class XyChartEditorJPanel extends ChartEditorJPanel {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 7040702924414575703L;

	public XyChartEditorJPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
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
			tableTab = new XyTableTab((XyDataModel) model, this);
		}
		return (XyTableTab) tableTab;
	}
	
	protected XyChartSettingsTab getSettingsTab(){
		if(this.settingsTab == null){
			this.settingsTab = new XyChartSettingsTab(this.model.getChartSettingModel(), this);
		}
		return (XyChartSettingsTab) this.settingsTab;
	}

	@Override
	protected Number parseKey(String key, String keyFormat, Number keyOffset) {
		return Float.parseFloat(key);
	}

	@Override
	protected Number parseValue(String value) {
		return Float.parseFloat(value);
	}

	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.model = new XyDataModel((XyChart) objectInstance);
		this.getChartTab().replaceModel(this.model);
		this.getTableTab().replaceModel(this.model);
		this.getSettingsTab().replaceModel(this.model.getChartSettingModel());
	}

	@Override
	public Object getOntologyClassInstance() {
		return ((XyOntologyModel)this.model.getOntologyModel()).getXyChart();
	}

	/* (non-Javadoc)
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#getJToolBarUserFunctions()
	 */
	@Override
	public JToolBar getJToolBarUserFunctions() {
		return this.getToolBar();
	}

}
