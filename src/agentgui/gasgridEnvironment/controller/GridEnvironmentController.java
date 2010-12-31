package agentgui.gasgridEnvironment.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import edu.uci.ics.jung.graph.Graph;

import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.common.FileCopier;
import agentgui.gasgridEnvironment.ontology.GridComponent;
import agentgui.gasgridEnvironment.ontology.GridLink;

public class GridEnvironmentController extends Observable implements
		Observer {
	
	public static final String SVG_LOADED = "svg loaded";
	
	private Project project = null;
	
	private Document svgDoc = null;
	
	private Graph<GridComponent, GridLink> graph = null;
	
	public GridEnvironmentController(Project project){
		this.project = project;
	}

	/**
	 * This method loads a new graph definition from graphMLFile
	 * The praphMLFile and, if existing, an equal named SVG file in the same 
	 * directory, are copied to the project's environment setup path. Both are
	 * loaded, and the environment and SVG file names of the current setup are
	 * set.  
	 * @param graphMLFile The GraphML file defining the new graph.
	 */
	public void loadNewGrap(File graphMLFile){
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
		
		this.graph = loadGraphFile(new File(graphMLTargetPath));
		if(svgFound){
			this.svgDoc = loadSVGFile(new File(svgTargetPath));
			if(this.svgDoc != null){
				setChanged();
				notifyObservers(SVG_LOADED);
			}
		}
	}
	
	/**
	 * This method loads the graph and SVG specified in the current SimSetup 
	 */
	public void loadSimSetupFiles(){
		String graphFileName = project.simSetups.getCurrSimSetup().getEnvironmentFileName();
		if(graphFileName != null && graphFileName != ""){
			this.graph = loadGraphFile(new File(project.getEnvSetupPath()+File.separator+graphFileName));
		}else{
			System.out.println("Keine Graph-Datei definiert!");
		}
		
		String svgFileName = project.simSetups.getCurrSimSetup().getSvgFileName();
		if(svgFileName != null && svgFileName != ""){
			this.svgDoc = loadSVGFile(new File(project.getEnvSetupPath()+File.separator+svgFileName));
			if(this.svgDoc != null){
				setChanged();
				notifyObservers(SVG_LOADED);
			}
		}
	}
	
	/**
	 * This method loads a graph definition from a GraphMLFile and creates a corresponding JUNG graph
	 * @param graphMLFile The GraphML file defining the new graph.
	 * @return The corresponding JUNG graph
	 */
	private Graph<GridComponent, GridLink> loadGraphFile(File graphMLFile){
		Graph<GridComponent, GridLink> graph = null;
		GraphParser gp = new GraphParser();
		graph = gp.getGraph(graphMLFile);
		System.out.println("Lade Graph-Datei "+graphMLFile.getName());
		return graph;
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
	public Graph<GridComponent, GridLink> getGraph() {
		return graph;
	}

	/**
	 * @return the svgDoc
	 */
	public Document getSvgDoc() {
		return svgDoc;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
