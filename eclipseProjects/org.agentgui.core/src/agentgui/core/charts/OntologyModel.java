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

import jade.util.leap.List;
import agentgui.ontology.Chart;
import agentgui.ontology.ChartSettingsGeneral;
import agentgui.ontology.DataSeries;

/**
 * The Class OntologyModel manages the translation between the ontology classes 
 * {@link Chart} and {@link DataModel} and the actual classes for the chart
 * representation.
 */
public abstract class OntologyModel {
	
	
	/**
	 * Gets the number of current data series.
	 * @return the series count
	 */
	public abstract int getSeriesCount();
	
	/**
	 * Gets the list of value pairs for the data series with the specified index.
	 * @param seriesIndex The series index
	 * @return The list of value pairs
	 * @throws NoSuchSeriesException Thrown if there is no series with the specified index
	 */
	public abstract List getSeriesData(int seriesIndex) throws NoSuchSeriesException;
	
	/**
	 * Adds a data series.
	 * @param series the series
	 */
	public abstract void addSeries(DataSeries series);

	/**
	 * Gets the data series.
	 *
	 * @param seriesIndex the series index
	 * @return the series
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public abstract DataSeries getSeries(int seriesIndex) throws NoSuchSeriesException;
	
	/**
	 * Exchange data series.
	 *
	 * @param seriesIndex the series index
	 * @param dataSeries the data series
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public abstract void exchangeSeries(int seriesIndex, DataSeries dataSeries) throws NoSuchSeriesException;
	
	/**
	 * Removes a data series.
	 *
	 * @param seriesIndex the series index
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public abstract void removeSeries(int seriesIndex) throws NoSuchSeriesException;
	
	/**
	 * Gets the chart data.
	 * @return the chart data
	 */
	public abstract List getChartData();

	/**
	 * Gets the chart settings for this chart.
	 * @return the general chart settings
	 */
	public abstract ChartSettingsGeneral getChartSettings();
	
	
}
