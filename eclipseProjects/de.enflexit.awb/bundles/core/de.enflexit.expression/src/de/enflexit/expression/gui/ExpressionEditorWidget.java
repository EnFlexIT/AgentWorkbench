package de.enflexit.expression.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionValidatorExtension;

/**
 * The ExpressionEditorWidget can be used to edit and validate an expression directly,
 * or display a graphical editor dialog to edit an expression in a more comfortable way.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorWidget extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -644272977163977965L;
	
	public static final String EXPRESSION_UPDATED = "Expresison Updated";
	
	private static final Dimension BUTTON_SIZE = new Dimension(26, 26);
	
	private JTextField jTextFieldExpression;
	private JButton jButtonEditor;
	private JButton jButtonValidate;
	
	private Expression expression;
	private ExpressionContext expressionContext;
	
	private ExpressionServiceFilter libraryFilter;
	
	private ArrayList<ExpressionValidatorExtension> validatorExtensions;
	
	/**
	 * Instantiates a new expression editor widget.
	 * @param expression the expression to edit
	 * @param expressionContext the expression context
	 */
	public ExpressionEditorWidget(Expression expression, ExpressionContext expressionContext) {
		this.expression = expression;
		this.expressionContext = expressionContext;
		this.initialize();
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
		gbc_jButtonEditor.insets = new Insets(0, 0, 0, 5);
		gbc_jButtonEditor.gridx = 1;
		gbc_jButtonEditor.gridy = 0;
		add(getJButtonEditor(), gbc_jButtonEditor);
		GridBagConstraints gbc_jButtonValidate = new GridBagConstraints();
		gbc_jButtonValidate.fill = GridBagConstraints.HORIZONTAL;
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
			
			Window ownerWindow = OwnerDetection.getOwnerWindowForComponent(this);
			ExpressionEditorDialog editorDialog = new ExpressionEditorDialog(ownerWindow, this.getExpression(), this.getExpressionContext(), true);
			if (ownerWindow!=null) {
				double scaleWidth = 1.15;
				double scaleHeight = 0.95;
				editorDialog.setSize((int)(ownerWindow.getWidth() * scaleWidth), (int)(ownerWindow.getHeight() * scaleHeight));
				WindowSizeAndPostionController.setJDialogPositionOnScreen(editorDialog, JDialogPosition.ParentCenter);
			}
			editorDialog.setExpressionServiceFilter(this.getExpressionLibraryFilter());
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
		
		boolean isValid;
		
		if (expression==null) {
			isValid = false;
		} else {
			// --- Check for syntax errors ----------------
			isValid = !expression.hasErrors();
			if (isValid==true) {
				// --- Perform additional validations, if configured
				for (ExpressionValidatorExtension validatorExtension : this.getValidatorExtensions()) {
					if (validatorExtension.validate()==false) {
						isValid = false;
						break;
					}
				}
			} else {
				System.out.println("[" + this.getClass().getSimpleName() + "] The expression " + this.getExpression().getExpressionString() + " has syntax errors!");
			}
		}
		
		this.getJButtonValidate().setIcon(ImageHelper.getImageIcon(isValid ? "CheckGreen.png" : "CheckRed.png"));
	}
	
	/**
	 * Gets the expression from the text field by parsing the content.
	 * @return the expression from the text field
	 */
	private Expression getExpressionFromTextfield() {
		Expression expression = null;
		String textFieldContent = this.getJTextFieldExpression().getText();
		if (textFieldContent!=null && textFieldContent.isEmpty()==false) {
			expression = Expression.parse(textFieldContent);
		} else {
			expression = null;
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
			jButtonEditor.setIcon(ImageHelper.getImageIcon("Edit.png"));
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
			jButtonValidate.setIcon(ImageHelper.getImageIcon("CheckRed.png"));
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

	/**
	 * Gets the expression library filter.
	 * @return the expression library filter
	 */
	public ExpressionServiceFilter getExpressionLibraryFilter() {
		return libraryFilter;
	}

	/**
	 * Sets the expression library filter.
	 * @param libraryFilter the new expression library filter
	 */
	public void setExpressionLibraryFilter(ExpressionServiceFilter libraryFilter) {
		this.libraryFilter = libraryFilter;
	}
	
	/**
	 * Gets the registered validator extensions.
	 * @return the validator extensions
	 */
	private ArrayList<ExpressionValidatorExtension> getValidatorExtensions() {
		if (validatorExtensions==null) {
			validatorExtensions = new ArrayList<ExpressionValidatorExtension>();
		}
		return validatorExtensions;
	}
	
	/**
	 * Adds a new validator extension.
	 * @param validatorExtension the validator extension
	 */
	public void addValidatorExtension(ExpressionValidatorExtension validatorExtension) {
		this.getValidatorExtensions().add(validatorExtension);
	}
	
	/**
	 * Removes a validator extension.
	 * @param validatorExtension the validator extension
	 */
	public void removeValidatorExtension(ExpressionValidatorExtension validatorExtension) {
		this.getValidatorExtensions().remove(validatorExtension);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.getJTextFieldExpression().setEnabled(enabled);
		this.getJButtonEditor().setEnabled(enabled);
		this.getJButtonValidate().setEnabled(enabled);
	}
	
}
