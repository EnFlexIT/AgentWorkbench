package de.enflexit.awb.desktop;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.ProjectsLoaded.ProjectAction;
import de.enflexit.awb.core.project.transfer.ProjectExportController;
import de.enflexit.awb.core.ui.AgentWorkbenchUI;
import de.enflexit.awb.core.ui.AwbAboutDialog;
import de.enflexit.awb.core.ui.AwbBenchmarkMonitor;
import de.enflexit.awb.core.ui.AwbConsole;
import de.enflexit.awb.core.ui.AwbConsoleDialog;
import de.enflexit.awb.core.ui.AwbDatabaseDialog;
import de.enflexit.awb.core.ui.AwbMainWindow;
import de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad;
import de.enflexit.awb.core.ui.AwbMonitoringDialogThreading;
import de.enflexit.awb.core.ui.AwbOptionsDialog;
import de.enflexit.awb.core.ui.AwbProgressMonitor;
import de.enflexit.awb.core.ui.AwbProjectExportDialog;
import de.enflexit.awb.core.ui.AwbProjectInteractionDialog;
import de.enflexit.awb.core.ui.AwbProjectWindow;
import de.enflexit.awb.core.ui.AwbProjectWindowTab;
import de.enflexit.awb.core.ui.AwbTranslationDialog;
import de.enflexit.awb.core.ui.AwbTrayIcon;
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

	@Override
	public void initialize() {
		LOGGER.info("Initializing " + this.getImplementationName());
	}

	@Override
	public AwbTrayIcon getTrayIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbMainWindow<?, ?, ?, ?> getMainWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbConsole getConsole(boolean isForLocalConsoleOutput) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbConsoleDialog getConsoleDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbProjectInteractionDialog getProjectInteractionDialog(String actionTitel, ProjectAction action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbProjectWindow getProjectWindow(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbProjectWindowTab createProjectWindowTab(Project currProject, int displayType_DEV_or_USER, String tabTitle, String toolTipText, Icon icon, Object displayComponent, String parentsName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbProgressMonitor getProgressMonitor(String windowTitle, String headerText, String progressText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbBenchmarkMonitor getBenchmarkMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbAboutDialog showModalAboutDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbOptionsDialog showModalOptionsDialog(String categoryToFocus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AwbTranslationDialog showModalTranslationDialog() {
		// TODO Auto-generated method stub
		return null;
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
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showInputDialog(java.lang.Object, java.lang.Object, java.lang.String, int, javax.swing.Icon, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public Object showInputDialog(Object parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) {
		return JOptionPane.showInputDialog((Component)parentComponent, message, title, messageType);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showMessageDialog(java.lang.Object, java.lang.Object, java.lang.String, int, javax.swing.Icon)
	 */
	@Override
	public void showMessageDialog(Object parentComponent, Object message, String title, int messageType, Icon icon) {
		JOptionPane.showMessageDialog((Component)parentComponent, message, title, messageType, icon);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showOptionDialog(java.lang.Object, java.lang.Object, java.lang.String, int, int, javax.swing.Icon, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public int showOptionDialog(Object parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
		return JOptionPane.showOptionDialog((Component)parentComponent, message, title, optionType, messageType, icon, options, initialValue);
	}

}
