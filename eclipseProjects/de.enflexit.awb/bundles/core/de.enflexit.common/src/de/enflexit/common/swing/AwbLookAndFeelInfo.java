package de.enflexit.common.swing;

import javax.swing.LookAndFeel;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * The Class AwbLookAndFeelInfo represents a container for a Swing {@link LookAndFeelInfo}
 * that can be extended with additional information that are to be displayed.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbLookAndFeelInfo {

	private LookAndFeelInfo lookAndFeelInfo;
	private String awbInfoText;

	
	/**
	 * Instantiates a new AwbLookAndFeelInfo.
	 *
	 * @param lookAndFeelInfo the look and feel info
	 * @param awbInfoText the awb info text
	 */
	public AwbLookAndFeelInfo(LookAndFeelInfo lookAndFeelInfo, String awbInfoText) {
		this.setLookAndFeelInfo(lookAndFeelInfo);
		this.setAwbInfoText(awbInfoText);
	}
	
	/**
	 * Sets the Swing LookAndFeelInfo.
	 * @param lookAndFeelInfo the new look and feel info
	 */
	public void setLookAndFeelInfo(LookAndFeelInfo lookAndFeelInfo) {
		this.lookAndFeelInfo = lookAndFeelInfo;
	}
	/**
	 * Returns the Swing LookAndFeelInfo.
	 * @return the look and feel info
	 */
	public LookAndFeelInfo getLookAndFeelInfo() {
		return lookAndFeelInfo;
	}
	
	/**
	 * Sets the AWB info text.
	 * @param awbInfoText the new awb info text
	 */
	public void setAwbInfoText(String awbInfoText) {
		this.awbInfoText = awbInfoText;
	}
	/**
	 * Returns the AWB info text.
	 * @return the awb info text
	 */
	public String getAwbInfoText() {
		return awbInfoText;
	}

	/**
	 * Return the display description for Agent.Workbench.
	 * @return the awb display description
	 */
	public String getDescription() {
		if (this.getAwbInfoText()==null || this.getAwbInfoText().isBlank()==true) {
			return this.getName();
		}
		return this.getName() + " (" + this.getAwbInfoText() + ")";
	}
	
	/**
     * Returns the name of the look and feel in a form suitable
     * for a menu or other presentation
     * @return a <code>String</code> containing the name
     * @see LookAndFeel#getName
     */
	public String getName() {
		return this.getLookAndFeelInfo().getName();
	}
	
	/**
     * Returns the name of the class that implements this look and feel.
     * @return the name of the class that implements this
     *              <code>LookAndFeel</code>
     * @see LookAndFeel
     */
	public String getClassName() {
		return this.getLookAndFeelInfo().getClassName();
	}

}
