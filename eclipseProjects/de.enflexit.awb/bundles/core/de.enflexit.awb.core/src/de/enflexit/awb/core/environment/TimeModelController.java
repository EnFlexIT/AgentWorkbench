package de.enflexit.awb.core.environment;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.classLoadService.ClassLoadServiceUtility;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.setup.SimulationSetup;
import de.enflexit.awb.core.project.setup.SimulationSetupNotification;
import de.enflexit.awb.core.ui.AgentWorkbenchUiManager;
import de.enflexit.awb.core.ui.AwbProjectWindowTab;
import de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.awb.simulation.environment.time.TimeModelDateBased;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;
import de.enflexit.common.ontology.OntologyVisualisationConfiguration;
import de.enflexit.language.Language;


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
	
	private AwbProjectWindowTab pwt;
	private AwbTimeModelConfigurationWidget timeModelConfigurationWidget;
	private int indexPositionOfTimeModelTab = 0;
	
	private String currentSetup;
	
	/**
	 * Instantiates a new time model controller.
	 * @param project the current project
	 */
	public TimeModelController(Project project) {
		this.currProject = project;
		this.currProject.addObserver(this);
	}
	/**
	 * Initializes the {@link TimeModelController}. May be invoked after instance generation;
	 */
	public void initialize() {
		this.addTimeModelDisplayToProjectWindow();
	}
	
	/**
	 * Sets the time model.
	 * @param newTimeModel the new time model
	 */
	public void setTimeModel(TimeModel newTimeModel) {
		this.timeModel = newTimeModel;
		this.saveTimeModelToSimulationSetup();
	}
	/**
	 * Returns the current time model.
	 * @return the time model
	 */
	public TimeModel getTimeModel() {

		// --- Check, if a TimeModel class is defined -----  
		String timeModelClass = this.currProject.getTimeModelClass();
		boolean isNoClass = timeModelClass==null || timeModelClass.isEmpty();
		if (isNoClass==true && this.timeModel!=null) {
			this.setTimeModel(null);
		}
		
		// --- Create new TimeModel instance? -------------
		boolean isDifferentClass = timeModel!=null && this.currProject.getTimeModelClass()!=null && this.currProject.getTimeModelClass().equals(timeModel.getClass().getName())==false;
		if (timeModel==null || isDifferentClass==true) {
			if (isNoClass==false) {
				try {
					timeModel = ClassLoadServiceUtility.getTimeModelInstance(timeModelClass);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException | NoSuchMethodException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		// --- Load data from setup? ----------------------
		boolean isDifferentSetup = currentSetup==null || currentSetup.equals(currProject.getSimulationSetupCurrent())==false;
		if (isDifferentSetup==true) {
			currentSetup = currProject.getSimulationSetupCurrent();
			this.loadTimeModelFromSimulationSetup();
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
	private AwbTimeModelConfigurationWidget geTimeModelConfigurationWidget() {
		if (Application.isOperatingHeadless()==false && timeModelConfigurationWidget==null && this.getTimeModel()!=null) {
			try {
				timeModelConfigurationWidget = this.getTimeModel().getConfigurationWidget();
				timeModelConfigurationWidget.setProject(this.currProject);
				timeModelConfigurationWidget.setTimeModelController(this);
				timeModelConfigurationWidget.addAsProjectObserver();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return timeModelConfigurationWidget;
	}
	/**
	 * Sets the time model display to project the window.
	 */
	private void addTimeModelDisplayToProjectWindow() {
		
		if (Application.isOperatingHeadless()==true) return;
		
		// --- First, remove time model visualization -----
		this.removeTimeModelDisplayFromProjectWindow();
		
		// --- Add the new time model visualization -------
		AwbTimeModelConfigurationWidget tmcWidget = this.geTimeModelConfigurationWidget();
		if (tmcWidget!=null) {
			this.pwt = AgentWorkbenchUiManager.getInstance().createProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Zeit-Konfiguration"), null, null, tmcWidget, Language.translate(AwbProjectWindowTab.TAB_4_SUB_PANES_Setup));
			this.pwt.add(this.indexPositionOfTimeModelTab);
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
			if (this.timeModelConfigurationWidget!=null) {
				this.timeModelConfigurationWidget.deleteAsProjectObserver();
				this.timeModelConfigurationWidget = null;
			}
		}
	}
	
	/**
	 * Sets the index position of time model tab.
	 * @param newIndexPosistion the new index position of time model tab
	 */
	public void setIndexPositionOfTimeModelTab(int newIndexPosistion) {
		
		if (newIndexPosistion!=this.indexPositionOfTimeModelTab && this.pwt!=null && this.geTimeModelConfigurationWidget()!=null) {
			// --- Remove current tab ---------------------  
			this.removeTimeModelDisplayFromProjectWindow();
			// --- Set the new position of the tab --------
			this.indexPositionOfTimeModelTab = newIndexPosistion;
			// --- Add tab again -------------------------- 
			this.addTimeModelDisplayToProjectWindow();
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
			} else {
				// --- Display the new TimeModel display ------------
				this.addTimeModelDisplayToProjectWindow();
				this.loadTimeModelFromSimulationSetup();
			}
			
		} else if (updateObject instanceof SimulationSetupNotification) {
			// --- Change inside the simulation setup ---------------
			SimulationSetupNotification scn = (SimulationSetupNotification) updateObject;
			switch (scn.getUpdateReason()) {
			case SIMULATION_SETUP_PREPARE_SAVING:
				this.saveTimeModelToSimulationSetup();
				break;
				
			default:
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
