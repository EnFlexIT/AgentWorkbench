package de.enflexit.df.core.processing.transformation.columnOperation;

public class ColumnOperation {
	
	public enum ResultHandling {
		REPLACE, APPEND
	}
	
	private String columnName;
    private String operationID;
    private String parameter;
    private ResultHandling resultHandling;
	
    public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getOperationID() {
		return operationID;
	}
	public void setOperationID(String operationID) {
		this.operationID = operationID;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public ResultHandling getResultHandling() {
		return resultHandling;
	}
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
	 * Creates a {@link ColumnOperation} from the provided string.
	 * @param operationAsString the operation as string
	 * @return the column operation
	 */
	public static ColumnOperation fromString(String operationAsString) {
		int startIndex = operationAsString.startsWith("[") ? 1 : 0;
		int endIndex = operationAsString.length() - (operationAsString.endsWith("]") ? 1 : 2);
		String[] parts = operationAsString.substring(startIndex, endIndex).split(";");
		if (parts.length!=4) {
			throw new IllegalArgumentException("Invalid operation definition: " + operationAsString);
		}
		ColumnOperation operation = new ColumnOperation();
		operation.setColumnName(parts[0]);
		operation.setOperationID(parts[1]);
		operation.setParameter(parts[2]);
		operation.setResultHandling(ResultHandling.valueOf(parts[3]));
		
		return operation;
	}
    
}
