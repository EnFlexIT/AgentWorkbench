package de.enflexit.expression.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionParser;
import de.enflexit.expression.ExpressionResult;

/**
 * This class implements the upper left part of the expression editor, 
 * which contains the text-based editor. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorTextPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 7546357806464607528L;
	
	public static final String EXPRESSION_PARSED = "ExpressionParsed";
	public static final String EXPRESSION_EVALUATED = "ExpressionEvaluated";
	public static final String EXPRESSION_PLACEHOLDER = "<EXP>";
	
	private Expression expression;
	private ExpressionContext expressionContext;
	
	private JLabel jLabelExpression;
	private JScrollPane jScrollBarExpression;
	private JTextArea jTextAreaExpression;
	private boolean isCheckForExpressionPlaceholder = true;
	
	private JButton jButtonParse;
	private JCheckBox jCheckBoxAutoParse;
	
	private JTextField jTextFieldResult;
	private JLabel jLabelResultType;
	
	
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
		gbl_this.columnWidths = new int[]{140, 0, 0, 0, 0};
		gbl_this.rowHeights = new int[]{0, 0, 0, 0};
		gbl_this.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_this.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gbl_this);
		
		GridBagConstraints gbc_jLabelExpression = new GridBagConstraints();
		gbc_jLabelExpression.insets = new Insets(0, 5, 0, 0);
		gbc_jLabelExpression.anchor = GridBagConstraints.WEST;
		gbc_jLabelExpression.gridx = 0;
		gbc_jLabelExpression.gridy = 0;
		this.add(this.getJLabelExpression(), gbc_jLabelExpression);
		GridBagConstraints gbc_jCheckBoxAutoParse = new GridBagConstraints();
		gbc_jCheckBoxAutoParse.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxAutoParse.gridx = 2;
		gbc_jCheckBoxAutoParse.gridy = 0;
		this.add(this.getJCheckBoxAutoParse(), gbc_jCheckBoxAutoParse);
		GridBagConstraints gbc_jButtonParse = new GridBagConstraints();
		gbc_jButtonParse.insets = new Insets(0, 5, 0, 0);
		gbc_jButtonParse.gridx = 3;
		gbc_jButtonParse.gridy = 0;
		this.add(this.getJButtonParse(), gbc_jButtonParse);
		GridBagConstraints gbc_jTextAreaExpression = new GridBagConstraints();
		gbc_jTextAreaExpression.gridwidth = 4;
		gbc_jTextAreaExpression.fill = GridBagConstraints.BOTH;
		gbc_jTextAreaExpression.gridx = 0;
		gbc_jTextAreaExpression.gridy = 1;
		this.add(this.getJScrollPaneExpression(), gbc_jTextAreaExpression);
		GridBagConstraints gbc_jLabelResultType = new GridBagConstraints();
		gbc_jLabelResultType.insets = new Insets(2, 5, 5, 0);
		gbc_jLabelResultType.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelResultType.gridx = 0;
		gbc_jLabelResultType.gridy = 2;
		this.add(this.getJLabelResultType(), gbc_jLabelResultType);
		GridBagConstraints gbc_jTextFieldResult = new GridBagConstraints();
		gbc_jTextFieldResult.gridwidth = 3;
		gbc_jTextFieldResult.insets = new Insets(2, 20, 5, 5);
		gbc_jTextFieldResult.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldResult.gridx = 1;
		gbc_jTextFieldResult.gridy = 2;
		this.add(this.getJTextFieldResult(), gbc_jTextFieldResult);
	}
	
	private JLabel getJLabelExpression() {
		if (jLabelExpression == null) {
			jLabelExpression = new JLabel("Expression");
			jLabelExpression.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelExpression;
	}
	private JScrollPane getJScrollPaneExpression() {
		if (jScrollBarExpression==null) {
			jScrollBarExpression = new JScrollPane(this.getJTextAreaExpression());
		}
		return jScrollBarExpression;
	}
	private JTextArea getJTextAreaExpression() {
		if (jTextAreaExpression == null) {
			jTextAreaExpression = new JTextArea();
			jTextAreaExpression.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent ce) {
					ExpressionEditorTextPanel.this.checkForExpressionPlaceholder();
				}
			});
			jTextAreaExpression.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent de) {
					this.checkToParseAndWrite();
				}
				@Override
				public void insertUpdate(DocumentEvent de) {
					this.checkToParseAndWrite();
				}
				@Override
				public void changedUpdate(DocumentEvent de) {
					this.checkToParseAndWrite();
				}
				private void checkToParseAndWrite() {
					if (ExpressionEditorTextPanel.this.getJCheckBoxAutoParse().isSelected()==true) {
						ExpressionEditorTextPanel.this.parseAndWriteResult();
					}
				}
			});
		}
		return jTextAreaExpression;
	}
	
	private JButton getJButtonParse() {
		if (jButtonParse == null) {
			jButtonParse = new JButton();
			jButtonParse.setToolTipText("Parse !");
			URL imageURL = ExpressionEditorTextPanel.class.getResource("/icons/ParseAndEvaluate16.png");
			if (imageURL!=null) {
				jButtonParse.setIcon(new ImageIcon(imageURL));
			}
			jButtonParse.setPreferredSize(new Dimension(26, 26));
			jButtonParse.addActionListener(this);
		}
		return jButtonParse;
	}
	private JCheckBox getJCheckBoxAutoParse() {
		if (jCheckBoxAutoParse == null) {
			jCheckBoxAutoParse = new JCheckBox("Auto parse");
			jCheckBoxAutoParse.setFont(new Font("Dialog", Font.PLAIN, 11));
			jCheckBoxAutoParse.addActionListener(this);
		}
		return jCheckBoxAutoParse;
	}
	
	private JLabel getJLabelResultType() {
		if (jLabelResultType == null) {
			jLabelResultType = new JLabel();
			jLabelResultType.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelResultType;
	}
	private JTextField getJTextFieldResult() {
		if (jTextFieldResult == null) {
			jTextFieldResult = new JTextField();
			jTextFieldResult.setHorizontalAlignment(SwingConstants.RIGHT);
			jTextFieldResult.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldResult.setBorder(null);
			jTextFieldResult.setOpaque(false);
			jTextFieldResult.setBackground(new Color(0, 0, 0, 0));
			jTextFieldResult.setEditable(false);
			
		}
		return jTextFieldResult;
	}
	
	private void setExpressionResult(ExpressionResult expressionResult) {
		if (expressionResult==null) {
			this.getJLabelResultType().setText(null);
			this.getJTextFieldResult().setText(null);
		} else {
			if (expressionResult.isValid()==false || expressionResult.getValue()==null) {
				this.getJLabelResultType().setText("Error");
				this.getJTextFieldResult().setText(expressionResult.getErrorMessage());
			} else {
				this.getJLabelResultType().setText("Result Type: " + expressionResult.getExpressionDataType().name());
				this.getJTextFieldResult().setText(expressionResult.getValue().toString());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonParse()) {
			this.parseAndWriteResult();
		} else if (ae.getSource()==this.getJCheckBoxAutoParse()) {
			if (this.getJCheckBoxAutoParse().isSelected()==true) {
				this.parseAndWriteResult();
			}
		}
	}
	/**
	 * Parses the and write result.
	 */
	private void parseAndWriteResult() {
		this.parseExpression();
		if (this.expression!=null) {
			ExpressionResult eResult = this.expression.getExpressionResult(this.getExpressionContext()); 
			this.setExpressionResult(eResult);
			this.firePropertyChange(EXPRESSION_EVALUATED, null, eResult);		
		}
	}
	
	/**
	 * Parses the expression from the text area.
	 */
	protected void parseExpression() {
		String expressionString = this.getJTextAreaExpression().getText();
		this.expression = ExpressionParser.parse(expressionString);
		this.firePropertyChange(EXPRESSION_PARSED, null, this.expression);
	}

	/**
	 * Sets the expression.
	 * @param expression the new expression
	 */
	public void setExpression(Expression expression) {
		this.expression = expression;
		if (this.expression!=null) {
			this.getJTextAreaExpression().setText(this.expression.getExpressionString());
		} else {
			this.getJTextAreaExpression().setText(null);
		}
	}
	/**
	 * Return the current expression.
	 * @return the expression
	 */
	public Expression getExpression() {
		return this.expression;
	}
	
	/**
	 * Sets the current expression context.
	 * @param context the new expression context
	 */
	public void setExpressionContext(ExpressionContext context) {
		this.expressionContext = context;
	}
	/**
	 * Returns the current ExpressionContext.
	 * @return the expression context
	 */
	public ExpressionContext getExpressionContext() {
		if (expressionContext==null) {
			// --- In this case, only a global context is available ----------- 
			expressionContext = new ExpressionContext();
		}
		return expressionContext;
	}
	
	
	/**
	 * Inserts the specified string at the current cursor position.
	 * @param expressionString the expression string
	 */
	public void insertExpressionString(String expressionString) {
		
		String stringToInsert = expressionString;
		if (expressionString.contains("<>")==true) {
			stringToInsert = expressionString.replaceAll("<>", EXPRESSION_PLACEHOLDER);
		}
		
		// --- Replace or insert new expression string --------------
		int selStart = this.getJTextAreaExpression().getSelectionStart();
		int selEnd  = this.getJTextAreaExpression().getSelectionEnd();
		if (selStart!=selEnd) {
			this.getJTextAreaExpression().replaceRange(stringToInsert, selStart, selEnd);
		} else {
			this.getJTextAreaExpression().insert(stringToInsert, selStart);
		}
		this.getJTextAreaExpression().requestFocusInWindow();
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
		this.highlightText(selectFrom, selectTo);
	}
	
	/**
	 * Highlights the specified text according to the specified index position.
	 *
	 * @param idxFrom the index from
	 * @param idxTo the index to
	 */
	private void highlightText(final int idxFrom, final int idxTo) {
		
		if (idxTo<=idxFrom || (idxTo>this.getJTextAreaExpression().getText().length()-1)) return;
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ExpressionEditorTextPanel.this.isCheckForExpressionPlaceholder = false;
					ExpressionEditorTextPanel.this.getJTextAreaExpression().setCaretPosition(idxFrom);
					ExpressionEditorTextPanel.this.getJTextAreaExpression().moveCaretPosition(idxTo);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					ExpressionEditorTextPanel.this.isCheckForExpressionPlaceholder = true;
				}
			}
		});
	}
	
	/**
	 * Check for expression placeholder.
	 */
	private void checkForExpressionPlaceholder() {
		
		if (this.isCheckForExpressionPlaceholder==false) return;
		
		boolean debug = false;
		
		int selStart = this.getJTextAreaExpression().getSelectionStart();
		int selEnd  = this.getJTextAreaExpression().getSelectionEnd();
		if (debug==true) System.out.println("Cursor-Pos: " + selStart + "/" + selEnd);
		if (selStart!=selEnd) return;

		// --- Early exit? --------------------------------
		String currExpression = this.getJTextAreaExpression().getText();
		if (currExpression==null || currExpression.isBlank()==true) return;
		if (selStart >= currExpression.length()) return;
		
		// --- Search to left for '<' ---------------------
		int foundOpen  = -1;
		int foundClose = -1;
		for (int i = selStart-1; i>= 0; i--) {
			char currChar = currExpression.charAt(i); 
			if (currChar=='>') {
				break;
			} else if (currExpression.charAt(i)=='<') {
				foundOpen = i;
				break;
			}
		}
		if (foundOpen==-1) return;
		
		// --- Search to right for '>' --------------------
		for (int i = selStart; i<currExpression.length(); i++) {
			char currChar = currExpression.charAt(i); 
			if (currChar=='<') {
				break;
			} else if (currExpression.charAt(i)=='>') {
				foundClose = i + 1;
				break;
			}
		}
		if (foundClose==-1) return;
		
		// --- Check substring ----------------------------
		String checkExp = currExpression.substring(foundOpen, foundClose);
		if (checkExp.equalsIgnoreCase(EXPRESSION_PLACEHOLDER)==false) return;
		
		if (debug==true) System.out.println(" => '" + currExpression + "' " + foundOpen + " to " + foundClose);
		this.highlightText(foundOpen, foundClose);
	}
	
}
