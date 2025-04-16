package de.enflexit.common.swing;

/**
 * The Interface AwbTabbedPaneHeaderPainter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbTabbedPaneHeaderPainter {

	/**
	 * Sets the tab header visible (or not).
	 * @param tabHeaderVisible the indicator to set tab header visible
	 */
	public void setTabHeaderVisible(boolean tabHeaderVisible);

	/**
	 * Checks if tab headers are visible.
	 * @return true, if tab headers are visible
	 */
	public boolean isTabHeaderVisible();

}