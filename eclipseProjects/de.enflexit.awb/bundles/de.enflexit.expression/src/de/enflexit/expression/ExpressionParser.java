package de.enflexit.expression;

import de.enflexit.expression.math.ExpressionTypeMath;

/**
 * The Class ExpressionParser parses the specified expression string and converts it
 * into a an Expression that can be evaluated by the {@link ExpressionEvaluator}.
 * 
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionParser {
	
	/**
	 * Parses the provided expression String, recursively processing sub-expressions.
	 * @param expressionString the expression string
	 * @return the expression
	 */
	public static Expression parse(String expressionString) {
		
		Expression expression = new Expression(expressionString);
		ExpressionType expressionType = null;
		
		// --- Check if the expression is atomic (i.e. contains no sub expressions)
		if (expressionString.indexOf(ExpressionService.EXPRESSION_OPENING_DELIMITER)==-1) {
			// --- Check for type specifiers (can only occur in atomic expressions
			int separatorPos = expressionString.indexOf('!');
			if (separatorPos>0) {
				// --- Find the corresponding ExpressionType ------------------
				String typeIdentifier = expressionString.substring(0, separatorPos);
				expressionType = ExpressionServiceHelper.getExpressionType(typeIdentifier);
				
				if (expressionType==null) {
					// -- No matching ExpressionType found --------------------
					expressionType = new ExpressionTypeUnknown(typeIdentifier);
				}
			}
			
		} else {
			// --- Not atomic, check for sub-expressions ----------------------
			int subStrBegin, subStrLength;
			for (int currPos = 0; currPos<expressionString.length(); currPos++) {
				if (expressionString.charAt(currPos)==ExpressionService.EXPRESSION_OPENING_DELIMITER) {

					subStrBegin = currPos;
					subStrLength = ExpressionParser.findClosingDelimiter(expressionString.substring(currPos));
					// --- Found a valid sub-expression -----------------
					if (subStrLength>0) {
						// --- Extract, parse and add to parent ---------
						String subExpressionString = expressionString.substring(subStrBegin+1, subStrBegin+subStrLength);
						expression.getSubExpressions().add(ExpressionParser.parse(subExpressionString));
						// --- Continue after the sub-expression -------- 
						currPos += subStrLength;
						
					} else {
						System.err.println("Found no matching closing delimiter for the opening delemiter at " + subStrBegin);
						expression.setHasErrors(true);
					}
				}
			}
		}
		
		// --- If no other type was determined, use math as default ----------- 
		expression.setExpressionType(expressionType==null ? ExpressionTypeMath.getInstance() : expressionType);
		
		return expression;
	}
	
	/**
	 * Finds the corresponding closing delimiter in a string that starts with an opening delimiter.
	 * 
	 * @param expressionString the expression string in which to find the closing delimiter
	 * @return the index position of the closing delimiter, if found or -1 
	 */
	private static int findClosingDelimiter(String expressionString) {
		int depth = 0;
		for (int i=0; i<expressionString.length(); i++) {
			char currentChar = expressionString.charAt(i); 
			if (currentChar==ExpressionService.EXPRESSION_OPENING_DELIMITER) {
				// --- Nested delimiter opened ------------ 
				depth++;
			} else if (currentChar==ExpressionService.EXPRESSION_CLOSING_DELIMITER) {
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
	
	
}
