package de.enflexit.df.core.expression;

import de.enflexit.expression.ExpressionType;

/**
 * The Class DataFrameExpressionType.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFrameExpressionType extends ExpressionType {
	
	private static DataFrameExpressionType instance;
	
	/**
	 * Instantiates a new data frame expression type.
	 */
	private DataFrameExpressionType() {	}

	/**
	 * Gets the single instance of DataFrameExpressionType.
	 * @return single instance of DataFrameExpressionType
	 */
	public static DataFrameExpressionType getInstance() {
		if (instance==null) {
			instance = new DataFrameExpressionType();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionType#getTypePrefix()
	 */
	@Override
	public String getTypePrefix() {
		return DataFrameExpressionConstants.DF_EXPRESSION_MARKER;
	}
}
