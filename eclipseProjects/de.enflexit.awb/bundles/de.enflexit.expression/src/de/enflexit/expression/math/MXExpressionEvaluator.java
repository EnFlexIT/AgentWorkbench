package de.enflexit.expression.math;

import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionService;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.UnknownExpressionException;

/**
 * The Class MXExpressionEvaluator evaluates .
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MXExpressionEvaluator implements ExpressionServiceEvaluator<Double> {

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionServiceEvaluator#evaluate(de.enflexit.expression.Expression)
	 */
	@Override
	public Double evaluate(Expression expression) throws UnknownExpressionException {
		
		// --- Exchange sub expressions by sub results -------------- 
		String expressionString = expression.getExpressionString(); 
		if (expression.getSubExpressions().size()>0) {
			// --- Replace sub expressions by sub results -----------
			for (int i = 0; i < expression.getSubExpressions().size(); i++) {
				Expression subExp = expression.getSubExpressions().get(i);
				String subExpStringSearch = ExpressionService.EXPRESSION_OPENING_DELIMITER + subExp.getExpressionString() + ExpressionService.EXPRESSION_CLOSING_DELIMITER; 
				Object subResult = subExp.getExpressionResult();
				expressionString = expressionString.replace(subExpStringSearch, subResult.toString());
			}
		}
		
		// --- Create mX Expression ---------------------------------
		org.mariuszgromada.math.mxparser.Expression mXExpression = new org.mariuszgromada.math.mxparser.Expression(expressionString);
		
		return mXExpression.calculate();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionServiceEvaluator#isValid(de.enflexit.expression.Expression)
	 */
	@Override
	public boolean isValid(Expression expression) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionServiceEvaluator#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
