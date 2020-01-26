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
package agentgui.simulationService.time;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.core.project.setup.SimulationSetup.SetupChangeEvent;

/**
 * The Class JPanel4TimeModelConfiguration has to be extended in order to
 * provide a specific JPanle for the configuration of a TimeModel.
 * 
 * @see TimeModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public abstract class JPanel4TimeModelConfiguration extends JPanel implements Observer {

	private static final long serialVersionUID = 4966720402773236025L;

	protected Project currProject;
	private TimeModelController timeModelController;
	
	private boolean isDisabledObserver;
	
	/**
	 * Instantiates a new display panel for the configuration of the current time model.
	 *
	 * @param project the current project
	 * @param timeModelController the TimeModelController that is part of the specified project
	 */
	public JPanel4TimeModelConfiguration(Project project, TimeModelController timeModelController) {
		this.currProject = project;
		this.timeModelController = timeModelController;
		this.addObserver();
		// --- Set the time model to display --------------
		TimeModelController tmController = this.getTimeModelController();
		if (tmController!=null) {
			this.setTimeModel(tmController.getTimeModel());
		}
	}
	

	/**
	 * Adds the observer to the current project.
	 */
	private void addObserver() {
		if (this.currProject!=null) {
			this.currProject.addObserver(this);
		}
	}
	/**
	 * Deletes the observer from the current project.
	 */
	public void deleteObserver() {
		if (this.currProject!=null) {
			this.currProject.deleteObserver(this);
		}
	}
	
	/**
	 * Sets the TimeModel.
	 * @param timeModel the new TimeModel
	 */
	public abstract void setTimeModel(TimeModel timeModel);
	
	/**
	 * Returns the TimeModel.
	 * @return the TimeModell
	 */
	public abstract TimeModel getTimeModel();
	
	
	
	/**
	 * Returns the currents project TimeModelController that holds the current {@link TimeModel}.
	 * @return the time model controller
	 */
	protected TimeModelController getTimeModelController() {
		if (timeModelController==null && this.currProject!=null) {
			timeModelController = this.currProject.getTimeModelController();
		}
		return timeModelController;
	}
	/**
	 * Save the current TimeModel to the simulation setup.
	 */
	protected void saveTimeModelToSimulationSetup() {
		this.isDisabledObserver = true;
		this.getTimeModelController().setTimeModel(this.getTimeModel());
		this.getTimeModelController().saveTimeModelToSimulationSetup();
		this.isDisabledObserver = false;
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		if (this.isDisabledObserver==true) return;
		
		if (updateObject instanceof SimulationSetupNotification) {
			// --- Change inside the simulation setup ---------------
			SimulationSetupNotification scn = (SimulationSetupNotification) updateObject;
			switch (scn.getUpdateReason()) {
			case SIMULATION_SETUP_SAVED:
				break;
			case SIMULATION_SETUP_PREPARE_SAVING:
				this.saveTimeModelToSimulationSetup();
				break;
				
			default:
				this.setTimeModel(this.getTimeModelController().getTimeModel());
				break;
			}
			
		} else if (updateObject instanceof SetupChangeEvent) {
			// --- Change inside the simulation setup ---------------
			SetupChangeEvent setupChangeEvent = (SetupChangeEvent) updateObject;
			switch (setupChangeEvent) {
			case TimeModelSettings:
				this.setTimeModel(this.getTimeModelController().getTimeModel()); 
				break;

			default:
				break;
			} 
		}
		
		
	}
	
}
