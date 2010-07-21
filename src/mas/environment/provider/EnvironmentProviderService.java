package mas.environment.provider;

import java.util.HashMap;

import mas.display.ontology.Environment;
import jade.core.AgentContainer;
import jade.core.BaseService;
import jade.core.Profile;
import jade.core.ProfileException;

/**
 * Core service providing environment information to agents 
 * @author Nils
 *
 */
public class EnvironmentProviderService extends BaseService {
	/**
	 * The service name
	 */
	public static final String NAME = "environment.provider.EnvironmentProvider";
	
	/**
	 * The projects Environment
	 */
	private HashMap<String, Environment> environments = null;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}
	
	public void init(AgentContainer ac, Profile p){
		try {
			super.init(ac, p);
			this.environments = new HashMap<String, Environment>();
		} catch (ProfileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void addEnvironment(Environment env){
		this.environments.put(env.getProjectName(), env);
	}

}
