package de.enflexit.awb.core.ui;

/**
 * The Interface AwbBenchmarkMonitor defines the required methods for a visual benchmark monitor.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface AwbBenchmarkMonitor {

	
	/**
	 * Enables/disable the skip button.
	 * @param isEnabled the new enabled skip
	 */
	public void setEnableSkipButton(boolean isEnabled);
	/**
	 * Checks if is skip.
	 * @return true, if is skip
	 */
	public boolean isSkip();

	
	/**
	 * Enables/disable the skip always button.
	 * @param isEnabled the new enabled skip always
	 */
	public void setEnableSkipAlwaysButton(boolean isEnabled);
	/**
	 * Checks if is skip always.
	 * @return true, if is skip always
	 */
	public boolean isSkipAlways();

	
	/**
	 * Sets the Benchmark Monitor visible or not.
	 * @param isVisible the new visible
	 */
	public void setVisible(boolean isVisible);
	
	/**
	 * Has to dispose the benchmark monitor
	 */
	public void dispose();
	
	/**
	 * Has to set the progress minimum.
	 * @param pMin the new progress minimum
	 */
	public void setProgressMinimum(int pMin);

	/**
	 * Has to set the progress maximum.
	 * @param pMax the new progress maximum
	 */
	public void setProgressMaximum(int pMax);

	/**
	 * Has to set the progress value.
	 * @param newValue the new progress value
	 */
	public void setProgressValue(int newValue);
	
	
	/**
	 * Sets the new benchmark value / result.
	 * @param newBenchkarkResult the new benchmark value
	 */
	public void setBenchmarkValue(float newBenchkarkResult);

	
}
