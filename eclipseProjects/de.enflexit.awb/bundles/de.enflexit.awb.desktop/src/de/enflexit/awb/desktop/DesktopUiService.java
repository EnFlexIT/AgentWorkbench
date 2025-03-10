package de.enflexit.awb.desktop;

import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JComponent;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.ProjectsLoaded.ProjectAction;
import de.enflexit.awb.core.project.transfer.ProjectExportController;
import de.enflexit.awb.core.ui.AgentWorkbenchUI;
import de.enflexit.awb.core.ui.AwbBenchmarkMonitor;
import de.enflexit.awb.core.ui.AwbConsole;
import de.enflexit.awb.core.ui.AwbConsoleDialog;
import de.enflexit.awb.core.ui.AwbDatabaseDialog;
import de.enflexit.awb.core.ui.AwbMainWindow;
import de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad;
import de.enflexit.awb.core.ui.AwbMonitoringDialogThreading;
import de.enflexit.awb.core.ui.AwbProgressMonitor;
import de.enflexit.awb.core.ui.AwbProjectExportDialog;
import de.enflexit.awb.core.ui.AwbProjectInteractionDialog;
import de.enflexit.awb.core.ui.AwbProjectWindow;
import de.enflexit.awb.core.ui.AwbProjectWindowTab;
import de.enflexit.awb.core.ui.AwbTrayIcon;
import de.enflexit.awb.desktop.dialogs.ProjectNewOpen;
import de.enflexit.awb.desktop.mainWindow.MainWindow;
import de.enflexit.awb.desktop.project.ProjectWindow;
import de.enflexit.awb.desktop.project.ProjectWindowTab;
import de.enflexit.awb.simulation.agents.LoadMeasureAgent;

/**
 * The Class DesktopUiService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DesktopUiService implements AgentWorkbenchUI {

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getImplementationName()
	 */
	@Override
	public String getImplementationName() {
		return "Desktop UI";
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#initialize()
	 */
	@Override
	public void initialize() {
		LOGGER.info("Initializing " + this.getImplementationName());
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getTrayIcon()
	 */
	@Override
	public AwbTrayIcon getTrayIcon() {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getConsole(boolean)
	 */
	@Override
	public AwbConsole getConsole(boolean isForLocalConsoleOutput) {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getConsoleDialog()
	 */
	@Override
	public AwbConsoleDialog getConsoleDialog() {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalAboutDialog()
	 */
	@Override
	public boolean showModalAboutDialog() {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalOptionsDialog(java.lang.String)
	 */
	@Override
	public boolean showModalOptionsDialog(String categoryToFocus) {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalTranslationDialog()
	 */
	@Override
	public boolean showModalTranslationDialog() {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return false;
	}
	
	@Override
	public AwbBenchmarkMonitor getBenchmarkMonitor() {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProgressMonitor(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AwbProgressMonitor getProgressMonitor(String windowTitle, String headerText, String progressText) {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getMainWindow()
	 */
	@Override
	public AwbMainWindow<?, ?, ?, ?> getMainWindow() {
		return new MainWindow();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProjectInteractionDialog(java.lang.String, de.enflexit.awb.core.project.ProjectsLoaded.ProjectAction)
	 */
	@Override
	public AwbProjectInteractionDialog getProjectInteractionDialog(String actionTitel, ProjectAction action) {

		ProjectNewOpen pno = null;
		if (Application.isMainWindowInitiated()==true && Application.getMainWindow() instanceof Window) {
			pno = new ProjectNewOpen((Window)Application.getMainWindow(), actionTitel, action);
		} else {
			pno = new ProjectNewOpen(null, actionTitel, action);
		}
		return pno;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProjectWindow(de.enflexit.awb.core.project.Project)
	 */
	@Override
	public AwbProjectWindow getProjectWindow(Project project) {
		return new ProjectWindow(project);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#createProjectWindowTab(de.enflexit.awb.core.project.Project, int, java.lang.String, java.lang.String, javax.swing.Icon, java.lang.Object, java.lang.String)
	 */
	@Override
	public AwbProjectWindowTab createProjectWindowTab(Project currProject, int displayType_DEV_or_USER, String tabTitle, String toolTipText, Icon icon, Object displayComponent, String parentsName) {
		return new ProjectWindowTab(currProject, displayType_DEV_or_USER, tabTitle, toolTipText, icon, (JComponent) displayComponent, parentsName);
	}


	
	@Override
	public AwbDatabaseDialog showModalDatabaseDialog(String factoryID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbProjectExportDialog showModalProjectExportDialog(Project project, ProjectExportController projectExportController) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbMonitoringDialogSystemLoad getAwbMonitoringDialogSystemLoad(LoadMeasureAgent lmAgent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AwbMonitoringDialogThreading getAwbMonitoringDialogThreading(LoadMeasureAgent lmAgent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showMessageDialog(java.lang.Object, java.lang.Object, java.lang.String, int, javax.swing.Icon)
	 */
	@Override
	public void showMessageDialog(Object parentComponent, Object message, String title, int messageType, Icon icon) {
		// --- Nothing to do here, since BaseUiService will create the component ----
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showOptionDialog(java.lang.Object, java.lang.Object, java.lang.String, int, int, javax.swing.Icon, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public int showOptionDialog(Object parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return NOT_IMPLEMENTED;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showInputDialog(java.lang.Object, java.lang.Object, java.lang.String, int, javax.swing.Icon, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public Object showInputDialog(Object parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) {
		// --- Nothing to do here, since BaseUiService will create the component ----
		return null;
	}

}
