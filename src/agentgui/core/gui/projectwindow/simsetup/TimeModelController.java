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

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.projectwindow.ProjectWindowTab;
import agentgui.core.project.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.core.sim.setup.SimulationSetupNotification;
import agentgui.simulationService.time.JPanel4TimeModelConfiguration;
import agentgui.simulationService.time.TimeModel;


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
	
	private int indexPosOfTimeModel = 0;
	
	
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
		JPanel4TimeModelConfiguration configPanel = this.getDisplayJPanel4Configuration();
		if (configPanel!=null) {
			configPanel.setTimeModel(newTimeModel);
		} 
	}
	/**
	 * Returns the current time model.
	 * @return the time model
	 */
	public TimeModel getTimeModel() {
		JPanel4TimeModelConfiguration configPanel = this.getDisplayJPanel4Configuration();
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
			simSetup.setTimeModelSettings(this.getDisplayJPanel4Configuration().getTimeModel().getTimeModelSetting());
		}
	}
	
	/**
	 * Sets the display for the selected TimeModel.
	 * @param display4TimeModel the new DisplayJPanel4Configuration
	 */
	public void setDisplay4TimeModel(JPanel4TimeModelConfiguration display4TimeModel) {
		this.display4TimeModel = display4TimeModel;
	}
	/**
	 * Returns the configuration display for the TimeModel.
	 * @return the DisplayJPanel4Configuration
	 */
	public JPanel4TimeModelConfiguration getDisplayJPanel4Configuration() {
		if (display4TimeModel==null) {
			
			this.currTimeModelClass = this.currProject.getTimeModelClass();
			if (this.currTimeModelClass!=null && this.currTimeModelClass.length()!=0) {

				try {
					@SuppressWarnings("unchecked")
					Class<? extends TimeModel> timeModelClass = (Class<? extends TimeModel>) Class.forName(this.currTimeModelClass);
					TimeModel timeModel = (TimeModel) timeModelClass.newInstance();
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
	 * Sets the time model display to project the window.
	 */
	private void addTimeModelDisplayToProjectWindow() {
		
		if (this.pwt!=null) {
			this.indexPosOfTimeModel = this.getIndexPositionOfProjectWindowTab();
			this.pwt.remove();
			this.pwt = null;
			this.display4TimeModel = null;
		}
		
		JPanel4TimeModelConfiguration configPanel = this.getDisplayJPanel4Configuration();
		if (configPanel!=null) {
			this.pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Zeit-Konfiguration"), null, null, configPanel, Language.translate(ProjectWindowTab.TAB_4_SUB_PANES_Setup));
			this.pwt.setIndexPosition(this.indexPosOfTimeModel);
			this.pwt.add(this.indexPosOfTimeModel);
			this.setTimeModel(null);
		}
	}
	
	/**
	 * Removes the time model display to project window.
	 */
	private void removeTimeModelDisplayFromProjectWindow() {
		if (this.pwt!=null) {
			this.indexPosOfTimeModel = this.getIndexPositionOfProjectWindowTab();
			this.pwt.remove();
			this.pwt = null;
			this.display4TimeModel = null;
			this.currTimeModelClass = null;
		}
	}
	
	/**
	 * Returns the current index position of the project window tab.
	 * @return the index position of project window tab
	 */
	private int getIndexPositionOfProjectWindowTab() {
		int indexPosFound = 0;
		if (this.pwt!=null) {
			JComponent component = this.pwt.getJComponentForVisualization();
			if (component.getParent()!=null) {
				JTabbedPane jTabbedPane = (JTabbedPane) component.getParent();	
				indexPosFound = jTabbedPane.indexOfComponent(this.pwt.getJComponentForVisualization());
			}
		}
		return indexPosFound;
	}
	
	/**
	 * Setup load.
	 */
	private void setupLoad() {

		// --- Get the current SimulationSetup ------------
		SimulationSetup simSetup = currProject.getSimulationSetups().getCurrSimSetup();
		// --- Get the right time model -------------------
		TimeModel timeModel = this.getTimeModel();
		if (timeModel!=null) {
			// --- Set the configuration from setup -------
			HashMap<String, String> configHash = simSetup.getTimeModelSettings();
			if (configHash!=null) {
				timeModel.setTimeModelSettings(configHash);
				// --- Set the TimeModel to the display ---
				this.setTimeModel(timeModel);	
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
				this.addTimeModelDisplayToProjectWindow();
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
