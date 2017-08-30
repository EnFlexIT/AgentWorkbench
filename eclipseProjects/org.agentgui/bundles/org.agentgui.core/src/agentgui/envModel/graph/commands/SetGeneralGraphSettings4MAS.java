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
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;


/**
 * The Class SetGeneralGraphSettings4MAS.
 */
public class SetGeneralGraphSettings4MAS extends AbstractUndoableEdit {

	private static final long serialVersionUID = -4772137855514690242L;

	private GraphEnvironmentController graphController = null;
	private GeneralGraphSettings4MAS newGeneralGraphSettings4MAS = null;
	
	private NetworkModel oldNetworkModel = null;
	
	
	/**
	 * Instantiates a new sets the general graph settings4 mas.
	 *
	 * @param graphController the graph controller
	 * @param newGeneralGraphSettings4MAS the new general graph settings4 mas
	 */
	public SetGeneralGraphSettings4MAS(GraphEnvironmentController graphController, GeneralGraphSettings4MAS newGeneralGraphSettings4MAS) {
		super();
		this.graphController = graphController;
		this.newGeneralGraphSettings4MAS = newGeneralGraphSettings4MAS;
		this.oldNetworkModel = this.graphController.getNetworkModel().getCopy();
		this.doEdit();
	}
	
	/**
	 * Do the wished edit.
	 */
	private void doEdit() {
		this.graphController.getNetworkModel().setGeneralGraphSettings4MAS(this.newGeneralGraphSettings4MAS);
		this.graphController.validateNetworkComponentAndAgents2Start();
		this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_ComponentTypeSettingsChanged));
		this.graphController.setProjectUnsaved();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Komponenten-Definition bearbeiten");
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
		this.graphController.setDisplayEnvironmentModel(oldNetworkModel.getCopy());
	}
	
	
}
