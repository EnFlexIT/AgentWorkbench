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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.project.Project;

/**
 * Represents the JPanel/Tab 'Simulation-Setup' - 'Simulation Environment'.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SimulationEnvironment extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 3230313372954316520L;
	
	private Project project;
	
	private SetupSelector jPanelTopNew = null;
	private EnvironmentPanel environmentControllerGUI = null;
	
	/**
	 * Constructor.
	 *
	 * @param project the project
	 */
	public SimulationEnvironment(Project project){
		this.project = project;
		this.project.addObserver(this);
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize(){
		
		this.setLayout(new BorderLayout());
		this.add(getJPanelTopNew(), BorderLayout.NORTH);
		this.add(getEnvironmentControllerGUI(), BorderLayout.CENTER);
		
	}
	
	/**
	 * Depending on the currently configured (predefined) environment/visualisation model, this
	 * method returns the EnvironmentPanel, which has to be displayed here on this JPanel.
	 *
	 * @return the currently configured Environment-Display
	 */
	private EnvironmentPanel getEnvironmentControllerGUI(){
		
		// --- Create an empty panel --------------------------------
		environmentControllerGUI = new EnvironmentPanel(this.project){

			private static final long serialVersionUID = 4276637599115228527L;

			@Override
			public void update(Observable o, Object arg) {
				// TODO Auto-generated method stub
				
			}
		};
		
		EnvironmentType envType = project.getEnvironmentModelType();
		Class<? extends EnvironmentPanel> envPanelClass = envType.getEnvironmentPanelClass();
	
		if (envPanelClass==null) {
			// ------------------------------------------------------
			// --- If NO environment is specified -------------------
			// ------------------------------------------------------
			return environmentControllerGUI;
		} 

		// ----------------------------------------------------------
		// --- If an environment IS specified -----------------------
		// ----------------------------------------------------------
		try {
			
			// --- look for the right constructor parameter ---------
			Class<?>[] conParameter = new Class[1];
			conParameter[0] = Project.class;
		
			// --- Get the constructor ------------------------------	
			Constructor<?> envPanelClassConstructor = envPanelClass.getConstructor(conParameter);

			// --- Define the argument for the newInstance call ----- 
			Object[] args = new Object[1];
			args[0] = this.project;
			
			environmentControllerGUI = (EnvironmentPanel) envPanelClassConstructor.newInstance(args);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return environmentControllerGUI;
	}
	
	/**
	 * Switch environment mode.
	 *
	 * @param mode the mode
	 */
	private void switchEnvironmentMode(String mode){
		this.remove(environmentControllerGUI);
		this.getEnvironmentControllerGUI();
		this.add(environmentControllerGUI, BorderLayout.CENTER);
	}
	
	/**
	 * This method initializes jPanelTopNew.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelTopNew() {
		if (jPanelTopNew == null) {
			jPanelTopNew = new SetupSelector(this.project);
		}
		return jPanelTopNew;
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(project) && arg.equals(Project.CHANGED_EnvironmentModel)){
			this.switchEnvironmentMode(project.getEnvironmentModelName());
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
