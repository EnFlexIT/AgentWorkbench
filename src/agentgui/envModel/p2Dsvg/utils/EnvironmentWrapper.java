/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.p2Dsvg.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import agentgui.envModel.p2Dsvg.ontology.ActiveObject;
import agentgui.envModel.p2Dsvg.ontology.PassiveObject;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PlaygroundObject;
import agentgui.envModel.p2Dsvg.ontology.StaticObject;


/**
 * Wrapper class for easier handling of Physical2DEnvironments
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class EnvironmentWrapper {
	/**
	 * The wrapped environment
	 */
	private Physical2DEnvironment environment = null;
	/**
	 * HashMap enabling id based object lookup
	 */
	private HashMap<String, Physical2DObject> objectysById = null;
	/**
	 * List of all AgentObjects in this environment
	 */
	private Vector<ActiveObject> agents = null;
	/**
	 * List of all ObstacleObjects in this environment
	 */
	private Vector<StaticObject> obstacles = null;
	/**
	 * List of all PayloadObjects in this environment
	 */
	private Vector<PassiveObject> payloads = null;
	/**
	 * List of all PlaygroundObjects in this environment
	 */
	private Vector<PlaygroundObject> playgrounds = null;
	
	/**
	 * Constructor, creating an EnvironmentWrapper for a given environment
	 * @param environment The environment to wrap
	 */
	public EnvironmentWrapper(Physical2DEnvironment environment){
		this.environment = environment;
		rebuildLists();
	}
	/**
	 * Rebuilding HashMap and Vectors
	 */
	public void rebuildLists(){
		
		objectysById = new HashMap<String, Physical2DObject>();
		agents = new Vector<ActiveObject>();
		obstacles = new Vector<StaticObject>();
		payloads = new Vector<PassiveObject>();
		playgrounds = new Vector<PlaygroundObject>();
		this.objectysById.put(this.environment.getRootPlayground().getId(), this.environment.getRootPlayground());
		if(this.environment != null){
			scanObjectsByPlayground(this.environment.getRootPlayground());
		}
	}
	
	/**
	 * Adding objects of the specified PlaygroundObject to the lists
	 * @param pg The PlaygroundObject to scan
	 */
	@SuppressWarnings("unchecked")
	private void scanObjectsByPlayground(PlaygroundObject pg){
		Iterator<Physical2DObject> objects = pg.getAllChildObjects();
		while(objects.hasNext()){
			Physical2DObject object = objects.next();
			objectysById.put(object.getId(), object);
			if(object instanceof ActiveObject){
				agents.add((ActiveObject) object);
			}else if(object instanceof StaticObject){
				obstacles.add((StaticObject) object);
			}else if(object instanceof PassiveObject){
				payloads.add((PassiveObject) object);
			}else if(object instanceof PlaygroundObject){
				playgrounds.add((PlaygroundObject) object);
				scanObjectsByPlayground((PlaygroundObject) object);
			}
		}
	}
	
	
	
	
	/**
	 * Getter method for objectsById
	 * @return the objectysById
	 */
	public HashMap<String, Physical2DObject> getObjectysById() {
		return objectysById;
	}



	/**
	 * Getter method for agents
	 * @return the agents
	 */
	public Vector<ActiveObject> getAgents() {
		return agents;
	}



	/**
	 * Getter method for obstacles
	 * @return the obstacles
	 */
	public Vector<StaticObject> getObstacles() {
		return obstacles;
	}



	/**
	 * Getter method for payloads
	 * @return the payloads
	 */
	public Vector<PassiveObject> getPayloads() {
		return payloads;
	}



	/**
	 * Getter method for playgrounds
	 * @return the playgrounds
	 */
	public Vector<PlaygroundObject> getPlaygrounds() {
		return playgrounds;
	}
	
	/**
	 * This method gets a Physical2DObject from the wrapped Physical2DEnvironment by it's ID
	 * @param id The object id to look for
	 * @return The object with the specified ID
	 */
	public Physical2DObject getObjectById(String id){
		return objectysById.get(id);
	}

	/**
	 * Adding a Physical2DObject to the wrapped environment 
	 * @param object The object to add
	 */
	public void addObject(Physical2DObject object){
		environment.getRootPlayground().addChildObjects(object);
		objectysById.put(object.getId(), object);
		if(object instanceof ActiveObject){
			agents.add((ActiveObject) object);
		}else if(object instanceof StaticObject){
			obstacles.add((StaticObject) object);
		}else if(object instanceof PassiveObject){
			payloads.add((PassiveObject) object);
		}else if(object instanceof PlaygroundObject){
			playgrounds.add((PlaygroundObject) object);
		}
	}
	/**
	 * Removing an object from the wrapped environment 
	 * @param Object
	 */
	public void removeObject(Physical2DObject Object){
		PlaygroundObject parent = (PlaygroundObject) getObjectById(Object.getParentPlaygroundID());
		parent.removeChildObjects(Object);
		rebuildLists();
	}
}
