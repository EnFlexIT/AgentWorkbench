package de.enflexit.awb.desktop.project.setup;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.setup.SimulationSetupNotification;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;
import de.enflexit.common.properties.PropertiesPanel;

/**
 * The Class SetupPropertiesPanel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SetupPropertiesPanel extends PropertiesPanel implements Observer {

	private static final long serialVersionUID = 4372968290090987468L;

	private Project currProject;
	
	/**
	 * Instantiates a new project properties panel.
	 *
	 * @param currProject the current project
	 * @param header the header
	 */
	public SetupPropertiesPanel(Project currProject, String header) {
		super(currProject.getSimulationSetups().getCurrSimSetup().getProperties(), header);
		this.currProject = currProject;
		this.currProject.addObserver(this);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.Observer#update(de.enflexit.common.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {

		if (updateObject instanceof SimulationSetupNotification) {
			// --- Change inside the simulation setup ---------------
			SimulationSetupNotification scn = (SimulationSetupNotification) updateObject;
			switch (scn.getUpdateReason()) {
			case SIMULATION_SETUP_ADD_NEW:
			case SIMULATION_SETUP_COPY:
			case SIMULATION_SETUP_LOAD:
				this.setProperties(this.currProject.getSimulationSetups().getCurrSimSetup().getProperties());
				break;
			default:
				break;
			}
		} 
	}
	
}
