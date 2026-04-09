package de.enflexit.df.core.model;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DataTreeNodeObjectBase.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeObjectBase {

	static Logger LOGGER = LoggerFactory.getLogger(DataTreeNodeObjectBase.class);
	
	private ImageIcon imageIcon;
	private String caption;
	private String tooltipText;
	
	private String errorMessage;
	
	
	/**
	 * Instantiates a new data tree node object.
	 */
	public DataTreeNodeObjectBase() {
		this(null);
	}
	/**
	 * Instantiates a new data tree node object.
	 * @param caption the caption
	 */
	public DataTreeNodeObjectBase(String caption) {
		this(null, caption, null);
	}
	/**
	 * Instantiates a new data tree node object.
	 *
	 * @param icon the icon
	 * @param caption the caption
	 * @param toolTipText the tool tip text
	 */
	public DataTreeNodeObjectBase(ImageIcon icon, String caption, String toolTipText) {
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
	
	/**
	 * Gets the error message.
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * Sets the error message.
	 * @param errorMessage the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * Checks if there are errors to be shown.
	 * @return true, if successful
	 */
	public boolean hasErrors() {
		return this.getErrorMessage()!=null && this.getErrorMessage().isBlank()==false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getCaption();
	}
	
	
}
