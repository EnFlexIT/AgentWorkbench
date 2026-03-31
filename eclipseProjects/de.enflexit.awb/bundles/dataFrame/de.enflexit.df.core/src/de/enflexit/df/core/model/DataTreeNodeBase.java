package de.enflexit.df.core.model;

import javax.swing.ImageIcon;

/**
 * The Class DataTreeNodeBase.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeBase {

	private ImageIcon imageIcon;
	private String caption;
	private String tooltipText;
	
	
	/**
	 * Instantiates a new data tree node object.
	 */
	public DataTreeNodeBase() {
		this(null);
	}
	/**
	 * Instantiates a new data tree node object.
	 * @param caption the caption
	 */
	public DataTreeNodeBase(String caption) {
		this(null, caption, null);
	}
	/**
	 * Instantiates a new data tree node object.
	 *
	 * @param icon the icon
	 * @param caption the caption
	 * @param toolTipText the tool tip text
	 */
	public DataTreeNodeBase(ImageIcon icon, String caption, String toolTipText) {
		this.setImageIcon(icon);
		this.setCaption(caption);
		this.setTooltipText(toolTipText);
	}
	
	/**
	 * Returns the image icon.
	 * @return the image icon
	 */
	public ImageIcon getImageIcon() {
		return imageIcon;
	}
	/**
	 * Sets the image icon.
	 * @param imageIcon the new image icon
	 */
	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}
	
	/**
	 * Returns the caption.
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * Sets the caption.
	 * @param caption the new caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Gets the tool tip text.
	 * @return the tooltipText
	 */
	public String getToolTipText() {
		return tooltipText;
	}
	/**
	 * Sets the tooltipText.
	 * @param tooltipText the new tooltipText
	 */
	public void setTooltipText(String tooltiptext) {
		this.tooltipText = tooltiptext;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getCaption();
	}
	
}
