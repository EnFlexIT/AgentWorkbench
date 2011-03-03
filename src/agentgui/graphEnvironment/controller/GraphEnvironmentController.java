package agentgui.graphEnvironment.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.common.FileCopier;
import agentgui.graphEnvironment.controller.yedGraphml.YedGraphMLFileLoader2;

public class GraphEnvironmentController extends Observable implements
		Observer {
	
	/**
	 * @param svgDoc the svgDoc to set
	 */
	public void setSvgDoc(Document svgDoc) {
		this.svgDoc = svgDoc;
	}

	/**
	 * @param graph the graph to set
	 */
	public void setGridModel(GridModel2 gridModel) {
		
		this.gridModel = gridModel;
		this.setChanged();
		notifyObservers(EVENT_GRAPH_LOADED);
	}
	
	
	public static final Integer EVENT_SVG_LOADED = 0;
	
	public static final Integer EVENT_GRAPH_LOADED = 1;
	
	public static final Integer EVENT_ONTOLOGY_CLASSES_SET = 2;
	
	public static final Integer EVENT_AGENT_CLASSES_SET = 3;
	
//	public HashMap<String, String> getOntologyClasses() {
//		return project.getOntoClassHash();
//	}

//	public void setOntologyClasses(HashMap<String, String> ontologyClasses) {
//		project.setOntoClassHash(ontologyClasses);
//		project.isUnsaved=true;
//		setChanged();
//		notifyObservers(EVENT_ONTOLOGY_CLASSES_SET);
//	}

	public HashMap<String, String> getAgentClasses() {
		return project.getAgentClassHash();
	}

	public void setAgentClasses(HashMap<String, String> agentClasses) {
		project.setAgentClassHash(agentClasses);
		project.isUnsaved=true;
		setChanged();
		notifyObservers(EVENT_AGENT_CLASSES_SET);
	}
	
	private GraphFileLoader2 fileLoader = null;

	private Project project = null;
	
	private Document svgDoc = null;
	
	private GridModel2 gridModel = null;
	
	public GraphEnvironmentController(Project project){
		this.project = project;
		this.loadFiles();
	}
	
	Project getProject(){
		return this.project;
	}

	/**
	 * This method loads a new graph definition from graphMLFile
	 * The praphMLFile and, if existing, an equal named SVG file in the same 
	 * directory, are copied to the project's environment setup path. Both are
	 * loaded, and the environment and SVG file names of the current setup are
	 * set.  
	 * @param graphMLFile The GraphML file defining the new graph.
	 */
	public void loadGridModel(File graphMLFile){
		String graphMLSourcePath = graphMLFile.getAbsolutePath();
		String graphMLTargetPath = project.getEnvSetupPath()+File.separator+project.simSetupCurrent+".graphml";
		
		String svgSourcePath = graphMLFile.getAbsolutePath().substring(0, graphMLFile.getAbsolutePath().lastIndexOf('.'))+".svg";
		String svgTargetPath = project.getEnvSetupPath()+File.separator+project.simSetupCurrent+".svg";
		boolean svgFound = new File(svgSourcePath).exists();
		
		FileCopier fc = new FileCopier();
		fc.copyFile(graphMLSourcePath, graphMLTargetPath);
		project.simSetups.getCurrSimSetup().setEnvironmentFileName(graphMLTargetPath.substring(graphMLTargetPath.lastIndexOf(File.separator)));
		if(svgFound){
			fc.copyFile(svgSourcePath, svgTargetPath);
			project.simSetups.getCurrSimSetup().setSvgFileName(svgTargetPath.substring(svgTargetPath.lastIndexOf(File.separator)));
		}else{
			System.err.println("Keine SVG-Datei gefunden!");
		}
		
		this.gridModel = loadGraphFile(new File(graphMLTargetPath));
		setChanged();
		notifyObservers(EVENT_GRAPH_LOADED);
		if(svgFound){
			this.svgDoc = loadSVGFile(new File(svgTargetPath));
			if(this.svgDoc != null){
				setChanged();
				notifyObservers(EVENT_SVG_LOADED);
			}
		}
		project.isUnsaved = true;
	}
	
	/**
	 * This method loads the graph and SVG specified in the current SimSetup 
	 */
	public void loadFiles(){
		String graphFileName = project.simSetups.getCurrSimSetup().getEnvironmentFileName();
		if(graphFileName != null && graphFileName != ""){
			File graphFile = new File(project.getEnvSetupPath()+File.separator+graphFileName);
			if(graphFile.exists()){
				setGridModel(loadGraphFile(graphFile));
			}else{
				System.err.println(Language.translate("Graph-Datei")+" "+graphFile.getName()+" "+Language.translate(" nicht gefunden!"));
			}
		}else{
			System.out.println(Language.translate("Keine Graph-Datei definiert!"));
		}
		
		String svgFileName = project.simSetups.getCurrSimSetup().getSvgFileName();
		if(svgFileName != null && svgFileName != ""){
			File svgFile = new File(project.getEnvSetupPath()+File.separator+svgFileName);
			if(svgFile.exists()){
				this.svgDoc = loadSVGFile(svgFile);
			}else{
				System.err.println(Language.translate("SVG-Datei")+" "+svgFile.getName()+" "+Language.translate(" nicht gefunden!"));
			}
		}else{
			System.out.println(Language.translate("Keine SVG-Datei definiert!"));
		}
	}
	
	/**
	 * This method loads a graph definition from a GraphMLFile and creates a corresponding JUNG graph
	 * @param graphMLFile The GraphML file defining the new graph.
	 * @return The corresponding JUNG graph
	 */
	private GridModel2 loadGraphFile(File graphMLFile){
		fileLoader = new YedGraphMLFileLoader2();
		System.out.println("Lade Graph-Datei "+graphMLFile.getName());
		return fileLoader.loadGraphFromFile(graphMLFile);
	}
	
	/**
	 * This method loads a SVG document from a SVG file.
	 * @param svgFile The SVG file.
	 * @return The SVG document
	 */
	private Document loadSVGFile(File svgFile){
		Document doc = null;
		if(svgFile.exists()){
			doc = SVGDOMImplementation.getDOMImplementation().createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
			System.out.println(Language.translate("Lade SVG-Datei")+" "+svgFile.getName());
			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
			try {
				doc = factory.createDocument(svgFile.toURI().toURL().toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return doc;
	}
	
	/**
	 * @return the graph
	 */
	public GridModel2 getGridModel() {
		return gridModel;
	}

	/**
	 * @return the svgDoc
	 */
	public Document getSvgDoc() {
		return svgDoc;
	}
	
	GraphFileLoader2 getFileLoader(){
		return fileLoader;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
