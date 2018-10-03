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
package agentgui.envModel.graph.controller;

import java.awt.Cursor;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.project.Project;
import agentgui.core.project.setup.AgentClassElement4SimStart;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.envModel.graph.controller.DataModelEnDecoderThread.OrganizerAction;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelAdapter;
import agentgui.simulationService.environment.AbstractEnvironmentModel;
import agentgui.simulationService.environment.DisplaytEnvironmentModel;
import jade.core.Agent;

/**
 * This class manages an environment model of the type graph / network.<br>
 * Also contains the network component type settings configuration.<br>
 * The observable class of the network model in the observer pattern.
 * 
 * @see agentgui.envModel.graph.networkModel.NetworkModel
 * @see agentgui.envModel.graph.networkModel.ComponentTypeSettings
 * @see GraphEnvironmentControllerGUI
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class GraphEnvironmentController extends EnvironmentController {

	/** Custom user object to be placed in the project object. Used here for storing the current component type settings. */
	private static final String GeneralGraphSettings4MASFile = "~GeneralGraphSettings~";

	
	/**
	 * The Enumeration with possible ControllerAction.
	 */
	private enum ControllerAction {
		Initializing
	}
	/** The current controller action. */
	private ControllerAction currControllerAction;
	
	
	/** The base file name used for saving the graph and the components (without suffix) */
	private String baseFileName;
	/** Known adapter for the import of network models */
	private Vector<NetworkModelFileImporter> importAdapter;

	/** The network model currently loaded */
	private NetworkModel networkModel;
	private NetworkModelAdapter networkModelAdapter;

	/** The NetworkModel that is stored in the clipboard */
	private NetworkModel clipboardNetworkModel;

	/** The abstract environment model is just an open slot, where individual things can be placed. */
	private AbstractEnvironmentModel abstractEnvironmentModel;

	/** The is temporary prevent saving. */
	private boolean isTemporaryPreventSaving;
	
	/**
	 * The constructor for the GraphEnvironmentController for displaying the current environment model during a running simulation. Use {@link #setDisplayEnvironmentModel(DisplaytEnvironmentModel)}, in order to set the current {@link NetworkModel}.
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
		if (this.getProject()!=null) {
			this.currControllerAction=ControllerAction.Initializing;
			this.updateGraphFileName();
			this.currControllerAction=null;;
			this.loadEnvironment();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.environment.EnvironmentController#createEnvironmentPanel()
	 */
	@Override
	public EnvironmentPanel createEnvironmentPanel() {
		return new GraphEnvironmentControllerGUI(this);
	}

	/**
	 * Gets the GraphEnvironmentControllerGUI.
	 * 
	 * @return the current GraphEnvironmentControllerGUI
	 */
	public GraphEnvironmentControllerGUI getGraphEnvironmentControllerGUI() {
		return (GraphEnvironmentControllerGUI) this.getEnvironmentPanel();
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
	 * 
	 * @see java.util.Observable#notifyObservers(java.lang.Object)
	 */
	@Override
	public void notifyObservers(Object arg) {
		this.setChanged();
		super.notifyObservers(arg);
	}

	/**
	 * Gets the general graph settings4 mas.
	 * 
	 * @return the general graph settings4 mas
	 */
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		return this.getNetworkModel().getGeneralGraphSettings4MAS();
	}

	/**
	 * Gets the current ComponentTypeSettings
	 * 
	 * @return HashMap<String, ComponentTypeSettings> The current component type settings map.
	 */
	public TreeMap<String, ComponentTypeSettings> getComponentTypeSettings() {
		return this.getGeneralGraphSettings4MAS().getCurrentCTS();
	}

	/**
	 * Returns the DomainSttings.
	 * 
	 * @return the DomainSttings
	 */
	public TreeMap<String, DomainSettings> getDomainSettings() {
		return this.getGeneralGraphSettings4MAS().getDomainSettings();
	}

	/**
	 * Sets the environment network model
	 * 
	 * @return NetworkModel - The environment model
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
		this.getNetworkModelAdapter().reLoadNetworkModel();

	}

	/**
	 * Returns the environment network model
	 * 
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
	 * Gets the network model for actions.
	 * 
	 * @return the network model action
	 */
	public NetworkModelAdapter getNetworkModelAdapter() {
		if (networkModelAdapter == null) {
			networkModelAdapter = new NetworkModelAdapter(this);
		}
		return networkModelAdapter;
	}

	/**
	 * Copies a set of NetworkComponent's as independent NetworkModel to the clipboard.
	 * 
	 * @param networkComponentsForClipboard the network components
	 */
	public void copyToClipboard(HashSet<NetworkComponent> networkComponentsForClipboard) {

		if (networkComponentsForClipboard == null)
			return;
		if (this.getNetworkModel() == null)
			return;

		// ---------- Prepare for the Clipboard ---------------------
		NetworkModel clipNetworkModel = this.getNetworkModel().getCopy();
		clipNetworkModel.removeNetworkComponentsInverse(networkComponentsForClipboard);
		clipNetworkModel.resetGraphElementLayout();

		this.setClipboardNetworkModel(clipNetworkModel);
	}

	/**
	 * Sets a NetworkModel to the clipboard.
	 * 
	 * @param newClipboardNetworkModel the new clipboard network model
	 */
	public void setClipboardNetworkModel(NetworkModel newClipboardNetworkModel) {
		this.clipboardNetworkModel = newClipboardNetworkModel;
	}

	/**
	 * Returns the NetworkModel from the clipboard .
	 * 
	 * @return the clipboard NetworkModel
	 */
	public NetworkModel getClipboardNetworkModel() {
		return clipboardNetworkModel;
	}

	/**
	 * This method handles the SimulationSetupChangeNotifications sent from the project
	 * 
	 * @param sscn The SimulationSetupChangeNotifications to handle
	 */
	@Override
	protected void handleSimSetupChange(SimulationSetupNotification sscn) {

		switch (sscn.getUpdateReason()) {
		case SIMULATION_SETUP_LOAD:
			this.updateGraphFileName();
			this.loadEnvironment(); // Loads network model and notifies observers
			break;

		case SIMULATION_SETUP_SAVED:
			this.saveEnvironment();
			break;

		case SIMULATION_SETUP_ADD_NEW:
			this.updateGraphFileName();
			this.setDisplayEnvironmentModel(null);
			// --- Register a new list of agents that has to be started with the environment ------
			this.setAgents2Start(new DefaultListModel<AgentClassElement4SimStart>());
			this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
			break;

		case SIMULATION_SETUP_COPY:
			this.updateGraphFileName();
			this.saveEnvironment();
			this.getProject().setUnsaved(true);
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
			this.updateGraphFileName();
			break;

		case SIMULATION_SETUP_RENAME:
			File oldGraphFile = this.getFileGraphML();
			File oldComponentFile = this.getFileXML();
			this.updateGraphFileName();
			if (oldGraphFile.exists()) {
				oldGraphFile.renameTo(this.getFileGraphML());
			}
			if (oldComponentFile.exists()) {
				oldComponentFile.renameTo(this.getFileXML());
			}
			break;

		case SIMULATION_SETUP_PREPARE_SAVING:
			// --- Nothing to do here ---------------------
			break;
		}

	}

	/**
	 * This method sets the baseFileName property and the SimulationSetup's environmentFileName according to the current SimulationSetup
	 */
	private void updateGraphFileName() {
		this.baseFileName = this.getProject().getSimulationSetupCurrent();
		this.getCurrentSimulationSetup().setEnvironmentFileName(baseFileName + ".graphml");
	}

	/**
	 * Returns the XML file for the current NetworkModel.
	 * 
	 * @return the XML file
	 */
	public File getFileXML() {
		return new File(this.getEnvFolderPath() + this.baseFileName + ".xml");
	}

	/**
	 * Returns the GraphML file for the current NetworkModel.
	 * 
	 * @return the GraphML file
	 */
	public File getFileGraphML() {
		return new File(this.getEnvFolderPath() + this.baseFileName + ".graphml");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.environment.EnvironmentController#loadEnvironment()
	 */
	@Override
	protected void loadEnvironment() {

		if (this.currControllerAction==ControllerAction.Initializing) return;
		
		final String fileName = this.getCurrentSimulationSetup().getEnvironmentFileName();
		if (fileName != null) {

			this.isTemporaryPreventSaving = true;
			
			Thread envLoader = new Thread(new Runnable() {
				@Override
				public void run() {

					try {

						// --- Set application status text ----------------------------------------
						if (Application.getMainWindow() != null) {
							Application.getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							Application.setStatusBarMessage(Language.translate("Lade Setup") + " :" + fileName + " ...");
						}
	
						// --- Register agents that have to be started with the environment -------
						GraphEnvironmentController.this.setAgents2Start(new DefaultListModel<AgentClassElement4SimStart>());
						GraphEnvironmentController.this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);

						// --- Define the NetworkModel --------------------------------------------
						NetworkModel netModel = new NetworkModel();

						// --- 1. Load the graph topology from the graph file ---------------------
						netModel.loadGraphFile(new File(getEnvFolderPath() + fileName));
						
						// --- 2. Load the component definitions from the component file ----------
						File componentsFile = new File(GraphEnvironmentController.this.getEnvFolderPath() + File.separator + baseFileName + ".xml");
						netModel.loadComponentsFile(componentsFile);
						
						// --- 3. Load component type settings from file --------------------------
						GeneralGraphSettings4MAS ggs4MAS = GraphEnvironmentController.this.loadGeneralGraphSettings();
						// --- Remind the list of custom toolbar elements -------------------------
						if (GraphEnvironmentController.this.getNetworkModel() != null) {
							GeneralGraphSettings4MAS gg4mas = GraphEnvironmentController.this.getGeneralGraphSettings4MAS();
							if (gg4mas!=null) {
								ggs4MAS.setCustomToolbarComponentDescriptions(gg4mas.getCustomToolbarComponentDescriptions());
							}
						}
						netModel.setGeneralGraphSettings4MAS(ggs4MAS);
						
						// --- Use case 'Application' ---------------------------------------------
						if (Application.getGlobalInfo().getExecutionMode()==ExecutionMode.APPLICATION) {
							// --- Wait for visualization component before assign network model ---
							while (GraphEnvironmentController.this.getEnvironmentPanel()==null) {
								Thread.sleep(50);
							}
							BasicGraphGui basicGraphGui = GraphEnvironmentController.this.getGraphEnvironmentControllerGUI().getBasicGraphGuiRootJSplitPane().getBasicGraphGui();
							while (basicGraphGui.isCreatedVisualizationViewer()==false) {
								Thread.sleep(50);
							}	
						}
						
						// --- Assign NetworkMoldel to visualization ------------------------------
						final NetworkModel netModelAssigen = netModel;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								// --- Use the local method in order to inform the observer -------
								GraphEnvironmentController.this.setDisplayEnvironmentModel(netModelAssigen);
								// --- Decode data models that are Base64 encoded in the moment ---
								GraphEnvironmentController.this.setNetworkComponentDataModelBase64Decoded();
							}
						});
						
						
					} catch (Exception ex) {
						ex.printStackTrace();
						
					} finally {
						GraphEnvironmentController.this.isTemporaryPreventSaving = false;
						Application.getMainWindow().setCursor(Cursor.getDefaultCursor());
					}
					
				}
			});
			envLoader.setName("GraphEnvrionmentLoader");
			envLoader.start();

		}

		// --- Reset Undo-Manager -----------------------------------------------------------------
		GraphEnvironmentController.this.getNetworkModelAdapter().getUndoManager().discardAllEdits();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.environment.EnvironmentController#saveEnvironment()
	 */
	@Override
	protected void saveEnvironment() {

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
			
			File graphFile = this.getFileGraphML();
			this.getNetworkModel().saveGraphFile(graphFile);
			
			File componentsFile = this.getFileXML();
			this.getNetworkModel().saveComponentsFile(componentsFile);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.environment.EnvironmentController#setEnvironmentModel(java.lang.Object)
	 */
	@Override
	public void setDisplayEnvironmentModel(DisplaytEnvironmentModel displaytEnvironmentModel) {
		try {
			if (displaytEnvironmentModel == null) {
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
	 * 
	 * @see agentgui.core.environment.EnvironmentController#getEnvironmentModel()
	 */
	@Override
	public DisplaytEnvironmentModel getDisplayEnvironmentModel() {
		return this.getNetworkModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.environment.EnvironmentController#getEnvironmentModelCopy()
	 */
	@Override
	public DisplaytEnvironmentModel getDisplayEnvironmentModelCopy() {
		return this.getNetworkModel().getCopy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.environment.EnvironmentController#setAbstractEnvironmentModel(java.lang.Object)
	 */
	@Override
	public void setAbstractEnvironmentModel(AbstractEnvironmentModel abstractEnvironmentModel) {
		this.abstractEnvironmentModel = abstractEnvironmentModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.environment.EnvironmentController#getAbstractEnvironmentModel()
	 */
	@Override
	public AbstractEnvironmentModel getAbstractEnvironmentModel() {
		return this.abstractEnvironmentModel;
	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * Load general graph settings.
	 */
	private GeneralGraphSettings4MAS loadGeneralGraphSettings() {
		File componentFile = new File(getEnvFolderPath() + GeneralGraphSettings4MASFile + ".xml");
		return GeneralGraphSettings4MAS.load(componentFile);
	}

	/**
	 * Save general graph settings.
	 */
	private void saveGeneralGraphSettings() {
		File componentFile = new File(getEnvFolderPath() + GeneralGraphSettings4MASFile + ".xml");
		GeneralGraphSettings4MAS.save(componentFile, this.getGeneralGraphSettings4MAS());
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
		Collection<String> compNameCollection = this.getNetworkModel().getNetworkComponents().keySet();
		String[] compNames = compNameCollection.toArray(new String[compNameCollection.size()]);
		for (int i = 0; i < compNames.length; i++) {
			// --- Current component ------------------------------------------
			String compName = compNames[i];
			NetworkComponent comp = this.getNetworkModel().getNetworkComponent(compName);

			if (!(comp instanceof ClusterNetworkComponent)) {
				// ----------------------------------------------------------------
				// --- Validate current component against ComponentTypeSettings ---
				ComponentTypeSettings ctsSingle = cts.get(comp.getType());
				if (ctsSingle == null) {
					// --- remove this component ---
					this.getNetworkModel().removeNetworkComponent(comp);
					comp = null;

				} else {

					if (ctsSingle.getAgentClass() == null) {
						comp.setAgentClassName(null);

					} else if (comp.getAgentClassName() == null) {
						// --- Correct this entry -------
						comp.setAgentClassName(ctsSingle.getAgentClass());

					} else if (comp.getAgentClassName().equals(ctsSingle.getAgentClass()) == false) {
						// --- Correct this entry -------
						comp.setAgentClassName(ctsSingle.getAgentClass());
					}
					if (comp.getPrototypeClassName().equals(ctsSingle.getGraphPrototype()) == false) {
						// --- Correct this entry -------
						// TODO change the graph elements if needed
						comp.setPrototypeClassName(ctsSingle.getGraphPrototype());
					}
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
		this.getNetworkModelAdapter().refreshNetworkModel();
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
		if (ace4s.getAgentClassReference().equals(comp.getAgentClassName()) == false) {
			return false;
		}
		if (ace4s.getStartAsName().equals(comp.getId()) == false) {
			return false;
		}
		return true;
	}

	/**
	 * Adds an agent to the start list corresponding to the current network component .
	 * 
	 * @param networkComponent the NetworkComponent
	 */
	public void addAgent(NetworkComponent networkComponent) {

		if (networkComponent == null) {
			return;
		}
		Class<? extends Agent> agentClass = this.getAgentClass(networkComponent.getAgentClassName());
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

		// Renaming the agent in the agent start list of the simulation setup
		int i = 0;
		for (i = 0; i < getAgents2Start().size(); i++) {
			AgentClassElement4SimStart ac4s = (AgentClassElement4SimStart) getAgents2Start().get(i);
			if (ac4s.getStartAsName().equals(oldCompID)) {
				ac4s.setStartAsName(newCompID);
				break;
			}
		}

	}

	/**
	 * Returns the agent class.
	 * 
	 * @param agentReference the agent reference
	 * @return the agent class
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends Agent> getAgentClass(String agentReference) {

		if (agentReference == null || agentReference.equals("")) {
			return null;
		}

		Class<? extends Agent> agentClass = null;
		try {
			agentClass = (Class<? extends Agent>) ClassLoadServiceUtility.forName(agentReference);
		} catch (ClassNotFoundException ex) {
			System.err.println("Could not find agent class '" + agentReference + "'");
		}
		return agentClass;
	}

	/**
	 * Sets all Base64 encoded data models to concrete instances.
	 */
	public void setNetworkComponentDataModelBase64Decoded() {
		new DataModelEnDecoderThread(this, OrganizerAction.ORGANIZE_DECODE_64).start();
	}

	/**
	 * Sets the instances of all data models to a Base64 encoded strings.
	 */
	public void setNetworkComponentDataModelBase64Encoded() {
		new DataModelEnDecoderThread(this, OrganizerAction.ORGANIZE_ENCODE_64).start();
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
					if (actionOnTopIsRunning == false) {
						basicGraphGuiVisViewer.repaint();
						basicGraphGuiVisViewer.requestFocus();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Return all known import adapter.
	 * @return the import adapter
	 */
	public Vector<NetworkModelFileImporter> getImportAdapter() {
		if (this.importAdapter == null) {
			this.importAdapter = new Vector<NetworkModelFileImporter>();
		}
		return this.importAdapter;
	}

	/**
	 * Adds a {@link CustomToolbarComponentDescription}, so that customized components will be added to a toolbar of the {@link BasicGraphGui}.
	 * @param customButtonDescription the CustomToolbarComponentDescription to add
	 */
	public void addCustomToolbarComponentDescription(CustomToolbarComponentDescription customButtonDescription) {
		if (this.getNetworkModel()!=null && this.getNetworkModel().getGeneralGraphSettings4MAS()!=null) {
			this.getNetworkModel().getGeneralGraphSettings4MAS().getCustomToolbarComponentDescriptions().add(customButtonDescription);
			this.getNetworkModelAdapter().addCustomToolbarComponentDescription(customButtonDescription);
		} else {
			String errMsg = Language.translate("Could not add custom button: No NetworkModel was defined yet!", Language.EN);
			System.err.println(errMsg);
		}
	}

}