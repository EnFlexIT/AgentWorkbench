package agentgui.graphEnvironment.controller;

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
import agentgui.graphEnvironment.environmentModel.GraphEdge;
import agentgui.graphEnvironment.environmentModel.GraphElementSettings;
import agentgui.graphEnvironment.environmentModel.GraphNode;
import agentgui.graphEnvironment.environmentModel.GridModel;
import agentgui.graphEnvironment.environmentModel.NetworkComponentList;

public class GraphEnvironmentController extends Observable implements Observer {
	
	public static final Integer EVENT_GRIDMODEL_CHANGED = 0;
	
	public static final Integer EVENT_ELEMENT_TYPES_SETTINGS_CHANGED = 1;
	
	private String envFilePath = null;
	
	private String baseFileName = null;
	
	private Project project = null;
	
	private GridModel gridModel = null;
	
	private HashMap<String, GraphElementSettings> currentGts = null;
	
	private GraphFileImporter graphFileImporter = null;
	
	private GraphMLWriter<GraphNode, GraphEdge> graphMLWriter = null;
	
	public GraphEnvironmentController(Project project){
		this.project = project;
		this.project.addObserver(this);
		envFilePath = this.project.getProjectFolderFullPath()+this.project.getSubFolderEnvSetups();
		loadGridModel();
		setGraphElementSettings(project.simSetups.getCurrSimSetup().getGraphElementSettings());
		// If no ETS are specified in the setup, assign an empty HashMap to avoid null pointers
		if(currentGts == null){
			currentGts = new HashMap<String, GraphElementSettings>();
		}
	}
	
	Project getProject(){
		return this.project;
	}

	public void setGraphElementSettings(HashMap<String, GraphElementSettings> etsVector){
		currentGts = etsVector;
		project.simSetups.getCurrSimSetup().setGraphElementSettings(etsVector);
		project.isUnsaved=true;
		setChanged();
		notifyObservers(EVENT_ELEMENT_TYPES_SETTINGS_CHANGED);
	}
	
	public HashMap<String, GraphElementSettings> getGraphElementSettings(){
		return this.currentGts;
	}
	
	/**
	 * This method loads a new graph definition from graphMLFile
	 * The praphMLFile and, if existing, an equal named SVG file in the same 
	 * directory, are copied to the project's environment setup path. Both are
	 * loaded, and the environment and SVG file names of the current setup are
	 * set.  
	 * @param graphMLFile The GraphML file defining the new graph.
	 */
	public void importGridModel(File graphMLFile){
		gridModel = getGraphFileImporter().loadGraphFromFile(graphMLFile);
		if(gridModel != null){
			this.setChanged();
			notifyObservers(EVENT_GRIDMODEL_CHANGED);
		}
		project.isUnsaved = true;
	}
	
	/**
	 * @return the graph
	 */
	public GridModel getGridModel() {
		return gridModel;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(project) && arg == Project.SAVED){
			saveGridModel();
		}else if(o.equals(project) && arg instanceof SimulationSetupsChangeNotification){
			handleSetupChange((SimulationSetupsChangeNotification) arg);
		}
	}
	
	GraphFileImporter getGraphFileImporter(){
		if(graphFileImporter == null){
			graphFileImporter = new YedGraphMLFileImporter(currentGts);
		}
		return graphFileImporter;
	}
	
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
					return arg0.getType();
				}
			});
			graphMLWriter.setVertexIDs(new Transformer<GraphNode, String>() {

				@Override
				public String transform(GraphNode arg0) {
					return arg0.getId();
				}
			});
		}
		return graphMLWriter;
	}
	
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
	
	
private void handleSetupChange(SimulationSetupsChangeNotification sscn){
		
		switch(sscn.getUpdateReason()){
			
			case SimulationSetups.SIMULATION_SETUP_COPY:
				System.out.println("Testausgabe: kopiere Setup");
				updateGraphFileName();
				saveGridModel();
			break;
			
			case SimulationSetups.SIMULATION_SETUP_ADD_NEW:
				System.out.println("Testausgabe: Erzeuge Setup");
				updateGraphFileName();
				gridModel = new GridModel();
				currentGts = null;
			break;
			
			case SimulationSetups.SIMULATION_SETUP_REMOVE:
				System.out.println("Testausgabe: Lösche Setup");
				File graphFile = new File(envFilePath+File.separator+baseFileName+".graphml");
				if(graphFile.exists()){
					graphFile.delete();
				}
			// No, there's no break missing here. After deleting a setup another one is loaded.
			
			case SimulationSetups.SIMULATION_SETUP_LOAD:
				System.out.println("Testausgabe: Lade Setup");
				updateGraphFileName();
				loadGridModel();
				setGraphElementSettings(project.simSetups.getCurrSimSetup().getGraphElementSettings());
			break;
			
			case SimulationSetups.SIMULATION_SETUP_RENAME:
				System.out.println("Testausgabe: Benenne Setup um");
				File oldGraphFile = new File(envFilePath+File.separator+baseFileName+".graphml");
				updateGraphFileName();
				if(oldGraphFile.exists()){
					File newGraphFile = new File(envFilePath+File.separator+baseFileName+".graphml");
					oldGraphFile.renameTo(newGraphFile);
				}
			break;
			
			case SimulationSetups.SIMULATION_SETUP_SAVED:
				System.out.println("Testausgabe: Speichere Setup");
			break;
		}
		
		setChanged();
		notifyObservers(EVENT_GRIDMODEL_CHANGED);
		
	}

	private void updateGraphFileName(){
		baseFileName = project.simSetupCurrent;
		System.out.println("Testausgabe: "+baseFileName);
		project.simSetups.getCurrSimSetup().setEnvironmentFileName(baseFileName+".graphml");
	}
	
	private void saveGridModel(){
		if(gridModel != null && gridModel.getGraph() != null){
			
			try {
				// Save the graph topology 
				String graphFileName = baseFileName+".graphml";
				File file = new File(envFilePath+File.separator+graphFileName);
				if(!file.exists()){
					file.createNewFile();
				}
				PrintWriter pw = new PrintWriter(new FileWriter(file));
				getGraphMLWriter().save(gridModel.getGraph(), pw);
				
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

				marsh.marshal(new NetworkComponentList(gridModel.getNetworkComponents()), componentFileWriter);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void loadGridModel(){
		
		gridModel = new GridModel();
		
		String fileName = project.simSetups.getCurrSimSetup().getEnvironmentFileName();
		if(fileName != null){
			
			String folderPath = this.project.getProjectFolderFullPath()+this.project.getSubFolderEnvSetups();
			File graphFile = new File(folderPath+File.separator+fileName);
			if(graphFile.exists()){
				baseFileName = fileName.substring(0, fileName.lastIndexOf('.'));
				try {
					// Load graph topology
					gridModel.setGraph(getGraphMLReader(graphFile).readGraph());
					
					// Load network component definitions
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GraphIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			File componentFile = new File(envFilePath+File.separator+baseFileName+".xml");
			if(componentFile.exists()){
				try {
					JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
					Unmarshaller unmarsh = context.createUnmarshaller();
					NetworkComponentList compList = (NetworkComponentList) unmarsh.unmarshal(new FileReader(componentFile));
					gridModel.setNetworkComponents(compList.getComponentList());
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
		notifyObservers(new Integer(EVENT_GRIDMODEL_CHANGED));
	}
}
