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
import jade.core.Agent;
import jade.core.BaseService;
import jade.core.HorizontalCommand;
import jade.core.IMTPException;
import jade.core.Node;
import jade.core.Profile;
import jade.core.Service;
import jade.core.ServiceException;
import jade.core.ServiceHelper;
import jade.core.VerticalCommand;
import jade.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;

import agentgui.envModel.p2Dsvg.display.SVGUtils;
import agentgui.envModel.p2Dsvg.ontology.ActiveObject;
import agentgui.envModel.p2Dsvg.ontology.Movement;
import agentgui.envModel.p2Dsvg.ontology.PassiveObject;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PlaygroundObject;
import agentgui.envModel.p2Dsvg.ontology.Position;
import agentgui.envModel.p2Dsvg.ontology.PositionUpdate;
import agentgui.envModel.p2Dsvg.utils.EnvironmentWrapper;

/**
 * This service provides and manages a Physical2DEnvironment for a distributed simulation.
 *
 *  @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *  @author Tim Lewen  - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentProviderService extends BaseService {
	
	/** The service name. */
	public static String SERVICE_NAME = "agentgui.envModel.p2Dsvg.provider.EnvironmentProvider";
	
	/** The Physical2DEnvironment handled by this service. */
	private Physical2DEnvironment environment = null;
	
	/** Wrapper object for easier handling of the Physical2DEnvironment object. */
	private EnvironmentWrapper envWrap = null;
	
	/** The SVG document visualizing the Physical2DEnvironment. */
	private Document svgDoc = null;
	
	/** The EnvironmentProviderHelper instance. */
	private ServiceHelper helper = new EnvironmentProviderHelperImpl();
	
	/** The local EnvironmentProviderSlice instance. */
	private Service.Slice localSlice = new EnvironmentProviderSliceImpl();
	
	/** Is the local node hosting the Physical2DEnvironment. */
	private boolean masterNode = false;
	
	/** The name of the node hosting the Physical2DEnvironment. */
	private String masterSlice = null;
	
	/** Set of currently moving ActiveObjects. */
	private HashSet<ActiveObject> currentlyMovingAgents;
	
	/** Set of currently moving Physical2DObjects. */
	private HashSet<Physical2DObject> currentlyMovingObjects;
	
	/** The step. */
	private int step;
	
	/** The transaction size. */
	private int transactionSize=-1;
	
	/** The is_running. */
	private boolean is_running=true;
	
	/** The transaction. */
	private HashMap <AID,ArrayList<PositionUpdate>> transaction=new HashMap<AID,ArrayList<PositionUpdate>>();
	
	/* (non-Javadoc)
	 * @see jade.core.Service#getName()
	 */
	@Override
	public String getName() {
		 return SERVICE_NAME;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.BaseService#boot(jade.core.Profile)
	 */
	public void boot(Profile p){
		try {
			super.boot(p);
			getLocalNode().exportSlice(SERVICE_NAME, getLocalSlize());
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (IMTPException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Initializes the Physical2DEnvironment.
	 *
	 * @param environment The environment
	 */
	private void setEnvironment(Physical2DEnvironment environment){
		if(environment != null){
			this.environment = environment;
			this.envWrap = new EnvironmentWrapper(environment);
			this.currentlyMovingAgents = new HashSet<ActiveObject>();
			this.currentlyMovingObjects = new HashSet<Physical2DObject>();
			this.masterNode = true;
		}else{
			this.environment = null;
			this.envWrap = null;
			this.currentlyMovingAgents = null;
			this.currentlyMovingObjects = null;
			this.masterNode = false;
		}
	}
	
	/**
	 * Finds the master node, i.e. the EnvironmentProviderService instance hosting the Physical2DEnvironment
	 * @return The container name
	 * @throws ServiceException Error accessing the EnvironmentProviderService
	 */
	private String getMasterSlice() throws ServiceException{
		if(masterSlice == null){
			try {
				Slice[] epsSlices = getAllSlices();
				for(int i=0; i<epsSlices.length && masterSlice == null; i++){
					EnvironmentProviderSlice slice = (EnvironmentProviderSlice) epsSlices[i];
					if(slice.isMaster()){
						masterSlice = slice.getNode().getName();
					}
				}
				
			} catch (ServiceException e) {
				throw new ServiceException("Error retrieving EnvironmentProviderService slices", e);
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		}
		return masterSlice;
	}
	
	/**
	 * Checks if is master.
	 *
	 * @return Result Is the local node the master node?
	 */
	private boolean isMaster(){
		return masterNode;
	}
	
	/**
	 * Gets the environment.
	 *
	 * @return The Physical2DEnvironment object
	 */
	private Physical2DEnvironment getEnvironment(){
		Physical2DEnvironment env = null;
		if(masterNode){
			env = environment;
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				env = mainSlice.getEnvironment();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		}
		
		return env;
	}
	
	/**
	 * Gets the project name.
	 *
	 * @return The name of the project the Physical2DEnvironment belongs to
	 */
	private String getProjectName(){
		if(masterNode){
			return environment.getProjectName();
		}else{
			String projectName = "";
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				projectName = mainSlice.getProjectName();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
			return projectName;
		}
	}
	
	/**
	 * Gets the Physical2DObject with the specified ID from the Physical2DEnvironment.
	 *
	 * @param id The Physical2DObject's ID
	 * @return The Physical2DObject
	 */
	private Physical2DObject getObject(String id){
		Physical2DObject object = null;
		
		if(masterNode){
			object = envWrap.getObjectById(id);
		}else{
		
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				object = mainSlice.getObject(id);
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		}
		return object;
	}
	
	/**
	 * Gets the currently moving agents.
	 *
	 * @return List of all currently moving ActiveObjects within the Physical2DEnvironment
	 */
	private HashSet<ActiveObject> getCurrentlyMovingAgents(){
		return this.currentlyMovingAgents;
	}
	
	/**
	 * Gets the currently moving objects.
	 *
	 * @return List of all currently moving Physical2DObjects within the environment
	 */
	private HashSet<Physical2DObject> getCurrentlyMovingObjects(){
		HashSet<Physical2DObject> objects = null;
		
		if(masterNode){
			objects = this.currentlyMovingObjects;
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				objects = mainSlice.getCurrentlyMovingObjects();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
			
		}
		
		return objects;
	}
	
	/**
	 * Setting an ActiveObjects movement.
	 *
	 * @param objectID The ActiveObject's ID
	 * @param movement The Movement instance
	 * @return Successful?
	 */
	@SuppressWarnings("unchecked")
	private boolean setMovement(String objectID, Movement movement){
		boolean result = false;
		

		if(masterNode){
			Physical2DObject object = getObject(objectID);
			if(object != null && object instanceof ActiveObject){
				
				ActiveObject agent = (ActiveObject) object;
		
					
		
				//				if(movement.getSpeed() <= maxSpeed+0.0005){		// Small tolerance required for inaccuracy in speed calculation  
					
					agent.setMovement(movement);
					
//				}
				if(agent.getMovement().getSpeed() > 0.0){
					
					this.currentlyMovingAgents.add(agent);
					this.currentlyMovingObjects.add(agent);
					Iterator<PassiveObject> controlledObjects = agent.getAllPayload();
					while(controlledObjects.hasNext()){
						this.currentlyMovingObjects.add(controlledObjects.next());
					}
				}else{
					this.currentlyMovingAgents.remove(agent);
					this.currentlyMovingObjects.remove(agent);
					Iterator<PassiveObject> controlledObjects = agent.getAllPayload();
					while(controlledObjects.hasNext()){
						this.currentlyMovingObjects.remove(controlledObjects.next());
					}
				}
				result = true;
			}
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				result = mainSlice.setMovement(objectID, movement);
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Gets the sVG doc.
	 *
	 * @return The SVG document visualizing the Physical2DEnvironment
	 */
	private Document getSVGDoc(){
		Document doc = null;
		if(masterNode){
			doc = this.svgDoc;
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				doc = mainSlice.getSVGDoc();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		}
		if(doc != null){
			return (Document) doc.cloneNode(true);
		}else{
			return null;
		}
	}
	
	/**
	 * Gets all Physical2DObjects within a specified PlaygroundObject.
	 *
	 * @param playgroundID The PlaygroundObject's ID
	 * @return List of Physical2DObjects
	 */
	@SuppressWarnings("unchecked")
	private List<Physical2DObject> getPlaygroundObjects(String playgroundID){
		List<Physical2DObject> objects = null;
		if(masterNode){
			PlaygroundObject pg = (PlaygroundObject) envWrap.getObjectById(playgroundID);
			objects = (List<Physical2DObject>) pg.getChildObjects();
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				objects = mainSlice.getPlaygroundObjects(playgroundID);
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		}
		return objects;
	}
	
	/**
	 * Assigns a PassiveObject to an ActiveObject.
	 *
	 * @param passiveObjectID The PassiveObject's ID
	 * @param activeObjectID The ActiveObject's ID
	 * @return Successful?
	 */
	private boolean assignPassiveObject(String passiveObjectID, String activeObjectID){
		boolean success = false;
		if(masterNode){
			// Get the ActiveObject
			Physical2DObject agentObject = envWrap.getObjectById(activeObjectID);
			if(agentObject != null && agentObject instanceof ActiveObject){
				ActiveObject agent = (ActiveObject) agentObject;
				// Get the PassiveObject
				Physical2DObject payloadObject = envWrap.getObjectById(passiveObjectID);
				// Found and correct class?
				if(payloadObject != null && payloadObject instanceof PassiveObject){
					PassiveObject payload = (PassiveObject) payloadObject;
					// Assignment only possible if the PassiveObject is not assigned to another Active Object
					if(payload.getControllingObjectID() == null){
						payload.setControllingObjectID(agent.getId());
						agent.addPayload(payload);
						success = true;
					}
				}
			}
			
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				success = mainSlice.assignPassiveObject(passiveObjectID, activeObjectID);
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		}
		return success;
	}
	
	/**
	 * Releases a PassiveObject from an ActiveObject controlling it.
	 *
	 * @param passiveObjectID The PassiveObject's ID
	 */
	private void releasePassiveObject(String passiveObjectID){
		if(masterNode){
			// Get the PassiveObject
			Physical2DObject payloadObject =envWrap.getObjectById(passiveObjectID);
			// If found and correct class, release it
			if(payloadObject != null && payloadObject instanceof PassiveObject){
				PassiveObject object = (PassiveObject) payloadObject;
				ActiveObject agent = (ActiveObject) envWrap.getObjectById(object.getControllingObjectID());
				agent.removePayload(object);
				object.setControllingObjectID(null);
			}
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				mainSlice.releasePassiveObject(passiveObjectID);
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets the SVG document.
	 *
	 * @param svgDoc the SVG document
	 */
	private void setSVGDoc(Document svgDoc){
		this.svgDoc = svgDoc;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getHelper(jade.core.Agent)
	 */
	public ServiceHelper getHelper(Agent a){
		return helper;
	}
	
	


	/**
	 * Gets the model.
	 *
	 * @param pos the pos
	 * @return the model
	 */
	private HashMap<AID, PositionUpdate> getModel(int pos) {
		if(masterNode)
		{
		HashMap<AID,PositionUpdate> result=new HashMap<AID,PositionUpdate>();
		Set<AID> keys=transaction.keySet();
		for(AID key : keys)
		{
			PositionUpdate update=null;
			int size=transaction.get(key).size();
			if(pos<size)
			{
			 update=transaction.get(key).get(pos);
			}
			else
			{
		
			 update=transaction.get(key).get(size-1);
			}
			
			result.put(key, update);
		}
		
		return result;
		}
		else
		{
			try
			{
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
				return mainSlice.getModel(pos);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		return null;
	}
	

	/**
	 * Step model.
	 *
	 * @param key the key
	 * @param updatedPosition the updated position
	 */
	private void stepModel(AID key, PositionUpdate updatedPosition)
	{
		if(masterNode)
		{
		ArrayList<PositionUpdate> list=transaction.get(key);
		if(list==null)
		{
			
			list=new ArrayList<PositionUpdate>();
			transaction.put(key, list);
		}
	
	
		PositionUpdate newPosition=new PositionUpdate();
		Position p=new Position();
		p.setXPos(updatedPosition.getNewPosition().getXPos());
		p.setYPos(updatedPosition.getNewPosition().getYPos());
		//System.out.println("EnviromentProvider Step Model:"+ key.getLocalName() +":"+ list.size() +","+p.getXPos() +","+p.getYPos() );
		newPosition.setNewPosition(p);
		newPosition.setCustomizedParameter(updatedPosition.getCustomizedParameter());
		list.add(newPosition);
		if(transactionSize<list.size())
		{
			transactionSize=list.size();
		}
		
	
	}
		else
		{
			try
			{
			 EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
			 mainSlice.stepModel(key, updatedPosition);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	
		}
		
	}

	
	/**
	 * Gets the transaction size.
	 *
	 * @return the transaction size
	 */
	private int getTransactionSize() {
		int result=0;  
		try
		{
			
		   if(masterNode)
		   {
			 
			   result=this.transactionSize;
		   }
		   else
		   {
			   EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(getMasterSlice());
			   result=mainSlice.getTransactionSize();
		
		   }
		}
		catch(Exception e)
		{
		  e.printStackTrace();	
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getHorizontalInterface()
	 */
	@SuppressWarnings({ "rawtypes" })
	public Class getHorizontalInterface(){
		return EnvironmentProviderSlice.class;
	}
	
	/**
	 * Gets the local slize.
	 *
	 * @return the local slize
	 */
	public Service.Slice getLocalSlize(){
		return localSlice;
	}
	
	
	
	
	
	
	
	/**
	 * This class implements the EnvironmentProviderHelper interface. It provides access to the services methods for agents 
	 * @author Nils
	 *
	 */
	public class EnvironmentProviderHelperImpl implements EnvironmentProviderHelper{

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getEnvironment()
		 */
		@Override
		public Physical2DEnvironment getEnvironment() {
			return EnvironmentProviderService.this.getEnvironment();
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getObject(java.lang.String)
		 */
		@Override
		public Physical2DObject getObject(String objectID) {
			return EnvironmentProviderService.this.getObject(objectID);
		}

		/* (non-Javadoc)
		 * @see jade.core.ServiceHelper#init(jade.core.Agent)
		 */
		@Override
		public void init(Agent arg0) {
			
		}
		

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#setCurrentPos(int)
		 */
		@Override
		public void setCurrentPos(int pos) {
			step=pos;
			
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getCurrentPos()
		 */
		@Override
		public int getCurrentPos() {
		
			return step;
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#setEnvironment(agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment)
		 */
		@Override
		public void setEnvironment(Physical2DEnvironment environment) {
			EnvironmentProviderService.this.setEnvironment(environment);
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getCurrentlyMovingAgents()
		 */
		@Override
		public HashSet<ActiveObject> getCurrentlyMovingAgents() {
			return EnvironmentProviderService.this.getCurrentlyMovingAgents();
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#setMovement(java.lang.String, agentgui.envModel.p2Dsvg.ontology.Movement)
		 */
		@Override
		public boolean setMovement(String agentID, Movement movement) {
			return EnvironmentProviderService.this.setMovement(agentID, movement);
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getSVGDoc()
		 */
		@Override
		public Document getSVGDoc() {
			return EnvironmentProviderService.this.getSVGDoc();
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#setSVGDoc(org.w3c.dom.Document)
		 */
		@Override
		public void setSVGDoc(Document svgDoc) {
			EnvironmentProviderService.this.setSVGDoc(svgDoc);
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getCurrentlyMovingObjects()
		 */
		@Override
		public HashSet<Physical2DObject> getCurrentlyMovingObjects() {
			return EnvironmentProviderService.this.getCurrentlyMovingObjects();
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getPlaygroundObjects(java.lang.String)
		 */
		@Override
		public List<Physical2DObject> getPlaygroundObjects(String playgroundID) {
			return EnvironmentProviderService.this.getPlaygroundObjects(playgroundID);
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#releasePassiveObject(java.lang.String)
		 */
		@Override
		public void releasePassiveObject(String objectID) {
			EnvironmentProviderService.this.releasePassiveObject(objectID);
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#assignPassiveObject(java.lang.String, java.lang.String)
		 */
		@Override
		public boolean assignPassiveObject(String objectID, String agentID) {
			return EnvironmentProviderService.this.assignPassiveObject(objectID, agentID);
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getProjectName()
		 */
		@Override
		public String getProjectName() {
			return EnvironmentProviderService.this.getProjectName();
		}
		
	
	
		
		


		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#is_running()
		 */
		@Override
		public boolean is_running() {
			return is_running;
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#setRunning(boolean)
		 */
		@Override
		public void setRunning(boolean running) {
			is_running=running;
			
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getModel(int)
		 */
		@Override
		public HashMap<AID, PositionUpdate> getModel(int pos) {
			return  EnvironmentProviderService.this.getModel(pos);
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#getTransactionSize()
		 */
		@Override
		public int getTransactionSize() {
			return  EnvironmentProviderService.this.getTransactionSize();
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper#stepModel(jade.core.AID, agentgui.envModel.p2Dsvg.ontology.PositionUpdate)
		 */
		@Override
		public void stepModel(AID key, PositionUpdate updatedPosition) {
			EnvironmentProviderService.this.stepModel(key, updatedPosition);
			
		}

		
	}
	
	/**
	 * This class implements the Service.Slice interface. It processes horizontal commands received from other nodes  
	 * @author Nils
	 *
	 */
	private class EnvironmentProviderSliceImpl implements Service.Slice{

		/** serialVersionUID. */
		private static final long serialVersionUID = -8545545290495057267L;

		/* (non-Javadoc)
		 * @see jade.core.Service.Slice#getNode()
		 */
		@Override
		public Node getNode() throws ServiceException {
			try {
				return EnvironmentProviderService.this.getLocalNode();
			} catch (IMTPException e) {
				throw new ServiceException("Unexpected error retrieving local node");
			}
		}

		/* (non-Javadoc)
		 * @see jade.core.Service.Slice#getService()
		 */
		@Override
		public Service getService() {
			return EnvironmentProviderService.this;
		}

		/* (non-Javadoc)
		 * @see jade.core.Service.Slice#serve(jade.core.HorizontalCommand)
		 */
		@Override
		public VerticalCommand serve(HorizontalCommand cmd) {
			
			if(cmd.getName().equals(EnvironmentProviderSlice.H_GET_ENVIRONMENT)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving environment request.");
				}
				cmd.setReturnValue(EnvironmentProviderService.this.getEnvironment());
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_GET_OBJECT)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving object request.");
				}
				String objectId = cmd.getParam(0).toString();
				Physical2DObject object = EnvironmentProviderService.this.getObject(objectId);
				cmd.setReturnValue(object);
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_GET_CURRENTLY_MOVING_OBJECTS)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving moving objects request.");
				}
				cmd.setReturnValue(EnvironmentProviderService.this.getCurrentlyMovingObjects());				
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_SET_MOVEMENT)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving set agent movement request.");
				}
				String agentID = (String) cmd.getParam(0);
				Movement movement = (Movement) cmd.getParam(1);
				cmd.setReturnValue(new Boolean(EnvironmentProviderService.this.setMovement(agentID, movement)));
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_GET_SVG_DOC)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving set agent movement request.");
				}
				cmd.setReturnValue(SVGUtils.svgToString(EnvironmentProviderService.this.getSVGDoc()));
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_GET_PLAYGROUND_OBJECTS)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving playground objects request.");
				}
				String pgID = (String) cmd.getParam(0);
				cmd.setReturnValue(EnvironmentProviderService.this.getPlaygroundObjects(pgID));
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_ASIGN_OBJECT)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving take object request.");
				}
				String objectID = (String) cmd.getParam(0);
				String agentID = (String) cmd.getParam(1);
				cmd.setReturnValue(new Boolean(EnvironmentProviderService.this.assignPassiveObject(objectID, agentID)));
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_RELEASE_OBJECT)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving put object request.");
				}
				String objectID = (String) cmd.getParam(0);
				EnvironmentProviderService.this.releasePassiveObject(objectID);
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_IS_MASTER)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving is master request.");
				}
				cmd.setReturnValue(new Boolean(EnvironmentProviderService.this.isMaster()));
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_GET_PROJECT_NAME)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving project name request.");
				}
				cmd.setReturnValue(EnvironmentProviderService.this.getProjectName());
			}
			else if(cmd.getName().equals(EnvironmentProviderSlice.H_STEP))
			{
			    AID aid=(AID) cmd.getParam(0);
			    PositionUpdate pos=(PositionUpdate) cmd.getParam(1);
			    EnvironmentProviderService.this.stepModel(aid, pos);
				cmd.setReturnValue(null);
			}
			else if(cmd.getName().equals(EnvironmentProviderSlice.H_TRANSACTION_SIZE))
			{
							
				cmd.setReturnValue(EnvironmentProviderService.this.getTransactionSize());
			}
			
			else if(cmd.getName().equals(EnvironmentProviderSlice.H_GET_MODEL))
			{
			
				int pos=((Integer) cmd.getParam(0)).intValue();
				cmd.setReturnValue(EnvironmentProviderService.this.getModel(pos));
			}
		
			
			return null;
		}
		
	}

}
