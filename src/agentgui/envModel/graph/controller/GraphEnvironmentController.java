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

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections15.Transformer;

import agentgui.core.environment.EnvironmentController;
import agentgui.core.project.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.core.sim.setup.SimulationSetups;
import agentgui.core.sim.setup.SimulationSetupsChangeNotification;
import agentgui.envModel.graph.controller.yedGraphml.YedGraphMLFileImporter;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponentList;
import agentgui.envModel.graph.networkModel.NetworkModel;
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
 */
public class GraphEnvironmentController extends EnvironmentController {
	
	/** Observer notification when a new NetworkModel is set */
	public static final Integer EVENT_NETWORKMODEL_LOADED = 0;
	/** Observer notification that the network model has to be refreshed */
	public static final Integer EVENT_NETWORKMODEL_REFRESHED = 3;
	/** Observer notification when the element type settings have been changed */
	public static final Integer EVENT_ELEMENT_TYPES_SETTINGS_CHANGED = 1;
	/** The key string used for saving the position in the GraphML file	 */
	private static final String KEY_POSITION_PROPERTY = "pos";
	/** The key string used for saving the ontology representation in the GraphML file */
	private static final String KEY_ONTOLOGY_REPRESENTATION_PROPERTY = "ontoRepr";
	
	
	/**
	 * The base file name used for saving the graph and the components (without suffix)
	 */
	private String baseFileName = null;
	/**
	 * The network model currently loaded
	 */
	private NetworkModel networkModel = null;
	
	/**
	 * Custom user object to be placed in the project object.
	 * Used here for storing the current component type settings.
	 */
	private GeneralGraphSettings4MAS generalGraphSettings4MAS = null;
	/**
	 * The GraphFileImporter used for importing externally defined graph definitions
	 */
	private GraphFileImporter graphFileImporter = null;
	/**
	 * The GraphMLWriter used to save the graph
	 */
	private GraphMLWriter<GraphNode, GraphEdge> graphMLWriter = null;
	
	
	/**
	 * The constructor for the GraphEnvironmentController
	 * for displaying the current environment model during 
	 * a running simulation.
	 * Use {@link #setEnvironmentModel(Object)}, in order to set 
	 * the current {@link NetworkModel}.
	 */
	public GraphEnvironmentController() { }
	/**
	 * The constructor for the GraphEnvironmentController 
	 * for configurations within Agent.GUI
	 * 
	 * @param project The current project
	 */
	public GraphEnvironmentController(Project project){
		super(project);		
		if (this.getProject()!=null) {
			this.updateGraphFileName();
			this.loadEnvironment();				
		}
	}
	
	/**
	 * Set the SimulationSetup's ComponentTypeSettings property
	 * @param gesVector
	 */
	public void setComponentTypeSettings(HashMap<String, ComponentTypeSettings> gesVector){
		
		if (this.generalGraphSettings4MAS==null) {
			this.generalGraphSettings4MAS = new GeneralGraphSettings4MAS();
		}
		this.generalGraphSettings4MAS.setCurrentCTS(gesVector);

		if (this.getProject()!=null) {
			this.getProject().setUserRuntimeObject(generalGraphSettings4MAS);
			this.getProject().isUnsaved=true;			
		}
		this.setChanged();
		this.notifyObservers(EVENT_ELEMENT_TYPES_SETTINGS_CHANGED);
	}
	/**
	 * Gets the current ComponentTypeSettings
	 * @return HashMap<String, ComponentTypeSettings> The current component type settings map.
	 */
	public HashMap<String, ComponentTypeSettings> getComponentTypeSettings(){
		return generalGraphSettings4MAS.getCurrentCTS();
	}
	
	/**
	 * This method imports a new network model using the GraphFileImporter  
	 * @param graphMLFile The GraphML file defining the new graph.
	 */
	public void importNetworkModel(File graphMLFile){
		NetworkModel netModel = this.getGraphFileImporter().importGraphFromFile(graphMLFile);
		this.setNetworkModel(netModel);
	}
	
	/**
	 * Sets the environment network model
	 * @return NetworkModel - The environment model
	 */
	private void setNetworkModel(NetworkModel networkModel) {
		this.networkModel = networkModel;
		if(this.networkModel!=null){
			this.setChanged();
			notifyObservers(EVENT_NETWORKMODEL_LOADED);
		}
		if (getProject()!=null) {
			getProject().isUnsaved = true;
		}
	}
	/**
	 * Returns the environment network model
	 * @return NetworkModel - The environment model
	 */
	public NetworkModel getNetworkModel() {
		return networkModel;
	}
	
	/**
	 * Clears the network model by replacing with an empty graph
	 * and notifies observers
	 */
	public void clearNetworkModel(){
		
		// clear the network model objects
		networkModel = new NetworkModel();
		this.getAgents2Start().clear();
		
		refreshNetworkModel();
	}
	
