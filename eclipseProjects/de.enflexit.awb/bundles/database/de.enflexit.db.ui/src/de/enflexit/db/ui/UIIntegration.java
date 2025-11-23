package de.enflexit.db.ui;

import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.ui.AwbMainWindowMenu;
import de.enflexit.awb.core.ui.AwbMainWindowToolBarGroup;
import de.enflexit.language.Language;

public class UIIntegration extends MainWindowExtension {

	private MenuItem trayIconMenuItem;
	private JMenuItem menuItem;
	private JButton jButtonDatabaseSettings;
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.mainWindow.MainWindowExtension#initialize()
	 */
	@Override
	public void initialize() {
		this.addTrayIconMenuItem(this.getTrayIconMenuItem(), 5, false);
		this.addJMenuItem(AwbMainWindowMenu.MenuExtra, this.getJMenuItem(), 6, false);
		this.addToolbarComponent(this.getJButtonDatabaseSettings(), null, false, AwbMainWindowToolBarGroup.ExtraTools);
	}

	/**
	 * Gets the tray icon menu item.
	 * @return the tray icon menu item
	 */
	private MenuItem getTrayIconMenuItem() {
		if (trayIconMenuItem==null) {
			trayIconMenuItem = new MenuItem(Language.translate("Datenbank Verbindungen"));
			trayIconMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Application.showDatabaseDialog(null);
				}
			});
		}
		return trayIconMenuItem;
	}
	
	/**
	 * Returns the JButton database settings.
	 * @return the JButton database settings
	 */
	private JButton getJButtonDatabaseSettings() {
		if (jButtonDatabaseSettings==null) {
			jButtonDatabaseSettings = new JButton();
			jButtonDatabaseSettings.setPreferredSize(new Dimension(26, 26));
			jButtonDatabaseSettings.setIcon(GlobalInfo.getInternalImageIcon("DB_State_Blue.png"));
			jButtonDatabaseSettings.setToolTipText(Language.translate("Datenbank Verbindungen"));
			jButtonDatabaseSettings.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Application.showDatabaseDialog(null);
				}
			});
		}
		return jButtonDatabaseSettings;
	}
	
	/**
	 * Gets the menu item.
	 * @return the menu item
	 */
	private JMenuItem getJMenuItem() {
		if (menuItem==null) {
			menuItem = new JMenuItem(Language.translate("Datenbank Verbindungen"), GlobalInfo.getInternalImageIcon("DB_State_Blue.png"));
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Application.showDatabaseDialog(null);
				}
			});
		}
		return menuItem;
	}
	
}
