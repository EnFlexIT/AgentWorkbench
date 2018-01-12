package org.agentgui.gui;

public interface AwbProgressMonitor {
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
	 * Sets the progress text.
	 * @param progressText the new progress text
	 */
	public void setProgressText(String progressText);
	
	/**
	 * Sets the progress.
	 * @param progress the new progress
	 */
	public void setProgress(int progress);
	
	/**
	 * Sets the component cancelable or not.
	 * @param allowToCancel the new allow to cancel
	 */
	public void setAllow2Cancel(boolean allowToCancel);
	
	/**
	 * Checks if the action has been canceled.
	 * @return true if canceled
	 */
	public boolean isCanceled();
}
