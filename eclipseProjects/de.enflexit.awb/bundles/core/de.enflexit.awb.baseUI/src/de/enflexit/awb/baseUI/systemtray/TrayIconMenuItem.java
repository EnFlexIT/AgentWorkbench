package de.enflexit.awb.baseUI.systemtray;

import java.awt.MenuItem;
import java.awt.TrayIcon;

import de.enflexit.awb.baseUI.SeparatorPosition;

/**
 * The Class TrayIconMenuItem describes a single menu item 
 * that are to be added to the AWB {@link TrayIcon}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TrayIconMenuItem {

	private MenuItem menuItem;
	private Integer indexPosition;
	private SeparatorPosition separatorPosition;
	
	
	/**
	 * Instantiates a new tray icon menu item.
	 *
	 * @param menuItem the menu item
	 * @param indexPosition the index position
	 * @param separatorPosition the separator position
	 */
	public TrayIconMenuItem(MenuItem menuItem, Integer indexPosition, SeparatorPosition separatorPosition) {
		this.setMenuItem(menuItem);
		this.setIndexPosition(indexPosition);
		this.setSeparatorPosition(separatorPosition);
	}

	/**
	 * Returns the menu item.
	 * @return the menu item
	 */
	public MenuItem getMenuItem() {
		return menuItem;
	}
	/**
	 * Sets the menu item.
	 * @param menuItem the new menu item
	 */
	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	/**
	 * Returns the index position.
	 * @return the index position
	 */
	public Integer getIndexPosition() {
		return indexPosition;
	}
	/**
	 * Sets the index position.
	 * @param indexPosition the new index position
	 */
	public void setIndexPosition(Integer indexPosition) {
		this.indexPosition = indexPosition;
	}

	/**
	 * Returns the separator position.
	 * @return the separator position
	 */
	public SeparatorPosition getSeparatorPosition() {
		return separatorPosition;
	}
	/**
	 * Sets the separator position.
	 * @param separatorPosition the new separator position
	 */
	public void setSeparatorPosition(SeparatorPosition separatorPosition) {
		this.separatorPosition = separatorPosition;
	}
	
}
