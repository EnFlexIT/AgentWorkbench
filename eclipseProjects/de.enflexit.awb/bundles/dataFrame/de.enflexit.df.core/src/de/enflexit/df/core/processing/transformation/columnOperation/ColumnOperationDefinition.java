package de.enflexit.df.core.processing.transformation.columnOperation;

/**
 * The Class ColumnOperation.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ColumnOperationDefinition {
	
	public enum ResultHandling {
		REPLACE("Replace Column"),
		APPEND("New Column");
		
		private ResultHandling(String label) {
			this.label = label;
		}
		
		private final String label;
		
		/**
		 * Gets the label.
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
		
	}
	
	private String columnName;
    private String operationID;
    private String parameter;
    private ResultHandling resultHandling;
	
    /**
     * Gets the column name.
     * @return the column name
     */
    public String getColumnName() {
		return columnName;
	}
	/**
	 * Sets the column name.
	 * @param columnName the new column name
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	/**
	 * Gets the operation ID.
	 * @return the operation ID
	 */
	public String getOperationID() {
		return operationID;
	}
	/**
	 * Sets the operation ID.
	 * @param operationID the new operation ID
	 */
	public void setOperationID(String operationID) {
		this.operationID = operationID;
	}
	
	/**
	 * Gets the parameter.
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}
	/**
	 * Sets the parameter.
	 * @param parameter the new parameter
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	/**
	 * Gets the result handling.
	 * @return the result handling
	 */
	public ResultHandling getResultHandling() {
		return resultHandling;
	}
	/**
	 * Sets the result handling.
	 * @param resultHandling the new result handling
	 */
	public void setResultHandling(ResultHandling resultHandling) {
		this.resultHandling = resultHandling;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String operationAsString = "[" + this.getColumnName() + ";" + this.getOperationID() + ";" + this.getParameter() + ";" + this.resultHandling.toString() + "]";
		return operationAsString;
	}
	
	/**
	 * Creates a {@link ColumnOperationDefinition} from the provided string.
	 * @param operationAsString the operation as string
	 * @return the column operation
	 */
	public static ColumnOperationDefinition fromString(String operationAsString) {
		int startIndex = operationAsString.startsWith("[") ? 1 : 0;
		int endIndex = operationAsString.length() - (operationAsString.endsWith("]") ? 1 : 2);
		String[] parts = operationAsString.substring(startIndex, endIndex).split(";");
		if (parts.length!=4) {
			throw new IllegalArgumentException("Invalid operation definition: " + operationAsString);
		}
		ColumnOperationDefinition operation = new ColumnOperationDefinition();
		operation.setColumnName(parts[0]);
		operation.setOperationID(parts[1]);
		operation.setParameter(parts[2]);
		operation.setResultHandling(ResultHandling.valueOf(parts[3]));
		
		return operation;
	}
    
}
