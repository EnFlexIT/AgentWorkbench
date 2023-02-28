package de.enflexit.expression.math;

import com.expression.parser.exception.CalculatorException;
import com.expression.parser.function.FunctionX;

import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionResult;
import de.enflexit.expression.ExpressionService;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.UnknownExpressionException;

/**
 * The Class MathExpressionEvaluator evaluates mathematical expressions 
 * and returns its result value as {@link ExpressionResult}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MathExpressionEvaluator implements ExpressionServiceEvaluator {
	
	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionServiceEvaluator#evaluate(de.enflexit.expression.Expression, de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionResult evaluate(Expression expression, ExpressionContext context) throws UnknownExpressionException {

		// --- Define an ExpressionResult ---------------------------
		ExpressionResult er = new ExpressionResult();
		
		// --- Early exit? ------------------------------------------
		if (expression.hasErrors()==true) {
			er.addErrorMessage(expression.getErrorMessage());
			return er;
		}
		
		// --- Exchange sub expressions by sub results -------------- 
		String expressionString = expression.getExpressionString(); 
		if (expression.getSubExpressions().size()>0) {
			// --- Replace sub expressions by sub results -----------
			for (int i = 0; i < expression.getSubExpressions().size(); i++) {
				Expression subExp = expression.getSubExpressions().get(i);
				if (subExp.hasErrors()==false) {
					// --- For an error-free expression -------------  
					ExpressionResult subResult = subExp.getExpressionResult(context);
					if (subResult.hasErrors()==false) {
						String subExpStringSearch = ExpressionService.EXPRESSION_OPENING_DELIMITER + subExp.getExpressionString() + ExpressionService.EXPRESSION_CLOSING_DELIMITER; 
						// --- VALID sub result --------------------- 
						expressionString = expressionString.replace(subExpStringSearch, subResult.getDoubleValue().toString());
						
					} else {
						er.addErrorMessage("Invalid sub-expression:");
						er.getMessageList().addAll(subResult.getMessageList());
						return er;
					}
				} else {
					er.addErrorMessage(subExp.getErrorMessage());
					return er;
				}
			}
		}
		return this.evaluateMathExpression(expressionString);
	}

	/**
	 * Evaluates the specified math expression and returns its result.
	 *
	 * @param expressionString the expression string to evaluate
	 * @return the expression result
	 */
	private ExpressionResult evaluateMathExpression(String expressionString) {
		
		ExpressionResult er = null;

		if (expressionString!=null && expressionString.isEmpty()==false) {
			// ----------------------------------------------------------------
			// --- Here, some string preparations could be done ---------------
			// ----------------------------------------------------------------
			String expressionWork = expressionString;
			// --- Remove line breaks ---------------------
			expressionWork = expressionWork.replace("\n", "").replace("\r", "");
			
			
			// ----------------------------------------------------------------
			// --- Similar to Parser.simpleEval() in parser library -----------
			// ----------------------------------------------------------------
			try {
				FunctionX f_x = new FunctionX(expressionWork);
				er = new ExpressionResult(f_x.getF_xo(0));
				
			} catch (final CalculatorException cEx) {
				er = new ExpressionResult();
				er.addErrorMessage(cEx.getMessage());
			}
		} else {
			er = new ExpressionResult();
			er.addErrorMessage("No expression string to evaluate!");
		}
		return er;
	}
	
}
