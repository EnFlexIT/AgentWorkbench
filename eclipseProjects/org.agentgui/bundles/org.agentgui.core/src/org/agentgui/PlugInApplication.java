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

import org.agentgui.bundle.evaluation.FilterForAgent;
import org.agentgui.bundle.evaluation.FilterForBaseService;
import org.agentgui.bundle.evaluation.FilterForOntology;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import agentgui.core.application.Application;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;

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
	private ApplicationVisualizationBy visualisationBy = ApplicationVisualizationBy.EclipseFramework;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {

		// --- Evaluate the already loaded bundles ------------------
		this.startBundleEvaluation();
		
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
				
//				E4Application e4application = new E4Application();
//				Object result = e4application.start(context);
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
	
	/**
	 * Starts the bundle evaluation for the .
	 */
	private void startBundleEvaluation() {
		// --- Evaluate the already loaded bundles ------------------
		BundleEvaluator be = BundleEvaluator.getInstance(); 
		be.addBundleClassFilter(new FilterForAgent(), false);
		be.addBundleClassFilter(new FilterForBaseService(), false);
		be.addBundleClassFilter(new FilterForOntology(), false);
		be.evaluateAllBundles();
	}
	
}
