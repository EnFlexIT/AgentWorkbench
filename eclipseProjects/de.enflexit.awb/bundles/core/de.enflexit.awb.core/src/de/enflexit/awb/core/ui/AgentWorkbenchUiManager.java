package de.enflexit.awb.core.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.ProjectsLoaded.ProjectAction;
import de.enflexit.awb.core.project.transfer.ProjectExportController;
import de.enflexit.awb.simulation.agents.LoadMeasureAgent;
import de.enflexit.common.ServiceFinder;
import de.enflexit.language.Language;

/**
 * The Class AgentWorkbenchUiManager.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AgentWorkbenchUiManager implements AgentWorkbenchUI, SwingToSwtConnectorService {

	private static Logger LOGGER = LoggerFactory.getLogger(AgentWorkbenchUiManager.class);
	
	private SwingToSwtConnectorService swingToSwtConnectorService;
	
	// ----------------------------------------------------
	// --- The singleton area -----------------------------
	// ----------------------------------------------------
	private static AgentWorkbenchUiManager awbUiManager;
	
	public static AgentWorkbenchUiManager getInstance() {
		if (awbUiManager==null) {
			awbUiManager = new AgentWorkbenchUiManager();
		}
		return awbUiManager;
	}
	/**
	 * Instantiates a new agent workbench ui manager.
	 */
	private AgentWorkbenchUiManager() {
		this.initializeManager();
	}
	// ----------------------------------------------------	
	
	
	// ----------------------------------------------------
	// --- The internal handling area ---------------------
	// ----------------------------------------------------
	private List<AgentWorkbenchUI> uiServices;
	private boolean isMissingAwbUIService;
	
	/**
	 * Initializes this manager.
	 */
	private void initializeManager() {
		
		// --- Do initial UI service checks ---------------
		if (this.getAgentWorkbenchUiList().size()==0) {
			LOGGER.warn(Language.translate("Could not find any service that implements the AgentWorkbench-UI!", Language.EN));
			this.isMissingAwbUIService = true;
			return;
		} else if (this.getAgentWorkbenchUiList().size()>1) {
			LOGGER.warn(Language.translate(uiServices.size() + " services were found that implement the AgentWorkbench-UI!", Language.EN));
		}

		// --- Initialize UI-Services --------------------- 
		for (AgentWorkbenchUI uiService : this.getAgentWorkbenchUiList()) {
			try {
				uiService.initialize();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * Returns the list of Agent.Workbench UI implementations.
	 * @return the agent workbench UI list
	 */
	private List<AgentWorkbenchUI> getAgentWorkbenchUiList() {
		if (uiServices==null) {
			uiServices = ServiceFinder.findServices(AgentWorkbenchUI.class);
		}
		return uiServices;
	}
	
	/**
	 * Returns the number of UI-implementations.
	 * @return the number of ui implementations
	 */
	private int getNumberOfUiImplementations() {
		return this.getAgentWorkbenchUiList().size();
	}
	/**
	 * Checks if is missing UI.
	 * @return true, if is missing UI
	 */
	private boolean isMissingAwbUIServce() {
		return this.isMissingAwbUIService;
	}
	// ----------------------------------------------------
	
	
	// ----------------------------------------------------
	// --- The interface caller ---------------------------
	// ----------------------------------------------------
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getImplementationName()
	 */
	@Override
	public String getImplementationName() {
		
		String implName = "NO UI-IMPLEMANTATION FOUND!";
		if (this.getAgentWorkbenchUiList().size()==1) {
			// --- Single UI implementation ------------------------- 
			implName = this.getAgentWorkbenchUiList().get(0).getImplementationName();
			
		} else if (this.getAgentWorkbenchUiList().size()>1) {
			// --- Concatenate the implementation names -------------
			String implNames = "";
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				implNames += implNames.isBlank()==true ? awbUI.getImplementationName() : ", " + awbUI.getImplementationName();
			}
			implName = "UI-Implementations: " + implNames;
			
		}
		return implName;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#initialize()
	 */
	@Override
	public void initialize() {
		// --- Nothing to do here: Will be done in the local initializeManager() method above ----
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getTrayIcon()
	 */
	@Override
	public AwbTrayIcon getTrayIcon() {
		
		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbTrayIcon> trayIconList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbTrayIcon trayIcon = awbUI.getTrayIcon();
				if (trayIcon!=null) trayIconList.add(trayIcon);
			}
			
			// --- Write Error or return TrayIcon -------------------
			if (trayIconList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbTrayIcon!");
			} else if (trayIconList.size()==1) {
				return trayIconList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbTrayIcon!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getTrayIcon();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getConsole(boolean)
	 */
	@Override
	public AwbConsole getConsole(boolean isForLocalConsoleOutput) {

		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbConsole> consoleList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbConsole console = awbUI.getConsole(isForLocalConsoleOutput);
				if (console!=null) consoleList.add(console);
			}
			
			// --- Write Error or return AwbConsole -----------------
			if (consoleList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbConsole!");
			} else if (consoleList.size()==1) {
				return consoleList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbConsole!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getConsole(isForLocalConsoleOutput);
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getConsoleDialog()
	 */
	@Override
	public AwbConsoleDialog getConsoleDialog() {
		
		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbConsoleDialog> consoleDialogList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbConsoleDialog consoleDialog = awbUI.getConsoleDialog();
				if (consoleDialog!=null) consoleDialogList.add(consoleDialog);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (consoleDialogList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbConsoleDialog!");
			} else if (consoleDialogList.size()==1) {
				return consoleDialogList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbConsoleDialog!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getConsoleDialog();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalAboutDialog()
	 */
	@Override
	public boolean showModalAboutDialog() {

		if (this.isMissingAwbUIServce()==true) return false;
		
		boolean dialogWasShown = false;
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check service instances --------------------------
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				dialogWasShown = dialogWasShown | awbUI.showModalAboutDialog();
			}
			// --- Write error if nothing was shown -----------------
			if (dialogWasShown==false) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbAboutDialog was shown!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).showModalAboutDialog();
		}
		return dialogWasShown;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalOptionsDialog(java.lang.String)
	 */
	@Override
	public boolean showModalOptionsDialog(String categoryToFocus) {

		if (this.isMissingAwbUIServce()==true) return false;
		
		boolean dialogWasShown = false;
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check service instances --------------------------
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				dialogWasShown = dialogWasShown | awbUI.showModalOptionsDialog(categoryToFocus);
			}
			// --- Write error if nothing was shown -----------------
			if (dialogWasShown==false) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbOptionsDialog was shown!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).showModalOptionsDialog(categoryToFocus);
		}
		return dialogWasShown;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalTranslationDialog()
	 */
	@Override
	public boolean showModalTranslationDialog() {

		if (this.isMissingAwbUIServce()==true) return false;
		
		boolean dialogWasShown = false;
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check service instances --------------------------
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				dialogWasShown = dialogWasShown | awbUI.showModalTranslationDialog();
			}
			// --- Write error if nothing was shown -----------------
			if (dialogWasShown==false) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbTranslationDialog was shown!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).showModalTranslationDialog();
		}
		return dialogWasShown;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showModalDatabaseDialog(java.lang.String)
	 */
	@Override
	public boolean showModalDatabaseDialog(String factoryID) {

		if (this.isMissingAwbUIServce()==true) return false;
		
		boolean dialogWasShown = false;
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check service instances --------------------------
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				dialogWasShown = dialogWasShown | awbUI.showModalDatabaseDialog(factoryID);
			}
			// --- Write error if nothing was shown -----------------
			if (dialogWasShown==false) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbTranslationDialog was shown!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).showModalDatabaseDialog(factoryID);
		}
		return dialogWasShown;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProgressMonitor(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AwbProgressMonitor getProgressMonitor(String windowTitle, String headerText, String progressText) {
		
		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbProgressMonitor> pmList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbProgressMonitor pMonitor = awbUI.getProgressMonitor(windowTitle, headerText, progressText);
				if (pMonitor!=null) pmList.add(pMonitor);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (pmList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbProgressMonitor!");
			} else if (pmList.size()==1) {
				return pmList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbProgressMonitor!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getProgressMonitor(windowTitle, headerText, progressText);
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getBenchmarkMonitor()
	 */
	@Override
	public AwbBenchmarkMonitor getBenchmarkMonitor() {

		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbBenchmarkMonitor> bmList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbBenchmarkMonitor bmMonitor = awbUI.getBenchmarkMonitor();
				if (bmMonitor!=null) bmList.add(bmMonitor);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (bmList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbBenchmarkMonitor!");
			} else if (bmList.size()==1) {
				return bmList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbBenchmarkMonitor!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getBenchmarkMonitor();
		}
		return null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getMainWindow()
	 */
	@Override
	public AwbMainWindow<?, ?, ?, ?> getMainWindow() {
		
		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbMainWindow<?, ?, ? ,?>> mainWindowList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbMainWindow<?, ?, ?, ?> mainWindow = awbUI.getMainWindow();
				if (mainWindow!=null) mainWindowList.add(mainWindow);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (mainWindowList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbMainWindow!");
			} else if (mainWindowList.size()==1) {
				return mainWindowList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbMainWindow!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getMainWindow();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProjectInteractionDialog(java.lang.String, de.enflexit.awb.core.project.ProjectsLoaded.ProjectAction)
	 */
	public AwbProjectInteractionDialog getProjectInteractionDialog(String title, ProjectAction action) {
		
		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbProjectInteractionDialog> pidList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbProjectInteractionDialog pIntDialog = awbUI.getProjectInteractionDialog(title, action);
				if (pIntDialog!=null) pidList.add(pIntDialog);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (pidList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbProjectInteractionDialog!");
			} else if (pidList.size()==1) {
				return pidList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbMainWindow!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getProjectInteractionDialog(title, action);
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProjectWindow(de.enflexit.awb.core.project.Project)
	 */
	@Override
	public AwbProjectWindow getProjectWindow(Project project) {
		
		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbProjectWindow> pwList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbProjectWindow projectWindow = awbUI.getProjectWindow(project);
				if (projectWindow!=null) pwList.add(projectWindow);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (pwList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbProjectWindow!");
			} else if (pwList.size()==1) {
				return pwList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbProjectWindow!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getProjectWindow(project);
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#createProjectWindowTab(de.enflexit.awb.core.project.Project, int, java.lang.String, java.lang.String, javax.swing.Icon, java.lang.Object, java.lang.String)
	 */
	@Override
	public AwbProjectWindowTab createProjectWindowTab(Project currProject, int displayType_DEV_or_USER, String tabTitle, String toolTipText, Icon icon, Object displayComponent, String parentsName) {

		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbProjectWindowTab> pwtList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbProjectWindowTab pwt = awbUI.createProjectWindowTab(currProject, displayType_DEV_or_USER, tabTitle, toolTipText, icon, displayComponent, parentsName);
				if (pwt!=null) pwtList.add(pwt);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (pwtList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbProjectWindowTab!");
			} else if (pwtList.size()==1) {
				return pwtList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbProjectWindowTab!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).createProjectWindowTab(currProject, displayType_DEV_or_USER, tabTitle, toolTipText, icon, displayComponent, parentsName);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProjectExportDialog(de.enflexit.awb.core.project.Project, de.enflexit.awb.core.project.transfer.ProjectExportController)
	 */
	@Override
	public AwbProjectExportDialog getProjectExportDialog(Project project, ProjectExportController projectExportController) {

		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbProjectExportDialog> pExList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbProjectExportDialog pIntDialog = awbUI.getProjectExportDialog(project, projectExportController);
				if (pIntDialog!=null) pExList.add(pIntDialog);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (pExList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbProjectExportDialog!");
			} else if (pExList.size()==1) {
				return pExList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbProjectExportDialog!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getProjectExportDialog(project, projectExportController);
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getAwbMonitoringDialogSystemLoad(de.enflexit.awb.simulation.agents.LoadMeasureAgent)
	 */
	@Override
	public AwbMonitoringDialogSystemLoad getAwbMonitoringDialogSystemLoad(LoadMeasureAgent lmAgent) {

		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbMonitoringDialogSystemLoad> pExList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbMonitoringDialogSystemLoad monDialog = awbUI.getAwbMonitoringDialogSystemLoad(lmAgent);
				if (monDialog!=null) pExList.add(monDialog);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (pExList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbMonitoringDialogSystemLoad!");
			} else if (pExList.size()==1) {
				return pExList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbMonitoringDialogSystemLoad!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getAwbMonitoringDialogSystemLoad(lmAgent);
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getAwbMonitoringDialogThreading(de.enflexit.awb.simulation.agents.LoadMeasureAgent)
	 */
	@Override
	public AwbMonitoringDialogThreading getAwbMonitoringDialogThreading(LoadMeasureAgent lmAgent) {

		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check instances ----------------------------------
			List<AwbMonitoringDialogThreading> pExList = new ArrayList<>();
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				AwbMonitoringDialogThreading threadMonDialog = awbUI.getAwbMonitoringDialogThreading(lmAgent);
				if (threadMonDialog!=null) pExList.add(threadMonDialog);
			}
			
			// --- Write Error or return AwbConsoleDialog -----------
			if (pExList.size()==0) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no AwbMonitoringDialogThreading!");
			} else if (pExList.size()==1) {
				return pExList.get(0);
			} else {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbMonitoringDialogThreading!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getAwbMonitoringDialogThreading(lmAgent);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getPasswordFromAwbPasswordDialog(boolean, java.lang.String, java.lang.String)
	 */
	public char[] getPasswordFromAwbPasswordDialog(boolean isConfirmPassword, String windowTitle, String headerText) {

		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check service instances --------------------------
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				char[] pswdArray = awbUI.getPasswordFromAwbPasswordDialog(isConfirmPassword, windowTitle, headerText);
				if (pswdArray!=null) {
					if (pswdArray==NOT_IMPLEMENTED_FOR_CHAR_ARRAY) {
						noOfUiImpls--;
						continue;
					}
					// --- Return the password that was entered -----
					return pswdArray;
				}
			}
			// --- Write error if nothing was shown -----------------
			if (noOfUiImpls>1) {
				LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no implementation for method showInputDialog( ... )!");
			}
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).getPasswordFromAwbPasswordDialog(isConfirmPassword, windowTitle, headerText);
		}
		return null;
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showMessageDialog(java.lang.Object, java.lang.Object, java.lang.String, int, javax.swing.Icon)
	 */
	@Override
	public void showMessageDialog(Object parentComponent, Object message, String title, int messageType, Icon icon) {
		
		if (this.isMissingAwbUIServce()==true) return;
		this.getAgentWorkbenchUiList().forEach(awbUI -> awbUI.showMessageDialog(parentComponent, message, title, messageType, icon));
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showOptionDialog(java.lang.Object, java.lang.Object, java.lang.String, int, int, javax.swing.Icon, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public int showOptionDialog(Object parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
		
		if (this.isMissingAwbUIServce()==true) return AwbMessageDialog.DEFAULT_OPTION;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check service instances --------------------------
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				int singleServiceAnswer = awbUI.showOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue);
				if (singleServiceAnswer!=NOT_IMPLEMENTED_FOR_INT) {
					return singleServiceAnswer;
				}
			}
			// --- Write error if nothing was shown -----------------
			LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no implementation for method showOptionDialog( ... )!");
			
		} else {
			return this.getAgentWorkbenchUiList().get(0).showOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue);
		}
		return AwbMessageDialog.CLOSED_OPTION;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showInputDialog(java.lang.Object, java.lang.Object, java.lang.String, int, javax.swing.Icon, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public Object showInputDialog(Object parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) {

		if (this.isMissingAwbUIServce()==true) return null;
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls > 1) {
			// --- Check service instances --------------------------
			for (AgentWorkbenchUI awbUI : this.getAgentWorkbenchUiList()) {
				Object singleServiceAnswer = awbUI.showInputDialog(parentComponent, message, title, messageType, icon, selectionValues, initialSelectionValue);
				if (singleServiceAnswer!=null) {
					return singleServiceAnswer;
				}
			}
			// --- Write error if nothing was shown -----------------
			LOGGER.error("Found " + noOfUiImpls + " UI-implementations but no implementation for method showInputDialog( ... )!");

		} else {
			return this.getAgentWorkbenchUiList().get(0).showInputDialog(parentComponent, message, title, messageType, icon, selectionValues, initialSelectionValue);
		}
		return null;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here the methods for the SwingToSwtConnectorService -----------
	// ------------------------------------------------------------------------	
	/**
	 * Returns the currently available SwingToSwtConnectorService (provided by the *.desktop.swt bundle).
	 * @return the SwingToSwtConnectorService
	 */
	private SwingToSwtConnectorService getSwingToSwtConnectorService() {
		if (swingToSwtConnectorService==null) {
			List<SwingToSwtConnectorService> serviceList = ServiceFinder.findServices(SwingToSwtConnectorService.class);
			if (serviceList!=null && serviceList.size()>0) {
				swingToSwtConnectorService = serviceList.get(0);
			}
		}
		return swingToSwtConnectorService;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipseWorkbench()
	 */
	@Override
	public void showEclipseWorkbench() {
		if (this.getSwingToSwtConnectorService()==null) return;
		this.getSwingToSwtConnectorService().showEclipseWorkbench();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#disposeEclipseWorkbench()
	 */
	@Override
	public void disposeEclipseWorkbench() {
		if (this.getSwingToSwtConnectorService()==null) return;
		this.getSwingToSwtConnectorService().disposeEclipseWorkbench();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipsePreferences()
	 */
	@Override
	public void showEclipsePreferences() {
		if (this.getSwingToSwtConnectorService()==null) return;
		this.getSwingToSwtConnectorService().showEclipsePreferences();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipseAbout()
	 */
	@Override
	public void showEclipseAbout() {
		if (this.getSwingToSwtConnectorService()==null) return;
		this.getSwingToSwtConnectorService().showEclipseAbout();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipseCheckForUpdates()
	 */
	@Override
	public void showEclipseCheckForUpdates() {
		if (this.getSwingToSwtConnectorService()==null) return;
		this.getSwingToSwtConnectorService().showEclipseCheckForUpdates();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipseInstallNewSoftware()
	 */
	@Override
	public void showEclipseInstallNewSoftware() {
		if (this.getSwingToSwtConnectorService()==null) return;
		this.getSwingToSwtConnectorService().showEclipseInstallNewSoftware();	
	}
	
}
