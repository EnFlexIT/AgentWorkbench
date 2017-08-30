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
package agentgui.core.gui.projectwindow.simsetup;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import agentgui.core.project.Project;

/**
 * Represents the JPanel/Tab 'EnvironmentModel' in the setup path of the project.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentModelSetup extends JPanel implements Observer {

	private static final long serialVersionUID = 3230313372954316520L;
	
	private Project currProject;
	private JPanel environmentControllerGUI = null;
	
	/**
	 * Constructor.
	 * @param project the current project
	 */
	public EnvironmentModelSetup(Project project){
		this.currProject = project;
		this.currProject.addObserver(this);
		this.initialize();
	}
	
	/** Initialize. */
	private void initialize(){
		this.setLayout(new BorderLayout());
		this.add(this.getEnvironmentControllerGUI(), BorderLayout.CENTER);
	}
	
	/**
	 * Depending on the currently configured (predefined) environment/visualisation model, this
	 * method returns the EnvironmentPanel, which has to be displayed here on this JPanel.
	 *
	 * @return the currently configured Environment-Display
	 */
	private JPanel getEnvironmentControllerGUI(){
		if (environmentControllerGUI==null) {
			// --- Create an empty panel as default -----------------
			environmentControllerGUI = new JPanel();
			// --- Try to get the current environment controller ----
			if (this.currProject.getEnvironmentController()!=null) {
				environmentControllerGUI = this.currProject.getEnvironmentController().getOrCreateEnvironmentPanel();	
			}
		}
		return environmentControllerGUI;
	}
	
	/**
	 * Switch environment mode.
	 */
	private void reloadEnvironmentControllerGUI(){
		this.remove(this.getEnvironmentControllerGUI());
		this.environmentControllerGUI = null;
		this.add(this.getEnvironmentControllerGUI(), BorderLayout.CENTER);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		if(observable.equals(currProject) && updateObject.equals(Project.CHANGED_EnvironmentModelType)){
			this.reloadEnvironmentControllerGUI();
		}
	}

}
