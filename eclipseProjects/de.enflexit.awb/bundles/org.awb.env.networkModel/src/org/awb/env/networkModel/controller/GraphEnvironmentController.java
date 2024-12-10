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
package org.awb.env.networkModel.controller;

import java.awt.Cursor;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import org.awb.env.networkModel.ClusterNetworkComponent;
import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler;
import org.awb.env.networkModel.controller.DataModelStorageThread.OrganizerAction;
import org.awb.env.networkModel.controller.ui.BasicGraphGui;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.GraphEnvironmentControllerGUI;
import org.awb.env.networkModel.controller.ui.commands.NetworkModelUndoManager;
import org.awb.env.networkModel.controller.ui.toolbar.CustomToolbarComponentDescription;
import org.awb.env.networkModel.persistence.NetworkModelExportService;
import org.awb.env.networkModel.persistence.NetworkModelImportService;
import org.awb.env.networkModel.persistence.SetupDataModelStorageService;
import org.awb.env.networkModel.persistence.SetupDataModelStorageService.DataModelServiceAction;
import org.awb.env.networkModel.positioning.GraphNodePositionFactory;
import org.awb.env.networkModel.settings.ComponentTypeSettings;
import org.awb.env.networkModel.settings.DomainSettings;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;

import agentgui.core.application.Application;
import de.enflexit.language.Language;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.project.Project;
import agentgui.core.project.setup.AgentClassElement4SimStart;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.simulationService.environment.AbstractEnvironmentModel;
import agentgui.simulationService.environment.DisplaytEnvironmentModel;
import de.enflexit.common.ServiceFinder;
import jade.core.Agent;

