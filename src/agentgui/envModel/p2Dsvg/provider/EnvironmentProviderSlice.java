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

import jade.core.AID;
import jade.core.IMTPException;
import jade.core.Service.Slice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;

import agentgui.envModel.p2Dsvg.ontology.Movement;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PositionUpdate;

/**
 *  @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *  @author Tim Lewen  - DAWIS - ICB - University of Duisburg - Esse
 * The Interface EnvironmentProviderSlice.
 */
public interface EnvironmentProviderSlice extends Slice {
	
	/** The Constant H_GET_ENVIRONMENT. */
	public static final String H_GET_ENVIRONMENT = "getEnvironment";
	
	/** The Constant H_GET_OBJECT. */
	public static final String H_GET_OBJECT = "getObject";
	
	/** The Constant H_GET_CURRENTLY_MOVING_OBJECTS. */
	public static final String H_GET_CURRENTLY_MOVING_OBJECTS = "getCurrentlyMovingObjects";
	
	/** The Constant H_SET_MOVEMENT. */
	public static final String H_SET_MOVEMENT = "setMovement";
	
	/** The Constant H_GET_SVG_DOC. */
	public static final String H_GET_SVG_DOC = "getSVGDoc";
	
	/** The Constant H_GET_PLAYGROUND_OBJECTS. */
	public static final String H_GET_PLAYGROUND_OBJECTS = "getPlaygroundObjects";
	
	/** The Constant H_ASIGN_OBJECT. */
	public static final String H_ASIGN_OBJECT = "assignObject";
	
	/** The Constant H_RELEASE_OBJECT. */
	public static final String H_RELEASE_OBJECT = "releaseObject";
	
	/** The Constant H_IS_MASTER. */
	public static final String H_IS_MASTER = "isMaster";
	
	/** The Constant H_GET_PROJECT_NAME. */
	public static final String H_GET_PROJECT_NAME = "getProjectName";
	
	/** The Constant H_STEP. */
	public static final String H_STEP="stepSimulation";
	
	/** The Constant H_GET_MODEL. */
	public static final String H_GET_MODEL="getModel";
	
	/** The Constant H_TRANSACTION_SIZE. */
	public static final String H_TRANSACTION_SIZE="transaction";
	
	
	/**
	 * Gets the environment.
	 *
	 * @return the environment
	 * @throws IMTPException the iMTP exception
	 */
	public Physical2DEnvironment getEnvironment() throws IMTPException;
	
	/**
	 * Gets the object.
	 *
	 * @param id the id
	 * @return the object
	 * @throws IMTPException the iMTP exception
	 */
	public Physical2DObject getObject(String id) throws IMTPException;
	
	/**
	 * Gets the currently moving objects.
	 *
	 * @return the currently moving objects
	 * @throws IMTPException the iMTP exception
	 */
	public HashSet<Physical2DObject> getCurrentlyMovingObjects() throws IMTPException;
	
	/**
	 * Sets the movement.
	 *
	 * @param agentID the agent id
	 * @param movement the movement
	 * @return true, if successful
	 * @throws IMTPException the iMTP exception
	 */
	public boolean setMovement(String agentID, Movement movement) throws IMTPException;
	
	/**
	 * Gets the sVG doc.
	 *
	 * @return the sVG doc
	 * @throws IMTPException the iMTP exception
	 */
	public Document getSVGDoc() throws IMTPException;
	
	/**
	 * Gets the playground objects.
	 *
	 * @param playgroundID the playground id
	 * @return the playground objects
	 * @throws IMTPException the iMTP exception
	 */
	public List<Physical2DObject> getPlaygroundObjects(String playgroundID) throws IMTPException;
	
	/**
	 * Assign passive object.
	 *
	 * @param objectID the object id
	 * @param agentID the agent id
	 * @return true, if successful
	 * @throws IMTPException the iMTP exception
	 */
	public boolean assignPassiveObject(String objectID, String agentID) throws IMTPException;
	
	/**
	 * Release passive object.
	 *
	 * @param objectID the object id
	 * @throws IMTPException the iMTP exception
	 */
	public void releasePassiveObject(String objectID) throws IMTPException;
	
	/**
	 * Gets the project name.
	 *
	 * @return the project name
	 * @throws IMTPException the iMTP exception
	 */
	public String getProjectName() throws IMTPException;
	
	/**
	 * Checks if is master.
	 *
	 * @return true, if is master
	 * @throws IMTPException the iMTP exception
	 */
	public boolean isMaster() throws IMTPException;
	
	/**
	 * Step model.
	 *
	 * @param key the key
	 * @param updatedPosition the updated position
	 * @throws IMTPException the iMTP exception
	 */
	public void stepModel(AID key, PositionUpdate updatedPosition) throws IMTPException;
	
	/**
	 * Gets the model.
	 *
	 * @param pos the pos
	 * @return the model
	 * @throws IMTPException the iMTP exception
	 */
	public HashMap<AID, PositionUpdate> getModel(int pos)  throws IMTPException;
	
	/**
	 * Gets the transaction size.
	 *
	 * @return the transaction size
	 * @throws IMTPException the iMTP exception
	 */
	public int getTransactionSize() throws IMTPException;

}
