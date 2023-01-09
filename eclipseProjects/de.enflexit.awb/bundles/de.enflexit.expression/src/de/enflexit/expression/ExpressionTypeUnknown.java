package de.enflexit.expression;

/**
 * This {@link ExpressionType} is used for expressions with prefixes 
 * for which no corresponding {@link ExpressionService} is found.
 * 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionTypeUnknown extends ExpressionType {
	
	private String typePrefix;
	
	/**
	 * Instantiates a new expression type unknown.
	 * @param typePrefix the type prefix
	 */
	public ExpressionTypeUnknown(String typePrefix) {
		this.typePrefix = "Unknown: " + typePrefix;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionType#getTypePrefix()
	 */
	@Override
	public String getTypePrefix() {
		return typePrefix;
	}
	
	/**
	 * Sets the type prefix.
	 * @param typePrefix the new type prefix
	 */
	public void setTypePrefix(String typePrefix) {
		this.typePrefix = typePrefix;
	}

}
