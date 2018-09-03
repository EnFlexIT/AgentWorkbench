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
package agentgui.simulationService.load.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.simulationService.agents.LoadMeasureAgent;

/**
 * This is the dialog window for displaying the current system load on
 * the platform and is used by the {@link LoadMeasureAgent}.
 * 
 * @see LoadMeasureAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SystemLoadDialog extends JFrame {

	private static final long serialVersionUID = 3170514914879967107L;

	private LoadMeasureAgent loadMeasureAgent;
	private SystemLoadPanel systemLoadPanel;

	
	/**
	 * Instantiates a new system load dialog.
	 * @param loadMeasureAgent the load measure agent
	 */
	public SystemLoadDialog(LoadMeasureAgent loadMeasureAgent) {
		super();
		this.loadMeasureAgent = loadMeasureAgent;
		this.initialize();
	}

	/**
	 * This method initialises this.
	 */
	private void initialize() {
		
		this.setSize(620, 120);
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
	    this.setTitle(Application.getGlobalInfo().getApplicationTitle() + ": " + Language.translate("Load Monitor"));
		this.setLookAndFeel();
		this.setContentPane(this.getSystemLoadPanel());		
		
		// --- Add a WindowsListener --------------------------------
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
	}

	/**
	 * Returns the system load panel.
	 * @return the system load panel
	 */
	public SystemLoadPanel getSystemLoadPanel() {
		if (systemLoadPanel==null) {
			systemLoadPanel = new SystemLoadPanel(this.loadMeasureAgent);
		}
		return systemLoadPanel;
	}
	
	/**
	 * This method set the Look and Feel of this Dialog.
	 */
	private void setLookAndFeel() {
 
		String lnfClassname = Application.getGlobalInfo().getAppLookAndFeelClassName();
		try {
			if (lnfClassname == null) {
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(lnfClassname);
			SwingUtilities.updateComponentTreeUI(this);
			
		}  catch (Exception ex) {
			System.err.println("Cannot install Look and Feel '" + lnfClassname + "' on this platform.");
			ex.printStackTrace();
		}
	}	

	
	
}  //  @jve:decl-index=0:visual-constraint="11,3"
