/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.graphEnvironment.controller;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;

import agentgui.core.application.Project;
import agentgui.core.sim.setup.SimulationSetups;
import agentgui.core.sim.setup.SimulationSetupsChangeNotification;
import agentgui.graphEnvironment.controller.yedGraphml.YedGraphMLFileImporter;
import agentgui.graphEnvironment.networkModel.ComponentTypeSettings;
import agentgui.graphEnvironment.networkModel.GraphEdge;
import agentgui.graphEnvironment.networkModel.GraphNode;
import agentgui.graphEnvironment.networkModel.NetworkComponentList;
import agentgui.graphEnvironment.networkModel.NetworkModel;
/**
 * This class manages an environment model of the type graph / network.<br>
 * Also contains the network component type settings configuration.<br>
 * The observable class of the network model in the observer pattern.
 *  
 * @see agentgui.graphEnvironment.networkModel.NetworkModel
 * @see agentgui.graphEnvironment.networkModel.ComponentTypeSettings
 * @see GraphEnvironmentControllerGUI
 * 
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author <br>Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
public class GraphEnvironmentController extends Observable implements Observer {
	/**
	 * Observer notification when a new NetworkModel is set 
	 */
	public static final Integer EVENT_NETWORKMODEL_LOADED = 0;
	
	/**
	 * Observer notification that the network model is updated.
	 * Similar to the LOADED, but instead of creating a new visualisation viewer, the graph is refreshed.
	 */
	public static final Integer EVENT_NETWORKMODEL_REFRESHED = 3;

	/**
	 * Observer notification when the element type settings have been changed
	 */
	public static final Integer EVENT_ELEMENT_TYPES_SETTINGS_CHANGED = 1;
	/**
	 * The key string used for saving the position in the GraphML file
	 */
	private static final String KEY_POSITION_PROPERTY = "pos";
	/**
	 * The key string used for saving the ontology representation in the GraphML file
	 */
	private static final String KEY_ONTOLOGY_REPRESENTATION_PROPERTY = "ontoRepr";

	/**
	 * The path to the folder where environment related files are stored 
	 */
	private String envFilePath = null;
	/**
	 * The base file name used for saving the graph and the components (without suffix)
	 */
	private String baseFileName = null;
	/**
	 * The current project
	 */
	private Project project = null;
	/**
	 * The network model currently loaded
	 */
	private NetworkModel networkModel = null;
	/**
	 * The currently defined GraphElementSettings, accessible by the type string
	 */
	private HashMap<String, ComponentTypeSettings> currentCTS = null;
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
	 * @param project
	 */
	public GraphEnvironmentController(Project project){
		this.project = project;
		this.project.addObserver(this);
		envFilePath = this.project.getProjectFolderFullPath()+this.project.getSubFolderEnvSetups();
		loadNetworkModel();
		setComponentTypeSettings(project.simSetups.getCurrSimSetup().getGraphElementSettings());
		// If no ETS are specified in the setup, assign an empty HashMap to avoid null pointers
		if(currentCTS == null){
			currentCTS = new HashMap<String, ComponentTypeSettings>();
		}
	}
	/**
	 * Returns the current project
	 * @return The current project
	 */
	Project getProject(){
		return this.project;
	}
	
	/**
	 * Set the SimulationSetup's ComponentTypeSettings property
	 * @param gesVector
	 */
	public void setComponentTypeSettings(HashMap<String, ComponentTypeSettings> gesVector){
		currentCTS = gesVector;
		project.simSetups.getCurrSimSetup().setGraphElementSettings(gesVector);
		project.isUnsaved=true;
		setChanged();
		notifyObservers(EVENT_ELEMENT_TYPES_SETTINGS_CHANGED);
	}
	/**
	 * Gets the current ComponentTypeSettings
	 * @return HashMap<String, ComponentTypeSettings> The current component type settings map.
	 */
	public HashMap<String, ComponentTypeSettings> getComponentTypeSettings(){
		return currentCTS;
	}
	
	/**
	 * This method imports a new network model using the GraphFileImporter  
	 * @param graphMLFile The GraphML file defining the new graph.
	 */
	public void importNetworkModel(File graphMLFile){
		networkModel = getGraphFileImporter().importGraphFromFile(graphMLFile);
		if(networkModel != null){
			Layout<GraphNode, GraphEdge> initLayout = new FRLayout<GraphNode, GraphEdge>(networkModel.getGraph(), new Dimension(400, 400));
			
			// Initialize node positions  !!! Should be optional later, positions might be defined in the importet file !!!
			getGraphFileImporter().initPosition(networkModel, initLayout);
			
			this.setChanged();
			notifyObservers(EVENT_NETWORKMODEL_LOADED);
		}
		project.isUnsaved = true;
	}
	
	/**
	 * Returns the environment network model
	 * @return NetworkModel - The environment model
	 */
	public NetworkModel getGridModel() {
		return networkModel;
	}
	
	/**
	 * Clears the network model by replacing with an empty graph
	 * and notifies observers
	 */
	public void clearNetworkModel(){
		
		// clear the network model objects
		networkModel = new NetworkModel();
		refreshNetworkModel();
	}
	
	/**
	 * Can be used to notify the observers after changing the network model from outside.
	 * Also sets the project as unsaved
	 */
	public void refreshNetworkModel() {		
		this.project.setChangedAndNotify(EVENT_NETWORKMODEL_REFRESHED);	
		setChanged();
		notifyObservers(EVENT_NETWORKMODEL_REFRESHED);		
	}
	
	/**
	 * Invoked when an observable( the {@link Project} in this case) notifies this class.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(project) && arg == Project.SAVED){
			saveNetworkModel();
		}else if(o.equals(project) && arg instanceof SimulationSetupsChangeNotification){
			handleSetupChange((SimulationSetupsChangeNotification) arg);
			
		}
	}
	
	/**
	 * Gets the GraphFileImporter, creates a new instance if null
	 * @return GraphFileImporter
	 */
	GraphFileImporter getGraphFileImporter(){
		if(graphFileImporter == null){
			graphFileImporter = new YedGraphMLFileImporter(currentCTS);
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
				// TODO Auto-generated method stub
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
	private void handleSetupChange(SimulationSetupsChangeNotification sscn){
		
		switch(sscn.getUpdateReason()){
			
			case SimulationSetups.SIMULATION_SETUP_COPY:
				// Saving the network model of previous setup before loading the next one
				saveNetworkModel(); 
				
				updateGraphFileName();
				saveNetworkModel();
				project.isUnsaved = true;
			break;
			
			case SimulationSetups.SIMULATION_SETUP_ADD_NEW:
				// Saving the network model of previous setup before loading the next one
				saveNetworkModel(); 
				
				updateGraphFileName();
				networkModel = new NetworkModel();
				//Copying the component type settings over
				setComponentTypeSettings(currentCTS);
				setChanged();
				notifyObservers(EVENT_NETWORKMODEL_LOADED);
				
			break;
			
			case SimulationSetups.SIMULATION_SETUP_REMOVE:
				File graphFile = new File(envFilePath+File.separator+baseFileName+".graphml");
				if(graphFile.exists()){
					graphFile.delete();
				}
				
				File componentFile = new File(envFilePath+File.separator+baseFileName+".xml");
				if(componentFile.exists()){
					componentFile.delete();
				}
			// No, there's no break missing here. After deleting a setup another one is loaded.
			
			case SimulationSetups.SIMULATION_SETUP_LOAD:
				// Saving the network model of previous setup before loading the next one
				saveNetworkModel(); 
				
				updateGraphFileName();
				loadNetworkModel();	//Loads network model and notifies observers	
				setComponentTypeSettings(project.simSetups.getCurrSimSetup().getGraphElementSettings());				
						
			break;
			
			case SimulationSetups.SIMULATION_SETUP_RENAME:
				File oldGraphFile = new File(envFilePath+File.separator+baseFileName+".graphml");
				File oldComponentFile = new File(envFilePath+File.separator+baseFileName+".xml");
				updateGraphFileName();
				if(oldGraphFile.exists()){
					File newGraphFile = new File(envFilePath+File.separator+baseFileName+".graphml");
					oldGraphFile.renameTo(newGraphFile);
				}
				if(oldComponentFile.exists()){
					File newComponentFile = new File(envFilePath+File.separator+baseFileName+".xml");
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
		baseFileName = project.simSetupCurrent;
		project.simSetups.getCurrSimSetup().setEnvironmentFileName(baseFileName+".graphml");
	}
	
	/**
	 * This method saves the current NetworkModel
	 */
	private void saveNetworkModel(){
		if(networkModel != null && networkModel.getGraph() != null){
			
			try {
				// Save the graph topology 
				String graphFileName = baseFileName+".graphml";
				File file = new File(envFilePath+File.separator+graphFileName);
				if(!file.exists()){
					file.createNewFile();
				}
				PrintWriter pw = new PrintWriter(new FileWriter(file));
				getGraphMLWriter().save(networkModel.getGraph(), pw);
				
				// Save the network component definitions
				File componentFile = new File(envFilePath+File.separator+baseFileName+".xml");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method loads a saved NetworkModel
	 */
	private void loadNetworkModel(){
		
		networkModel = new NetworkModel();
		
		String fileName = project.simSetups.getCurrSimSetup().getEnvironmentFileName();
		if(fileName != null){
			
			String folderPath = this.project.getProjectFolderFullPath()+this.project.getSubFolderEnvSetups();
			
			// Load the graph topology from the graph file
			File graphFile = new File(folderPath+File.separator+fileName);
			if(graphFile.exists()){
				baseFileName = fileName.substring(0, fileName.lastIndexOf('.'));
				try {
					// Load graph topology
					networkModel.setGraph(getGraphMLReader(graphFile).readGraph());
					
					// Load network component definitions
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GraphIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// Load the component definitions from the component file
			File componentFile = new File(envFilePath+File.separator+baseFileName+".xml");
			if(componentFile.exists()){
				try {
					JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
					Unmarshaller unmarsh = context.createUnmarshaller();
					NetworkComponentList compList = (NetworkComponentList) unmarsh.unmarshal(new FileReader(componentFile));
					networkModel.setNetworkComponents(compList.getComponentList());
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}		
		setChanged();
		notifyObservers(new Integer(EVENT_NETWORKMODEL_LOADED));
	}


	
}
