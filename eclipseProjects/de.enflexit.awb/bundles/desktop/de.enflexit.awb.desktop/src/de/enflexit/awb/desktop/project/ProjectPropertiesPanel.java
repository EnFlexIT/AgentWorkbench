package de.enflexit.awb.desktop.project;

import de.enflexit.awb.core.project.Project;
import de.enflexit.common.properties.PropertiesPanel;

/**
 * The Class SetupPropertiesPanel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ProjectPropertiesPanel extends PropertiesPanel {

	private static final long serialVersionUID = 4372968290090987468L;

	/**
	 * Instantiates a new project properties panel.
	 *
	 * @param currProject the current project
	 * @param header the header
	 */
	public ProjectPropertiesPanel(Project currProject, String header) {
		super(currProject.getProperties(), header);
	}
	
}
