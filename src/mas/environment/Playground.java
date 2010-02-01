package mas.environment;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



/**
 * Eine (Teil-)Umgebung
 * @author Nils
 *
 */
public class Playground extends BasicObject{
	/**
	 * Alle Objekte (auch Agenten und Kind-Playgrounds) in diesem Playground 
	 */
	private HashMap<String, BasicObject> objects = null;
	/**
	 * Alle Agenten in diesem Playground 
	 */
	private HashMap<String, AgentObject> agents = null;
	/**
	 * Alle Kind-Playgrounds in diesem Playground
	 */
	private HashMap<String, Playground> playgrounds = null;
	
	/**
	 * Alle statischen Umgebungsobjekte
	 */
	private HashMap<String, ObstacleObject> obstacles = null;
		
	public Playground(){
		// "Leeres" Objekt, Initialisierung über loadFromXML()
		this.objects = new HashMap<String, BasicObject>();
		this.agents = new HashMap<String, AgentObject>();
		this.obstacles = new HashMap<String, ObstacleObject>();
		this.playgrounds = new HashMap<String, Playground>();
	}
	
	public Playground(Element svg){
		this("MainPG", svg);		
	}
	
	public Playground(String id, Element svg){
		super(id, svg);
		this.objects = new HashMap<String, BasicObject>();
		this.agents = new HashMap<String, AgentObject>();
		this.obstacles = new HashMap<String, ObstacleObject>();
		this.playgrounds = new HashMap<String, Playground>();
	}
	
	/**
	 * Fügt ein statisches Objekt zum Playground hinzu
	 * @param object
	 */
	public void addObject(BasicObject object){
		if(object != null){
			object.setPlayground(this);
			this.objects.put(object.getId(), object);
		}
	}
	
	/**
	 * Fügt einen Agenten zu diesem Playground hinzu
	 * @param agent
	 */
	public void addAgent(AgentObject agent){
		if(agent != null){
			this.agents.put(agent.getId(), agent);
			this.addObject(agent);
		}
	}	
	
	/**
	 * Fügt ein Hinderniss zu diesem Playground hinzu
	 * @param obstacle
	 */
	public void addObstacle(ObstacleObject obstacle){
		if(obstacle != null){
			this.obstacles.put(obstacle.getId(), obstacle);
			this.addObject(obstacle);
		}		
	}
	
	public void addPlayground(Playground playground){
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
			System.out.println("Removing agent "+id);
		}else if(obstacles.remove(id) != null){
			System.out.println("Removing obstacle "+id);
		}else if(playgrounds.remove(id) != null){
			System.out.println("Removing child playground "+id);
		}
		objects.remove(id);
	}
	
	/**
	 * Liefert eine Liste aller Umgebungsobjekte in diesem Playground
	 * @return HashMap<String, BasicObject>
	 */
	public HashMap<String, BasicObject> getObjects(){
		return this.objects;
	}
	
	/**
	 * Liefert eine Liste aller Agenten in diesem Playground
	 * @return HashMap<String, AgentObject>
	 */
	public Collection<AgentObject> getAgents(){
		return this.agents.values();
	}
	
	/**
	 * Liefert eine Liste aller Kind-Playgrounds in diesem Playground
	 * @return HashMap<String, Playground>
	 */
	public Collection<Playground> getPlaygrounds(){
		return this.playgrounds.values();
	}
	
	public Collection<ObstacleObject> getObstacles(){
		return this.obstacles.values();
	}

	@Override
	public Element saveAsXML(Document doc) {
		Element xml = doc.createElement("playground");
		this.saveBasics(xml);
		Iterator<BasicObject> children = this.getObjects().values().iterator();
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
