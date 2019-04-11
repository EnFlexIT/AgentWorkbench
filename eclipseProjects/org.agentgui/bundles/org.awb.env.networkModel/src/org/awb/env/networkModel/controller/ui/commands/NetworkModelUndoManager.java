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
package org.awb.env.networkModel.controller.ui.commands;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.undo.UndoManager;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.toolbar.CustomToolbarComponentDescription;
import org.awb.env.networkModel.helper.GraphNodePairs;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The Class NetworkModelUndoManager is used for the action / interaction
 * with the NetworkModel in the context of the UI and provides an Swing UndoManager.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkModelUndoManager {
	
	private GraphEnvironmentController graphController;
	private UndoManager undoManager;
	
	/**
	 * Instantiates a new network model action.
	 * 
	 * @param controller the controller
	 */
	public NetworkModelUndoManager(GraphEnvironmentController controller) {
		this.graphController=controller;
	}
	
	/**
	 * Notifies the connected observer of the GraphEnvironmentController.
	 * @param notification the notification
	 */
	private void notifyObservers(NetworkModelNotification notification) {
		this.graphController.notifyObservers(notification);
	}
	
	
	/**
	 * Returns the undo manager.
	 * @return the undoManager
	 */
	public UndoManager getUndoManager() {
		if (undoManager==null) {
			undoManager = new UndoManager();
		}
		return undoManager;
	}
	/**
	 * Undo the last action.
	 */
	public void undo() {
		try {
			this.getUndoManager().undo();	
		} catch (Exception ex) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Can't undo");
			ex.printStackTrace();
		}
	}
	/**
	 * Redo's the last action, if possible.
	 */
	public void redo() {
		try {
			this.getUndoManager().redo();	
		} catch (Exception ex) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Can't redo");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Sets a new network model.
	 * @param networkModel the new network model
	 */
	public void setNetworkModel(NetworkModel networkModel) {
		this.getUndoManager().addEdit(new SetNetworkModel(this.graphController, networkModel));
		this.graphController.setProjectUnsaved();
	}
	/**
	 * Returns the network model.
	 * @return the network model
	 */
	public NetworkModel getNetworkModel() {
		return this.graphController.getNetworkModel();
	}

	/**
	 * Sets the GeneralGraphSettings4MAS.
	 * @param generalGraphSettings4MAS the new general graph settings 4 MAS
	 */
	public void setGeneralGraphSettings4MAS(GeneralGraphSettings4MAS generalGraphSettings4MAS) {
		this.getUndoManager().addEdit(new SetGeneralGraphSettings4MAS(this.graphController, generalGraphSettings4MAS));
	}
	/**
	 * Gets the general graph settings 4 MAS.
	 * @return the general graph settings 4 MAS
	 */
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		return this.graphController.getNetworkModel().getGeneralGraphSettings4MAS();
	}


	/**
	 * Reloads the NetworModel.
	 */
	public void reLoadNetworkModel() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Reload);
		this.notifyObservers(notification);
	}
	/**
	 * Refreshes the NetworkModel visualization.
	 */
	public void refreshNetworkModel() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Repaint);
		this.notifyObservers(notification);
	}
	
	/**
	 * Sets the satellite view visible or not.
	 * @param visible the new satellite view
	 */
	public void setSatelliteView(boolean visible) {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Satellite_View);
		notification.setInfoObject(((Boolean)visible));
		this.notifyObservers(notification);
	}
	/**
	 * Zoom fit to window.
	 */
	public void zoomFit2Window() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Zoom_Fit2Window);
		this.notifyObservers(notification);
	}
	/**
	 * Zoom to the original size.
	 */
	public void zoomOne2One() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Zoom_One2One);
		this.notifyObservers(notification);
	}
	/**
	 * Zoom to the currently selected NetworkComponent.
	 */
	public void zoomNetworkComponent() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Zoom_Component);
		this.notifyObservers(notification);
	}
	/**
	 * Zoom in.
	 */
	public void zoomIn() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Zoom_In);
		this.notifyObservers(notification);
	}
	/**
	 * Zoom out.
	 */
	public void zoomOut() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Zoom_Out);
		this.notifyObservers(notification);
	}
	
	/**
	 * Save the current graph as image.
	 */
	public void saveAsImage() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_ExportGraphAsImage);
		this.notifyObservers(notification);
	}
	
	/**
	 * Sets the graph mouse to transforming mode.
	 */
	public void setGraphMouseTransforming(){
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_GraphMouse_Transforming);
		this.notifyObservers(notification);
	}
	/**
	 * Sets the graph mouse to picking mode.
	 */
	public void setGraphMousePicking() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking);
		this.notifyObservers(notification);
	}
	
	/**
	 * Can be used in order to select a NetworkComponent.
	 * @param networkComponent the network component
	 */
	public void selectNetworkComponent(NetworkComponent networkComponent) {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Select);
		notification.setInfoObject(networkComponent);
		this.notifyObservers(notification);
	}
	
	/**
	 * Informs that a CustomToolbarComponentDescription was added to the {@link GeneralGraphSettings4MAS}
	 * @param customButtonDescription the custom button description
	 */
	public void addCustomToolbarComponentDescription(CustomToolbarComponentDescription customButtonDescription) {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_AddedCustomToolbarComponentDescription);
		notification.setInfoObject(customButtonDescription);
		this.notifyObservers(notification);
	}
	
	/**
	 * Import a network model from a file.
	 */
	public void importNetworkModel() {
		ImportNetworkModel importNM = new ImportNetworkModel(this.graphController);
		if (importNM.isCanceled()==false) {
			this.getUndoManager().addEdit(importNM);	
		}
	}
	/**
	 * Clears the current NetworModel.
	 */
	public void clearNetworkModel() {
		ClearNetworkModel clearNM = new ClearNetworkModel(this.graphController);
		if (clearNM.isCanceled()==false) {
			this.getUndoManager().addEdit(clearNM);
		}
	}
	
	/**
	 * Adds a NetworkComponent to the NetworkModel.
	 *
	 * @param networkComponent the network component
	 * @return the network component
	 */
	public NetworkComponent addNetworkComponent(NetworkComponent networkComponent) {
		
		NetworkComponent newComponent = this.graphController.getNetworkModel().addNetworkComponent(networkComponent); 
		this.graphController.addAgent(networkComponent);
		
		NetworkModelNotification  notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Added);
		notification.setInfoObject(newComponent);
		this.notifyObservers(notification);

		return newComponent;
	}


	/**
	 * Renames a NetworkComponent.
	 *
	 * @param oldCompID the old NetworkComponent ID
	 * @param newCompID the new NetworkComponent ID
	 */
	public void renameNetworkComponent(String oldCompID, String newCompID) {
		this.getUndoManager().addEdit(new RenameNetworkComponent(this.graphController, oldCompID, newCompID));
	}

	/**
	 * Removes the specified NetworkComponent from the current NetworkModel.
	 * @param networkComponent the network component to remove
	 */
	public void removeNetworkComponent(NetworkComponent networkComponent) {
		HashSet<NetworkComponent> compHash = new HashSet<NetworkComponent>();
		compHash.add(networkComponent);
		this.removeNetworkComponents(compHash, true);
	}
	/**
	 * Removes the specified NetworkComponents from the current NetworkModel.
	 * @param networkComponents the network components to remove
	 */
	public void removeNetworkComponents(HashSet<NetworkComponent> networkComponents) {
		this.removeNetworkComponents(networkComponents, true);
	}
	/**
	 * Removes the specified NetworkComponents from the current NetworkModel.
	 * @param networkComponents the network components to remove
	 * @param removeDistributionNodes the remove distribution nodes
	 */
	public void removeNetworkComponents(HashSet<NetworkComponent> networkComponents, boolean removeDistributionNodes) {
		this.getUndoManager().addEdit(new RemoveNetworkComponent(this.graphController, networkComponents, removeDistributionNodes));
	}
	
	/**
	 * Removes all NetworkComponent's from the current NetworkModel, except of the specified ones.
	 *
	 * @param networkComponentsToKeep the network components to keep when deleting network components
	 * @return the hash set of removed NetworkComponents
	 */
	public HashSet<NetworkComponent> removeNetworkComponentsInverse(HashSet<NetworkComponent> networkComponentsToKeep) {
		
		HashSet<NetworkComponent> removedComponents = this.graphController.getNetworkModel().removeNetworkComponentsInverse(networkComponentsToKeep);
		for (NetworkComponent networkComponent : removedComponents) {
			
			this.graphController.removeAgent(networkComponent);
			
			NetworkModelNotification  notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Removed);
			notification.setInfoObject(networkComponent);
			this.notifyObservers(notification);
		}
		return removedComponents;
	}
	

	/**
	 * Merges the supplement NetworkModel with the current NetworkModel with the help of the nodes that are to merge.
	 *
	 * @param supplementNetworkModel the supplement network model
	 * @param node2Merge the node 2 merge
	 */
	public void mergeNetworkModel(NetworkModel supplementNetworkModel, GraphNodePairs node2Merge) {
		this.getUndoManager().addEdit(new MergeNetworkModel(this.graphController, supplementNetworkModel, node2Merge));
	}
	
	/**
	 * Merges the specified {@link GraphNodePairs}.
	 * @param nodes2Merge the nodes 2 merge
	 */
	public void mergeNodes(GraphNodePairs nodes2Merge) {
		this.getUndoManager().addEdit(new MergeNetworkComponents(this.graphController, nodes2Merge));
	}

	/**
	 * Splits the NetworkModel at the specified node.
	 * @param node2SplitAt the node to split at
	 */
	public void splitNetworkModelAtNode(GraphNode node2SplitAt) {
		this.getUndoManager().addEdit(new SplitNetworkComponent(this.graphController, node2SplitAt));
	}
	
	/**
	 * Sets the movement of GraphNodes to the undoManager.
	 *
	 * @param visViewer the current {@link VisualizationViewer}
	 * @param nodesMovedOldPositions the nodes moved old positions
	 */
	public void setGraphNodesMoved(VisualizationViewer<GraphNode,GraphEdge> visViewer, HashMap<String, Point2D> nodesMovedOldPositions) {
		this.getUndoManager().addEdit(new MoveGraphNodes(this.graphController, visViewer, nodesMovedOldPositions));
	}
	
	/**
	 * Sets a paste action to the undo manager.
	 * @param networkModelPasted the network model pasted
	 */
	public void pasteNetworkModel(NetworkModel networkModelPasted) {
		this.getUndoManager().addEdit(new PasteNetworkModel(this.graphController, networkModelPasted));
	}

}
