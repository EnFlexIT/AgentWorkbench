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

import java.util.HashSet;

import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import de.enflexit.common.ontology.gui.OntologyInstanceViewer;

/**
 * The abstract Class UpdateDataSeries.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class UpdateDataSeries extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = 4688700854113446061L;

	/** The Enumeration for the addressed component type. */
	public static enum COMPONENT_TYPE {
		NetworkComponent,
		GraphNode
	}
	/** The Enumeration UPDATE_ACTION specifies the possible actions of data series updates. */
	public static enum UPDATE_ACTION {
		AddDataSeries,
		AddOrExchangeDataSeries,
		ExchangeDataSeries,
		RemoveDataSeries,
		
		EditDataSeriesAddData,
		EditDataSeriesAddOrExchangeData,
		EditDataSeriesExchangeData,
		EditDataSeriesRemoveData,
		
		TimeSeriesChartAddOrExchangeDataRow
	}

	
	/** The component type. */
	private COMPONENT_TYPE componentType = null;
	/** The component ID. */
	private String componentID = null;
	
	/** The target data model index. */
	private int targetDataModelIndex = -1;
	/** The target data series index. */
	private int targetDataSeriesIndex = -1;
	/** The target action. */
	private UPDATE_ACTION targetAction = null; 
	
	private transient HashSet<Integer> editedInstances = null;

	/**
	 * Instantiates a new data series update.
	 *
	 * @param networkComponent the network component
	 * @param targetDataModelIndex the target data model index
	 */
	public UpdateDataSeries(NetworkComponent networkComponent, int targetDataModelIndex) {
		this.componentType = COMPONENT_TYPE.NetworkComponent;
		this.componentID = networkComponent.getId();
		this.targetDataModelIndex = targetDataModelIndex;
	}
	/**
	 * Instantiates a new data series update.
	 *
	 * @param graphNode the graph node
	 * @param targetDataModelIndex the target data model index
	 */
	public UpdateDataSeries(GraphNode graphNode, int targetDataModelIndex) {
		this.componentType = COMPONENT_TYPE.GraphNode;
		this.componentID = graphNode.getId();
		this.targetDataModelIndex = targetDataModelIndex;
	}
	
	/**
	 * Gets the component type.
	 * @return the component type
	 */
	public COMPONENT_TYPE getComponentType() {
		return componentType;
	}
	/**
	 * Gets the component type name as String.
	 * @return the component type name
	 */
	public String getComponentTypeName() {
		if (this.getComponentType()==null) return null;
		String componentTypeName = null;
		switch (this.getComponentType()) {
		case GraphNode:
			componentTypeName = "GraphNode";
		case NetworkComponent:
			componentTypeName = "NetworkComponent";
		}
		return componentTypeName;
	}
	/**
	 * Gets the component id.
	 * @return the component id
	 */
	public String getComponentID() {
		return componentID;
	}
	/**
	 * Gets the target data model index.
	 * @return the target data model index
	 */
	public int getTargetDataModelIndex() {
		return targetDataModelIndex;
	}
	
	/**
	 * Sets the target action.
	 * @param targetAction the new target action
	 */
	protected void setTargetAction(UPDATE_ACTION targetAction) {
		this.targetAction = targetAction;
	}
	/**
	 * Gets the target action.
	 * @return the target action
	 */
	public UPDATE_ACTION getTargetAction() {
		return targetAction;
	}
	
	/**
	 * Sets the target data series index.
	 * @param targetDataSeriesIndex the new target data series index
	 */
	protected void setTargetDataSeriesIndex(int targetDataSeriesIndex) {
		this.targetDataSeriesIndex = targetDataSeriesIndex;
	}
	/**
	 * Gets the target data series index.
	 * @return the target data series index
	 */
	public int getTargetDataSeriesIndex() {
		return targetDataSeriesIndex;
	}
	
	/**
	 * Returns the collection of identity hash codes of object instances that were already edited.
	 * 
	 * @see System#identityHashCode(Object)
	 * @return the series instance edited
	 */
	protected HashSet<Integer> getEditedInstances() {
		if (editedInstances==null) {
			editedInstances = new HashSet<Integer>();
		}
		return editedInstances;
	}
	
	/**
	 * Applies this update to the NetworkModel only.
	 *
	 * @param networkModel the network model
	 * @throws UpdateDataSeriesException the update data series exception
	 */
	public abstract void applyToNetworkModelOnly(NetworkModel networkModel) throws UpdateDataSeriesException;
	
	/**
	 * Applies this update to a specified OntologyInstanceViewer.
	 *
	 * @param ontologyInstanceViewer the ontology instance viewer
	 * @throws UpdateDataSeriesException the update data series exception
	 */
	public abstract void applyToOntologyInstanceViewer(OntologyInstanceViewer ontologyInstanceViewer) throws UpdateDataSeriesException;
	
	
}
