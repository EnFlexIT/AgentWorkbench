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
package agentgui.core.systemtray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import agentgui.core.application.Application;

/**
 * This class will show the tray icon and will prepare its context menu, if it is supported by the OS. 
 * If not a simple dialog will be shown.
 * 
 * @see AgentGUITrayDialog
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class AgentGUITrayIcon implements ActionListener {

	private SystemTray tray = SystemTray.getSystemTray();
	public TrayIcon trayIcon;
	
	private final String PathImage = Application.getGlobalInfo().PathImageIntern();
	
	public ImageIcon imageIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );;
	public Image     image = imageIcon.getImage();
	
	public ImageIcon imageGreenIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUIGreen.png") );;
	public Image     imageGreen = imageGreenIcon.getImage();
	
	public AgentGUITrayPopUp popUp = null;
	public AgentGUITrayDialog trayDialog = null;
	
	/**
	 * Constructor of this class.
	 */
	public AgentGUITrayIcon() {
		this.initialize();
	}

	/**
	 * Starts the TrayIcon, if this is supported. If not, it should start a
	 * simple JDialog, doing the same as independent Window.
	 */
	private void initialize() {

		popUp = new AgentGUITrayPopUp(this);
		
		if (SystemTray.isSupported()) {
			// --- System-Tray is supported -------------------------
			trayIcon = new TrayIcon(image, Application.getGlobalInfo().getApplicationTitle(), popUp);
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(this);
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("TrayIcon supported, but could not be added.");
				// --- System Tray is NOT supported -----------------
				trayDialog = new AgentGUITrayDialog(null,this);
				trayDialog.setVisible(true);			
				// --------------------------------------------------
			}
		} else {
			// --- System Tray is NOT supported --------------------- 
			trayDialog = new AgentGUITrayDialog(null,this);
			trayDialog.setVisible(true);
			// ------------------------------------------------------
		}
		popUp.refreshView();
	}

	/**
	 * Removes the tray icon out off the system tray.
	 */
	public void remove() {
		tray.remove(trayIcon);
	}
		
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//trayIcon.displayMessage(Application.RunInfo.AppTitel(), "An Action Event Has Been Performed!", TrayIcon.MessageType.INFO);
		if ( Application.isServer == true ) {
			Application.showOptionDialog();	
		} else {
			Application.getMainWindow().restoreFocus();
		}
	}

}
