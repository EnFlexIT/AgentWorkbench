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

import java.util.HashSet;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import agentgui.core.application.Language;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;

/**
 * The Class PasteNetworkModel.
 */
public class PasteNetworkModel extends AbstractUndoableEdit {

	private static final long serialVersionUID = -9041673988115208133L;
	
	private GraphEnvironmentController graphController = null;
	private NetworkModel pastedNetworkModel = null;
	
	/**
	 * Instantiates a new import network model.
	 *
	 * @param graphController the graph controller
	 * @param networkModelPasted the network model pasted
	 */
	public PasteNetworkModel(GraphEnvironmentController graphController, NetworkModel networkModelPasted) {
		this.graphController = graphController;
		this.pastedNetworkModel = networkModelPasted;
	}
	
	/**
	 * Do edit.
	 */
	private void doEdit() {
		
		this.graphController.getNetworkModel().mergeNetworkModel(this.pastedNetworkModel, null, false);
		for (NetworkComponent networkComponentPasted : this.pastedNetworkModel.getNetworkComponents().values()) {
			this.graphController.addAgent(networkComponentPasted);
		}
		
		// --- Inform about changes -------------------------
		this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Merged_With_Supplement_NetworkModel));
		this.graphController.setProjectUnsaved();
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();

		// --- Remove the added components --------------
		HashSet<NetworkComponent> netComps2Remove = new HashSet<NetworkComponent>();
		for (NetworkComponent networkComponentPasted : this.pastedNetworkModel.getNetworkComponents().values()) {
			String netCompID = networkComponentPasted.getId();
			NetworkComponent netCompRemove = this.graphController.getNetworkModel().getNetworkComponent(netCompID);
			netComps2Remove.add(netCompRemove);
			this.graphController.removeAgent(networkComponentPasted);
		}
		this.graphController.getNetworkModel().removeNetworkComponents(netComps2Remove);
		
		NetworkModelNotification  notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Removed);
		notification.setInfoObject(netComps2Remove);
		this.graphController.notifyObservers(notification);
		
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
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Paste from Clipboard");
	}
	
	
}
