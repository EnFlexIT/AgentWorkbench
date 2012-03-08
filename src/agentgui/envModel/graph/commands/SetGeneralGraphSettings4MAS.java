package agentgui.envModel.graph.commands;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import agentgui.core.application.Language;
import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;


public class SetGeneralGraphSettings4MAS extends AbstractUndoableEdit {

	private static final long serialVersionUID = -4772137855514690242L;

	private GraphEnvironmentController graphController = null;
	private GeneralGraphSettings4MAS newGeneralGraphSettings4MAS = null;
	
	private NetworkModel oldNetworkModel = null;
	
	
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
	}
	
	@Override
	public String getPresentationName() {
		return Language.translate("Komponenten-Definition bearbeiten");
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		this.doEdit();
	}
	
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		this.graphController.setEnvironmentModel(oldNetworkModel.getCopy());
	}
	
	
}
