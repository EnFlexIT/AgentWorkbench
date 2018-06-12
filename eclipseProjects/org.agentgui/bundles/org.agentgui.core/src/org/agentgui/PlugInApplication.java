/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package org.agentgui;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.MainWindow;
import de.enflexit.common.SystemEnvironmentHelper;

/**
 * This class controls all aspects of the application's execution
 * and therefore also the different execution modes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PlugInApplication implements IApplication {

	/**
	 * The Enumeration ApplicationVisualizationBy.
	 */
	public enum ApplicationVisualizationBy {
		AgentGuiSwing,
		EclipseFramework
	}
	
	/** Set this variable to switch the visualization */
	private final ApplicationVisualizationBy visualisationBy = ApplicationVisualizationBy.AgentGuiSwing;
	
	private IApplicationContext iApplicationContext;
	private Integer appReturnValue = IApplication.EXIT_OK;
	
	/** This will hold the instance of the Swing application window */
	private MainWindow mainWindowSwing;
	
	
	/**
	 * Returns the visualization platform that is either swing or the Eclipse UI.
	 * @return the visualization by
	 */
	public ApplicationVisualizationBy getVisualisationPlatform() {
		return visualisationBy;
	}
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
		this.iApplicationContext.applicationRunning();	
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

		// --- In case of Swing visualization (for MAC) ---
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
							if (display!=null) {
								if (display.getActiveShell()!=null) {
									display.getActiveShell().setMinimized(true);
								} else {
									for (int i = 0; i < display.getShells().length; i++) {
										display.getShells()[i].setMinimized(true);
									} 
								}
							}
							// --- Do regular start -------
							Application.start(PlugInApplication.this);
						}
					});
				}
			});
			
		} else {
			// ++++++++++++++++++++++++++++++++++++++++++++
			// +++ All other cases ++++++++++++++++++++++++
			// ++++++++++++++++++++++++++++++++++++++++++++
			// --- Start the application ------------------
			Application.start(this);
			
			// --- Wait for termination of application ----
			this.waitForApplicationTermination();
			
		}

		// --- Stop the Application class -----------------
		System.out.println(Language.translate("Programmende... "));
		this.stop();

		return this.appReturnValue;
	}
	
	/**
	 * Checks if is the special MAC start is required.
	 * @return true, if is special start on mac
	 */
	private boolean isSpecialStartOnMac() {
		boolean isMac = SystemEnvironmentHelper.isMacOperatingSystem();
		boolean isSwingVisualiszation = this.getVisualisationPlatform()==ApplicationVisualizationBy.AgentGuiSwing;
		return isMac & isSwingVisualiszation;
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
		
		if (this.getVisualisationPlatform()==ApplicationVisualizationBy.AgentGuiSwing) {
			// --- Check for open projects ------
			if (Application.stopAgentWorkbench()==false) return;
			// --- Stop LogFileWriter -----------
			Application.setLogFileWriter(null);
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
			switch (this.visualisationBy) {
			case AgentGuiSwing:
				// --- Visualization by Agent.GUI/Swing ---
				this.appReturnValue = this.startSwingMainWindow(postWindowOpenRunnable);
				break;
				
			case EclipseFramework:
				// --- Visualization by Eclipse -----------
				this.appReturnValue = this.startEclipseUI(postWindowOpenRunnable);
				break;
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
			if (this.getVisualisationPlatform()==ApplicationVisualizationBy.EclipseFramework || eclipseReturnValue==IApplication.EXIT_RESTART) {
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
			this.setSwingMainWindow(new MainWindow());
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

	/**
	 * Gets the main window.
	 * @return the main window
	 */
	public MainWindow getSwingMainWindow() {
		return this.mainWindowSwing;
	}
	/**
	 * Sets the main window.
	 * @param newMainWindow the new main window
	 */
	public void setSwingMainWindow(MainWindow newMainWindow) {
		if (mainWindowSwing!=null && newMainWindow==null) {
			mainWindowSwing.dispose();
		}
		mainWindowSwing = newMainWindow;	
	}
	
	
}
