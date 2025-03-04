package de.enflexit.awb.baseUI;

import java.awt.Component;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import de.enflexit.awb.baseUI.console.JFrame4Consoles;
import de.enflexit.awb.baseUI.console.JPanelConsole;
import de.enflexit.awb.baseUI.dialogs.AboutDialog;
import de.enflexit.awb.baseUI.monitor.load.SystemLoadDialog;
import de.enflexit.awb.baseUI.monitor.threading.ThreadMonitorDialog;
import de.enflexit.awb.baseUI.options.OptionDialog;
import de.enflexit.awb.baseUI.systemtray.AwbTrayIcon;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.ProjectsLoaded.ProjectAction;
import de.enflexit.awb.core.project.transfer.ProjectExportController;
import de.enflexit.awb.core.ui.AgentWorkbenchUI;
import de.enflexit.awb.core.ui.AgentWorkbenchUiManager;
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
import de.enflexit.awb.core.ui.AwbTranslationDialog;
import de.enflexit.awb.simulation.agents.LoadMeasureAgent;

/**
 * The Class BaseUiService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BaseUiService implements AgentWorkbenchUI {

	private AboutDialog aboutDialog;
	private OptionDialog optionDialog;
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getImplementationName()
	 */
	@Override
	public String getImplementationName() {
		return "Base UI";
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
		return new AwbTrayIcon();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getConsole(boolean)
	 */
	@Override
	public AwbConsole getConsole(boolean isForLocalConsoleOutput) {
		return new JPanelConsole(isForLocalConsoleOutput);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getConsoleDialog()
	 */
	@Override
	public AwbConsoleDialog getConsoleDialog() {
		return new JFrame4Consoles();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalAboutDialog()
	 */
	@Override
	public boolean showModalAboutDialog() {

		if (aboutDialog!=null) return false;
		
		if (Application.isRunningAsServer()==true) {
			aboutDialog = new AboutDialog(null);
		} else {
			// --- In case a MainWindow is available ... -------
			AwbMainWindow<?, ?, ?, ?> mainWindow = AgentWorkbenchUiManager.getInstance().getMainWindow();
			if (mainWindow instanceof Window) {
				aboutDialog = new AboutDialog((Window) mainWindow);
			} else {
				aboutDialog = new AboutDialog(null);
			}
		}
		aboutDialog.setVisible(true);
		// - - - Wait for user - - - - - - - - -  
		aboutDialog.dispose();
		aboutDialog = null;		
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalOptionsDialog(java.lang.String)
	 */
	@Override
	public boolean showModalOptionsDialog(String categoryToFocus) {

		if (optionDialog!=null) {
			if (optionDialog.isVisible()==true) {
				// --- Set focus again ----------
				optionDialog.requestFocus();
				return false;
			} else {
				// --- dispose it first --------- 
				optionDialog.dispose();
				optionDialog = null;
			}
		}
		
		if (Application.isRunningAsServer()==true) {
			optionDialog = new OptionDialog(null);
		} else {
			optionDialog = new OptionDialog((Window) AgentWorkbenchUiManager.getInstance().getMainWindow());
		}
		if (categoryToFocus!=null) {
			optionDialog.setFocusOnTab(categoryToFocus);
		}
		optionDialog.setVisible(true);
		// - - - - - - - - - - - - - - - - - - - -
		if (optionDialog!=null) {
			optionDialog.dispose();
		}
		optionDialog = null;
		return true;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getMainWindow()
	 */
	@Override
	public AwbMainWindow<?, ?, ?, ?> getMainWindow() {
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

	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getAwbMonitoringDialogSystemLoad(de.enflexit.awb.simulation.agents.LoadMeasureAgent)
	 */
	@Override
	public AwbMonitoringDialogSystemLoad getAwbMonitoringDialogSystemLoad(LoadMeasureAgent lmAgent) {
		return new SystemLoadDialog(lmAgent);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getAwbMonitoringDialogThreading(de.enflexit.awb.simulation.agents.LoadMeasureAgent)
	 */
	@Override
	public AwbMonitoringDialogThreading getAwbMonitoringDialogThreading(LoadMeasureAgent lmAgent) {
		return new ThreadMonitorDialog(lmAgent);
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
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showInputDialog(java.lang.Object, java.lang.Object, java.lang.String, int, javax.swing.Icon, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public Object showInputDialog(Object parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) {
		return JOptionPane.showInputDialog((Component)parentComponent, message, title, messageType);
	}

}
