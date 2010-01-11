package mas.environment;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import org.w3c.dom.Element;



/**
 * Eine (Teil-)Umgebung
 * @author Nils
 *
 */
public class Playground extends BasicObject implements Serializable{
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
		
	public Playground(Element svg){
		super(svg);
		this.objects = new HashMap<String, BasicObject>();
		this.agents = new HashMap<String, AgentObject>();
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
	 * Entfernt ein beliebiges Umgebungsobjekt aus dem Playground
	 * @param id String ID des zu entfernenden Objektes
	 */
	public void removeElement(String id){
		objects.remove(id);
		agents.remove(id);
//		playgrounds.remove(id);
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
}
