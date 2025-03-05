package de.enflexit.awb.ws.core;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import de.enflexit.awb.core.Application;
import de.enflexit.language.Language;

/**
 * The Class WSApplication.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class WSApplication implements IApplication {

	private boolean isDebug = true;
	
	private IApplicationContext iApplicationContext;
	private Integer appReturnValue = IApplication.EXIT_OK;
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {
		
		if (this.isDebug==true) {
			System.out.println("Starting WS-Application");
		}
		
		// --- Remind application context -----------------
		this.iApplicationContext = context;

		// --- Wait for termination of application --------
		this.waitForApplicationTermination();

		// --- Stop the Application class -----------------
		System.out.println(Language.translate("Programmende... "));
		this.stop();

		return appReturnValue;
	}

	/**
	 * Waits for the termination of the application.
	 */
	private void waitForApplicationTermination() throws Exception {
		// --- Wait for termination of the application ----
		while (Application.isQuitJVM()==false) {
			Thread.sleep(250);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		
		if (this.isDebug==true) {
			System.out.println("Stopping WS-Application");
			
		}

	}

	
}
