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

import javax.swing.JTable;
import javax.swing.JToolBar;

import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.charts.xyChart.XyDataModel;
import agentgui.core.charts.xyChart.XyOntologyModel;
import agentgui.ontology.XyChart;
import de.enflexit.common.ontology.gui.DynForm;

/**
 * Implementation of OntologyClassEditorJPanel for XyCharts
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class XyChartEditorJPanel extends ChartEditorJPanel {

	private static final long serialVersionUID = 7040702924414575703L;

	/** The local data model. */
	protected XyDataModel dataModel;
	
	
	/**
	 * Instantiates a new XyChartEditorJPanel.
	 *
	 * @param dynForm the current DynForm
	 * @param startArgIndex the current start argument index
	 */
	public XyChartEditorJPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#getDataModel()
	 */
	@Override
	public XyDataModel getDataModel() {
		if (this.dataModel==null) {
			this.dataModel = new XyDataModel(this);
		}
		return this.dataModel;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.ontology.gui.OntologyClassEditorJPanel#setOntologyClassInstance(java.lang.Object)
	 */
	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.getDataModel().setOntologyInstanceChart((XyChart) objectInstance);
		
		this.getChartTab().replaceModel(this.getDataModel());
		this.getChartSettingsTab().replaceModel(this.getDataModel().getChartSettingModel());
		
		this.getDataModel().getChartSettingModel().addObserver(this.getChartTab());
		
		this.getTableTab().setButtonsEnabledToSituation();
		
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.ontology.gui.OntologyClassEditorJPanel#getOntologyClassInstance()
	 */
	@Override
	public Object getOntologyClassInstance() {
		return ((XyOntologyModel)this.getDataModel().getOntologyModel()).getXyChart();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#getChartTab()
	 */
	@Override
	protected XyChartTab getChartTab() {
		if(chartTab == null){
			chartTab = new XyChartTab((XyDataModel) this.getDataModel(), this);
		}
		return (XyChartTab) chartTab;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#getTableTab()
	 */
	@Override
	protected XyTableTab getTableTab() {
		if(tableTab == null){
			tableTab = new XyTableTab(this.getDataModel(), this);
		}
		return (XyTableTab) tableTab;
	}
	/**
	 * Returns the current JTable for the data series.
	 * @return the JTable for the data series
	 */
	public JTable getJTabelDataSeries() {
		return this.getTableTab().getTable();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#getSettingsTab()
	 */
	protected XyChartSettingsTab getChartSettingsTab(){
		if(this.settingsTab == null){
			this.settingsTab = new XyChartSettingsTab(this.getDataModel().getChartSettingModel(), this);
		}
		return (XyChartSettingsTab) this.settingsTab;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#parseKey(java.lang.String, java.lang.String, java.lang.Number)
	 */
	@Override
	protected Number parseKey(String key, String keyFormat, Number keyOffset) {
		return Float.parseFloat(key);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartEditorJPanel#parseValue(java.lang.String)
	 */
	@Override
	protected Number parseValue(String value) {
		return Float.parseFloat(value);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.ontology.gui.OntologyClassEditorJPanel#getJToolBarUserFunctions()
	 */
	@Override
	public JToolBar getJToolBarUserFunctions() {
		return this.getToolBar();
	}


}
