package de.enflexit.awb.baseUI.systemtray;

import java.awt.MenuItem;
import java.util.Vector;

import de.enflexit.awb.baseUI.SeparatorPosition;

/**
 * The Class TrayIconMenuExtension can be extended to define individual menus, menu items
 * and toolbar components to the Swing main window of Agent.Workbench.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class TrayIconMenuExtension implements AwbTrayIconMenuExtension {

	private Vector<TrayIconMenuItem> trayIconMenuItemVector;
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.systemtray.AwbTrayIconMenuExtension#getTrayIconMenuItemVector()
	 */
	@Override
	public Vector<TrayIconMenuItem> getTrayIconMenuItemVector() {
		if (trayIconMenuItemVector==null) {
			trayIconMenuItemVector = new Vector<>();
		}
		return trayIconMenuItemVector;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.systemtray.AwbTrayIconMenuExtension#addTrayIconMenuItem(java.awt.MenuItem, java.lang.Integer, de.enflexit.awb.baseUI.SeparatorPosition)
	 */
	@Override
	public void addTrayIconMenuItem(MenuItem tryIconMenuItemToAdd, Integer indexPosition, SeparatorPosition separatorPosition) {
		if (tryIconMenuItemToAdd==null) {
			throw new NullPointerException("The menu item to be added is null.");
		}
		this.getTrayIconMenuItemVector().add(new TrayIconMenuItem(tryIconMenuItemToAdd, indexPosition, separatorPosition));
	}
	
}
