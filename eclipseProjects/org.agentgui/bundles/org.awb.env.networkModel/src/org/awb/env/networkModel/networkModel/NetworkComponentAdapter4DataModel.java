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
package org.awb.env.networkModel.networkModel;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JComponent;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiJDesktopPane;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiProperties;


/**
 * The abstract Class NetworkComponentAdapter4DataModel can be extended to provide the required data handling
 * and visualization functionalities for individual data types for {@link NetworkComponent}s or {@link GraphNode}s.
 * It is used in {@link NetworkComponentAdapter}.   
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class NetworkComponentAdapter4DataModel {

	/** The current NetworkComponentAdapter that owns the current instance */
	private NetworkComponentAdapter networkComponentAdapter;

	/** The current GraphEnvironmentController */
	protected GraphEnvironmentController graphController;
	/** The current NetworkComponent */
	protected NetworkComponent networkComponent;
	/** The current GraphNode */
	protected GraphNode graphNode;

	
	/**
	 * Instantiates a new network component data model adapter.
	 * @param graphController the GraphEnvironmentController
	 */
	public NetworkComponentAdapter4DataModel(GraphEnvironmentController graphController) {
		this.setGraphEnvironmentController(graphController);
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
	 * Returns the data model of a {@link NetworkComponent} or a {@link GraphNode} as Vector of Base64 encoded String
	 * by using the corresponding static method of the {@link DataModelEnDecoder64}.
	 *
	 * @param dataModel the data model model (usually an object array) 
	 * @return the data model encoded as Base64 String vector
	 */
	public Vector<String> getDataModelBase64Encoded(Object dataModel) {
		return DataModelEnDecoder64.getDataModelBase64Encoded(dataModel);
	}
	/**
	 * Returns the data model specified by the Base64 encoded String vector of a {@link NetworkComponent} or a {@link GraphNode} 
	 * as Object array by using the corresponding static method of the {@link DataModelEnDecoder64}.
	 *
	 * @param dataModel the data model as Base64 encoded String vector
	 * @return the data model array 
	 */
	public Object getDataModelBase64Decoded(Vector<String> dataModel) {
		return DataModelEnDecoder64.getDataModelBase64Decoded(dataModel);
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
	public NetworkComponent getNetworkComponent() {
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
	public GraphNode getGraphNode() {
		return graphNode;
	}
}
