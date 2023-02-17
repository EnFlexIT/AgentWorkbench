package org.agentgui.gui.swing.project;

import agentgui.core.project.Project;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertiesPanel;

/**
 * The Class ProjectPropertiesPanel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ProjectPropertiesPanel extends PropertiesPanel implements Observer {

	private static final long serialVersionUID = 4372968290090987468L;

	private Project currProject;
	
	/**
	 * Instantiates a new project properties panel.
	 *
	 * @param currProject the current project
	 * @param header the header
	 */
	public ProjectPropertiesPanel(Project currProject, String header) {
		super(currProject.getProperties(), header);
		this.currProject = currProject;
		this.currProject.addObserver(this);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.Observer#update(de.enflexit.common.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		String updateReason = updateObject.toString();
		if (updateReason.equalsIgnoreCase(Project.PREPARE_FOR_SAVING)) {
			// --- Get the new settings from the UI -------
			Properties newProps = this.getProperties();  
			if (this.currProject.getProperties().equals(newProps)==false) {
				this.currProject.setProperties(newProps);
			}
			
		}
	}
	
}
