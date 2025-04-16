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
package de.enflexit.awb.desktop.project;

import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;

import de.enflexit.awb.core.project.Project;

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
	 * This is the default constructor.
	 * @param project the current project
	 */
	public ProjectDesktop(Project project) {
		super();
		this.currProject = project;
		this.currProject.setProjectDesktop(this);
		this.initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setAutoscrolls(true);		
		this.setSize(new Dimension(300, 200));
		this.addContainerListener(new ContainerListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ContainerListener#componentAdded(java.awt.event.ContainerEvent)
			 */
			@Override
			public void componentAdded(ContainerEvent ce) {
				currProject.setNotChangedButNotify(Project.PROJECT_DESKTOP_COMPONENT_ADDED);
				ProjectDesktop.this.setFocusToProjectDesktop();
			}
			/* (non-Javadoc)
			 * @see java.awt.event.ContainerListener#componentRemoved(java.awt.event.ContainerEvent)
			 */
			@Override
			public void componentRemoved(ContainerEvent ce) {
				currProject.setNotChangedButNotify(Project.PROJECT_DESKTOP_COMPONENT_REMOVED);
			}
		});
	}

	/**
	 * Set's the focus to the project-desktop
	 */
	public void setFocusToProjectDesktop () {
		((JTabbedPane) this.getParent()).setSelectedComponent(this);
	}

} 
