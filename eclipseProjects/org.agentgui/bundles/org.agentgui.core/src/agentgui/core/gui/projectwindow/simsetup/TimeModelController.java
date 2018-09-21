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
	
	private String currTimeModelClass;
	private ProjectWindowTab pwt;
	private JPanel4TimeModelConfiguration display4TimeModel;
	
	private int indexPositionOfTimeModelTab = 0;
	
	
	/**
	 * Instantiates a new time model controller.
	 */
	public TimeModelController(Project project) {
		this.currProject = project;
		this.currProject.addObserver(this);
		if (Application.isOperatingHeadless()==false) {
			this.addTimeModelDisplayToProjectWindow();
		}
		this.setupLoad();
	}
	
	
	/**
	 * Sets the time model.
	 * @param newTimeModel the new time model
	 */
	public void setTimeModel(TimeModel newTimeModel) {
		JPanel4TimeModelConfiguration configPanel = this.getJPanel4TimeModelConfiguration();
		if (configPanel!=null) {
			configPanel.setTimeModel(newTimeModel);
		} 
	}
	/**
	 * Returns the current time model.
	 * @return the time model
	 */
	public TimeModel getTimeModel() {
		JPanel4TimeModelConfiguration configPanel = this.getJPanel4TimeModelConfiguration();
		if (configPanel!=null) {
			return configPanel.getTimeModel();
		} 
		return null;
	}
	/**
	 * Returns, if available,  a copy of the current TimeModel.
	 * @return the TimeModel copy
	 */
	public TimeModel getTimeModelCopy() {
		TimeModel timeModelCopy = null;
		if (this.getTimeModel()!=null) {
			timeModelCopy = this.getTimeModel().getCopy();
		}
		return timeModelCopy;
	}
	
	/**
	 * Save the current TimeModel to the current simulation setup.
	 */
	public void saveTimeModelToSimulationSetup() {
		SimulationSetup simSetup = this.currProject.getSimulationSetups().getCurrSimSetup();
		if (simSetup!=null) {
			simSetup.setTimeModelSettings(this.getJPanel4TimeModelConfiguration().getTimeModel().getTimeModelSetting());
		}
	}
	
	/**
	 * Returns the JPanel for the time model configuration.
	 * @return the DisplayJPanel4Configuration
	 */
	public JPanel4TimeModelConfiguration getJPanel4TimeModelConfiguration() {
		if (display4TimeModel==null) {
			
			this.currTimeModelClass = this.currProject.getTimeModelClass();
			if (this.currTimeModelClass!=null && this.currTimeModelClass.length()!=0) {

				try {
					TimeModel timeModel = ClassLoadServiceUtility.getTimeModelInstance(this.currTimeModelClass);
					this.display4TimeModel = timeModel.getJPanel4Configuration(this.currProject);

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return display4TimeModel;
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
	
	/**
	 * Sets the time model display to project the window.
	 */
	private void addTimeModelDisplayToProjectWindow() {
		
		if (this.pwt!=null) {
			this.indexPositionOfTimeModelTab = this.pwt.getIndexPosition();
			this.pwt.remove();
			this.pwt = null;
			this.display4TimeModel = null;
		}
		
		JPanel4TimeModelConfiguration configPanel = this.getJPanel4TimeModelConfiguration();
		if (configPanel!=null) {
			this.pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Zeit-Konfiguration"), null, null, configPanel, Language.translate(ProjectWindowTab.TAB_4_SUB_PANES_Setup));
			this.pwt.add(this.indexPositionOfTimeModelTab);
			configPanel.setTimeModel(null);
		}
	}
	
	/**
	 * Removes the time model display to project window.
	 */
	private void removeTimeModelDisplayFromProjectWindow() {
		if (this.pwt!=null) {
			this.indexPositionOfTimeModelTab = this.pwt.getIndexPosition();
			this.pwt.remove();
			this.pwt = null;
			this.display4TimeModel = null;
			this.currTimeModelClass = null;
		}
	}
	
	/**
	 * Loads the setup configuration.
	 */
	private void setupLoad() {

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
				this.setTimeModel(timeModel);
				// --- Forward time format to ontology visualization? ---------
				if (timeModel!=null && timeModel instanceof TimeModelDateBased) {
					TimeModelDateBased tmDateBased = (TimeModelDateBased) timeModel;
					OntologyVisualisationConfiguration.setTimeFormat(tmDateBased.getTimeFormat());
				}
			}	
		}
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
				
			} else if (this.currProject.getTimeModelClass().equals(this.currTimeModelClass)==false) {
				// --- Display the new TimeModel display ------------
				if (Application.isOperatingHeadless()==false) {
					this.addTimeModelDisplayToProjectWindow();
				}
				this.setupLoad();
			}
			
		} else if (updateObject instanceof SimulationSetupNotification) {
			// --- Change inside the simulation setup ---------------
			SimulationSetupNotification scn = (SimulationSetupNotification) updateObject;
			switch (scn.getUpdateReason()) {
			case SIMULATION_SETUP_SAVED:
				break;
				
			default:
				this.setupLoad();	
				break;
			}
		}
	}

}
