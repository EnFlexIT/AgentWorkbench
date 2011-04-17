package agentgui.graphEnvironment.controller.yedGraphml;

import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import agentgui.core.application.Language;
import agentgui.graphEnvironment.controller.GraphFileImporter;
import agentgui.graphEnvironment.environmentModel.GraphElementSettings;
import agentgui.graphEnvironment.environmentModel.GridModel;
import agentgui.graphEnvironment.prototypes.GraphElementPrototype;
/**
 * GraphFileImporter for GraphML files specified with yEd
 * @author Nils
 *
 */
public class YedGraphMLFileImporter extends GraphFileImporter {
	/**
	 * The file extension used for filtering in JFileChooser selecting the file to import 
	 */
	private String fileTypeExtension = "graphml";
	/**
	 * The file type description for the JFileChooser for selecting the file to import
	 */
	private String fileTypeDescription = "yEd GraphML";
	/**
	 * Intermediate graph, after loading the GraphML file, before converting to a GridModel   
	 */
	private Graph<TempNode, Object> tempGraph;
	/**
	 * The GridModel created from the imported yEd GraphML file
	 */
	private GridModel gridModel;
	
	private HashMap<String, GraphElementPrototype> addedElements = null;
	
	public YedGraphMLFileImporter(HashMap<String, GraphElementSettings> elementSettings) {
		super(elementSettings);
		addedElements = new HashMap<String, GraphElementPrototype>();
	}

	@Override
	public GridModel loadGraphFromFile(File graphFile) {
		// GraphML parser instance
		YedGraphMLParser parser = new YedGraphMLParser();
		// List of the graphs start nodes
		Vector<TempNode> startNodesList = null;
		
		// Load the intermediate graph from the yEd file
		tempGraph = parser.getGraph(graphFile);
		if (tempGraph==null) {
			return null;
		} else {
//			showDebugView();
			// Find the graphs start nodes
			Iterator<TempNode> nodes = tempGraph.getVertices().iterator();
			startNodesList = new Vector<TempNode>();
			while(nodes.hasNext()){
				TempNode node = nodes.next();
				if(tempGraph.inDegree(node) == 0){
					startNodesList.add(node);
				}
			}	
		}
		
		// Build the final graph, starting from the start nodes
		if (startNodesList!=null) {
			Iterator<TempNode> startNodes = startNodesList.iterator();
			gridModel = new GridModel();
			while(startNodes.hasNext()){
				TempNode startNode = startNodes.next();
				addElement(startNode, null);
			}
		}		
		return gridModel;
	}
	
	private void addElement(TempNode tempElement, GraphElementPrototype predecessor){
		GraphElementPrototype newElement = null;
		
		try {
			newElement = (GraphElementPrototype) Class.forName(elementSettings.get(tempElement.getType()).getGraphPrototype()).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(newElement != null){
			newElement.setId(tempElement.getId());
			newElement.setType(tempElement.getType());
			
			// Look for an existing successor
			// Not supporting multiple successors at the moment !!!
			Iterator<TempNode> successors = tempGraph.getSuccessors(tempElement).iterator();
			GraphElementPrototype successor = null;
			while(successors.hasNext()){
				TempNode succTempNode = successors.next();
				successor = addedElements.get(succTempNode.getId());
			}
			
			if(predecessor == null && successor == null){
				newElement.addToGraph(gridModel.getGraph());
			}else if(predecessor != null && successor == null){
				newElement.addAfter(gridModel.getGraph(), predecessor);
			}else if(predecessor == null && successor != null){
				newElement.addBefore(gridModel.getGraph(), successor);
			}else if(predecessor != null && successor != null){
				newElement.addBetween(gridModel.getGraph(), predecessor, successor);
			}
			
			// Call recursively for successor nodes
			// New iterator 
			successors = tempGraph.getSuccessors(tempElement).iterator();
			while(successors.hasNext()){
				TempNode succTempNode = successors.next();
				addElement(succTempNode, newElement);
			}
		}else{
			System.err.println(Language.translate("Fehler beim Instanziieren des GraphElementPrototyps für Element-Typ ")+tempElement.getType());
		}
	}

	@Override
	public String getGraphFileExtension() {
		return fileTypeExtension;
	}

	@Override
	public String getTypeString() {
		return fileTypeDescription;
	}
	
	/**
	 * Show a simple visualization of the tempGraph for debugging purposes
	 */
	@SuppressWarnings("unused")
	private void showDebugView(){
		Layout<TempNode, Object> tempGraphLayout = new FRLayout<TempNode, Object>(tempGraph, new Dimension(300, 300));
		VisualizationViewer<TempNode, Object> visView = new VisualizationViewer<TempNode, Object>(tempGraphLayout, new Dimension(350, 350));
		DefaultModalGraphMouse<TempNode, Object> dgm = new DefaultModalGraphMouse<TempNode, Object>();
		dgm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		visView.addMouseListener(dgm);
		visView.getRenderContext().setVertexLabelTransformer(new Transformer<TempNode, String>() {
			
			@Override
			public String transform(TempNode arg0) {
				return arg0.getType();
			}
		});
		JFrame debugViewFrame = new JFrame("TempGraph Debug View");
		debugViewFrame.getContentPane().add(visView);
		debugViewFrame.pack();
		debugViewFrame.setVisible(true);
	}

}
