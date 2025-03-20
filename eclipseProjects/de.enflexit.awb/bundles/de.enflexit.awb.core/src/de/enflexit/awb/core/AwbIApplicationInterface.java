package de.enflexit.awb.core;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * The Interface AwbIApplicationInterface.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbIApplicationInterface extends IApplication {

	/**
	 * Returns the current IApplicationContext.
	 * @return the i application context
	 */
	public IApplicationContext getIApplicationContext();
	
	/**
	 * Has to remind the  the current IApplicationContext..
	 * @param iApplicationContext the new i application context
	 */
	public void setIApplicationContext(IApplicationContext iApplicationContext);
	
	
	/**
	 * Sets that the application is running, which will close the splash screen.
	 */
	public void setApplicationIsRunning();

	/**
	 * Stop the IApplication with a specific return value
	 * @param returnValue The return value
	 */
	public void stop(Integer returnValue);

	
	/**
	 * Starts the end user application that either based on Swing or SWT.
	 * @param postWindowOpenRunnable the post window open runnable
	 * @return the integer
	 */
	public Integer startEndUserApplication(Runnable postWindowOpenRunnable);

	/**
	 * Start swing UI.
	 *
	 * @param postWindowOpenRunnable the post window open runnable
	 * @return the integer
	 * @throws Exception the exception
	 */
	public Integer startMainWindow(Runnable postWindowOpenRunnable) throws Exception;

	/**
	 * Just starts JADE without any further visualization.
	 *
	 * @param arguments the command line arguments for the JADE platform 
	 * @return the integer
	 */
	public Integer startJadeStandalone(String[] arguments);

}