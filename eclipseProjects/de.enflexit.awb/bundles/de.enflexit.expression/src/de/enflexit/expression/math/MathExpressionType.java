package de.enflexit.expression.math;

import de.enflexit.expression.ExpressionType;

/**
 * The Class MathExpressionType.
 * 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class MathExpressionType extends ExpressionType {
	
	private static MathExpressionType instance;
	
	/**
	 * Instantiates a new math expression type. Private due to singleton.
	 */
	private MathExpressionType() {}
	
	/**
	 * Gets the single instance of MathExpressionType.
	 * @return single instance of MathExpressionType
	 */
	public static MathExpressionType getInstance() {
		if (instance==null) {
			instance = new MathExpressionType();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionType#getTypePrefix()
	 */
	@Override
	public String getTypePrefix() {
		// --- Just for display purposes, 
		return "Math";
	}

}
