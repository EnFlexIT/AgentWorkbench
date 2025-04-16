package org.awb.env.networkModel.settings.ui;

/**
 * The Class ComponentTypeError is used to describe an error.
 */
public class ComponentTypeError {

	private String message;
	private String title;

	/**
	 * Instantiates a new error.
	 *
	 * @param title   the title
	 * @param message the message
	 */
	public ComponentTypeError(String title, String message) {
		this.setTitle(title);
		this.setMessage(message);
	}

	/**
	 * Gets the title.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Sets the title.
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @param messag the new message
	 */
	public void setMessage(String messag) {
		this.message = messag;
	}

}
