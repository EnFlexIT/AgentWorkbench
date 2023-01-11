package de.enflexit.expression;

/**
 * The Class ExpressionEvaluator will evaluate the specified {@link Expression}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionEvaluator {

	private Expression expression;
	private Object expressionResult;
	
	
	/**
	 * Instantiates a new expression evaluator.
	 * @param expression the expression to evaluate
	 */
	public ExpressionEvaluator(Expression expression) {
		this.expression = expression;
	}
	/**
	 * Evaluates the locally specified {@link Expression}.
	 * @return the result object
	 */
	public Object getExpressionResult() {
		if (this.expression==null) throw new IllegalArgumentException("No Expression was specified to be evaluated!");
		if (this.expressionResult==null) {
			this.expressionResult = this.evaluate(this.expression);
		}
		return expressionResult;
	}
	
	/**
	 * Evaluates the specified expression (also recursively if necessary).
	 *
	 * @param expression the expression
	 * @return the object
	 */
	private Object evaluate(Expression expression) {
		
		// --- Check for errors in the expression -------------------
		if (expression.hasErrors()==true) {
			// TODO
		}
		
		// --- Evaluate sub expressions first -----------------------
		if (expression.getSubExpressions().size()>0) {
			for (Expression subExp : expression.getSubExpressions()) {
				subExp.getExpressionResult();
			}
		}
		
		// --- Get the service that manages the Expression ----------
		Object evaluationResult = null;
		ExpressionService eService = ExpressionServiceHelper.getExpressionService(expression.getExpressionType());
		if (eService!=null) {
			try {
				// --- Get the ExpressionServiceEvaluator ---------------
				ExpressionServiceEvaluator evaluator = eService.getExpressionServiceEvaluator();
				if (evaluator!=null) {
					evaluationResult = evaluator.evaluate(expression);
					expression.setExpressionResult(evaluationResult);
				} else {
					System.err.println("[" + this.getClass().getSimpleName() + "] Did not get evaluator for ExpressionService " + expression.getExpressionType());
				}
				
			} catch (UnknownExpressionException exex) {
				exex.printStackTrace();
			}
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Could not find ExpressionService " + expression.getExpressionType());
		}
		return evaluationResult;
	}
	
	
}
