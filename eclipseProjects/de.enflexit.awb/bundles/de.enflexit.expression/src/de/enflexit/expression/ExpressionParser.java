package de.enflexit.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.enflexit.common.ServiceFinder;
import de.enflexit.expression.math.ExpressionTypeMath;

public class ExpressionParser {
	
	private static final char openingDelimiter = '[';
	private static final char closingDelimiter = ']';
	
	private HashMap<ExpressionType, ExpressionService> availableExpressionServices;

	/**
	 * Provides a dictionary of all available {@link ExpressionService}s, accessible by the corresponding {@link ExpressionType}.
	 * @return the available expression services
	 */
	public HashMap<ExpressionType, ExpressionService> getAvailableExpressionServices() {
		if (availableExpressionServices==null) {
			availableExpressionServices = new HashMap<>();
			List<ExpressionService> expressionServices = ServiceFinder.findServices(ExpressionService.class);
			for (int i=0; i<expressionServices.size(); i++) {
				ExpressionService expressionService = expressionServices.get(i);
				availableExpressionServices.put(expressionService.getExpressionType(), expressionService);
			}
		}
		return availableExpressionServices;
	}
	
	/**
	 * Parses the provided expression String, recursively processing sub-expressions.
	 * @param expressionString the expression string
	 * @return the expression
	 */
	public Expression parse (String expressionString) {
		
		Expression expression = new Expression(expressionString);
		ExpressionType expressionType = null;
		
		// --- Check if the expression is atomic (i.e. contains no sub expressions)
		if (expressionString.indexOf(openingDelimiter)==-1) {
			
			// --- Check for type specifiers (can only occur in atomic expressions
			int separatorPos = expressionString.indexOf('!');
			if (separatorPos>0) {
				
				// --- Find the corresponding ExpressionType ------------------
				String typeIdentifier = expressionString.substring(0, separatorPos);
				expressionType = this.getExpressionType(typeIdentifier);
				
				if (expressionType==null) {
					// -- No matching ExpressionType found --------------------
					expressionType = new ExpressionTypeUnknown(typeIdentifier);
				}
			}
			
		} else {
			
			// --- Not atomic, check for sub-expressions ----------------------
			int subStrBegin, subStrLength;
			for (int currPos = 0; currPos<expressionString.length(); currPos++) {
				if (expressionString.charAt(currPos)==openingDelimiter) {
					subStrBegin = currPos;
					subStrLength = this.findClosingDelimiter(expressionString.substring(currPos));
					
					// --- Found a valid sub-expression -----------------
					if (subStrLength>0) {
						
						// --- Extract, parse and add to parent ---------
						String subExpressionString = expressionString.substring(subStrBegin+1, subStrBegin+subStrLength);
						expression.getSubExpressions().add(this.parse(subExpressionString));
						
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
		if (expressionType==null) {
			expressionType = ExpressionTypeMath.getInstance();
		}
		expression.setExpressionType(expressionType);
		
		return expression;
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
	
	private ExpressionType getExpressionType(String typePrefix) {
		ArrayList<ExpressionType> availableTypes = new ArrayList<>(this.getAvailableExpressionServices().keySet());
		for (ExpressionType type : availableTypes){
			if (type.getTypePrefix().equals(typePrefix)) {
				return type;
			}
		}
		return null;
	}
}
