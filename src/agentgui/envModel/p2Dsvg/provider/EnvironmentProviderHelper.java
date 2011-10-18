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
 package agentgui.envModel.p2Dsvg.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;

import agentgui.envModel.p2Dsvg.ontology.ActiveObject;
import agentgui.envModel.p2Dsvg.ontology.Movement;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PositionUpdate;

import jade.core.AID;
import jade.core.ServiceHelper;

/**
 * This interfaces provides access to the EnvironmentProviderService for agents.
 *
 *  * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *    @author Tim Lewen  - DAWIS - ICB - University of Duisburg - Essen
 */
public interface EnvironmentProviderHelper extends ServiceHelper {
	
	/**
	 * Gets the whole environment model managed by the EnvironmentProviderService.
	 *
	 * @return The environment model
	 */
	public Physical2DEnvironment getEnvironment();
	
	/**
	 * Gets an environment object's data from the environment model.
	 *
	 * @param id The objects ID
	 * @return The Physical2DObject instance representing the object with the specified IF
	 */
	public Physical2DObject getObject(String id);
	/**
	 * Sets the environment model
	 * This method is intended to be used only by the EnvironmentProviderAgent when initializing the service.
	 * It is always executed at the local node, accessing remote slices is not implemented. 
	 * @param environment The environment model
	 */
	public void setEnvironment(Physical2DEnvironment environment);
	
	/**
	 * Gets a list of all currently moving agents.
	 * This method is intended to be used only by the EnvironmentProviderAgent for updating the positions
	 * of the agents and the objects controlled by them in the environment model.
	 * It is always executed at the local node, accessing remote slices is not implemented.
	 *
	 * @return the currently moving agents
	 */
	public HashSet<ActiveObject> getCurrentlyMovingAgents();
	/**
	 * Gets a list of all objects currently moving in the environment model.
	 * @return The list
	 */
	public HashSet<Physical2DObject> getCurrentlyMovingObjects();
	
	/**
	 * Sets an agent's current movement.
	 *
	 * @param agentID The agent's ID
	 * @param movement The agent's movement
	 * @return True if the movement could be set, false if not (maximum speed exceeded?)
	 */
	public boolean setMovement(String agentID, Movement movement);
	
	/**
	 * Gets the SVG document visualizing the environment model.
	 *
	 * @return The SVG document
	 */
	public Document getSVGDoc();
	/**
	 * Sets the SVG document visualizing the environment model.
	 * This method is intended to be used only by the EnvironmentProviderAgent when initializing the service.
	 * It is always executed at the local node, accessing remote slices is not implemented. 
	 * @param svgDoc The SVG document
	 */
	public void setSVGDoc(Document svgDoc);
	
	/**
	 * Gets a list of all environment objects existing inside of a playground.
	 *
	 * @param playgroundID The playground's ID
	 * @return The list
	 */
	public List<Physical2DObject> getPlaygroundObjects(String playgroundID);
	
	/**
	 * Assigns a PassiveObject to an ActiveObject.
	 *
	 * @param passiveObjectID The object's ID
	 * @param activeObjectID The agent's ID
	 * @return Successful?
	 */
	public boolean assignPassiveObject(String passiveObjectID, String activeObjectID);
	
	/**
	 * Releases a PassiveObject from an ActiveObject controlling it.
	 *
	 * @param passiveObjectID The PassiveObject's ID
	 */
	public void releasePassiveObject(String passiveObjectID);
	
	/**
	 * Gets the project name.
	 *
	 * @return The name of the project the Physical2DEnvironment belongs to.
	 */
	public String getProjectName();
	
	/**
	 * Step model.
	 *
	 * @param key the key
	 * @param updatedPosition the updated position
	 */
	public void stepModel(AID key, PositionUpdate updatedPosition);
	
	/**
	 * Gets the model.
	 *
	 * @param pos the pos
	 * @return the model
	 */
	public HashMap<AID,PositionUpdate> getModel(int pos);
	
	/**
	 * Gets the transaction size.
	 *
	 * @return The number of Transactions
	 */
	public int getTransactionSize();
	
	/**
	 * Set the current position of the simulation.
	 *
	 * @param pos the new current pos
	 */
	public void setCurrentPos(int pos);
	
	/**
	 * Returns the current position of the simulation.
	 *
	 * @return The current Position of the simulation
	 */
	public int getCurrentPos();
	
	/**
	 * Checks if is _running.
	 *
	 * @return If the simulation pauses or is running
	 */
	public boolean is_running();
	
	/**
	 * Sets the running.
	 *
	 * @param running the new running
	 */
	public void setRunning(boolean running);
	
	
}
