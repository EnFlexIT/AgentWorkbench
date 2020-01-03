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

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import agentgui.core.project.Project;
import de.enflexit.common.swing.AwbBasicTabbedPaneUI;

/**
 * This class can be used in order to create a tab that can hold further sub tabs 
 * (like the project tabs 'configuration' or 'Setup').
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TabForSubPanels extends JPanel  {

	private static final long serialVersionUID = 1L;

	private Project currProject = null;
	private JTabbedPane jTabbedPaneIntern = null;

	/**
	 * This is the default constructor.
	 * @param project the project
	 */
	public TabForSubPanels(Project project) {
		super();
		this.currProject = project;
		this.initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 250);
		this.setLayout(new BorderLayout());
		this.add(getJTabbedPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jTabbedPaneSimSetup	
	 * @return javax.swing.JTabbedPane	
	 */
	public JTabbedPane getJTabbedPane() {
		if (jTabbedPaneIntern == null) {
			jTabbedPaneIntern = new JTabbedPane();
			jTabbedPaneIntern.setTabPlacement(JTabbedPane.TOP);
			jTabbedPaneIntern.setUI(new AwbBasicTabbedPaneUI());
			jTabbedPaneIntern.setFont(new Font("Dialog", Font.BOLD, 13));
			jTabbedPaneIntern.setBorder(BorderFactory.createEmptyBorder());
			
			ProjectWindow projectWindow = (ProjectWindow) currProject.getProjectEditorWindow();
			jTabbedPaneIntern.addMouseListener(projectWindow.getTabMouseListener());
			jTabbedPaneIntern.addChangeListener(projectWindow.getTabSelectionListener());
		}
		return jTabbedPaneIntern;
	}
	
} 
