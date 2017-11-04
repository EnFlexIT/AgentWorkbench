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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import agentgui.core.project.Project;

/**
 * This class can be used in order to create a tab which can hold further sub tabs (like the tabs
 * 'configuration' or 'Simulation-Setup').
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TabForSubPanels extends JPanel  {

	private static final long serialVersionUID = 1L;

	private Project currProject = null;
	private JTabbedPane jTabbedPaneIntern = null;

	/**
	 * This is the default constructor
	 */
	public TabForSubPanels(Project project) {
		super();
		this.currProject = project;
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(421, 239);
		this.add(getJTabbedPane(), gridBagConstraints);
	}

	/**
	 * This method initializes jTabbedPaneSimSetup	
	 * @return javax.swing.JTabbedPane	
	 */
	public JTabbedPane getJTabbedPane() {
		if (jTabbedPaneIntern == null) {
			jTabbedPaneIntern = new JTabbedPane();
			jTabbedPaneIntern.setFont(new Font("Dialog", Font.BOLD, 12));
			ProjectWindow projectWindow = (ProjectWindow) currProject.getProjectEditorWindow();
			jTabbedPaneIntern.addMouseListener(projectWindow.getTabMouseListener());
			jTabbedPaneIntern.addChangeListener(projectWindow.getTabSelectionListener());
		}
		return jTabbedPaneIntern;
	}
	
	
	
}  //  @jve:decl-index=0:visual-constraint="10,21"
