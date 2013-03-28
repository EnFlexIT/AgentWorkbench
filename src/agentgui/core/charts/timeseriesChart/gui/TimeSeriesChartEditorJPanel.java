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

import agentgui.core.charts.ChartSettings;
import javax.swing.JToolBar;

import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.charts.timeseriesChart.TimeSeriesChartSettings;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.core.charts.timeseriesChart.TimeSeriesOntologyModel;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.ontology.TimeSeriesChart;

/**
 * Implementation of OntologyClassEditorJPanel for TimeSeriesChart
 * @author Nils
 *
 */
public class TimeSeriesChartEditorJPanel extends ChartEditorJPanel {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 6342520178418229017L;

	public TimeSeriesChartEditorJPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
	}

	@Override
	protected TimeSeriesChartTab getChartTab(){
		if(chartTab == null){
			chartTab = new TimeSeriesChartTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesChartTab) chartTab;
	}

	@Override
	protected TimeSeriesTableTab getTableTab(){
		if(tableTab == null){
			tableTab = new TimeSeriesTableTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesTableTab) tableTab;
	}
	
	protected TimeSeriesChartSettingsTab getSettingsTab(){
		if(this.settingsTab == null){
			this.settingsTab = new TimeSeriesChartSettingsTab(this.model, this);
		}
		return (TimeSeriesChartSettingsTab) settingsTab;
	}

	@Override
	protected Number parseKey(String key) {
		// TODO This implementation expects time to be in minutes, should be made configurable
		// TODO Move to the model class?
		int minutes = Integer.parseInt(key);
		Long timestamp = ((TimeSeriesDataModel)model).getStartDate().getTime() + minutes * 60000;
		return timestamp;
	}

	@Override
	protected Number parseValue(String value) {
		return Float.parseFloat(value);
	}

	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.model = new TimeSeriesDataModel((TimeSeriesChart) objectInstance, this.getDefaultTimeFormat());
		
		this.getChartTab().replaceModel(this.model);
		this.getTableTab().replaceModel(this.model);
		this.getSettingsTab().replaceModel(this.model);
		this.model.addObserver(this);
	}

	@Override
	public Object getOntologyClassInstance() {
		return ((TimeSeriesOntologyModel)this.getModel().getOntologyModel()).getTimeSeriesChart();
	}

	
	/* (non-Javadoc)
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#getJToolBarUserFunctions()
	 */
	@Override
	public JToolBar getJToolBarUserFunctions() {
		return this.getToolBar();
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#applyChartSettings(agentgui.core.charts.ChartSettings)
	 */
	@Override
	protected void applyChartSettings(ChartSettings newSettings) {
		// Apply general chart settings
		super.applyChartSettings(newSettings);
		
		// Apply time format settings
		String newTimeFormat = ((TimeSeriesChartSettings)newSettings).getTimeFormat();
		getChartTab().setTimeFormat(newTimeFormat);
		
	}
	
	


}
