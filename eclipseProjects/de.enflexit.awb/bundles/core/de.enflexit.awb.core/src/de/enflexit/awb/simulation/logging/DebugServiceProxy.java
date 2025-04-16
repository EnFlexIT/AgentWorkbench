package de.enflexit.awb.simulation.logging;

import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.Node;
import jade.core.ServiceException;
import jade.core.SliceProxy;

import java.util.Vector;

/**
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class DebugServiceProxy extends SliceProxy implements DebugServiceSlice {

	private static final long serialVersionUID = -7016240061703852319L;

	@Override
	public void sendLocalConsoleOutput2Main(String localContainerName, Vector<String> lines2transfer) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(DEBUG_SEND_LOCAL_OUTPUT, DebugService.NAME, null);
			cmd.addParam(localContainerName);
			cmd.addParam(lines2transfer);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}			
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
		
	}
	


}