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
package agentgui.core.environment;

import java.util.Observer;

import javax.swing.JPanel;

import agentgui.envModel.graph.controller.GraphEnvironmentControllerGUI;

/**
 * In order to build an user interface, where environments can be defined by
 * end users in a visual way, this class has to be extended.  
 * 
 * @see GraphEnvironmentControllerGUI
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class EnvironmentPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -5522022346976174783L;

	/** The environment controller that is used for the management of the environment model. */
	protected EnvironmentController environmentController = null;

	
	/**
	 * Constructor for displaying the current environment model during a running simulation.
	 */
	public EnvironmentPanel(EnvironmentController controller){
		super();
		this.setEnvironmentController(controller);
		this.getEnvironmentController().addObserver(this);
	}
	/**
	 * Returns the {@link EnvironmentController}
	 * @return the environmentController
	 */
	public EnvironmentController getEnvironmentController() {
		return environmentController;
	}
	/**
	 * Sets the new {@link EnvironmentController}.
	 * @param environmentController the environmentController to set
	 */
	public void setEnvironmentController(EnvironmentController environmentController) {
		this.environmentController = environmentController;
	}
	
	/**
	 * Should be invoked before an EnvironmentPanel has to be destroyed.
	 */
	public abstract void dispose();
	
	
}
