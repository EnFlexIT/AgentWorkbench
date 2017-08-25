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

import agentgui.core.charts.xyChart.XyDataModel;
import agentgui.core.charts.xyChart.XySeriesHelper;
import agentgui.core.charts.xyChart.gui.XyChartEditorJPanel;
import agentgui.core.charts.xyChart.gui.XyWidget;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.visualisation.DisplayAgent;
import agentgui.ontology.XyChart;
import agentgui.ontology.XyDataSeries;
import agentgui.simulationService.transaction.DisplayAgentNotification;
import de.enflexit.common.ontology.gui.OntologyClassEditorJPanel;
import de.enflexit.common.ontology.gui.OntologyInstanceViewer;

/**
 * The Class UpdateXySeries can be used in order to send 
 * {@link DisplayAgentNotification}'s to the {@link DisplayAgent}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class UpdateXySeries extends UpdateDataSeries {

	private static final long serialVersionUID = 8563478170121892593L;

	private XyDataSeries specifiedXySeries = null;
	
	/**
	 * Instantiates a new update xy series.
	 *
	 * @param networkComponent the network component
	 * @param targetDataModelIndex the target data model index
	 */
	public UpdateXySeries(NetworkComponent networkComponent, int targetDataModelIndex) {
		super(networkComponent, targetDataModelIndex);
	}
	
	/**
	 * Instantiates a new update xy series.
	 *
	 * @param graphNode the graph node
	 * @param targetDataModelIndex the target data model index
	 */
	public UpdateXySeries(GraphNode graphNode, int targetDataModelIndex) {
		super(graphNode, targetDataModelIndex);
	}
	
	/**
	 * Adds a xy series.
	 * @param newXySeries the new xy series
	 */
	public void addXySeries(XyDataSeries newXySeries) {
		this.setTargetAction(UPDATE_ACTION.AddDataSeries);
		this.setSpecifiedXySeries(newXySeries);
	}
	/**
	 * Adds or exchanges a Xy Series at a specified data series index.
	 * @param newXySeries the new xy series
	 * @param dataSeriesIndex the data series index
	 */
	public void addOrExchangeXySeries(XyDataSeries newXySeries, int dataSeriesIndex) {
		this.setTargetAction(UPDATE_ACTION.AddOrExchangeDataSeries);
		this.setSpecifiedXySeries(newXySeries);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
	}
	/**
	 * Exchange exchanges a Xy Series at a specified data series index.
	 * If the specified index can not be found, nothing will be done.
	 * @param newXySeries the new xy series
	 * @param dataSeriesIndex the data series index
	 */
	public void exchangeXySeries(XyDataSeries newXySeries, int dataSeriesIndex) {
		this.setTargetAction(UPDATE_ACTION.ExchangeDataSeries);
		this.setSpecifiedXySeries(newXySeries);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
	}
	/**
	 * Removes the xy series.
	 * @param dataSeriesIndex the data series index
	 */
	public void removeXySeries(int dataSeriesIndex) {
		this.setTargetAction(UPDATE_ACTION.RemoveDataSeries);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
	}

	/**
	 * Sets the specified xy series.
	 * @param specifiedXySeries the new specified xy series
	 */
	public void setSpecifiedXySeries(XyDataSeries specifiedXySeries) {
		this.specifiedXySeries = specifiedXySeries;
	}
	/**
	 * Gets the specified xy series.
	 * @return the specified xy series
	 */
	public XyDataSeries getSpecifiedXySeries() {
		return specifiedXySeries;
	}
	

	/**
	 * Edits the xy series: adds new data to a xy series.
	 *
	 * @param dataSeriesIndex the data series index
	 * @param xySeriesData2Add the xy series data2 add
	 */
	public void editXySeriesAddXySeriesData(int dataSeriesIndex, XyDataSeries xySeriesData2Add) {
		this.setTargetAction(UPDATE_ACTION.EditDataSeriesAddData);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
		this.setSpecifiedXySeries(xySeriesData2Add);
	}
	
	/**
	 * Edits the xy series: adds or exchanges xy series data.
	 *
	 * @param dataSeriesIndex the data series index
	 * @param xySeriesData2AddOrExchange the xy series data2 add or exchange
	 */
	public void editXySeriesAddOrExchangeXySeriesData(int dataSeriesIndex, XyDataSeries xySeriesData2AddOrExchange) {
		this.setTargetAction(UPDATE_ACTION.EditDataSeriesAddOrExchangeData);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
		this.setSpecifiedXySeries(xySeriesData2AddOrExchange);
	}
	/**
	 * Edits the xy series: exchanges xy series data. If the specified xystamps 
	 * can not be found, nothing will be done.
	 *
	 * @param dataSeriesIndex the data series index
	 * @param xySeriesData2AddOrExchange the xy series data2 add or exchange
	 */
	public void editXySeriesExchangeXySeriesData(int dataSeriesIndex, XyDataSeries xySeriesData2AddOrExchange) {
		this.setTargetAction(UPDATE_ACTION.EditDataSeriesExchangeData);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
		this.setSpecifiedXySeries(xySeriesData2AddOrExchange);
	}
	
	/**
	 * Edits the xy series: removes xy series data specified by the xystamps.
	 *
	 * @param dataSeriesIndex the data series index
	 * @param xySeriesData2Remove the xy series data2 remove
	 */
	public void editXySeriesRemoveXySeriesData(int dataSeriesIndex, XyDataSeries xySeriesData2Remove) {
		this.setTargetAction(UPDATE_ACTION.EditDataSeriesRemoveData);
		this.setTargetDataSeriesIndex(dataSeriesIndex);
		this.setSpecifiedXySeries(xySeriesData2Remove);
	}

	

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.visualisation.notifications.UpdateDataSeries#applyToNetworkModelOnly(agentgui.envModel.graph.networkModel.NetworkModel)
	 */
	@Override
	public void applyToNetworkModelOnly(NetworkModel networkModel) throws UpdateDataSeriesException {

		Object dataModel = null;
		switch (this.getComponentType()) {
		case GraphNode:
			GraphNode node = (GraphNode)networkModel.getGraphElement(this.getComponentID());
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
			XyChart tsc = (XyChart) objectArr[this.getTargetDataModelIndex()];
			this.applyUpdate(tsc);
			
		} catch (Exception ex) {
			throw new UpdateDataSeriesException(this.getTargetDataModelIndex(), this.getComponentTypeName(), this.getComponentID(), ex);
		}
		
	}
	
	/**
	 * Apply the update configured in this class.
	 * @param xySeriesChart the actual xy series chart
	 */
	private void applyUpdate(XyChart xySeriesChart) {
		
		switch (this.getTargetAction()) {
		case AddDataSeries:
			xySeriesChart.getXyChartData().add(this.getSpecifiedXySeries());
			break;
			
		case AddOrExchangeDataSeries:
			if (this.getTargetDataSeriesIndex()<=xySeriesChart.getXyChartData().size()-1) {
				xySeriesChart.getXyChartData().remove(this.getTargetDataSeriesIndex());	
				xySeriesChart.getXyChartData().add(this.getTargetDataSeriesIndex(), this.getSpecifiedXySeries());
			} else {
				xySeriesChart.getXyChartData().add(this.getSpecifiedXySeries());
			}
			break;

		case ExchangeDataSeries:
			if (this.getTargetDataSeriesIndex()<=xySeriesChart.getXyChartData().size()-1) {
				xySeriesChart.getXyChartData().remove(this.getTargetDataSeriesIndex());
				xySeriesChart.getXyChartData().add(this.getTargetDataSeriesIndex(), this.getSpecifiedXySeries());
			} 
			break;
			
		case RemoveDataSeries:
			if (this.getTargetDataSeriesIndex()<=xySeriesChart.getXyChartData().size()-1) {
				xySeriesChart.getXyChartData().remove(this.getTargetDataSeriesIndex());	
			}
			break;

		// ------------------------------------------------	
		// --- From here: Edits of single XySeries ------
		// ------------------------------------------------
		case EditDataSeriesAddData:
		case EditDataSeriesAddOrExchangeData:
		case EditDataSeriesExchangeData:
		case EditDataSeriesRemoveData:			
			// -- Get the xy series to work on ---------- 
			XyDataSeries xySeries = (XyDataSeries) xySeriesChart.getXyChartData().get(this.getTargetDataSeriesIndex());
			List oldValuePairs = xySeries.getXyValuePairs();
			Integer editInstance = System.identityHashCode(oldValuePairs);
			// --- Was the instance already edited? -------
			if (this.getEditedInstances().contains(editInstance)==false) {
				// --- Get the list of new value pairs ----
				XySeriesHelper xySeriesHelper = new XySeriesHelper(xySeries);
				// --- Again, case separation -------------
				switch (this.getTargetAction()) {
				case EditDataSeriesAddData:
					// --- Add data to series -------------
					xySeriesHelper.addSeriesData(this.getSpecifiedXySeries());
					this.getEditedInstances().add(editInstance);
					break;

				case EditDataSeriesAddOrExchangeData:
					// --- Add or Exchange data -----------
					xySeriesHelper.addOrExchangeSeriesData(this.getSpecifiedXySeries());
					this.getEditedInstances().add(editInstance);
					break;
					
				case EditDataSeriesExchangeData:
					// --- Exchange data ------------------
					xySeriesHelper.exchangeSeriesData(this.getSpecifiedXySeries());
					this.getEditedInstances().add(editInstance);
					break;
					
				case EditDataSeriesRemoveData:
					// --- Remove data --------------------
					xySeriesHelper.removeSeriesData(this.getSpecifiedXySeries());
					this.getEditedInstances().add(editInstance);
					break;
				default:
					break;
				}
			} // end of not edited instances
			break;
			
		// ------------------------------------------------	
		// --- From here: Some 'spare' cases --------------
		// ------------------------------------------------
		case TimeSeriesChartAddOrExchangeDataRow:
			// --- Nothing to do here ---------------------
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
				XyChart tsc = (XyChart) objectArr[this.getTargetDataModelIndex()];
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
					
					XyChartEditorJPanel tscep = null;
					XyDataModel dataModelXySeries = null; 
					OntologyClassEditorJPanel ontoVisPanelSingle = ontoVisPanel.get(i);
					// --- Get the data model of the chart --------
					if (ontoVisPanelSingle instanceof XyChartEditorJPanel) {
						tscep = (XyChartEditorJPanel) ontoVisPanelSingle;
						dataModelXySeries = (XyDataModel) tscep.getDataModel();
						
					} else if (ontoVisPanelSingle instanceof XyWidget) {
						XyWidget tsw = (XyWidget) ontoVisPanelSingle;
						tscep = (XyChartEditorJPanel) tsw.getXyChartEditorJDialog().getContentPane();
						dataModelXySeries = (XyDataModel) tscep.getDataModel();
					}
					
					// --- Apply Changes --------------------------
					switch (this.getTargetAction()) {
					case AddDataSeries:
						dataModelXySeries.addSeries(this.getSpecifiedXySeries());
						break;
					case AddOrExchangeDataSeries:
						dataModelXySeries.addOrExchangeSeries(this.getSpecifiedXySeries(), this.getTargetDataSeriesIndex());
						break;
					case ExchangeDataSeries:
						dataModelXySeries.exchangeSeries(this.getSpecifiedXySeries(), this.getTargetDataSeriesIndex());					
						break;
					case RemoveDataSeries:
						if (this.getTargetDataSeriesIndex()<=dataModelXySeries.getSeriesCount()-1) {
							dataModelXySeries.removeSeries(this.getTargetDataSeriesIndex());	
						}
						break;
						
					// ------------------------------------------------	
					// --- From here: Edits of single XySeries ------
					// ------------------------------------------------
					case EditDataSeriesAddData:
					case EditDataSeriesAddOrExchangeData:
					case EditDataSeriesExchangeData:
					case EditDataSeriesRemoveData:			
						// -- Get the xy series to work on ---------- 
						List oldValuePairs = dataModelXySeries.getOntologyModel().getSeriesData(this.getTargetDataSeriesIndex());
						Integer editInstance = System.identityHashCode(oldValuePairs);
						boolean editOntology = !this.getEditedInstances().contains(editInstance);
						switch (this.getTargetAction()) {
						case EditDataSeriesAddData:
							dataModelXySeries.editDataSeriesAddData(this.getSpecifiedXySeries(), this.getTargetDataSeriesIndex(), editOntology);
							this.getEditedInstances().add(editInstance);
							break;
						case EditDataSeriesAddOrExchangeData:
							dataModelXySeries.editDataSeriesAddOrExchangeData(this.getSpecifiedXySeries(), this.getTargetDataSeriesIndex(), editOntology);
							this.getEditedInstances().add(editInstance);
							break;
						case EditDataSeriesExchangeData:
							dataModelXySeries.editDataSeriesExchangeData(this.getSpecifiedXySeries(), this.getTargetDataSeriesIndex(), editOntology);
							this.getEditedInstances().add(editInstance);
							break;
						case EditDataSeriesRemoveData:
							dataModelXySeries.editDataSeriesRemoveData(this.getSpecifiedXySeries(), this.getTargetDataSeriesIndex(), editOntology);
							this.getEditedInstances().add(editInstance);
							break;
						default:
							break;
						}
						break;
						
					// ------------------------------------------------	
					// --- From here: Some 'spare' cases --------------
					// ------------------------------------------------
					case TimeSeriesChartAddOrExchangeDataRow:
						// --- Nothing to do here ---------------------
						break;
					} // end switch
					
				} // end for
			
			} catch (Exception ex) {
				throw new UpdateDataSeriesException(this.getTargetDataModelIndex(), this.getComponentTypeName(), this.getComponentID(), ex);
			}
			
		}//end apply to visualisation
		
		
	} // end method
	
	
}
