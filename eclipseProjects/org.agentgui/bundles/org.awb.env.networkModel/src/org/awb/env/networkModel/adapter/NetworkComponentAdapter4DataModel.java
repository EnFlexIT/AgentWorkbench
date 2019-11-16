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
package org.awb.env.networkModel.adapter;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler;
import org.awb.env.networkModel.adapter.dataModel.DataModelStorageHandlerBase64;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiJDesktopPane;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiProperties;


/**
 * The abstract Class NetworkComponentAdapter4DataModel can be extended to provide the required data handling
 * and visualization functionalities for individual data model types for {@link NetworkComponent}s or {@link GraphNode}s.
 * It is used in {@link NetworkComponentAdapter}.   
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class NetworkComponentAdapter4DataModel {

	public static final String DATA_MODEL_TYPE_SERIALIZABLE_OBJECT = "SerializableObject";
	
	private List<String> dataModleTypes;
	
	protected NetworkComponentAdapter networkComponentAdapter;

	protected GraphEnvironmentController graphController;
	protected NetworkComponent networkComponent;
	protected GraphNode graphNode;

	protected AbstractDataModelStorageHandler dataModelStorageHandler;
	
	private String partModelID;
	
	/**
	 * <b>Just for an internal use!</b> Instantiates a new network component data model adapter. 
	 */
	protected NetworkComponentAdapter4DataModel() { }
	/**
	 * Instantiates a new network component data model adapter.
	 * @param graphController the GraphEnvironmentController
	 */
	public NetworkComponentAdapter4DataModel(GraphEnvironmentController graphController) {
		this.setGraphEnvironmentController(graphController);
	}
	
	/**
	 * Has to return a vector of string qualifier that specify the data model types that can be handled 
	 * with the current data model adapter. Thus, it can be checked, if the data model adapter is of a 
	 * specific type.<br><br>
	 * Mostly, the list will contain only a single element. By default, this method will return 
	 * 'SerializableObject' as single element within the list.<br>
	 * <b>Overwrite this method, to specify individual data model types</b>   
	 *
	 * @return the data model types that will be handled with the current data model adapter
	 * @see #containsDataModelType(String)
	 */
	protected List<String> getDataModelTypes() {
		if (dataModleTypes==null) {
			dataModleTypes = new ArrayList<String>();
			dataModleTypes.add(DATA_MODEL_TYPE_SERIALIZABLE_OBJECT);	
		}
		return dataModleTypes;
	}
	/**
	 * This method allows to check, if the current data model adapter is of a specific type.
	 *
	 * @param dataModelType the data model type to check
	 * @return true, if this data model adapter is of the specified type
	 * @see #getDataModelTypes()
	 */
	public boolean containsDataModelType(String dataModelType) {
		List<String> handledDataModelTypes = this.getDataModelTypes();
		if (handledDataModelTypes!=null) {
			return handledDataModelTypes.contains(dataModelType);
		}
		return false;
	}
	
	/**
	 * Returns the JComponent for the visualization of the data model.
	 *
	 * @param internalPropertyFrame the current internal property frame that will display the properties
	 * @return the visualization component
	 */
	public abstract JComponent getVisualizationComponent(BasicGraphGuiProperties internalPropertyFrame);
	
	/**
	 * Sets the visualization component.
	 * @param visualizationComponent the new visualization component
	 */
	public abstract void setVisualizationComponent(JComponent visualizationComponent);
	
	
	/**
	 * May return additional components for the tool bar of the properties dialog.
	 * For this, overwrite this method in your specific implementation.,
	 * @return the tool bar elements
	 */
	public Vector<JComponent> getToolBarElements() {
		return null;
	}
	
	
	/**
	 * Returns the default size for the visualization of the data model.
	 * Override this method in order to get control over the window size 
	 * of your own, customized visualization. 
	 * By default this method just returns null.
	 *
	 * @param graphDesktop the current desktop instance that will be the host of the visualization 
	 * @return the Dimension of the visualization
	 */
	public Dimension getSizeOfVisualisation(BasicGraphGuiJDesktopPane graphDesktop) {
		return null;
	}
	
	
	/**
	 * Will be invoked to prepare for saving the current data model instance and can be used to
	 * validate the settings in the visualization component. The name 'save' used here, basically 
	 * indicates that the save button was pressed by the user or that the window is about to close.
	 * Use this method to rise error messages for the user.
	 *
	 * @return true, if the edit / property window can be closed, <code>false</code> otherwise.
	 */
	public abstract boolean save();
	
	
	/**
	 * Sets the data model of a NetworkComponent to the visualization component.
	 * @param networkComponent the NetworkComponent
	 */
	public void setDataModel(NetworkComponent networkComponent) {
		try {
			this.setDataModel(networkComponent.getDataModel());
		} catch (Exception ex) {
			System.err.println("Error while setting data model of NetworkComponent '" + networkComponent.getId() + "'!");
			ex.printStackTrace();
		}
	}
	/**
	 * Sets the data model of a GraphNode to the visualization component.
	 * @param graphNode the GraphNode
	 */
	public void setDataModel(GraphNode graphNode) {
		try {
			this.setDataModel(graphNode.getDataModel());
		} catch (Exception ex) {
			System.err.println("Error while setting data model of GraphNode '" + graphNode.getId() + "'!");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Has to set the data model to the visualization component (and refresh it).
	 * @param dataModel the new data model
	 */
	public abstract void setDataModel(Object dataModel);
	
	/**
	 * Returns the data model from the visualization component.
	 * @return the data model
	 */
	public abstract Object getDataModel();
	
	
	/**
	 * Basically, calls the method {@link #getDataModelStorageHandler()}. If this method does not return 
	 * an individual storage handler the default {@link DataModelStorageHandlerBase64} will be used instead.
	 *
	 * @return the storage handler
	 */
	public final AbstractDataModelStorageHandler getDataModelStorageHandlerInternal() {
		if (dataModelStorageHandler==null) {
			dataModelStorageHandler = this.getDataModelStorageHandler();
			if (dataModelStorageHandler==null) {
				dataModelStorageHandler = new DataModelStorageHandlerBase64();
			}
		}
		return dataModelStorageHandler;
	}
	/**
	 * May return an individual data storage handler to be used to save the data model of a {@link DataModelNetworkElement}
	 * that is either a {@link NetworkComponent} or a {@link GraphNode}. Overwrite this method to place an individual storage handler here.
	 * @return the individual data model storage handler
	 */
	protected AbstractDataModelStorageHandler getDataModelStorageHandler() {
		return null;
	}
	
	
	// --------------------------------------------------------------
	// --- From here some simple getter and setter methods ----------
	// --------------------------------------------------------------
	/**
	 * Sets the current GraphEnvironmentController.
	 * @param graphController the new GraphEnvironmentController
	 */
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	/**
	 * Returns the current GraphEnvironmentController.
	 * @return the GraphEnvironmentController
	 */
	protected GraphEnvironmentController getGraphEnvironmentController() {
		return this.graphController;
	}
	
	/**
	 * Sets the current network component adapter.
	 * @param networkComponentAdapter the new network component adapter
	 */
	public void setNetworkComponentAdapter(NetworkComponentAdapter networkComponentAdapter) {
		this.networkComponentAdapter = networkComponentAdapter;
	}
	/**
	 * Returns the superordinate NetworkComponentAdapter.
	 * @return the network component adapter
	 */
	protected NetworkComponentAdapter getNetworkComponentAdapter() {
		return networkComponentAdapter;
	}
	
	/**
	 * Sets the network component that will be edited by the adapter.
	 * @param networkComponent the new network component
	 */
	public void setNetworkComponent(NetworkComponent networkComponent) {
		this.networkComponent = networkComponent;
	}
	/**
	 * Returns the network component that will be edited.
	 * @return the network component
	 */
	protected NetworkComponent getNetworkComponent() {
		return networkComponent;
	}
	
	/**
	 * Sets the graph node that will be edited by the adapter.
	 * @param graphNode the new graph node
	 */
	public void setGraphNode(GraphNode graphNode) {
		this.graphNode = graphNode;
	}
	/**
	 * Gets the graph node.
	 * @return the graph node
	 */
	protected GraphNode getGraphNode() {
		return graphNode;
	}
	
	/**
	 * Returns either the {@link NetworkComponent} or the {@link GraphNode} that is currently edited.
	 * @return the current {@link DataModelNetworkElement}
	 */
	protected DataModelNetworkElement getDataModelNetworkElement() {
		return this.getNetworkComponentAdapter().getDataModelNetworkElement();
	}
	
	/**
	 * Sets the part model ID, if there is any.
	 * @param partModelID the new part model ID
	 */
	public void setPartModelID(String partModelID) {
		this.partModelID = partModelID;
	}
	/**
	 * Returns the part model ID, if there is any.
	 * @return the part model ID or null
	 */
	protected String getPartModelID() {
		return partModelID;
	}
	
}
