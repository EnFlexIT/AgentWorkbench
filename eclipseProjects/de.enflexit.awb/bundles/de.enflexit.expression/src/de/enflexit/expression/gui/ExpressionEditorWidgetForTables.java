package de.enflexit.expression.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import de.enflexit.expression.Expression;
import de.enflexit.expression.math.ExpressionTypeMath;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * Reduced version of the {@link ExpressionEditorWidget} for the use as a table cell editor. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorWidgetForTables extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 6056053207940611095L;
	
	private JTextField jTextFieldExpression;
	private JButton jButtonExpressionEditor;
	
	private Expression expression;
	
	private TableCellEditor parentEditor;
	
	public ExpressionEditorWidgetForTables(TableCellEditor parentEditor) {
		this.parentEditor = parentEditor;
		this.initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{80, 33, 0};
		gridBagLayout.rowHeights = new int[]{14, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jTextFieldExpression = new GridBagConstraints();
		gbc_jTextFieldExpression.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldExpression.anchor = GridBagConstraints.NORTH;
		gbc_jTextFieldExpression.gridx = 0;
		gbc_jTextFieldExpression.gridy = 0;
		add(getJTextFieldExpression(), gbc_jTextFieldExpression);
		GridBagConstraints gbc_jButtonExpressionEditor = new GridBagConstraints();
		gbc_jButtonExpressionEditor.anchor = GridBagConstraints.WEST;
		gbc_jButtonExpressionEditor.gridx = 1;
		gbc_jButtonExpressionEditor.gridy = 0;
		add(getJButtonExpressionEditor(), gbc_jButtonExpressionEditor);
	}

	private JTextField getJTextFieldExpression() {
		if (jTextFieldExpression == null) {
			jTextFieldExpression = new JTextField();
			jTextFieldExpression.setOpaque(true);
			jTextFieldExpression.setBorder(BorderFactory.createEmptyBorder());
			jTextFieldExpression.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					// --- Update the expression when leaving the textfield ---
					ExpressionEditorWidgetForTables.this.expression = ExpressionEditorWidgetForTables.this.setExpressionAccordingToTextfield();
				}
			});
			
			jTextFieldExpression.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent ke) {
					if (ke.getKeyCode()==KeyEvent.VK_ENTER) {
						ExpressionEditorWidgetForTables.this.setExpression(ExpressionEditorWidgetForTables.this.setExpressionAccordingToTextfield());
						ExpressionEditorWidgetForTables.this.parentEditor.stopCellEditing();
					}
				}
			});
		}
		return jTextFieldExpression;
	}
	
	private JButton getJButtonExpressionEditor() {
		if (jButtonExpressionEditor == null) {
			jButtonExpressionEditor = new JButton();
//			jButtonExpressionEditor.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_EDIT_BUTTON)));
			jButtonExpressionEditor.setToolTipText("Open Expression Editor");
			jButtonExpressionEditor.addActionListener(this);
		}
		return jButtonExpressionEditor;
	}
	
	/**
	 * Sets the current expression according to the text field.
	 * @return the expression
	 */
	private Expression setExpressionAccordingToTextfield() {
		Expression expression = null;
		String textFieldContent = this.getJTextFieldExpression().getText();
		if (textFieldContent!=null && textFieldContent.isEmpty()==false) {
			expression = new Expression(textFieldContent);
			expression.setExpressionType(ExpressionTypeMath.getInstance());
		}
		return expression;
	}
	
	/**
	 * Gets the expression.
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}
	/**
	 * Sets the expression.
	 * @param expression the new expression
	 */
	public void setExpression(Expression expression) {
		this.expression = expression;
		if (expression!=null) {
			this.getJTextFieldExpression().setText(expression.getExpressionString());
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonExpressionEditor()) {
			ExpressionEditorDialog editorDialog = new ExpressionEditorDialog(null, this.getExpression(), true);
			editorDialog.setVisible(true);
			
			if (editorDialog.isCanceled()==false) {
				this.setExpression(editorDialog.getExpression());
			}
			
		}
	}
}
