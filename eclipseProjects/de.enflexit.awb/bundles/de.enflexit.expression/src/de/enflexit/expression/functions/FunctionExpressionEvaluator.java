package de.enflexit.expression.functions;

import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionResult;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.UnknownExpressionException;

/**
 * The Class FunctionExpressionEvaluator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class FunctionExpressionEvaluator implements ExpressionServiceEvaluator {

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionServiceEvaluator#evaluate(de.enflexit.expression.Expression, de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionResult evaluate(Expression expression, ExpressionContext context) throws UnknownExpressionException {
		
		System.err.println(this.getClass().getSimpleName() + ": Here we go! ");
		
		
		
		
		return null;
	}

}
