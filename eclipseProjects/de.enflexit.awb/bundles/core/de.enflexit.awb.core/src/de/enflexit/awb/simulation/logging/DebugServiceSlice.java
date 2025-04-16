package de.enflexit.awb.simulation.logging;

import jade.core.IMTPException;
import jade.core.Service;

import java.util.Vector;

/**
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface DebugServiceSlice extends Service.Slice {

	// ----------------------------------------------------------
	// --- Horizontal commands of the service -------------------
	// ----------------------------------------------------------
	
	static final String DEBUG_SEND_LOCAL_OUTPUT = "send-local-output";
	public void sendLocalConsoleOutput2Main(String localContainerName, Vector<String> lines2transfer) throws IMTPException;
	
}
