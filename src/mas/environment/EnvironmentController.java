package mas.environment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.PipedReader;
//import java.io.PipedWriter;
import java.net.MalformedURLException;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.svg2svg.PrettyPrinter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import application.Project;

import svganalyzer.SvgAnalyzer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EnvironmentController {
	
	private SvgAnalyzer gui = null;
	private Project currentProject = null;	
	private Playground mainPlayground = null;
	private Document svgDoc = null;
	
	/**
	 * 
	 */
	private boolean newEnv = false; 
	
	public EnvironmentController(){
		
	};
	
	
	
	public EnvironmentController(SvgAnalyzer gui, Project proj){
		this.gui = gui;
		this.currentProject = proj;
		if(currentProject.getSvgPath()!=null){
			loadSvgFile(new File(currentProject.getSvgPath()));			
		}else{
			System.out.println("No SVG file specified");
		}
		this.loadEnvironment();
	}
	
	public Playground getMainPlayground(){
		return this.mainPlayground;
	}
	
	public void setMainPlayground(Playground pg){
		this.mainPlayground = pg;
	}
	
	public Document getSvgDoc(){
		return this.svgDoc;
	}
	public void setSvgDoc(Document doc){
		this.svgDoc = doc;
	}
	
	public boolean isNewEnv(){
		return this.newEnv;
	}
	public void setNewEnv(boolean isNew){
		this.newEnv = isNew;
	}
	
	public void loadSvgFile(File svgFile){
		if(svgFile.exists()){
			try {
				System.out.println("Loading SVG File "+svgFile.getName());
				this.gui.getCanvas().setURI(svgFile.toURI().toURL().toString());
				this.svgDoc = this.gui.getCanvas().getSVGDocument();
				
				currentProject.setSvgFileName(svgFile.getName());
				currentProject.ProjectUnsaved=true;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Speichert eine Beschreibung der Umgebung als XML Datei  
	 * @param mainPlayground Wurzel-Playground der Umgebung
	 */
	public void saveEnvironment(){
		File tempFile = new File(currentProject.getProjectFolderFullPath()+"temp.xml");
		File envFile = new File(currentProject.getProjectFolderFullPath()+currentProject.getEnvFileName());
		if(!tempFile.exists()){
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!envFile.exists()){
			try {
				envFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Saving Environment to "+envFile.getName());
		System.out.println("Agents:            "+mainPlayground.getAgents().size());
		System.out.println("Objects:           "+mainPlayground.getObstacles().size());
		System.out.println("Child playgrounds: "+mainPlayground.getPlaygrounds().size());
		
		Document envDoc = SVGDOMImplementation.getDOMImplementation().createDocument(null, "environment", null);
		Element envRoot = envDoc.getDocumentElement();
		envRoot.setAttribute("project", currentProject.getProjectName());
		envRoot.appendChild(mainPlayground.saveAsXML(envDoc));
		
		// Ohne PrettyPrinter
		try {
			FileWriter fw = new FileWriter(tempFile);
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			DOMUtilities.writeDocument(envDoc, fw);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Mit PrettyPrinter  !!! Funktioniert noch nicht !!!
		try {
//			PipedWriter pw = new PipedWriter();
//			PipedReader pr = new PipedReader();
			PrettyPrinter pp = new PrettyPrinter();
			FileReader fr = new FileReader(tempFile);
			FileWriter fw = new FileWriter(envFile);
			
//			pr.connect(pw);
//			pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
//			DOMUtilities.writeDocument(envDoc, pw);
			pp.print(fr, fw);
			fw.close();
			tempFile.delete();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadEnvironment(){
		
		File svgFile = new File(currentProject.getProjectFolderFullPath()+currentProject.getEnvFileName());
		if(svgFile.exists()){
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				svgDoc = builder.parse(svgFile);
				
				Element mainPg = (Element) svgDoc.getDocumentElement().getFirstChild();
				if(mainPg.getTagName().equals("playground")){
					System.out.println("Loading Environment from "+svgFile.getName()+"...");
					this.mainPlayground = new Playground();
					mainPlayground.loadFromXML(mainPg);
					if(mainPg.hasChildNodes()){
						NodeList children = mainPg.getChildNodes();
						for(int i=0; i<children.getLength(); i++){
							loadObject((Element) children.item(i), this.mainPlayground);
						}
					}
					System.out.println(this.mainPlayground.getAgents().size()+" agents loaded");
					System.out.println(this.mainPlayground.getObstacles().size()+" obstacles loaded");
					System.out.println(this.mainPlayground.getPlaygrounds().size()+" child playgrounds loaded");
					
				}
				
				
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("No environment file found.");
			this.newEnv=true;
		}
	}
	
	public void loadObject(Element xml, Playground parent){
		if(xml.getTagName().equals("obstacle")){
			ObstacleObject object = new ObstacleObject();
			object.loadFromXML(xml);
			
			parent.addObstacle(object);
		}else if(xml.getTagName().equals("agent")){
			AgentObject agent = new AgentObject();
			agent.loadFromXML(xml);
			parent.addAgent(agent);
		}else if(xml.getTagName().equals("playground")){
			Playground pg = new Playground();
			pg.loadFromXML(xml);
			if(xml.hasChildNodes()){
				NodeList children = xml.getChildNodes();
				for(int i=0; i<children.getLength(); i++){
					loadObject((Element) children.item(i), pg);
				}
			}
			parent.addPlayground(pg);
		}
	}
	
	
}
