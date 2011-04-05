package agentgui.core.plugin;

import java.util.Observable;
import java.util.Observer;

import agentgui.core.application.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.core.sim.setup.SimulationSetups;
import agentgui.core.sim.setup.SimulationSetupsChangeNotification;

/**
 * This class is the root for a customised plugin, which can
 * be loaded to extend an individual agent project.
 * Classes which inherit from this class can be registered to an
 * agent project by using the "Resources"-Tab in the project
 * configuration. 
 *   
 * @author Christian Derksen
 */
public abstract class PlugIn implements Observer {

	public static final String CHANGED = "PlugIns";
	public static final int ADDED = 1;
	public static final int REMOVED = 2;
	
	private Project project = null;
	private String classReference = null;
	
	/**
	 * Default constructor for this class
	 * @param currProject
	 */
	public PlugIn(Project currProject) {
		this.project = currProject;
		this.project.addObserver(this);		
	}
	
	/**
	 * @param classReference the classReference to set
	 */
	public void setClassReference(String classReference) {
		this.classReference = classReference;
	}
	/**
	 * @return the classReference
	 */
	public String getClassReference() {
		return classReference;
	}

	/**
	 * @return the pluginName
	 */
	public abstract String getName();

	/**
	 * This method be called if the plugin is loaded into the project.
	 * This happens immediately after the project was opened. 
	 */
	public void onPlugIn() {
		System.out.println( "+ PlugIn loaded [" + this.getName() + "]" );
	}
	/**
	 * This method will be called if the plugin will be removed from the 
	 * project, means immediately before the project will be closed.
	 */
	public void onPlugOut() {
		this.project.deleteObserver(this);
		System.out.println( "- PlugIn removed [" + this.getName() + "]" );
	}
	
