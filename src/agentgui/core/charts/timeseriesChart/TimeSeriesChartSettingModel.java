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
package agentgui.core.charts.timeseriesChart;

import agentgui.core.charts.ChartSettingModel;
import agentgui.core.charts.DataModel;
import agentgui.ontology.TimeSeriesChartSettings;

/**
 * The Class TimeSeriesChartSettingModel.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class TimeSeriesChartSettingModel extends ChartSettingModel {

	private String timeFormat;
	
	/**
	 * Instantiates a new time series chart setting model.
	 * @param parentDataModel the parent data model
	 */
	public TimeSeriesChartSettingModel(DataModel parentDataModel) {
		super(parentDataModel);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartSettingModel#setFromOntology()
	 */
	@Override
	public void refresh() {
		TimeSeriesOntologyModel tsom = ((TimeSeriesOntologyModel)this.parentDataModel.getOntologyModel());
		TimeSeriesChartSettings tscs = (TimeSeriesChartSettings) tsom.getChartSettings();
		this.timeFormat = tscs.getTimeFormat();
		super.refresh();
	}

	/**
	 * Gets the time format.
	 * @return the time format
	 */
	public String getTimeFormat() {
		return timeFormat;
	}
	/**
	 * Sets the time format.
	 * @param newTimeFormat the new time format
	 */
	public void setTimeFormat(String newTimeFormat) {
		this.timeFormat = newTimeFormat;
		TimeSeriesOntologyModel tsom = ((TimeSeriesOntologyModel)this.parentDataModel.getOntologyModel());
		TimeSeriesChartSettings tscs = (TimeSeriesChartSettings) tsom.getChartSettings();
		tscs.setTimeFormat(newTimeFormat);
	}
	
	
}
