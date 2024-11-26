package de.enflexit.awb.simulation.environment;

import de.enflexit.common.AbstractUserObject;


/**
 * The Class AbstractEnvironmentModel has to be used in environment models 
 * in order to define an abstract environment model that can exist beside
 * the displayEnvironment model and the time model.
 * 
 * @see EnvironmentModel
 * @see EnvironmentModel#getAbstractEnvironment()
 * @see EnvironmentController
 * @see EnvironmentController#getAbstractEnvironmentModel() 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public abstract class AbstractEnvironmentModel extends AbstractUserObject {

	private static final long serialVersionUID = 7052170323244857840L;

	/**
	 * Has to return a copy of the current abstract environment.
	 * @return the copy
	 */
	public abstract AbstractEnvironmentModel getCopy();
	
}
