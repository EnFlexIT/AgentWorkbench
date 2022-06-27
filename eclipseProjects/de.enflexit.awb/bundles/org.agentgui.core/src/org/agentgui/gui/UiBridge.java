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
package org.agentgui.gui;

import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

import org.agentgui.PlugInApplication.ApplicationVisualizationBy;
import org.agentgui.gui.AwbProjectNewOpenDialog.ProjectAction;
import org.agentgui.gui.swt.AppModelId;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.MainWindow;
import agentgui.core.project.Project;


/**
 * The Class UiBridge is used to provide a switch between SWT and Swing elements of the user interface.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class UiBridge {

	private static UiBridge thisInstance;
	
	/**Instantiates a new UiBridge. */
	private UiBridge() {
	}
	/**
	 * Returns the singleton instance of the UiBridge.
	 * @return single instance of the UiBridge
	 */
	public static UiBridge getInstance() {
		if (thisInstance==null) {
			thisInstance = new UiBridge();
		}
		return thisInstance;
	}

	// --------------------------------------------------------------
	// --- Helper methods to know the visualization platform --------
	// --------------------------------------------------------------
	/**
	 * Returns the current visualization platform.
	 * @return the visualization platform
	 */
	public ApplicationVisualizationBy getVisualisationPlatform() {
		return Application.getIApplication().getVisualisationPlatform();
	}
	
	// --------------------------------------------------------------
	// --- Helper methods for accessing the workbench ---------------
	// --------------------------------------------------------------
	/**
	 * Checks if is workbench running.
	 * @return true, if is workbench running
	 */
	public boolean isWorkbenchRunning() {
		return PlatformUI.isWorkbenchRunning();
	}
	/**
	 * Returns the current workbench, if active.
	 * @return the workbench
	 */
	public IWorkbench getWorkbench() {
		if (this.isWorkbenchRunning()==true) {
			return PlatformUI.getWorkbench();
		}
		return null;
	}
	/**
	 * Returns the active workbench window.
	 * @return the active workbench window
	 */
	public IWorkbenchWindow getActiveWorkbenchWindow() {
		IWorkbench iwb = this.getWorkbench();
		if (iwb!=null) {
			return iwb.getActiveWorkbenchWindow();
		}
		return null;
	}
	/**
	 * Returns the active workbench window shell.
	 * @return the active workbench window shell
	 */
	public Shell getActiveWorkbenchWindowShell() {
		IWorkbenchWindow iwbWindow = this.getActiveWorkbenchWindow();
		if (iwbWindow!=null) {
			return iwbWindow.getShell();
		}
		return null;
	}

	// --------------------------------------------------------------
	// --- Helper methods for accessing the swing main window -------
	// --------------------------------------------------------------
	/**
	 * Returns the current swing main window that usually is the {@link MainWindow}.
	 * @return the swing main window
	 */
	private Frame getSwingMainWindow() {
		return Application.getMainWindow();
	}
	
	// --------------------------------------------------------------
	// --- Methods for accessing the required UI element ------------
	// --------------------------------------------------------------
	
	// ------------------------------------------
	// --- General UI elements ------------------
	// ------------------------------------------
	/**
	 * Returns a new instance of a console (Swing or SWT).
	 *
	 * @param isForLocalConsoleOutput the is for local console output
	 * @return the console
	 */
	public AwbConsole getConsole(boolean isForLocalConsoleOutput) {
		AwbConsole console = null;
		switch (this.getVisualisationPlatform()) {
		case EclipseFramework:
			// --- SWT console ------------------
			//TODO
			System.err.println("SWT Console not implemented yet!");
			break;

		case AgentGuiSwing:
			// --- Swing console ----------------
			console = new org.agentgui.gui.swing.logging.JPanelConsole(isForLocalConsoleOutput);
			break;
		}
		if (console!=null) {
			console.setLocalConsole(isForLocalConsoleOutput);
		}
		return console;
	}
	
	/**
	 * Returns a new instance of a console dialog (Swing or SWT).
	 * @return the console dialog
	 */
	public AwbConsoleDialog getConsoleDialog() {
		AwbConsoleDialog consoleDialog = null;
		switch (this.getVisualisationPlatform()) {
		case EclipseFramework:
			// --- SWT console dialog -----------
			//TODO
			System.err.println("SWT Console Dialog not implemented yet!");
			break;

		case AgentGuiSwing:
			// --- Swing console dialog ---------
			consoleDialog = new org.agentgui.gui.swing.logging.JFrame4Consoles();
			break;
		}
		return consoleDialog;
	}
	
	/**
	 * Returns a new instance of a tray icon (Swing or SWT).
	 * @return the tray icon 
	 */
	public AwbTrayIcon getTrayIcon() {
		
		AwbTrayIcon trayIcon = null;
		switch (this.getVisualisationPlatform()) {
		case EclipseFramework:
			// --- SWT tray icon ----------------
			//TODO
			System.err.println("SWT Tray Icon not implemented yet!");
			break;

		case AgentGuiSwing:
			// --- Swing tray icon --------------
			trayIcon = new org.agentgui.gui.swing.systemtray.AgentGUITrayIcon();
			break;
		}
		return trayIcon;
	}
	
	/**
	 * Returns the benchmark monitor (Swing or SWT).
	 * @return the benchmark monitor
	 */
	public AwbBenchmarkMonitor getBenchmarkMonitor() {
		
		AwbBenchmarkMonitor monitor = null;
		switch (this.getVisualisationPlatform()) {
		case EclipseFramework:
			// --- SWT tray icon ----------------
			//TODO
			System.err.println("SWT Benchmark Monitor not implemented yet!");
			break;

		case AgentGuiSwing:
			// --- Swing tray icon --------------
			monitor = new org.agentgui.gui.swing.dialogs.BenchmarkMonitor(Application.getMainWindow());
			break;
		}
		return monitor;
	}
	
	/**
	 * Gets the progress monitor.
	 *
	 * @param windowTitle the window title
	 * @param headerText the header text
	 * @param progressText the progress text
	 * @return the progress monitor
	 */
	public AwbProgressMonitor getProgressMonitor(String windowTitle, String headerText, String progressText) {
		AwbProgressMonitor progressMonitor = null;
		switch (this.getVisualisationPlatform()) {
		case EclipseFramework:
			// --- SWT progress visualization dialog ----------------
			//TODO
			System.err.println("SWT Progress Monitor not implemented yet!");
			break;
	
		case AgentGuiSwing:
			// --- Swing progress visualization dialog --------------------
			
			// --- Try to get a JDesktopPane ----------------------------
			JDesktopPane desktop = null;
			if (Application.getMainWindow() != null) {
				desktop = Application.getMainWindow().getJDesktopPane4Projects();
			}
	
			// --- Get the image icon for the progress monitor ----------
			ImageIcon imageIcon = GlobalInfo.getInternalImageIconAwbIcon16();
			// --- Get the look and feel --------------------------------
			String lookAndFeelClassName = Application.getGlobalInfo().getAppLookAndFeelClassName();
			progressMonitor = new org.agentgui.gui.swing.dialogs.ProgressMonitor(windowTitle, headerText, progressText, imageIcon, desktop, lookAndFeelClassName);
			break;
		}
		
		return progressMonitor;
		
	}
	// ------------------------------------------
	// --- Project UI-Elements ------------------
	// ------------------------------------------
	/**
	 * Returns the project new open dialog (Swing or SWT).
	 *
	 * @param title the title
	 * @param currentAction the current action
	 * @return the project new open dialog
	 */
	public AwbProjectNewOpenDialog getProjectNewOpenDialog(String title, ProjectAction currentAction ) {
		AwbProjectNewOpenDialog projectDialog = null;
		switch (this.getVisualisationPlatform()) {
		case EclipseFramework:
			// --- SWT dialog -------------------
			projectDialog = new org.agentgui.gui.swt.dialogs.ProjectNewOpen(this.getActiveWorkbenchWindowShell(), title, currentAction);
			break;

		case AgentGuiSwing:
			// --- Swing dialog -----------------
			projectDialog = new org.agentgui.gui.swing.dialogs.ProjectNewOpen(this.getSwingMainWindow(), title, currentAction);
			break;
		}
		return projectDialog;
	}
	
	/**
	 * Returns the project window (Swing or SWT).
	 *
	 * @param project the project
	 * @return the project window
	 */
	public AwbProjectEditorWindow getProjectEditorWindow(Project project) {
		
		AwbProjectEditorWindow projectEditorWindow = null;
		switch (this.getVisualisationPlatform()) {
		case EclipseFramework:
			// --- SWT editor -------------------
			MPartStack editorStack = (MPartStack) project.getEclipseEModelService().find(AppModelId.PARTSTACK_ORG_AGENTGUI_CORE_PARTSTACK_EDITOR, project.getEclipseMApplication());
			MPart editorPart = project.getEclipseEPartService().createPart(AppModelId.PARTDESCRIPTOR_ORG_AGENTGUI_CORE_PARTDESCRIPTOR_AGENTPROJECT);
			if (editorPart!=null) {
				editorPart.getTransientData().put(Project.class.getName(), project);
				editorPart.setVisible(true);
				editorStack.getChildren().add(editorPart);
				project.getEclipseEPartService().showPart(editorPart, PartState.VISIBLE);

				projectEditorWindow = (AwbProjectEditorWindow) editorPart.getObject();
			}
			break;
			
		case AgentGuiSwing:
			// --- Swing editor -----------------
			projectEditorWindow = new org.agentgui.gui.swing.project.ProjectWindow(project);
			break;
		}
		return projectEditorWindow;
	}
}
