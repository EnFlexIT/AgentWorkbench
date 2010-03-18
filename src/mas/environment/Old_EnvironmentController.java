package mas.environment;

import java.io.File;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.apache.batik.bridge.UpdateManager;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
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
public class Old_EnvironmentController {
	
	/**
	 * GUI des EnvironmentControllers 
	 */
	private Old_EnvironmentControllerGUI gui = null;
	/**
	 * Projekt
	 */
	private Project currentProject = null;
	/**
	 * (Wurzel-)Umgebung des Projektes
	 */
	private Old_Playground mainPlayground = null;
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
	public Old_EnvironmentController(Old_EnvironmentControllerGUI gui, Project proj){
		this.gui = gui;
		this.currentProject = proj;
		currentProject.setEnvironmentController(this);
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
	}
	
	public Old_Playground getMainPlayground(){
		return this.mainPlayground;
	}
	
	public void setMainPlayground(Old_Playground pg){
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
		this.mainPlayground = new Old_Playground(root);
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
		
		try {
			if(!(envFile.exists())){
				envFile.createNewFile();
			}
			System.out.println(Language.translate("Speichere Umgebung nach")+" "+envFile.getName());
			System.out.println(mainPlayground.getAgents().size()+" "+Language.translate("Agenten"));
			System.out.println(mainPlayground.getObstacles().size()+" "+Language.translate("Hindernisse"));
			System.out.println(mainPlayground.getPlaygrounds().size()+" "+Language.translate(" Kind-Umgebungen"));
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			Document doc = impl.createDocument(null, "environment", null);
			Element envRoot = doc.getDocumentElement();
			envRoot.setAttribute("project", currentProject.getProjectName());
			envRoot.setAttribute("Date", new java.util.Date().toString());
			envRoot.appendChild(mainPlayground.saveAsXML(doc));
			
			
			
			OutputFormat format = new OutputFormat(doc);
			FileOutputStream fos = new FileOutputStream(envFile);
			format.setIndenting(true);
			format.setIndent(2);
			XMLSerializer serializer = new XMLSerializer(fos, format);
//			XMLSerializer serializer = new XMLSerializer(fos, null);
		    serializer.serialize(doc);
		    fos.close();

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	/**
	 * Läd eine gespeicherte Umgebung
	 */
	public void loadEnvironment(File envFile){
		
		if(envFile.exists()){
			try {
				
				DOMParser parser = new DOMParser();
				
				FileReader reader = new FileReader(envFile);
				InputSource source = new InputSource(reader);
				parser.parse(source);
				Document doc = parser.getDocument();				
				
				Element mainPg = (Element) doc.getElementsByTagName("playground").item(0);
					System.out.println(Language.translate("Lade Umgebung aus")+" "+envFile.getName()+"...");
					this.mainPlayground = new Old_Playground();
					mainPlayground.loadFromXML(mainPg);
					if(mainPg.hasChildNodes()){
						NodeList children = mainPg.getChildNodes();
						for(int i=0; i<children.getLength(); i++){
							if(children.item(i) instanceof Element)
							loadObject((Element) children.item(i), this.mainPlayground);
						}
					}
					System.out.println(mainPlayground.getAgents().size()+" "+Language.translate("Agenten"));
					System.out.println(mainPlayground.getObstacles().size()+" "+Language.translate("Hindernisse"));
					System.out.println(mainPlayground.getPlaygrounds().size()+" "+Language.translate(" Kind-Umgebungen"));
					
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
	public void loadObject(Element xml, Old_Playground parent){
		if(xml.getTagName().equals("obstacle")){
			Old_ObstacleObject object = new Old_ObstacleObject();
			object.loadFromXML(xml);
			
			parent.addObstacle(object);
		}else if(xml.getTagName().equals("agent")){
			Old_AgentObject agent = new Old_AgentObject();
			agent.loadFromXML(xml);
			parent.addAgent(agent);
		}else if(xml.getTagName().equals("playground")){
			Old_Playground pg = new Old_Playground();
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
		
			String ecAgentName="eca";
			Object[] args = new Object[1];
			args[0] = this;
			
			Application.JadePlatform.jadeAgentStart(ecAgentName, "mas.environment.EnvironmentControllerAgent", args, currentProject.getProjectFolder() );
		
			// Agent Objekte aus der Umgebung
			Iterator<Old_AgentObject> agents = mainPlayground.getAgents().values().iterator();
			while(agents.hasNext()){
				startAgent(agents.next());
			}
	}
	
	public void startAgent(Old_AgentObject agent){
		String agentName = agent.getId();
		String agentClass = agent.getAgentClass();
		Old_AgentObject[] arg = new Old_AgentObject[1];
		arg[0]=agent;
		Application.JadePlatform.jadeAgentStart(agentName, agentClass, arg, currentProject.getProjectFolder() );
	}
	
	/**
	 * Macht die Funktion des ElementSelectors von Außen verfügbar 
	 * @param element
	 */
	public void setSelectedElement(Element element){
		UpdateManager um = gui.getCanvas().getUpdateManager();
		if(um != null){
			um.getUpdateRunnableQueue().invokeLater(gui.new ElementSelector(element));
		}
	}
}
