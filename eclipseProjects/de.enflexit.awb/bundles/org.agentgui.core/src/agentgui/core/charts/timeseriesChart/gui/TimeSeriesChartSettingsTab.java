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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import agentgui.core.application.Language;
import agentgui.core.charts.ChartSettingModel;
import agentgui.core.charts.gui.ChartSettingsTab;
import agentgui.core.charts.timeseriesChart.TimeSeriesChartSettingModel;
import agentgui.simulationService.time.TimeFormatSelection;

/**
 * ChartSettingsTab-implementation for time series charts, adding the possibility 
 * to set the time format.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class TimeSeriesChartSettingsTab extends ChartSettingsTab {
	
	private static final long serialVersionUID = 675579393370531354L;
	
	private JLabel lblTimeFormatSelector = null;
	private TimeFormatSelection timeFormatSelector = null;
	
	private boolean pauseLocalActionListener = false;
	
	
	/**
	 * Instantiates a new time series chart settings tab.
	 * @param chartSettingModel the chart setting model
	 * @param parent the parent
	 */
	public TimeSeriesChartSettingsTab(ChartSettingModel chartSettingModel, TimeSeriesChartEditorJPanel parent) {
		super(chartSettingModel, parent);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartSettingsTab#setChartSettingModelData()
	 */
	@Override
	public void setChartSettingModelData() {
		String newTimeFormat = ((TimeSeriesChartSettingModel)this.chartSettingModel).getTimeFormat();
		this.pauseLocalActionListener = true;
		this.getTimeFormatSelector().setTimeFormat(newTimeFormat);
		this.pauseLocalActionListener = false;
		super.setChartSettingModelData();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartSettingsTab#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		// --- Insert time format setter above series settings table 
		
		// --- Move table
		GridBagLayout layoutManager = (GridBagLayout) getLayout();
		GridBagConstraints gbcTable = layoutManager.getConstraints(getSpTblSeriesSettings());
		int oldY = gbcTable.gridy;
		gbcTable.gridy = oldY + 1;
		layoutManager.setConstraints(getSpTblSeriesSettings(), gbcTable);
		
		// --- Add label
		GridBagConstraints gbcLblTimeFormatSelector = new GridBagConstraints();
		gbcLblTimeFormatSelector.gridx = 0;
		gbcLblTimeFormatSelector.gridy = oldY;
		gbcLblTimeFormatSelector.anchor = GridBagConstraints.NORTHWEST;
		gbcLblTimeFormatSelector.insets = new Insets(8, 5, 5, 5);
		this.add(getLblTimeFormatSelector(), gbcLblTimeFormatSelector);
		
		// --- Add format selector component
		GridBagConstraints gbcTimeFormatSelector = new GridBagConstraints();
		gbcTimeFormatSelector.gridx = 1;
		gbcTimeFormatSelector.gridy = oldY;
		gbcTimeFormatSelector.anchor = GridBagConstraints.WEST;
		gbcTimeFormatSelector.fill = GridBagConstraints.HORIZONTAL;
		gbcTimeFormatSelector.insets = new Insets(3, 0, 0, 0);
		this.add(getTimeFormatSelector(), gbcTimeFormatSelector);
		
	}

	/**
	 * Gets the lbl time format selector.
	 * @return the lblTimeFormatSelector
	 */
	public JLabel getLblTimeFormatSelector() {
		if(lblTimeFormatSelector == null){
			lblTimeFormatSelector = new JLabel(Language.translate("Zeit-Format"));
		}
		return lblTimeFormatSelector;
	}
	/**
	 * Gets the default time format.
	 * @return The default time format
	 */
	public String getDefaultTimeFormat(){
		return parent.getDefaultTimeFormat();
	}
	/**
	 * @return the jPanelTimeFormater
	 */
	public TimeFormatSelection getTimeFormatSelector() {
		if (timeFormatSelector==null) {
			timeFormatSelector = new TimeFormatSelection(false);
			timeFormatSelector.setDefaultTimeFormat(this.getDefaultTimeFormat());
			timeFormatSelector.setPreferredSize(new Dimension(360, 80));
			timeFormatSelector.setTimeFormat(((TimeSeriesChartSettingModel)chartSettingModel).getTimeFormat());
			timeFormatSelector.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					if (pauseLocalActionListener==false) {
						TimeFormatSelection tfs = (TimeFormatSelection) ae.getSource();
						setTimeFormat(tfs.getTimeFormat());	
					}
				}
			});
		}
		return timeFormatSelector;
	}
	/**
	 * Sets the time format for the chart
	 * @param newTimeFormat The new time format
	 */
	private void setTimeFormat(String newTimeFormat){
 		((TimeSeriesChartSettingModel)chartSettingModel).setTimeFormat(newTimeFormat);
		((TimeSeriesChartEditorJPanel)parent).getChartTab().setTimeFormat(newTimeFormat);
	}


}
