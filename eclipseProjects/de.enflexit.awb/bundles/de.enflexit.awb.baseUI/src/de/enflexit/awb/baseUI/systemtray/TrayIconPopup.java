package de.enflexit.awb.baseUI.systemtray;

import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import de.enflexit.awb.baseUI.SeparatorPosition;
import de.enflexit.awb.baseUI.systemtray.AwbTrayIcon.TrayUsage;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo.DeviceSystemExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.core.jade.Platform.SystemAgent;
import de.enflexit.awb.core.ui.AwbOptionsDialog;
import de.enflexit.awb.core.update.AWBUpdater;
import de.enflexit.language.Language;

/**
 * This class represents the context menu of the tray icon.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TrayIconPopup extends PopupMenu implements ActionListener {

	private static final long serialVersionUID = -126917985058515163L;

	public static final String TRAY_ICON_MENU_EXTENSION_ID = "de.enflexit.awb.baseUI.systemtray.trayIconMenuExtension";
	public static final String MAIN_WINDOW_EXTENSION_ID = "de.enflexit.awb.desktop.mainWindowExtension";
	
	private AwbTrayIcon awbTrayIcon = null;
	
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
	public TrayIconPopup(AwbTrayIcon tray) {
		this.awbTrayIcon = tray;
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
			this.proceedTrayIconMenuExtensions();
		}
		return menuItemList;
	}
	
	/**
	 * Proceeds the main window extensions that are defines 
	 * by the corresponding extension point.
	 */
	private void proceedTrayIconMenuExtensions() {
		
		// --- Collect relevant extension  points -------------------
		List<IConfigurationElement> configElementList = new ArrayList<>();
		configElementList.addAll(Arrays.asList(Platform.getExtensionRegistry().getConfigurationElementsFor(TRAY_ICON_MENU_EXTENSION_ID)));
		configElementList.addAll(Arrays.asList(Platform.getExtensionRegistry().getConfigurationElementsFor(MAIN_WINDOW_EXTENSION_ID)));
		
		try {
			for (int i = 0; i < configElementList.size(); i++) {
				IConfigurationElement configElement = configElementList.get(i); 
				final Object execExt = configElement.createExecutableExtension("class");
				if (execExt instanceof TrayIconMenuExtension) {
					TrayIconMenuExtension timExtension = (TrayIconMenuExtension) execExt;
					this.proceedTrayIconMenuExtension(timExtension);
				}
			} 

		} catch (CoreException ex) {
            System.err.println(ex.getMessage());
        }
	}
	/**
	 * Proceeds the specified {@link TrayIconMenuExtension} and adds it specified tray icon menu itmes.
	 * @param tiExtension the TrayIconMenuExtension to proceed
	 */
	private void proceedTrayIconMenuExtension(AwbTrayIconMenuExtension tiExtension) {
		
		if (tiExtension==null) return;

		try {
			// --- Call the initialize method first -----------------
			tiExtension.initialize();
			
		} catch (Exception ex) {
			System.err.println(tiExtension.getClass().getName() + ": Error while initializing the TrayIconMenuExtension.");
			ex.printStackTrace();
		}

		try {
			// --- Check for single MenuItems -----------------------
			if (tiExtension.getTrayIconMenuItemVector().size()>0) {
				// --- Add the specified menu items ----------------- 
				for (int i = 0; i < tiExtension.getTrayIconMenuItemVector().size(); i++) {
					// --- Get single element ----------------------- 
					TrayIconMenuItem trayIconMenuItem = tiExtension.getTrayIconMenuItemVector().get(i);
					if (trayIconMenuItem.getMenuItem()==null) {
						System.err.println(tiExtension.getClass().getName() + ": No menu was specified for the tray icon menu.");
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
			System.err.println(tiExtension.getClass().getName() + ": Error while adding a menu item to the TryIcon menu.");
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
			if (this.awbTrayIcon.getTrayIconUsage()==TrayUsage.TrayIcon) {
				this.awbTrayIcon.getTrayIcon().setImage(this.awbTrayIcon.getImageGreen());	
			} else if (awbTrayIcon.getTrayIconUsage()==TrayUsage.TrayDialog) {
				this.awbTrayIcon.getAgentGUITrayDialog().getjLabel4Icon().setIcon((Icon) awbTrayIcon.getImageIconGreen());
			}
				
		} else {
			// --- JADE is NOT running ------------------------------
			this.getMenuItemServiceStart().setEnabled(true);
			this.getMenuItemServiceStop().setEnabled(false);
			this.getMenuItemOpenRMA().setEnabled(false);
			// --- Change Icon color  -------------------------------
			if (this.awbTrayIcon.getTrayIconUsage()==TrayUsage.TrayIcon) {
				this.awbTrayIcon.getTrayIcon().setImage(this.awbTrayIcon.getImageRed());	
			} else if (awbTrayIcon.getTrayIconUsage()==TrayUsage.TrayDialog) {
				this.awbTrayIcon.getAgentGUITrayDialog().getjLabel4Icon().setIcon((Icon) awbTrayIcon.getImageIconRed());	
			}
			
		}
		
		// --- Set description of the current execution mode --------
		if (this.awbTrayIcon.getTrayIconUsage()==TrayUsage.TrayIcon) {
			this.awbTrayIcon.getTrayIcon().setToolTip(Application.getGlobalInfo().getApplicationTitle() + " - " + Application.getGlobalInfo().getExecutionModeDescription());	
		} else if (awbTrayIcon.getTrayIconUsage()==TrayUsage.TrayDialog) {
			this.awbTrayIcon.getAgentGUITrayDialog().getjLabel4Icon().setToolTipText(Application.getGlobalInfo().getApplicationTitle() + " - " + Application.getGlobalInfo().getExecutionModeDescription());
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
			Application.showOptionsDialog();
			
		} else if (ae.getSource()==this.getMenuItemConsole()) {
			Application.showOptionsDialog(AwbOptionsDialog.TAB_CONSOLE);
			
		} else if (ae.getSource()==this.getMenuItemExit()) {
			Application.stop();				
			
		} else { 
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ae.getActionCommand());
		}
	}
	
}
