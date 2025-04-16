package org.agentgui.gui.swing.systemtray;

import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Icon;

import org.agentgui.gui.swing.MainWindowExtension;
import org.agentgui.gui.swing.MainWindowExtension.SeparatorPosition;
import org.agentgui.gui.swing.MainWindowExtension.TrayIconMenuItem;
import org.agentgui.gui.swing.systemtray.AWBTrayIcon.TrayUsage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import agentgui.core.application.Application;
import de.enflexit.language.Language;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.gui.MainWindow;
import agentgui.core.jade.Platform.SystemAgent;
import agentgui.core.update.AWBUpdater;

/**
 * This class represents the context menu of the tray icon.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AWBTrayPopUp extends PopupMenu implements ActionListener {

	private static final long serialVersionUID = -126917985058515163L;

	private AWBTrayIcon agentGUItray = null;
	
	private Vector<MenuItem> menuItemList;
	
	private MenuItem itemHeader;
	private MenuItem itemUpdate;
	private MenuItem itemAbout;
	private MenuItem itemServiceStart;
	private MenuItem itemServiceStop;
	private MenuItem itemOpenRMA;
	private MenuItem itemConfig;
	private MenuItem itemConsole;
	private MenuItem itemExit;
	
	/**
	 * Instantiates a new tray pop up menu.
	 *
	 * @param tray the tray
	 */
	public AWBTrayPopUp(AWBTrayIcon tray) {
		this.agentGUItray = tray;
		this.initialize();
	}

	private void initialize() {
		// --- Add all menu items -------------------------
		for (int i = 0; i < this.getMenuItemVector().size(); i++) {
			this.add(this.getMenuItemVector().get(i));
		}
	}
	
	public Vector<MenuItem> getMenuItemVector() {
		if (menuItemList==null) {
			menuItemList = new Vector<>();
			// --- Add standard menu items ----------------
			this.addStandardMenuItems();
			// --- Add extension menu items ---------------
			this.addExtensionMenuItems();
		}
		return menuItemList;
	}
	
	/**
	 * Adds the extension menu items.
	 */
	private void addExtensionMenuItems() {
		
		IConfigurationElement[] configElements = Platform.getExtensionRegistry().getConfigurationElementsFor(MainWindow.MAIN_WINDOW_EXTENSION_ID);
		try {
			for (int i = 0; i < configElements.length; i++) {
				IConfigurationElement configElement = configElements[i]; 
				final Object execExt = configElement.createExecutableExtension("class");
				if (execExt instanceof MainWindowExtension) {
					MainWindowExtension mwExtension = (MainWindowExtension) execExt;
					this.proceedMainWindowExtension(mwExtension);
				}
			} 

		} catch (CoreException ex) {
            System.err.println(ex.getMessage());
        }
	}
	/**
	 * Proceeds the specified {@link MainWindowExtension} and adds it specified tray icon menu itmes.
	 * @param mwExtension the MainWindowExtension to proceed
	 */
	private void proceedMainWindowExtension(MainWindowExtension mwExtension) {
		
		if (mwExtension==null) return;

		try {
			// --- Call the initialize method first -----------------
			mwExtension.initialize();
			
		} catch (Exception ex) {
			System.err.println(mwExtension.getClass().getName() + ": Error while initializing the MainWindowExtension.");
			ex.printStackTrace();
		}

		try {
			// --- Check for single MenuItems -----------------------
			if (mwExtension.getTrayIconMenuItemVector().size()>0) {
				// --- Add the specified menu items ----------------- 
				for (int i = 0; i < mwExtension.getTrayIconMenuItemVector().size(); i++) {
					// --- Get single element ----------------------- 
					TrayIconMenuItem trayIconMenuItem = mwExtension.getTrayIconMenuItemVector().get(i);
					if (trayIconMenuItem.getMenuItem()==null) {
						System.err.println(mwExtension.getClass().getName() + ": No menu was specified for the tray icon menu.");
						continue;
					}
					
					// --- Add the menu item to the menu --------
					Integer indexPosition = trayIconMenuItem.getIndexPosition(); 
					if (indexPosition!=null && indexPosition <= (this.getMenuItemVector().size()-1)) {
						this.getMenuItemVector().add(indexPosition, trayIconMenuItem.getMenuItem());
					} else {
						this.getMenuItemVector().add(trayIconMenuItem.getMenuItem());
						indexPosition = this.getMenuItemVector().size() - 1;
					}
					
					// --- Add a separator also ? --------------- 
					if (trayIconMenuItem.getSeparatorPosition()==SeparatorPosition.SeparatorInFrontOf) {
						this.getMenuItemVector().add(indexPosition, new MenuItem("-"));
					} else if (trayIconMenuItem.getSeparatorPosition()==SeparatorPosition.SeparatorAfter) {
						this.getMenuItemVector().add(indexPosition + 1, new MenuItem("-"));
					}
				} // end for
			}
			
		} catch (Exception ex) {
			System.err.println(mwExtension.getClass().getName() + ": Error while adding a menu item to the TryIcon menu.");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Adds the standard menu items.
	 */
	private void addStandardMenuItems() {

		// --- Build PopUp-Menu ---------------------------------
		this.getMenuItemVector().add(this.getMenuItemHeader());
		this.addSeparatorMenuItem();
		
		this.getMenuItemVector().add(this.getMenuItemUpdate());
		this.getMenuItemVector().add(this.getMenuItemAbout());
		this.addSeparatorMenuItem();
		
		// --- Case ExecutionMode -------------------------------
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			this.getMenuItemVector().add(this.getMenuItemConfig());
			this.addSeparatorMenuItem();
			this.getMenuItemVector().add(this.getMenuItemExit());
			break;

		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			// --- In case of running as Server -----------------
			this.getMenuItemVector().add(this.getMenuItemServiceStart());
			this.getMenuItemVector().add(this.getMenuItemServiceStop());
			this.addSeparatorMenuItem();
			this.getMenuItemVector().add(this.getMenuItemOpenRMA());
			this.addSeparatorMenuItem();
			this.getMenuItemVector().add(this.getMenuItemConfig());
			this.getMenuItemVector().add(this.getMenuItemConsole());	
			this.addSeparatorMenuItem();
			this.getMenuItemVector().add(this.getMenuItemExit());
			break;
			
		case DEVICE_SYSTEM:
			// --- Case DeviceServiceExecutionMode --------------
			switch (Application.getGlobalInfo().getDeviceServiceExecutionMode()) {
			case SETUP:
				this.getMenuItemVector().add(this.getMenuItemConfig());
				this.addSeparatorMenuItem();
				this.getMenuItemVector().add(this.getMenuItemExit());
				break;

			case AGENT:
				this.getMenuItemVector().add(this.getMenuItemServiceStart());
				this.getMenuItemVector().add(this.getMenuItemServiceStop());
				this.addSeparatorMenuItem();
				this.getMenuItemVector().add(this.getMenuItemOpenRMA());
				this.addSeparatorMenuItem();
				this.getMenuItemVector().add(this.getMenuItemConfig());
				this.getMenuItemVector().add(this.getMenuItemConsole());	
				this.addSeparatorMenuItem();
				this.getMenuItemVector().add(this.getMenuItemExit());
				break;
			}
			break;
		}
	}
	private void addSeparatorMenuItem() {
		this.getMenuItemVector().add(new MenuItem("-"));
	}

	
	private MenuItem getMenuItemHeader() {
		if (itemHeader==null) {
			itemHeader = new MenuItem(Application.getGlobalInfo().getApplicationTitle() + " - " + Application.getGlobalInfo().getExecutionModeDescription());
			itemHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return itemHeader;
	}
	private MenuItem getMenuItemUpdate() {
		if (itemUpdate==null) {
			itemUpdate = new MenuItem(Language.translate("Nach Update suchen ..."));
			itemUpdate.addActionListener(this);
		}
		return itemUpdate;
	}
	private MenuItem getMenuItemAbout() {
		if (itemAbout==null) {
			itemAbout = new MenuItem(Language.translate("Über..."));
			itemAbout.addActionListener(this);
		}
		return itemAbout;
	}
	private MenuItem getMenuItemServiceStart() {
		if (itemServiceStart==null) {
			itemServiceStart = new MenuItem(Language.translate("Starte AWB-Service"));
			itemServiceStart.addActionListener(this);
		}
		return itemServiceStart;
	}
	private MenuItem getMenuItemServiceStop() {
		if (itemServiceStop==null) {
			itemServiceStop = new MenuItem(Language.translate("Stop AWB-Service"));
			itemServiceStop.addActionListener(this);
		}
		return itemServiceStop;
	}
	private MenuItem getMenuItemOpenRMA() {
		if (itemOpenRMA==null) {
			itemOpenRMA = new MenuItem(Language.translate("RMA (Remote Monitoring Agent) öffnen"));
			itemOpenRMA.addActionListener(this);
		}
		return itemOpenRMA;
	}
	private MenuItem getMenuItemConsole() {
		if (itemConsole==null) {
			itemConsole = new MenuItem(Language.translate("Konsole"));
			itemConsole.addActionListener(this);
		}
		return itemConsole;
	}
	private MenuItem getMenuItemConfig() {
		if (itemConfig==null) {
			itemConfig = new MenuItem(Language.translate("Optionen"));
			itemConfig.addActionListener(this);
		}
		return itemConfig;
	}
	private MenuItem getMenuItemExit() {
		if (itemExit==null) {
			itemExit = new MenuItem(Language.translate("Beenden"));
			itemExit.addActionListener(this);
		}
		return itemExit;
	}
	
	
	/**
	 * Refresh view.
	 */
	public void refreshView() {
		
		if (Application.getJadePlatform().isMainContainerRunning(false)) {
			// --- JADE is running ----------------------------------
			this.getMenuItemServiceStart().setEnabled(false);
			this.getMenuItemServiceStop().setEnabled(true);
			this.getMenuItemOpenRMA().setEnabled(true);
			// --- Change Icon color  -------------------------------
			if (this.agentGUItray.getTrayIconUsage()==TrayUsage.TrayIcon) {
				this.agentGUItray.getTrayIcon().setImage(this.agentGUItray.getImageGreen());	
			} else if (agentGUItray.getTrayIconUsage()==TrayUsage.TrayDialog) {
				this.agentGUItray.getAgentGUITrayDialog().getjLabel4Icon().setIcon((Icon) agentGUItray.getImageIconGreen());
			}
				
		} else {
			// --- JADE is NOT running ------------------------------
			this.getMenuItemServiceStart().setEnabled(true);
			this.getMenuItemServiceStop().setEnabled(false);
			this.getMenuItemOpenRMA().setEnabled(false);
			// --- Change Icon color  -------------------------------
			if (this.agentGUItray.getTrayIconUsage()==TrayUsage.TrayIcon) {
				this.agentGUItray.getTrayIcon().setImage(this.agentGUItray.getImageRed());	
			} else if (agentGUItray.getTrayIconUsage()==TrayUsage.TrayDialog) {
				this.agentGUItray.getAgentGUITrayDialog().getjLabel4Icon().setIcon((Icon) agentGUItray.getImageIconRed());	
			}
			
		}
		
		// --- Set description of the current execution mode --------
		if (this.agentGUItray.getTrayIconUsage()==TrayUsage.TrayIcon) {
			this.agentGUItray.getTrayIcon().setToolTip(Application.getGlobalInfo().getApplicationTitle() + " - " + Application.getGlobalInfo().getExecutionModeDescription());	
		} else if (agentGUItray.getTrayIconUsage()==TrayUsage.TrayDialog) {
			this.agentGUItray.getAgentGUITrayDialog().getjLabel4Icon().setToolTipText(Application.getGlobalInfo().getApplicationTitle() + " - " + Application.getGlobalInfo().getExecutionModeDescription());
		}
		this.getMenuItemHeader().setLabel(Application.getGlobalInfo().getApplicationTitle() + " - " + Application.getGlobalInfo().getExecutionModeDescription());
		
	}
	
	/**
	 * Start jade.
	 */
	private void startJade() {
		
		ExecutionMode appExecMode = Application.getGlobalInfo().getExecutionMode();
		DeviceSystemExecutionMode deviceSysExecMode = Application.getGlobalInfo().getDeviceServiceExecutionMode(); 
		if (appExecMode==ExecutionMode.DEVICE_SYSTEM && deviceSysExecMode==DeviceSystemExecutionMode.AGENT) {
			// --- Start JADE for an embedded system agent ----------------
			Application.getJadePlatform().start4EmbeddedSystemAgent();
		} else {
			Application.getJadePlatform().doStartInDedicatedThread();	
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actionCommand = ae.getActionCommand();
		if ( ae.getSource()==this.getMenuItemUpdate()) {
			new AWBUpdater(true).start();
			
		} else if ( ae.getSource()==this.getMenuItemAbout()) {
			Application.showAboutDialog();
			
		} else if (ae.getSource()==this.getMenuItemServiceStart()) {
			this.startJade();
			this.refreshView();
			
		} else if (ae.getSource()==this.getMenuItemServiceStop()) {
			Application.getJadePlatform().stop(true);
			this.refreshView();
			
		} else if (ae.getSource()==this.getMenuItemOpenRMA()) {
			Application.getJadePlatform().startSystemAgent(SystemAgent.RMA, null);
			
		} else if (ae.getSource()==this.getMenuItemConfig() || actionCommand.equalsIgnoreCase("Config")) {
			Application.showOptionDialog();
			
		} else if (ae.getSource()==this.getMenuItemConsole()) {
			Application.showOptionDialog("Konsole");
			
		} else if (ae.getSource()==this.getMenuItemExit()) {
			Application.stop();				
			
		} else { 
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ae.getActionCommand());
		}
	}
	
}
