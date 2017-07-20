package org.agentgui;

import org.agentgui.bundle.evaluation.BundleEvaluator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import agentgui.core.application.Application;

/**
 * This class controls all aspects of the application's execution
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
	private ApplicationVisualizationBy visualisationBy = ApplicationVisualizationBy.AgentGuiSwing;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {

		// --- Evaluate the already loaded bundles ------------------
		BundleEvaluator.getInstance();
		
		// --- Case separation UI -----------------------------------
		Integer startReturnValue = IApplication.EXIT_OK;
		switch (this.visualisationBy) {
		case AgentGuiSwing:
			// ------------------------------------------------------
			// --- Visualization by Agent.GUI/Swing -----------------
			// ------------------------------------------------------
			context.applicationRunning();
			Application.main(Platform.getApplicationArgs());
			Application.getMainWindow().toFront();
			Application.setDoSystemExitOnQuit(false);
			// --- Wait for the end of the Swing application --------
			while (Application.isQuitJVM()==false) {
				Thread.sleep(250);
			}
			break;
			
		case EclipseFramework:
			// ------------------------------------------------------
			// --- Visualization by Eclipse -------------------------
			// ------------------------------------------------------
			Display display = PlatformUI.createDisplay();
			try {
				// --- Returns if visualization was closed ---------- 
				int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
				if (returnCode == PlatformUI.RETURN_RESTART) {
					startReturnValue = IApplication.EXIT_RESTART;
				} else {
					startReturnValue = IApplication.EXIT_OK;
				}
			} finally {
				display.dispose();
			}
			break;
		}
		return startReturnValue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		
		if (!PlatformUI.isWorkbenchRunning()) return;
		
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}
	
}
