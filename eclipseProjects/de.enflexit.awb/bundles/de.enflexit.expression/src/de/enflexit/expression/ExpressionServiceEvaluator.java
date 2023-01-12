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
	public ExpressionResult evaluate(Expression expression) throws UnknownExpressionException;
	
}
