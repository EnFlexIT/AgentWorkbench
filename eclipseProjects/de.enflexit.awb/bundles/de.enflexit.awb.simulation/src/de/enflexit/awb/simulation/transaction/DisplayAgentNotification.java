package de.enflexit.awb.simulation.transaction;

import java.io.Serializable;

import de.enflexit.awb.core.simulation.agents.SimulationManagerAgent;


/**
 * The abstract Class DisplayAgentNotification is the base class for notifications that can 
 * be directed to display agents that are working on an environment model, located in
 * {@link EnvironmentModel#getDisplayEnvironment()}. 
 * For the possible notifications that can be send to specialized display agents, have 
 * a look to the corresponding packages of a special environment model (e.g. the graph environment). 
 *  
 * @see SimulationManagerAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class DisplayAgentNotification implements Serializable {

	private static final long serialVersionUID = -2755204178753148481L;
	
}
