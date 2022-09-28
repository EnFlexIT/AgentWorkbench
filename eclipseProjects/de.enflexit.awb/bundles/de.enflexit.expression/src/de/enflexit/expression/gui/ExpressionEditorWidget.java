package de.enflexit.expression.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;

import de.enflexit.common.BundleHelper;
import de.enflexit.expression.Expression;

import java.awt.Insets;
import javax.swing.JButton;

/**
 * The ExpressionEditorWidget can be used to edit and validate an expression directly,
 * or display a graphical editor dialog to edit an expression in a more comfortable way.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorWidget extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -644272977163977965L;
	
	private JLabel jLabelExpression;
	private JTextField jTextFieldExpression;
	private JButton jButtonEditor;
	private JButton jButtonValidate;
	
	private ExpressionEditorDialog editorDialog;

	/**
	 * Instantiates a new expression editor widget.
	 */
	public ExpressionEditorWidget() {
		initialize();
	}
	
	/**
	 * Initialize the GUI elements.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelExpression = new GridBagConstraints();
		gbc_jLabelExpression.insets = new Insets(0, 0, 0, 5);
		gbc_jLabelExpression.anchor = GridBagConstraints.EAST;
		gbc_jLabelExpression.gridx = 0;
		gbc_jLabelExpression.gridy = 0;
		add(getJLabelExpression(), gbc_jLabelExpression);
		GridBagConstraints gbc_jTextFieldExpression = new GridBagConstraints();
		gbc_jTextFieldExpression.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldExpression.gridx = 1;
		gbc_jTextFieldExpression.gridy = 0;
		add(getJTextFieldExpression(), gbc_jTextFieldExpression);
		GridBagConstraints gbc_jButtonEditor = new GridBagConstraints();
		gbc_jButtonEditor.insets = new Insets(0, 2, 0, 0);
		gbc_jButtonEditor.gridx = 2;
		gbc_jButtonEditor.gridy = 0;
		add(getJButtonEditor(), gbc_jButtonEditor);
		GridBagConstraints gbc_jButtonValidate = new GridBagConstraints();
		gbc_jButtonValidate.insets = new Insets(0, 2, 0, 0);
		gbc_jButtonValidate.gridx = 3;
		gbc_jButtonValidate.gridy = 0;
		add(getJButtonValidate(), gbc_jButtonValidate);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonEditor()) {
			this.getEditorDialog().setExpression(this.getExpressionFromTextfield());
			this.getEditorDialog().setVisible(true);
			
			//TODO handle result expression from the dialog
		} else if (ae.getSource()==this.getJButtonValidate()) {
			//TODO validate expression
		}
	}
	
	private Expression getExpressionFromTextfield() {
		Expression expression = null;
		String textFieldContent = this.getJTextFieldExpression().getText();
		if (textFieldContent!=null && textFieldContent.isEmpty()==false) {
			expression = new Expression(textFieldContent);
		}
		return expression;
	}

	private JLabel getJLabelExpression() {
		if (jLabelExpression == null) {
			jLabelExpression = new JLabel("Expression");
			jLabelExpression.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelExpression;
	}
	private JTextField getJTextFieldExpression() {
		if (jTextFieldExpression == null) {
			jTextFieldExpression = new JTextField();
			jTextFieldExpression.setColumns(10);
		}
		return jTextFieldExpression;
	}
	private JButton getJButtonEditor() {
		if (jButtonEditor == null) {
			jButtonEditor = new JButton();
			jButtonEditor.setIcon(BundleHelper.getImageIcon("Edit.png"));
			jButtonEditor.setToolTipText("Show expression editor...");
			jButtonEditor.setSize(new Dimension(26, 26));
			jButtonEditor.setPreferredSize(new Dimension(26, 26));
			jButtonEditor.addActionListener(this);
		}
		return jButtonEditor;
	}
	private JButton getJButtonValidate() {
		if (jButtonValidate == null) {
			jButtonValidate = new JButton("Validate");
			jButtonValidate.setIcon(BundleHelper.getImageIcon("CheckRed.png"));
			jButtonValidate.setToolTipText("Validate Expression");
			jButtonValidate.setSize(new Dimension(26, 26));
			jButtonValidate.setPreferredSize(new Dimension(26, 26));
			jButtonValidate.addActionListener(this);
		}
		return jButtonValidate;
	}
	
	private ExpressionEditorDialog getEditorDialog() {
		if (editorDialog==null) {
			editorDialog = new ExpressionEditorDialog();
		}
		return editorDialog;
	}
}
