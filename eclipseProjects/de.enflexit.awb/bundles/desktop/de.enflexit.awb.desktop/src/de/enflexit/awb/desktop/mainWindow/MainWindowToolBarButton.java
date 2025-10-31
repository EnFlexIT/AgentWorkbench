package de.enflexit.awb.desktop.mainWindow;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import de.enflexit.awb.baseUI.ToolBarGroup;
import de.enflexit.awb.core.config.GlobalInfo;

/**
 * The Class MainWindowToolBarButton.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MainWindowToolBarButton extends JButton {

	private static final long serialVersionUID = 3182841608690374369L;

	private ToolBarGroup toolBarGroup;
	
	/**
	 * Instantiates a new MainWindowToolBarButton.
	 *
	 * @param actionCommand the action command
	 * @param alistener the ActionListener to be used
	 * @param toolTipText the tool tip text
	 * @param altText the alt text
	 * @param imgName the img name
	 * @param toolBarGroup the button group
	 */
	public MainWindowToolBarButton(String actionCommand, ActionListener alistener, String toolTipText, String altText, String imgName) {
		this(actionCommand, alistener, toolTipText, altText, imgName, null);
	}
	/**
	 * Instantiates a new MainWindowToolBarButton.
	 *
	 * @param actionCommand the action command
	 * @param alistener the ActionListener to be used
	 * @param toolTipText the tool tip text
	 * @param altText the alt text
	 * @param imgName the img name
	 * @param buttonGroup the button group
	 */
	public MainWindowToolBarButton(String actionCommand, ActionListener alistener, String toolTipText, String altText, String imgName, ToolBarGroup buttonGroup) {
		this(actionCommand, alistener, toolTipText, altText, GlobalInfo.getInternalImageIcon(imgName), buttonGroup);
	}
	
	/**
	 * Instantiates a new MainWindowToolBarButton.
	 *
	 * @param actionCommand the action command
	 * @param alistener the ActionListener to be used
	 * @param toolTipText the tool tip text
	 * @param altText the alt text
	 * @param imageIcon the image icon
	 * @param buttonGroup the button group
	 */
	public MainWindowToolBarButton(String actionCommand, ActionListener alistener, String toolTipText, String altText, ImageIcon imageIcon, ToolBarGroup buttonGroup) {
		
		this.setText(altText);
		this.setToolTipText(toolTipText);
		this.setSize(36, 36);

		if (imageIcon != null) {
			this.setPreferredSize(new Dimension(26, 26));
			this.setIcon(imageIcon);
		} else {
			this.setPreferredSize(null);
		}

		if (alistener!=null) {
			this.addActionListener(alistener);
		}
		this.setActionCommand(actionCommand);
		this.toolBarGroup = buttonGroup;
		
	}
	
	/**
	 * Returns the ButtonGroup that is the group to which this button belongs.
	 * @return the button group
	 */
	public ToolBarGroup getToolBarGroup() {
		return toolBarGroup;
	}
	
}
