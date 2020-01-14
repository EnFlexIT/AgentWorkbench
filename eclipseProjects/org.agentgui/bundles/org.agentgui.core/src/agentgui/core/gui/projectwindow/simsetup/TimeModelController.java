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

import java.awt.Container;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTabbedPane;

import org.agentgui.gui.swing.project.ProjectWindowTab;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.simulationService.time.JPanel4TimeModelConfiguration;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelDateBased;
import de.enflexit.common.ontology.OntologyVisualisationConfiguration;


/**
 * The Class TimeModelController is used within Project 
 * and manages the display of TimeModel's
 * 
 * @see Project
 * @see TimeModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelController implements Observer {

	private Project currProject;
	
	private TimeModel timeModel;
	
	private ProjectWindowTab pwt;
	private JPanel4TimeModelConfiguration jPanel4TimeModel;
	private int indexPositionOfTimeModelTab = 0;
	
	
	/**
	 * Instantiates a new time model controller.
	 */
	public TimeModelController(Project project) {
		this.currProject = project;
		this.currProject.addObserver(this);
		this.addTimeModelDisplayToProjectWindow();
		this.loadTimeModelFromSimulationSetup();
	}
	
	
	/**
	 * Sets the time model.
	 * @param newTimeModel the new time model
	 */
	public void setTimeModel(TimeModel newTimeModel) {
		this.timeModel = newTimeModel;
	}
	/**
	 * Returns the current time model.
	 * @return the time model
	 */
	public TimeModel getTimeModel() {
		
		String timeModelClass = this.currProject.getTimeModelClass();
		boolean isNoClass = timeModelClass==null || timeModelClass.isEmpty();
		if (isNoClass==true && this.timeModel!=null) {
			this.setTimeModel(null);
		}
		
		boolean isDifferentClass = timeModel!=null && this.currProject.getTimeModelClass()!=null && this.currProject.getTimeModelClass().equals(timeModel.getClass().getName())==false; 
		if (timeModel==null || isDifferentClass==true) {
			if (isNoClass==false) {
				try {
					timeModel = ClassLoadServiceUtility.getTimeModelInstance(timeModelClass);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException ex) {
					ex.printStackTrace();
				}
			}
		}
		return timeModel;
	}
	/**
	 * Returns, if available,  a copy of the current TimeModel.
	 * @return the TimeModel copy
	 */
	public TimeModel getTimeModelCopy() {
		if (this.getTimeModel()!=null) {
			return this.getTimeModel().getCopy();
		}
		return null;
	}
	
	/**
	 * Returns the JPanel for the time model configuration.
	 * @return the DisplayJPanel4Configuration
	 */
	private JPanel4TimeModelConfiguration getJPanel4TimeModelConfiguration() {
		if (Application.isOperatingHeadless()==false && jPanel4TimeModel==null && this.getTimeModel()!=null) {
			jPanel4TimeModel = this.getTimeModel().getJPanel4Configuration(this.currProject);
		}
		return jPanel4TimeModel;
	}
	/**
	 * Sets the time model display to project the window.
	 */
	private void addTimeModelDisplayToProjectWindow() {
		
		if (Application.isOperatingHeadless()==true) return;
		
		// --- First, remove time model visualization -----
		this.removeTimeModelDisplayFromProjectWindow();
		
		// --- Add the new time model visualization -------
		JPanel4TimeModelConfiguration configPanel = this.getJPanel4TimeModelConfiguration();
		if (configPanel!=null) {
			this.pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Zeit-Konfiguration"), null, null, configPanel, Language.translate(ProjectWindowTab.TAB_4_SUB_PANES_Setup));
			this.pwt.add(this.indexPositionOfTimeModelTab);
			configPanel.setTimeModel(this.getTimeModel());
		}
	}
	/**
	 * Removes the time model display from the project window.
	 */
	private void removeTimeModelDisplayFromProjectWindow() {
		if (this.pwt!=null) {
			this.indexPositionOfTimeModelTab = this.pwt.getIndexPosition();
			this.pwt.remove();
			this.pwt = null;
			this.jPanel4TimeModel = null;
		}
	}
	
	/**
	 * Updates the time model visualization.
	 */
	public void updateTimeModelVisualization() {
		TimeModel timeModel = this.getTimeModel();
		if (timeModel!=null) {
			JPanel4TimeModelConfiguration configPanel = this.getJPanel4TimeModelConfiguration();
			if (configPanel!=null) {
				configPanel.setTimeModel(timeModel);
			}
		}
	}
	
	
	/**
	 * Gets the index position of time model tab.
	 * @return the index position of time model tab
	 */
	public int getIndexPositionOfTimeModelTab() {
		return indexPositionOfTimeModelTab;
	}
	/**
	 * Sets the index position of time model tab.
	 * @param newIndexPosistion the new index position of time model tab
	 */
	public void setIndexPositionOfTimeModelTab(int newIndexPosistion) {
		
		if (newIndexPosistion!=this.indexPositionOfTimeModelTab && this.pwt!=null && this.getJPanel4TimeModelConfiguration()!=null) {
			// --- Set the new position of the tab --------
			JPanel4TimeModelConfiguration configPanel = this.getJPanel4TimeModelConfiguration();
			Container container = configPanel.getParent();
			if (container!=null && container instanceof JTabbedPane) {
				JTabbedPane tabbedPane = (JTabbedPane) container;
				tabbedPane.remove(configPanel);
				try {
					tabbedPane.add(configPanel, newIndexPosistion);
				} catch (Exception ex) {
					System.err.println("[" + this.getClass().getSimpleName() + "] The Tab for the TimeModel configuration could not pe placed on new index positon " + newIndexPosistion + "!");
					tabbedPane.add(configPanel, this.indexPositionOfTimeModelTab);
					return;
				}
			}
			this.pwt.setIndexPosition(newIndexPosistion);
		}
		this.indexPositionOfTimeModelTab = newIndexPosistion;
	}
	
	
	

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		if (updateObject.equals(Project.CHANGED_TimeModelClass)) {
			// --- Changes in the TimeModel class of the project ----   
			if (this.currProject.getTimeModelClass()==null) {
				// --- Remove the Displaying parts, if there any ----
				this.removeTimeModelDisplayFromProjectWindow();
			} else {
				// --- Display the new TimeModel display ------------
				this.addTimeModelDisplayToProjectWindow();
				this.loadTimeModelFromSimulationSetup();
			}
			
		} else if (updateObject instanceof SimulationSetupNotification) {
			// --- Change inside the simulation setup ---------------
			SimulationSetupNotification scn = (SimulationSetupNotification) updateObject;
			switch (scn.getUpdateReason()) {
			case SIMULATION_SETUP_SAVED:
				break;
			case SIMULATION_SETUP_PREPARE_SAVING:
				this.saveTimeModelToSimulationSetup();
				break;
				
			default:
				this.loadTimeModelFromSimulationSetup();	
				break;
			}
		}
	}
	
	/**
	 * Loads the setup configuration.
	 */
	private void loadTimeModelFromSimulationSetup() {

		// --- Get the current SimulationSetup --------------------------------
		SimulationSetup simSetup = currProject.getSimulationSetups().getCurrSimSetup();
		// --- Get the right time model ---------------------------------------
		TimeModel timeModel = this.getTimeModel();
		if (timeModel!=null) {
			// --- Set the configuration from setup ---------------------------
			HashMap<String, String> configHash = simSetup.getTimeModelSettings();
			if (configHash!=null) {
				timeModel.setTimeModelSettings(configHash);
				// --- Set the TimeModel to the display -----------------------
				this.updateTimeModelVisualization();
				// --- Forward time format to ontology visualization? ---------
				if (timeModel!=null && timeModel instanceof TimeModelDateBased) {
					TimeModelDateBased tmDateBased = (TimeModelDateBased) timeModel;
					OntologyVisualisationConfiguration.setTimeFormat(tmDateBased.getTimeFormat());
				}
			}	
		}
	}
	
	/**
	 * Save the current TimeModel to the current simulation setup.
	 */
	public void saveTimeModelToSimulationSetup() {
		SimulationSetup simSetup = this.currProject.getSimulationSetups().getCurrSimSetup();
		if (simSetup!=null && this.getTimeModel()!=null) {
			simSetup.setTimeModelSettings(this.getTimeModel().getTimeModelSetting());
		}
	}
	
}
