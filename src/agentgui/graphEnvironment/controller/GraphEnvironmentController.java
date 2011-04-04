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
	
	public static final Integer EVENT_AGENT_CLASSES_SET = 1;
	
	private String graphFilePath = null;
	
	private String currGraphFileName = null;
	
	private Project project = null;
	
	private GridModel gridModel = null;
	
	private GraphFileImporter graphFileImporter = null;
	
	private GraphMLWriter<PropagationPoint, GridComponent> graphMLWriter = null;
	
	public GraphEnvironmentController(Project project){
		this.project = project;
		this.project.addObserver(this);
		graphFilePath = this.project.getProjectFolderFullPath()+this.project.getSubFolderEnvSetups();
		loadGridModel();
	}
	
	Project getProject(){
		return this.project;
	}

	public HashMap<String, String> getAgentClasses() {
		return project.simSetups.getCurrSimSetup().getAgentClassesHash();
	}

	public void setAgentClasses(HashMap<String, String> agentClasses) {
		project.simSetups.getCurrSimSetup().setAgentClassesHash(agentClasses);
		project.isUnsaved=true;
		setChanged();
		notifyObservers(EVENT_AGENT_CLASSES_SET);
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
	
	private GraphMLWriter<PropagationPoint, GridComponent> getGraphMLWriter(){
		if(graphMLWriter == null){
			graphMLWriter = new GraphMLWriter<PropagationPoint, GridComponent>();
			graphMLWriter.setEdgeIDs(new Transformer<GridComponent, String>() {

				@Override
				public String transform(GridComponent arg0) {
					return arg0.getAgentID();
				}
			});
			graphMLWriter.setEdgeDescriptions(new Transformer<GridComponent, String>() {

				@Override
				public String transform(GridComponent arg0) {
					return arg0.getType();
				}
			});
			graphMLWriter.setVertexIDs(new Transformer<PropagationPoint, String>() {

				@Override
				public String transform(PropagationPoint arg0) {
					return ""+arg0.getIndex();
				}
			});
		}
		return graphMLWriter;
	}
	
	private GraphMLReader2<Graph<PropagationPoint, GridComponent>, PropagationPoint, GridComponent> getGraphMLReader(File file) throws FileNotFoundException{
		Transformer<GraphMetadata, Graph<PropagationPoint, GridComponent>> graphTransformer = new Transformer<GraphMetadata, Graph<PropagationPoint,GridComponent>>() {

			@Override
			public SparseGraph<PropagationPoint, GridComponent> transform(
					GraphMetadata gmd) {
				return new SparseGraph<PropagationPoint, GridComponent>();
			}
		};
		
		Transformer<NodeMetadata, PropagationPoint> nodeTransformer = new Transformer<NodeMetadata, PropagationPoint>() {

			@Override
			public PropagationPoint transform(NodeMetadata nmd) {
				return new PropagationPoint(Integer.parseInt(nmd.getId()));
			}
		};
		
		Transformer<EdgeMetadata, GridComponent> edgeTransformer = new Transformer<EdgeMetadata, GridComponent>() {

			@Override
			public GridComponent transform(EdgeMetadata emd) {
				return new GridComponent(emd.getId(), emd.getDescription());
			}
		};
		
		Transformer<HyperEdgeMetadata, GridComponent> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, GridComponent>() {

			@Override
			public GridComponent transform(HyperEdgeMetadata arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		FileReader fileReader = new FileReader(file);
		
		return new GraphMLReader2<Graph<PropagationPoint,GridComponent>, PropagationPoint, GridComponent>(fileReader, graphTransformer, nodeTransformer, edgeTransformer, hyperEdgeTransformer);
		
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
