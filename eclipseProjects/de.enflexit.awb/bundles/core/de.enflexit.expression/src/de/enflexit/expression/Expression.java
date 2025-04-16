package de.enflexit.expression;

import java.util.Vector;

/**
 * This class represents one single expression, that may have sub-expressions.
 * The class is only used internally to handle expression strings.  
 *  
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @author Christian Derksen - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class Expression {
	
	// --- Attributes for the definition ------------------
	private String expressionString;
	private Vector<Expression> subExpressions;
	
	private ExpressionType expressionType;
	private boolean hasErrors;
	private String errorMessage;
	
	
	// --- Attributes for the evaluation ------------------
	private ExpressionResult expressionResult;
	
	/**
	 * Instantiates a new, empty expression.
	 */
	public Expression() {};
	/**
	 * Instantiates a new expression, that is initialized with the provided string.
	 * @param expressionString the expression string
	 */
	public Expression(String expressionString) {
		this.expressionString = expressionString;
	}
	
	/**
	 * Gets the expression string.
	 * @return the expression string
	 */
	public String getExpressionString() {
		return expressionString;
	}
	/**
	 * Sets the expression string.
	 * @param expressionString the new expression string
	 */
	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}
	
	/**
	 * Gets the expression type.
	 * @return the expression type
	 */
	public ExpressionType getExpressionType() {
		return expressionType;
	}
	/**
	 * Sets the expression type.
	 * @param expressionType the new expression type
	 */
	public void setExpressionType(ExpressionType expressionType) {
		this.expressionType = expressionType;
	}

	/**
	 * Sets the error indicator for this expression
	 * @param hasErrors the new checks for errors
	 */
	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}
	/**
	 * Checks for errors.
	 * @return true, if this expression has errors
	 */
	public boolean hasErrors() {
		return this.hasErrors;
	}
	
	/**
	 * Sets the error message.
	 * @param errorMessage the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * Gets the error message.
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	

	/**
	 * Gets the sub expressions.
	 * @return the sub expressions
	 */
	public Vector<Expression> getSubExpressions() {
		if (subExpressions==null) {
			subExpressions = new Vector<Expression>();
		}
		return subExpressions;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, methods for the evaluation ------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Based on the global context and the specified context, returns the expression result.
	 *
	 * @param context one or more context information that needs to be considered, additionally to the global context  
	 * @return the expression result
	 */
	public ExpressionResult getExpressionResult(ExpressionContext context) {
		if (expressionResult==null) {
			expressionResult = new ExpressionEvaluator(this, context).getExpressionResult();
		}
		return expressionResult;
	}
	/**
	 * Sets the expression result.
	 * @param expressionResult the new expression result
	 */
	public void setExpressionResult(ExpressionResult expressionResult) {
		this.expressionResult = expressionResult;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.expressionString;
	}
	
	
	// --------------------------------------------------------------
	// --- From here, static methods to work with expressions ------- 
	// --------------------------------------------------------------	
	/**
	 * Parses the specified expression string and returns a corresponding {@link Expression}.
	 *
	 * @param expressionString the expression string
	 * @return the expression
	 */
	public static Expression parse(String expressionString) {
		return ExpressionParser.parse(expressionString);
	}
	/**
	 * Evaluates the specified expression string with the help of the global context and the specified context.
	 *
	 * @param expressionString the expression string
	 * @param context the specific context that needs to be considered additionally to the global context.
	 * @return the expression result
	 */
	public static ExpressionResult evaluate(String expressionString, ExpressionContext context) {
		return evaluate(Expression.parse(expressionString), context);
	}
	/**
	 * Evaluates the specified expression with the help of the global context and the specified context.
	 *
	 * @param expression the expression with the help of the global context and the specified context.
	 * @param context the specific context that needs to be considered additionally to the global context.
	 * @return the expression result
	 */
	public static ExpressionResult evaluate(Expression expression, ExpressionContext context) {
		if (expression.hasErrors()==false) {
			return expression.getExpressionResult(context);
		} 
		// --- Prepare return value -------------
		ExpressionResult er = new ExpressionResult();
		er.addErrorMessage(expression.getErrorMessage());
		return er;
	}
	
}
