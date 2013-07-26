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
package agentgui.envModel.graph.visualisation.notifications;

import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.ontology.TimeSeries;

/**
 * The Class UpdateTimeSeries.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class UpdateTimeSeries extends UpdateDataSeries {

	private static final long serialVersionUID = 8563478170121892593L;

	private TimeSeries specifiedTimeSeries = null;
	
	/**
	 * Instantiates a new update time series.
	 *
	 * @param networkComponent the network component
	 * @param targetDataModelIndex the target data model index
	 */
	public UpdateTimeSeries(NetworkComponent networkComponent, int targetDataModelIndex) {
		super(networkComponent, targetDataModelIndex);
	}
	
	/**
	 * Instantiates a new update time series.
	 *
	 * @param graphNode the graph node
	 * @param targetDataModelIndex the target data model index
	 */
	public UpdateTimeSeries(GraphNode graphNode, int targetDataModelIndex) {
		super(graphNode, targetDataModelIndex);
	}
	
	/**
	 * Adds a time series.
	 * @param newTimeSeries the new time series
	 */
	public void addTimeSeries(TimeSeries newTimeSeries) {
		this.setTargetAction(UPDATE_ACTION.AddDataSeries);
		this.setSpecifiedTimeSeries(newTimeSeries);
	}
	/**
	 * Exchanges a Time Series at a specified data series index.
	 * @param newTimeSeries the new time series
	 * @param dataSeriesIndex the data series index
	 */
	public void addOrExchangeTimeSeries(TimeSeries newTimeSeries, int dataSeriesIndex) {
		this.setTargetAction(UPDATE_ACTION.AddOrExchangeDataSeries);
		this.setSpecifiedTimeSeries(newTimeSeries);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
	}
	/**
	 * Removes the time series.
	 * @param dataSeriesIndex the data series index
	 */
	public void removeTimeSeries(int dataSeriesIndex) {
		this.setTargetAction(UPDATE_ACTION.RemoveDataSeries);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
	}

	/**
	 * Sets the specified time series.
	 * @param specifiedTimeSeries the new specified time series
	 */
	public void setSpecifiedTimeSeries(TimeSeries specifiedTimeSeries) {
		this.specifiedTimeSeries = specifiedTimeSeries;
	}
	/**
	 * Gets the specified time series.
	 * @return the specified time series
	 */
	public TimeSeries getSpecifiedTimeSeries() {
		return specifiedTimeSeries;
	}
	
	
	/**
	 * Edits the time series.
	 *
	 * @param dataSeriesIndex the data series index
	 * @param timeSeriesData2Add the time series data2 add
	 */
	public void editTimeSeriesAddTimeSeriesData(int dataSeriesIndex, TimeSeries timeSeriesData2Add) {
		this.setTargetAction(UPDATE_ACTION.EditDataSeriesAddData);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
		this.setSpecifiedTimeSeries(timeSeriesData2Add);
	}
	
	/**
	 * Edits the time series add or exchange time series data.
	 *
	 * @param dataSeriesIndex the data series index
	 * @param timeSeriesData2AddOrExchange the time series data2 add or exchange
	 */
	public void editTimeSeriesAddOrExchangeTimeSeriesData(int dataSeriesIndex, TimeSeries timeSeriesData2AddOrExchange) {
		this.setTargetAction(UPDATE_ACTION.EditDataSeriesAddOrExchangeData);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
		this.setSpecifiedTimeSeries(timeSeriesData2AddOrExchange);
	}
	
	/**
	 * Edits the time series remove time series data.
	 *
	 * @param dataSeriesIndex the data series index
	 * @param timeSeriesData2Remove the time series data2 remove
	 */
	public void editTimeSeriesRemoveTimeSeriesData(int dataSeriesIndex, TimeSeries timeSeriesData2Remove) {
		this.setTargetAction(UPDATE_ACTION.EditDataSeriesRemoveData);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
		this.setSpecifiedTimeSeries(timeSeriesData2Remove);
	}
	
}