/**
 * This class manages an environment model of the type graph / network.<br>
 * Also contains the network component type settings configuration.<br>
 * The observable class of the network model in the observer pattern.
 * 
 * @see org.awb.env.networkModel.NetworkModel
 * @see org.awb.env.networkModel.settings.ComponentTypeSettings
 * @see GraphEnvironmentControllerGUI
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class GraphEnvironmentController extends EnvironmentController {

	/** Custom user object to be placed in the project object. Used here for storing the current component type settings. */
	private static final String GeneralGraphSettings4MASFile = "~GeneralGraphSettings~";

	
	/** The setup name that serves as base for the file name used for saving the graph and the components (without suffix) */
	private String setupName;
	/** Storage handler (services) for individual data models for {@link DataModelNetworkElement} */
	private HashMap<Class<? extends AbstractDataModelStorageHandler>, SetupDataModelStorageService> setupStorageServiceHashMap;
	
	/** The network model currently loaded */
	private NetworkModel networkModel;
	private NetworkModelUndoManager networkModelUndoManager;
	
	/** The controller for user messages */
	private UIMessagingController uiMessagingController;

	/** The NetworkModel that is stored in the clipboard */
	private NetworkModel clipboardNetworkModel;

	/** The abstract environment model is just an open slot, where individual things can be placed. */
	private AbstractEnvironmentModel abstractEnvironmentModel;

	/** The is temporary prevent saving. */
	private boolean isTemporaryPreventSaving;
	
	
	/**
	 * The constructor for the GraphEnvironmentController for displaying the current environment model during 
	 * a running simulation. Use {@link #setDisplayEnvironmentModel(DisplaytEnvironmentModel)}, in order to set 
	 * the current {@link NetworkModel}.
	 */
	public GraphEnvironmentController() {
	}
	/**
	 * The constructor for the GraphEnvironmentController for configurations within Agent.GUI
	 * 
	 * @param project The current project
	 */
	public GraphEnvironmentController(Project project) {
		super(project);
	}

	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#createEnvironmentPanel()
	 */
	@Override
	public EnvironmentPanel createEnvironmentPanel() {
		return new GraphEnvironmentControllerGUI(this);
	}

	/**
	 * Returns the current {@link GraphEnvironmentControllerGUI}.
	 * @return the current GraphEnvironmentControllerGUI
	 */
	public GraphEnvironmentControllerGUI getGraphEnvironmentControllerGUI() {
		return (GraphEnvironmentControllerGUI) this.getEnvironmentPanel();
	}

	/**
	 * Returns the current {@link UIMessagingController}.
	 * @return the ui messaging controller
	 */
	public UIMessagingController getUiMessagingController() {
		if (uiMessagingController==null) {
			uiMessagingController = new UIMessagingController(this);
		}
		return uiMessagingController;
	}
	
	/**
	 * Sets the project unsaved.
	 */
	public void setProjectUnsaved() {
		if (this.getProject() != null) {
			this.getProject().setUnsaved(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Observable#notifyObservers(java.lang.Object)
	 */
	@Override
	public void notifyObservers(Object arg) {
		this.setChanged();
		super.notifyObservers(arg);
	}

	/**
	 * Returns the {@link GeneralGraphSettings4MAS}
	 * @return the general graph settings4 mas
	 */
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		return this.getNetworkModel().getGeneralGraphSettings4MAS();
	}
	/**
	 * Returns the current {@link ComponentTypeSettings}
	 * @return the current component type settings TreeMap.
	 */
	public TreeMap<String, ComponentTypeSettings> getComponentTypeSettings() {
		return this.getGeneralGraphSettings4MAS().getCurrentCTS();
	}
	/**
	 * Returns the current {@link DomainSettings}.
	 * @return the current domain settings as TreeMap
	 */
	public TreeMap<String, DomainSettings> getDomainSettings() {
		return this.getGeneralGraphSettings4MAS().getDomainSettings();
	}
	/**
	 * Enables to set the current {@link NetworkModel}.
	 * @param newNetworkModel the new network model
	 */
	private void setNetworkModel(NetworkModel newNetworkModel) {
		// --- Remind the GeneralGraphSettings4MAS ----------------------------
		GeneralGraphSettings4MAS generalGraphSettings4MAS = null;
		if (this.networkModel != null) {
			generalGraphSettings4MAS = this.networkModel.getGeneralGraphSettings4MAS();
		}
		// --- Set NetworkModel -----------------------------------------------
		if (newNetworkModel == null) {
			this.networkModel = new NetworkModel();
			this.networkModel.setGeneralGraphSettings4MAS(generalGraphSettings4MAS);
		} else {
			this.networkModel = newNetworkModel;
		}

		// --- Clean up agents list corresponding to current NetworkModel -----
		this.validateNetworkComponentAndAgents2Start();
		// --- Notify all Observers about a new NetworkModel ------------------
		this.getNetworkModelUndoManager().reLoadNetworkModel();

	}

	/**
	 * Returns the current {@link NetworkModel}
	 * @return NetworkModel - The environment model
	 */
	public NetworkModel getNetworkModel() {
		if (networkModel == null) {
			networkModel = new NetworkModel();
			networkModel.setGeneralGraphSettings4MAS(this.loadGeneralGraphSettings());
		}
		return networkModel;
	}

	/**
	 * Returns the {@link NetworkModelUndoManager} that enables to do or undo actions in the visualization.
	 * @return the NetworkModelUndoManager for the current controller
	 */
	public NetworkModelUndoManager getNetworkModelUndoManager() {
		if (networkModelUndoManager == null) {
			networkModelUndoManager = new NetworkModelUndoManager(this);
		}
		return networkModelUndoManager;
	}
	
	
	/**
	 * Copies a set of NetworkComponent's as independent NetworkModel to the clipboard.
	 * @param networkComponentsForClipboard the network components
	 */
	public void copyToClipboard(List<NetworkComponent> networkComponentsForClipboard) {

		if (networkComponentsForClipboard==null || networkComponentsForClipboard.size()==0) return;
		if (this.getNetworkModel()==null) return;

		// ---------- Prepare for the Clipboard ---------------------
		NetworkModel clipNetworkModel = this.getNetworkModel().getCopy();
		clipNetworkModel.removeNetworkComponentsInverse(networkComponentsForClipboard);
		clipNetworkModel.resetGraphElementLayout();

		this.setClipboardNetworkModel(clipNetworkModel);
	}

	/**
	 * Sets a NetworkModel to the clipboard.
	 * @param newClipboardNetworkModel the new clipboard network model
	 */
	public void setClipboardNetworkModel(NetworkModel newClipboardNetworkModel) {
		this.clipboardNetworkModel = newClipboardNetworkModel;
	}
	/**
	 * Returns the NetworkModel from the clipboard .
	 * @return the clipboard NetworkModel
	 */
	public NetworkModel getClipboardNetworkModel() {
		return clipboardNetworkModel;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#dispose()
	 */
	@Override
	public void dispose() {
		this.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_ControllerIsDisposing));
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#handleProjectNotification(java.lang.Object)
	 */
	@Override
	protected void handleProjectNotification(Object updateObject) {
		if (updateObject.equals(Project.PREPARE_FOR_SAVING)) {
			this.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Prepare4Saving));
		}
	}
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#handleSimulationSetupNotification(agentgui.core.sim.setup.SimulationSetupsChangeNotification)
	 */
	@Override
	protected void handleSimulationSetupNotification(SimulationSetupNotification sscn) {

		switch (sscn.getUpdateReason()) {
		case SIMULATION_SETUP_ADD_NEW:
			this.updateSetupName();
			this.setDisplayEnvironmentModel(null);
			// --- Register a new list of agents that has to be started with the environment ------
			this.setAgents2Start(new DefaultListModel<AgentClassElement4SimStart>());
			this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
			break;

		case SIMULATION_SETUP_COPY:
			this.saveEnvironment();
			break;

		case SIMULATION_SETUP_REMOVE:
			File graphFile = this.getFileGraphML();
			if (graphFile.exists()) {
				graphFile.delete();
			}
			File componentFile = this.getFileXML();
			if (componentFile.exists()) {
				componentFile.delete();
			}
			this.callSetupDataModelStorageServices(DataModelServiceAction.RemoveSetup);
			break;

		case SIMULATION_SETUP_RENAME:

			// --- Collect old settings -------------------
			String setupNameOld = this.setupName;
			File oldGraphFile = this.getFileGraphML();
			File oldComponentFile = this.getFileXML();
			
			// --- Internally update to new settings ------
			this.updateSetupName();

			// --- Rename ---------------------------------
			if (oldGraphFile.exists()) {
				oldGraphFile.renameTo(this.getFileGraphML());
			}
			if (oldComponentFile.exists()) {
				oldComponentFile.renameTo(this.getFileXML());
			}
			
			// --- Call storage services ------------------
			String setupNameNew = this.getProject().getSimulationSetupCurrent();
			if (setupNameNew.equals(setupNameOld)==false) {
				this.callSetupDataModelStorageServices(DataModelServiceAction.RenameSetup, setupNameOld, setupNameNew);
			}
			break;

		case SIMULATION_SETUP_PREPARE_SAVING:
			this.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Prepare4Saving));
			break;

		case SIMULATION_SETUP_LOAD:
		case SIMULATION_SETUP_SAVED:
		case SIMULATION_SETUP_DETAILS_LOADED:
		case SIMULATION_SETUP_DETAILS_SAVED:
		case SIMULATION_SETUP_AGENT_ADDED:
		case SIMULATION_SETUP_AGENT_REMOVED:
		case SIMULATION_SETUP_AGENT_RENAMED:
			// --- Nothing to do here ---------------------
			break;
		}

	}

	/**
	 * This method sets the setupName property and the SimulationSetup's environmentFileName according to the current SimulationSetup
	 */
	private void updateSetupName() {
		this.setupName = this.getProject().getSimulationSetupCurrent();
	}
	/**
	 * Returns the current setup name.
	 * @return the setup name
	 */
	public String getSetupName() {
		return this.setupName;
	}
	
	/**
	 * Returns the XML file for the NetworkComponents and so on.
	 * @return the XML file for the NetworkComponents and so on 
	 */
	public File getFileXML() {
		return getFileXML(this.getEnvFolderPath(), this.setupName);
	}
	/**
	 * Returns the XML file for the NetworkComponents and so on.
	 *
	 * @param envDirectory the directory for the environment model 
	 * @param setupName the setup name
	 * @return the XML file for the NetworkComponents and so on
	 */
	public static File getFileXML(String envDirectory, String setupName) {
		return new File(envDirectory + setupName + ".xml");
	}
	
	/**
	 * Returns the GraphML file for the current NetworkModel.
	 * @return the GraphML file
	 */
	public File getFileGraphML() {
		return getFileGraphML(this.getEnvFolderPath(), this.setupName);
	}
	/**
	 * Returns the XML file with the graph definition in graphml.
	 *
	 * @param envDirectory the directory for the environment model
	 * @param setupName the setup name
	 * @return the XML file for the NetworkComponents and so on
	 */
	public static File getFileGraphML(String envDirectory, String setupName) {
		return new File(envDirectory + setupName + ".graphml");
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getSetupFiles(java.lang.String)
	 */
	@Override
	public List<File> getSetupFiles(String setupName) {
		
		List<File> fileList = new ArrayList<File>();
		
		// --- Get the default files for graph and NetworkModel -----
		fileList.add(GraphEnvironmentController.getFileGraphML(this.getEnvFolderPath(), setupName));
		fileList.add(GraphEnvironmentController.getFileXML(this.getEnvFolderPath(), setupName));

		// --- Check if a sub directory exists ----------------------
		String setupSubDir = this.getEnvFolderPath();
		if (setupSubDir.endsWith(File.separator)==false) setupSubDir+=File.separator;
		setupSubDir += setupName;
		File envSetupSubDir = new File(setupSubDir);
		if (envSetupSubDir.exists()==true) {
			File[] subDirFileArray = envSetupSubDir.listFiles();
			if (subDirFileArray!=null && subDirFileArray.length>0) {
				fileList.addAll(Arrays.asList(subDirFileArray));
			}
		}
		
		// --- Ask the specific storage services for files ----------
		List<File> sdmServicesFileList = this.getSetupFilesFromSetupDataModelStorageServices(setupName);
		if (sdmServicesFileList!=null) {
			fileList.addAll(sdmServicesFileList);
		}
		return fileList.stream().distinct().collect(Collectors.toList());
	}
	
	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getPersistenceStrategy()
	 */
	@Override
	protected PersistenceStrategy getPersistenceStrategy() {
		return PersistenceStrategy.HandleWithSetupOpenOrSave;
	}
	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#loadEnvironment()
	 */
	@Override
	public void loadEnvironment() {
		
		// --- Check for the current setup --------------------------------------------------------
		if (this.setupName==null) {
			this.updateSetupName();
			if (setupName==null) return;
		}

		// --- Set lock to prevent parallel saving actions ----------------------------------------
		this.isTemporaryPreventSaving = true;
		
		// --- Define loader thread ---------------------------------------------------------------
		Thread envLoader = new Thread(new Runnable() {
			@Override
			public void run() {

				try {

					boolean isApplication = Application.getGlobalInfo().getExecutionMode()==ExecutionMode.APPLICATION;

					// --- Register agents that have to be started with the environment -----------
					GraphEnvironmentController.this.setAgents2Start(new DefaultListModel<AgentClassElement4SimStart>());
					GraphEnvironmentController.this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);

					// --- Update path according to setup -----------------------------------------
					GraphEnvironmentController.this.updateSetupName();

					// --- Set application status text --------------------------------------------
					if (Application.getMainWindow()!=null) {
						Application.getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						Application.setStatusBarMessage(Language.translate("Lade Setup") + ": " + GraphEnvironmentController.this.getFileXML().getAbsolutePath() + " ...");
						// --- Reset Undo-Manager -------------------------------------------------
						GraphEnvironmentController.this.getNetworkModelUndoManager().getUndoManager().discardAllEdits();
					}
					
					// --- 0. Define a new NetworkModel -------------------------------------------
					NetworkModel networkModel = new NetworkModel();
					// --- => Set the NetworkModel instance to the GraphNodePositionFactory -------
					GraphNodePositionFactory.setLoadingNetworkModel(networkModel);
					
					// --- 1. Load component type settings from file ------------------------------
					GeneralGraphSettings4MAS ggs4MAS = GraphEnvironmentController.this.loadGeneralGraphSettings();
					networkModel.setGeneralGraphSettings4MAS(ggs4MAS);
					// --- 2. Remind the list of custom toolbar elements --------------------------
					if (ggs4MAS!=null && GraphEnvironmentController.this.getNetworkModel()!=null && GraphEnvironmentController.this.getGeneralGraphSettings4MAS()!=null) {
						ggs4MAS.setCustomToolbarComponentDescriptions(GraphEnvironmentController.this.getGeneralGraphSettings4MAS().getCustomToolbarComponentDescriptions());
					}
					
					// --- 3. Load the component definitions and other from the component file ----
					networkModel.loadComponentsFile(GraphEnvironmentController.this.getFileXML());

					// --- 4. Load the graph topology from the graph file -------------------------
					networkModel.loadGraphFile(GraphEnvironmentController.this.getFileGraphML());
					
					// --- 5. Refresh the graph elements in the NetworkModel ----------------------
					networkModel.refreshGraphElements();
					
					// --- 6. Load individual data models of components ---------------------------
					GraphEnvironmentController.this.loadDataModelNetworkElements(networkModel, GraphEnvironmentController.this.getSetupName(), isApplication, null, null);
					
					// --- Use case 'Application' ? -----------------------------------------------
					if (isApplication==false) {
						// ------------------------------------------------------------------------
						// --- Directly assign NetworkModel to graph controller -------------------
						// ------------------------------------------------------------------------
						GraphEnvironmentController.this.setDisplayEnvironmentModel(networkModel);
						
					} else {
						// ------------------------------------------------------------------------
						// --- Wait for visualization component before assign network model -------
						// ------------------------------------------------------------------------
						while (GraphEnvironmentController.this.getEnvironmentPanel()==null) {
							Thread.sleep(20);
						}
						BasicGraphGui basicGraphGui = GraphEnvironmentController.this.getGraphEnvironmentControllerGUI().getBasicGraphGuiRootJSplitPane().getBasicGraphGui();
						while (basicGraphGui.isCreatedVisualizationViewer()==false) {
							Thread.sleep(20);
						}	
						
						// --- Assign NetworkMoldel to visualization ------------------------------
						final NetworkModel netModelFinal = networkModel;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								// --- Use the local method in order to inform the observer -------
								GraphEnvironmentController.this.setDisplayEnvironmentModel(netModelFinal);
							}
						});
					}
					
				} catch (Exception ex) {
					ex.printStackTrace();
					
				} finally {
					// --- => Reset the NetworkModel instance in the GraphNodePositionFactory -----
					GraphNodePositionFactory.setLoadingNetworkModel(null);
					GraphEnvironmentController.this.isTemporaryPreventSaving = false;
					Application.getMainWindow().setCursor(Cursor.getDefaultCursor());
					Application.setStatusBarMessageReady();
				}
				
			}
		});
		envLoader.setName("GraphEnvironmentLoader");
		envLoader.start();

	}

	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#saveEnvironment()
	 */
	@Override
	public void saveEnvironment() {

		// --- Check if saving is currently allowed ----------------- 
		while (this.isTemporaryPreventSaving==true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException iEx) {
				iEx.printStackTrace();
				break;
			}
		}
		
		// --- Prepare for saving -----------------------------------
		this.validateNetworkComponentAndAgents2Start();
		this.saveGeneralGraphSettings();
		if (this.getNetworkModel()!=null && this.getNetworkModel().getGraph()!=null) {
			
			// --- Update path according to setup -------------------
			this.updateSetupName();

			File graphFile = this.getFileGraphML();
			this.getNetworkModel().saveGraphFile(graphFile);
			
			File componentsFile = this.getFileXML();
			this.getNetworkModel().saveComponentsFile(componentsFile);
			
			// --- Save individual models ---------------------------
			this.callSetupDataModelStorageServices(DataModelServiceAction.SaveSetup);
		}
	}

	
	/**
	 * Sets all persisted data models of {@link DataModelNetworkElement}s to instances (e.g. Base64 encoded models strings).
	 *
	 * @param isShowProgress the indicator to show a progress monitor or not
	 * @param netElementVector the explicit vector of DataModelNetworkElement's to load or reload (<code>null</code> is allowed)
	 * @param maxNumberOfThread the max number of thread
	 */
	public void loadDataModelNetworkElements(boolean isShowProgress, Vector<DataModelNetworkElement> netElementVector, Integer maxNumberOfThread) {
		this.loadDataModelNetworkElements(null, null, isShowProgress, netElementVector, maxNumberOfThread);
	}
	/**
	 * Sets all persisted data models of {@link DataModelNetworkElement}s to instances (e.g. Base64 encoded models strings).
	 *
	 * @param networkModel the network model to work on; may be <code>null</code>. If null, the current NetworkModel will be used.
	 * @param setupName the name of the setup; may be <code>null</code>. If null, the current setup name will be used.
	 * @param isShowProgress the indicator to show a progress monitor or not
	 * @param netElementVector the explicit vector of DataModelNetworkElement's to load or reload (<code>null</code> is allowed)
	 * @param maxNumberOfThread the max number of thread
	 */
	public void loadDataModelNetworkElements(NetworkModel networkModel, String setupName, boolean isShowProgress, Vector<DataModelNetworkElement> netElementVector, Integer maxNumberOfThread) {
		this.callSetupDataModelStorageServices(DataModelServiceAction.LoadSetup, setupName, null);
		new DataModelStorageThread(this, networkModel, setupName, OrganizerAction.ORGANIZE_LOADING, isShowProgress, netElementVector, maxNumberOfThread).start();
	}
	
	/**
	 * Saves all instances of individual data models in {@link DataModelNetworkElement}s (e.g. as Base64 encoded strings).
	 *
	 * @param isShowProgress the indicator to show a progress monitor or not
	 * @param netElementVector the explicit vector of DataModelNetworkElement's to save (<code>null</code> is allowed)
	 * @param maxNumberOfThreads the maximum number of threads to use (<code>null</code> is allowed)
	 */
	public void saveDataModelNetworkElements(boolean isShowProgress, Vector<DataModelNetworkElement> netElementVector, Integer maxNumberOfThreads) {
		this.saveDataModelNetworkElements(null, null, isShowProgress, netElementVector, maxNumberOfThreads);
	}
	/**
	 * Saves all instances of individual data models in {@link DataModelNetworkElement}s (e.g. as Base64 encoded strings).
	 *
	 * @param networkModel the network model to work on; may be <code>null</code>. If null, the current NetworkModel will be used.
	 * @param setupName the name of the setup; may be <code>null</code>. If null, the current setup name will be used.
	 * @param isShowProgress the indicator to show a progress monitor or not
	 * @param netElementVector the explicit vector of DataModelNetworkElement's to save (<code>null</code> is allowed)
	 * @param maxNumberOfThreads the maximum number of threads to use (<code>null</code> is allowed)
	 */
	public void saveDataModelNetworkElements(NetworkModel networkModel, String setupName, boolean isShowProgress, Vector<DataModelNetworkElement> netElementVector, Integer maxNumberOfThreads) {
		new DataModelStorageThread(this, networkModel, setupName, OrganizerAction.ORGANIZE_SAVING, isShowProgress, netElementVector, maxNumberOfThreads).start();
		this.callSetupDataModelStorageServices(DataModelServiceAction.SaveSetup, setupName, null);
	}
	
	
	// ----------------------------------------------------------------------------------
	// --- From here, the handling of setup data model storage handler can be found -----
	// ----------------------------------------------------------------------------------
	/**
	 * Return all known setup storage handler that were registered as such an OSGI - service.
	 * @return the setup storage handler
	 */
	private HashMap<Class<? extends AbstractDataModelStorageHandler>, SetupDataModelStorageService> getSetupDataModelStorageServiceHashMap() {
		if (setupStorageServiceHashMap==null) {
			setupStorageServiceHashMap = new HashMap<>();
			// --- Get all registered services ----------------------
			List<SetupDataModelStorageService> sdmServiceList = ServiceFinder.findServices(SetupDataModelStorageService.class);
			for (int i = 0; i < sdmServiceList.size(); i++) {
				SetupDataModelStorageService sdmService = sdmServiceList.get(i);
				sdmService.setGraphEnvironmentController(this);
				Class<? extends AbstractDataModelStorageHandler> storageHandlerClass = sdmService.getDataModelStorageHandlerClass();
				if (storageHandlerClass!=null) {
					setupStorageServiceHashMap.put(storageHandlerClass, sdmService);
				}
			}
		}
		return setupStorageServiceHashMap;
	}
	/**
	 * Return the setup data model storage handler (setup scope) for the specified data model  
	 * storage handler (scope of the individual data model of {@link NetworkComponent} or {@link GraphNode}).
	 *
	 * @param storageHandlerClass the storage handler class
	 * @return the setup handler for individual data models
	 */
	public SetupDataModelStorageService getSetupDataModelStorageService(Class<? extends AbstractDataModelStorageHandler> storageHandlerClass) {
		if (storageHandlerClass!=null) {
			return this.getSetupDataModelStorageServiceHashMap().get(storageHandlerClass);
		}
		return null;
	}
	
	/**
	 * Calls the known {@link SetupDataModelStorageService}s to do the specified action.
	 *
	 * @param serviceAction the service action to invoke
	 * @param setupName the name of the setup to work on
	 */
	private void callSetupDataModelStorageServices(DataModelServiceAction serviceAction) {
		this.callSetupDataModelStorageServices(serviceAction, null, null);
	}
	/**
	 * Calls the known {@link SetupDataModelStorageService}s to do the specified action.
	 *
	 * @param serviceAction the service action to invoke
	 * @param setupName the name of the setup to work on
	 * @param setupNameNew only needed for renaming. The new setup name
	 */
	private void callSetupDataModelStorageServices(DataModelServiceAction serviceAction, String setupName, String setupNameNew) {
		
		if (setupName==null) setupName = this.getSetupName();
		
		List<SetupDataModelStorageService> sdmServiceList = new ArrayList<SetupDataModelStorageService>(this.getSetupDataModelStorageServiceHashMap().values());
		for (int i = 0; i < sdmServiceList.size(); i++) {
			SetupDataModelStorageService sdmService = sdmServiceList.get(i);
			try {
				
				switch (serviceAction) {
				case LoadSetup:
					sdmService.loadNetworkElementDataModels(setupName);
					break;
				case SaveSetup:
					sdmService.saveNetworkElementDataModels(setupName);
					break;
				case RemoveSetup:
					sdmService.removeNetworkElementDataModels(setupName);
					break;
				case RenameSetup:
					sdmService.renameNetworkElementDataModels(setupName, setupNameNew);
					break;
				}
				
			} catch (Exception ex) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error while invoking '" + serviceAction.toString() + "' on SetupDataModelStorageService '" + sdmService.getClass().getName() + "'");
				ex.printStackTrace();
			}
		}
	}
	/**
	 * Return the setup files from setup data model storage services for the current setup.
	 * @param setupName2 
	 * @return the setup files from setup data model storage services
	 */
	private List<File> getSetupFilesFromSetupDataModelStorageServices(String setupName) {
	
		List<File> setupFilesOfServices = new ArrayList<>();
		
		List<SetupDataModelStorageService> sdmServiceList = new ArrayList<SetupDataModelStorageService>(this.getSetupDataModelStorageServiceHashMap().values());
		for (int i = 0; i < sdmServiceList.size(); i++) {
			SetupDataModelStorageService sdmService = sdmServiceList.get(i);
			try {
				List<File> sdmServiveSetupFiles = sdmService.getSetupFiles(setupName);
				if (sdmServiveSetupFiles!=null) {
					setupFilesOfServices.addAll(sdmServiveSetupFiles);
				}
				
			} catch (Exception ex) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error while invoking 'getSetupFiles()' on SetupDataModelStorageService '" + sdmService.getClass().getName() + "'");
				ex.printStackTrace();
			}
		}
		return setupFilesOfServices;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#setEnvironmentModel(java.lang.Object)
	 */
	@Override
	public void setDisplayEnvironmentModel(DisplaytEnvironmentModel displaytEnvironmentModel) {
		try {
			if (displaytEnvironmentModel==null && !(displaytEnvironmentModel instanceof NetworkModel) ) {
				this.setNetworkModel(null);
			} else {
				this.setNetworkModel((NetworkModel) displaytEnvironmentModel);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getEnvironmentModel()
	 */
	@Override
	public DisplaytEnvironmentModel getDisplayEnvironmentModel() {
		return this.getNetworkModel();
	}
	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getEnvironmentModelCopy()
	 */
	@Override
	public DisplaytEnvironmentModel getDisplayEnvironmentModelCopy() {
		return this.getNetworkModel().getCopy();
	}

	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#setAbstractEnvironmentModel(java.lang.Object)
	 */
	@Override
	public void setAbstractEnvironmentModel(AbstractEnvironmentModel abstractEnvironmentModel) {
		this.abstractEnvironmentModel = abstractEnvironmentModel;
	}
	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getAbstractEnvironmentModel()
	 */
	@Override
	public AbstractEnvironmentModel getAbstractEnvironmentModel() {
		return this.abstractEnvironmentModel;
	}
	/*
	 * (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getAbstractEnvironmentModelCopy()
	 */
	@Override
	public AbstractEnvironmentModel getAbstractEnvironmentModelCopy() {
		if (this.abstractEnvironmentModel == null) {
			return null;
		} else {
			return this.abstractEnvironmentModel.getCopy();
		}
	}

	/**
	 * Loads the general graph settings from the usual location on disc.
	 * @return the GeneralGraphSettings4MAS
	 */
	public GeneralGraphSettings4MAS loadGeneralGraphSettings() {
		File componentFile = new File(this.getEnvFolderPath() + GeneralGraphSettings4MASFile + ".xml");
		return GeneralGraphSettings4MAS.load(componentFile);
	}
	/**
	 * Saves general graph settings to the usual location on disc.
	 */
	public void saveGeneralGraphSettings() {
		this.saveGeneralGraphSettings(this.getGeneralGraphSettings4MAS());
	}
	/**
	 * Saves the specified general graph settings to the usual location on disc.
	 * @param graphSettings the actual graph settings to save
	 */
	public void saveGeneralGraphSettings(GeneralGraphSettings4MAS graphSettings) {
		File componentFile = new File(this.getEnvFolderPath() + GeneralGraphSettings4MASFile + ".xml");
		GeneralGraphSettings4MAS.save(componentFile, graphSettings);
	}
	
	
	/**
	 * Clean up / correct list of agents corresponding to the current NetworkModel.
	 */
	public void validateNetworkComponentAndAgents2Start() {

		// --------------------------------------------------------------------
		// --- Get the current ComponentTypeSettings --------------------------
		TreeMap<String, ComponentTypeSettings> cts = this.getNetworkModel().getGeneralGraphSettings4MAS().getCurrentCTS();

		// --------------------------------------------------------------------
		// --- Transfer the agent list into a HashMap for a faster access -----
		HashMap<String, AgentClassElement4SimStart> agents2StartHash = new HashMap<String, AgentClassElement4SimStart>();
		for (int i = 0; i < this.getAgents2Start().size(); i++) {
			AgentClassElement4SimStart ace4s = (AgentClassElement4SimStart) this.getAgents2Start().get(i);
			String agentName = ace4s.getStartAsName();

			AgentClassElement4SimStart ace4sThere = agents2StartHash.get(agentName);
			agents2StartHash.put(agentName, ace4s);
			if (ace4sThere != null) {
				// --- Remove the redundant entries and let one entry survive -
				AgentClassElement4SimStart[] ace4sArr2Delete = getAgents2StartFromAgentName(agentName);
				for (int j = 0; j < ace4sArr2Delete.length - 1; j++) {
					this.getAgents2Start().removeElement(ace4sArr2Delete[j]);
				}
			}
		}

		// --------------------------------------------------------------------
		// --- Run through the network components and validate agent list -----
		for (String compName : this.getNetworkModel().getNetworkComponents().keySet()) {
			NetworkComponent comp = this.getNetworkModel().getNetworkComponent(compName);
			
			if (comp==null) {
				System.err.println("[" + this.getClass().getSimpleName() +"] Netowrk component for compName " + compName + " not found!");
				continue;
			}

			if (!(comp instanceof ClusterNetworkComponent)) {
				// ----------------------------------------------------------------
				// --- Validate current component against ComponentTypeSettings ---
				
				try {
					ComponentTypeSettings ctsSingle = cts.get(comp.getType());
					if (ctsSingle == null) {
						// --- remove this component ---
						this.getNetworkModel().removeNetworkComponent(comp);
						comp = null;
						
					} else {
						// --- Component settings here? -----------
						// Empty after revision of NetworkComponent  
					}
					
				} catch (NullPointerException npe) {
					System.err.println("[" + this.getClass().getSimpleName() + "] Null Pointer Exception!");
					npe.printStackTrace();
				}

				// ----------------------------------------------------------------
				// --- Check if an Agent can be found in the start list -----------
				AgentClassElement4SimStart ace4s = agents2StartHash.get(compName);
				if (ace4s == null) {
					// --- Agent definition NOT found in agent start list ---------
					this.addAgent(comp);

				} else {
					// --- Agent definition found in agent start list -------------
					if (isValidAgent2Start(ace4s, comp) == false) {
						// --- Error found --------------------
						this.getAgents2Start().removeElement(ace4s);
						this.addAgent(comp);
					}
					agents2StartHash.remove(compName);
				}
			}

		} // end for

		// --------------------------------------------------------------------
		// --- Are there remaining agents in the start list? ------------------
		if (agents2StartHash.size() != 0) {

			Collection<String> remainingAgents2Start = agents2StartHash.keySet();
			String[] remainingAgents = remainingAgents2Start.toArray(new String[remainingAgents2Start.size()]);
			for (int i = 0; i < remainingAgents.length; i++) {
				String remainingAgent = remainingAgents[i];
				AgentClassElement4SimStart remainingAce4s = agents2StartHash.get(remainingAgent);
				this.getAgents2Start().removeElement(remainingAce4s);
			}
		}

		// --------------------------------------------------------------------
		// --- Renumber list --------------------------------------------------
		this.reNumberAgents2Start();
		this.getNetworkModelUndoManager().refreshNetworkModel();
	}

	/**
	 * Checks if is valid agent2 start.
	 * 
	 * @param ace4s the AgentClassElement4SimStart
	 * @param comp the NetworkComponent
	 * @return true, if the AgentClassElement4SimStart is valid
	 */
	private boolean isValidAgent2Start(AgentClassElement4SimStart ace4s, NetworkComponent comp) {
		if (comp == null) {
			return false;
		}
		String agentClassName = this.getNetworkModel().getAgentClassName(comp);
		if (ace4s.getAgentClassReference().equals(agentClassName)==false) {
			return false;
		}
		if (ace4s.getStartAsName().equals(comp.getId()) == false) {
			return false;
		}
		return true;
	}

	/**
	 * Adds an agent to the start list corresponding to the current network component .
	 * @param networkComponent the NetworkComponent
	 */
	public void addAgent(NetworkComponent networkComponent) {

		if (networkComponent == null) return;
		
		String agentClassName = this.getNetworkModel().getAgentClassName(networkComponent);
		Class<? extends Agent> agentClass = this.getAgentClass(agentClassName);
		if (agentClass != null) {

			int newPosNo = this.getEmptyPosition4Agents2Start();
			// --- Agent class found. Create new list element ---------
			AgentClassElement4SimStart ace4s = new AgentClassElement4SimStart(agentClass, SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
			ace4s.setStartAsName(networkComponent.getId());
			ace4s.setPostionNo(newPosNo);
			// --- Add the new list element to the list ---------------
			this.getAgents2Start().add(newPosNo - 1, ace4s);
		}
	}

	/**
	 * Removes an agent from the start list corresponding to the current network component .
	 * 
	 * @param networkComponent the NetworkComponent
	 */
	public void removeAgent(NetworkComponent networkComponent) {

		if (networkComponent == null) {
			return;
		}

		String search4 = networkComponent.getId();
		DefaultListModel<AgentClassElement4SimStart> agentList = this.getAgents2Start();
		int i = 0;
		for (i = 0; i < agentList.size(); i++) {
			AgentClassElement4SimStart agentElement = (AgentClassElement4SimStart) agentList.get(i);
			if (agentElement.getStartAsName().equals(search4)) {
				agentList.remove(i);
				break;
			}
		}

		// Shifting the positions of the later components by 1
		for (int j = i; j < agentList.size(); j++) {
			AgentClassElement4SimStart ac4s = (AgentClassElement4SimStart) agentList.get(j);
			ac4s.setPostionNo(ac4s.getPostionNo() - 1);
		}

	}

	/**
	 * Renames an agent by its old NetworkComponentID.
	 * 
	 * @param oldCompID the old NetworkComponentID
	 * @param newCompID the new NetworkComponentID
	 */
	public void renameAgent(String oldCompID, String newCompID) {
		if (this.getCurrentSimulationSetup()!=null) {
			this.getCurrentSimulationSetup().renameAgent(oldCompID, newCompID);
		}
	}

	/**
	 * Returns the agent class.
	 * 
	 * @param agentClassName the agent class name 
	 * @return the agent class
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends Agent> getAgentClass(String agentClassName) {

		if (agentClassName == null || agentClassName.isEmpty()==true) return null;

		Class<? extends Agent> agentClass = null;
		try {
			agentClass = (Class<? extends Agent>) ClassLoadServiceUtility.forName(agentClassName);
		} catch (ClassNotFoundException ex) {
			System.err.println("Could not find agent class '" + agentClassName + "'");
		}
		return agentClass;
	}


	/**
	 * Sets the indicator that an action on top is running, in order to prevent permanently (re-)paint actions.
	 * 
	 * @param actionOnTopIsRunning the indicator that an action on top of the graph is running or not
	 */
	public void setBasicGraphGuiVisViewerActionOnTop(boolean actionOnTopIsRunning) {

		BasicGraphGuiVisViewer<GraphNode, GraphEdge> basicGraphGuiVisViewer = null;
		try {
			GraphEnvironmentControllerGUI graphControllerGUI = this.getGraphEnvironmentControllerGUI();
			if (graphControllerGUI != null) {
				basicGraphGuiVisViewer = graphControllerGUI.getBasicGraphGuiRootJSplitPane().getBasicGraphGui().getVisualizationViewer();
				if (basicGraphGuiVisViewer != null) {
					basicGraphGuiVisViewer.setActionOnTop(actionOnTopIsRunning);
					if (actionOnTopIsRunning==false) {
						basicGraphGuiVisViewer.repaint();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	
	/**
	 * Return all known import adapter from the OSGI service registry.
	 * @return the import adapter
	 */
	public Vector<NetworkModelImportService> getImportAdapter() {
		
		Vector<NetworkModelImportService> importAdapter = new Vector<NetworkModelImportService>();
			
		List<NetworkModelImportService> importServices = ServiceFinder.findServices(NetworkModelImportService.class);
		for (int i=0; i<importServices.size(); i++) {
			NetworkModelImportService importer = importServices.get(i);
			importer.setGraphController(this);
			importAdapter.add(importer);
		}
		return importAdapter;
	}
	/**
	 * Return all known export adapter from the OSGI service registry.
	 * @return the export adapter
	 */
	public Vector<NetworkModelExportService> getExportAdapter() {
		
		Vector<NetworkModelExportService> exportAdapter = new Vector<NetworkModelExportService>();
			
		List<NetworkModelExportService> exportServices = ServiceFinder.findServices(NetworkModelExportService.class);
		for (int i=0; i<exportServices.size(); i++) {
			NetworkModelExportService importer = exportServices.get(i);
			importer.setGraphController(this);
			exportAdapter.add(importer);
		}
		return exportAdapter;
	}
	
	
	/**
	 * Adds a {@link CustomToolbarComponentDescription}, so that customized components will be added to a toolbar of the {@link BasicGraphGui}.
	 * @param customButtonDescription the CustomToolbarComponentDescription to add
	 */
	public void addCustomToolbarComponentDescription(CustomToolbarComponentDescription customButtonDescription) {
		if (this.getNetworkModel()!=null && this.getNetworkModel().getGeneralGraphSettings4MAS()!=null) {
			this.getNetworkModel().getGeneralGraphSettings4MAS().getCustomToolbarComponentDescriptions().add(customButtonDescription);
			this.getNetworkModelUndoManager().addCustomToolbarComponentDescription(customButtonDescription);
		} else {
			String errMsg = Language.translate("Could not add custom button: No NetworkModel was defined yet!", Language.EN);
			System.err.println(errMsg);
		}
	}

}