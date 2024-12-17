package de.enflexit.awb.core;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import de.enflexit.language.Language;

/**
 * This class controls all aspects of the application's execution
 * and therefore also the different execution modes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AwbIApplication implements IApplication {

	private IApplicationContext iApplicationContext;
	private Integer appReturnValue = IApplication.EXIT_OK;
	
	/**
	 * Returns the current IApplicationContext.
	 * @return the i application context
	 */
	public IApplicationContext getIApplicationContext() {
		return this.iApplicationContext;
	}
	/**
	 * Sets the application is running.
	 */
	public void setApplicationIsRunning() {
		this.getIApplicationContext().applicationRunning();	
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
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {

		// --- Remind application context -----------------
		this.iApplicationContext = context;
		
		// --- Start the actual application ---------------
		this.startApplication();
		
		// --- Wait for termination of application --------
		this.waitForApplicationTermination();

		// --- Stop the Application class -----------------
		System.out.println(Language.translate("Programmende... "));
		this.stop();

		return this.appReturnValue;
	}
	
	/**
	 * Starts the application.
	 * @throws Exception the exception
	 * @see Application#start(AwbIApplication)
	 */
	private void startApplication() throws Exception {
		Application.start(this);
	}
	
	/**
	 * Stop the IApplication with a specific return value
	 * @param returnValue The return value
	 */
	public void stop(Integer returnValue) {
		this.appReturnValue = returnValue;
		this.stop();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {

		// --- Check for open projects ------
		if (Application.stopAgentWorkbench()==false) return;
		// --- Stop LogFileWriter -----------
		Application.stopLoggingWriter();
		// --- ShutdownExecuter -------------
		Application.setShutdownThread(null);
		// --- Indicate to stop the JVM -----
		Application.setQuitJVM(true);
	}
	
	/**
	 * Starts the end user application that either based on Swing or SWT.
	 * @param postWindowOpenRunnable the post window open runnable
	 * @return the integer
	 */
	public Integer startEndUserApplication(Runnable postWindowOpenRunnable) {
		
		try {
			this.appReturnValue = this.startMainWindow(postWindowOpenRunnable);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.appReturnValue;
	}
	
	/**
	 * Start swing UI.
	 *
	 * @param postWindowOpenRunnable the post window open runnable
	 * @return the integer
	 * @throws Exception the exception
	 */
	public Integer startMainWindow(Runnable postWindowOpenRunnable) throws Exception {
		
		Integer appReturnValue = IApplication.EXIT_OK;
		if (Application.isOperatingHeadless()==false) {
			// -- TODO Start UI 
//			this.setSwingMainWindow(new MainWindow());
//			Application.getProjectsLoaded().setProjectView();
		}
		
		// --- Execute the post window open runnable ------
		if (postWindowOpenRunnable!=null) {
			Thread postAction = new Thread(postWindowOpenRunnable, "AWB-Swing-PostWindowOpen-Action");
			postAction.setPriority(Thread.MAX_PRIORITY);
			postAction.start();
		}

		// --- Remove splash screen -----------------------
		this.setApplicationIsRunning();
		
		return appReturnValue;
	}
	
	/**
	 * Just starts JADE without any further visualization.
	 *
	 * @param arguments the command line arguments for the JADE platform 
	 * @return the integer
	 */
	public Integer startJadeStandalone(String[] arguments) {
		
		// --- Remove splash screen -----------------------
		this.setApplicationIsRunning();
		
		// --- Boot JADE as from command line ------------- 
		jade.Boot.main(arguments);
		jade.core.Runtime.instance().invokeOnTermination(new Runnable() {
			@Override
			public void run() {
				Application.setQuitJVM(true);
			}
		});
		return IApplication.EXIT_OK;
	}
	
}