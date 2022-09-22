package de.enflexit.expression.gui;

import javax.swing.JDialog;

import de.enflexit.common.swing.JDialogSizeAndPostionController;
import de.enflexit.common.swing.JDialogSizeAndPostionController.JDialogPosition;

public class ExpressionEvaluatorDialog extends JDialog{
	
	private static final long serialVersionUID = -5820516711953973203L;
	
	private ExpressionEvaluatorPanel evaluatorPanel;
	
	public ExpressionEvaluatorDialog() {
		this.initialize();
	}

	private void initialize() {
		this.setTitle("Expression Evaluator");
		this.setContentPane(this.getEvaluatorPanel());
		this.setSize(500, 300);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JDialogSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	}
	
	public ExpressionEvaluatorPanel getEvaluatorPanel() {
		if (evaluatorPanel==null) {
			evaluatorPanel = new ExpressionEvaluatorPanel();
		}
		return evaluatorPanel;
	}
}
