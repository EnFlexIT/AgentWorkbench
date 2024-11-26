package de.enflexit.awb.core.ui;

import java.util.List;

import javax.swing.Icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.common.ServiceFinder;
import de.enflexit.language.Language;

/**
 * The Class AgentWorkbenchUiManager.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AgentWorkbenchUiManager implements AgentWorkbenchUI {

	private static Logger LOGGER = LoggerFactory.getLogger(AgentWorkbenchUiManager.class);
	
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
	
	/**
	 * Initializes this manager.
	 */
	private void initializeManager() {
		
		// --- Do initial UI service checks ---------------
		if (this.getAgentWorkbenchUiList().size()==0) {
			LOGGER.warn(Language.translate("Could not find any service that implements the AgentWorkbench-UI!", Language.EN));
			return;
		} else if (this.getAgentWorkbenchUiList().size()>1) {
			LOGGER.warn(Language.translate(uiServices.size() + " services were found that implement the AgentWorkbench-UI!", Language.EN));
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
	private boolean isMissingUI() {
		return this.getNumberOfUiImplementations()==0;
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
		
		if (this.isMissingUI()==true) LOGGER.error("Could not find any UI-implementation to initialize!");
		// --- Call to initialize the UI implementation ---  
		this.getAgentWorkbenchUiList().forEach(awbUI -> awbUI.initialize());
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getMainWindow()
	 */
	@Override
	public AwbMainWindow getMainWindow() {
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls==0) {
			LOGGER.error("Could not find any UI-implementation to request!");
		} else if (noOfUiImpls > 1) {
			LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbMainWindow!");
		} else {
			return this.getAgentWorkbenchUiList().get(0).getMainWindow();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getConsole()
	 */
	@Override
	public AwbConsole getConsole() {

		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls==0) {
			LOGGER.error("Could not find any UI-implementation to request!");
		} else if (noOfUiImpls > 1) {
			LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbConsole!");
		} else {
			return this.getAgentWorkbenchUiList().get(0).getConsole();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#getProgressMonitor(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AwbProgressMonitor getProgressMonitor(String windowTitle, String headerText, String progressText) {
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls==0) {
			LOGGER.error("Could not find any UI-implementation to request!");
		} else if (noOfUiImpls > 1) {
			LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously answer the request for an AwbProgressMonitor!");
		} else {
			return this.getAgentWorkbenchUiList().get(0).getProgressMonitor(windowTitle, headerText, progressText);
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showMessageDialog(java.lang.Object, java.lang.Object, java.lang.String, int, javax.swing.Icon)
	 */
	@Override
	public void showMessageDialog(Object parentComponent, Object message, String title, int messageType, Icon icon) {
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls==0) {
			LOGGER.error("Could not find any UI-implementation to request!");
		} 
		this.getAgentWorkbenchUiList().forEach(awbUI -> awbUI.showMessageDialog(parentComponent, message, title, messageType, icon));
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showOptionDialog(java.lang.Object, java.lang.Object, java.lang.String, int, int, javax.swing.Icon, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public int showOptionDialog(Object parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
		
		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls==0) {
			LOGGER.error("Could not find any UI-implementation to request!");
		} else if (noOfUiImpls > 1) {
			LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously show an OptionDialog!");
		} else {
			return this.getAgentWorkbenchUiList().get(0).showOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue);
		}
		return AwbOptionPane.CLOSED_OPTION;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AgentWorkbenchUI#showInputDialog(java.lang.Object, java.lang.Object, java.lang.String, int, javax.swing.Icon, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public Object showInputDialog(Object parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) {

		int noOfUiImpls = this.getNumberOfUiImplementations();
		if (noOfUiImpls==0) {
			LOGGER.error("Could not find any UI-implementation to request!");
		} else if (noOfUiImpls > 1) {
			LOGGER.error("Found " + noOfUiImpls + " UI-implementations and thus can't unambiguously show an InputDialog!");
		} else {
			return this.getAgentWorkbenchUiList().get(0).showInputDialog(parentComponent, message, title, messageType, icon, selectionValues, initialSelectionValue);
		}
		return null;
	}
	
}
