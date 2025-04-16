package de.enflexit.expression.functions;

import de.enflexit.expression.ExpressionType;

/**
 * The Class FunctionExpressionType.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class FunctionExpressionType extends ExpressionType {
	
	private static FunctionExpressionType instance;
	
	/**
	 * Instantiates a new function expression type. Private due to singleton.
	 */
	private FunctionExpressionType() {}
	
	/**
	 * Gets the single instance of FunctionExpressionType.
	 * @return single instance of FunctionExpressionType
	 */
	public static FunctionExpressionType getInstance() {
		if (instance==null) {
			instance = new FunctionExpressionType();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionType#getTypePrefix()
	 */
	@Override
	public String getTypePrefix() {
		return "f(x)";
	}

}
