package de.enflexit.awb.ws.core;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * The Class WSApplication.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class WSApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Starting WS-Application");
		return null;
	}

	@Override
	public void stop() {
		System.out.println("Stopping WS-Application");

	}

}
