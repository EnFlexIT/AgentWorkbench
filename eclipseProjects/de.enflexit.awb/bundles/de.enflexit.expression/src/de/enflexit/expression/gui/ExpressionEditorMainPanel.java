package de.enflexit.expression.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;

/**
 * This class implements the main panel of the expression editor. It mainly just hosts
 * the sub-panels for the different components of the editor and mediates between them.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorMainPanel extends JPanel implements PropertyChangeListener{
	
	private static final long serialVersionUID = 4901839979331733505L;
	
	private JSplitPane jSplitPaneMain;
	private JSplitPane jSplitPaneLeft;
	private ExpressionEditorTextPanel jPanelExpressionTextEditor;
	private ExpressionEditorLibraryPanel jPanelLibrary;
	private ExpressionEditorStructureTreePanel jPanelStructureTreeRight;

	/**
	 * Instantiates a new expression editor main panel.
	 */
	public ExpressionEditorMainPanel() {
		initialize();
	}
	
	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jSplitPaneMain = new GridBagConstraints();
		gbc_jSplitPaneMain.insets = new Insets(10, 10, 10, 10);
		gbc_jSplitPaneMain.fill = GridBagConstraints.BOTH;
		gbc_jSplitPaneMain.gridx = 0;
		gbc_jSplitPaneMain.gridy = 0;
		this.add(this.getJSplitPaneMain(), gbc_jSplitPaneMain);
	}
	
	
	/**
	 * Returns the current expression.
	 * @return the expression
	 */
	public Expression getExpression() {
		this.getJPanelExpressionTextEditor().parseExpression();
		return this.getJPanelExpressionTextEditor().getExpression();
	}
	/**
	 * Sets the current expression.
	 * @param expression the new expression
	 */
	public void setExpression(Expression expression) {
		this.getJPanelExpressionTextEditor().setExpression(expression);
	}
	
	/**
	 * Returns the current expression context.
	 * @return the expression context
	 */
	public ExpressionContext getExpressionContext() {
		return this.getJPanelExpressionTextEditor().getExpressionContext();
	}
	/**
	 * Sets the current expression context.
	 * @param context the new expression context
	 */
	public void setExpressionContext(ExpressionContext context) {
		this.getJPanelExpressionTextEditor().setExpressionContext(context);
		this.getJPanelLibrary().setExpressionContext(context);
	}

	
	private JSplitPane getJSplitPaneMain() {
		if (jSplitPaneMain == null) {
			jSplitPaneMain = new JSplitPane();
			jSplitPaneMain.setRightComponent(this.getJPanelStructureTreeRight());
			jSplitPaneMain.setLeftComponent(this.getJSplitPaneLeft());
			jSplitPaneMain.setBorder(BorderFactory.createEmptyBorder());
			jSplitPaneMain.setDividerSize(7);
			jSplitPaneMain.setDividerLocation(0.75);
			jSplitPaneMain.setResizeWeight(0.5);
			jSplitPaneMain.setOneTouchExpandable(true);
		}
		return jSplitPaneMain;
	}
	private ExpressionEditorStructureTreePanel getJPanelStructureTreeRight() {
		if (jPanelStructureTreeRight==null) {
			jPanelStructureTreeRight = new ExpressionEditorStructureTreePanel();
			jPanelStructureTreeRight.setBorder(new EmptyBorder(0, 5, 0, 0));
			jPanelStructureTreeRight.addPropertyChangeListener(this);
		}
		return jPanelStructureTreeRight;
	}
	
	
	private JSplitPane getJSplitPaneLeft() {
		if (jSplitPaneLeft == null) {
			jSplitPaneLeft = new JSplitPane();
			jSplitPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneLeft.setTopComponent(this.getJPanelExpressionTextEditor());
			jSplitPaneLeft.setBottomComponent(this.getJPanelLibrary());
			jSplitPaneLeft.setBorder(BorderFactory.createEmptyBorder());
			jSplitPaneLeft.setDividerSize(7);
			jSplitPaneLeft.setResizeWeight(0.33);
			jSplitPaneLeft.setDividerLocation(0.33);
			jSplitPaneLeft.setOneTouchExpandable(true);
		}
		return jSplitPaneLeft;
	}
	
	private ExpressionEditorTextPanel getJPanelExpressionTextEditor() {
		if (jPanelExpressionTextEditor==null) {
			jPanelExpressionTextEditor = new ExpressionEditorTextPanel();
			jPanelExpressionTextEditor.setBorder(new EmptyBorder(0, 0, 0, 5));
			jPanelExpressionTextEditor.addPropertyChangeListener(this);
		}
		return jPanelExpressionTextEditor;
	}
	private ExpressionEditorLibraryPanel getJPanelLibrary() {
		if (jPanelLibrary==null) {
			jPanelLibrary = new ExpressionEditorLibraryPanel();
			jPanelLibrary.setBorder(new EmptyBorder(5, 0, 0, 5));
			jPanelLibrary.addPropertyChangeListener(this);
		}
		return jPanelLibrary;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if (evt.getSource()==this.getJPanelExpressionTextEditor()) {
			if (evt.getPropertyName().equals(ExpressionEditorTextPanel.EXPRESSION_PARSED)) {
				// --- The expression was parsed, show the corresponding structure tree
				this.getJPanelStructureTreeRight().setExpression((Expression) evt.getNewValue());
				
			} else if (evt.getPropertyName().equals(ExpressionEditorTextPanel.EXPRESSION_EVALUATED)) {
				//ExpressionResult er = (ExpressionResult) evt.getNewValue();
				//System.out.println(er.getValue());
			}
			
		} else if (evt.getSource()==this.getJPanelStructureTreeRight()) {
			if (evt.getPropertyName().equals(ExpressionEditorStructureTreePanel.SELECTION_CHANGED)) {
				// --- A sub-expression was selected, highlight it in the editor
				Expression selectedExpression = (Expression) evt.getNewValue();
				this.getJPanelExpressionTextEditor().highlightSubExpression(selectedExpression);
			}
			
		} else if (evt.getSource()==this.getJPanelLibrary()) {
			if (evt.getPropertyName().equals(ExpressionEditorLibraryPanel.EXPRESSION_INSERTED)) {
				String stringToInsert = (String) evt.getNewValue();
				this.getJPanelExpressionTextEditor().insertExpressionString(stringToInsert);
			}
			
		}
	}

}
