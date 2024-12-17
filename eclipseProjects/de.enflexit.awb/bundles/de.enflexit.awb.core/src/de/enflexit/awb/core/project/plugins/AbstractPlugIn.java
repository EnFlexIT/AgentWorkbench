package de.enflexit.awb.core.project.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.setup.AgentClassElement4SimStart;
import de.enflexit.awb.core.project.setup.SimulationSetup;
import de.enflexit.awb.core.project.setup.SimulationSetupNotification;
import de.enflexit.awb.core.ui.AwbProjectWindowTab;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;
import de.enflexit.common.ontology.OntologyVisualisationConfiguration;
import de.enflexit.common.ontology.gui.OntologyClassEditorJPanel;
import de.enflexit.common.ontology.gui.OntologyClassVisualisation;
import de.enflexit.common.ontology.gui.OntologyClassWidget;
import de.enflexit.common.ontology.gui.OntologyInstanceViewer;
import jade.core.ProfileImpl;

/**
 * This abstract class is the root for customized plug-in's, which can
 * be loaded to extend an individual agent project.<br>
 * Classes which inherit from this class can be registered to an
 * agent project by using the "Resources"-Tab in the project
 * configuration. 
 *   
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractPlugIn<MenueType, MenuItemType, ToolBarComponentType> implements AwbPlugIn {

	protected Project project = null;
	protected String classReference = null;

	private Vector<Object> customComponent = new Vector<>();
	private Vector<AwbProjectWindowTab> customProjectWindowTab = new Vector<AwbProjectWindowTab>();
	private Vector<OntologyClassVisualisation> customOntologyClassVisualisation = new Vector<OntologyClassVisualisation>();
	
	
	/**
	 * Constructor for this class.
	 * @param currProject the current project
	 */
	public AbstractPlugIn(Project currProject) {
		this.project = currProject;
		this.project.addObserver(this);		
	}
	
	/**
	 * Sets the class reference of this PugIn.
	 * @param classReference the classReference to set
	 */
	public void setClassReference(String classReference) {
		this.classReference = classReference;
	}
	/**
	 * Returns the class reference of the current {@link AbstractPlugIn}.
	 * @return the classReference as {@link String}
	 */
	public String getClassReference() {
		return classReference;
	}

	/**
	 * Returns the name of the current {@link AbstractPlugIn}.
	 * @return the pluginName
	 */
	public abstract String getName();

	/**
	 * This method be called if the plug-in is loaded into the project.
	 * This happens immediately after the project was opened. 
	 */
	public void onPlugIn() {
		System.out.println( "+ Plug-In loaded [" + this.getName() + "]" );
	}
	/**
	 * This method will be called if the plug-in will be removed from the 
	 * project, means immediately before the project will be closed.
	 */
	public void onPlugOut() {
		System.out.println( "- Plug-In removed [" + this.getName() + "]" );
	}
	
	/**
	 * This method will be invoked just after the onPlugOut() method 
	 * was executed.
	 * DO NOT OVERRIDE !!!
	 */
	public void afterPlugOut() {
		this.project.deleteObserver(this);
		this.removeCustomElements();
	}
	
	// --------------------------------------------------------------
	// --- Handling of custom elements for the GUI -------- START ---
	// --------------------------------------------------------------
	// --- Start of adding functions ------------
	/**
	 * This method can be used in order to add an individual menu.
	 * @param myMenu the my menu
	 */
	protected void addJMenu(MenueType myMenu) {
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().addPlugInMenu(myMenu);
		}
		customComponent.add(myMenu);
	}
	
	/**
	 * This method can be used in order to add an individual menu
	 * at a specified index position of the menu bar.
	 *
	 * @param myMenu the my menu
	 * @param indexPosition the index position
	 */
	protected void addJMenu(MenueType myMenu, int indexPosition) {
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().addPlugInMenu(myMenu, indexPosition);
		}
		customComponent.add(myMenu);
	}
	
	/**
	 * This method can be used in order to add an
	 * individual JMmenuItem to the given menu.
	 *
	 * @param menu2add the menu2add
	 * @param myMenuItemComponent the my menu item component
	 */
	protected void addJMenuItemComponent(MenueType menu2add, MenuItemType myMenuItemComponent) {
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().addPlugInMenuItemComponent(menu2add, myMenuItemComponent);
		}
		customComponent.add(myMenuItemComponent);
	}
	
	/**
	 * This method can be used in order to add an individual JMmenuItem
	 * at a specified index position of the given menu.
	 *
	 * @param menu2add the menu2add
	 * @param myMenuItemComponent the my menu item component
	 * @param indexPosition the index position
	 */
	protected void addJMenuItemComponent(MenueType menu2add, MenuItemType myMenuItemComponent, int indexPosition) {
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().addPlugInMenuItemComponent(menu2add, myMenuItemComponent, indexPosition);
		}
		customComponent.add(myMenuItemComponent);
	}
	
	/**
	 * This method can be used in order to add an
	 * individual menu button to the toolbar.
	 *
	 * @param myComponent the my component
	 */
	protected void addJToolbarComponent(ToolBarComponentType myComponent) {
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().addJToolbarComponent(myComponent);
		}
		customComponent.add(myComponent);
	}
	/**
	 * This method can be used in order to add an individual menu button
	 * a specified index position of the toolbar.
	 *
	 * @param myComponent the my component
	 * @param indexPosition the index position
	 */
	protected void addJToolbarComponent(ToolBarComponentType myComponent, int indexPosition) {
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().addJToolbarComponent(myComponent, indexPosition);
		}
		customComponent.add(myComponent);
	}
	
	/**
	 * This method can be used in order to add a customized Tab to the project window.
	 *
	 * @param projectWindowTab the project window tab
	 */
	protected void addProjectWindowTab(AwbProjectWindowTab projectWindowTab) {
		projectWindowTab.add();
		customProjectWindowTab.add(projectWindowTab);
	}
	
	/**
	 * This method can be used in order to add a customized Tab to the project window at the specified index position.
	 * The index position has to be greater than 1, in order to keep the 'Info'-Tab and the 'Configuration'-Tab at
	 * its provided position!
	 *
	 * @param projectWindowTab the project window tab
	 * @param indexPosition the index position (greater one)
	 */
	protected void addProjectWindowTab(AwbProjectWindowTab projectWindowTab, int indexPosition) {
		projectWindowTab.add(indexPosition);
		customProjectWindowTab.add(projectWindowTab);
	}
	
	/**
	 * Adds a OntologyClassVisualisation to the global settings. The idea of such an object is
	 * that you are able to mask / visualize a specified class of your Ontology in order to allow 
	 * a more common or simplified access to its data.
	 *
	 * @param ontologyClassVisualisation the class name of an OntologyClassVisualisation
	 * 
	 * @see OntologyClassVisualisation
	 * @see OntologyClassWidget
	 * @see OntologyClassEditorJPanel
	 * 
	 * @see GlobalInfo
	 * 
	 * @see OntologyVisualisationConfiguration#registerOntologyClassVisualisation(OntologyClassVisualisation)
	 * @see OntologyVisualisationConfiguration#unregisterOntologyClassVisualisation(String)
	 * @see OntologyVisualisationConfiguration#isRegisteredOntologyClassVisualisation(String)
	 * @see OntologyInstanceViewer
	 */
	protected void addOntologyClassVisualisation(OntologyClassVisualisation ontologyClassVisualisation) {
		if (ontologyClassVisualisation!=null) {
			OntologyClassVisualisation ontoClassVis = OntologyVisualisationConfiguration.registerOntologyClassVisualisation(ontologyClassVisualisation);
			if (ontoClassVis!=null) {
				this.customOntologyClassVisualisation.add(ontoClassVis);
			}
		}
	}
	
	// --- End of adding functions --------------
	
	/**
	 * This method can be used to remove all custom components
	 * for menus and for the toolbar.
	 */
	private void removeCustomElements() {
		
		// --- Remove custom toolbar/menu elements --------
		for (int i = 0; i < customComponent.size(); i++) {
			Object component = customComponent.get(i);
			Application.getMainWindow().removePlugInComponent(component);
		}
		customComponent = new Vector<>();
		
		// --- Remove custom tab elements -----------------
		for (int i = customProjectWindowTab.size()-1; i>-1; i--) {
			AwbProjectWindowTab pwt = customProjectWindowTab.get(i);
			pwt.remove();
		}
		customProjectWindowTab = new Vector<AwbProjectWindowTab>();
		
		// --- Remove custom ontology visualizations ------
		for (int i = customOntologyClassVisualisation.size()-1; i>-1; i--) {
			OntologyClassVisualisation ontoClVis = customOntologyClassVisualisation.get(i);
			OntologyVisualisationConfiguration.unregisterOntologyClassVisualisation(ontoClVis);
		}
		customOntologyClassVisualisation = new Vector<OntologyClassVisualisation>();
		
		// --- Refresh the main window --------------------
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().refreshView();
		}
		
	}
	// --------------------------------------------------------------
	// --- Handling of custom elements for the GUI -------- END -----
	// --------------------------------------------------------------
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.Observer#update(de.enflexit.common.Observable, java.lang.Object)
	 */
	@Override
	public final void update(Observable observable, Object updateObject) {
		
		try {

			// ----------------------------------------------------------
			this.updateFromObserver(observable, updateObject);
			// ----------------------------------------------------------
			
			// ----------------------------------------------------------
			// --- Changes in the Project-Configuration -----------------
			// ----------------------------------------------------------
			if (updateObject==null) {
				return;			
			} else if (updateObject.equals(Project.PREPARE_FOR_SAVING)) {
				this.onPrepareForSaving();
			} else if (updateObject.equals(Project.SAVED)) {
				this.onProjectSaved(false);
			} else if (updateObject.equals(Project.SAVED_EXCLUDING_SETUP)) {
				this.onProjectSaved(true);
			} else if (updateObject.equals(Project.CHANGED_ProjectName)) {
				this.onProjectChangedProjectName();
			} else if (updateObject.equals(Project.CHANGED_ProjectDescription)) {
				this.onProjectChangedProjectDescription();
			} else if (updateObject.equals(Project.CHANGED_ProjectFolder)) {
				// --- Do Nothing here !!! ----------
				// this.onProjectChangedProjectFolder();
			} else if (updateObject.equals(Project.CHANGED_ProjectView)) {
				this.onProjectChangedProjectView();
			} else if (updateObject.equals(Project.CHANGED_EnvironmentModelType)) {
				this.onProjectChangedEnvironmentModel();
			} else if (updateObject.equals(Project.CHANGED_StartArguments4BaseAgent)) {
				this.onProjectChangedAgentStartConfiguration();
			} else if (updateObject.equals(Project.CHANGED_TimeModelClass)) {
				this.onProjectChangedTimeModelConfiguration();
			} else if (updateObject.equals(Project.CHANGED_ProjectOntology)) {
				this.onProjectChangedProjectOntology();
			} else if (updateObject.equals(Project.CHANGED_ProjectResources)) {
				this.onProjectChangedProjectResources();
			} else if (updateObject.equals(Project.CHANGED_JadeConfiguration)) {
				this.onProjectChangedJadeConfiguration();
				
			} else if (updateObject.equals(Project.CHANGED_DistributionSetup)) {
				this.onProjectChangedDistributionSetup();
			} else if (updateObject.equals(Project.CHANGED_RemoteContainerConfiguration)) {
				this.onProjectChangedRemoteContainerConfiguration();
			} else if (updateObject.equals(Project.CHANGED_UserRuntimeObject)) {
				this.onProjectChangedUserRuntimeObject();
				
			// ----------------------------------------------------------
			// --- Changes with the SimulationSetups --------------------			
			// ----------------------------------------------------------
			} else if (updateObject instanceof SimulationSetupNotification) {
				
				SimulationSetupNotification sscn = (SimulationSetupNotification) updateObject;
				SimulationSetup simSetup = project.getSimulationSetups().getCurrSimSetup();
				switch (sscn.getUpdateReason()) {
				case SIMULATION_SETUP_ADD_NEW:
					this.onSimSetupAddNew(simSetup);
					break;
				case SIMULATION_SETUP_COPY:
					this.onSimSetupCopy(simSetup);
					break;
				case SIMULATION_SETUP_LOAD:
					this.onSimSetupLoad(simSetup);
					break;
				case SIMULATION_SETUP_PREPARE_SAVING:
					this.onSimSetupPrepareSaving(simSetup);
					break;
				case SIMULATION_SETUP_REMOVE:
					this.onSimSetupRemove(simSetup);
					break;
				case SIMULATION_SETUP_RENAME:
					this.onSimSetupRename(simSetup);
					break;
				case SIMULATION_SETUP_SAVED:
					this.onSimSetupSaved(simSetup);
					break;
					
				case SIMULATION_SETUP_DETAILS_LOADED:
					this.onSimSetupDetailsLoaded(simSetup);
					break;
				case SIMULATION_SETUP_DETAILS_SAVED:
					this.onSimSetupDetailsSaved(simSetup);
					break;
					
				case SIMULATION_SETUP_AGENT_ADDED:
					@SuppressWarnings("unchecked") 
					ArrayList<AgentClassElement4SimStart> agentAdded = (ArrayList<AgentClassElement4SimStart>) sscn.getNotificationObject();
					this.onSimSetupAgentAdded(simSetup, agentAdded);
					break;
				case SIMULATION_SETUP_AGENT_REMOVED:
					@SuppressWarnings("unchecked")
					ArrayList<AgentClassElement4SimStart> agentRemoved = (ArrayList<AgentClassElement4SimStart>) sscn.getNotificationObject();
					this.onSimSetupAgentRemoved(simSetup, agentRemoved);
					break;
				case SIMULATION_SETUP_AGENT_RENAMED:
					@SuppressWarnings("unchecked")
					HashMap<String, String> renamedHM = (HashMap<String, String>) sscn.getNotificationObject();
					String oldAgentName = renamedHM.get("oldAgentName");
					String newAgentName = renamedHM.get("newAgentName");
					this.onSimSetupAgentRenamed(simSetup, oldAgentName, newAgentName);
					break;
				default:
					break;
				}
			
			// ----------------------------------------------------------
			// --- Changes with the Project-PlugIns ---------------------			
			// ----------------------------------------------------------
			} else if (updateObject.toString().equals(AbstractPlugIn.CHANGED)) {

				PlugInNotification pin = (PlugInNotification) updateObject;
				int pinUpdate = pin.getUpdateReason();
				AwbPlugIn plugIn = pin.getPlugIn();
				if (pinUpdate == ADDED) {
					this.onPlugInAdded(plugIn);
				} else if (pinUpdate == REMOVED) {
					this.onPlugInRemoved(plugIn);
				}			
				
			} else {
				//System.out.println("Unknown Notification from Observer " + observable.toString() + ": " + updateObject.toString() );
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * In order to perceive individual informations from the {@link Project} {@link Observer}
	 * (observer pattern), this method should be used in the extended class.
	 * !!! Do NOT override the update method directly !!
	 *
	 * @param observable the observable
	 * @param updateObject the update object
	 */
	protected void updateFromObserver(Observable observable, Object updateObject) { }

	// ------------------------------------------------------------------------
	// --- Protected methods for the PlugIn-development --- START ------------- 
	// ------------------------------------------------------------------------
	
	// --- Changes in the project configuration --------------------------
	/**On notification to do the preparations to save a project or simulation setup. */
	protected void onPrepareForSaving() { };

	/**
	 * On project saved.
	 *
	 * @param isExcludeSetup indicator that reports, if the setup was excluded from the save operation
	 */
	protected void onProjectSaved(boolean isExcludeSetup) { }
	
	/** On project changed project name. */
	protected void onProjectChangedProjectName() { }
	
	/** On project changed project description. */
	protected void onProjectChangedProjectDescription() { }
	
	/** On project changed environment model. */
	protected void onProjectChangedEnvironmentModel() {	}
	
	/** On project changed agent start configuration. */
	protected void onProjectChangedAgentStartConfiguration() { }
	
	/** On project changed TimeModel configuration. */
	protected void onProjectChangedTimeModelConfiguration() { }
	
	/** On project changed project resources. */
	protected void onProjectChangedProjectResources() {	}
	
	/** On project changed jade configuration. */
	protected void onProjectChangedJadeConfiguration() { }
	
	/** On project changed project ontology. */
	protected void onProjectChangedProjectOntology() { }
	
	/** On project changed project view. */
	protected void onProjectChangedProjectView() {	}
	
	/** On project changed user runtime object. */
	private void onProjectChangedUserRuntimeObject() { }

	/** On project changed remote container configuration. */
	private void onProjectChangedRemoteContainerConfiguration() { }

	/** On project changed distribution setup. */
	private void onProjectChangedDistributionSetup() { }
	
	
	// --- Changes in the SimulationSetup --------------------------------
	/**
	 * Will be invoked, if a new {@link SimulationSetup} was created.
	 * @param simSetup the new {@link SimulationSetup}
	 */
	protected void onSimSetupAddNew(SimulationSetup simSetup) { }
	/**
	 * Will be invoked, if a {@link SimulationSetup} was copied.
	 * @param simSetup the copied {@link SimulationSetup}
	 */
	protected void onSimSetupCopy(SimulationSetup simSetup) { }
	/**
	 * Will be invoked, if a {@link SimulationSetup} was loaded.
	 * @param simSetup the loaded {@link SimulationSetup}
	 */
	protected void onSimSetupLoad(SimulationSetup simSetup) { }
	/**
	 * Will be invoked, if a {@link SimulationSetup} was removed.
	 * @param simSetup the current {@link SimulationSetup}
	 */
	protected void onSimSetupRemove(SimulationSetup simSetup) { }
	/**
	 * Will be invoked, if a {@link SimulationSetup} was renamed.
	 * @param simSetup the current {@link SimulationSetup}
	 */
	protected void onSimSetupRename(SimulationSetup simSetup) { }
	/**
	 * Will be invoked, before a {@link SimulationSetup} will be saved
	 * @param simSetup the current {@link SimulationSetup}
	 */
	protected void onSimSetupPrepareSaving(SimulationSetup simSetup) { }
	/**
	 * Will be invoked, if a {@link SimulationSetup} was saved
	 * @param simSetup the current {@link SimulationSetup}
	 */
	protected void onSimSetupSaved(SimulationSetup simSetup) { }
	
	/**
	 * Will be invoked, if the details (e.g. the environment model) of a {@link SimulationSetup} were loaded
	 * @param simSetup the current {@link SimulationSetup}
	 */
	protected void onSimSetupDetailsLoaded(SimulationSetup simSetup) { }
	/**
	 * Will be invoked, if the details (e.g. the environment model) of a {@link SimulationSetup} were saved
	 * @param simSetup the current {@link SimulationSetup}
	 */
	protected void onSimSetupDetailsSaved(SimulationSetup simSetup) { }
	
	/**
	 * Will be invoked, if an agent was added to a start list of a {@link SimulationSetup}.
	 * @param simSetSetup the {@link SimulationSetup}
	 * @param agentsAdded the agents added
	 */
	protected void onSimSetupAgentAdded(SimulationSetup simSetSetup, List<AgentClassElement4SimStart> agentsAdded) { };
	/**
	 * Will be invoked, if an agent was removed from a start list of a {@link SimulationSetup}.
	 * @param simsetSetup the {@link SimulationSetup}
	 * @param agentsRemoved the list of agents removed
	 */
	protected void onSimSetupAgentRemoved(SimulationSetup simsetSetup, List<AgentClassElement4SimStart> agentsRemoved) { };
	/**
	 * Will be invoked, if an agent was renamed in a start list of a {@link SimulationSetup}.
	 * @param simsetSetup the {@link SimulationSetup}
	 * @param oldAgentName the old agent name
	 * @param newAgentName the new agent name
	 */
	protected void onSimSetupAgentRenamed(SimulationSetup simsetSetup, String oldAgentName, String newAgentName) { };
	
	
	// --- Changes with the PlugIns --------------------------------------
	/**
	 * On plug in removed.
	 * @param plugIn the plug in
	 */
	protected void onPlugInRemoved(AwbPlugIn plugIn) { }
	
	/**
	 * On plug in added.
	 * @param plugIn the plug in
	 */
	protected void onPlugInAdded(AwbPlugIn plugIn) { }

	/**
	 * Overriding his method allows to extend/change the currently 
	 * used Profile JADE container.
	 * 
	 * @param jadeContainerProfile The profile to CHANGE
	 * @return the SetupChangeEvent (!) configuration of the JADE Profile
	 */
	public ProfileImpl getJadeProfile(ProfileImpl jadeContainerProfile) {
		return jadeContainerProfile;
	}

	/**
	 * Checks for valid precondition before the MAS execution.
	 * @return true, if the preconditions are valid
	 */
	public boolean hasValidPreconditionForMasExecution() {
		return true;
	}

	/**
	 * Will be executed, if the Multi-Agent System is about to start.
	 */
	public void onMasWillBeExecuted() {	}

	/**
	 * Will be executed, if the Multi-Agent System was terminated.
	 */
	public void onMasWasTerminated() {	}
	
	
	// ------------------------------------------------------------------------
	// --- Protected methods for the PlugIn-development --- END --------------- 
	// ------------------------------------------------------------------------
}