package mas.environment.provider;

import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
import mas.environment.ontology.Position;
import mas.environment.utils.EnvironmentWrapper;
import jade.core.Agent;
import jade.core.BaseService;
import jade.core.Filter;
import jade.core.GenericCommand;
import jade.core.HorizontalCommand;
import jade.core.IMTPException;
import jade.core.Node;
import jade.core.Profile;
import jade.core.Service;
import jade.core.ServiceException;
import jade.core.ServiceHelper;
import jade.core.VerticalCommand;

public class EnvironmentProviderService extends BaseService {
	
	public static final String SERVICE_NAME ="mas.environment.provider.EnvironmentProvider";
	
	private Physical2DEnvironment environment = null;
	
	private EnvironmentWrapper envWrap = null;
	
	private ServiceHelper helper = new EnvironmentProviderHelperImpl();
	
	private Filter outFilter = new OutgoingEnvironmentProviderFilter();
	
	private Service.Slice localSlice = new EnvironmentProviderSliceImpl();
	
	private boolean masterNode = false;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return SERVICE_NAME;
	}
	
	void initEnvironment(Physical2DEnvironment env){
		environment = env;
		envWrap = new EnvironmentWrapper(environment);
		masterNode = true;
	}
	
	boolean isEnvReady(){
		return environment != null;
	}
	
	public void boot(Profile p){
		try {
			super.boot(p);
			System.out.println("Testausgabe: Starte "+SERVICE_NAME);
		} catch (ServiceException e) {
			System.err.println("Dienst starten fehlgeschlagen!");
		}
	}
	
	public ServiceHelper getHelper(Agent a){
		return helper;
	}
	
	public Class getHorizontalInterface(){
		return EnvironmentProviderSlice.class;
	}
	
	public Service.Slice getLocalSlice(){
		return localSlice;
	}
	
	Position getObjectPosition(String objectId){
		Position pos = null;
		try {
			EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(MAIN_SLICE);
			pos = mainSlice.getObjectPosition(objectId);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pos;
	}
	
	public Filter getCommandFilter(boolean direction){
		if (direction == Filter.OUTGOING){
			return outFilter;
		}else{
			return null;
		}
	}
	
	
	class EnvironmentProviderHelperImpl implements EnvironmentProviderHelper{

		@Override
		public boolean isEnvSet() {
			return environment != null;
		}

		@Override
		public void setEnvironment(Physical2DEnvironment environment) {
			initEnvironment(environment);
		}

		@Override
		public void init(Agent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Position getObjectPosition(String id) {
			return EnvironmentProviderService.this.getObjectPosition(id);
		}

		@Override
		public void horizontalTest(String testMsg) {
			GenericCommand cmd = new GenericCommand(EnvironmentProviderSlice.H_TEST_COMMAND, SERVICE_NAME, null);
			cmd.addParam(testMsg);
			try {
				getLocalNode().accept(cmd);
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class OutgoingEnvironmentProviderFilter extends Filter{
		public boolean accept(VerticalCommand cmd){
			Position pos = null;
			if(cmd.getName().equals(EnvironmentProviderSlice.H_POSITION_REQUEST)){
				try {
					EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(MAIN_SLICE);
					pos = mainSlice.getObjectPosition((String) cmd.getParams()[0]);
					
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(cmd.getName().equals(EnvironmentProviderSlice.H_TEST_COMMAND)){
				try {
					EnvironmentProviderSlice mainSlice = (EnvironmentProviderSlice) getSlice(MAIN_SLICE);
					mainSlice.test((String) cmd.getParam(0));
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return true;
		}
	}

	private class EnvironmentProviderSliceImpl implements Service.Slice{

		@Override
		public Node getNode() throws ServiceException {
			Node localNode = null;
			try {
				return EnvironmentProviderService.this.getLocalNode();
			} catch (IMTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return localNode;
		}

		@Override
		public Service getService() {
			return EnvironmentProviderService.this;
		}

		@Override
		public VerticalCommand serve(HorizontalCommand inCmd) {
			try {
				System.out.println("Testausgabe: "+getLocalSlice().getNode().getName()+" received "+inCmd.getName());
			} catch (ServiceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(inCmd.getName().equals(EnvironmentProviderSlice.H_POSITION_REQUEST)){
				Object[] params = inCmd.getParams();
				String objectId = (String) params[0];
				
				Position objectPos = envWrap.getObjectById(objectId).getPosition();
				try {
					GenericCommand outCmd = new GenericCommand(EnvironmentProviderSlice.H_POSITION_RESPONSE, SERVICE_NAME, inCmd.getInteraction());
					outCmd.addParam(objectPos);
					getNode().accept(outCmd);
				} catch (IMTPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(inCmd.equals(EnvironmentProviderSlice.H_TEST_COMMAND)){
				System.out.println(inCmd.getParam(0));
			}
			return null;
		}
		
	}
}
