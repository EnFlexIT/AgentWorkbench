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
package agentgui.core.charts;

import org.jfree.data.general.Series;

import agentgui.ontology.ChartSettingsGeneral;
import agentgui.ontology.DataSeries;

public interface ChartModel {
	
	
	/**
	 * Returns the chart settings.
	 * @return the chart settings
	 */
	public ChartSettingsGeneral getChartSettings();
	
	/**
	 * Returns the jfreeChart series specified by the index position.
	 * @param seriesIndex the series index
	 * @return the series
	 */
	public abstract Series getSeries(int seriesIndex);
	
	/**
	 * Adds the series.
	 * @param series the series
	 */
	public void addSeries(DataSeries series);
	/**
	 * Exchanges the series specified by the series index with the given DataSeries.
	 * @param seriesIndex the series index
	 * @param series the DataSeries
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public void exchangeSeries(int seriesIndex, DataSeries series) throws NoSuchSeriesException;
	/**
	 * Removes the data series with the given index from the chart model
	 * @param seriesIndex The series index
	 */
	public void removeSeries(int seriesIndex);
	
	
}
