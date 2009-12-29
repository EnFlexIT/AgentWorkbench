package mas.environment;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Element;



/**
 * Represents an area containing agents
 * @author Nils
 *
 */
public class Playground extends BasicObject{
	/**
	 * List of all objects in this playground 
	 */
	private HashMap<String, BasicObject> objects = null;
	/**
	 * List of all agents in this playground
	 */
	private HashMap<String, AgentObject> agents = null;
	/**
	 * List of subplaygrounds
	 */
	private HashMap<String, Playground> playgrounds = null;
		
	public Playground(){		
		this.objects = new HashMap<String, BasicObject>();
		this.agents = new HashMap<String, AgentObject>();		
	}
	
	public Playground(Element svg){
		super(svg);
		this.objects = new HashMap<String, BasicObject>();
		this.agents = new HashMap<String, AgentObject>();
	}	
	
	public void addObject(BasicObject object){
		if(object != null){
			object.setPlayground(this);
			this.objects.put(object.getId(), object);
		}
	}
	
	public void addAgent(AgentObject agent){
		if(agent != null){
			this.agents.put(agent.getId(), agent);
			this.addObject(agent);
		}
	}	
	
	public void removeElement(String id){
		objects.remove(id);
		agents.remove(id);
//		playgrounds.remove(id);
	}
	
	public HashMap<String, BasicObject> getObjects(){
		return this.objects;
	}
	
	public Collection<AgentObject> getAgents(){
		return this.agents.values();
	}
	
	public Collection<Playground> getPlaygrounds(){
		return this.playgrounds.values();
	}
}
