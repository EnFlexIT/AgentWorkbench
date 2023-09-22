package de.enflexit.expression;

import de.enflexit.expression.functions.ExpressionFunction;
import de.enflexit.expression.functions.FunctionExpressionType;
import de.enflexit.expression.math.MathExpressionType;

/**
 * The Class ExpressionParser parses the specified expression string and converts it
 * into a an Expression that can be evaluated by the {@link ExpressionEvaluator}.
 * 
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionParser {

	/**
	 * Returns the prepared / cleaned expression string.
	 *
	 * @param expressionString the expression string
	 * @return the prepared expression string
	 */
	private static String getPreparedExpressionString(String expressionString) {
		
		expressionString = expressionString.trim();
		if (expressionString.startsWith(String.valueOf(ExpressionService.EXPRESSION_OPENING_DELIMITER)) && expressionString.endsWith(String.valueOf(ExpressionService.EXPRESSION_CLOSING_DELIMITER))) {
			String workExpressionString = expressionString.substring(1, expressionString.length()-1);
			if (workExpressionString.startsWith(String.valueOf(ExpressionService.EXPRESSION_OPENING_DELIMITER))==false && workExpressionString.endsWith(String.valueOf(ExpressionService.EXPRESSION_CLOSING_DELIMITER))==false) {
				expressionString = workExpressionString;
			}
		}
		return expressionString.trim();
	}
	
	/**
	 * Parses the provided expression String, recursively processing sub-expressions.
	 * @param expressionString the expression string
	 * @return the expression
	 */
	public static Expression parse(String expressionString) {
		
		// ------------------------------------------------------------------------------
		// --- Do some initial preparations --------------------------------------------- 
		// ------------------------------------------------------------------------------
		expressionString = ExpressionParser.getPreparedExpressionString(expressionString);
		
		// --- Define the Expression ----------------------------------------------------
		Expression expression = new Expression(expressionString);
		ExpressionType expressionType = null;
		
		if (expressionString.indexOf(ExpressionService.EXPRESSION_OPENING_DELIMITER)==-1 && ExpressionFunction.containsExpressionFunction(expressionString)==false) {
			// --------------------------------------------------------------------------
			// --- Atomic expression (i.e. contains no sub expressions)------------------
			// --------------------------------------------------------------------------

			// --- Check for type specifiers (can only occur in atomic expressions) -----
			int separatorPos = expressionString.indexOf(ExpressionService.EXPRESSION_SERVICE_TYPE_SUFFIX);
			if (separatorPos>0) {
				// --- Find the corresponding ExpressionType ----------------------------
				String typeIdentifier = expressionString.substring(0, separatorPos);
				expressionType = ExpressionServiceHelper.getExpressionType(typeIdentifier);
				
				if (expressionType==null) {
					// -- No matching ExpressionType found ------------------------------
					expressionType = new ExpressionTypeUnknown(typeIdentifier);
				}
			}
			
		} else {
			// --------------------------------------------------------------------------
			// --- Not atomic, check for sub-expressions --------------------------------
			// --------------------------------------------------------------------------
			int subStrLength;
			for (int currPos = 0; currPos<expressionString.length(); currPos++) {
				
				expressionType = getExpressionType(expressionString);
				if (expressionString.charAt(currPos)==ExpressionService.EXPRESSION_OPENING_DELIMITER) {
					// --- Found opening delimiter --------------------------------------
					subStrLength = ExpressionParser.findClosingDelimiter(expressionString.substring(currPos), ExpressionService.EXPRESSION_OPENING_DELIMITER, ExpressionService.EXPRESSION_CLOSING_DELIMITER);
					// --- Found a valid sub-expression ---------------------------------
					if (subStrLength>0) {
						// --- Extract, parse and add to parent -------------------------
						String subExpressionString = expressionString.substring(currPos + 1, currPos + subStrLength);
						expression.getSubExpressions().add(ExpressionParser.parse(subExpressionString));
						// --- Continue after the sub-expression ------------------------
						currPos += subStrLength;
						
					} else {
						String errMsg = "Missing closing expression delimiter for opening delemiter at " + currPos;
						expression.setHasErrors(true);
						expression.setErrorMessage(errMsg);
					}
					
				} else if (ExpressionFunction.getExpressionFunctionPrefix(expressionString.substring(currPos))!=null) {

					int startPos = expressionString.substring(currPos).indexOf(ExpressionService.EXPRESSION_FUNCTION_OPENING_DELIMITER);
					if (startPos!=-1) {
						// --- Found expression function --------------------------------
						subStrLength = ExpressionParser.findClosingDelimiter(expressionString.substring(currPos), ExpressionService.EXPRESSION_FUNCTION_OPENING_DELIMITER, ExpressionService.EXPRESSION_FUNCTION_CLOSING_DELIMITER);
						// --- Found an expression function------------------------------
						if (subStrLength>0) {
							// --- Extract, parse and add to parent ---------------------
							String subExpressionString = expressionString.substring(currPos, currPos + subStrLength + 1);
							if (subExpressionString.equals(expressionString)==true) {
								expressionType = FunctionExpressionType.getInstance();
								// --- Get arguments as a sub expressions ---------------
								ExpressionParser.addFunctionArgumentExpressions(expression, subExpressionString);
							} else {
								expression.getSubExpressions().add(ExpressionParser.parse(subExpressionString));
							}
							// --- Continue after the sub-expression --------------------
							currPos += subStrLength + 1;
							
						} else {
							String errMsg = "Missing closing function delimiter for opening delemiter at " + currPos;
							expression.setHasErrors(true);
							expression.setErrorMessage(errMsg);
						}
					}
						
				}
					
			} // -- end for ---
		}
		
		// --- If no other type was determined, use math as default ---------------------
		expression.setExpressionType(expressionType==null ? MathExpressionType.getInstance() : expressionType);
		
		return expression;
	}
	
	private static ExpressionType getExpressionType(String expressionString) {
		ExpressionType expressionType = null;
		int separatorPos = expressionString.indexOf(ExpressionService.EXPRESSION_SERVICE_TYPE_SUFFIX);
		if (separatorPos>0) {
			// --- Find the corresponding ExpressionType ----------------------------
			
			String typeIdentifier = expressionString.substring(0, separatorPos);
			expressionType = ExpressionServiceHelper.getExpressionType(typeIdentifier);
		}
		
		return expressionType;
	}
	
	/**
	 * Finds the corresponding closing delimiter in a string that starts with an opening delimiter.
	 *
	 * @param expressionString the expression string in which to find the closing delimiter
	 * @param openingDelimiter the opening delimiter
	 * @param closingDelimiter the closing delimiter
	 * @return the index position of the closing delimiter, if found or -1
	 */
	private static int findClosingDelimiter(String expressionString, char openingDelimiter, char closingDelimiter) {
		int depth = 0;
		for (int i=0; i<expressionString.length(); i++) {
			char currentChar = expressionString.charAt(i); 
			if (currentChar==openingDelimiter) {
				// --- Nested delimiter opened ------------ 
				depth++;
			} else if (currentChar==closingDelimiter) {
				// --- Nested delimiter closed ------------
				depth--;
				// --- Matching closing delimiter ---------
				if (depth==0) {
					return i;
				}
			}
		}
		// --- No matching closing delimiter found --------
		return -1; 
	}
	
	/**
	 * Adds the function argument expressions.
	 *
	 * @param expression the expression
	 * @param expressionFunction the expression function
	 */
	private static void addFunctionArgumentExpressions(Expression expression, String expressionFunction) {
		
		int start = expressionFunction.indexOf(ExpressionService.EXPRESSION_FUNCTION_OPENING_DELIMITER) + 1;
		int end = expressionFunction.lastIndexOf(ExpressionService.EXPRESSION_FUNCTION_CLOSING_DELIMITER);
		
		String  argumentString = expressionFunction.substring(start, end);
		if (ExpressionFunction.getExpressionFunctionPrefix(argumentString)!=null) {
			// --- Contains further expression functions -> parse -------------
			expression.getSubExpressions().add(Expression.parse(argumentString));
		} else {
			// --- Contains only arguments -------------------------------------
			String[] argumentArray = argumentString.split(String.valueOf(ExpressionService.EXPRESSION_FUNCTION_ARGUMENT_DELIMITER));
			for (int i = 0; i < argumentArray.length; i++) {
				expression.getSubExpressions().add(ExpressionParser.parse(argumentArray[i]));
			}
		}
	}
	
}
