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
import application.Language;
import application.Project;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Verwaltet die Umgebung für ein Projekt
 * @author Nils
 *
 */
public class EnvironmentController {
	
	/**
	 * GUI des EnvironmentControllers 
	 */
	private EnvironmentControllerGUI gui = null;
	/**
	 * Projekt
	 */
	private Project currentProject = null;
	/**
	 * (Wurzel-)Umgebung des Projektes
	 */
	private Playground mainPlayground = null;
	/**
	 * SVG-Dokument des Projektes
	 */
	private Document svgDoc = null;
	
	/**
	 * Wenn false wurde die Umgebung aus einer Datei geladen
	 * Wenn true wird die Umgebung neu angelegt 
	 */
	private boolean newEnv = false; 
	
	/**
	 * Konstruktor
	 * @param gui	GUI dieses EnvironmentControllers
	 * @param proj	Projekt, desen Umgebung verwaltet wird
	 */
	public EnvironmentController(EnvironmentControllerGUI gui, Project proj){
		this.gui = gui;
		this.currentProject = proj;
		currentProject.ec = this;
		// SVG und Umgebungsdatei Laden, wenn im Projekt definiert		
		if(currentProject.getSvgPath()!=null){
			loadSvgFile(new File(currentProject.getSvgPath()));			
		}else{
			System.out.println(Language.translate("Keine SVG-Datei zugewiesen"));
		}
		if(currentProject.getEnvPath() != null){
			loadEnvironment(new File(currentProject.getEnvPath()));			
		}else{
			System.out.println(Language.translate("Keine Umgebungsdatei zugewiesen"));
		}
//		System.out.println(System.getProperty("user.dir"));
//		System.out.println(Application.RunInfo.PathBaseDir());
//		System.out.println(Application.RunInfo.PathProjects(false, true));
		
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
	}
	
	public void loadSvgFile(File svgFile){
		if(svgFile.exists()){
			try {
				System.out.println(Language.translate("Lade SVG-Datei")+" "+svgFile.getName());
				// Weise das geladene SVG-Dokument GUI und Umgebung zu
				this.gui.getCanvas().setURI(svgFile.toURI().toURL().toString());
				this.svgDoc = this.gui.getCanvas().getSVGDocument();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.err.println(Language.translate("SVG-Datei")+" "+svgFile.getName()+" "+Language.translate("nicht gefunden"));
		}
	}
	
	/**
	 * Speichert eine Beschreibung der Umgebung als XML Datei  
	 */
	public void saveEnvironment(File envFile){
		// Hole Dateiname aus dem Projekt
//		File envFile = new File(currentProject.getProjectFolderFullPath()+currentProject.getEnvFileName());
		// Wenn noch nicht vorhanden, lege die Datei an
		if(!envFile.exists()){
			try {
				envFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(Language.translate("Speichere Umgebung nach")+" "+envFile.getName());
		System.out.println(mainPlayground.getAgents().size()+" "+Language.translate("Agenten"));
		System.out.println(mainPlayground.getObstacles().size()+" "+Language.translate("Hindernisse"));
		System.out.println(mainPlayground.getPlaygrounds().size()+" "+Language.translate(" Kind-Umgebungen"));
		
		// Erzeuge ein XML-Dokument und Speichere die Wurzel-Umgebung (inkl. Kind-Objekte)
		Document envDoc = SVGDOMImplementation.getDOMImplementation().createDocument(null, "environment", null);
		Element envRoot = envDoc.getDocumentElement();
		envRoot.setAttribute("project", currentProject.getProjectName());
		envRoot.appendChild(mainPlayground.saveAsXML(envDoc));
		
		// Schreibe die Datei
		try {
			FileWriter fw = new FileWriter(envFile);
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			DOMUtilities.writeDocument(envDoc, fw);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentProject.setEnvPath(envFile.getPath());
	}
	
	/**
	 * Läd eine gespeicherte Umgebung
	 */
	public void loadEnvironment(File envFile){
		
		if(envFile.exists()){
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				svgDoc = builder.parse(envFile);
				
				Element mainPg = (Element) svgDoc.getDocumentElement().getFirstChild();
				if(mainPg.getTagName().equals("playground")){
					System.out.println(Language.translate("Lade Umgebung aus")+" "+envFile.getName()+"...");
					this.mainPlayground = new Playground();
					mainPlayground.loadFromXML(mainPg);
					if(mainPg.hasChildNodes()){
						NodeList children = mainPg.getChildNodes();
						for(int i=0; i<children.getLength(); i++){
							loadObject((Element) children.item(i), this.mainPlayground);
						}
					}
					System.out.println(mainPlayground.getAgents().size()+" "+Language.translate("Agenten"));
					System.out.println(mainPlayground.getObstacles().size()+" "+Language.translate("Hindernisse"));
					System.out.println(mainPlayground.getPlaygrounds().size()+" "+Language.translate(" Kind-Umgebungen"));					
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
			System.err.println(Language.translate("Umgebungsdatei")+" "+envFile.getName()+" "+Language.translate("nicht gefunden."));
			this.newEnv=true;
		}
	}
	
	/**
	 * 
	 * @param xml		Knoten im XML-Dokument, der das Objekt repräsentiert
	 * @param parent	Playground, in den das Objekt eingefügt wird
	 */
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
	
	/**
	 * Startet die in der Umgebung definierten Agenten
	 */
	public void startSimulation(){
			// Agent Objekte aus der Umgebung
			Iterator<AgentObject> agents = mainPlayground.getAgents().iterator();
			while(agents.hasNext()){
				startAgent(agents.next());
			}
	}
	
	public void startAgent(AgentObject agent){
		String agentName = agent.getId();
		String agentClass = agent.getAgentClass();
		AgentObject[] arg = new AgentObject[1];
		arg[0]=agent;
		Application.JadePlatform.jadeAgentStart(agentName, agentClass, arg, currentProject.getProjectFolder() );
	}	
}
