package agentgui.core.plugin;

import java.util.Observable;
import java.util.Observer;

import agentgui.core.application.Project;

/**
 * This class is the root for a customized plugin, which can
 * be loaded to extend an individual agent project.
 * Classes which inherit from this class can be registerd to an
 * agent project by using the "Resources"-Tab in the project
 * configuration. 
 *   
 * @author Christian Derksen
 */
public class ProjectPlugin implements Observer{

	private Project project = null;
	
	/**
	 * Default constructor for this class
	 * @param currProject
	 */
	public ProjectPlugin(Project currProject) {
		this.project = currProject;
		this.project.addObserver(this);
	}
	
	/**
	 * This method be called if the plugin is loaded into the projekt.
	 * This happens immediatly after the project was opened. 
	 */
	public void onPlugIn() {
	}
	/**
	 * This method will be called if the plugin will be removed from the 
	 * projekt, means immediatly before the project will be closed.
	 */
	public void onPlugOut() {
	}

	/**
	 * AgentGUI uses the observer pattern to inform about changes 
	 * within the project. They can be  
	 */
	@Override
	public void update(Observable o, Object arg) {
		
	}

	
	
}
