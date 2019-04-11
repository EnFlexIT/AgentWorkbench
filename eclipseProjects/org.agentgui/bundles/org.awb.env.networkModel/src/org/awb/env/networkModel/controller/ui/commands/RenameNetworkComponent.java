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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;

import agentgui.core.application.Language;

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
		RenamedNetworkComponent renamed = new RenamedNetworkComponent(netComp, oldComponentID, newComponentID);

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
		RenamedNetworkComponent renamed = new RenamedNetworkComponent(netComp, newComponentID, oldComponentID);

		NetworkModelNotification  notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Renamed);
		notification.setInfoObject(renamed);
		this.graphController.notifyObservers(notification);
		
	}
	
}
