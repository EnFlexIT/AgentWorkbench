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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * 
 * @author Nils
 */
public class TimeSeriesChartEditorJPanel extends ChartEditorJPanel {

	private static final long serialVersionUID = 6342520178418229017L;


	/**
	 * Instantiates a new time series chart editor j panel.
	 *
	 * @param dynForm the dyn form
	 * @param startArgIndex the start arg index
	 */
	public TimeSeriesChartEditorJPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#getChartTab()
	 */
	@Override
	protected TimeSeriesChartTab getChartTab(){
		if(chartTab == null){
			chartTab = new TimeSeriesChartTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesChartTab) chartTab;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#getTableTab()
	 */
	@Override
	protected TimeSeriesTableTab getTableTab(){
		if(tableTab == null){
			tableTab = new TimeSeriesTableTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesTableTab) tableTab;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#getSettingsTab()
	 */
	@Override
	protected TimeSeriesChartSettingsTab getSettingsTab(){
		if(this.settingsTab == null){
			this.settingsTab = new TimeSeriesChartSettingsTab(this.model, this);
		}
		return (TimeSeriesChartSettingsTab) settingsTab;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#parseKey(java.lang.String, java.lang.String, java.lang.Number)
	 */
	@Override
	protected Number parseKey(String key, String keyFormat, Number keyOffset) {

		Long timestamp = (long) 0; 
		try {
			DateFormat df = new SimpleDateFormat(keyFormat);
			Date dateParsed =  df.parse(key);
			timestamp = dateParsed.getTime();
			if (keyOffset!=null) {
				timestamp = timestamp + (Long) keyOffset;	
			}
			// System.out.println( "=> " + new Date(timestamp) );
			
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return timestamp;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#parseValue(java.lang.String)
	 */
	@Override
	protected Number parseValue(String value) {
		return Float.parseFloat(value);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#setOntologyClassInstance(java.lang.Object)
	 */
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
		((TimeSeriesOntologyModel)model.getOntologyModel()).getAdditionalSettings().setTimeFormat(newTimeFormat);
		
	}

}
