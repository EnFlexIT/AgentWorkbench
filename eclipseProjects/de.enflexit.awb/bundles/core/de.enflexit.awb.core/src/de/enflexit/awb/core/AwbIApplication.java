package de.enflexit.awb.core;

import javax.swing.SwingUtilities;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.config.GlobalInfo.AWBProduct;
import de.enflexit.awb.core.ui.AgentWorkbenchUiManager;
import de.enflexit.common.SystemEnvironmentHelper;
import de.enflexit.language.Language;

/**
 * This class controls all aspects of the application's execution
 * and therefore also the different execution modes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AwbIApplication implements AwbIApplicationInterface {
	
	/**
	 * The Enumeration ApplicationVisualizationBy.
	 */
	public enum ApplicationVisualizationBy {
		AgentWorkbenchSwing,
		EclipseFramework
	}
	
	/** Set this variable to switch the visualization */
	private final ApplicationVisualizationBy visualisationBy = ApplicationVisualizationBy.AgentWorkbenchSwing;

	private IApplicationContext iApplicationContext;
	private Integer appReturnValue = IApplication.EXIT_OK;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#getIApplicationContext()
	 */
	@Override
	public IApplicationContext getIApplicationContext() {
		return this.iApplicationContext;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#setIApplicationContext(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public void setIApplicationContext(IApplicationContext iApplicationContext) {
		this.iApplicationContext = iApplicationContext;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#getApplicationReturnValue()
	 */
	@Override
	public Integer getApplicationReturnValue() {
		return appReturnValue;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#setAppReturnValue(java.lang.Integer)
	 */
	@Override
	public void setApplicationReturnValue(Integer appReturnValue) {
		this.appReturnValue = appReturnValue;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#setApplicationIsRunning()
	 */
	@Override
	public void setApplicationIsRunning() {
		this.getIApplicationContext().applicationRunning();	
	}
	
	/**
	 * Waits for the termination of the application.
	 */
	protected void waitForApplicationTermination() throws Exception {
		// --- Wait for termination of the application ----
		while (Application.isQuitJVM()==false) {
			Thread.sleep(250);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#getAwbProduct()
	 */
	@Override
	public AWBProduct getAwbProduct() {
		return AWBProduct.DESKTOP_SWING;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {

		// --- Set the product indicator ------------------
		GlobalInfo.catchProduct(this.getAwbProduct());
		
		// --- Remind application context -----------------
		this.setIApplicationContext(context);
		
		if (this.isSpecialStartOnMac()==true) {
			// --- Special start mechanism for Mac OS -----
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final Display display = workbench.getDisplay();
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					// --- Minimize display -------
					AwbIApplication.this.eclipseUiMinimize(display);
					// --- Do  regular start ------
					AwbIApplication.this.startApplicationInOwnThread();
					// --- Close the --------------
					AwbIApplication.this.eclipseUiHide(display);
				}
			});
		} else {
			// --- Regular start for Windows and Linux ----
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
	 * Starts the application.
	 * @throws Exception the exception
	 * @see Application#start(AwbIApplication)
	 */
	private void startApplication() throws Exception {
		Application.start(this);
	}
	
	/**
	 * Starts the application in a separate thread.
	 */
	private void startApplicationInOwnThread() {
		Thread awbStarter = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							try {
								AwbIApplication.this.startApplication();
							} catch(Exception ex) {
								ex.printStackTrace();
							}
						}
					});
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		awbStarter.setName("AWB-Starter");
		awbStarter.start();
	}
	
	/**
	 * Minimizes the Eclipse workbench .
	 * @param display the current display
	 */
	private void eclipseUiMinimize(Display display) {
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
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#stop(java.lang.Integer)
	 */
	@Override
	public void stop(Integer returnValue) {
		this.appReturnValue = returnValue;
		this.stop();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#stop()
	 */
	@Override
	public void stop() {

		// --- Check for open projects ------
		if (Application.stopAgentWorkbench()==false) return;
		// --- Stop Eclipse workbench -------
		AgentWorkbenchUiManager.getInstance().disposeEclipseWorkbench();
		// --- Stop LogFileWriter -----------
		Application.stopLoggingWriter();
		// --- ShutdownExecuter -------------
		Application.setShutdownThread(null);
		// --- Indicate to stop the JVM -----
		Application.setQuitJVM(true);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#startEndUserApplication(java.lang.Runnable)
	 */
	@Override
	public Integer startEndUserApplication(Runnable postWindowOpenRunnable) {
		
		try {
			this.appReturnValue = this.startMainWindow(postWindowOpenRunnable);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.appReturnValue;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#startMainWindow(java.lang.Runnable)
	 */
	@Override
	public Integer startMainWindow(Runnable postWindowOpenRunnable) throws Exception {
		
		Integer appReturnValue = IApplication.EXIT_OK;
		if (Application.isOperatingHeadless()==false) {
			// --- Start UI -------------------------------
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
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#startJadeStandalone(java.lang.String[])
	 */
	@Override
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
	
	/**
	 * Checks if is the special MAC start is required.
	 * @return true, if is special start on mac
	 */
	private boolean isSpecialStartOnMac() {
		boolean isMac = SystemEnvironmentHelper.isMacOperatingSystem();
		boolean isSwingVisualiszation = this.getVisualisationPlatform()==ApplicationVisualizationBy.AgentWorkbenchSwing;
		return isMac & isSwingVisualiszation;
	}
	
	/**
	 * Returns the visualization platform that is either swing or the Eclipse UI.
	 * @return the visualization by
	 */
	public ApplicationVisualizationBy getVisualisationPlatform() {
		return visualisationBy;
	}
	
}
