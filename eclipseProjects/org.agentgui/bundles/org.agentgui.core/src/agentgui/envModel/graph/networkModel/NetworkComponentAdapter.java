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
package agentgui.envModel.graph.networkModel;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.core.environment.EnvironmentController;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.visualisation.DisplayAgent;
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
	
	/** The stored NetworkComponentAdapter4DataModel for accelerating the load and save process of NetworkModel's */
	private NetworkComponentAdapter4DataModel networkComponentAdapter4DataModel;
	
	
	/**
	 * Instantiates a new network component adapter that allows access 
	 * to the controlling agent if there is one.
	 * 
	 * @see EnvironmentController#getDisplayAgent()
	 * @see EnvironmentController#setDisplayAgent(agentgui.simulationService.agents.AbstractDisplayAgent)
	 * 
	 * @see GraphEnvironmentController#getDisplayAgent()
	 * @see GraphEnvironmentController#setDisplayAgent(agentgui.simulationService.agents.AbstractDisplayAgent)
	 * 
	 * @param graphEnvironmentController the graph environment controller
	 */
	public NetworkComponentAdapter(GraphEnvironmentController graphEnvironmentController) {
		this.graphController = graphEnvironmentController;
	}
	
	/**
	 * Returns the data model adapter for the {@link NetworkComponent}.
	 * @return the adapter visualization
	 */
	public abstract NetworkComponentAdapter4DataModel getNewDataModelAdapter();
	
	/**
	 * Returns a stored (not new) NetworkComponentAdapter4DataModel. 
	 * The idea to store one instance of the NetworkComponentAdapter4DataModel comes from
	 * the fact that the instantiation of such Object can be quit time consuming. In order 
	 * to accelerate the load process of a NetworkModel (with possibly hundreds of similar
	 * components) one instance will be stored here. In case that no data model adapter is
	 * found, the method will created a new one by invoking {@link #getNewDataModelAdapter()}.
	 * 
	 * @return the stored data model adapter
	 */
	public NetworkComponentAdapter4DataModel getStoredDataModelAdapter() {
		if (networkComponentAdapter4DataModel==null) {
			networkComponentAdapter4DataModel = this.getNewDataModelAdapter();
		}
		return networkComponentAdapter4DataModel;
	}
	
	/**
	 * Invokes to get the JPopup menu elements for this kind of NetworkComponent.
	 * DO NOT OVERRIDE !!!
	 *
	 * @param networkComponentOrGraphNode the current NetworkComponent
	 * @return the vector of menu elements
	 */
	public Vector<JComponent> invokeGetJPopupMenuElements(Object networkComponentOrGraphNode) {
		
		if (networkComponentOrGraphNode instanceof NetworkComponent) {
			this.networkComponent = (NetworkComponent) networkComponentOrGraphNode;
			this.graphNode = null;
		} else if (networkComponentOrGraphNode instanceof GraphNode) {
			this.networkComponent = null;
			this.graphNode = (GraphNode) networkComponentOrGraphNode;
		}
		return this.getJPopupMenuElements();
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
		return (this.graphController.getProject()==null);
	}
	/**
	 * Checks if the current view is the setup configuration.
	 * @return true, if is setup configuration
	 */
	protected boolean isSetupConfiguration(){
		return (this.graphController.getProject()!=null);
	}
	
}
