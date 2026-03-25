package de.enflexit.common.properties;

/**
 * The Class PropertyMessage.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyMessage extends PropertyValue {

	private static final long serialVersionUID = 3215652313861993855L;

	public enum MessageType {
		Info,
		Warning,
		Error
	}
	
	private MessageType messageType;
	private String message;
	
	
	/**
	 * Instantiates a new property message.
	 *
	 * @param mType the m type
	 * @param message the message
	 */
	public PropertyMessage(MessageType mType, String message) {
		this.setMessageType(mType);
		this.setMessage(message);
	}
	
	/**
	 * Returns the message type.
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
	 * Returns the message.
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
	
}
