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

import javax.swing.JOptionPane;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The AbstractUndoableEdit 'ClearNetworkModel' clears the current NetworkModel.
 */
public class ClearNetworkModel extends AbstractUndoableEdit {

	private static final long serialVersionUID = -409810728677898514L;

	private GraphEnvironmentController graphController = null;
	private NetworkModel oldNetworkModel = null;
	private boolean canceled = false;

	
	public ClearNetworkModel(GraphEnvironmentController graphController) {
		
		this.graphController = graphController;
		this.oldNetworkModel = this.graphController.getNetworkModel().getCopy();
		
		// --- Ask user if the NetworkModel should be cleared -----
		int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(),
					 Language.translate("Are you sure that you want to clear the graph?", Language.EN), 
					 Language.translate("Confirmation", Language.EN), 
					 JOptionPane.YES_NO_OPTION);

		if (answer==JOptionPane.YES_OPTION) {
			this.doEdit();
		} else {
			this.setCanceled(true);
		}
	}
	
	private void doEdit() {
		this.graphController.getAgents2Start().clear();
		this.graphController.setDisplayEnvironmentModel(null);
		this.graphController.setProjectUnsaved();
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
		this.graphController.setDisplayEnvironmentModel(this.oldNetworkModel);
		this.graphController.setProjectUnsaved();
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Netzwerkmodell leeren");
	}
	
	/**
	 * Sets if this action was canceled.
	 * @param canceled the new canceled
	 */
	private void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	/**
	 * Returns true, if this action was canceled.
	 * @return true, if successful
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
}
