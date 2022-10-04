package de.enflexit.expression.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionTypeUnknown;

/**
 * This class implements the right sub-panel of the expression editor, showing the expression structure tree. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorStructureTreePanel extends JPanel implements TreeSelectionListener {

	private static final long serialVersionUID = 1598277005013258708L;
	
	public static final String SELECTION_CHANGED = "SelectedExpression";
	
	private JLabel jLabelExpressionStructure;
	private JScrollPane jScrollPaneExpressionTree;
	private JTree expressionTree;
	private JLabel jLabelType;
	private JLabel jLabelValid;
	private JLabel typeLabel;
	private JLabel validLabel;
	
	/**
	 * Instantiates a new expression editor structure tree panel.
	 */
	public ExpressionEditorStructureTreePanel() {
		super();
		this.initialize();
	}
	
	/**
	 * Initialize the GUI components.
	 */
	private void initialize() {
		GridBagLayout gbl_this = new GridBagLayout();
		gbl_this.columnWidths = new int[]{0, 0, 0};
		gbl_this.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_this.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_this.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gbl_this);
		GridBagConstraints gbc_jLabelExpressionStructure = new GridBagConstraints();
		gbc_jLabelExpressionStructure.gridwidth = 2;
		gbc_jLabelExpressionStructure.anchor = GridBagConstraints.WEST;
		gbc_jLabelExpressionStructure.gridx = 0;
		gbc_jLabelExpressionStructure.gridy = 0;
		this.add(getJLabelExpressionStructure(), gbc_jLabelExpressionStructure);
		GridBagConstraints gbc_jScrollPaneExpressionTree = new GridBagConstraints();
		gbc_jScrollPaneExpressionTree.gridwidth = 2;
		gbc_jScrollPaneExpressionTree.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneExpressionTree.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneExpressionTree.gridx = 0;
		gbc_jScrollPaneExpressionTree.gridy = 1;
		this.add(getJScrollPaneExpressionTree(), gbc_jScrollPaneExpressionTree);
		GridBagConstraints gbc_jLabelType = new GridBagConstraints();
		gbc_jLabelType.insets = new Insets(5, 0, 0, 0);
		gbc_jLabelType.gridx = 0;
		gbc_jLabelType.gridy = 2;
		this.add(getJLabelType(), gbc_jLabelType);
		GridBagConstraints gbc_typeLabel = new GridBagConstraints();
		gbc_typeLabel.anchor = GridBagConstraints.WEST;
		gbc_typeLabel.insets = new Insets(5, 5, 0, 0);
		gbc_typeLabel.gridx = 1;
		gbc_typeLabel.gridy = 2;
		this.add(getTypeLabel(), gbc_typeLabel);
		GridBagConstraints gbc_jLabelValid = new GridBagConstraints();
		gbc_jLabelValid.insets = new Insets(5, 0, 5, 0);
		gbc_jLabelValid.gridx = 0;
		gbc_jLabelValid.gridy = 3;
		this.add(getJLabelValid(), gbc_jLabelValid);
		GridBagConstraints gbc_validLabel = new GridBagConstraints();
		gbc_validLabel.insets = new Insets(5, 5, 5, 0);
		gbc_validLabel.anchor = GridBagConstraints.WEST;
		gbc_validLabel.gridx = 1;
		gbc_validLabel.gridy = 3;
		this.add(getValidLabel(), gbc_validLabel);
	}
	private JLabel getJLabelExpressionStructure() {
		if (jLabelExpressionStructure == null) {
			jLabelExpressionStructure = new JLabel("Expression Structure");
			jLabelExpressionStructure.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelExpressionStructure;
	}
	private JScrollPane getJScrollPaneExpressionTree() {
		if (jScrollPaneExpressionTree == null) {
			jScrollPaneExpressionTree = new JScrollPane();
			jScrollPaneExpressionTree.setViewportView(getExpressionTree());
		}
		return jScrollPaneExpressionTree;
	}
	private JTree getExpressionTree() {
		if (expressionTree == null) {
			expressionTree = new JTree();
			expressionTree.setFont(new Font("Dialog", Font.PLAIN, 12));
			expressionTree.setModel(this.getInitialTreeModel());
			expressionTree.addTreeSelectionListener(this);
		}
		return expressionTree;
	}
	private JLabel getJLabelType() {
		if (jLabelType == null) {
			jLabelType = new JLabel("Type:");
			jLabelType.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelType;
	}
	private JLabel getJLabelValid() {
		if (jLabelValid == null) {
			jLabelValid = new JLabel("Valid:");
			jLabelValid.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelValid;
	}
	private JLabel getTypeLabel() {
		if (typeLabel == null) {
			typeLabel = new JLabel("<n.a.>");
			typeLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return typeLabel;
	}
	private JLabel getValidLabel() {
		if (validLabel == null) {
			validLabel = new JLabel("<n.a.>");
		}
		return validLabel;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent tse) {
		
		if (tse.getSource()==this.getExpressionTree()) {
			Expression selectedExpression = null;
			if (this.getExpressionTree().getLastSelectedPathComponent()!=null) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.getExpressionTree().getLastSelectedPathComponent();
				selectedExpression = (Expression) selectedNode.getUserObject();
				
				if (selectedExpression.getExpressionType()!=null) {
					this.getTypeLabel().setText(selectedExpression.getExpressionType().getTypePrefix());
				}
				
				if (selectedExpression.getExpressionType() instanceof ExpressionTypeUnknown) {
					this.getValidLabel().setText("<n.a.>");
				} else {
					this.getValidLabel().setText("" + !selectedExpression.hasErrors());
				}
				
			} else {
				this.getTypeLabel().setText("<n.a.>");
				this.getValidLabel().setText("<n.a.>");
			}
			
			this.firePropertyChange(SELECTION_CHANGED, null, selectedExpression);
		}
	}
	
	/**
	 * Gets the initial tree model for the expression structure tree.
	 * @return the initial tree model
	 */
	private DefaultTreeModel getInitialTreeModel() {
		DefaultMutableTreeNode initialNode = new DefaultMutableTreeNode("<Not initialized>");
		return new DefaultTreeModel(initialNode);
	}
	
	/**
	 * Creates the tree model for the expression structure tree, based on the provided expression.
	 * @param expression the expression
	 * @return the default tree model
	 */
	private DefaultTreeModel createExpressionTreeModel(Expression expression) {
		DefaultMutableTreeNode rootNode = this.createTreeNode(expression);
		return new DefaultTreeModel(rootNode);
	}
	
	/**
	 * Recursively creates tree nodes for an expression and its' sub expressions.
	 * @param expression the expression
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode createTreeNode(Expression expression) {
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode();
		treeNode.setUserObject(expression);
		for (Expression subExpression : expression.getSubExpressions()) {
			DefaultMutableTreeNode childNode = this.createTreeNode(subExpression);
			treeNode.add(childNode);
		}
		return treeNode;
	}
	
	/**
	 * Sets the expression, initiates the creation of the corresponding structure tree.
	 * @param expression the new expression
	 */
	public void setExpression(Expression expression) {
		this.getExpressionTree().setModel(this.createExpressionTreeModel(expression));
		for (int i=0; i<this.getExpressionTree().getRowCount(); i++) {
			this.getExpressionTree().expandRow(i);
		}
	}
	
}
