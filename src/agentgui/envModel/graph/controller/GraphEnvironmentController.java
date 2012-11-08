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
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
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

    /** The key string used for saving the position in the GraphML file */
    private static final String KEY_POSITION_PROPERTY = "pos";
    /** The key string used for saving the ontology representation in the GraphML file */
    private static final String KEY_ONTOLOGY_REPRESENTATION_PROPERTY = "ontoRepr";

    /** The base file name used for saving the graph and the components (without suffix) */
    private String baseFileName = null;
    /** The network model currently loaded */
    private NetworkModel networkModel = null;
    private NetworkModelAdapter networkModelAdapter = new NetworkModelAdapter(this);
    
    /** Custom user object to be placed in the project object. Used here for storing the current component type settings. */
    private static final String generalGraphSettings4MASFile = "~GeneralGraphSettings~";
    
    
    /** Known adapter for the import of network models */
    private Vector<NetworkModelFileImporter> importAdapter = new Vector<NetworkModelFileImporter>();
    /** The GraphMLWriter used to save the graph */
    private GraphMLWriter<GraphNode, GraphEdge> graphMLWriter = null;

    /**
     * The constructor for the GraphEnvironmentController for displaying the current environment 
     * model during a running simulation. Use {@link #setEnvironmentModel(Object)}, in order to 
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
		if (this.getProject() != null) {
		    this.updateGraphFileName();
		    this.loadEnvironment();
		}
		
    }

    /*
     * (non-Javadoc)
     * @see agentgui.core.environment.EnvironmentController#createEnvironmentPanel()
     */
    @Override
    protected EnvironmentPanel createEnvironmentPanel() {
    	GraphEnvironmentControllerGUI graphDisplay = new GraphEnvironmentControllerGUI(this);
    	return graphDisplay;
    }

    /**
     * Sets the project unsaved.
     */
    public void setProjectUnsaved() {
    	if (this.getProject() != null) {
		    this.getProject().isUnsaved = true;
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
		    this.setEnvironmentModel(this.networkModel);
		    
		    // --- register a new list of agents, which has to be started with the environment ------
		    this.setAgents2Start(new DefaultListModel());
		    this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
	
		    break;
	
		case SimulationSetups.SIMULATION_SETUP_COPY:
		    this.updateGraphFileName();
		    this.saveEnvironment();
		    this.getProject().isUnsaved = true;
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
		this.getCurrentSimSetup().setEnvironmentFileName(baseFileName + ".graphml");
    }

    /*
     * (non-Javadoc)
     * @see EnvironmentController#loadEnvironment()
     */
    @Override
    protected void loadEnvironment() {

    	this.networkModel = new NetworkModel();
    	String fileName = getCurrentSimSetup().getEnvironmentFileName();
    	
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
		this.setEnvironmentModel(this.networkModel);

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
     * 
     * @see agentgui.core.environment.EnvironmentController#setEnvironmentModel(java.lang.Object)
     */
    @Override
    public void setEnvironmentModel(Object environmentObject) {
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
    public Object getEnvironmentModel() {
    	return this.networkModel;
    }

    /*
     * (non-Javadoc)
     * @see agentgui.core.environment.EnvironmentController#getEnvironmentModelCopy()
     */
    @Override
    public Object getEnvironmentModelCopy() {
		NetworkModel netModel = this.networkModel.getCopy();
		return netModel;
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
    		this.importAdapter.add(new YedGraphMLFileImporter(this, "graphml", "yEd GraphML"));
    	}
    	return this.importAdapter;
    }
    /**
     * This method imports a new network model using the GraphFileImporter
     * @param file The file defining the new graph.
     */
    public void importNetworkModel(NetworkModelFileImporter importer, File file) {
    	this.getAgents2Start().clear();
    	this.setNetworkModel(null);
    	NetworkModel newNetworkModel = importer.importGraphFromFile(file);
    	if (newNetworkModel!=null) {
    		this.setNetworkModel(newNetworkModel);
    		// --- Initially translate the data models of the  ------
    		// --- NetworkComponents to Base64 encoded Strings ------
    		this.setNetworkComponentDataModelBase64Encoded();
    	}
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
			public String transform(GraphEdge arg0) {
			    return arg0.getComponentType();
			}
		    });
		    graphMLWriter.setVertexIDs(new Transformer<GraphNode, String>() {
	
			@Override
			public String transform(GraphNode arg0) {
			    return arg0.getId();
			}
		    });
		    graphMLWriter.addVertexData(KEY_POSITION_PROPERTY, "position", "", new Transformer<GraphNode, String>() {
	
			@Override
			public String transform(GraphNode node) {
			    String pos = node.getPosition().getX() + ":" + node.getPosition().getY();
			    return pos;
			}
		    });
		    graphMLWriter.addVertexData(KEY_ONTOLOGY_REPRESENTATION_PROPERTY, "Base64 encoded ontology representation", "", new Transformer<GraphNode, String>() {
	
			@Override
			public String transform(GraphNode arg0) {
			    return arg0.getEncodedOntologyRepresentation();
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
	
			GraphNode gn = new GraphNode();
			gn.setId(nmd.getId());
			gn.setEncodedOntologyRepresentation(nmd.getProperty(KEY_ONTOLOGY_REPRESENTATION_PROPERTY));
	
			Point2D pos = null;
			String posString = nmd.getProperty(KEY_POSITION_PROPERTY);
			if (posString != null) {
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
			gn.setPosition(pos);
			return gn;
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
    private void setNetworkComponentDataModelBase64Decoded() {
    	
    	String title = Language.translate("Initiating network components", Language.EN);
    	String header = Language.translate("Initiating network components and setting data model", Language.EN);
    	String progress = Language.translate("Reading", Language.EN) + "...";
    	final String progressRun = Language.translate("Setting data model for", Language.EN) + " ";
    	
    	final ProgressMonitor pm = new ProgressMonitor(Application.getMainWindow(), title, header, progress);
    	pm.setAllow2Cancel(false);
    	pm.setAlwaysOnTop(true);
    	pm.setVisible(true);
    	pm.validate();
    	pm.repaint();
    	
    	Runnable decode = new Runnable() {
			public void run() {
		    	
				NetworkModel networkModel = getNetworkModel();
		    	Object[] netCompArr = networkModel.getNetworkComponents().values().toArray();
		    	int netCompCount = netCompArr.length;
		    	for (int i = 0; i<netCompCount; i++) {
		    	
		    		NetworkComponent netComp = (NetworkComponent) netCompArr[i];
		    		
		    		// --- Set Progress monitor -----------------------------
		    		float progressCalc = (float) (((float)i/(float)netCompCount) * 100.0);
		    		final int progressInt = Math.round(progressCalc);
		    		final String netCompID = netComp.getId();
		    		
		    		SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							synchronized (pm) {
								pm.setProgress(progressInt);
						    	pm.setProgressText(progressRun +  netCompID);								
							};
						}
					});
		    		
		    		// --- Set the components data model instance ----------- 
		    		NetworkComponentAdapter netCompAdapter = networkModel.getNetworkComponentAdapter(netComp);
		    		if (netCompAdapter!=null) {
		    			Vector<String> dataModelBase64 = netComp.getDataModelBase64();
		    			if (dataModelBase64!=null) {
		    				// --- Get DataModelAdapter ---------------------
		    				NetworkComponentAdapter4DataModel netCompDataModelAdapter = netCompAdapter.invokeGetDataModelAdapter();
		    				if (netCompDataModelAdapter!=null) {
		    					// --- Get Base64 decoded Object ------------
		    					Object dataModel = netCompDataModelAdapter.getDataModelBase64Decoded(dataModelBase64);
		    	    			netComp.setDataModel(dataModel);
		    				}
		    			}
		    		}
		    		
		    	} // end for ---
		    	pm.setVisible(false);
		    	pm.dispose();
		    	
			}
		};
		Thread decoder = new Thread(decode);
		decoder.setName("Base64-Decoder");
		decoder.start();
		
    }

    /**
     * Sets the instances of the NetworkComponents data models to a Base64 encoded String.
     */
    private void setNetworkComponentDataModelBase64Encoded() {
    	
    	String title = Language.translate("Preparing network components", Language.EN);
    	String header = Language.translate("Preparing and encoding network components for saving", Language.EN);
    	String progress = Language.translate("Reading", Language.EN) + "...";
    	final String progressRun = Language.translate("Base64-encoding of data model for", Language.EN) + " ";
    	
    	final ProgressMonitor pm = new ProgressMonitor(Application.getMainWindow(), title, header, progress);
    	pm.setAllow2Cancel(false);
    	pm.setAlwaysOnTop(true);
    	pm.setVisible(true);
    	pm.validate();
    	pm.repaint();
    	
    	Runnable encode = new Runnable() {
			public void run() {
				
				NetworkModel networkModel = getNetworkModel();
		    	Object[] netCompArr = networkModel.getNetworkComponents().values().toArray();
		    	int netCompCount = netCompArr.length;
		    	for (int i = 0; i<netCompCount; i++) {
		    		
		    		NetworkComponent netComp = (NetworkComponent) netCompArr[i];
		    		
		    		// --- Set Progress monitor -----------------------------
		    		float progressCalc = (float) (((float)i/(float)netCompCount) * 100.0);
		    		final int progressInt = Math.round(progressCalc);
		    		final String netCompID = netComp.getId();
		    		
		    		SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
				    		synchronized (pm) {
				    			pm.setProgress(progressInt);
						    	pm.setProgressText(progressRun + netCompID);	
							}
						}
					});
		    		
		    		// --- Set the components data model as Base64 ----------
		    		NetworkComponentAdapter netCompAdapter = networkModel.getNetworkComponentAdapter(netComp);
		    		if (netCompAdapter!=null) {
		    			Object dataModel = netComp.getDataModel();
		    			if (dataModel==null) {
		    				// --- No data model ----------------------------
		    				netComp.setDataModelBase64(null);
		    			} else {
		    				// --- Get DataModelAdapter ---------------------
		    				NetworkComponentAdapter4DataModel netCompDataModelAdapter = netCompAdapter.invokeGetDataModelAdapter();
		    				if (netCompDataModelAdapter==null) {
		    					// --- No DataModelAdapter found ------------
		    					netComp.setDataModelBase64(null);	
		    				} else {
		    					// --- Get Base64 encoded String ------------
		    					Vector<String> dataModelBase64 = netCompDataModelAdapter.getDataModelBase64Encoded(dataModel);
		    	    			netComp.setDataModelBase64(dataModelBase64);
		    				}
		    			}
		    		}
		    		
		    	} // end for ---
		    	pm.setVisible(false);
		    	pm.dispose();

			}
    	};
		Thread encoder = new Thread(encode);
		encoder.setName("Base64-Encoder");
		encoder.start();
    	
    }

}