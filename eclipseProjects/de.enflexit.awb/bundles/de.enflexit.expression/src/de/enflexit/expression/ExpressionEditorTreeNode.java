package de.enflexit.expression;

import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The Class ExpressionEditorTreeNode specifies a node in the expression editor GUI panel. 
 * Each node provides a {@link TreeMap}, containing expression templates grouped by categories. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = -6995251247736826244L;
	
	private TreeMap<String, ArrayList<String>> expressionTemplates;
	
	/**
	 * Instantiates a new expression editor tree node.
	 * @param nodeLabel the node label
	 */
	public ExpressionEditorTreeNode(String nodeLabel) {
		super(nodeLabel);
	}

	/**
	 * Gets the expression templates provided by the service this node represents.
	 * Templates are grouped in categories, which are represented by the keys of
	 * the TreeMap. The templates of one category are provided as an ArrayList.    
	 * @return the expression templates
	 */
	public TreeMap<String, ArrayList<String>> getExpressionTemplates(){
		if (expressionTemplates==null) {
			expressionTemplates = new TreeMap<>();
		}
		return expressionTemplates;
	}
	
	/**
	 * Sets the expression templates for this service type.
	 * @param expressionTemplates the expression templates
	 */
	public void setExpressionTemplates(TreeMap<String, ArrayList<String>> expressionTemplates) {
		this.expressionTemplates = expressionTemplates;
	}
}

