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

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;

import agentgui.core.application.Language;

/**
 * The class SwitchLayoutAction represents the undoable action to switch between different graph layouts.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SwitchLayoutAction extends AbstractUndoableEdit {

	private static final long serialVersionUID = -7057568320137759472L;

	private GraphEnvironmentController graphController;
	private String newLayoutID;
	private String oldLayoutID;
	
	
	/**
	 * Instantiates a new UndoableEdit for the movements of GraphNode's.
	 *
	 * @param graphController the graph controller
	 * @param newLayoutID the new layout ID
	 */
	public SwitchLayoutAction(GraphEnvironmentController graphController, String newLayoutID) {
		this.graphController = graphController;
		this.newLayoutID = newLayoutID;
		this.oldLayoutID = this.graphController.getNetworkModel().getLayoutID();
		this.switchGraphLayoutTo(newLayoutID);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Switch Graph Layout", Language.EN);
	}

	/**
	 * Switches the graph layout.
	 *
	 * @param oldLayoutID the old layout ID
	 * @param newLayoutID the new layout ID
	 */
	private void switchGraphLayoutTo(String newLayoutID) {
		
		this.graphController.getNetworkModel().setLayoutIdAndExchangeLayoutSettings(newLayoutID);
		
		// --- Notify the controllers observers ---
		this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_LayoutChanged));
		this.graphController.setProjectUnsaved();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		this.switchGraphLayoutTo(this.newLayoutID);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		this.switchGraphLayoutTo(this.oldLayoutID);
	}
	
}
