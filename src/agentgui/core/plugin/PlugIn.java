package agentgui.core.plugin;

import java.util.Observable;
import java.util.Observer;

import agentgui.core.application.Project;

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

	/**
	 * Default constructor for this class
	 * @param currProject
	 */
	public PlugIn(Project currProject) {
		this.project = currProject;
		this.project.addObserver(this);		
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
		System.out.println( "- PlugIn unloaded/removed [" + this.getName() + "]" );
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
	 */
	@Override
	public void update(Observable observable, Object arg) {
		
		
		
		
	}

	
	
}
