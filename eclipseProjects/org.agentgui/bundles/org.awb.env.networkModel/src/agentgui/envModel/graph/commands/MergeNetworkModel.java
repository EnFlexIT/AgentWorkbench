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
import agentgui.envModel.graph.networkModel.GraphNodePairs;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;

/**
 * The Class AddNetworkComponent.
 */
public class MergeNetworkModel extends AbstractUndoableEdit {

	private static final long serialVersionUID = -4772137855514690242L;

	private GraphEnvironmentController graphController = null;

	private NetworkModel suppNetModel = null;
	private GraphNodePairs nodes2Merge = null;

	/**
	 * Instantiates a new merge network model.
	 *
	 * @param graphController the graph controller
	 * @param supplementNetworkModel the supplement network model
	 * @param node2Merge the merge description
	 */
	public MergeNetworkModel(GraphEnvironmentController graphController, NetworkModel supplementNetworkModel, GraphNodePairs node2Merge) {
		super();
		this.graphController = graphController;
		
		this.suppNetModel = this.graphController.getNetworkModel().adjustNameDefinitionsOfSupplementNetworkModel(supplementNetworkModel);
		this.nodes2Merge = node2Merge;
		
		this.doEdit();
	}
	
	/**
	 * Do edit.
	 */
	private void doEdit() {
		
		// --- 1. Merge NetworkModels -------------------------------
		this.nodes2Merge = this.graphController.getNetworkModel().mergeNetworkModel(this.suppNetModel, this.nodes2Merge);
		
		// --- 2. Add the Agent definitions -------------------------
		for(NetworkComponent networkComponent : this.suppNetModel.getNetworkComponents().values()) {
			this.graphController.addAgent(networkComponent);
		}
		
		this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Merged_With_Supplement_NetworkModel));
		this.graphController.setProjectUnsaved();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Netzwerkkomponente(n) hinzufügen");
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
		
		this.graphController.getNetworkModel().mergeNodesRevert(this.nodes2Merge);
		
		for (NetworkComponent networkComponent: this.suppNetModel.getNetworkComponents().values()) {

			NetworkComponent netComp2Remove = this.graphController.getNetworkModel().getNetworkComponent(networkComponent.getId());
			this.graphController.getNetworkModel().removeNetworkComponent(netComp2Remove);
			
			NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Removed);
			notification.setInfoObject(netComp2Remove);
			this.graphController.notifyObservers(notification);
		}
		this.graphController.setProjectUnsaved();
	}
	
}
