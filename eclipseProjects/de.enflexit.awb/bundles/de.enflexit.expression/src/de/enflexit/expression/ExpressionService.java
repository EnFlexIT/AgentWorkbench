package de.enflexit.expression;

/**
 * The Service definition for extending the expression parser and editor. Every
 * implementation must provide methods to validate and parse an expression, and, 
 * if extending the expression editor is intended, should also provide a tree
 * node to extend the selection of expression templates provided by the editor.  
 *
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface ExpressionService {
	
	/**
	 * Gets the expression type.
	 * @return the expression type
	 */
	public ExpressionType getExpressionType();
	
	/**
	 * Evaluates the provided expression.
	 * @param expression the expression
	 * @return the expression result
	 * @throws UnknownExpressionException thrown if the expression cannot be evaluated by this service
	 */
	public double evaluate(Expression expression) throws UnknownExpressionException;
	
	/**
	 * Checks if the provided expression is valid.
	 * @param expression the expression
	 * @return true, if is valid
	 */
	public boolean isValid(Expression expression);
	
	/**
	 * This method should provide a meaningful error message if validating or evaluating an expression failed-  
	 * @return the error message
	 */
	public String getErrorMessage();
	
	/**
	 * Provides a TreeNode to integrate the expression templates provided
	 * by this service in the expression editor.
	 * @return the expression tree root node
	 */
	public ExpressionEditorTreeNode getExpressionEditorRootNode();
}