	/**
	 * AgentGUI uses the observer pattern to inform about changes 
	 * within the project. They can consist on the following kinds
	 * of notifications  
	 * 
	 * - String Project.SAVED
	 * - String Project.CHANGED_ProjectName
	 * - String Project.CHANGED_ProjectDescription 
	 * - String Project.CHANGED_ProjectFolder
	 * - String Project.CHANGED_ProjectView 
	 * - String Project.CHANGED_EnvironmentModel
	 * - String Project.CHANGED_AgentReferences 
	 * - String Project.CHANGED_ProjectOntology 
	 * - String Project.CHANGED_ProjectResources
	 * 
	 * - String SimulationSetups.CHANGED
	 * 	 Here in Detail, while using the 
	 *   'SimulationSetupsChangeNotification.getUpdateReason' - method:
	 * 	 => int SimulationSetups.SIMULATION_SETUP_LOAD
	 * 	 => int SimulationSetups.SIMULATION_SETUP_ADD_NEW
	 * 	 => int SimulationSetups.SIMULATION_SETUP_COPY
	 * 	 => int SimulationSetups.SIMULATION_SETUP_REMOVE
	 * 	 => int SimulationSetups.SIMULATION_SETUP_RENAME
	 *   => int SimulationSetups.SIMULATION_SETUP_SAVED
	 * 
	 * - String PlugIn.CHANGED
	 * 	 Here in Detail, while using the 
	 *   'PlugInNotification.getUpdateReason' - method:
	 *   => int PlugIn.ADDED
	 *   => int PlugIn.REMOVED
	 *   Furthermore the instance of the PlugIn can be get
	 *   by using 'PlugInNotification.getPlugIn()'
	 *   	 
	 * 
	 * Do NOT override this methode directly. Use the updateFromObserver()-method
	 * instead, in order to get your individual Observer-Changes.  
	 * 
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		// ----------------------------------------------------------
		this.updateFromObserver(observable, updateObject);
		// ----------------------------------------------------------
		
		// ----------------------------------------------------------
		// --- Changes in the Project-Configuration -----------------
		// ----------------------------------------------------------
		if (updateObject==null) {
			return;			
		} else if (updateObject.equals(Project.SAVED)) {
			this.onProjectSaved();
		} else if (updateObject.equals(Project.CHANGED_ProjectName)) {
			this.onProjectChangedProjectName();
		} else if (updateObject.equals(Project.CHANGED_ProjectDescription)) {
			this.onProjectChangedProjectDescription();
		} else if (updateObject.equals(Project.CHANGED_ProjectFolder)) {
			// --- Do Nothing here !!! ----------
			// this.onProjectChangedProjectFolder();
		} else if (updateObject.equals(Project.CHANGED_ProjectView)) {
			this.onProjectChangedProjectView();
		} else if (updateObject.equals(Project.CHANGED_EnvironmentModel)) {
			this.onProjectChangedEnvironmentModel();
		} else if (updateObject.equals(Project.CHANGED_AgentStartConfiguration)) {
			this.onProjectChangedAgentStartConfiguration();
		} else if (updateObject.equals(Project.CHANGED_ProjectOntology)) {
			this.onProjectChangedProjectOntology();
		} else if (updateObject.equals(Project.CHANGED_ProjectResources)) {
			this.onProjectChangedProjectResources();
		
		// ----------------------------------------------------------
		// --- Changes with the SimulationSetups --------------------			
		// ----------------------------------------------------------
		} else if (updateObject.equals(SimulationSetups.CHANGED)) {
			
			SimulationSetupsChangeNotification sscn = (SimulationSetupsChangeNotification) updateObject;
			int sscnUpdate = sscn.getUpdateReason();
			SimulationSetup simSetup = project.simSetups.getCurrSimSetup();
			if (sscnUpdate==SimulationSetups.SIMULATION_SETUP_ADD_NEW) {
				this.onSimSetupChangedAddNew(simSetup);
			} else if (sscnUpdate==SimulationSetups.SIMULATION_SETUP_COPY) {
				this.onSimSetupChangedCopy(simSetup);
			} else if (sscnUpdate==SimulationSetups.SIMULATION_SETUP_LOAD) {
				this.onSimSetupChangedLoad(simSetup);
			} else if (sscnUpdate==SimulationSetups.SIMULATION_SETUP_REMOVE) {
				this.onSimSetupChangedRemove(simSetup);
			} else if (sscnUpdate==SimulationSetups.SIMULATION_SETUP_RENAME) {
				this.onSimSetupChangedRename(simSetup);
			} else if (sscnUpdate==SimulationSetups.SIMULATION_SETUP_SAVED) {
				this.onSimSetupChangedSaved(simSetup);				
			}
		
		// ----------------------------------------------------------
		// --- Changes with the Project-PlugIns ---------------------			
		// ----------------------------------------------------------
		} else if (updateObject.equals(PlugIn.CHANGED)) {

			PlugInNotification pin = (PlugInNotification) updateObject;
			int pinUpdate = pin.getUpdateReason();
			PlugIn plugIn = pin.getPlugIn();
			if (pinUpdate == PlugIn.ADDED) {
				this.onPlugInAdded(plugIn);
			} else if (pinUpdate == PlugIn.REMOVED) {
				this.onPlugInRemoved(plugIn);
			}			
			
		} else {
			//System.out.println("Unknown Notification from Observer " + observable.toString() + ": " + updateObject.toString() );
		}
		
		
	}

	/**
	 * In order to perceive individual informations from the project Observer
	 * (observer pattern), this mehtod should be used in the extended class.
	 * !!! Do NOT override the update methode directly !! 
	 * @param observable
	 * @param updateObject
	 */
	protected void updateFromObserver(Observable observable, Object updateObject) {
	}

	// ------------------------------------------------------------------------
	// --- Protected methods for the PlugIn-development --- START ------------- 
	// ------------------------------------------------------------------------
	
	// --- Changes in the project configuration --------------------------
	protected void onProjectSaved() {
	}
	protected void onProjectChangedProjectName() {
	}
	protected void onProjectChangedProjectDescription() {
	}
	protected void onProjectChangedEnvironmentModel() {
	}
	protected void onProjectChangedAgentStartConfiguration() {
	}
	protected void onProjectChangedProjectResources() {
	}
	protected void onProjectChangedProjectOntology() {
	}
	protected void onProjectChangedProjectView() {
	}
	
	// --- Changes in the SimulationSetup --------------------------------
	private void onSimSetupChangedAddNew(SimulationSetup currSimSetup) {
	}
	private void onSimSetupChangedCopy(SimulationSetup currSimSetup) {
	}
	private void onSimSetupChangedLoad(SimulationSetup currSimSetup) {
	}
	private void onSimSetupChangedRemove(SimulationSetup currSimSetup) {
	}
	private void onSimSetupChangedRename(SimulationSetup currSimSetup) {
	}
	private void onSimSetupChangedSaved(SimulationSetup currSimSetup) {
	}

	// --- Changes with the PlugIns --------------------------------------
	private void onPlugInRemoved(PlugIn plugIn) {
	}
	private void onPlugInAdded(PlugIn plugIn) {
	}

	// ------------------------------------------------------------------------
	// --- Protected methods for the PlugIn-development --- END --------------- 
	// ------------------------------------------------------------------------
	
}
