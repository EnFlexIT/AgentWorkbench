package de.enflexit.expression.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextField;

import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.math.ExpressionTypeMath;

import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * The ExpressionEditorWidget can be used to edit and validate an expression directly,
 * or display a graphical editor dialog to edit an expression in a more comfortable way.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorWidget extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -644272977163977965L;
	
	public static final String EXPRESSION_UPDATED = "Expresison Updated";
	
	private static final Dimension BUTTON_SIZE = new Dimension(26, 26);
	private static final String ICON_PATH_EDIT = "/icons/Edit.png";
	private static final String ICON_PATH_CHECK_FAILED = "/icons/CheckRed.png";
	private static final String ICON_PATH_CHECK_PASSED = "/icons/CheckGreen.png";
	
	private JTextField jTextFieldExpression;
	private JButton jButtonEditor;
	private JButton jButtonValidate;
	
	private Expression expression;
	private ExpressionContext expressionContext;
	
	/**
	 * Instantiates a new expression editor widget.
	 */
	public ExpressionEditorWidget() {
		initialize();
	}
	
	public ExpressionEditorWidget(Expression expression) {
		this.expression = expression;
	}

	/**
	 * Initialize the GUI elements.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jTextFieldExpression = new GridBagConstraints();
		gbc_jTextFieldExpression.insets = new Insets(0, 0, 0, 5);
		gbc_jTextFieldExpression.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldExpression.gridx = 0;
		gbc_jTextFieldExpression.gridy = 0;
		add(getJTextFieldExpression(), gbc_jTextFieldExpression);
		GridBagConstraints gbc_jButtonEditor = new GridBagConstraints();
		gbc_jButtonEditor.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButtonEditor.insets = new Insets(0, 2, 0, 5);
		gbc_jButtonEditor.gridx = 1;
		gbc_jButtonEditor.gridy = 0;
		add(getJButtonEditor(), gbc_jButtonEditor);
		GridBagConstraints gbc_jButtonValidate = new GridBagConstraints();
		gbc_jButtonValidate.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButtonValidate.insets = new Insets(0, 2, 0, 0);
		gbc_jButtonValidate.gridx = 2;
		gbc_jButtonValidate.gridy = 0;
		add(getJButtonValidate(), gbc_jButtonValidate);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonEditor()) {
			ExpressionEditorDialog editorDialog = new ExpressionEditorDialog(null, this.getExpression(), this.getExpressionContext(), true);
			editorDialog.setVisible(true);
			
			if (editorDialog.isCanceled()==false) {
				this.setExpression(editorDialog.getExpression());
			}
			
		} else if (ae.getSource()==this.getJButtonValidate()) {
			this.validateExpression();
		}
	}
	
	/**
	 * Validates the expression, sets the icon of the validation button according to the result.
	 */
	private void validateExpression() {
		if (this.expression!=null && this.expression.hasErrors()==false) {
			this.getJButtonValidate().setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_CHECK_PASSED)));
		} else {
			this.getJButtonValidate().setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_CHECK_FAILED)));
		}
	}
	
	private Expression getExpressionFromTextfield() {
		Expression expression = null;
		String textFieldContent = this.getJTextFieldExpression().getText();
		if (textFieldContent!=null && textFieldContent.isEmpty()==false) {
			expression = new Expression(textFieldContent);
			expression.setExpressionType(ExpressionTypeMath.getInstance());
		}
		return expression;
	}

	private JTextField getJTextFieldExpression() {
		if (jTextFieldExpression == null) {
			jTextFieldExpression = new JTextField();
			jTextFieldExpression.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldExpression.setColumns(10);
			jTextFieldExpression.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					// --- Update the expression when leaving the textfield ---
					ExpressionEditorWidget.this.setExpression(ExpressionEditorWidget.this.getExpressionFromTextfield());
				}
			});
		}
		return jTextFieldExpression;
	}
	private JButton getJButtonEditor() {
		if (jButtonEditor == null) {
			jButtonEditor = new JButton();
			jButtonEditor.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_EDIT)));
			jButtonEditor.setToolTipText("Show expression editor...");
			jButtonEditor.setSize(BUTTON_SIZE);
			jButtonEditor.setPreferredSize(BUTTON_SIZE);
			jButtonEditor.addActionListener(this);
		}
		return jButtonEditor;
	}
	private JButton getJButtonValidate() {
		if (jButtonValidate == null) {
			jButtonValidate = new JButton();
			jButtonValidate.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_CHECK_FAILED)));
			jButtonValidate.setToolTipText("Validate Expression");
			jButtonValidate.setSize(BUTTON_SIZE);
			jButtonValidate.setPreferredSize(BUTTON_SIZE);
			jButtonValidate.addActionListener(this);
		}
		return jButtonValidate;
	}
	
	/**
	 * Sets the expression to be displayed by this widget.
	 * @param expression the new expression
	 */
	public void setExpression(Expression expression) {
		Expression oldExpression = this.expression;
		this.expression = expression;
		if (expression!=null) {
			this.getJTextFieldExpression().setText(expression.getExpressionString());
			this.validateExpression();
		} else {
			this.getJTextFieldExpression().setText(null);
		}
		this.firePropertyChange(EXPRESSION_UPDATED, oldExpression, expression);
		
	}
	/**
	 * Gets the expression.
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}
	
	/**
	 * Sets the current expression context.
	 * @param expressionContext the new expression context
	 */
	public void setExpressionContext(ExpressionContext expressionContext) {
		this.expressionContext = expressionContext;
	}
	/**
	 * Returns the current expression context.
	 * @return the expression context
	 */
	public ExpressionContext getExpressionContext() {
		return expressionContext;
	}
	
}
