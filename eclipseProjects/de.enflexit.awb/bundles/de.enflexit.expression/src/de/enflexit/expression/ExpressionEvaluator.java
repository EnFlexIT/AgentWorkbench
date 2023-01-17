package de.enflexit.expression;

/**
 * The Class ExpressionEvaluator will evaluate the specified {@link Expression}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionEvaluator {

	private Expression expression;
	private Object context;

	private ExpressionResult expressionResult;
	
	/**
	 * Instantiates a new expression evaluator.
	 *
	 * @param expression the expression to evaluate
	 * @param context one or more context information that needs to be considered, additionally to the global context
	 */
	public ExpressionEvaluator(Expression expression, Object... context) {
		this.expression = expression;
		this.context = context;
	}
	/**
	 * Evaluates the locally specified {@link Expression}.
	 * @return the result object
	 */
	public ExpressionResult getExpressionResult() {
		if (this.expression==null) throw new IllegalArgumentException("No Expression was specified to be evaluated!");
		if (this.expressionResult==null) {
			this.expressionResult = this.evaluate();
		}
		return expressionResult;
	}
	
	/**
	 * Evaluates the specified expression (also recursively if necessary).
	 *
	 * @param expression the expression
	 * @return the object
	 */
	private ExpressionResult evaluate() {
		
		// --- Evaluate sub expressions first -----------------------
		if (this.expression.getSubExpressions().size()>0) {
			for (Expression subExp : this.expression.getSubExpressions()) {
				subExp.getExpressionResult(this.context);
			}
		}
		
		// --- Get the service that manages the Expression ----------
		ExpressionResult evaluationResult = null;
		ExpressionService eService = ExpressionServiceHelper.getExpressionService(this.expression.getExpressionType());
		if (eService!=null) {
			try {
				// --- Get the ExpressionServiceEvaluator ---------------
				ExpressionServiceEvaluator evaluator = eService.getExpressionServiceEvaluator();
				if (evaluator!=null) {
					evaluationResult = evaluator.evaluate(this.expression);
					expression.setExpressionResult(evaluationResult);
				} else {
					System.err.println("[" + this.getClass().getSimpleName() + "] Did not get evaluator for ExpressionService " + this.expression.getExpressionType());
				}
				
			} catch (UnknownExpressionException exex) {
				exex.printStackTrace();
			}
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Could not find ExpressionService " + this.expression.getExpressionType());
		}
		return evaluationResult;
	}
	
	
}
