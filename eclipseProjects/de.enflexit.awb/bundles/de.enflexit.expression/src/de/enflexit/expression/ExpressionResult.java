package de.enflexit.expression;


/**
 * The Class ExpressionResult.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionResult {

	private Object value;
	
	private boolean isValid;
	private String errorMessage;
	
	/**
	 * Instantiates a new expression result.
	 */
	public ExpressionResult() {
		this(null);
	}
	/**
	 * Instantiates a new expression result.
	 * @param value the value to return 
	 */
	public ExpressionResult(Object returnValue) {
		this(returnValue, true, null);
	}
	/**
	 * Instantiates a new expression result.
	 *
	 * @param returnValue the return value
	 * @param isValid the validity of the current result
	 * @param errorMessage the error message corresponding to the validity
	 */
	public ExpressionResult(Object returnValue, boolean isValid, String errorMessage) {
		this.setValue(returnValue);
		this.setValid(isValid);
		this.setErrorMessage(errorMessage);
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
	 * Sets if the base expression was valid or not.
	 * @param isValid the validity indicator
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	/**
	 * Returns if the base expression was valid.
	 *
	 * @param expression the expression
	 * @return true, if is valid
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * Sets the error message corresponding error message to the current validity.
	 * @param errorMessage the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * Returns the current error message according to the current validity.
	 * @return the error message
	 * @see #isValid
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.isValid()==true) {
			return this.getExpressionDataType().toString() + ": " + this.getValue().toString();
		} 
		return "Invalid: " + this.getErrorMessage();
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
