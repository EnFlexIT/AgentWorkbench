package de.enflexit.awb.core.ui;

/**
 * The Interface AwbPasswordDialog.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbPasswordDialog {

	/**
	 * Sets the component visible or invisible.
	 * @param visible the new visible
	 */
	public void setVisible(boolean visible);
	
	/**
	 * Checks if the component is visible.
	 *
	 * @return true if visible
	 */
	public boolean isVisible();
	
	/**
	 * Disposes the component.
	 */
	public void dispose();
	
	/**
	 * Sets the window title.
	 * @param windowTitle the new window title
	 */
	public void setWindowTitle(String windowTitle);
	
	/**
	 * Sets the header text.
	 * @param headerText the new header text
	 */
	public void setHeaderText(String headerText);
	
	/**
	 * Sets the confirm password.
	 * @param isConfirmPassword the new confirm password
	 */
	public void setConfirmPassword(boolean isConfirmPassword);
	
	/**
	 * Has to check if the password entering process was canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled();
	
	/**
	 * Has to return the password as an character array.
	 * @return the password
	 */
	public char[] getPassword();
	
}
