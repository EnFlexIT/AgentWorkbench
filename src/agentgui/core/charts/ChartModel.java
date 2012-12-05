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

import agentgui.ontology.DataSeries;

public interface ChartModel {
	/**
	 * Adds or updates a value to/in a data series.
	 * @param seriesIndex The index of the series that should be changed
	 * @param key The key for the value pair to be added/updated
	 * @param value The new value for the value pair
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public void addOrUpdateValuePair(int seriesIndex, Number key, Number value) throws NoSuchSeriesException;
	public void addSeries(DataSeries series);
	/**
	 * Update the time stamp in all series that contain it
	 * @param oldKey The old time stamp
	 * @param newKey The new time stamp
	 */
	public void updateKey(Number oldKey, Number newKey);
	/**
	 * Removes the value pair with the given key from the series with the given index.
	 * @param seriesIndex The series index
	 * @param key The key
	 * @throws NoSuchSeriesException Thrown if there is no series with the specified index
	 */
	public void removeValuePair(int seriesIndex, Number key) throws NoSuchSeriesException;
	/**
	 * Removes the data series with the given index from the chart model
	 * @param seriesIndex The series index
	 */
	public void removeSeries(int seriesIndex);
	
	public abstract Series getSeries(int seriesIndex);
	
}
