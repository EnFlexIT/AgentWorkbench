package de.enflexit.expression;

/**
 * Implement this interface to add additional checks to the validation of an expression.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface ExpressionValidatorExtension {
	
	/**
	 * Perform the validation.
	 * @return true if the expression is valid, i.e. has no errors.
	 */
	public boolean validate();

}
