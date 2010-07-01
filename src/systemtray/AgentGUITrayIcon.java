package systemtray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import application.Application;

public class AgentGUITrayIcon implements ActionListener {

	private SystemTray tray = SystemTray.getSystemTray();
	public TrayIcon trayIcon;
	
	private final String PathImage = Application.RunInfo.PathImageIntern();
	
	public ImageIcon imageIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );;
	public Image     image = imageIcon.getImage();
	
	public ImageIcon imageGreenIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUIGreen.png") );;
	public Image     imageGreen = imageGreenIcon.getImage();
	
	public AgentGUITrayPopUp popUp = null;
	public AgentGUITrayDialogue trayDialog = null;
	
	/**
	 * Constructor of this class
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
			trayIcon = new TrayIcon(image, Application.RunInfo.AppTitel(), popUp);
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(this);
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("TrayIcon supported, but could not be added.");
				// --- System Tray is NOT supported -----------------
				trayDialog = new AgentGUITrayDialogue(null,this);
				trayDialog.setVisible(true);			
				// --------------------------------------------------
			}
		} else {
			// --- System Tray is NOT supported --------------------- 
			trayDialog = new AgentGUITrayDialogue(null,this);
			trayDialog.setVisible(true);
			// ------------------------------------------------------
		}
		popUp.refreshView();
	}

	public void remove() {
		tray.remove(trayIcon);
	}
		
	// --------------------------------------------------------------
	// --- ActionListener -------------------- S T A R T ------------
	// --------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
		//trayIcon.displayMessage(Application.RunInfo.AppTitel(), "An Action Event Has Been Performed!", TrayIcon.MessageType.INFO);
		if ( Application.isServer == true ) {
			Application.showOptionDialog();	
		} else {
			Application.MainWindow.restoreFocus();
		}
	}
	// --------------------------------------------------------------
	// --- ActionListener -------------------- E N D ----------------
	// --------------------------------------------------------------

}
