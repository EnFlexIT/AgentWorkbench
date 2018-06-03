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
package agentgui.core.gui.components;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import agentgui.core.project.Project;

/**
 * The Class JPanel4Visualization.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanel4Visualization extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2070782580889468458L;

	private String tabName = null;
	private Project currProject = null;
	
	/**
	 * Instantiates a new j panel4 visualization.
	 *
	 * @param tabName the tab name
	 */
	public JPanel4Visualization(Project project, String tabName) {
		super();
		this.currProject = project;
		this.tabName = tabName;
		
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(10);
		borderLayout.setVgap(10);
		this.setLayout(borderLayout);
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	@Override
	public Component add(Component comp) {
		currProject.getProjectEditorWindow().setFocus2Tab(this.tabName);
		return super.add(comp);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component, int)
	 */
	@Override
	public Component add(Component comp, int index) {
		currProject.getProjectEditorWindow().setFocus2Tab(this.tabName);
		return super.add(comp, index);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component, java.lang.Object)
	 */
	@Override
	public void add(Component comp, Object constraints) {
		currProject.getProjectEditorWindow().setFocus2Tab(this.tabName);
		super.add(comp, constraints);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component, java.lang.Object, int)
	 */
	@Override
	public void add(Component comp, Object constraints, int index) {
		currProject.getProjectEditorWindow().setFocus2Tab(this.tabName);
		super.add(comp, constraints, index);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.lang.String, java.awt.Component)
	 */
	@Override
	public Component add(String name, Component comp) {
		currProject.getProjectEditorWindow().setFocus2Tab(this.tabName);
		return super.add(name, comp);
	}
	
}
