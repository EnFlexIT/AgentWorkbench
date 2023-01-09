package de.enflexit.expression.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionParser;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * This class implements the upper left part of the expression editor, 
 * which contains the text-based editor. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorTextPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 7546357806464607528L;
	
	public static final String EXPRESSION_PARSED = "ExpressionParsed";
	
	private JTextArea jTextAreaExpression;
	private JCheckBox jCheckBoxAutoParse;
	private JButton jButtonParse;
	
	private Expression expression;
	private JLabel jLabelExpression;
	private JTextField jTextFieldResult;
	
	/**
	 * Instantiates a new expression editor text panel.
	 */
	public ExpressionEditorTextPanel() {
		super();
		this.initialize();
	}
	
	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		
		GridBagLayout gbl_this = new GridBagLayout();
		gbl_this.columnWidths = new int[]{0, 0, 0, 0};
		gbl_this.rowHeights = new int[]{0, 0, 0, 0};
		gbl_this.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_this.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gbl_this);
		
		GridBagConstraints gbc_jLabelExpression = new GridBagConstraints();
		gbc_jLabelExpression.gridwidth = 2;
		gbc_jLabelExpression.anchor = GridBagConstraints.WEST;
		gbc_jLabelExpression.gridx = 0;
		gbc_jLabelExpression.gridy = 0;
		add(getJLabelExpression(), gbc_jLabelExpression);
		GridBagConstraints gbc_jTextAreaExpression = new GridBagConstraints();
		gbc_jTextAreaExpression.gridwidth = 3;
		gbc_jTextAreaExpression.insets = new Insets(5, 0, 0, 0);
		gbc_jTextAreaExpression.fill = GridBagConstraints.BOTH;
		gbc_jTextAreaExpression.gridx = 0;
		gbc_jTextAreaExpression.gridy = 1;
		this.add(getJTextAreaExpression(), gbc_jTextAreaExpression);
		GridBagConstraints gbc_jButtonParse = new GridBagConstraints();
		gbc_jButtonParse.insets = new Insets(5, 0, 5, 0);
		gbc_jButtonParse.gridx = 0;
		gbc_jButtonParse.gridy = 2;
		this.add(getJButtonParse(), gbc_jButtonParse);
		GridBagConstraints gbc_jCheckBoxAutoParse = new GridBagConstraints();
		gbc_jCheckBoxAutoParse.insets = new Insets(5, 10, 5, 0);
		gbc_jCheckBoxAutoParse.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxAutoParse.gridx = 1;
		gbc_jCheckBoxAutoParse.gridy = 2;
		this.add(getJCheckBoxAutoParse(), gbc_jCheckBoxAutoParse);
		GridBagConstraints gbc_jTextFieldResult = new GridBagConstraints();
		gbc_jTextFieldResult.insets = new Insets(5, 0, 0, 5);
		gbc_jTextFieldResult.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldResult.gridx = 2;
		gbc_jTextFieldResult.gridy = 2;
		add(getJTextFieldResult(), gbc_jTextFieldResult);
	}
	
	private JLabel getJLabelExpression() {
		if (jLabelExpression == null) {
			jLabelExpression = new JLabel("Expression");
			jLabelExpression.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelExpression;
	}
	private JTextArea getJTextAreaExpression() {
		if (jTextAreaExpression == null) {
			jTextAreaExpression = new JTextArea();
		}
		return jTextAreaExpression;
	}
	/**
	 * Highlights a sub-expression of the current expression by selecting it in the 
	 * text area. If the provided expression is null, the selection will be cleared.
	 * @param subExpression the sub expression
	 */
	protected void highlightSubExpression(Expression subExpression) {
		// --- Default: No selection --------------------------------
		int selectFrom = 0;
		int selectTo = 0;
		
		if (subExpression!=null) {
			// --- Find the position of the sub-expression ----------
			selectFrom = this.getJTextAreaExpression().getText().indexOf(subExpression.getExpressionString());
			selectTo = selectFrom + subExpression.getExpressionString().length();
		}
		
		// --- Set/clear the selection --------------------------
		this.getJTextAreaExpression().requestFocus();
		this.getJTextAreaExpression().select(selectFrom, selectTo);
	}
	
	private JButton getJButtonParse() {
		if (jButtonParse == null) {
			jButtonParse = new JButton("Parse");
			jButtonParse.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonParse.addActionListener(this);
		}
		return jButtonParse;
	}
	private JCheckBox getJCheckBoxAutoParse() {
		if (jCheckBoxAutoParse == null) {
			jCheckBoxAutoParse = new JCheckBox("Auto parse");
			jCheckBoxAutoParse.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxAutoParse.addActionListener(this);
		}
		return jCheckBoxAutoParse;
	}
	private JTextField getJTextFieldResult() {
		if (jTextFieldResult == null) {
			jTextFieldResult = new JTextField();
			jTextFieldResult.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldResult.setColumns(10);
		}
		return jTextFieldResult;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonParse()) {
			String expressionString = this.getJTextAreaExpression().getText();
			this.expression = ExpressionParser.parse(expressionString);
			this.firePropertyChange(EXPRESSION_PARSED, null, this.expression);
			if (this.expression!=null) {
				this.getJTextFieldResult().setText(this.expression.getExpressionResult().toString());
			}
		}
	}

	/**
	 * Sets the expression.
	 * @param expression the new expression
	 */
	public void setExpression(Expression expression) {
		this.expression = expression;
		this.getJTextAreaExpression().setText(expression.getExpressionString());
	}
	/**
	 * Gets the expression.
	 * @return the expression
	 */
	public Expression getExpression() {
		return this.expression;
	}

	
}
