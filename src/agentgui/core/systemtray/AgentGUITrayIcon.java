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

	// --- Some test / debugging settings -----------------
	public enum TrayUsage {
		Automatic,
		TrayIcon,
		TrayDialog
	}
	private TrayUsage trayIconUsage = TrayUsage.Automatic;
	
	
	private final String pathImage = Application.getGlobalInfo().getPathImageIntern();
	private TrayIcon trayIcon;
	
	private ImageIcon imageIconRed = new ImageIcon( this.getClass().getResource(pathImage + "AgentGUI.png"));
	private Image     imageRed = imageIconRed.getImage();
	
	private ImageIcon imageIconGreen = new ImageIcon( this.getClass().getResource(pathImage + "AgentGUIGreen.png"));
	private Image     imageGreen = imageIconGreen.getImage();
	
	private AgentGUITrayPopUp trayPopUp;
	private AgentGUITrayDialog trayDialog;
	
	
	/**
	 * Constructor of this class.
	 */
	public AgentGUITrayIcon() {
		this.initialize();
	}
	
	/**
	 * Gets the tray icon usage.
	 * @return the tray icon usage
	 */
	public TrayUsage getTrayIconUsage() {
		
		if (this.trayIconUsage==null) {
			this.trayIconUsage=TrayUsage.Automatic;
		}
		if (this.trayIconUsage==TrayUsage.Automatic) {
			if (SystemTray.isSupported()==true) {
				this.trayIconUsage=TrayUsage.TrayIcon;
			} else {
				this.trayIconUsage=TrayUsage.TrayDialog;
			}
		}
		return trayIconUsage;
	}
	
	/**
	 * Returns the local {@link SystemTray}.
	 * @return the system tray
	 */
	private SystemTray getSystemTray() {
		return SystemTray.getSystemTray();
	}
	/**
	 * Starts the TrayIcon, if this is supported. If not, it should start a
	 * simple JDialog, doing the same as independent Window.
	 */
	private void initialize() {

		switch (this.getTrayIconUsage()) {
		case TrayIcon:
			try {
				// --- System-Tray is supported ---------------------
				this.getSystemTray().add(this.getTrayIcon(true));
				
			} catch (AWTException e) {
				System.err.println("TrayIcon supported, but could not be added. => Use TrayDialog instead !");
				this.getAgentGUITrayDialog(true).setVisible(true);			
			}
			break;
			
		case TrayDialog:
			this.getAgentGUITrayDialog(true).setVisible(true);
			break;

		default:
			break;
		}
		
		// --- Refresh tray icon ------------------------------------
		this.getAgentGUITrayPopUp().refreshView();
	}

	/**
	 * Returns the AgentGUITrayPopUp.
	 * @return the AgentGUITrayPopUp
	 */
	public AgentGUITrayPopUp getAgentGUITrayPopUp() {
		if (trayPopUp==null) {
			trayPopUp = new AgentGUITrayPopUp(this);
		}
		return trayPopUp;
	}

	/**
	 * Returns the AgentGUITrayDialog.
	 * @return the AgentGUITrayDialog
	 */
	public AgentGUITrayDialog getAgentGUITrayDialog() {
		return this.getAgentGUITrayDialog(false);
	}
	/**
	 * Returns the AgentGUITrayDialog.
	 * @param createIfNotAvailable set true, if you want that an instance should automatically be created
	 * @return the AgentGUITrayDialog
	 */
	public AgentGUITrayDialog getAgentGUITrayDialog(boolean createIfNotAvailable) {
		if (trayDialog==null) {
			if (createIfNotAvailable==true) {
				// --- Try to create TrayIconDialog -------------
				trayDialog = new AgentGUITrayDialog(null, this);
			}
		}
		return trayDialog;
	}
	
	/**
	 * Returns the current TrayIcon.
	 *
	 * @param createIfNotAvailable the create if not available
	 * @return the TrayIcon
	 */
	public TrayIcon getTrayIcon() {
		return this.getTrayIcon(false);
	}
	/**
	 * Returns or creates the TrayIcon.
	 *
	 * @param createIfNotAvailable the create if not available
	 * @return the TrayIcon
	 */
	public TrayIcon getTrayIcon(boolean createIfNotAvailable) {
		if (trayIcon==null) {
			if (createIfNotAvailable==true) {
				trayIcon = new TrayIcon(this.getImageRed(), Application.getGlobalInfo().getApplicationTitle(), this.getAgentGUITrayPopUp());
				trayIcon.setImageAutoSize(true);
				trayIcon.addActionListener(this);	
			}
		}
		return trayIcon;
	}
	
	/**
	 * Gets the red image icon of the Agent.GUI TrayIcon
	 * @return the red image icon
	 */
	public ImageIcon getImageIconRed() {
		return this.imageIconRed;
	}
	/**
	 * Gets the red image of the Agent.GUI TrayIcon.
	 * @return the red image 
	 */
	public Image getImageRed() {
		return this.imageRed;
	}
	
	/**
	 * Gets the green image icon of the Agent.GUI TrayIcon.
	 * @return the image icon green
	 */
	public ImageIcon getImageIconGreen() {
		return this.imageIconGreen;
	}
	/**
	 * Gets the green image of the Agent.GUI TrayIcon.
	 * @return the image green
	 */
	public Image getImageGreen() {
		return this.imageGreen;
	}
	
	/**
	 * Removes the tray icon out off the system tray.
	 */
	public void remove() {
		if (this.getTrayIcon()!=null) {
			this.getSystemTray().remove(this.getTrayIcon());
		}
	}
		
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (Application.isRunningAsServer()==true || Application.getMainWindow()==null) {
			Application.showOptionDialog();	
		} else {
			Application.getMainWindow().restoreFocus();
		}
	}

}
