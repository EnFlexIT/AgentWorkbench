package sma.agents;

import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.provider.EnvironmentProviderHelper;
import mas.environment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;

public class SMAStarterAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public void setup(){
		Object[] args = getArguments();
		if(args[0] != null && args[0] instanceof Physical2DEnvironment){
			
			try {
				EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
				helper.setEnvironment((Physical2DEnvironment) args[0]);
				System.out.println("Testausgabe: Umgebung initialisiert");
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				doDelete();
			}
		}
	}
	
}
