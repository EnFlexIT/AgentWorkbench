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
package agentgui.envModel.graph.commands;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import agentgui.core.application.Language;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;

/**
 * This action can be used in order to rename a NetworkComponent.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class RenameNetworkComponent extends AbstractUndoableEdit {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4772137855514690242L;

	/** The graph controller. */
	private GraphEnvironmentController graphController = null;
	
	/** The old component id. */
	private String oldComponentID = null;
	
	/** The new component id. */
	private String newComponentID = null;
	
	
	/**
	 * Instantiates a new action to rename a NetworkComponent.
	 *
	 * @param graphController the graph controller
	 * @param oldComponentID the old name of the NetworkComponent
	 * @param newComponentID the new name of the NetworkComponent
	 */
	public RenameNetworkComponent(GraphEnvironmentController graphController, String oldComponentID, String newComponentID) {
		super();
		this.graphController = graphController;
		this.oldComponentID = oldComponentID;
		this.newComponentID = newComponentID;
		this.doEdit();
	}

	/**
	 * Do the wished edit.
	 */
	private void doEdit() {
		this.graphController.getNetworkModel().renameNetworkComponent(this.oldComponentID, this.newComponentID);
		this.graphController.renameAgent(this.oldComponentID, this.newComponentID);
		this.graphController.setProjectUnsaved();

		NetworkComponent netComp = this.graphController.getNetworkModel().getNetworkComponent(this.newComponentID);
		NetworkComponentRenamed renamed = new NetworkComponentRenamed(netComp, oldComponentID, newComponentID);

		NetworkModelNotification  notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Renamed);
		notification.setInfoObject(renamed);
		this.graphController.notifyObservers(notification);

	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Netzwerkkomponente umbenennen");
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		this.doEdit();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		this.graphController.getNetworkModel().renameNetworkComponent(this.newComponentID, this.oldComponentID);
		this.graphController.renameAgent(this.newComponentID, this.oldComponentID);
		this.graphController.setProjectUnsaved();

		NetworkComponent netComp = this.graphController.getNetworkModel().getNetworkComponent(this.oldComponentID);
		NetworkComponentRenamed renamed = new NetworkComponentRenamed(netComp, newComponentID, oldComponentID);

		NetworkModelNotification  notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Renamed);
		notification.setInfoObject(renamed);
		this.graphController.notifyObservers(notification);
		
	}

	
	/**
	 * The Class RenamedNetworkComponent.
	 */
	public class NetworkComponentRenamed {
		
		private NetworkComponent networkComponent = null;
		private String oldNetworkComponentID = null;
		private String newNetworkComponentID = null;
		
		/**
		 * Instantiates a new renamed network component.
		 * @param networkComponent the network component
		 */
		public NetworkComponentRenamed(NetworkComponent networkComponent, String oldCompID, String newCompID) {
			this.networkComponent = networkComponent;
			this.oldNetworkComponentID = oldCompID;
			this.newNetworkComponentID = newCompID;
		}
		
		/**
		 * Gets the network component.
		 * @return the networkComponent
		 */
		public NetworkComponent getNetworkComponent() {
			return networkComponent;
		}
		/**
		 * Sets the network component.
		 * @param networkComponent the networkComponent to set
		 */
		public void setNetworkComponent(NetworkComponent networkComponent) {
			this.networkComponent = networkComponent;
		}
		
		/**
		 * Gets the old network component id.
		 * @return the oldNetworkComponentID
		 */
		public String getOldNetworkComponentID() {
			return oldNetworkComponentID;
		}
		/**
		 * Sets the old network component id.
		 * @param oldNetworkComponentID the oldNetworkComponentID to set
		 */
		public void setOldNetworkComponentID(String oldNetworkComponentID) {
			this.oldNetworkComponentID = oldNetworkComponentID;
		}
		
		/**
		 * Gets the new network component id.
		 * @return the newNetworkComponentID
		 */
		public String getNewNetworkComponentID() {
			return newNetworkComponentID;
		}
		/**
		 * Sets the new network component id.
		 * @param newNetworkComponentID the newNetworkComponentID to set
		 */
		public void setNewNetworkComponentID(String newNetworkComponentID) {
			this.newNetworkComponentID = newNetworkComponentID;
		}
	} // end sub class
	
}
