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

import jade.util.leap.List;

import java.util.Vector;

import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.core.charts.timeseriesChart.TimeSeriesHelper;
import agentgui.core.charts.timeseriesChart.gui.TimeSeriesChartEditorJPanel;
import agentgui.core.charts.timeseriesChart.gui.TimeSeriesWidget;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.visualisation.DisplayAgent;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.Simple_Long;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesValuePair;
import agentgui.simulationService.transaction.DisplayAgentNotification;
import de.enflexit.common.ontology.gui.OntologyClassEditorJPanel;
import de.enflexit.common.ontology.gui.OntologyInstanceViewer;

/**
 * The Class UpdateTimeSeries can be used in order to send 
 * {@link DisplayAgentNotification}'s to the {@link DisplayAgent}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class UpdateTimeSeries extends UpdateDataSeries {

	private static final long serialVersionUID = 8563478170121892593L;

	private TimeSeries specifiedTimeSeries = null;
	
	private long timeStamp;
	private Vector<Float> valueVector;
	
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
	 * Adds or exchanges a Time Series at a specified data series index.
	 * @param newTimeSeries the new time series
	 * @param dataSeriesIndex the data series index
	 */
	public void addOrExchangeTimeSeries(TimeSeries newTimeSeries, int dataSeriesIndex) {
		this.setTargetAction(UPDATE_ACTION.AddOrExchangeDataSeries);
		this.setSpecifiedTimeSeries(newTimeSeries);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
	}
	/**
	 * Exchange exchanges a Time Series at a specified data series index.
	 * If the specified index can not be found, nothing will be done.
	 * @param newTimeSeries the new time series
	 * @param dataSeriesIndex the data series index
	 */
	public void exchangeTimeSeries(TimeSeries newTimeSeries, int dataSeriesIndex) {
		this.setTargetAction(UPDATE_ACTION.ExchangeDataSeries);
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
	 * Edits the TimeSeriesChartby adding or exchanging a time dependent data row.
	 *
	 * @param timeStamp the time stamp
	 * @param valueVector the value vector
	 */
	public void editTimeSeriesChartAddOrExchangeDataRow(long timeStamp, Vector<Float> valueVector) {
		this.setTargetAction(UPDATE_ACTION.TimeSeriesChartAddOrExchangeDataRow);
		this.setTimeStamp(timeStamp);
		this.setValueVector(valueVector);
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
	 * Sets the time stamp.
	 * @param timeStamp the new time stamp
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * Gets the time stamp.
	 * @return the time stamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Sets the value vector.
	 * @param valueVector the new value vector
	 */
	public void setValueVector(Vector<Float> valueVector) {
		this.valueVector = valueVector;
	}
	/**
	 * Gets the value vector.
	 * @return the value vector
	 */
	public Vector<Float> getValueVector() {
		return valueVector;
	}
	
	/**
	 * Edits the time series: adds new data to a time series.
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
	 * Edits the time series: adds or exchanges time series data.
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
	 * Edits the time series: exchanges time series data. If the specified timestamps 
	 * can not be found, nothing will be done.
	 *
	 * @param dataSeriesIndex the data series index
	 * @param timeSeriesData2AddOrExchange the time series data2 add or exchange
	 */
	public void editTimeSeriesExchangeTimeSeriesData(int dataSeriesIndex, TimeSeries timeSeriesData2AddOrExchange) {
		this.setTargetAction(UPDATE_ACTION.EditDataSeriesExchangeData);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
		this.setSpecifiedTimeSeries(timeSeriesData2AddOrExchange);
	}
	
	/**
	 * Edits the time series: removes time series data specified by the timestamps.
	 *
	 * @param dataSeriesIndex the data series index
	 * @param timeSeriesData2Remove the time series data2 remove
	 */
	public void editTimeSeriesRemoveTimeSeriesData(int dataSeriesIndex, TimeSeries timeSeriesData2Remove) {
		this.setTargetAction(UPDATE_ACTION.EditDataSeriesRemoveData);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
		this.setSpecifiedTimeSeries(timeSeriesData2Remove);
	}

	

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.visualisation.notifications.UpdateDataSeries#applyToNetworkModelOnly(agentgui.envModel.graph.networkModel.NetworkModel)
	 */
	@Override
	public void applyToNetworkModelOnly(NetworkModel networkModel) throws UpdateDataSeriesException {

		Object dataModel = null;
		switch (this.getComponentType()) {
		case GraphNode:
			GraphNode node =(GraphNode)networkModel.getGraphElement(this.getComponentID());
			if (node==null) {
				throw new UpdateDataSeriesException("GraphNode '" + this.getComponentID() + "' could not be found in the NetworkModel!");
			}
			dataModel = node.getDataModel();
			if (dataModel==null) {
				NetworkComponentAdapter nca = networkModel.getNetworkComponentAdapter(null, node);
				NetworkComponentAdapter4Ontology nca4o = (NetworkComponentAdapter4Ontology) nca.getStoredDataModelAdapter();
				nca4o.setDataModel(dataModel);
				dataModel = nca4o.getDataModel();
				node.setDataModel(dataModel);
			}
			break;
			
		case NetworkComponent:
			NetworkComponent networkComponent = networkModel.getNetworkComponent(this.getComponentID());
			if (networkComponent==null) {
				throw new UpdateDataSeriesException("NetworkComponent '" + this.getComponentID() + "' could not be found in the NetworkModel!");
			}
			dataModel = networkComponent.getDataModel();
			if (dataModel==null) {
				NetworkComponentAdapter nca = networkModel.getNetworkComponentAdapter(null, networkComponent);
				NetworkComponentAdapter4Ontology nca4o = (NetworkComponentAdapter4Ontology) nca.getStoredDataModelAdapter();
				nca4o.setDataModel(dataModel);
				dataModel = nca4o.getDataModel();
				networkComponent.setDataModel(dataModel);
			}
			break;
		} 

		if (dataModel==null) {
			throw new UpdateDataSeriesException("NullPointerException: The data model of " + this.getComponentTypeName() + " '" + this.getComponentID() + "' is null!");
		}
		
		// --- Get the right data model part -------------------- 
		try {
			Object[] objectArr = (Object[]) dataModel; 
			TimeSeriesChart tsc = (TimeSeriesChart) objectArr[this.getTargetDataModelIndex()];
			this.applyUpdate(tsc);
			
		} catch (Exception ex) {
			throw new UpdateDataSeriesException(this.getTargetDataModelIndex(), this.getComponentTypeName(), this.getComponentID(), ex);
		}
		
	}
	
	/**
	 * Apply the update configured in this class.
	 * @param timeSeriesChart the actual time series chart
	 */
	private void applyUpdate(TimeSeriesChart timeSeriesChart) {
		
		switch (this.getTargetAction()) {
		case AddDataSeries:
			timeSeriesChart.getTimeSeriesChartData().add(this.getSpecifiedTimeSeries());
			break;
			
		case AddOrExchangeDataSeries:
			if (this.getTargetDataSeriesIndex()<=timeSeriesChart.getTimeSeriesChartData().size()-1) {
				timeSeriesChart.getTimeSeriesChartData().remove(this.getTargetDataSeriesIndex());	
				timeSeriesChart.getTimeSeriesChartData().add(this.getTargetDataSeriesIndex(), this.getSpecifiedTimeSeries());
			} else {
				timeSeriesChart.getTimeSeriesChartData().add(this.getSpecifiedTimeSeries());
			}
			break;

		case ExchangeDataSeries:
			if (this.getTargetDataSeriesIndex()<=timeSeriesChart.getTimeSeriesChartData().size()-1) {
				timeSeriesChart.getTimeSeriesChartData().remove(this.getTargetDataSeriesIndex());
				timeSeriesChart.getTimeSeriesChartData().add(this.getTargetDataSeriesIndex(), this.getSpecifiedTimeSeries());
			} 
			break;
			
		case RemoveDataSeries:
			if (this.getTargetDataSeriesIndex()<=timeSeriesChart.getTimeSeriesChartData().size()-1) {
				timeSeriesChart.getTimeSeriesChartData().remove(this.getTargetDataSeriesIndex());	
			}
			break;

		// ------------------------------------------------	
		// --- From here: Edits for the TimeSeriesChart ---
		// ------------------------------------------------
		case TimeSeriesChartAddOrExchangeDataRow:

			for (int i = 0; i < this.getValueVector().size(); i++) {
				// -- Get the new value for the Time Series i -------
				Float newFloatValue = this.getValueVector().get(i);
				if (newFloatValue==null) continue;
				
				// --- Create a new value pair ----------------------
				Simple_Long sl = new Simple_Long();
				sl.setLongValue(this.getTimeStamp());
				
				Simple_Float sf = new Simple_Float();
				sf.setFloatValue(newFloatValue);
				
				TimeSeriesValuePair tsvp = new TimeSeriesValuePair();
				tsvp.setTimestamp(sl);
				tsvp.setValue(sf);
				
				// --- Add new value pair to data model -------------
				if (i>=0 && i<=timeSeriesChart.getTimeSeriesChartData().size()-1) {
					TimeSeries ts = (TimeSeries) timeSeriesChart.getTimeSeriesChartData().get(i);
					ts.addTimeSeriesValuePairs(tsvp);
				}
			}
			break;
			
		// ------------------------------------------------	
		// --- From here: Edits of single TimeSeries ------
		// ------------------------------------------------
		case EditDataSeriesAddData:
		case EditDataSeriesAddOrExchangeData:
		case EditDataSeriesExchangeData:
		case EditDataSeriesRemoveData:			
			// -- Get the time series to work on ---------- 
			TimeSeries timeSeries = (TimeSeries) timeSeriesChart.getTimeSeriesChartData().get(this.getTargetDataSeriesIndex());
			List oldValuePairs = timeSeries.getTimeSeriesValuePairs();
			Integer editInstance = System.identityHashCode(oldValuePairs);
			// --- Was the instance already edited? -------
			if (this.getEditedInstances().contains(editInstance)==false) {
				// --- Get the list of new value pairs ----
				TimeSeriesHelper timeSeriesHelper = new TimeSeriesHelper(timeSeries);
				// --- Again, case separation -------------
				switch (this.getTargetAction()) {
				case EditDataSeriesAddData:
					// --- Add data to series -------------
					timeSeriesHelper.addSeriesData(this.getSpecifiedTimeSeries());
					this.getEditedInstances().add(editInstance);
					break;

				case EditDataSeriesAddOrExchangeData:
					// --- Add or Exchange data -----------
					timeSeriesHelper.addOrExchangeSeriesData(this.getSpecifiedTimeSeries());
					this.getEditedInstances().add(editInstance);
					break;
					
				case EditDataSeriesExchangeData:
					// --- Exchange data ------------------
					timeSeriesHelper.exchangeSeriesData(this.getSpecifiedTimeSeries());
					this.getEditedInstances().add(editInstance);
					break;
					
				case EditDataSeriesRemoveData:
					// --- Remove data --------------------
					timeSeriesHelper.removeSeriesData(this.getSpecifiedTimeSeries());
					this.getEditedInstances().add(editInstance);
					break;
				default:
					break;
				}
			} // end of not edited instances
			break;
		}

	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.visualisation.notifications.UpdateDataSeries#applyToOntologyInstanceViewer(de.enflexit.common.ontology.gui.OntologyInstanceViewer)
	 */
	@Override
	public void applyToOntologyInstanceViewer(OntologyInstanceViewer ontologyInstanceViewer) throws UpdateDataSeriesException {
		
		Vector<OntologyClassEditorJPanel> ontoVisPanel = ontologyInstanceViewer.getOntologyClassEditorJPanel(this.getTargetDataModelIndex());
		if (ontoVisPanel==null || ontoVisPanel.size()==0) {
			// ----------------------------------------------------------------
			// --- Apply just to the data model -------------------------------
			// ----------------------------------------------------------------
			try {
				Object[] objectArr = ontologyInstanceViewer.getConfigurationInstances();
				TimeSeriesChart tsc = (TimeSeriesChart) objectArr[this.getTargetDataModelIndex()];
				this.applyUpdate(tsc);
				
			} catch (Exception ex) {
				throw new UpdateDataSeriesException(this.getTargetDataModelIndex(), this.getComponentTypeName(), this.getComponentID(), ex);
			}
			
		} else {
			// ----------------------------------------------------------------
			// --- Apply to the visualisation ---------------------------------
			// ----------------------------------------------------------------
			try {
				
				for (int i = 0; i < ontoVisPanel.size(); i++) {
					
					TimeSeriesChartEditorJPanel tscep = null;
					TimeSeriesDataModel dataModelTimeSeries = null; 
					OntologyClassEditorJPanel ontoVisPanelSingle = ontoVisPanel.get(i);
					// --- Get the data model of the chart --------
					if (ontoVisPanelSingle instanceof TimeSeriesChartEditorJPanel) {
						tscep = (TimeSeriesChartEditorJPanel) ontoVisPanelSingle;
						dataModelTimeSeries = (TimeSeriesDataModel) tscep.getDataModel();
						
					} else if (ontoVisPanelSingle instanceof TimeSeriesWidget) {
						TimeSeriesWidget tsw = (TimeSeriesWidget) ontoVisPanelSingle;
						tscep = (TimeSeriesChartEditorJPanel) tsw.getTimeSeriesChartEditorJDialog().getContentPane();
						dataModelTimeSeries = (TimeSeriesDataModel) tscep.getDataModel();
					}
					
					// --- Apply Changes --------------------------
					switch (this.getTargetAction()) {
					case AddDataSeries:
						dataModelTimeSeries.addSeries(this.getSpecifiedTimeSeries());
						break;
					case AddOrExchangeDataSeries:
						dataModelTimeSeries.addOrExchangeSeries(this.getSpecifiedTimeSeries(), this.getTargetDataSeriesIndex());
						break;
					case ExchangeDataSeries:
						dataModelTimeSeries.exchangeSeries(this.getSpecifiedTimeSeries(), this.getTargetDataSeriesIndex());					
						break;
					case RemoveDataSeries:
						if (this.getTargetDataSeriesIndex()<=dataModelTimeSeries.getSeriesCount()-1) {
							dataModelTimeSeries.removeSeries(this.getTargetDataSeriesIndex());	
						}
						break;
						
					// ------------------------------------------------	
					// --- From here: Edits for the TimeSeriesChart ---
					// ------------------------------------------------
					case TimeSeriesChartAddOrExchangeDataRow:
						
						for (int c=0; c<this.getValueVector().size(); c++) {
							// -- Get the new value for the Time Series i -------
							Float newFloatValue = this.getValueVector().get(c);
							if (newFloatValue==null) continue;
							// --- Add new value pair to data model -------------
							dataModelTimeSeries.getTimeSeriesChartModel().addOrUpdateValuePair(c, this.getTimeStamp(), newFloatValue);
						}
						
						Vector<Number> newRow = new Vector<>();
						newRow.add(this.getTimeStamp());
						newRow.addAll(this.getValueVector());
						dataModelTimeSeries.getTimeSeriesTableModel().addRow(newRow);
						break;
						
					// ------------------------------------------------	
					// --- From here: Edits of single TimeSeries ------
					// ------------------------------------------------
					case EditDataSeriesAddData:
					case EditDataSeriesAddOrExchangeData:
					case EditDataSeriesExchangeData:
					case EditDataSeriesRemoveData:			
						// -- Get the time series to work on ---------- 
						List oldValuePairs = dataModelTimeSeries.getOntologyModel().getSeriesData(this.getTargetDataSeriesIndex());
						Integer editInstance = System.identityHashCode(oldValuePairs);
						boolean editOntology = !this.getEditedInstances().contains(editInstance);
						switch (this.getTargetAction()) {
						case EditDataSeriesAddData:
							dataModelTimeSeries.editDataSeriesAddData(this.getSpecifiedTimeSeries(), this.getTargetDataSeriesIndex(), editOntology);
							this.getEditedInstances().add(editInstance);
							break;
						case EditDataSeriesAddOrExchangeData:
							dataModelTimeSeries.editDataSeriesAddOrExchangeData(this.getSpecifiedTimeSeries(), this.getTargetDataSeriesIndex(), editOntology);
							this.getEditedInstances().add(editInstance);
							break;
						case EditDataSeriesExchangeData:
							dataModelTimeSeries.editDataSeriesExchangeData(this.getSpecifiedTimeSeries(), this.getTargetDataSeriesIndex(), editOntology);
							this.getEditedInstances().add(editInstance);
							break;
						case EditDataSeriesRemoveData:
							dataModelTimeSeries.editDataSeriesRemoveData(this.getSpecifiedTimeSeries(), this.getTargetDataSeriesIndex(), editOntology);
							this.getEditedInstances().add(editInstance);
							break;
						default:
							break;
						}
						break;
						
					} // end switch
					
				} // end for
			
			} catch (Exception ex) {
				throw new UpdateDataSeriesException(this.getTargetDataModelIndex(), this.getComponentTypeName(), this.getComponentID(), ex);
			}
			
		}//end apply to visualisation
		
		
	} // end method
	
	
}
