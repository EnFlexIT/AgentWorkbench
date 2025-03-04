package de.enflexit.awb.baseUI.systemtray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import de.enflexit.awb.baseUI.BundleHelper;
import de.enflexit.awb.core.Application;

/**
 * This class will show the tray icon and will prepare its context menu, if it is supported by the OS. 
 * If not a simple dialog will be shown.
 * 
 * @see TrayIconDialog
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class AwbTrayIcon implements de.enflexit.awb.core.ui.AwbTrayIcon, ActionListener {

	// --- Some test / debugging settings -----------------
	public enum TrayUsage {
		Automatic,
		TrayIcon,
		TrayDialog
	}
	private TrayUsage trayIconUsage = TrayUsage.Automatic;
	
	private TrayIcon trayIcon;
	
	private ImageIcon imageIconRed;
	private Image     imageRed;

	private ImageIcon imageIconGreen;
	private Image     imageGreen;
	
	private TrayIconPopup trayPopUp;
	private TrayIconDialog trayDialog;
	
	
	/**
	 * Constructor of this class.
	 */
	public AwbTrayIcon() {
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
		this.getTrayPopUp().refreshView();
	}

	/**
	 * Returns the TrayIconPopup.
	 * @return the TrayIconPopup
	 */
	public TrayIconPopup getTrayPopUp() {
		if (trayPopUp==null) {
			trayPopUp = new TrayIconPopup(this);
		}
		return trayPopUp;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbTrayIcon#refreshView()
	 */
	@Override
	public void refreshView() {
		this.getTrayPopUp().refreshView();
	}
	
	/**
	 * Returns the TrayIconDialog.
	 * @return the TrayIconDialog
	 */
	public TrayIconDialog getAgentGUITrayDialog() {
		return this.getAgentGUITrayDialog(false);
	}
	/**
	 * Returns the TrayIconDialog.
	 * @param createIfNotAvailable set true, if you want that an instance should automatically be created
	 * @return the TrayIconDialog
	 */
	public TrayIconDialog getAgentGUITrayDialog(boolean createIfNotAvailable) {
		if (trayDialog==null) {
			if (createIfNotAvailable==true) {
				// --- Try to create TrayIconDialog -------------
				trayDialog = new TrayIconDialog(null, this);
			}
		}
		return trayDialog;
	}
	
	/**
	 * Returns the current TrayIcon.
	 * 
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
				trayIcon = new TrayIcon(this.getImageRed(), Application.getGlobalInfo().getApplicationTitle(), this.getTrayPopUp());
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
		if (imageIconRed==null) {
			imageIconRed = BundleHelper.getImageIcon("AWB-Red.png"); 
		}
		return imageIconRed;
	}
	/**
	 * Gets the red image of the Agent.GUI TrayIcon.
	 * @return the red image 
	 */
	public Image getImageRed() {
		if (imageRed==null && this.getImageIconRed()!=null) {
			imageRed = this.getImageIconRed().getImage();
		}
		return imageRed;
	}
	
	/**
	 * Gets the green image icon of the Agent.GUI TrayIcon.
	 * @return the image icon green
	 */
	public ImageIcon getImageIconGreen() {
		if (imageIconGreen==null ) {
			imageIconGreen = BundleHelper.getImageIcon("AWB-Green.png");
		}
		return imageIconGreen;
	}
	/**
	 * Gets the green image of the Agent.GUI TrayIcon.
	 * @return the image green
	 */
	public Image getImageGreen() {
		if (imageGreen==null && this.getImageIconGreen()!=null) {
			imageGreen = this.getImageIconGreen().getImage();
		}
		return imageGreen;
	}
	
	/**
	 * Removes the tray icon out off the system tray.
	 */
	public void dispose() {
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
			Application.showOptionsDialog();
		} else {
			Application.getMainWindow().restoreFocus();
		}
	}

}
