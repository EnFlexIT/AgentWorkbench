package de.enflexit.awb.baseUI.systemtray;

import java.awt.MenuItem;
import java.util.Vector;

import de.enflexit.awb.baseUI.SeparatorPosition;

/**
 * The Class DefaultTrayIconExtension can be extended to define individual menus, menu items
 * and toolbar components to the Swing main window of Agent.Workbench.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface AwbTrayIconExtension {

	/**
	 * Initializes the extension. Use this method to add your individual 
	 * elements to the main window of the workbench.
	 */
	public void initialize();
	
	/**
	 * Returns the tray icon menu item vector.
	 * @return the tray icon menu item vector
	 */
	public Vector<TrayIconMenuItem> getTrayIconMenuItemVector();
	
	/**
	 * Adds the specified AWT tray icon {@link MenuItem}.
	 *
	 * @param tryIconMenuItemToAdd the tray icon menu item to add
	 * @param indexPosition the index position (may be <code>null</code> also)
	 * @param separatorPosition the separator position  (may be <code>null</code> also)
	 */
	public void addTrayIconMenuItem(MenuItem tryIconMenuItemToAdd, Integer indexPosition, SeparatorPosition separatorPosition);
	

}
