package de.enflexit.expression;

import java.util.Vector;

/**
 * This class represents one single expression, that may have sub-expressions.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class Expression {
	
	private String expressionString;
	private Vector<Expression> subExpressions;
	
	private ExpressionType expressionType;
	
	private boolean hasErrors;
	
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.expressionString;
	}
}
