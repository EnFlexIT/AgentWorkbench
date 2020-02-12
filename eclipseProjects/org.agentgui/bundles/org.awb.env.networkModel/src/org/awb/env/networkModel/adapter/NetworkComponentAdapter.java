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

import java.util.Vector;

import javax.swing.JComponent;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.visualisation.DisplayAgent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

/**
 * The Class NetworkComponentAdapter can be used in order to extend the local 
 * data model and its visual representation of a {@link NetworkComponent}.
 * Also further functionalities can be added for context menus and so on.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class NetworkComponentAdapter {

	/** The current GraphEnvironmentController */
	protected GraphEnvironmentController graphController;
	/** The current NetworkComponent */
	protected NetworkComponent networkComponent;
	/** The current GraphNode */
	protected GraphNode graphNode;
	
	private NetworkComponentAdapter4DataModel networkComponentAdapter4DataModel;
	

	/**
	 * Instantiates a new network component adapter. Use {@link #setGraphEnvironmentController(GraphEnvironmentController)}
	 * to finalize the initialization of a NetworkComponentAdapter !!!
	 */
	public NetworkComponentAdapter() {}
	
	/**
	 * Can do some initialization and will be invoked right after the {@link GraphEnvironmentController} was 
	 * assigned to the current instance.<br>
	 * <b>Override this method if required.</b>  
	 */
	public void initialize() { }
	
	
	/**
	 * Has to return the data model adapter for this NetworkComponentAdapter.
	 * @return the data model adapter
	 * @see NetworkComponentAdapter4DataModel
	 */
	public abstract NetworkComponentAdapter4DataModel getNewDataModelAdapter();
	
	/**
	 * Returns a stored (not new) NetworkComponentAdapter4DataModel. 
	 * The idea to store one instance of the NetworkComponentAdapter4DataModel comes from
	 * the fact that the instantiation of such Object can be quit time consuming. In order 
	 * to accelerate the load process of a NetworkModel (with possibly hundreds of similar
	 * components) one instance will be stored here. In case that no data model adapter is
	 * found, the method will created a new one by using the abstract corresponding method.
	 * 
	 * @return the stored data model adapter
	 */
	public NetworkComponentAdapter4DataModel getStoredDataModelAdapter() {
		if (networkComponentAdapter4DataModel==null) {
			networkComponentAdapter4DataModel = this.getNewDataModelAdapter();
		}
		if (networkComponentAdapter4DataModel==null) return null;
		// --- Assign runtime objects to the adapter ------
		networkComponentAdapter4DataModel.setNetworkComponentAdapter(this);
		networkComponentAdapter4DataModel.setNetworkComponent(this.getNetworkComponent());
		networkComponentAdapter4DataModel.setGraphNode(this.getGraphNode());
		return networkComponentAdapter4DataModel;
	}
	/**
	 * Resets the stored data model adapter to <code>null</code>.
	 */
	public void resetStoredDataModelAdapter() {
		this.networkComponentAdapter4DataModel = null;
	}
	
	/**
	 * Returns either the {@link NetworkComponent} or the {@link GraphNode} that is currently edited.
	 * @return the data model network element
	 */
	protected DataModelNetworkElement getDataModelNetworkElement() {
		
		DataModelNetworkElement networkElment = null;
		if (this.getNetworkComponent()!=null && this.getGraphNode()!=null) {
			// --- [NetworkComponentToGraphNodeAdapter] => GraphNode ----------
			networkElment = this.getGraphNode();
		} else if (this.getNetworkComponent()!=null && this.getGraphNode()==null) {
			// --- NetworkComponent -------------------------------------------
			networkElment = this.getNetworkComponent();
		} else if (this.getNetworkComponent()==null && this.getGraphNode()!=null) {
			// --- GraphNode --------------------------------------------------
			networkElment = this.getGraphNode();
		}
		return networkElment;
	}
	
	/**
	 * Will be invoked to get the individual menu elements for this kind of NetworkComponent or GraphNode.
	 * @return the vector of individual menu elements
	 */
	public final Vector<JComponent> invokeGetJPopupMenuElements() {
		
		Vector<JComponent> popUpMenuElements = null;
		try {
			// --- Try to get the individual pop up elements --------
			popUpMenuElements = this.getJPopupMenuElements();
			
		} catch (Exception ex) {
			// --- Exception handling -------------------------------
			String elementDescription = null;
			if (this.getNetworkComponent()!=null && this.getGraphNode()!=null) {
				elementDescription = "GraphNode " + this.getGraphNode().getId() + " [NetworkComponentToGraphNodeAdapter]";
			} else if (this.getNetworkComponent()!=null && this.getGraphNode()==null) {
				elementDescription = "NetworkComponent " + this.getNetworkComponent().getId() + " [" + this.getNetworkComponent().getType() + "]";
			} else if (this.getNetworkComponent()==null && this.getGraphNode()!=null) {
				elementDescription = "GraphNode " + this.getGraphNode().getId();
			}
			System.err.println("[" + this.getClass().getSimpleName() + "] Error getting pop up menu for " + elementDescription);
			ex.printStackTrace();
		}
		return popUpMenuElements;
	}
	
	/**
	 * Returns the JPopup menu elements for this kind of NetworkComponent.
	 * @return the JPopup menu elements
	 */
	public abstract Vector<JComponent> getJPopupMenuElements();

	
	/**
	 * Gets the current display agent.
	 * @see DisplayAgent
	 * 
	 * @return the display agent
	 */
	public DisplayAgent getDisplayAgent() {
		DisplayAgent displayAgent = null;
		if (this.graphController!=null) {
			displayAgent = (DisplayAgent) this.graphController.getDisplayAgent();
		}
		return displayAgent;
	}
	/**
	 * This method can be used to transfer any kind of information to the Manager of the current environment model.
	 *
	 * @param notification the notification
	 * @return true, if successful
	 */
	public boolean sendManagerNotification(Object notification) {
		boolean successful = false;
		DisplayAgent displayAgent = this.getDisplayAgent();
		if (displayAgent!=null) {
			successful = displayAgent.sendManagerNotification(notification);
		}
		return successful;
	}
	/**
	 * This method can be used to transfer any kind of information to one member of the current environment model.
	 *
	 * @param receiverAID the AID of receiver agent
	 * @param notification the notification
	 * @return true, if successful
	 */
	public boolean sendAgentNotification(AID receiverAID, Object notification) {
		boolean successful = false;
		DisplayAgent displayAgent = this.getDisplayAgent();
		if (displayAgent!=null) {
			successful = displayAgent.sendAgentNotification(receiverAID, notification);
		}
		return successful;
	}
	/**
	 *  Send an <b>ACL</b> message to another agent. This methods sends	 a message to the agent specified 
	 *  in <code>:receiver</code> message field (more than one agent can be specified as message receiver).
	 *  
	 *  @see jade.lang.acl.ACLMessage
	 *  @param msg the ACL message to be send	 
	 */
	public void send(ACLMessage msg) {
		DisplayAgent displayAgent = this.getDisplayAgent();
		if (displayAgent!=null) {
			displayAgent.send(msg);
		}
	}

	/**
	 * Checks if the current view is the runtime visualization.
	 * @return true, if is runtime visualization
	 */
	protected boolean isRuntimeVisualization(){
		return (this.graphController!=null && this.graphController.getProject()==null);
	}
	/**
	 * Checks if the current view is the setup configuration.
	 * @return true, if is setup configuration
	 */
	protected boolean isSetupConfiguration(){
		return (this.graphController!=null && this.graphController.getProject()!=null);
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
