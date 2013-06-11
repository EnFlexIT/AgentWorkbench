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

import jade.core.Agent;

import java.awt.Cursor;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections15.Transformer;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.gui.ProgressMonitor;
import agentgui.core.project.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.core.sim.setup.SimulationSetups;
import agentgui.core.sim.setup.SimulationSetupsChangeNotification;
import agentgui.envModel.graph.controller.yedGraphml.YedGraphMLFileImporter;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;
import agentgui.envModel.graph.networkModel.NetworkComponentList;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelAdapter;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;

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

    /** Separator used for the local graphMLWriter **/
    private static final String GraphML_NewLine = System.getProperty("line.separator");  
    private static final String GraphML_VectorBrackets = "[conent]";

	/** The key string used for saving the position in the GraphML file */
    private static final String KEY_POSITION_PROPERTY = "pos";
    /** The key string used for saving the ontology representation in the GraphML file */
    private static final String KEY_DATA_MODEL_BASE64_PROPERTY = "dataModelVectorBase64Encoded";
    /** Custom user object to be placed in the project object. Used here for storing the current component type settings. */
    private static final String generalGraphSettings4MASFile = "~GeneralGraphSettings~";

    /** The base file name used for saving the graph and the components (without suffix) */
    private String baseFileName = null;
    /** The GraphMLWriter used to save the graph */
    private GraphMLWriter<GraphNode, GraphEdge> graphMLWriter = null;
    /** Known adapter for the import of network models */
    private Vector<NetworkModelFileImporter> importAdapter = new Vector<NetworkModelFileImporter>();

    /** The network model currently loaded */
    private NetworkModel networkModel = null;
    private NetworkModelAdapter networkModelAdapter = new NetworkModelAdapter(this);
    
    /** The NetworkModel that is stored in the clipboard */
    private NetworkModel clipboardNetworkModel = null;
    
    /** The abstract environment model is just an open slot, where individual things can be placed. */
    private Object abstractEnvironmentModel = null;
    
    
    /**
     * The constructor for the GraphEnvironmentController for displaying the current environment 
     * model during a running simulation. Use {@link #setDisplayEnvironmentModel(Object)}, in order to 
     * set the current {@link NetworkModel}.
     */
    public GraphEnvironmentController() {
    }

    /**
     * The constructor for the GraphEnvironmentController for configurations within Agent.GUI
     * @param project The current project
     */
    public GraphEnvironmentController(Project project) {
		super(project);
		if (this.getProject()!=null) {
		    this.updateGraphFileName();
		    this.loadEnvironment();
		}
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
     * Gets the GraphEnvironmentControllerGUI.
     * @return the current GraphEnvironmentControllerGUI
     */
    public GraphEnvironmentControllerGUI getGraphEnvironmentControllerGUI() {
    	return (GraphEnvironmentControllerGUI) this.getEnvironmentPanel();
    }
    
    /**
     * Sets the project unsaved.
     */
    public void setProjectUnsaved() {
    	if (this.getProject()!=null) {
		    this.getProject().setUnsaved(true);
		}
    }
    
    /* (non-Javadoc)
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    @Override
    public void notifyObservers(Object arg) {
    	this.setChanged();
    	super.notifyObservers(arg);
    }
    
    /**
     * Gets the general graph settings4 mas.
     * @return the general graph settings4 mas
     */
    public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
    	return this.networkModel.getGeneralGraphSettings4MAS();
    }
    /**
     * Gets the current ComponentTypeSettings
     * @return HashMap<String, ComponentTypeSettings> The current component type settings map.
     */
    public HashMap<String, ComponentTypeSettings> getComponentTypeSettings() {
    	return this.getGeneralGraphSettings4MAS().getCurrentCTS();
    }
    /**
     * Returns the DomainSttings.
     * @return the DomainSttings
     */
    public HashMap<String, DomainSettings> getDomainSettings() {
    	return this.getGeneralGraphSettings4MAS().getDomainSettings();
    }
    
    /**
     * Sets the environment network model
     * @return NetworkModel - The environment model
     */
    private void setNetworkModel(NetworkModel newNetworkModel) {
		// --- Remind the GeneralGraphSettings4MAS ----------------------------
    	GeneralGraphSettings4MAS generalGraphSettings4MAS = null;
		if (this.networkModel!=null) {
			generalGraphSettings4MAS = this.networkModel.getGeneralGraphSettings4MAS();	
		}
		// --- Set NetworkModel -----------------------------------------------
    	if (newNetworkModel==null) {
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
     * @return NetworkModel - The environment model
     */
    public NetworkModel getNetworkModel() {
    	return networkModel;
    }
	/**
     * Gets the network model for actions.
     * @return the network model action
     */
    public NetworkModelAdapter getNetworkModelAdapter() {
    	if (networkModelAdapter==null) {
    		this.networkModelAdapter = new NetworkModelAdapter(this);
    	}
    	return this.networkModelAdapter;
    }
    
    /**
     * Copies  to clipboard.
     *
     * @param sourceNetworkModel the source network model
     * @param networkComponentsForClipboard the network components
     */
    public void copyToClipboard(HashSet<NetworkComponent> networkComponentsForClipboard) {
    	
    	if (networkComponentsForClipboard==null) return;
    	if (this.getNetworkModel()==null) return;

    	// ---------- Prepare for the Clipboard ---------------------
		NetworkModel clipNetworkModel = this.getNetworkModel().getCopy();
		clipNetworkModel.setAlternativeNetworkModel(null);
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

	/**
     * This method handles the SimulationSetupChangeNotifications sent from the project
     * @param sscn The SimulationSetupChangeNotifications to handle
     */
    @Override
    protected void handleSimSetupChange(SimulationSetupsChangeNotification sscn) {

		switch (sscn.getUpdateReason()) {
		case SimulationSetups.SIMULATION_SETUP_LOAD:
		    this.updateGraphFileName();
		    this.loadEnvironment(); // Loads network model and notifies observers
		    break;
	
		case SimulationSetups.SIMULATION_SETUP_SAVED:
		    this.saveEnvironment();
		    break;
	
		case SimulationSetups.SIMULATION_SETUP_ADD_NEW:
		    
			GeneralGraphSettings4MAS generalGraphSettings4MAS = null;
			if (this.networkModel!=null) {
				generalGraphSettings4MAS = this.networkModel.getGeneralGraphSettings4MAS();	
			}
			
			this.updateGraphFileName();
		    networkModel = new NetworkModel();
		    networkModel.setGeneralGraphSettings4MAS(generalGraphSettings4MAS);
		    this.setDisplayEnvironmentModel(this.networkModel);
		    
		    // --- register a new list of agents, which has to be started with the environment ------
		    this.setAgents2Start(new DefaultListModel());
		    this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
	
		    break;
	
		case SimulationSetups.SIMULATION_SETUP_COPY:
		    this.updateGraphFileName();
		    this.saveEnvironment();
		    this.getProject().setUnsaved(true);
		    break;
	
		case SimulationSetups.SIMULATION_SETUP_REMOVE:
		    File graphFile = new File(getEnvFolderPath() + baseFileName + ".graphml");
		    if (graphFile.exists()) {
		    	graphFile.delete();
		    }
	
		    File componentFile = new File(getEnvFolderPath() + baseFileName + ".xml");
		    if (componentFile.exists()) {
		    	componentFile.delete();
		    }
		    updateGraphFileName();
		    break;
	
		case SimulationSetups.SIMULATION_SETUP_RENAME:
		    File oldGraphFile = new File(getEnvFolderPath() + baseFileName + ".graphml");
		    File oldComponentFile = new File(getEnvFolderPath() + baseFileName + ".xml");
		    updateGraphFileName();
		    if (oldGraphFile.exists()) {
				File newGraphFile = new File(getEnvFolderPath() + baseFileName + ".graphml");
				oldGraphFile.renameTo(newGraphFile);
		    }
		    if (oldComponentFile.exists()) {
				File newComponentFile = new File(getEnvFolderPath() + baseFileName + ".xml");
				oldComponentFile.renameTo(newComponentFile);
		    }
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

    /*
     * (non-Javadoc)
     * @see EnvironmentController#loadEnvironment()
     */
    @Override
    protected void loadEnvironment() {

    	this.networkModel = new NetworkModel();
    	String fileName = getCurrentSimulationSetup().getEnvironmentFileName();
    	
    	Application.getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    	Application.setStatusBar(Language.translate("Lade Setup") + " :" + fileName + " ...");
    	
		if (fileName != null) {
	
		    // --- register the list of agents, which has to be started with the environment ------
		    this.setAgents2Start(new DefaultListModel());
		    this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
	
		    // --- Load the graph topology from the graph file ------------------------------------
		    File graphFile = new File(getEnvFolderPath() + fileName);
		    if (graphFile.exists()) {
		    	baseFileName = fileName.substring(0, fileName.lastIndexOf('.'));
				try {
				    // Load graph topology
				    this.networkModel.setGraph(getGraphMLReader(graphFile).readGraph());
		
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (GraphIOException e) {
				    e.printStackTrace();
				}
		    }
	
		    // --- Load the component definitions from the component file -------------------------
		    File componentFile = new File(getEnvFolderPath() + File.separator + baseFileName + ".xml");
		    if (componentFile.exists()) {
				try {
				    FileReader componentReader = new FileReader(componentFile);
		
				    JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
				    Unmarshaller unmarsh = context.createUnmarshaller();
				    NetworkComponentList compList = (NetworkComponentList) unmarsh.unmarshal(componentReader);
				    this.networkModel.setNetworkComponents(compList.getComponentList());
		
				    componentReader.close();
		
				} catch (JAXBException e) {
				    e.printStackTrace();
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (IOException e) {
				    e.printStackTrace();
				}
		    }
		}
	
		// --- Loading component type settings from the simulation setup --------------------------
		this.loadGeneralGraphSettings();
	
		// --- Use the local method in order to inform the observer -------------------------------
		this.setDisplayEnvironmentModel(this.networkModel);

		// --- Decode the data models that are Base64 encoded in the moment -----------------------  
		this.setNetworkComponentDataModelBase64Decoded();
		
		// --- Reset Undo-Manager -----------------------------------------------------------------
		this.networkModelAdapter.getUndoManager().discardAllEdits();

		Application.setStatusBar(Language.translate("Fertig"));
		Application.getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
    }

    /**
     * Clean up / correct list of agents corresponding to the current NetworkModel.
     */
    public void validateNetworkComponentAndAgents2Start() {

		// --------------------------------------------------------------------
		// --- Get the current ComponentTypeSettings --------------------------
		HashMap<String, ComponentTypeSettings> cts = this.networkModel.getGeneralGraphSettings4MAS().getCurrentCTS();
	
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
		Collection<String> compNameCollection = this.networkModel.getNetworkComponents().keySet();
		String[] compNames = compNameCollection.toArray(new String[compNameCollection.size()]);
		for (int i = 0; i < compNames.length; i++) {
		    // --- Current component ------------------------------------------
		    String compName = compNames[i];
		    NetworkComponent comp = this.networkModel.getNetworkComponent(compName);
	
		    if (!(comp instanceof ClusterNetworkComponent)) {
			    // ----------------------------------------------------------------
			    // --- Validate current component against ComponentTypeSettings ---
			    ComponentTypeSettings ctsSingle = cts.get(comp.getType());
			    if (ctsSingle == null) {
					// --- remove this component ---
					this.networkModel.removeNetworkComponent(comp);
					comp = null;
		
			    } else {
			    	
			    	if (ctsSingle.getAgentClass()==null) {
			    		comp.setAgentClassName(null);
			    		
			    	} else  if (comp.getAgentClassName()==null) {
			    		// --- Correct this entry -------
			    		comp.setAgentClassName(ctsSingle.getAgentClass());
			    		
			    	} else  if (comp.getAgentClassName().equals(ctsSingle.getAgentClass()) == false) {
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
     * @param networkComponent the NetworkComponent
     */
    public void removeAgent(NetworkComponent networkComponent){
    	
    	if (networkComponent == null) {
			return;
		}
    	
    	String search4 = networkComponent.getId();
    	DefaultListModel agentList = this.getAgents2Start();
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
		
    	if (agentReference==null || agentReference.equals("")) {
    		return null;
    	}
    	
    	Class<? extends Agent> agentClass = null;
		try {
		    agentClass = (Class<? extends Agent>) Class.forName(agentReference);
		} catch (ClassNotFoundException ex) {
		    System.err.println("Could not find agent class '" + agentReference + "'");
		}
		return agentClass;
    }

    /*
     * (non-Javadoc)
     * 
     * @see EnvironmentController#saveEnvironment()
     */
    @Override
    protected void saveEnvironment() {

		this.validateNetworkComponentAndAgents2Start();
		this.saveGeneralGraphSettings();
		if (networkModel != null && networkModel.getGraph() != null) {
			
		    try {
				// Save the graph topology
				String graphFileName = baseFileName + ".graphml";
				File file = new File(getEnvFolderPath() + graphFileName);
				if (!file.exists()) {
				    file.createNewFile();
				}
				PrintWriter pw = new PrintWriter(new FileWriter(file));
				getGraphMLWriter().save(networkModel.getGraph(), pw);
		
				// Save the network component definitions
				File componentFile = new File(getEnvFolderPath() + baseFileName + ".xml");
				if (!componentFile.exists()) {
				    componentFile.createNewFile();
				}
				FileWriter componentFileWriter = new FileWriter(componentFile);
		
				JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
				Marshaller marsh = context.createMarshaller();
				marsh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marsh.marshal(new NetworkComponentList(networkModel.getNetworkComponents()), componentFileWriter);
		
				componentFileWriter.close();
	
				
		    } catch (IOException e) {
		    	e.printStackTrace();
		    } catch (JAXBException e) {
		    	e.printStackTrace();
		    }
		}
    }

    /*
     * (non-Javadoc)
     * @see agentgui.core.environment.EnvironmentController#setEnvironmentModel(java.lang.Object)
     */
    @Override
    public void setDisplayEnvironmentModel(Object environmentObject) {
		try {
		    if (environmentObject == null) {
		    	this.setNetworkModel(null);
		    } else {
		    	this.setNetworkModel((NetworkModel) environmentObject);
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
    public Object getDisplayEnvironmentModel() {
    	return this.networkModel;
    }

    /*
     * (non-Javadoc)
     * @see agentgui.core.environment.EnvironmentController#getEnvironmentModelCopy()
     */
    @Override
    public Object getDisplayEnvironmentModelCopy() {
		NetworkModel netModel = this.networkModel.getCopy();
		return netModel;
    }
    
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#setAbstractEnvironmentModel(java.lang.Object)
	 */
	@Override
	public void setAbstractEnvironmentModel(Object abstractEnvironmentModel) {
		this.abstractEnvironmentModel = abstractEnvironmentModel;
	}
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getAbstractEnvironmentModel()
	 */
	@Override
	public Object getAbstractEnvironmentModel() {
		return this.abstractEnvironmentModel;
	}
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getAbstractEnvironmentModelCopy()
	 */
	@Override
	public Object getAbstractEnvironmentModelCopy() {
		return this.abstractEnvironmentModel;
	}

	
    /**
     * Load general graph settings.
     */
    private void loadGeneralGraphSettings() {

		try {
		    File componentFile = new File(getEnvFolderPath() + generalGraphSettings4MASFile + ".xml");
		    FileReader componentReader = new FileReader(componentFile);
	
		    JAXBContext context = JAXBContext.newInstance(GeneralGraphSettings4MAS.class);
		    Unmarshaller unmarsh = context.createUnmarshaller();
		    GeneralGraphSettings4MAS ggs4MAS = (GeneralGraphSettings4MAS) unmarsh.unmarshal(componentReader);
		    this.networkModel.setGeneralGraphSettings4MAS(ggs4MAS);
		    componentReader.close();
	
		} catch (JAXBException ex) {
		    ex.printStackTrace();
		} catch (FileNotFoundException ex) {
		    // create the file for the GeneralGraphSettings
			this.saveGeneralGraphSettings();
			System.out.println("Created file " + getEnvFolderPath() + generalGraphSettings4MASFile + ".xml");
			
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
    }

    /**
     * Save general graph settings.
     */
    private void saveGeneralGraphSettings() {

		try {
		    File componentFile = new File(getEnvFolderPath() + generalGraphSettings4MASFile + ".xml");
		    if (!componentFile.exists()) {
		    	componentFile.createNewFile();
		    }
	
		    FileWriter componentFileWriter = new FileWriter(componentFile);
	
		    JAXBContext context = JAXBContext.newInstance(GeneralGraphSettings4MAS.class);
		    Marshaller marsh = context.createMarshaller();
		    marsh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		    marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		    marsh.marshal(this.networkModel.getGeneralGraphSettings4MAS(), componentFileWriter);
	
		    componentFileWriter.close();
	
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (JAXBException e) {
		    e.printStackTrace();
		}

    }
    
    /**
     * Gets the known import adapter.
     * @return the import adapter
     */
    public Vector<NetworkModelFileImporter> getImportAdapter() {
    	if (this.importAdapter.size()==0) {
    		// --- Here the default import adapter are defined ------ 
    		this.importAdapter.add(new YedGraphMLFileImporter(this, "graphml", "yEd GraphML"));
    	}
    	return this.importAdapter;
    }
    
    /**
     * Gets the GraphMLWriter, creates and initiates a new instance if null
     * @return The GraphMLWriter
     */
    private GraphMLWriter<GraphNode, GraphEdge> getGraphMLWriter() {

		if (graphMLWriter == null) {
		    graphMLWriter = new GraphMLWriter<GraphNode, GraphEdge>();
		    graphMLWriter.setEdgeIDs(new Transformer<GraphEdge, String>() {
				@Override
				public String transform(GraphEdge arg0) {
				    return arg0.getId();
				}
		    });
		    graphMLWriter.setEdgeDescriptions(new Transformer<GraphEdge, String>() {
				@Override
				public String transform(GraphEdge graphEdge) {
				    return graphEdge.getComponentType();
				}
		    });
		    graphMLWriter.setVertexIDs(new Transformer<GraphNode, String>() {
				@Override
				public String transform(GraphNode graphNode) {
				    return graphNode.getId();
				}
		    });
		    graphMLWriter.addVertexData(KEY_POSITION_PROPERTY, "position", "", new Transformer<GraphNode, String>() {
				@Override
				public String transform(GraphNode graphNode) {
				    String pos = graphNode.getPosition().getX() + ":" + graphNode.getPosition().getY();
				    return pos;
				}
		    });
		    graphMLWriter.addVertexData(KEY_DATA_MODEL_BASE64_PROPERTY, "Base64 encoded individual data model", "", new Transformer<GraphNode, String>() {
				@Override
				public String transform(GraphNode graphNode) {
					String dmBase64StringSave = null;
					if (graphNode.getDataModelBase64()!=null) {
						for (String dmBase64String : graphNode.getDataModelBase64()) {
							dmBase64String = GraphML_VectorBrackets.replace("conent", dmBase64String);
							if (dmBase64StringSave==null) {
								dmBase64StringSave = dmBase64String;
							} else {
								dmBase64StringSave = dmBase64StringSave + GraphML_NewLine + dmBase64String;
							}
						}
					}
					if (dmBase64StringSave!=null) {
						dmBase64StringSave = GraphML_NewLine + dmBase64StringSave + GraphML_NewLine;
					}
					return dmBase64StringSave;
				}
		    });
	
		}
		return graphMLWriter;
    }

    /**
     * Creates a new GraphMLReader2 and initiates it with the GraphML file to be loaded
     * 
     * @param file The file to be loaded
     * @return The GraphMLReader2
     * @throws FileNotFoundException
     */
    private GraphMLReader2<Graph<GraphNode, GraphEdge>, GraphNode, GraphEdge> getGraphMLReader(File file) throws FileNotFoundException {

		Transformer<GraphMetadata, Graph<GraphNode, GraphEdge>> graphTransformer = new Transformer<GraphMetadata, Graph<GraphNode, GraphEdge>>() {
		    @Override
		    public SparseGraph<GraphNode, GraphEdge> transform(GraphMetadata gmd) {
			return new SparseGraph<GraphNode, GraphEdge>();
		    }
		};
	
		Transformer<NodeMetadata, GraphNode> nodeTransformer = new Transformer<NodeMetadata, GraphNode>() {
		    @Override
		    public GraphNode transform(NodeMetadata nmd) {

				// --- Create GraphNode instance and set ID ---------
		    	GraphNode graphNode = new GraphNode();
				graphNode.setId(nmd.getId());

				// --- Load the individual data model ---------------
		    	String dmBase64StringSaved = nmd.getProperty(KEY_DATA_MODEL_BASE64_PROPERTY);
		    	if (dmBase64StringSaved!=null) {
		    		Vector<String> base64Vector = new Vector<String>();
		    		while (dmBase64StringSaved.contains("]")) {
		    			int cutAtOpen = dmBase64StringSaved.indexOf("[")+1;
		    			int cutAtClose = dmBase64StringSaved.indexOf("]");
			    		String singleString = dmBase64StringSaved.substring(cutAtOpen, cutAtClose);
			    		base64Vector.add(singleString);
			    		dmBase64StringSaved = dmBase64StringSaved.substring(cutAtClose+1);
			    	}
			    	if (base64Vector.size()>0) {
			    		graphNode.setDataModelBase64(base64Vector);	
			    	}
		    	}

		    	// --- Set the position of the node -----------------
				Point2D pos = null;
				String posString = nmd.getProperty(KEY_POSITION_PROPERTY);
				if (posString!=null) {
				    String[] coords = posString.split(":");
				    if (coords.length == 2) {
						double xPos = Double.parseDouble(coords[0]);
						double yPos = Double.parseDouble(coords[1]);
						pos = new Point2D.Double(xPos, yPos);
				    }
				}
				if (pos == null) {
				    System.err.println("Keine Position definiert für Knoten " + nmd.getId());
				    pos = new Point2D.Double(0, 0);
				}
				graphNode.setPosition(pos);
				return graphNode;
		    }
		};
	
		Transformer<EdgeMetadata, GraphEdge> edgeTransformer = new Transformer<EdgeMetadata, GraphEdge>() {
		    @Override
		    public GraphEdge transform(EdgeMetadata emd) {
		    	return new GraphEdge(emd.getId(), emd.getDescription());
		    }
		};
	
		Transformer<HyperEdgeMetadata, GraphEdge> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, GraphEdge>() {
		    @Override
		    public GraphEdge transform(HyperEdgeMetadata arg0) {
		    	return null;
		    }
		};
	
		FileReader fileReader = new FileReader(file);
	
		return new GraphMLReader2<Graph<GraphNode, GraphEdge>, GraphNode, GraphEdge>(fileReader, graphTransformer, nodeTransformer, edgeTransformer, hyperEdgeTransformer);

    }
    
    /**
     * Sets the network component Base64 encoded data models to concrete instances.
     */
    public void setNetworkComponentDataModelBase64Decoded() {
    	
    	final Long displayTime = System.currentTimeMillis() + new Long(1000);
    	final GraphEnvironmentController graphController = this;
    	
    	String title = Language.translate("Initiating network components", Language.EN);
    	String header = Language.translate("Initiating network components and setting data model", Language.EN);
    	String progress = Language.translate("Reading", Language.EN) + "...";
    	
    	final ProgressMonitor pm = new ProgressMonitor(title, header, progress);
    	pm.setAllow2Cancel(false);
    	
    	Runnable decode = new Runnable() {
    		public void run() {
		    	
				long nextGraphRenderingInterval = 500; // ms
				long nextGraphRendering = System.currentTimeMillis() + nextGraphRenderingInterval;
					
		    	Object[] netCompArr =  getNetworkModel().getNetworkComponents().values().toArray();
		    	Object[] graphNodeArr =  getNetworkModel().getGraph().getVertices().toArray();
		    	
		    	int progressIntOld = -1;
		    	int netCompCount = netCompArr.length + graphNodeArr.length;
		    	for (int i=0; i<netCompCount; i++) {
		    	
		    		try {
		    			// --- Only display progress, if procedure is to long -----
			    		if (System.currentTimeMillis() > displayTime) {
			    			if (pm.isVisible()==false) {
			    				pm.setVisible(true);
			    		    	pm.validate();
			    		    	pm.repaint();
			    			}
			    			// --- Set Progress monitor ---------------------------
				    		float progressCalc = (float) (((float)i/(float)netCompCount) * 100.0);
				    		final int progressInt = Math.round(progressCalc);
				    		if (progressInt!=progressIntOld) {
				    			progressIntOld = progressInt;
								pm.setProgress(progressInt);
				    		}
			    		}

			    		// --- Render/paint graph ---------------------------------
			    		if (System.currentTimeMillis()>=nextGraphRendering) {
			    			setBasicGraphGuiVisViewerActionOnTop(false);
			    			setBasicGraphGuiVisViewerActionOnTop(true);
			    			nextGraphRendering = System.currentTimeMillis() + nextGraphRenderingInterval;
			    		}
			    		
			    		// --- Find the corresponding NetworkComponentAdapter -----
			    		NetworkComponentAdapter netCompAdapter=null;
			    		NetworkComponent netComp = null;
			    		GraphNode graphNode = null;
			    		if (i<netCompArr.length) {
			    			netComp = (NetworkComponent) netCompArr[i];		
			    			netCompAdapter = getNetworkModel().getNetworkComponentAdapter(graphController, netComp);
			    		} else {
			    			graphNode = (GraphNode) graphNodeArr[i-netCompArr.length];
			    			netCompAdapter = getNetworkModel().getNetworkComponentAdapter(graphController, graphNode);	
			    		}
			    		
			    		// --- Set the components data model instance -------------
			    		if (netCompAdapter!=null) {
			    			Vector<String> dataModelBase64 = null;
			    			if (graphNode!=null) {
			    				dataModelBase64 = graphNode.getDataModelBase64();
			    			} else {
			    				dataModelBase64 = netComp.getDataModelBase64();
			    			}
			    			if (dataModelBase64!=null) {
			    				// --- Get DataModelAdapter -----------------------
			    				NetworkComponentAdapter4DataModel netCompDataModelAdapter = netCompAdapter.getStoredDataModelAdapter();
			    				if (netCompDataModelAdapter!=null) {
			    					// --- Get Base64 decoded Object --------------
			    					Object dataModel = netCompDataModelAdapter.getDataModelBase64Decoded(dataModelBase64, true);
			    					if (graphNode!=null) {
					    				graphNode.setDataModel(dataModel);
					    			} else {
					    				netComp.setDataModel(dataModel);
					    			}
			    				}
			    			}
			    		}
			    		
					} catch (Exception ex) {
						ex.printStackTrace();
					}
		    		
		    	} // end for ---
		    	pm.setVisible(false);
		    	pm.dispose();
		    	setBasicGraphGuiVisViewerActionOnTop(false);
			}
		};
		Thread decoder = new Thread(decode);
		decoder.setName("Base64-Decoder");
		decoder.start();
		
    }

    /**
     * Sets the instances of the NetworkComponents data models to a Base64 encoded String.
     */
    public void setNetworkComponentDataModelBase64Encoded() {
    	
    	final Long displayTime = System.currentTimeMillis() + new Long(1000);
    	final GraphEnvironmentController graphController = this;
    	
    	String title = Language.translate("Preparing network components", Language.EN);
    	String header = Language.translate("Preparing and encoding network components for saving", Language.EN);
    	String progress = Language.translate("Reading", Language.EN) + "...";
    	
    	final ProgressMonitor pm = new ProgressMonitor(title, header, progress);
    	pm.setAllow2Cancel(false);
    	
    	Runnable encode = new Runnable() {
    		public void run() {

    			long nextGraphRenderingInterval = 500; // ms
				long nextGraphRendering = System.currentTimeMillis() + nextGraphRenderingInterval;
    			
		    	Object[] netCompArr =  getNetworkModel().getNetworkComponents().values().toArray();
		    	Object[] graphNodeArr =  getNetworkModel().getGraph().getVertices().toArray();
		    	
		    	int progressIntOld = -1;
		    	int netCompCount = netCompArr.length + graphNodeArr.length;
		    	for (int i = 0; i<netCompCount; i++) {
		    		
		    		try {
		    			// --- Only display progress, if procedure is to long -----
			    		if (System.currentTimeMillis() > displayTime) {
			    			if (pm.isVisible()==false) {
			    				pm.setVisible(true);
			    		    	pm.validate();
			    		    	pm.repaint();
			    			}
			    			// --- Set Progress monitor ---------------------------
				    		float progressCalc = (float) (((float)i/(float)netCompCount) * 100.0);
				    		final int progressInt = Math.round(progressCalc);
				    		if (progressInt!=progressIntOld) {
				    			progressIntOld = progressInt;
				    			pm.setProgress(progressInt);
				    		}
			    		}

			    		// --- Render/paint graph ---------------------------------
			    		if (System.currentTimeMillis()>=nextGraphRendering) {
			    			setBasicGraphGuiVisViewerActionOnTop(false);
			    			setBasicGraphGuiVisViewerActionOnTop(true);
			    			nextGraphRendering = System.currentTimeMillis() + nextGraphRenderingInterval;
			    		}
			    		
			    		// --- Find the corresponding NetworkComponentAdapter -----
			    		NetworkComponentAdapter netCompAdapter=null;
			    		NetworkComponent netComp = null;
			    		GraphNode graphNode = null;
			    		if (i<netCompArr.length) {
			    			netComp = (NetworkComponent) netCompArr[i];		
			    			netCompAdapter = getNetworkModel().getNetworkComponentAdapter(graphController, netComp);
			    		} else {
			    			graphNode = (GraphNode) graphNodeArr[i-netCompArr.length];
			    			netCompAdapter = getNetworkModel().getNetworkComponentAdapter(graphController, graphNode);	
			    		}
			    		
			    		// --- Set the components data model as Base64 ------------
			    		if (netCompAdapter!=null) {
			    			Object dataModel = null;
			    			if (graphNode!=null) {
			    				dataModel = graphNode.getDataModel();
			    			} else {
			    				dataModel = netComp.getDataModel();
			    			}
			    			if (dataModel==null) {
			    				// --- No data model ------------------------------
			    				if (graphNode!=null) {
				    				graphNode.setDataModelBase64(null);
				    			} else {
				    				netComp.setDataModelBase64(null);;
				    			}
			    			} else {
			    				// --- Get DataModelAdapter -----------------------
			    				NetworkComponentAdapter4DataModel netCompDataModelAdapter = netCompAdapter.getStoredDataModelAdapter();
			    				if (netCompDataModelAdapter==null) {
			    					// --- No DataModelAdapter found --------------
			    					if (graphNode!=null) {
					    				graphNode.setDataModelBase64(null);
					    			} else {
					    				netComp.setDataModelBase64(null);
					    			}
			    						
			    				} else {
			    					// --- Get Base64 encoded String ------------
			    					Vector<String> dataModelBase64 = netCompDataModelAdapter.getDataModelBase64Encoded(dataModel);
			    					if (graphNode!=null) {
					    				graphNode.setDataModelBase64(dataModelBase64);
					    			} else {
					    				netComp.setDataModelBase64(dataModelBase64);
					    			}
			    					
			    				}
			    			}
			    		}
			    		
					} catch (Exception ex) {
						ex.printStackTrace();
					}
		    		
		    	} // end for ---
		    	pm.setVisible(false);
		    	pm.dispose();
		    	setBasicGraphGuiVisViewerActionOnTop(false);
		    	
			}
    	};
		Thread encoder = new Thread(encode);
		encoder.setName("Base64-Encoder");
		encoder.start();
    	
    }

    /**
     * Sets the indicator that an action on top is running, in order to prevent permanently (re-)paint actions.
     * @param actionOnTopIsRunning the indicator that an action on top of the graph is running or not
     */
    private void setBasicGraphGuiVisViewerActionOnTop(boolean actionOnTopIsRunning) {
    	
    	BasicGraphGuiVisViewer<GraphNode, GraphEdge> basicGraphGuiVisViewer = null;
		try {
			GraphEnvironmentControllerGUI graphControllerGUI = this.getGraphEnvironmentControllerGUI();
			if (graphControllerGUI!=null) {
				basicGraphGuiVisViewer = graphControllerGUI.getBasicGraphGuiRootJSplitPane().getBasicGraphGui().getVisView();
				if (basicGraphGuiVisViewer!=null) {
					basicGraphGuiVisViewer.setActionOnTop(actionOnTopIsRunning);
					if (actionOnTopIsRunning==false) {
						basicGraphGuiVisViewer.repaint();
						basicGraphGuiVisViewer.requestFocus();
					}	
				}
			} 
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	
    }
    
}