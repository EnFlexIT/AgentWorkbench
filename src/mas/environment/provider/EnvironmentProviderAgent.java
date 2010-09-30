package mas.environment.provider;

import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.utils.EnvironmentWrapper;
import jade.core.Agent;
import jade.core.ServiceException;
/**
 * Agent managing a Physical2dEnvironment instance
 * @author Nils
 *
 */
public class EnvironmentProviderAgent extends Agent {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2239610711152280115L;
	/**
	 * The Physical2DEnvironment managed by this agent
	 */
	private Physical2DEnvironment environment;
	
	/**
	 * Wrapper object for easier handling of the Physical2DEnvironment
	 */
	private EnvironmentWrapper envWrap = null;
	
	/**
	 * Setup method 
	 * This method initializes environment and envWrap properties and registers this agent at the local EnvironmentProviderService
	 */
	public void setup(){
		Object[] args = getArguments();
		if(args != null && args[0] instanceof Physical2DEnvironment){
			setEnvironment((Physical2DEnvironment) args[0]);
		}else{
			System.err.println(getLocalName()+"- Error: No Physical2DEnvironment given, shutting down!");
		}
		try {
			EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
			helper.registerEnvironmentProviderAgent(this);
		} catch (ServiceException e) {
			System.err.println(getLocalName()+" - Error: Environment provider service not found, shutting down!");
		}
	}
	
	private void setEnvironment(Physical2DEnvironment environment){
		this.environment = environment;
		this.envWrap = new EnvironmentWrapper(this.environment);
	}
	
	Physical2DEnvironment getEnvironment(){
		return this.environment;
	}
	
	EnvironmentWrapper getEnvWrap(){
		return this.envWrap;
	}

}
