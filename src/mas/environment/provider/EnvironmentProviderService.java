package mas.environment.provider;

import java.util.HashSet;

import org.w3c.dom.Document;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Movement;
import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
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
	 * The SVG document visualizing the environment
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
	 * True if this node hosts the Physical2DEnvironment
	 */
	private boolean masterNode = false;
	/**
	 * Set of currently moving ActiveObjects
	 */
	private HashSet<ActiveObject> currentlyMoving;
	
	@Override
	public String getName() {
		 return SERVICE_NAME;
	}
	
	public void boot(Profile p){
		try {
			super.boot(p);
			System.out.println(getLocalNode().getName());
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
	 * Initializes the service's environment
	 * @param environment The environment
	 */
	private void setEnvironment(Physical2DEnvironment environment){
		if(environment != null){
			this.environment = environment;
			this.envWrap = new EnvironmentWrapper(environment);
			this.currentlyMoving = new HashSet<ActiveObject>();
			this.masterNode = true;
		}else{
			this.environment = null;
			this.envWrap = null;
			this.currentlyMoving = null;
			this.masterNode = false;
		}
	}
	
	/**
	 * Gets the services environment Physical2DEnvironment object
	 * @return The Physical2DEnvironment object
	 */
	private Physical2DEnvironment getEnvironment(){
		Physical2DEnvironment env = null;
		if(masterNode){
			env = environment;
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(MAIN_SLICE);
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
	 * Gets the Physical2DObject instance with the specified id from the environment
	 * @param id The object id to look for
	 * @return The Physical2DObject instance, or null if not found
	 */
	private Physical2DObject getObject(String id){
		Physical2DObject object = null;
		
		if(masterNode){
			object = envWrap.getObjectById(id);
		}else{
		
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(MAIN_SLICE);
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
	
	private HashSet<ActiveObject> getCurrentlyMoving(){
		HashSet<ActiveObject> cm = null;
		if(masterNode){
			cm = this.currentlyMoving;
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(MAIN_SLICE);
				cm = mainSlice.getCurrentlyMoving();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cm;
	}
	
	private boolean setMovement(String agentID, Movement movement){
		boolean result = false;
		if(masterNode){
			Physical2DObject object = getObject(agentID);
			if(object != null && object instanceof ActiveObject){
				ActiveObject agent = (ActiveObject) object;
				float maxSpeed = agent.getMaxSpeed();
				if(movement.getSpeed() <= maxSpeed+0.0005){		// Small tolerance required for inaccuracy in speed calculation  
					agent.setMovement(movement);
				}
				if(agent.getMovement().getSpeed() > 0){
					this.currentlyMoving.add(agent);
				}else{
					this.currentlyMoving.remove(agent);
				}
				result = true;
			}
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(MAIN_SLICE);
				result = mainSlice.setMovement(agentID, movement);
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
	
	private Document getSVGDoc(){
		Document doc = null;
		if(masterNode){
			doc = this.svgDoc;
		}else{
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(MAIN_SLICE);
				doc = mainSlice.getSVGDoc();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return doc;
	}
	
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
	
	public class EnvironmentProviderHelperImpl implements EnvironmentProviderHelper{

		@Override
		public Physical2DEnvironment getEnvironment() {
			return EnvironmentProviderService.this.getEnvironment();
		}

		@Override
		public Physical2DObject getObject(String id) {
			return EnvironmentProviderService.this.getObject(id);
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
		public HashSet<ActiveObject> getCurrentlyMoving() {
			return EnvironmentProviderService.this.getCurrentlyMoving();
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
	}
	
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
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_GET_CURRENTLY_MOVING)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving moving objects request.");
				}
				cmd.setReturnValue(EnvironmentProviderService.this.getCurrentlyMoving());				
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_SET_MOVEMENT)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving set agent movement request.");
				}
				Object[] params = cmd.getParams();
				String agentID = params[0].toString();
				Movement movement = (Movement) params[1];
				cmd.setReturnValue(new Boolean(EnvironmentProviderService.this.setMovement(agentID, movement)));
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_GET_SVG_DOC)){
				if(myLogger.isLoggable(Logger.FINE)){
					myLogger.log(Logger.FINE, "Serving set agent movement request.");
				}
				cmd.setReturnValue(EnvironmentProviderService.this.getSVGDoc());
			}
			return null;
		}
		
	}

}
