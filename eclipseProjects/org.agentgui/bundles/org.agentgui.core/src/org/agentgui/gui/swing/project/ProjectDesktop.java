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
package org.agentgui.gui.swing.project;

import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;

import agentgui.core.project.Project;
import java.awt.Dimension;

/**
 * Represents the tab 'Project Desktop'.<br>
 * This JDesktopPane can be used for displaying individual JInternalFrame in a project.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectDesktop extends JDesktopPane {

	private static final long serialVersionUID = -9219224867898326652L;
	private Project currProject;

	/**
	 * This is the default constructor
	 */
	public ProjectDesktop(Project project) {
		super();
		this.currProject = project;
		this.currProject.setProjectDesktop(this);
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setAutoscrolls(true);		
		this.setSize(new Dimension(300, 200));
		this.addContainerListener(new java.awt.event.ContainerListener() {
			public void componentAdded(java.awt.event.ContainerEvent e) {
				//System.out.println( e.getSource() );
				setFocus();
			}
			public void componentRemoved(java.awt.event.ContainerEvent e) {
			}
		});
	}

	/**
	 * Set's the focus to the project-desktop
	 */
	public void setFocus () {
		((JTabbedPane) this.getParent()).setSelectedComponent(this);
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
