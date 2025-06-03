package de.enflexit.awb.simulation.logging;

import jade.core.ServiceException;
import jade.core.ServiceHelper;


/**
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface DebugServiceHelper extends ServiceHelper {

	public static final String SERVICE_NAME = DebugService.class.getName();
	
	// --- Methods for distributed debugging ------------------------
	public void sendLocalConsoleOutput() throws ServiceException;
	
}
