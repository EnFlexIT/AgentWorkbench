package agentgui.envModel.graph.commands;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import agentgui.core.application.Language;
import agentgui.envModel.graph.networkModel.NetworkModelAdapter;


public class AddNetworkComponent extends AbstractUndoableEdit {

	private static final long serialVersionUID = -4772137855514690242L;

	private NetworkModelAdapter networkModelAdapter = null;
	
	public AddNetworkComponent(NetworkModelAdapter networkModelAdapter) {
		super();
		this.networkModelAdapter = networkModelAdapter;
		
		
		
	}
	
	@Override
	public String getPresentationName() {
		return Language.translate("Netzwerkkomponente hinzufügen");
	}

	
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		
	}
	
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		
	}
	
	
}
