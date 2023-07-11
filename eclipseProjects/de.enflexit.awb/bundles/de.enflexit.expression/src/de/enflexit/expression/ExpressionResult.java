package de.enflexit.expression;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.expression.ExpressionData.DataColumn;
import de.enflexit.expression.ExpressionData.DataType;

/**
 * The Class ExpressionResult.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionResult {

	public enum MessageType {
		Information,
		Warning,
		Error
	}
	
	private ExpressionData expressionData;
	private List<Message> messageList;

	
	/**
	 * Instantiates a new expression result.
	 */
	public ExpressionResult() { }
	/**
	 * Instantiates a new expression result.
	 * @param expressionData the expression data
	 */
	public ExpressionResult(ExpressionData expressionData) {
		this.setExpressionData(expressionData);
	}

	/**
	 * Instantiates a new expression result.
	 * @param boolValue the single boolean value
	 */
	public ExpressionResult(boolean boolValue) {
		this.setExpressionData(new ExpressionData(boolValue));
	}
	/**
	 * Instantiates a new expression result.
	 * @param intValue the single integer value
	 */
	public ExpressionResult(int intValue) {
		this.setExpressionData(new ExpressionData(intValue));
	}
	/**
	 * Instantiates a new expression result.
	 * @param doubleValue the single double value
	 */
	public ExpressionResult(double doubleValue) {
		this.setExpressionData(new ExpressionData(doubleValue));
	}
	
	/**
	 * Instantiates a new expression result.
	 * @param booleanArray the boolean array
	 */
	public ExpressionResult(boolean[] booleanArray) {
		this.setExpressionData(new ExpressionData(booleanArray));
	}
	
	/**
	 * Instantiates a new expression result.
	 * @param intArray the integer array
	 */
	public ExpressionResult(int[] intArray) {
		this.setExpressionData(new ExpressionData(intArray));
	}
	/**
	 * Instantiates a new expression result.
	 * @param doubleArray the double array
	 */
	public ExpressionResult(double[] doubleArray) {
		this.setExpressionData(new ExpressionData(doubleArray));
	}
	
	/**
	 * Instantiates a new ExpressionResult that contains a time series.
	 * @param tsd the {@link TimeSeriesDescription} that describes the expression data structure 
	 */
	public ExpressionResult(TimeSeriesDescription tsd) {
		this.setExpressionData(new ExpressionData(tsd));
	}
	
	
	/**
	 * Gets the expression data.
	 * @return the expression data
	 */
	public ExpressionData getExpressionData() {
		return expressionData;
	}
	/**
	 * Sets the expression data.
	 * @param expressionData the new expression data
	 */
	public void setExpressionData(ExpressionData expressionData) {
		this.expressionData = expressionData;
	}
	
	
	// ----------------------------------------------------------------------------------
	// --- From here, simplifying access methods for simple ExpressionData instances ----
	// ----------------------------------------------------------------------------------
	// --- General requests -----------
	/**
	 * Returns if the current ExpressionData instance consist of a single data column result.
	 * @return true, if is single data column result
	 */
	public boolean isSingleDataColumnResult() {
		return this.getExpressionData()!=null ? this.getExpressionData().isSingleDataColumnResult() : false;
	}
	/**
	 * Returns if the current ExpressionData instance is a single value result.
	 * @return true, if is single data column result
	 */
	public boolean isSingleValueResult() {
		return this.getExpressionData()!=null ? this.getExpressionData().isSingleValueResult() : false;
	}
	/**
	 * If this ExpressionData instance was specified with as single {@link DataColumn}, this
	 * method returns, if the current value represents an array or not.
	 * @return true, if the value is an array or <code>null</code> if the current instance contains less or more that one data column
	 */
	public boolean isArray() {
		return this.getExpressionData()!=null ? this.getExpressionData().isArray() : false; 
	}
	/**
	 * If this ExpressionData instance was specified with as single {@link DataColumn}, this
	 * method returns the specified {@link DataType}.
	 *
	 * @return the data type  or <code>null</code> if the current instance contains less or more that one data column
	 */
	public DataType getDataType() {
		return this.getExpressionData()!=null ? this.getExpressionData().getDataType() : null;
	}
	
	// --- Single Value handling ------
	/**
	 * Returns the single boolean value if the class was initiated with as single boolean value.
	 * @return the boolean value or <code>null</code>
	 */
	public Boolean getBooleanValue() {
		return this.getExpressionData()!=null ? this.getExpressionData().getBooleanValue() : null;
	}
	/**
	 * Returns the single integer value if the class was initiated with as single integer value.
	 * @return the boolean value or <code>null</code>
	 */
	public Integer getIntegerValue() {
		return this.getExpressionData()!=null ? this.getExpressionData().getIntegerValue() : null;
	}
	/**
	 * Returns the single Long value if the class was initiated with as single Long value.
	 * @return the boolean value or <code>null</code>
	 */
	public Long getLongValue() {
		return this.getExpressionData()!=null ? this.getExpressionData().getLongValue() : null;
	}
	/**
	 * Returns the single Double value if the class was initiated with as single double or as single integer value.
	 * @return the boolean value or <code>null</code>
	 */
	public Double getDoubleValue() {
		return this.getExpressionData()!=null ? this.getExpressionData().getDoubleValue() : null;
	}
	
	// --- Array Value handling -------
	/**
	 * Returns the boolean array if the class only contains as single boolean array.
	 * @return the boolean array or <code>null</code>
	 */
	public Boolean[] getBooleanArray() {
		return this.getExpressionData()!=null ? this.getExpressionData().getBooleanArray() : null;
	}
	/**
	 * Returns the integer array if the class only contains as single integer array.
	 * @return the boolean array or <code>null</code>
	 */
	public Integer[] getIntegerArray() {
		return this.getExpressionData()!=null ? this.getExpressionData().getIntegerArray() : null;
	}
	/**
	 * Returns the Long array if the class only contains as single Long array.
	 * @return the boolean array or <code>null</code>
	 */
	public Long[] getLongArray() {
		return this.getExpressionData()!=null ? this.getExpressionData().getLongArray() : null;
	}
	/**
	 * Returns the Double array if the class only contains as single double array.
	 * @return the boolean array or <code>null</code>
	 */
	public Double[] getDoubleArray() {
		return this.getExpressionData()!=null ? this.getExpressionData().getDoubleArray() : null;
	}

	// --- Time series handling -------
	/**
	 * Checks if the current data represents a time series.
	 * @return true, if this is time series
	 */
	public boolean isTimeSeries() {
		return this.getExpressionData()!=null ? this.getExpressionData().isTimeSeries() : false;
	}
	/**
	 * Adds the specified data row.
	 *
	 * @param timestamp the timestamp
	 * @param cellValues the cell values
	 * @throws TimeSeriesException 
	 */
	public boolean addDataRow(long timestamp, Object ... cellValues) throws TimeSeriesException {
		return this.getExpressionData()!=null ? this.getExpressionData().addDataRow(timestamp, cellValues) : false;
	}
	/**
	 * Adds the specified data row to a time series.
	 *
	 * @param timestamp the timestamp of the data
	 * @param doOverwrite the indicator to overwrite already available data
	 * @param cellValues the cell values
	 * @return true, if successful
	 * @throws TimeSeriesException the time series exception
	 */
	public boolean addDataRow(long timestamp, boolean doOverwrite, Object ... cellValues) throws TimeSeriesException {
		return this.getExpressionData()!=null ? this.getExpressionData().addDataRow(timestamp, doOverwrite, cellValues) : false;
	}
	/**
	 * Returns the data row of a time series including the timestamp as first value.
	 *
	 * @param timestamp the timestamp
	 * @return the data row as Object array including the timestamp as first value.
	 */
	public Object[] getDataRow(long timestamp) {
		return this.getExpressionData()!=null ? this.getExpressionData().getDataRow(timestamp) : null;
	}
	/**
	 * Returns the data row for the specified row index.
	 *
	 * @param rowIndex the row index
	 * @return the data row as Object array including the timestamp as first value.
	 */
	public Object[] getDataRow(int rowIndex) {
		return this.getExpressionData()!=null ? this.getExpressionData().getDataRow(rowIndex) : null;
	}
	/**
	 * Sets the value for the specified time in the specified column.
	 *
	 * @param timestamp the timestamp
	 * @param columnIndex the column index, where '0' represents the timestamp column
	 * @param value the value to set
	 * @return the element previously at the specified position
	 */
	public Object setValueAt(long timestamp, int columnIndex, Object value) {
		return this.getExpressionData()!=null ? this.getExpressionData().setValueAt(timestamp , columnIndex, value) : null;
	}
	/**
	 * Returns the value for the specified time in the specified column.
	 *
	 * @param timestamp the timestamp
	 * @param columnIndex the column index, where '0' represents the timestamp column
	 * @param value the value to set
	 * @return true, if successful
	 */
	public Object getValueAt(long timestamp, int columnIndex) {
		return this.getExpressionData()!=null ? this.getExpressionData().getValueAt(timestamp , columnIndex) : null;
	}
	
	/**
	 * Set the value for the specified row and column index.
	 *
	 * @param rowIndex the row index
	 * @param columnIndex the column index, where '0' represents the timestamp column
	 * @param value the value to set
	 * @return the element previously at the specified position
	 */
	public Object setValueAt(int rowIndex, int columnIndex, Object value) {
		return this.getExpressionData()!=null ? this.getExpressionData().setValueAt(rowIndex, columnIndex, value) : null;
	}
	/**
	 * Return the value at the specified row and column index..
	 *
	 * @param rowIndex the row index
	 * @param columnIndex the column index
	 * @return the value at
	 */
	public Object getValueAt(int rowIndex, int columnIndex ) {
		return this.getExpressionData()!=null ? this.getExpressionData().getValueAt(rowIndex, columnIndex) : null;
	}
	
	/**
	 * Returns the row index for the specified timestamp.
	 *
	 * @param timeStamp the time stamp
	 * @return the row index or -1, if no value could be found for the timestamp
	 */
	public int getRowIndex(long timeStamp) {
		return this.getExpressionData()!=null ? this.getExpressionData().getRowIndex(timeStamp) : -1;
	}
	
	
	/**
	 * Returns the data type description of the current expression result.
	 * @return the data type description
	 */
	public String getDataTypeDescription() {
		return this.getExpressionData()==null ? "Result Type Undefined" : this.getExpressionData().getDataTypeDescription();
	}
	/**
	 * Returns the data value description.
	 * @return the data value description
	 */
	public String getDataValueDescription() {
		return this.getExpressionData()==null ? "Result Value Undefined" : this.getExpressionData().getDataValueDescription();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getDataTypeDescription() + ", " + this.getDataValueDescription();
	}
	
	
	
	// ----------------------------------------------------------------------------------
	// --- From here, message handling for ExpressionResult -----------------------------
	// ----------------------------------------------------------------------------------	
	/**
	 * Returns the current message list.
	 * @return the message list
	 */
	public List<Message> getMessageList() {
		if (messageList==null) {
			messageList = new ArrayList<>();
		}
		return messageList;
	}
	/**
	 * Adds the specified message instance to the local list of messages.
	 * @param message the type message
	 */
	public void addMessage(Message message) {
		this.getMessageList().add(message);
	}
	/**
	 * Adds an information message to the local list of messages.
	 * @param message the string message
	 */
	public void addInformationMessage(String message) {
		if (message==null || message.isBlank()==true) return;
		this.getMessageList().add(new Message(MessageType.Information, message));
	}
	/**
	 * Adds a warning message to the local list of messages.
	 * @param message the string message
	 */
	public void addWarningMessage(String message) {
		if (message==null || message.isBlank()==true) return;
		this.getMessageList().add(new Message(MessageType.Warning, message));
	}
	/**
	 * Adds a error message to the local list of messages.
	 * @param message the string message
	 */
	public void addErrorMessage(String message) {
		if (message==null || message.isBlank()==true) return;
		this.getMessageList().add(new Message(MessageType.Error, message));
	}
	
	/**
	 * Checks for messages.
	 * @return true, if successful
	 */
	public boolean hasMessages() {
		return this.getMessageList().size()>0;
	}
	/**
	 * Checks if the current ExpressionResults contains warning messages.
	 * @return true, if a warning message could be found 
	 */
	public boolean hasWarnings() {
		for (Message message : this.getMessageList()) {
			if (message.getMessageType()==MessageType.Warning) return true;
		}
		return false;
	}
	/**
	 * Checks if the current ExpressionResults contains error messages.
	 * @return true, if an error message could be found 
	 */
	public boolean hasErrors() {
		for (Message message : this.getMessageList()) {
			if (message.getMessageType()==MessageType.Error) return true;
		}
		return false;
	}
	
	
	/**
	 * The Class Message describes a corpus for a message that can 
	 * be provided with a {@link ExpressionResult}.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class Message {
		
		private MessageType messageType;
		private String message;
		
		/**
		 * Instantiates a new message that can be used within a {@link ExpressionResult}.
		 *
		 * @param messageType the message type
		 * @param message the message
		 */
		public Message(MessageType messageType, String message) {
			this.setMessageType(messageType);
			this.setMessage(message);
		}

		/**
		 * Gets the message type.
		 * @return the message type
		 */
		public MessageType getMessageType() {
			return messageType;
		}
		/**
		 * Sets the message type.
		 * @param messageType the new message type
		 */
		public void setMessageType(MessageType messageType) {
			this.messageType = messageType;
		}

		/**
		 * Gets the message.
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
		/**
		 * Sets the message.
		 * @param message the new message
		 */
		public void setMessage(String message) {
			this.message = message;
		}
	} // end sub class


}
