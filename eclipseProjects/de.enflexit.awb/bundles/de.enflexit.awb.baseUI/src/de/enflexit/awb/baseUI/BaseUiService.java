package de.enflexit.awb.baseUI;

import java.awt.Component;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import de.enflexit.awb.baseUI.console.JFrame4Consoles;
import de.enflexit.awb.baseUI.console.JPanelConsole;
import de.enflexit.awb.baseUI.dialogs.AboutDialog;
import de.enflexit.awb.baseUI.dialogs.BenchmarkMonitor;
import de.enflexit.awb.baseUI.dialogs.ProgressMonitor;
import de.enflexit.awb.baseUI.dialogs.TranslationDialog;
import de.enflexit.awb.baseUI.monitor.load.SystemLoadDialog;
import de.enflexit.awb.baseUI.monitor.threading.ThreadMonitorDialog;
import de.enflexit.awb.baseUI.options.OptionDialog;
import de.enflexit.awb.baseUI.systemtray.AwbTrayIcon;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.ProjectsLoaded.ProjectAction;
import de.enflexit.awb.core.project.transfer.ProjectExportController;
import de.enflexit.awb.core.ui.AgentWorkbenchUI;
import de.enflexit.awb.core.ui.AwbBenchmarkMonitor;
import de.enflexit.awb.core.ui.AwbConsole;
import de.enflexit.awb.core.ui.AwbConsoleDialog;
import de.enflexit.awb.core.ui.AwbDatabaseDialog;
import de.enflexit.awb.core.ui.AwbMainWindow;
import de.enflexit.awb.core.ui.AwbMainWindowProjectDesktop;
import de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad;
import de.enflexit.awb.core.ui.AwbMonitoringDialogThreading;
import de.enflexit.awb.core.ui.AwbProgressMonitor;
import de.enflexit.awb.core.ui.AwbProjectExportDialog;
import de.enflexit.awb.core.ui.AwbProjectInteractionDialog;
import de.enflexit.awb.core.ui.AwbProjectWindow;
import de.enflexit.awb.core.ui.AwbProjectWindowTab;
import de.enflexit.awb.simulation.agents.LoadMeasureAgent;

/**
 * The Class BaseUiService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BaseUiService implements AgentWorkbenchUI {

	private AboutDialog aboutDialog;
	private OptionDialog optionDialog;
	private TranslationDialog translationDialog;
	
	
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
			if (Application.isMainWindowInitiated()==true) {
				aboutDialog = new AboutDialog((Window) Application.getMainWindow());
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
		
		if (Application.isRunningAsServer()==true || Application.isMainWindowInitiated()==false) {
			optionDialog = new OptionDialog(null);
		} else {
			optionDialog = new OptionDialog((Window) Application.getMainWindow());
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
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalTranslationDialog()
	 */
	@Override
	public boolean showModalTranslationDialog() {

		if (translationDialog!=null) {
			if (translationDialog.isVisible()==true) {
				// --- Set focus again ----------
				translationDialog.requestFocus();
				return false;
			} else {
				// --- dispose it first --------- 
				translationDialog.dispose();
				translationDialog = null;
			}
		}
		
		if (Application.isRunningAsServer()==true || Application.isMainWindowInitiated()==false) {
			translationDialog = new TranslationDialog(null);
		} else {
			translationDialog = new TranslationDialog((Window) Application.getMainWindow());
		}
		translationDialog.setVisible(true);
		// - - - - - - - - - - - - - - - - - - - -
		if (translationDialog!=null) {
			translationDialog.dispose();
		}
		translationDialog = null;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getBenchmarkMonitor()
	 */
	@Override
	public AwbBenchmarkMonitor getBenchmarkMonitor() {
		Window owner = null;
		if (Application.isMainWindowInitiated()==true) {
			owner = (Window)Application.getMainWindow();
		}
		return new BenchmarkMonitor(owner);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProgressMonitor(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AwbProgressMonitor getProgressMonitor(String windowTitle, String headerText, String progressText) {

		// --- Try to get a JDesktopPane ----------------------------
		AwbMainWindowProjectDesktop projectDesktop = null;
		if (Application.isMainWindowInitiated()==true) {
			projectDesktop = Application.getMainWindow().getProjectDesktop();
		}

		// --- Get the image icon for the progress monitor ----------
		ImageIcon imageIcon = GlobalInfo.getInternalImageIconAwbIcon16();
		// --- Get the look and feel --------------------------------
		String lookAndFeelClassName = Application.getGlobalInfo().getAppLookAndFeelClassName();
		return new ProgressMonitor(windowTitle, headerText, progressText, imageIcon, projectDesktop, lookAndFeelClassName);
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getMainWindow()
	 */
	@Override
	public AwbMainWindow<?, ?, ?, ?> getMainWindow() {
		// --- Implemented in the desktop bundle ----------
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProjectInteractionDialog(java.lang.String, de.enflexit.awb.core.project.ProjectsLoaded.ProjectAction)
	 */
	@Override
	public AwbProjectInteractionDialog getProjectInteractionDialog(String actionTitel, ProjectAction action) {
		// --- Implemented in the desktop bundle ----------
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProjectWindow(de.enflexit.awb.core.project.Project)
	 */
	@Override
	public AwbProjectWindow getProjectWindow(Project project) {
		// --- Implemented in the desktop bundle ----------
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#createProjectWindowTab(de.enflexit.awb.core.project.Project, int, java.lang.String, java.lang.String, javax.swing.Icon, java.lang.Object, java.lang.String)
	 */
	@Override
	public AwbProjectWindowTab createProjectWindowTab(Project currProject, int displayType_DEV_or_USER, String tabTitle, String toolTipText, Icon icon, Object displayComponent, String parentsName) {
		// --- Implemented in the desktop bundle ----------
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
