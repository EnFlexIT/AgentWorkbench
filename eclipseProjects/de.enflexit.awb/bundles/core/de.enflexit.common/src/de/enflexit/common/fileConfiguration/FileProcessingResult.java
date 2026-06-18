package de.enflexit.common.fileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class FileProcessingResult.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileProcessingResult {
	
	private boolean isSuccess;
	private String message;
	private List<String> errorList;
	
	
	/**
	 * Returns whether the file was processed successfully.
	 * @return true, if successful
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	/**
	 * Sets success for the file processing
	 * @param success the new success
	 */
	public void setSuccess(boolean success) {
		this.isSuccess = success;
	}
	
	/**
	 * Returns the result message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Sets the result message.
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Returns the errorList.
	 * @return the errorList
	 */
	public List<String> getErrorList() {
		if (errorList == null) {
			errorList = new ArrayList<String>();
		}
		return errorList;
	}
	/**
	 * Sets the errorList.
	 * @param errorList the new errorList
	 */
	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}
	/**
	 * Adds the error to the list.
	 * @param error the error
	 */
	public void addError(String error) {
		this.getErrorList().add(error);
	}
	
}