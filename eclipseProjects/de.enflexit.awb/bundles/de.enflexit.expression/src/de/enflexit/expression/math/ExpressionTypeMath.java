package de.enflexit.expression.math;

import de.enflexit.expression.ExpressionType;

/**
 * The Class ExpressionTypeMath.
 * 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionTypeMath extends ExpressionType {
	
	private static ExpressionTypeMath instance;
	
	/**
	 * Instantiates a new expression type math. Private due to singleton.
	 */
	private ExpressionTypeMath() {}
	
	/**
	 * Gets the single instance of ExpressionTypeMath.
	 * @return single instance of ExpressionTypeMath
	 */
	public static ExpressionTypeMath getInstance() {
		if (instance==null) {
			instance = new ExpressionTypeMath();
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