	/**
	 * Can be used to notify the observers after changing the network model from outside.
	 * Also sets the project as unsaved
	 */
	public void refreshNetworkModel() {		
		if (this.getProject()!=null) {
			this.getProject().setChangedAndNotify(EVENT_NETWORKMODEL_REFRESHED);	
		}
		setChanged();
		notifyObservers(EVENT_NETWORKMODEL_REFRESHED);		
	}	
	
	/**
	 * Gets the GraphFileImporter, creates a new instance if null
	 * @return GraphFileImporter
	 */
	public GraphFileImporter getGraphFileImporter(){
		if(graphFileImporter == null){
			graphFileImporter = new YedGraphMLFileImporter(getComponentTypeSettings());
		}
		return graphFileImporter;
	}
	
	/**
	 * Gets the GraphMLWriter, creates and initiates a new instance if null 
	 * @return The GraphMLWriter
	 */
	private GraphMLWriter<GraphNode, GraphEdge> getGraphMLWriter(){
		if(graphMLWriter == null){
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
					String pos = node.getPosition().getX()+":"+node.getPosition().getY();
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
	 * @param file The file to be loaded
	 * @return The GraphMLReader2
	 * @throws FileNotFoundException
	 */
	private GraphMLReader2<Graph<GraphNode, GraphEdge>, GraphNode, GraphEdge> getGraphMLReader(File file) throws FileNotFoundException{
		Transformer<GraphMetadata, Graph<GraphNode, GraphEdge>> graphTransformer = new Transformer<GraphMetadata, Graph<GraphNode,GraphEdge>>() {

			@Override
			public SparseGraph<GraphNode, GraphEdge> transform(
					GraphMetadata gmd) {
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
				if(posString != null){
					String[] coords = posString.split(":");
					
					if(coords.length == 2){
						double xPos = Double.parseDouble(coords[0]);
						double yPos = Double.parseDouble(coords[1]);
						pos = new Point2D.Double(xPos, yPos);
					}
				}
				if(pos == null){
					System.err.println("Keine Position definiert für Knoten "+nmd.getId());
					pos = new Point2D.Double(0,0);
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
		
		return new GraphMLReader2<Graph<GraphNode,GraphEdge>, GraphNode, GraphEdge>(fileReader, graphTransformer, nodeTransformer, edgeTransformer, hyperEdgeTransformer);
		
	}
	
	/**
	 * This method handles the SimulationSetupChangeNotifications sent from the project
	 * @param sscn The SimulationSetupChangeNotifications to handle
	 */
	@Override
	protected void handleSimSetupChange(SimulationSetupsChangeNotification sscn){
		
		switch(sscn.getUpdateReason()){
			
			case SimulationSetups.SIMULATION_SETUP_COPY:
				// Saving the network model of previous setup before loading the next one
				saveEnvironment();
				
				updateGraphFileName();
				saveEnvironment();
				this.getProject().isUnsaved = true;
			break;
			
			case SimulationSetups.SIMULATION_SETUP_ADD_NEW:
				// Saving the network model of previous setup before loading the next one
				saveEnvironment(); 
				
				updateGraphFileName();
				networkModel = new NetworkModel();
				
				setChanged();
				notifyObservers(EVENT_NETWORKMODEL_LOADED);
				
			break;
			
			case SimulationSetups.SIMULATION_SETUP_REMOVE:
				File graphFile = new File(getEnvFolderPath()+File.separator+baseFileName+".graphml");
				if(graphFile.exists()){
					graphFile.delete();
				}
				
				File componentFile = new File(getEnvFolderPath()+File.separator+baseFileName+".xml");
				if(componentFile.exists()){
					componentFile.delete();
				}
			// No, there's no break missing here. After deleting a setup another one is loaded.
			
			case SimulationSetups.SIMULATION_SETUP_LOAD:
				// Saving the network model of previous setup before loading the next one
				saveEnvironment();
				
				updateGraphFileName();
				this.loadEnvironment(); //Loads network model and notifies observers	
			
				generalGraphSettings4MAS = (GeneralGraphSettings4MAS) this.getProject().getUserRuntimeObject();
				setComponentTypeSettings(generalGraphSettings4MAS.getCurrentCTS());				
						
			break;
			
			case SimulationSetups.SIMULATION_SETUP_RENAME:
				File oldGraphFile = new File(getEnvFolderPath()+File.separator+baseFileName+".graphml");
				File oldComponentFile = new File(getEnvFolderPath()+File.separator+baseFileName+".xml");
				updateGraphFileName();
				if(oldGraphFile.exists()){
					File newGraphFile = new File(getEnvFolderPath()+File.separator+baseFileName+".graphml");
					oldGraphFile.renameTo(newGraphFile);
				}
				if(oldComponentFile.exists()){
					File newComponentFile = new File(getEnvFolderPath()+File.separator+baseFileName+".xml");
					oldComponentFile.renameTo(newComponentFile);
				}
			break;
			
			case SimulationSetups.SIMULATION_SETUP_SAVED:
			break;
		}
		
	}
	
	/**
	 * This method sets the baseFileName property and the SimulationSetup's environmentFileName according to the current SimulationSetup
	 */
	private void updateGraphFileName(){
		baseFileName = this.getProject().simulationSetupCurrent;
		getCurrentSimSetup().setEnvironmentFileName(baseFileName+".graphml");
	}
	
	/* (non-Javadoc)
	 * @see EnvironmentController#loadEnvironment()
	 */
	@Override
	protected void loadEnvironment() {
		networkModel = new NetworkModel();
		
		String fileName = getCurrentSimSetup().getEnvironmentFileName();
		if(fileName != null){

			// --- register the list of agents, which has to be started with the environment ------
			this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
			
			// --- Load the graph topology from the graph file ------
			File graphFile = new File(getEnvFolderPath()+fileName);
			if(graphFile.exists()){
				baseFileName = fileName.substring(0, fileName.lastIndexOf('.'));
				try {
					// Load graph topology
					networkModel.setGraph(getGraphMLReader(graphFile).readGraph());
					
					// Load network component definitions
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (GraphIOException e) {
					e.printStackTrace();
				}
			}
			
			// Load the component definitions from the component file
			File componentFile = new File(getEnvFolderPath()+File.separator+baseFileName+".xml");
			if(componentFile.exists()){
				try {
					JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
					Unmarshaller unmarsh = context.createUnmarshaller();
					NetworkComponentList compList = (NetworkComponentList) unmarsh.unmarshal(new FileReader(componentFile));
					networkModel.setNetworkComponents(compList.getComponentList());
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}				
			}
		}		
		setChanged();
		notifyObservers(new Integer(EVENT_NETWORKMODEL_LOADED));
				
		//Loading component type settings from the simulation setup		
		generalGraphSettings4MAS = (GeneralGraphSettings4MAS) this.getProject().getUserRuntimeObject();
		// If no ETS are specified in the setup, assign an empty HashMap to avoid null pointers
		if(generalGraphSettings4MAS == null){
			generalGraphSettings4MAS = new GeneralGraphSettings4MAS();
		}
		
		setComponentTypeSettings(generalGraphSettings4MAS.getCurrentCTS());
		
	}
	/* (non-Javadoc)
	 * @see EnvironmentController#saveEnvironment()
	 */
	@Override
	protected void saveEnvironment() {
		if(networkModel != null && networkModel.getGraph() != null){
			
			try {
				// Save the graph topology 
				String graphFileName = baseFileName+".graphml";
				File file = new File(getEnvFolderPath()+File.separator+graphFileName);
				if(!file.exists()){
					file.createNewFile();
				}
				PrintWriter pw = new PrintWriter(new FileWriter(file));
				getGraphMLWriter().save(networkModel.getGraph(), pw);
				
				// Save the network component definitions
				File componentFile = new File(getEnvFolderPath()+File.separator+baseFileName+".xml");
				if(!componentFile.exists()){
					componentFile.createNewFile();
				}
				FileWriter componentFileWriter = new FileWriter(componentFile); 
				
				JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
				Marshaller marsh = context.createMarshaller();
				marsh.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
				marsh.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );

				marsh.marshal(new NetworkComponentList(networkModel.getNetworkComponents()), componentFileWriter);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#setEnvironmentModel(java.lang.Object)
	 */
	@Override
	public void setEnvironmentModel(Object environmentObject) {
		try {
			if (environmentObject==null) {
				this.setNetworkModel(null);
				this.generalGraphSettings4MAS = null;
				
			} else {
				this.networkModel = (NetworkModel) environmentObject;
				if (this.networkModel.getGeneralGraphSettings4MAS()!=null) {
					this.generalGraphSettings4MAS = this.networkModel.getGeneralGraphSettings4MAS(); 
				}
				this.setNetworkModel((NetworkModel) environmentObject);
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getEnvironmentModel()
	 */
	@Override
	public Object getEnvironmentModel() {
		return this.networkModel;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getEnvironmentModelCopy()
	 */
	@Override
	public Object getEnvironmentModelCopy() {
		NetworkModel netModel = this.networkModel.getCopy();
		netModel.setGeneralGraphSettings4MAS((GeneralGraphSettings4MAS) this.generalGraphSettings4MAS.clone());
		return netModel;
	}	
	
}
