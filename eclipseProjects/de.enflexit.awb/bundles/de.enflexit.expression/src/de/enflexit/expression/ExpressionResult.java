package de.enflexit.expression;

import java.util.ArrayList;
import java.util.List;

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
	public ExpressionResult() {
	}
	/**
	 * Instantiates a new expression result.
	 * @param expressionData the expression data
	 */
	public ExpressionResult(ExpressionData expressionData) {
		this.setExpressionData(expressionData);
	}

	/**
	 * Instantiates a new expression result.
	 * @param expressionData the expression data
	 */
	public ExpressionResult(boolean boolValue) {
		this.setExpressionData(new ExpressionData(boolValue));
	}
	/**
	 * Instantiates a new expression result.
	 * @param expressionData the expression data
	 */
	public ExpressionResult(int intValue) {
		this.setExpressionData(new ExpressionData(intValue));
	}
	/**
	 * Instantiates a new expression result.
	 * @param expressionData the expression data
	 */
	public ExpressionResult(double doubleValue) {
		this.setExpressionData(new ExpressionData(doubleValue));
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
	 * Returns the single Double value if the class was initiated with as single double or as single integer value.
	 * @return the boolean value or <code>null</code>
	 */
	public Double getDoubleValue() {
		return this.getExpressionData()!=null ? this.getExpressionData().getDoubleValue() : null;
	}
	
	
	
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
