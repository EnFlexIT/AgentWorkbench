package de.enflexit.expression;


/**
 * The Class ExpressionReturnValue.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionReturnValue {

	private Object value;
	
	/**
	 * Instantiates a new expression return value.
	 */
	public ExpressionReturnValue() {
		this(null);
	}
	/**
	 * Instantiates a new expression return value.
	 * @param value the value to return 
	 */
	public ExpressionReturnValue(Object returnValue) {
		this.setValue(returnValue);
	}
	
	
	/**
	 * Sets the return value.
	 * @param value the new return value
	 */
	public void setValue(Object returnValue) {
		this.value = returnValue;
	}
	/**
	 * Gets the return value.
	 * @return the return value
	 */
	public Object getValue() {
		return value;
	}
	
	
	/**
	 * Returns the expression data type of the current value.
	 * @return the expression data type
	 */
	public ExpressionDataType getExpressionDataType() {
		
		if (this.value!=null) {
			
			if (this.value instanceof Boolean) {
				return ExpressionDataType.Boolean;
			} else if (this.value instanceof Integer) {
				return ExpressionDataType.Integer;
			} else if (this.value instanceof Double) {
				return ExpressionDataType.Double;
				
			} else if (this.value.getClass().isArray()==true) {
				// TODO
				
				
			}	
		}
		return null;
	}
	/**
	 * Checks if the current value is of the specified expression data type.
	 *
	 * @param dataType the expression data type to validate for
	 * @return true, if the current value is of the specified {@link ExpressionDataType}
	 */
	public boolean isExpressionDataType(ExpressionDataType dataType) {
		if (dataType==this.getExpressionDataType()) {
			return true;
		}
		return false;
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here methods to convert to the requested data type ----------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Return the current boolean value or <code>null</code> if not of type boolean.
	 * @return the current boolean value
	 */
	public Boolean getBooleanValue() {
		if (this.getExpressionDataType()==ExpressionDataType.Boolean) {
			return (Boolean) value;
		}
		return null;
	}
	/**
	 * Return the current integer value or <code>null</code> if not of type integer.
	 * @return the current integer value
	 */
	public Integer getIntegerValue() {
		if (this.getExpressionDataType()==ExpressionDataType.Integer) {
			return (Integer) value;
		}
		return null;
	}
	/**
	 * Return the current double value or <code>null</code> if not of type double.
	 * @return the current double value
	 */
	public Double getDoubleValue() {
		if (this.getExpressionDataType()==ExpressionDataType.Double) {
			return (Double) value;
		}
		return null;
	}
	
	
	
	
	
}
