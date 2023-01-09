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
	
	// --- Attributes for the evaluation ------------------
	private Object expressionResult;
	private Vector<Object> subExpressionResults;
	
	
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
	 * Gets the sub expressions.
	 * @return the sub expressions
	 */
	public Vector<Expression> getSubExpressions() {
		if (subExpressions==null) {
			subExpressions = new Vector<Expression>();
		}
		return subExpressions;
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
	
	
	// ------------------------------------------------------------------------
	// --- From here, methods for the evaluation ------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Returns the expression result.
	 * @return the expression result
	 */
	public Object getExpressionResult() {
		if (expressionResult==null) {
			expressionResult = new ExpressionEvaluator(this).getExpressionResult();
		}
		return expressionResult;
	}
	/**
	 * Sets the expression result.
	 * @param expressionResult the new expression result
	 */
	public void setExpressionResult(Object expressionResult) {
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
	// --- From here some static help methods ----------------------- 
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
	
}
