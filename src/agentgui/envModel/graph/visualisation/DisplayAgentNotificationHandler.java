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
package agentgui.envModel.graph.visualisation;

import jade.core.AID;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphElementLayout;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import agentgui.envModel.graph.visualisation.notifications.DataModelOpenViewNotification;
import agentgui.envModel.graph.visualisation.notifications.DisplayAgentNotificationGraph;
import agentgui.envModel.graph.visualisation.notifications.DisplayAgentNotificationGraphMultiple;
import agentgui.envModel.graph.visualisation.notifications.GraphLayoutNotification;
import agentgui.envModel.graph.visualisation.notifications.DataModelNotification;
import agentgui.envModel.graph.visualisation.notifications.NetworkComponentDirectionNotification;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * The Class DisplayAgentNotificationHandler is used by the {@link DisplayAgent}
 * and applies the {@link DisplayAgentNotificationGraph}'s to current visualisation.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class DisplayAgentNotificationHandler {


	
	/**
	 * Instantiates a new display agent notification handler.
	 */
	public DisplayAgentNotificationHandler() {
	}
	
	public EnvironmentNotification setDisplayNotification(EnvironmentNotification notification, NetworkModel networkModel) {
		return this.setDisplayNotification(notification, networkModel, null);
	}
	
	/**
	 * Translates a display notification into a visual representation.
	 *
	 * @param networkModel the network model
	 * @param notification the notification
	 * @param graphController the current GraphEnvironmentController, if available (can also be null)
	 * @return the environment notification
	 */
	public EnvironmentNotification setDisplayNotification(EnvironmentNotification notification, NetworkModel networkModel, GraphEnvironmentController graphController) {
		
		// ----------------------------------------------------------
		// --- Check for missing information ------------------------
		// ----------------------------------------------------------
		if (notification==null || networkModel==null) {
			try {
				String exceptionText = null;
				if (notification==null) {
					exceptionText = "EnvironmentNotification was not set or is null!";
				} else if (networkModel==null) {
					exceptionText = "There is no NetworkModel defined!";
				}
				throw new NullPointerException(exceptionText);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return notification; 
		}
		
		// ----------------------------------------------------------
		// --- Single or multiple DisplayAgentNotificationGraph ----- 
		DisplayAgentNotificationGraph displayNotification = null;
		DisplayAgentNotificationGraphMultiple displayNotificationMultiple = null;
			
		// ----------------------------------------------------------
		// --- Separate cases single or multiple notifications ------ 
		// ----------------------------------------------------------
		if (notification.getNotification() instanceof DisplayAgentNotificationGraphMultiple) {
			// ------------------------------------------------------
			// --- Work on multiple notifications -------------------
			displayNotificationMultiple = (DisplayAgentNotificationGraphMultiple) notification.getNotification();
			for (int i = 0; i < displayNotificationMultiple.getDisplayNotifications().size(); i++) {
				// --- Work on a single notification ----------------
				DisplayAgentNotificationGraph displayNotificationSingle =  displayNotificationMultiple.getDisplayNotifications().get(i);
				try {
					// --- Try to apply the current settings --------
					this.doDisplayAction(networkModel, notification.getSender(), displayNotificationSingle);
					
				} catch (Exception ex) {
					System.out.println("=> Error in DisplayAgent!");
					ex.printStackTrace();
				}
			}
			
		} else if (notification.getNotification() instanceof DisplayAgentNotificationGraph) {
			// ------------------------------------------------------
			// --- Work on a single notification --------------------
			displayNotification = (DisplayAgentNotificationGraph) notification.getNotification();
			try {
				// --- Try to apply the current settings ------------
				this.doDisplayAction(networkModel, notification.getSender(), displayNotification);
				
			} catch (Exception ex) {
				System.out.println("=> Error in DisplayAgent!");
				ex.printStackTrace();
			}
			
		} 
		
		// ----------------------------------------------------------
		// --- Repaint the Graph ------------------------------------
		// ----------------------------------------------------------
		if (graphController!=null) {
			NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Repaint);
			if (displayNotification!=null) {
				nmn.setInfoObject(displayNotification);
			} else if (displayNotificationMultiple!=null) {
				nmn.setInfoObject(displayNotificationMultiple);	
			}
			graphController.notifyObservers(nmn);	
		}
		return notification;
	}
	
	/**
	 * Do the concrete display action.
	 *
	 * @param senderAID the sender aid
	 * @param displayNotification the GraphDisplayAgentNotification
	 */
	private void doDisplayAction(NetworkModel networkModel, AID senderAID, DisplayAgentNotificationGraph displayNotification) {
		
		if (displayNotification instanceof NetworkComponentDirectionNotification) {
			// ------------------------------------------------------
			// => Set directions of the current NetworkComponent ----
			// ------------------------------------------------------
			NetworkComponentDirectionNotification netCompDirection = (NetworkComponentDirectionNotification) displayNotification;
			NetworkComponent netComp = netCompDirection.getNetworkComponent();
			networkModel.setDirectionsOfNetworkComponent(netComp);

		} else if (displayNotification instanceof GraphLayoutNotification) {
			// ------------------------------------------------------
			// => Set the layout changes to the GraphElements -------
			// ------------------------------------------------------
			GraphLayoutNotification layoutNotifications = (GraphLayoutNotification) displayNotification;
			for (int i = 0; i < layoutNotifications.getGraphElementLayouts().size(); i++) {
				
				GraphElementLayout graphElementLayout = layoutNotifications.getGraphElementLayouts().get(i);
				String graphElementID = graphElementLayout.getGraphElement().getId();
				
				// --- Get the local element and apply the layout ---
				GraphElement localGrahElement = networkModel.getGraphElement(graphElementID);
				graphElementLayout.setGraphElement(localGrahElement);
				localGrahElement.setGraphElementLayout(graphElementLayout);
			}

		} else if (displayNotification instanceof DataModelOpenViewNotification) {
			// ------------------------------------------------------
			// => Open the data model visualisation of a component --
			// ------------------------------------------------------
			// Here is nothing to do with the data model. The notification will
			// be forwarded to the displaying components and the observer pattern
			// of the GraphEnvironmentController.
			
		} else if (displayNotification instanceof DataModelNotification) {
			// ------------------------------------------------------
			// => Set data model of NetworkComponent or GraphNode --
			// ------------------------------------------------------
			DataModelNotification dmNote = (DataModelNotification) displayNotification;
			if (dmNote.isEmpty()==false) {

				if (dmNote.isNetworkComponentConfiguration()==true) {
					// ----------------------------------------------
					// --- Case NetworkComponent --------------------
					NetworkComponent netCompSend  = dmNote.getNetworkComponent();
					NetworkComponent netCompLocal = networkModel.getNetworkComponent(netCompSend.getId());
					if (dmNote.isUseDataModelBase64Encoded()==true) {
						// --- Case Base64 -------------------------- 
						if (dmNote.getDataModelPartUpdateIndex()==-1) {
							// --- Take everything ------------------
							netCompLocal.setDataModelBase64(netCompSend.getDataModelBase64());
						} else {
							// --- Just take a specified detail -----
							int updateIndex = dmNote.getDataModelPartUpdateIndex();
							String updateString = netCompSend.getDataModelBase64().get(updateIndex);
							netCompLocal.getDataModelBase64().setElementAt(updateString, updateIndex);
						}
						
					} else {
						// --- Case Object instance -----------------
						if (dmNote.getDataModelPartUpdateIndex()==-1) {
							// --- Take everything ------------------
							netCompLocal.setDataModel(netCompSend.getDataModel());
						} else {
							// --- Just take a specified detail -----
							Object dataModelSend = netCompSend.getDataModel();
							Object dataModelLocal = netCompLocal.getDataModel();
							
							if (dataModelSend instanceof Object[] && dataModelLocal instanceof Object[]) {
								// --- Just take a specified detail -
								Object[] dataModelSendArr = (Object[]) dataModelSend;
								Object[] dataModelLocalArr = (Object[]) dataModelLocal;
								int updateIndex = dmNote.getDataModelPartUpdateIndex();
								dataModelLocalArr[updateIndex] = dataModelSendArr[updateIndex];
							} else {
								// --- Worst Case: Take everything --
								netCompLocal.setDataModel(netCompSend.getDataModel());
							}
						}
						
					}
					// --- Case NetworkComponent - End --------------
					// ----------------------------------------------
					
				} else if (dmNote.isGraphNodeConfiguration()==true) {
					// ----------------------------------------------
					// --- Case GraphNode ---------------------------
					GraphNode graphNodeSend  = dmNote.getGraphNode();
					GraphNode graphNodeLocal = (GraphNode) networkModel.getGraphElement(graphNodeSend.getId());
					if (dmNote.isUseDataModelBase64Encoded()==true) {
						// --- Case Base64 -------------------------- 
						if (dmNote.getDataModelPartUpdateIndex()==-1) {
							// --- Take everything ------------------
							graphNodeLocal.setDataModelBase64(graphNodeSend.getDataModelBase64());
						} else {
							int updateIndex = dmNote.getDataModelPartUpdateIndex();
							String updateString = graphNodeSend.getDataModelBase64().get(updateIndex);
							graphNodeLocal.getDataModelBase64().setElementAt(updateString, updateIndex);
						}
						
					} else {
						// --- Case Object instance -----------------
						if (dmNote.getDataModelPartUpdateIndex()==-1) {
							// --- Take everything ------------------
							graphNodeLocal.setDataModel(graphNodeSend.getDataModel());
						} else {
							// --- Just take a specified detail -----
							Object dataModelSend = graphNodeSend.getDataModel();
							Object dataModelLocal = graphNodeLocal.getDataModel();
							
							if (dataModelSend instanceof Object[] && dataModelLocal instanceof Object[]) {
								// --- Just take a specified detail -
								Object[] dataModelSendArr = (Object[]) dataModelSend;
								Object[] dataModelLocalArr = (Object[]) dataModelLocal;
								int updateIndex = dmNote.getDataModelPartUpdateIndex();
								dataModelLocalArr[updateIndex] = dataModelSendArr[updateIndex];
							} else {
								// --- Worst Case: Take everything --
								graphNodeLocal.setDataModel(graphNodeSend.getDataModel());
							}
						}
					}
					// --- Case GraphNode - End ---------------------
					// ----------------------------------------------
				}
			}
			
		} // end of case separation

	}
	
}
