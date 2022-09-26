package de.enflexit.expression.gui;

import javax.swing.JDialog;

import de.enflexit.common.swing.JDialogSizeAndPostionController;
import de.enflexit.common.swing.JDialogSizeAndPostionController.JDialogPosition;

public class ExpressionEditorDialog extends JDialog{
	
	private static final long serialVersionUID = -5820516711953973203L;
	
	private ExpressionEditorPanel evaluatorPanel;
	
	public ExpressionEditorDialog() {
		this.initialize();
	}

	private void initialize() {
		this.setTitle("Expression Evaluator");
		this.setContentPane(this.getEvaluatorPanel());
		this.setSize(500, 300);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JDialogSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	}
	
	public ExpressionEditorPanel getEvaluatorPanel() {
		if (evaluatorPanel==null) {
			evaluatorPanel = new ExpressionEditorPanel();
		}
		return evaluatorPanel;
	}
}
