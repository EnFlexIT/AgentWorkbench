package de.enflexit.expression;

/**
 * The Interface ExpressionServiceEvaluator describes the public interfaces that are 
 * required to evaluate a specified expression string.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface ExpressionServiceEvaluator {

	/**
	 * Evaluates the provided expression.
	 * @param expression the expression
	 * @return the expression result
	 * @throws UnknownExpressionException thrown if the expression cannot be evaluated by this service
	 */
	public ExpressionReturnValue evaluate(Expression expression) throws UnknownExpressionException;
	
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
	
}
