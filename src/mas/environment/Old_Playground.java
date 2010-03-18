package mas.environment;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import application.Language;



/**
 * Eine (Teil-)Umgebung, die Umgebungsobjekte enthält
 * @author Nils
 *
 */
public class Old_Playground extends Old_BasicObject{
	/**
	 * Alle Umgebungsobjekte in diesem Playground 
	 */
	private HashMap<String, Old_BasicObject> objects = null;
	/**
	 * Alle Agenten in diesem Playground 
	 */
	private HashMap<String, Old_AgentObject> agents = null;
	/**
	 * Alle Kind-Playgrounds in diesem Playground
	 */
	private HashMap<String, Old_Playground> playgrounds = null;
	
	/**
	 * Alle statischen Umgebungsobjekte in diesem Playground
	 */
	private HashMap<String, Old_ObstacleObject> obstacles = null;
	
	/**
	 * Erzeugt ein "leeres" Playground-Objekt
	 */
	public Old_Playground(){		
		this.objects = new HashMap<String, Old_BasicObject>();
		this.agents = new HashMap<String, Old_AgentObject>();
		this.obstacles = new HashMap<String, Old_ObstacleObject>();
		this.playgrounds = new HashMap<String, Old_Playground>();
	}
	
	/**
	 * Erzeugt ein Playground-Objekt, das auf dem übergebenen SVG-Element basiert 
	 * @param svg
	 */
	public Old_Playground(Element svg){
		this("MainPG", svg);		
	}
	
	public Old_Playground(String id, Element svg){
		super(id, svg);
		this.objects = new HashMap<String, Old_BasicObject>();
		this.agents = new HashMap<String, Old_AgentObject>();
		this.obstacles = new HashMap<String, Old_ObstacleObject>();
		this.playgrounds = new HashMap<String, Old_Playground>();
	}
	
	/**
	 * Fügt ein statisches Objekt zum Playground hinzu
	 * @param object
	 */
	public void addObject(Old_BasicObject object){
		if(object != null){
			object.setPlayground(this);
			this.objects.put(object.getId(), object);
		}
	}
	
	/**
	 * Fügt einen Agenten zu diesem Playground hinzu
	 * @param agent
	 */
	public void addAgent(Old_AgentObject agent){
		if(agent != null){
			this.agents.put(agent.getId(), agent);
			this.addObject(agent);
		}
	}	
	
	/**
	 * Fügt ein Hinderniss zu diesem Playground hinzu
	 * @param obstacle
	 */
	public void addObstacle(Old_ObstacleObject obstacle){
		if(obstacle != null){
			this.obstacles.put(obstacle.getId(), obstacle);
			this.addObject(obstacle);
		}		
	}
	
	/**
	 * Fügt einen Kind-Playground zu diesem Playground hinzu
	 * @param playground
	 */
	public void addPlayground(Old_Playground playground){
		if(playground != null){
			this.playgrounds.put(playground.getId(), playground);
			this.addObject(playground);
		}
	}
	
	/**
	 * Entfernt ein beliebiges Umgebungsobjekt aus dem Playground
	 * @param id String ID des zu entfernenden Objektes
	 */
	public void removeElement(String id){
		if(agents.remove(id) != null){
			System.out.println(Language.translate("Entferne Agent")+" "+id);
		}else if(obstacles.remove(id) != null){
			System.out.println(Language.translate("Entferne Hindernis")+" "+id);
		}else if(playgrounds.remove(id) != null){
			System.out.println(Language.translate("Entferne Kind-Playground")+" "+id);
		}
		objects.remove(id);
	}
	
	/**
	 * Liefert eine Liste aller Umgebungsobjekte in diesem Playground
	 * @return HashMap<String, BasicObject>
	 */
	public HashMap<String, Old_BasicObject> getObjects(){
		return this.objects;
	}
	
	/**
	 * Liefert eine Liste aller Agenten in diesem Playground
	 * @return HashMap<String, AgentObject>
	 */
	public HashMap<String, Old_AgentObject> getAgents(){
		return this.agents;
	}
	
	/**
	 * Liefert eine Liste aller Kind-Playgrounds in diesem Playground
	 * @return HashMap<String, Playground>
	 */
	public Collection<Old_Playground> getPlaygrounds(){
		return this.playgrounds.values();
	}
	
	/**
	 * Liefert eine Liste aller statischen Objekte in diesem Playground
	 * @return
	 */
	public Collection<Old_ObstacleObject> getObstacles(){
		return this.obstacles.values();
	}
	
	
	/**
	 * Erzeugt ein XML-Element, das diesen Playground speichert
	 */
	@Override
	public Element saveAsXML(Document doc) {
		Element xml = doc.createElement("playground");
		this.saveBasics(xml);
		// Rekursiver Aufruf für enthaltene Objekte
		Iterator<Old_BasicObject> children = this.getObjects().values().iterator();
		while(children.hasNext()){
			xml.appendChild(children.next().saveAsXML(doc));
		}
		return xml;
	}

	
	@Override
	public void loadFromXML(Element elem) {
		this.loadBasics(elem);
		if(elem.hasChildNodes()){
			NodeList children = elem.getChildNodes();
			for(int i=0; i < children.getLength(); i++){
				
			}
		}		
	}
}
