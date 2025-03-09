package org.awb.env.networkModel.visualisation.notifications;

import de.enflexit.awb.simulation.environment.EnvironmentModel;

/**
 * The Class EnvironmentModelUpdateNotification can be used in order to explicitly
 * distribute a new {@link EnvironmentModel} to the DisplayAgent.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentModelUpdateNotification extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = -224881716018746102L;

	private EnvironmentModel envModel;

	/**
	 * Instantiates a new environment model update notification.
	 * @param envMode the EnvironmentModel to use
	 */
	public EnvironmentModelUpdateNotification(EnvironmentModel envMode) {
		this.setEnvironmentModel(envMode);
	}

	/**
	 * Sets the environment model.
	 * @param newEnvironmentModel the new environment model
	 */
	public void setEnvironmentModel(EnvironmentModel newEnvironmentModel) {
		this.envModel = newEnvironmentModel;
	}
	/**
	 * Gets the environment model.
	 * @return the environment model
	 */
	public EnvironmentModel getEnvironmentModel() {
		return envModel;
	}
	
}
