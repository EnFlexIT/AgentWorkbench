package de.enflexit.expression;

import java.util.Vector;

/**
 * This class represents one single expression, that may have sub-expressions.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class Expression {
	
	private static final char openingDelimiter = '[';
	private static final char closingDelimiter = ']';
	
	private String expressionString;
	private Vector<Expression> subExpressions;
	
	private ExpressionType expressionType;
	private String expressionTypePrefix;
	
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
	 * Gets the expression type prefix.
	 * @return the expression type prefix
	 */
	public String getExpressionTypePrefix() {
		return expressionTypePrefix;
	}
	/**
	 * Sets the expression type prefix.
	 * @param expressionTypePrefix the new expression type prefix
	 */
	public void setExpressionTypePrefix(String expressionTypePrefix) {
		this.expressionTypePrefix = expressionTypePrefix;
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
	 * Parses the expression.
	 * @return the expression
	 */
	public Expression parse() {
		
		// --- Check if the expression is atomic (i.e. contains no sub expressions)
		if (expressionString.indexOf(openingDelimiter)==-1) {
			
			// --- Check for type specifiers (can only occur in atomic expressions
			int separatorPos = this.expressionString.indexOf('!');
			if (separatorPos>0) {
				String typeIdentifier = expressionString.substring(0, separatorPos);
				this.setExpressionTypePrefix(typeIdentifier);
			}
		
		} else {
			
			// --- Check for sub-expressions ----------------------------
			int subStrBegin, subStrLength;
			for (int currPos = 0; currPos<this.expressionString.length(); currPos++) {
				if (this.expressionString.charAt(currPos)==openingDelimiter) {
					subStrBegin = currPos;
					subStrLength = this.findClosingDelimiter(this.expressionString.substring(currPos));
					
					// --- Found a valid sub-expression -----------------
					if (subStrLength>0) {
						
						// --- Extract, parse and add to parent ---------
						String subString = this.expressionString.substring(subStrBegin+1, subStrBegin+subStrLength);
						Expression subExpression = new Expression(subString);
						subExpression.parse();
						this.getSubExpressions().add(subExpression);
						
						// --- Continue after the sub-expression -------- 
						currPos += subStrLength;
						
					} else {
						System.err.println("Found no matching closing delimiter for the opening delemiter at " + subStrBegin);
						this.hasErrors = true;
						return this;
					}
				}
			}
		}
		
		return this;
	}
	
	/**
	 * Finds the corresponding closing delimiter in a string that starts with an opening delimiter.
	 * @param string the string
	 * @return the int
	 */
	private int findClosingDelimiter(String string) {
		int depth = 0;
		for (int i=0; i<string.length(); i++) {
			char currentChar = string.charAt(i); 
			if (currentChar==openingDelimiter) {
				// --- Nested delimiter opened ------------ 
				depth++;
			} else if (currentChar==closingDelimiter) {
				// --- Nested delimiter closed ------------
				depth--;
				// --- Matching closing delimiter -------------
				if (depth==0) {
					return i;
				}
			}
			
		}
		
		// --- No matching closing delimiter found --------
		return -1; 
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
