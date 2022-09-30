package de.enflexit.expression.gui;

import javax.swing.JPanel;
import de.enflexit.expression.Expression;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class implements the main panel of the expression editor. It mainly just hosts
 * the sub-panels for the different components of the editor and mediates between them.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorMainPanel extends JPanel implements PropertyChangeListener{
	
	private static final long serialVersionUID = 4901839979331733505L;
	
	private ExpressionEditorTextPanel textPanel;
	private ExpressionEditorLibraryPanel libraryPanel;
	private ExpressionEditorStructureTreePanel structureTreePanel;
	
	private Expression expression;

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
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_textPanel = new GridBagConstraints();
		gbc_textPanel.weighty = 3.0;
		gbc_textPanel.weightx = 7.0;
		gbc_textPanel.insets = new Insets(10, 10, 5, 5);
		gbc_textPanel.fill = GridBagConstraints.BOTH;
		gbc_textPanel.gridx = 0;
		gbc_textPanel.gridy = 0;
		add(getTextPanel(), gbc_textPanel);
		GridBagConstraints gbc_structureTreePanel = new GridBagConstraints();
		gbc_structureTreePanel.weightx = 3.0;
		gbc_structureTreePanel.gridheight = 2;
		gbc_structureTreePanel.insets = new Insets(10, 5, 10, 10);
		gbc_structureTreePanel.fill = GridBagConstraints.BOTH;
		gbc_structureTreePanel.gridx = 1;
		gbc_structureTreePanel.gridy = 0;
		add(getStructureTreePanel(), gbc_structureTreePanel);
		GridBagConstraints gbc_libraryPanel = new GridBagConstraints();
		gbc_libraryPanel.weighty = 7.0;
		gbc_libraryPanel.weightx = 7.0;
		gbc_libraryPanel.insets = new Insets(5, 10, 10, 5);
		gbc_libraryPanel.fill = GridBagConstraints.BOTH;
		gbc_libraryPanel.gridx = 0;
		gbc_libraryPanel.gridy = 1;
		add(getLibraryPanel(), gbc_libraryPanel);
	}
	private ExpressionEditorTextPanel getTextPanel() {
		if (textPanel==null) {
			textPanel = new ExpressionEditorTextPanel();
			textPanel.addPropertyChangeListener(this);
		}
		return textPanel;
	}
	private ExpressionEditorLibraryPanel getLibraryPanel() {
		if (libraryPanel==null) {
			libraryPanel = new ExpressionEditorLibraryPanel();
		}
		return libraryPanel;
	}
	private ExpressionEditorStructureTreePanel getStructureTreePanel() {
		if (structureTreePanel==null) {
			structureTreePanel = new ExpressionEditorStructureTreePanel();
			structureTreePanel.addPropertyChangeListener(this);
		}
		return structureTreePanel;
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
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource()==this.getTextPanel()) {
			if (evt.getPropertyName().equals(ExpressionEditorTextPanel.EXPRESSION_PARSED)) {
				// --- The expression was parsed, show the corresponding structure tree
				Expression parsedExpression = this.getTextPanel().getExpression();
				this.getStructureTreePanel().setExpression(parsedExpression);
			}
		} else if (evt.getSource()==this.getStructureTreePanel()) {
			if (evt.getPropertyName().equals(ExpressionEditorStructureTreePanel.SELECTION_CHANGED)) {
				// --- A sub-expression was selected, highlight it in the editor
				Expression selectedExpression = (Expression) evt.getNewValue();
				this.getTextPanel().highlightSubExpression(selectedExpression);
			}
		}
	}
	
}
