package mas.environment;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import application.Application;
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
		currentProject.ec = this;
//		this.currentProject.setEnvCont(this);
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
	
	public void buildNewEnv(Element root){
		this.mainPlayground = new Playground(root);
//		currentProject.setEnvironment(this.mainPlayground);
	}
	
	public void loadSvgFile(File svgFile){
		if(svgFile.exists()){
			try {
				System.out.println("Lade SVG-Datei "+svgFile.getName());
				this.gui.getCanvas().setURI(svgFile.toURI().toURL().toString());
				this.svgDoc = this.gui.getCanvas().getSVGDocument();
				
				currentProject.setSvgFileName(svgFile.getName());
				currentProject.ProjectUnsaved=true;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			currentProject.setSvgDoc(svgDoc);
		}
	}
	
	/**
	 * Speichert eine Beschreibung der Umgebung als XML Datei  
	 * @param mainPlayground Wurzel-Playground der Umgebung
	 */
	public void saveEnvironment(){
//		File tempFile = new File(currentProject.getProjectFolderFullPath()+"temp.xml");
		File envFile = new File(currentProject.getProjectFolderFullPath()+currentProject.getEnvFileName());
//		if(!tempFile.exists()){
//			try {
//				tempFile.createNewFile();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		if(!envFile.exists()){
			try {
				envFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Speichere Umgebung nach "+envFile.getName());
		System.out.println(mainPlayground.getAgents().size()+" Agenten");
		System.out.println(mainPlayground.getObstacles().size()+" Hindernisse");
		System.out.println(mainPlayground.getPlaygrounds().size()+" Kind-Umgebungen");
		
		Document envDoc = SVGDOMImplementation.getDOMImplementation().createDocument(null, "environment", null);
		Element envRoot = envDoc.getDocumentElement();
		envRoot.setAttribute("project", currentProject.getProjectName());
		envRoot.appendChild(mainPlayground.saveAsXML(envDoc));
		
		try {
			FileWriter fw = new FileWriter(envFile);
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			DOMUtilities.writeDocument(envDoc, fw);
			fw.close();
		} catch (IOException e) {
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
					System.out.println("Lade Umgebung aus "+svgFile.getName()+"...");
					this.mainPlayground = new Playground();
					mainPlayground.loadFromXML(mainPg);
					if(mainPg.hasChildNodes()){
						NodeList children = mainPg.getChildNodes();
						for(int i=0; i<children.getLength(); i++){
							loadObject((Element) children.item(i), this.mainPlayground);
						}
					}
					System.out.println(this.mainPlayground.getAgents().size()+" Agenten");
					System.out.println(this.mainPlayground.getObstacles().size()+" Hindernisse");
					System.out.println(this.mainPlayground.getPlaygrounds().size()+" Kind-Umgebungen");					
				}
				
//				currentProject.setEnvironment(mainPlayground);
				
				
				
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
			System.out.println("Keine Umgebungsdatei gefunden.");
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
	
	public void startSimulation(){
		
//		if ( Application.JadePlatform.jadeMainContainerIsRunning(true)) {
			// DisplayAgent
			
//			String agentNameBase = "DA";
//			int agentNameSuffix = 1;
//			String agentName = agentNameBase+agentNameSuffix;
//			
//			while( Application.JadePlatform.jadeAgentIsRunning(agentName)){
//				agentNameSuffix++;
//				agentName = agentNameBase + agentNameSuffix;
//			}	
//			System.out.println("Agent name "+agentNameBase);				
//						
//			Object[] args = new Object[2];
//			args[0] = mainPlayground;
//			args[1] = svgDoc;
//			Application.JadePlatform.jadeAgentStart(agentName, "mas.display.DisplayAgent2", args, currentProject.getProjectFolder() );
			
			// Agent Objekte aus der Umgebung
			Iterator<AgentObject> agents = mainPlayground.getAgents().iterator();
			while(agents.hasNext()){
				startAgent(agents.next());
			}
//		}
	}
	
	public void startAgent(AgentObject agent){
		String agentName = agent.getId();
		String agentClass = agent.getAgentClass();
		AgentObject[] arg = new AgentObject[1];
		arg[0]=agent;
		Application.JadePlatform.jadeAgentStart(agentName, agentClass, arg, currentProject.getProjectFolder() );
	}
	
	
}
