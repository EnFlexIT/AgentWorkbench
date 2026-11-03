package de.enflexit.df.core.processing.transformation;

/**
 * The Class DataTransformationMessage.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationMessage {
	
	public enum Type {
		ERROR, WARNING, INFORMATION
	}
	
	private Type messageType;
	private String messageContent;
	
	/**
	 * Instantiates a new data transformation message.
	 */
	public DataTransformationMessage() {}

	/**
	 * Instantiates a new data transformation message with the provided type and content.
	 * @param messageType the message type
	 * @param messageContent the message content
	 */
	public DataTransformationMessage(Type messageType, String messageContent) {
		this.messageType = messageType;
		this.messageContent = messageContent;
	}

	/**
	 * Gets the message type.
	 * @return the message type
	 */
	public Type getMessageType() {
		return messageType;
	}
	/**
	 * Sets the message type.
	 * @param messageType the new message type
	 */
	public void setMessageType(Type messageType) {
		this.messageType = messageType;
	}

	/**
	 * Gets the message content.
	 * @return the message content
	 */
	public String getMessageContent() {
		return messageContent;
	}
	/**
	 * Sets the message content.
	 * @param messageContent the new message content
	 */
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	/**
	 * Creates an error message with the provided content.
	 * @param content the content
	 * @return the data transformation message
	 */
	public static final DataTransformationMessage error(String content) {
		return new DataTransformationMessage(Type.ERROR, content);
	}
	
	/**
	 * Creates a warning message with the provided content.
	 * @param content the content
	 * @return the data transformation message
	 */
	public static final DataTransformationMessage warning(String content) {
		return new DataTransformationMessage(Type.WARNING, content);
	}
	
	/**
	 * Creates an information message with the provided content.
	 * @param content the content
	 * @return the data transformation message
	 */
	public static final DataTransformationMessage information(String content) {
		return new DataTransformationMessage(Type.INFORMATION, content);
	}
	
}
