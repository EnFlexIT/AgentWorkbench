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
package org.agentgui.gui.swing.logging;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTabbedPane;

import org.agentgui.gui.AwbConsole;
import org.agentgui.gui.AwbConsoleFolder;

public class JTabbedPane4Consoles extends JTabbedPane implements AwbConsoleFolder {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public JTabbedPane4Consoles() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setFont(new Font("Dialog", Font.BOLD, 12));
	}

	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsoleFolder#addTab(java.lang.String, org.agentgui.gui.AwbConsole)
	 */
	@Override
	public void addTab(String title, AwbConsole console) {
		this.addTab(title, (Component)console);
		
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsoleFolder#remove(org.agentgui.gui.AwbConsole)
	 */
	@Override
	public void remove(AwbConsole console) {
		this.remove((Component)console);
	}

	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsoleFolder#setSelectedComponent(org.agentgui.gui.AwbConsole)
	 */
	@Override
	public void setSelectedComponent(AwbConsole console) {
		this.setSelectedComponent((Component) console);
	}
	
}
