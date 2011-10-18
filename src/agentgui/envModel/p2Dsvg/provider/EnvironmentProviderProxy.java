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

import agentgui.envModel.p2Dsvg.display.SVGUtils;
import agentgui.envModel.p2Dsvg.ontology.Movement;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PositionUpdate;

import jade.core.AID;
import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.ServiceException;
import jade.core.SliceProxy;

/**
 * The Class EnvironmentProviderProxy implements the slice management and creates vertical commands.
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen.
 * @author Tim Lewen  - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentProviderProxy extends SliceProxy implements
		EnvironmentProviderSlice {

	/** serialVersionUID. */
	private static final long serialVersionUID = -4218111250581084215L;

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#getEnvironment()
	 */
	@Override
	public Physical2DEnvironment getEnvironment() throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(H_GET_ENVIRONMENT, EnvironmentProviderService.SERVICE_NAME, null);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)){
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Physical2DEnvironment) result;
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#getObject(java.lang.String)
	 */
	@Override
	public Physical2DObject getObject(String id) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(H_GET_OBJECT, EnvironmentProviderService.SERVICE_NAME, null);
			cmd.addParam(id);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Physical2DObject) result;
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#getCurrentlyMovingObjects()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashSet<Physical2DObject> getCurrentlyMovingObjects() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(H_GET_CURRENTLY_MOVING_OBJECTS, EnvironmentProviderService.SERVICE_NAME, null);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (HashSet<Physical2DObject>) result;
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#setMovement(java.lang.String, agentgui.envModel.p2Dsvg.ontology.Movement)
	 */
	@Override
	public boolean setMovement(String agentID, Movement movement)
			throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(H_SET_MOVEMENT, EnvironmentProviderService.SERVICE_NAME, null);
			cmd.addParam(agentID);
			cmd.addParam(movement);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return ((Boolean)result).booleanValue();
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#getSVGDoc()
	 */
	@Override
	public Document getSVGDoc() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(H_GET_SVG_DOC, EnvironmentProviderService.SERVICE_NAME, null);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)){
				if(result instanceof IMTPException){
					throw (IMTPException)result;
				}else{
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return SVGUtils.stringToSVG((String) result);
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#getPlaygroundObjects(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Physical2DObject> getPlaygroundObjects(String playgroundID)
			throws IMTPException {
		GenericCommand cmd = new GenericCommand(H_GET_PLAYGROUND_OBJECTS, EnvironmentProviderService.SERVICE_NAME, null);
		cmd.addParam(playgroundID);
		try {
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)){
				if(result instanceof IMTPException){
					throw (IMTPException)result;
				}else{
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (List<Physical2DObject>) result;
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#releasePassiveObject(java.lang.String)
	 */
	@Override
	public void releasePassiveObject(String objectID) throws IMTPException {
		GenericCommand cmd = new GenericCommand(H_RELEASE_OBJECT, EnvironmentProviderService.SERVICE_NAME, null);
		cmd.addParam(objectID);
		try {
			getNode().accept(cmd);
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#assignPassiveObject(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean assignPassiveObject(String objectID, String agentID) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(H_ASIGN_OBJECT, EnvironmentProviderService.SERVICE_NAME, null);
			cmd.addParam(objectID);
			cmd.addParam(agentID);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)){
				if(result instanceof IMTPException){
					throw (IMTPException)result;
				}else{
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return ((Boolean)result).booleanValue();
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#isMaster()
	 */
	@Override
	public boolean isMaster() throws IMTPException {
		GenericCommand cmd = new GenericCommand(H_IS_MASTER, EnvironmentProviderService.SERVICE_NAME, null);
		try {
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)){
				if(result instanceof IMTPException){
					throw (IMTPException)result;
				}else{
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return ((Boolean)result).booleanValue();
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}


	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#getProjectName()
	 */
	@Override
	public String getProjectName() throws IMTPException {
		GenericCommand cmd = new GenericCommand(H_GET_PROJECT_NAME, EnvironmentProviderService.SERVICE_NAME, null);
		try{
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)){
				if(result instanceof IMTPException){
					throw (IMTPException)result;
				}else{
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (String) result;
		}catch (ServiceException e){
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#stepModel(jade.core.AID, agentgui.envModel.p2Dsvg.ontology.PositionUpdate)
	 */
	@Override
	public void stepModel(AID key, PositionUpdate updatedPosition)
			throws IMTPException {
		GenericCommand cmd = new GenericCommand(H_STEP, EnvironmentProviderService.SERVICE_NAME, null);
		cmd.addParam(key);
		cmd.addParam(updatedPosition);
		try {
			getNode().accept(cmd);
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#getModel(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<AID, PositionUpdate> getModel(int pos) throws IMTPException {

		GenericCommand cmd = new GenericCommand(H_GET_MODEL, EnvironmentProviderService.SERVICE_NAME, null);
		cmd.addParam(pos);
		try{
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)){
				if(result instanceof IMTPException){
					throw (IMTPException)result;
				}else{
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (HashMap<AID, PositionUpdate>) result;
		}catch (ServiceException e){
			throw new IMTPException("Unable to access remote node", e);
		}
		
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderSlice#getTransactionSize()
	 */
	@Override
	public int getTransactionSize() throws IMTPException {
		
		
		GenericCommand cmd = new GenericCommand(H_TRANSACTION_SIZE, EnvironmentProviderService.SERVICE_NAME, null);
		try{
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)){
				if(result instanceof IMTPException){
					throw (IMTPException)result;
				}else{
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			
			return ((Integer) result).intValue();
		}catch (ServiceException e){
			throw new IMTPException("Unable to access remote node", e);
		}
	
	}	
		
}		
	



