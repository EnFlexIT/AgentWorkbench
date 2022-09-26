package de.enflexit.expression;

/**
 * Abstract superclass to specify different types of expressions.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class ExpressionType {
	
	/**
	 * Gets the type prefix, which is used in expression strings to identify expressions of this type. 
	 * @return the type prefix
	 */
	public abstract String getTypePrefix();
}
