package de.enflexit.expression;

/**
 * The Service definition for extending the expression parser and editor. Every
 * implementation must provide methods to validate and parse an expression, and, 
 * if extending the expression editor is intended, should also provide a tree
 * node to extend the selection of expression templates provided by the editor.  
 *
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @author Christian Derksen - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface ExpressionService<ReturnType> {
	
	public static final char EXPRESSION_OPENING_DELIMITER = '[';
	public static final char EXPRESSION_CLOSING_DELIMITER = ']';
	
	
	/**
	 * Gets the expression type.
	 * @return the expression type
	 */
	public ExpressionType getExpressionType();
	
	/**
	 * Has to return the expression service evaluator instance that is able to evaluate an expression string.
	 * @return the expression service evaluator
	 */
	public ExpressionServiceEvaluator<ReturnType> getExpressionServiceEvaluator();
	
	/**
	 * Provides a TreeNode to integrate the expression templates provided
	 * by this service in the expression editor.
	 * @return the expression tree root node
	 */
	public ExpressionEditorTreeNode getExpressionEditorRootNode();
	
}
