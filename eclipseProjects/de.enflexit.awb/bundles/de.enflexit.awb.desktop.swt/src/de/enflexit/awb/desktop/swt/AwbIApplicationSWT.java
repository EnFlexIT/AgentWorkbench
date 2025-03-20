package de.enflexit.awb.desktop.swt;

import javax.swing.SwingUtilities;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import de.enflexit.language.Language;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.AwbIApplication;
import de.enflexit.common.SystemEnvironmentHelper;

/**
 * This class controls all aspects of the application's execution
 * and therefore also the different execution modes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AwbIApplicationSWT extends AwbIApplication {

	private final boolean isSwingVisualisation = false;
	private Integer appReturnValue = IApplication.EXIT_OK;
	
	
	/**
	 * Checks if is the special MAC start is required.
	 * @return true, if is special start on mac
	 */
	private boolean isSpecialStartOnMac() {
		boolean isMac = SystemEnvironmentHelper.isMacOperatingSystem();
		return isMac & this.isSwingVisualisation;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {

		// --- Remind application context -----------------
		this.setIApplicationContext(context);

		if (this.isSpecialStartOnMac()) {
			// ++++++++++++++++++++++++++++++++++++++++++++
			// +++ Just for MAC and Swing +++++++++++++++++
			// ++++++++++++++++++++++++++++++++++++++++++++
			// --- Start Eclipse Workbench first ---------
			this.startEclipseUI(new Runnable() {
				@Override
				public void run() {
					final IWorkbench workbench = PlatformUI.getWorkbench();
					final Display display = workbench.getDisplay();
					display.asyncExec(new Runnable() {
						public void run() {
							// --- Minimize display -------
							AwbIApplicationSWT.this.eclipseUiMminmize(display);
							// --- Do  regular start ------
							AwbIApplicationSWT.this.startApplicationInOwnThread();
							// --- Close the --------------
							AwbIApplicationSWT.this.eclipseUiHide(display);
						}
					});
				}
			});
			
		} else {
			// ++++++++++++++++++++++++++++++++++++++++++++
			// +++ All other cases ++++++++++++++++++++++++
			// ++++++++++++++++++++++++++++++++++++++++++++
			this.startApplication();
			
		}
		
		// --- Wait for termination of application --------
		this.waitForApplicationTermination();

		// --- Stop the Application class -----------------
		System.out.println(Language.translate("Programmende... "));
		this.stop();

		return this.appReturnValue;
	}
	
	/**
	 * Starts the application in own thread.
	 */
	private void startApplicationInOwnThread() {
	
		Thread awbStarter = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// --- Start the application --------------
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							// --- Do AWB start ---------------
							try {
								AwbIApplicationSWT.this.startApplication();
								
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					});
	
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		awbStarter.setName("AWB-Starter");
		awbStarter.start();
	}
	
	/**
	 * Starts the application.
	 * @throws Exception the exception
	 * @see Application#start(AwbIApplicationSWT)
	 */
	private void startApplication() throws Exception {
		Application.start(this);
	}
	
	/**
	 * Minimizes the Eclipse workbench .
	 */
	private void eclipseUiMminmize(Display display) {
		if (display!=null) {
			if (display.getActiveShell()!=null) {
				display.getActiveShell().setMinimized(true);
			} else {
				for (int i = 0; i < display.getShells().length; i++) {
					display.getShells()[i].setMinimized(true);
				} 
			}
		}
	}
	/**
	 * Hides the Eclipse workbench.
	 * @param display the display
	 */
	private void eclipseUiHide(Display display) {
		if (display!=null) {
			if (display.getActiveShell()!=null) {
				display.getActiveShell().setVisible(false);
			} else {
				for (int i = 0; i < display.getShells().length; i++) {
					display.getShells()[i].setVisible(false);
				} 
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		
		if (this.isSwingVisualisation==true) {
			// --- Check for open projects ------
			if (Application.stopAgentWorkbench()==false) return;
			// --- Stop LogFileWriter -----------
			Application.stopLoggingWriter();
			// --- ShutdownExecuter -------------
			Application.setShutdownThread(null);
			// --- Indicate to stop the JVM -----
			Application.setQuitJVM(true);
		}
		this.stopEclipseUI();
		
	}
	/**
	 * Stop Eclipse UI.
	 */
	private void stopEclipseUI() {
		
		if (PlatformUI.isWorkbenchRunning()==false) return;
		
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}
	
	/**
	 * Starts the end user application that either based on Swing or SWT.
	 * @param postWindowOpenRunnable the post window open runnable
	 * @return the integer
	 */
	public Integer startEndUserApplication(Runnable postWindowOpenRunnable) {
		
		try {
			// --- Case separation UI ---------------------
			if (this.isSwingVisualisation==true) { 
				// --- Visualization by Agent.GUI/Swing ---
				this.appReturnValue = this.startSwingMainWindow(postWindowOpenRunnable);
			
			} else {
				// --- Visualization by Eclipse -----------
				this.appReturnValue = this.startEclipseUI(postWindowOpenRunnable);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.appReturnValue;
	}
	
	/**
	 * Starts the eclipse UI by using a separate thread.
	 * @param postWindowOpenRunnable the post window open runnable
	 */
	public void startEclipseUiThroughThread(final Runnable postWindowOpenRunnable) {

		Thread eclipsStarter = new Thread(new Runnable() {
			@Override
			public void run() {
				startEclipseUI(postWindowOpenRunnable);
			}
		});
		eclipsStarter.setName("EclipseUI-Starter");
		eclipsStarter.start();
	}
	
	/**
	 * Starts the eclipse UI.
	 * @param postWindowOpenRunnable the post window open runnable
	 * @return the integer
	 */
	public Integer startEclipseUI(Runnable postWindowOpenRunnable) {
		
		Integer eclipseReturnValue = IApplication.EXIT_OK;
		Display display = PlatformUI.createDisplay();
		try {
			// --- Returns if visualization was closed ---- 
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor(postWindowOpenRunnable));
			if (returnCode == PlatformUI.RETURN_RESTART) {
				eclipseReturnValue = IApplication.EXIT_RESTART;
			} else {
				eclipseReturnValue = IApplication.EXIT_OK;
			}
			
		} finally {
			display.dispose();
			// --- Just in case of the Eclipse UI ---------
			// --- usage or after an update + restart -----
			if (this.isSwingVisualisation==false || eclipseReturnValue==IApplication.EXIT_RESTART) {
				appReturnValue = eclipseReturnValue;
				Application.setQuitJVM(true);
			}
		}		
		return eclipseReturnValue;
	}
	
	/**
	 * Start swing UI.
	 *
	 * @param postWindowOpenRunnable the post window open runnable
	 * @return the integer
	 * @throws Exception the exception
	 */
	public Integer startSwingMainWindow(Runnable postWindowOpenRunnable) throws Exception {
		
		Integer appReturnValue = IApplication.EXIT_OK;
		if (Application.isOperatingHeadless()==false) {
			Application.getMainWindow();
			Application.getProjectsLoaded().setProjectView();
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
