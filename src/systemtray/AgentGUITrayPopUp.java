package systemtray;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import application.Application;
import application.Language;

public class AgentGUITrayPopUp extends PopupMenu implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AgentGUITrayIcon agentGUItray = null;
	
	private MenuItem itemAbout = null;
	private MenuItem itemServiceStart = null;
	private MenuItem itemServiceStop = null;
	private MenuItem itemOpenRMA = null;
	private MenuItem itemConfig = null;
	private MenuItem itemConsole = null;
	private MenuItem itemExit = null;
	
	public AgentGUITrayPopUp(AgentGUITrayIcon tray) {

		agentGUItray = tray;
		
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
		this.add(itemAbout);
		this.addSeparator();
		
		if (Application.isServer == true) {
			// --- In case of running as Server -----------------
			this.add(itemServiceStart);
			this.add(itemServiceStop);
			this.addSeparator();
			this.add(itemOpenRMA);
			this.addSeparator();
			this.add(itemConfig);
			if (Application.RunInfo.AppUseInternalConsole()==true) {
				this.add(itemConsole);	
			}			
			this.addSeparator();
			this.add(itemExit);
		} else {
			// --- In case of running as Application ------------
			this.add(itemExit);
		}
		
	}

	public void refreshView() {
		
		if (Application.JadePlatform.jadeMainContainerIsRunning(false)) {
			// --- JADE is running ----------------------------------
			itemServiceStart.setEnabled(false);
			itemServiceStop.setEnabled(true);
			itemOpenRMA.setEnabled(true);
			// --- Icon-Farbe in der entsprechenden Anzeige ändern --
			if (agentGUItray.trayIcon!=null) {
				agentGUItray.trayIcon.setImage( agentGUItray.imageGreen );	
			}
			if (agentGUItray.trayDialog!=null ){
				agentGUItray.trayDialog.jLabelIcon.setIcon((Icon) agentGUItray.imageGreenIcon);
			}
		} else {
			// --- JADE is NOT running ------------------------------
			itemServiceStart.setEnabled(true);
			itemServiceStop.setEnabled(false);
			itemOpenRMA.setEnabled(false);
			// --- Icon-Farbe in der entsprechenden Anzeige ändern --
			if (agentGUItray.trayIcon!=null) {
				agentGUItray.trayIcon.setImage( agentGUItray.image );	
			}
			if (agentGUItray.trayDialog!=null ){
				agentGUItray.trayDialog.jLabelIcon.setIcon((Icon) agentGUItray.imageIcon);
			}
		}
		agentGUItray.trayIcon.setToolTip(Application.RunInfo.AppTitel() + " - " + Application.JadePlatform.MASrunningMode);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String ActCMD = ae.getActionCommand();
		if ( ActCMD.equalsIgnoreCase("About")) {
			Application.showAboutDialog();
		}else if ( ActCMD.equalsIgnoreCase("startAgentGUIService")) {
			Application.JadePlatform.jadeStart();
			this.refreshView();
		} else if ( ActCMD.equalsIgnoreCase("stoptAgentGUIService")) {
			Application.JadePlatform.jadeStop();
			this.refreshView();
		} else if ( ActCMD.equalsIgnoreCase("openRMA")) {
			Application.JadePlatform.jadeSystemAgentOpen("rma", null);
		} else if ( ActCMD.equalsIgnoreCase("Config")) {
			Application.showOptionDialog();
		} else if ( ActCMD.equalsIgnoreCase("Konsole")) {
			Application.showOptionDialog(ActCMD);
		} else if ( ActCMD.equalsIgnoreCase("quit")) {
			Application.quit();				
		} else { 
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
		};
		
	}
	
	
}
