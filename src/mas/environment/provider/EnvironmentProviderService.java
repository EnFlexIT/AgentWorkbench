package mas.environment.provider;

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
	
	private ServiceHelper helper = new EnvironmentProviderHelperImpl();
	
	private Service.Slice localSlice = new EnvironmentProviderSliceImpl();
	
	private boolean masterNode = false;
	
	private EnvironmentProviderAgent epa;

	@Override
	public String getName() {
		 return SERVICE_NAME;
	}
	
	public void boot(Profile p){
		try {
			super.boot(p);
			System.out.println(getLocalNode().getName());
			System.out.println("Service-name: "+this.getName());
			
			getLocalNode().exportSlice(SERVICE_NAME, getLocalSlize());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IMTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void registerEPA(EnvironmentProviderAgent epa){
		this.epa = epa;
		this.masterNode = true;
	}
	/**
	 * Gets the services environment Physical2DEnvironment object
	 * @return The Physical2DEnvironment object
	 */
	private Physical2DEnvironment getEnvironment(){
		Physical2DEnvironment env = null;
		if(masterNode){
			env = epa.getEnvironment();
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
			object = epa.getEnvWrap().getObjectById(id);
		}else{
		
			try {
				EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(MAIN_SLICE);
				try {
					object = mainSlice.getObject(id);
				} catch (IMTPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return object;
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
		public void registerEnvironmentProviderAgent(
				EnvironmentProviderAgent epa) {
			EnvironmentProviderService.this.registerEPA(epa);
		}
	}
	
	private class EnvironmentProviderSliceImpl implements Service.Slice{

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -8545545290495057267L;

		@Override
		public Node getNode() throws ServiceException {
			Node node = null;
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
			}
			return null;
		}
		
	}

}
