package de.enflexit.expression.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;

/**
 * Reduced version of the {@link ExpressionEditorWidget} for the use as a table cell editor. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorWidgetForTables extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 6056053207940611095L;
	
	private Expression expression;
	private ExpressionContext expressionContext;

	private JTextField jTextFieldExpression;
	private JButton jButtonExpressionEditor;
	
	private ExpressionServiceFilter expressionLibraryFilter;
	
	public ExpressionEditorWidgetForTables(Expression expression, ExpressionContext expressionContext) {
		this.expression = expression;
		this.expressionContext = expressionContext;
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
		gbc_jButtonExpressionEditor.fill = GridBagConstraints.VERTICAL;
		gbc_jButtonExpressionEditor.gridx = 1;
		gbc_jButtonExpressionEditor.gridy = 0;
		add(getJButtonExpressionEditor(), gbc_jButtonExpressionEditor);
		
		this.setToolTipText(this.getJTextFieldExpression().getText());
	}

	private JTextField getJTextFieldExpression() {
		if (jTextFieldExpression == null) {
			jTextFieldExpression = new JTextField();
			if (this.expression!=null) {
				jTextFieldExpression.setText(this.expression.getExpressionString());
			}
			jTextFieldExpression.setOpaque(false);
			jTextFieldExpression.setBorder(BorderFactory.createEmptyBorder());
			
			jTextFieldExpression.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent ke) {
					if (ke.getKeyCode()==KeyEvent.VK_ENTER || ke.getKeyCode()==KeyEvent.VK_TAB) {
						ExpressionEditorWidgetForTables.this.setExpression(ExpressionEditorWidgetForTables.this.getExpressionFromTextField());
						// --- Pass to the parent -----------------------------
						JComponent sourceComponent = (JComponent) ke.getSource();
						sourceComponent.getParent().dispatchEvent(ke);
					}
				}
			});
		}
		return jTextFieldExpression;
	}
	
	private JButton getJButtonExpressionEditor() {
		if (jButtonExpressionEditor == null) {
			jButtonExpressionEditor = new JButton();
			jButtonExpressionEditor.setToolTipText("Open Expression Editor");
			jButtonExpressionEditor.addActionListener(this);
//			int preferredWidth = 26;
//			int preferredHeight = this.getHeight();
			Dimension buttonSize = new Dimension(16, 16);
			jButtonExpressionEditor.setSize(buttonSize);
			jButtonExpressionEditor.setPreferredSize(buttonSize);
			jButtonExpressionEditor.setMaximumSize(buttonSize);
		}
		return jButtonExpressionEditor;
	}
	
	/**
	 * Sets the current expression according to the text field.
	 * @return the expression
	 */
	private Expression getExpressionFromTextField() {
		Expression expression = null;
		String textFieldContent = this.getJTextFieldExpression().getText();
		if (textFieldContent!=null && textFieldContent.isEmpty()==false) {
			expression = Expression.parse(textFieldContent);
		} else {
			expression = null;
		}
		return expression;
	}
	
	/**
	 * Gets the expression.
	 * @return the expression
	 */
	public Expression getExpression() {
		this.setExpression(this.getExpressionFromTextField());
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
			this.setToolTipText(expression.getExpressionString());
		} else {
			this.getJTextFieldExpression().setText(null);
			this.setToolTipText(null);
		}
	}

	/**
	 * Gets the expression context.
	 * @return the expression context
	 */
	public ExpressionContext getExpressionContext() {
		return expressionContext;
	}
	/**
	 * Sets the expression context.
	 * @param expressionContext the new expression context
	 */
	public void setExpressionContext(ExpressionContext expressionContext) {
		this.expressionContext = expressionContext;
	}
	
	/**
	 * Gets the expression library filter.
	 * @return the expression library filter
	 */
	public ExpressionServiceFilter getExpressionServiceFilter() {
		return expressionLibraryFilter;
	}

	/**
	 * Sets the expression library filter.
	 * @param expressionServiceFilter the new expression library filter
	 */
	public void setExpressionServiceFilter(ExpressionServiceFilter expressionServiceFilter) {
		this.expressionLibraryFilter = expressionServiceFilter;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonExpressionEditor()) {
			
			Window ownerWindow = OwnerDetection.getOwnerWindowForComponent(jButtonExpressionEditor);
			ExpressionEditorDialog editorDialog = new ExpressionEditorDialog(ownerWindow, this.getExpressionFromTextField(), this.getExpressionContext(), true);
			editorDialog.setExpressionServiceFilter(this.getExpressionServiceFilter());
			if (ownerWindow!=null) {
				double scaleWidth = 1.15;
				double scaleHeight = 0.95;
				editorDialog.setSize((int)(ownerWindow.getWidth() * scaleWidth), (int)(ownerWindow.getHeight() * scaleHeight));
				WindowSizeAndPostionController.setJDialogPositionOnScreen(editorDialog, JDialogPosition.ParentCenter);
			}
			editorDialog.setVisible(true);
			
			if (editorDialog.isCanceled()==false) {
				this.setExpression(editorDialog.getExpression());
			}
			
			this.jTextFieldExpression.requestFocus();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setBackground(java.awt.Color)
	 */
	@Override
	public void setBackground(Color bgColor) {
		super.setBackground(bgColor);
		this.getJTextFieldExpression().setBackground(bgColor);
		if (this.expression!=null) {
			this.getJTextFieldExpression().setText(this.expression.getExpressionString());
			this.setToolTipText(this.expression.getExpressionString());
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setToolTipText(java.lang.String)
	 */
	@Override
	public void setToolTipText(String text) {
		// --- Set for the text field only, while the button keeps its tool tip text
		this.getJTextFieldExpression().setToolTipText(text);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	@Override
	public String getToolTipText(MouseEvent me) {
		//TODO figure out how to determine the actual button bounds
		if (this.getComponentAt(me.getPoint())==this.getJButtonExpressionEditor()) {
			// --- Return the button's tooltip text if inside the button
			return this.getJButtonExpressionEditor().getToolTipText();
		} else {
			// --- Return the textfields tooltip text (=the expression) otherwise
			return this.getJTextFieldExpression().getToolTipText();
		}
	}
}
