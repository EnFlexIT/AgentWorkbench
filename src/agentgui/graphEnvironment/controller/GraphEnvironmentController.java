package agentgui.graphEnvironment.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

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

public class GraphEnvironmentController extends Observable implements Observer {
	
	public static final Integer EVENT_GRIDMODEL_CHANGED = 0;
	
	public static final Integer EVENT_ELEMENT_TYPES_SETTINGS_CHANGED = 1;
	
	private String graphFilePath = null;
	
	private String currGraphFileName = null;
	
	private Project project = null;
	
	private GridModel gridModel = null;
	
	private Vector<ElementTypeSettings> currentEts = null;
	
	private GraphFileImporter graphFileImporter = null;
	
	private GraphMLWriter<GraphNode, GraphEdge> graphMLWriter = null;
	
	public GraphEnvironmentController(Project project){
		this.project = project;
		this.project.addObserver(this);
		graphFilePath = this.project.getProjectFolderFullPath()+this.project.getSubFolderEnvSetups();
		loadGridModel();
		setElementTypeSettings(project.simSetups.getCurrSimSetup().getElementTypeSettings());
	}
	
	Project getProject(){
		return this.project;
	}

	public void setElementTypeSettings(Vector<ElementTypeSettings> etsVector){
		currentEts = etsVector;
		project.simSetups.getCurrSimSetup().setElementTypeSettings(etsVector);
		project.isUnsaved=true;
		setChanged();
		notifyObservers(EVENT_ELEMENT_TYPES_SETTINGS_CHANGED);
	}
	
	public Vector<ElementTypeSettings> getElementTypeSettings(){
		return this.currentEts;
	}
	
	public ElementTypeSettings getElementTypeSettingsByType(String type){
		Iterator<ElementTypeSettings> etsIter = currentEts.iterator();
		while(etsIter.hasNext()){
			ElementTypeSettings ets = etsIter.next();
			if(ets.getName().equals(type)){
				return ets;
			}
		}
		return null;
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
			graphFileImporter = new YedGraphMLFileImporter();
		}
		return graphFileImporter;
	}
	
	private GraphMLWriter<GraphNode, GraphEdge> getGraphMLWriter(){
		if(graphMLWriter == null){
			graphMLWriter = new GraphMLWriter<GraphNode, GraphEdge>();
			graphMLWriter.setEdgeIDs(new Transformer<GraphEdge, String>() {

				@Override
				public String transform(GraphEdge arg0) {
					return arg0.id();
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
				currentEts = null;
			break;
			
			case SimulationSetups.SIMULATION_SETUP_REMOVE:
				System.out.println("Testausgabe: Lösche Setup");
				File graphFile = new File(graphFilePath+File.separator+currGraphFileName);
				if(graphFile.exists()){
					graphFile.delete();
				}
			// No, there's no break missing here. After deleting a setup another one is loaded.
			
			case SimulationSetups.SIMULATION_SETUP_LOAD:
				System.out.println("Testausgabe: Lade Setup");
				updateGraphFileName();
				loadGridModel();
				setElementTypeSettings(project.simSetups.getCurrSimSetup().getElementTypeSettings());
			break;
			
			case SimulationSetups.SIMULATION_SETUP_RENAME:
				System.out.println("Testausgabe: Benenne Setup um");
				File oldGraphFile = new File(graphFilePath+File.separator+currGraphFileName);
				updateGraphFileName();
				if(oldGraphFile.exists()){
					File newGraphFile = new File(graphFilePath+File.separator+currGraphFileName);
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
		currGraphFileName = project.simSetupCurrent+".graphml";
		project.simSetups.getCurrSimSetup().setEnvironmentFileName(currGraphFileName);
	}
	
	private void saveGridModel(){
		if(gridModel != null && gridModel.getGraph() != null){
			try {
				File file = new File(graphFilePath+File.separator+currGraphFileName);
				if(!file.exists()){
					file.createNewFile();
				}
				PrintWriter pw = new PrintWriter(new FileWriter(file));
				getGraphMLWriter().save(gridModel.getGraph(), pw);
			} catch (IOException e) {
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
			File envFile = new File(folderPath+File.separator+fileName);
			if(envFile.exists()){
				currGraphFileName = fileName;
				try {
					gridModel.setGraph(getGraphMLReader(envFile).readGraph());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GraphIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		setChanged();
		notifyObservers(new Integer(EVENT_GRIDMODEL_CHANGED));
	}
}
