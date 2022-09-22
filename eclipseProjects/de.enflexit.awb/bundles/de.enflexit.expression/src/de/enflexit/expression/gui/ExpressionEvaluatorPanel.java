package de.enflexit.expression.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import de.enflexit.expression.Expression;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JLabel;
import java.awt.Font;

/**
 * A simple GUI panel for evaluating and visualizing nested expressions.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEvaluatorPanel extends JPanel implements ActionListener, TreeSelectionListener {

	private static final long serialVersionUID = -4476462699829568443L;
	
	private JTextField jTextFieldExpression;
	private JButton jButtonEvaluate;
	private JScrollPane JScrollPaneExpressionTree;
	private JTree expressionTree;
	private JLabel jLabelTypeHeader;
	private JLabel jLabelTypeValue;
	private JLabel jLabelValidHeader;
	private JLabel jLabelValidValue;
	
	/**
	 * Instantiates a new expression evaluator panel.
	 */
	public ExpressionEvaluatorPanel() {
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{182, 0, 0};
		gridBagLayout.rowHeights = new int[]{20, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jTextFieldExpression = new GridBagConstraints();
		gbc_jTextFieldExpression.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldExpression.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldExpression.gridx = 0;
		gbc_jTextFieldExpression.gridy = 0;
		add(getJTextFieldExpression(), gbc_jTextFieldExpression);
		GridBagConstraints gbc_jButtonEvaluate = new GridBagConstraints();
		gbc_jButtonEvaluate.anchor = GridBagConstraints.NORTHWEST;
		gbc_jButtonEvaluate.insets = new Insets(5, 5, 5, 5);
		gbc_jButtonEvaluate.gridx = 1;
		gbc_jButtonEvaluate.gridy = 0;
		add(getJButtonEvaluate(), gbc_jButtonEvaluate);
		GridBagConstraints gbc_JScrollPaneExpressionTree = new GridBagConstraints();
		gbc_JScrollPaneExpressionTree.gridheight = 4;
		gbc_JScrollPaneExpressionTree.insets = new Insets(0, 5, 5, 5);
		gbc_JScrollPaneExpressionTree.fill = GridBagConstraints.BOTH;
		gbc_JScrollPaneExpressionTree.gridx = 0;
		gbc_JScrollPaneExpressionTree.gridy = 1;
		add(getJScrollPaneExpressionTree(), gbc_JScrollPaneExpressionTree);
		GridBagConstraints gbc_jLabelTypeHeader = new GridBagConstraints();
		gbc_jLabelTypeHeader.insets = new Insets(10, 0, 5, 0);
		gbc_jLabelTypeHeader.gridx = 1;
		gbc_jLabelTypeHeader.gridy = 1;
		add(getJLabelTypeHeader(), gbc_jLabelTypeHeader);
		GridBagConstraints gbc_jLabelTypeValue = new GridBagConstraints();
		gbc_jLabelTypeValue.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelTypeValue.gridx = 1;
		gbc_jLabelTypeValue.gridy = 2;
		add(getJLabelTypeValue(), gbc_jLabelTypeValue);
		GridBagConstraints gbc_jLabelValidHeader = new GridBagConstraints();
		gbc_jLabelValidHeader.insets = new Insets(10, 0, 5, 0);
		gbc_jLabelValidHeader.gridx = 1;
		gbc_jLabelValidHeader.gridy = 3;
		add(getJLabelValidHeader(), gbc_jLabelValidHeader);
		GridBagConstraints gbc_jLabelValidValue = new GridBagConstraints();
		gbc_jLabelValidValue.anchor = GridBagConstraints.NORTH;
		gbc_jLabelValidValue.gridx = 1;
		gbc_jLabelValidValue.gridy = 4;
		add(getJLabelValidValue(), gbc_jLabelValidValue);
	}

	/**
	 * Gets the j text field expression.
	 * @return the j text field expression
	 */
	private JTextField getJTextFieldExpression() {
		if (jTextFieldExpression == null) {
			jTextFieldExpression = new JTextField();
		}
		return jTextFieldExpression;
	}
	
	/**
	 * Gets the j button evaluate.
	 * @return the j button evaluate
	 */
	private JButton getJButtonEvaluate() {
		if (jButtonEvaluate == null) {
			jButtonEvaluate = new JButton("Evaluate");
			jButtonEvaluate.addActionListener(this);
		}
		return jButtonEvaluate;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonEvaluate()) {
			this.getExpressionTree().clearSelection();
			Expression expression = new Expression(this.getJTextFieldExpression().getText());
			expression.parse();
			if (expression.hasErrors()==false) {
				this.getExpressionTree().setModel(this.createExpressionTreeModel(expression));
				for (int i=0; i<this.getExpressionTree().getRowCount(); i++) {
					this.getExpressionTree().expandRow(i);
				}
			} else {
				DefaultMutableTreeNode errorNode = new DefaultMutableTreeNode("Error: Expression not well-formed!");
				this.getExpressionTree().setModel(new DefaultTreeModel(errorNode));
			}
		}
	}
	private JScrollPane getJScrollPaneExpressionTree() {
		if (JScrollPaneExpressionTree == null) {
			JScrollPaneExpressionTree = new JScrollPane();
			JScrollPaneExpressionTree.setViewportView(getExpressionTree());
		}
		return JScrollPaneExpressionTree;
	}
	private JTree getExpressionTree() {
		if (expressionTree == null) {
			expressionTree = new JTree();
			expressionTree.setModel(getInitialTreeModel());
			expressionTree.addTreeSelectionListener(this);
		}
		return expressionTree;
	}
	
	private DefaultTreeModel getInitialTreeModel() {
		DefaultMutableTreeNode initialNode = new DefaultMutableTreeNode("<Not initialized - Please enter and evaluate an expression!>");
		return new DefaultTreeModel(initialNode);
	}
	
	private DefaultMutableTreeNode createTreeNode(Expression expression) {
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode();
		treeNode.setUserObject(expression);
		for (Expression subExpression : expression.getSubExpressions()) {
			DefaultMutableTreeNode childNode = this.createTreeNode(subExpression);
			treeNode.add(childNode);
		}
		return treeNode;
	}
	
	private DefaultTreeModel createExpressionTreeModel(Expression expression) {
		DefaultMutableTreeNode rootNode = this.createTreeNode(expression);
		return new DefaultTreeModel(rootNode);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent tse) {
		if (tse.getSource()==this.getExpressionTree()) {
			
			if (this.getExpressionTree().getLastSelectedPathComponent()!=null) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.getExpressionTree().getLastSelectedPathComponent();
				Expression expression = (Expression) selectedNode.getUserObject();
				this.setExpressionToVisualization(expression);
			} else {
				this.getJTextFieldExpression().requestFocus();
				this.getJTextFieldExpression().select(0, 0);
				this.getJLabelTypeValue().setText("<n.a.>");
				this.getJLabelValidValue().setText("<n.a.>");
			}
		}
	}
	
	private void setExpressionToVisualization(Expression expression) {
		
		// --- Highlight the selected (sub) expression in the text field
		int positionFrom = this.getJTextFieldExpression().getText().indexOf(expression.getExpressionString());
		int positionTo = positionFrom + expression.getExpressionString().length();
		this.getJTextFieldExpression().requestFocus();
		this.getJTextFieldExpression().select(positionFrom, positionTo);

		// --- Set some metadata to the labels -------------
		if (expression.getExpressionType()!=null) {
			this.getJLabelTypeValue().setText(expression.getExpressionType());
		} else {
			this.getJLabelTypeValue().setText("Math");
		}
		this.getJLabelValidValue().setText("" + !expression.hasErrors());
	}
	
	private JLabel getJLabelTypeHeader() {
		if (jLabelTypeHeader == null) {
			jLabelTypeHeader = new JLabel("Type:");
			jLabelTypeHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTypeHeader;
	}
	private JLabel getJLabelTypeValue() {
		if (jLabelTypeValue == null) {
			jLabelTypeValue = new JLabel("<n.a.>");
			jLabelTypeValue.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelTypeValue;
	}
	private JLabel getJLabelValidHeader() {
		if (jLabelValidHeader == null) {
			jLabelValidHeader = new JLabel("Valid:");
			jLabelValidHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelValidHeader;
	}
	private JLabel getJLabelValidValue() {
		if (jLabelValidValue == null) {
			jLabelValidValue = new JLabel("<n.a.>");
			jLabelValidValue.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelValidValue;
	}
}
