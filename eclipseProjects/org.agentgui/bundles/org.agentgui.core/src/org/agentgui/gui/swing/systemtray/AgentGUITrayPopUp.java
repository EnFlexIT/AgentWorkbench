/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package org.agentgui.gui.swing.systemtray;

import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import org.agentgui.gui.swing.systemtray.AgentGUITrayIcon.TrayUsage;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.update.AWBUpdater;

/**
 * This class represents the context menu of the tray icon.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentGUITrayPopUp extends PopupMenu implements ActionListener {

	private static final long serialVersionUID = -126917985058515163L;

	private AgentGUITrayIcon agentGUItray = null;
	
	private MenuItem itemHeader = null;
	private MenuItem itemUpdate = null;
	private MenuItem itemAbout = null;
	private MenuItem itemServiceStart = null;
	private MenuItem itemServiceStop = null;
	private MenuItem itemOpenRMA = null;
	private MenuItem itemConfig = null;
	private MenuItem itemConsole = null;
	private MenuItem itemExit = null;
	
	/**
	 * Instantiates a new tray pop up menu.
	 *
	 * @param tray the tray
	 */
	public AgentGUITrayPopUp(AgentGUITrayIcon tray) {

		agentGUItray = tray;
		
		itemUpdate = new MenuItem(Language.translate("Nach Update suchen ..."));
		itemUpdate.setActionCommand("Update");
		itemUpdate.addActionListener(this);
		
		itemAbout = new MenuItem(Language.translate("Über..."));
		itemAbout.setActionCommand("About");
		itemAbout.addActionListener(this);
		
		itemServiceStart = new MenuItem(Language.translate("Starte Agent.GUI - Service"));
		itemServiceStart.setActionCommand("startAgentGUIService");
		itemServiceStart.addActionListener(this);
		
		itemServiceStop = new MenuItem(Language.translate("Stop Agent.GUI - Service"));
		itemServiceStop.setActionCommand("stoptAgentGUIService");
		itemServiceStop.addActionListener(this);
		
		itemOpenRMA = new MenuItem(Language.translate("RMA (Remote Monitoring Agent) öffnen"));
		itemOpenRMA.setActionCommand("openRMA");
		itemOpenRMA.addActionListener(this);
		
		itemConfig = new MenuItem(Language.translate("Optionen"));
		itemConfig.setActionCommand("Config");
		itemConfig.addActionListener(this);

		itemConsole = new MenuItem(Language.translate("Konsole"));
		itemConsole.setActionCommand("Konsole");
		itemConsole.addActionListener(this);
		
		itemExit = new MenuItem(Language.translate("Beenden"));
		itemExit.setActionCommand("quit");
		itemExit.addActionListener(this);
		
		// --- Build PopUp-Menu ---------------------------------
		this.add(this.getMenuItemHeader());
		this.addSeparator();
		
		this.add(itemUpdate);
		this.add(itemAbout);
		this.addSeparator();
		
		// --- Case ExecutionMode -------------------------------
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			this.add(itemConfig);
			this.addSeparator();
			this.add(itemExit);
			break;

		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			// --- In case of running as Server -----------------
			this.add(itemServiceStart);
			this.add(itemServiceStop);
			this.addSeparator();
			this.add(itemOpenRMA);
			this.addSeparator();
			this.add(itemConfig);
			this.add(itemConsole);	
			this.addSeparator();
			this.add(itemExit);
			break;
			
		case DEVICE_SYSTEM:
			// --- Case DeviceServiceExecutionMode --------------
			switch (Application.getGlobalInfo().getDeviceServiceExecutionMode()) {
			case SETUP:
				this.add(itemExit);
				break;

			case AGENT:
				this.add(itemServiceStart);
				this.add(itemServiceStop);
				this.addSeparator();
				this.add(itemOpenRMA);
				this.addSeparator();
				this.add(itemConfig);
				this.add(itemConsole);	
				this.addSeparator();
				this.add(itemExit);
				break;
			}
			break;

		}
		
	}

	/**
	 * Gets the menu item for the context menu header.
	 * @return the menu item header
	 */
	private MenuItem getMenuItemHeader() {
		if (itemHeader==null) {
			itemHeader = new MenuItem(Application.getGlobalInfo().getApplicationTitle() + " - " + Application.getGlobalInfo().getExecutionModeDescription());
			itemHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return itemHeader;
	}
	
	/**
	 * Refresh view.
	 */
	public void refreshView() {
		
		if (Application.getJadePlatform().isMainContainerRunning(false)) {
			// --- JADE is running ----------------------------------
			this.itemServiceStart.setEnabled(false);
			this.itemServiceStop.setEnabled(true);
			this.itemOpenRMA.setEnabled(true);
			// --- Change Icon color  -------------------------------
			if (agentGUItray.getTrayIconUsage()==TrayUsage.TrayIcon) {
				this.agentGUItray.getTrayIcon().setImage(this.agentGUItray.getImageGreen());	
			} else if (agentGUItray.getTrayIconUsage()==TrayUsage.TrayDialog) {
				this.agentGUItray.getAgentGUITrayDialog().getjLabel4Icon().setIcon((Icon) agentGUItray.getImageIconGreen());
			}
				
		} else {
			// --- JADE is NOT running ------------------------------
			this.itemServiceStart.setEnabled(true);
			this.itemServiceStop.setEnabled(false);
			this.itemOpenRMA.setEnabled(false);
			// --- Change Icon color  -------------------------------
			if (agentGUItray.getTrayIconUsage()==TrayUsage.TrayIcon) {
				this.agentGUItray.getTrayIcon().setImage(this.agentGUItray.getImageRed());	
			} else if (agentGUItray.getTrayIconUsage()==TrayUsage.TrayDialog) {
				this.agentGUItray.getAgentGUITrayDialog().getjLabel4Icon().setIcon((Icon) agentGUItray.getImageIconRed());	
			}
			
		}
		
		// --- Set description of the current execution mode --------
		if (agentGUItray.getTrayIconUsage()==TrayUsage.TrayIcon) {
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
			Application.getJadePlatform().start();	
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String ActCMD = ae.getActionCommand();
		if ( ActCMD.equalsIgnoreCase("Update")) {
			new AWBUpdater(true).start();
		} else if ( ActCMD.equalsIgnoreCase("About")) {
			Application.showAboutDialog();
		}else if ( ActCMD.equalsIgnoreCase("startAgentGUIService")) {
			this.startJade();
			this.refreshView();
		} else if ( ActCMD.equalsIgnoreCase("stoptAgentGUIService")) {
			Application.getJadePlatform().stop();
			this.refreshView();
		} else if ( ActCMD.equalsIgnoreCase("openRMA")) {
			Application.getJadePlatform().startSystemAgent("rma", null);
		} else if ( ActCMD.equalsIgnoreCase("Config")) {
			Application.showOptionDialog();
		} else if ( ActCMD.equalsIgnoreCase("Konsole")) {
			Application.showOptionDialog(ActCMD);
		} else if ( ActCMD.equalsIgnoreCase("quit")) {
			Application.stop();				
		} else { 
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
		};
		
	}
	
	
}
