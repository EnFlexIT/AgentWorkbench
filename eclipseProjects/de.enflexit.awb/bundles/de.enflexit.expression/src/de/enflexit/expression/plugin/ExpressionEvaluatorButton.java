package de.enflexit.expression.plugin;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.toolbar.AbstractCustomToolbarComponent;

import de.enflexit.expression.gui.ExpressionEvaluatorDialog;

/**
 * A Toolbar Button that shows the expression evaluator dialog. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEvaluatorButton extends AbstractCustomToolbarComponent implements ActionListener{
	
	private static final String ICON_PATH = "/icons/ConstructionSite.png";
	
	private JButton toolbarButton;
	
	private ExpressionEvaluatorDialog evaluatorDialog;

	public ExpressionEvaluatorButton(GraphEnvironmentController graphController) {
		super(graphController);
	}

	@Override
	public JComponent getCustomComponent() {
		if (toolbarButton==null) {
			toolbarButton = new JButton();
			toolbarButton.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH)));
			toolbarButton.setToolTipText("Expression Evaluator");
			toolbarButton.setPreferredSize(new Dimension(26, 26));
			toolbarButton.addActionListener(this);
		}
		return toolbarButton;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		this.getEvaluatorDialog().setVisible(true);
	}
	
	private ExpressionEvaluatorDialog getEvaluatorDialog() {
		if (evaluatorDialog==null) {
			evaluatorDialog = new ExpressionEvaluatorDialog();
		}
		return evaluatorDialog;
	}

}
