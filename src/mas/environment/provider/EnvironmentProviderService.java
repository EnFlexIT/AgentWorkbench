package mas.environment.provider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;

import mas.display.SVGUtils;
import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Movement;
import mas.environment.ontology.PassiveObject;
import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
import mas.environment.ontology.PlaygroundObject;
import mas.environment.utils.EnvironmentWrapper;
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
/**
 * This service provides and manages a Physical2DEnvironment for a distributed simulation 
 * @author Nils
 *
 */
public class EnvironmentProviderService extends BaseService {
	/**
	 * The service name
	 */
	public static String SERVICE_NAME = "mas.environment.provider.EnvironmentProvider";
	/**
	 * The Physical2DEnvironment handled by this service
	 */
	private Physical2DEnvironment environment = null;
	/**
	 * Wrapper object for easier handling of the Physical2DEnvironment object
	 */
	private EnvironmentWrapper envWrap = null;
	/**
	 * The SVG document visualizing the Physical2DEnvironment
	 */
	private Document svgDoc = null;
	/**
	 * The EnvironmentProviderHelper instance 
	 */
	private ServiceHelper helper = new EnvironmentProviderHelperImpl();
	/**
	 * The local EnvironmentProviderSlice instance
	 */
	private Service.Slice localSlice = new EnvironmentProviderSliceImpl();
	/**
	 * Is the local node hosting the Physical2DEnvironment
	 */
	private boolean masterNode = false;
	/**
	 * The name of the node hosting the Physical2DEnvironment
	 */
	private String masterSlice = null;
	/**
	 * Set of currently moving ActiveObjects
	 */
	private HashSet<ActiveObject> currentlyMovingAgents;
	/**
	 * Set of currently moving Physical2DObjects
	 */
	private HashSet<Physical2DObject> currentlyMovingObjects;
	
	@Override
	public String getName() {
		 return SERVICE_NAME;
	}
	
	public void boot(Profile p){
		try {
			super.boot(p);
			getLocalNode().exportSlice(SERVICE_NAME, getLocalSlize());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IMTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Initializes the Physical2DEnvironment
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return masterSlice;
	}
	
	/**
	 * @return Result Is the local node the master node?
	 */
	private boolean isMaster(){
		return masterNode;
	}
	/**
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return env;
	}
	/**
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return projectName;
		}
	}
	/**
	 * Gets the Physical2DObject with the specified ID from the Physical2DEnvironment
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return object;
	}
	
	/**
	 * @return List of all currently moving ActiveObjects within the Physical2DEnvironment 
	 */
	private HashSet<ActiveObject> getCurrentlyMovingAgents(){
		return this.currentlyMovingAgents;
	}
	/**
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return objects;
	}
	
	/**
	 * Setting an ActiveObjects movement
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
				float maxSpeed = agent.getMaxSpeed();
				if(agent.getMovement().getSpeed() <= maxSpeed+0.0005){		// Small tolerance required for inaccuracy in speed calculation  
					agent.setMovement(movement);
				}
				if(agent.getMovement().getSpeed() > 0){
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
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
	 * Gets all Physical2DObjects within a specified PlaygroundObject
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return objects;
	}
	
	/**
	 * Assigns a PassiveObject to an ActiveObject
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return success;
	}
	
	/**
	 * Releases a PassiveObject from an ActiveObject controlling it
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets the SVG document
	 * @param svgDoc the SVG document
	 */
	private void setSVGDoc(Document svgDoc){
		this.svgDoc = svgDoc;
	}
	
	public ServiceHelper getHelper(Agent a){
		return helper;
	}
	
	@SuppressWarnings("unchecked")
	public Class getHorizontalInterface(){
		return EnvironmentProviderSlice.class;
	}
	
	public Service.Slice getLocalSlize(){
		return localSlice;
	}
	
	
	
	/**
	 * This class implements the EnvironmentProviderHelper interface. It provides access to the services methods for agents 
	 * @author Nils
	 *
	 */
	public class EnvironmentProviderHelperImpl implements EnvironmentProviderHelper{

		@Override
		public Physical2DEnvironment getEnvironment() {
			return EnvironmentProviderService.this.getEnvironment();
		}

		@Override
		public Physical2DObject getObject(String objectID) {
			return EnvironmentProviderService.this.getObject(objectID);
		}

		@Override
		public void init(Agent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setEnvironment(Physical2DEnvironment environment) {
			EnvironmentProviderService.this.setEnvironment(environment);
		}

		@Override
		public HashSet<ActiveObject> getCurrentlyMovingAgents() {
			return EnvironmentProviderService.this.getCurrentlyMovingAgents();
		}

		@Override
		public boolean setMovement(String agentID, Movement movement) {
			return EnvironmentProviderService.this.setMovement(agentID, movement);
		}

		@Override
		public Document getSVGDoc() {
			return EnvironmentProviderService.this.getSVGDoc();
		}

		@Override
		public void setSVGDoc(Document svgDoc) {
			EnvironmentProviderService.this.setSVGDoc(svgDoc);
		}

		@Override
		public HashSet<Physical2DObject> getCurrentlyMovingObjects() {
			return EnvironmentProviderService.this.getCurrentlyMovingObjects();
		}

		@Override
		public List<Physical2DObject> getPlaygroundObjects(String playgroundID) {
			return EnvironmentProviderService.this.getPlaygroundObjects(playgroundID);
		}

		@Override
		public void releasePassiveObject(String objectID) {
			EnvironmentProviderService.this.releasePassiveObject(objectID);
		}

		@Override
		public boolean assignPassiveObject(String objectID, String agentID) {
			return EnvironmentProviderService.this.assignPassiveObject(objectID, agentID);
		}

		@Override
		public String getProjectName() {
			return EnvironmentProviderService.this.getProjectName();
		}
	}
	
	/**
	 * This class implements the Service.Slice interface. It processes horizontal commands received from other nodes  
	 * @author Nils
	 *
	 */
	private class EnvironmentProviderSliceImpl implements Service.Slice{

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -8545545290495057267L;

		@Override
		public Node getNode() throws ServiceException {
			try {
				return EnvironmentProviderService.this.getLocalNode();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				throw new ServiceException("Unexpected error retrieving local node");
			}
		}

		@Override
		public Service getService() {
			return EnvironmentProviderService.this;
		}

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
			return null;
		}
		
	}

}
