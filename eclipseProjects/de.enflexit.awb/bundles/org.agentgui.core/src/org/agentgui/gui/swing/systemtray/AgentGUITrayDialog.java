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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

/**
 * This JDialog is a reserve dialog in case that tray icons are not supported
 * by the OS. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class AgentGUITrayDialog extends JDialog implements MouseListener {

	private static final long serialVersionUID = -663073173834257611L;
	
	private PopupMenu trayIconPopUp = null;  //  @jve:decl-index=0:
	private AgentGUITrayIcon appTrayIconInstance = null;
	
	private JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private JLabel jLabelIcon = null;
	private JLabel jLabelInfo = null;
	
	/**
	 * Instantiates a new Agent.GUI tray dialog.
	 *
	 * @param owner the owner
	 * @param trayIconInstance the tray icon instance
	 */
	public AgentGUITrayDialog(Frame owner, AgentGUITrayIcon trayIconInstance) {
		super(owner);
		appTrayIconInstance = trayIconInstance;
		trayIconPopUp = trayIconInstance.getAgentGUITrayPopUp();
		initialize();
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		
		this.setSize(279, 66);
		this.add(getJContentPane());
		this.setUndecorated(true);
		this.addMouseListener(this);
		this.add(trayIconPopUp);
		this.setAlwaysOnTop(true);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int newX = (int) (screen.getWidth() - this.getWidth());
		int newY = (int) (screen.getHeight()- 1.5*this.getHeight());
		this.setLocation( newX, newY );
		
		String viewTitle = Application.getGlobalInfo().getApplicationTitle();
		jContentPane.setToolTipText(viewTitle);
		jLabelInfo.setToolTipText(viewTitle);
		
		String viewText = Language.translate("Rechts-Klick für weitere Optionen");
		jLabelInfo.setText(viewText);
		this.getjLabel4Icon().setToolTipText(viewText);
	}

	/**
	 * This method initializes jContentPane.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			jLabelInfo = new JLabel();
			jLabelInfo.setBounds(new Rectangle(60, 25, 210, 16));
			jLabelInfo.setText("Rechts-Klick für weitere Optionen");
			jLabelInfo.addMouseListener(this);
			
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setSize(new Dimension(329, 68));
			jContentPane.addMouseListener(this);
			jContentPane.add(this.getjLabel4Icon(), null);
			jContentPane.add(jLabelInfo, null);
		}
		return jContentPane;
	}

	/**
	 * Returns the JLable for the Icon.
	 * @return the JLable for the Icon
	 */
	public JLabel getjLabel4Icon() {
		if (jLabelIcon==null) {
			jLabelIcon = new JLabel();
			jLabelIcon.setBounds(new Rectangle(5, 5, 51, 57));
			jLabelIcon.setIcon(appTrayIconInstance.getImageIconRed());
			jLabelIcon.setText("");
			jLabelIcon.addMouseListener(this);
		}
		return jLabelIcon;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println( "MouseReleased" );
		if (e.isPopupTrigger()) {
			if (trayIconPopUp == null) {
				System.out.println("Could not find context menu.");
			} else {
				trayIconPopUp.show( e.getComponent(), e.getX(), e.getY());	
			}			
		}		
	}

}  //  @jve:decl-index=0:visual-constraint="24,14"
