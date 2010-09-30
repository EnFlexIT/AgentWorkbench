package mas.environment.provider;

import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
import jade.core.ServiceHelper;

public interface EnvironmentProviderHelper extends ServiceHelper {
	public Physical2DEnvironment getEnvironment();
	public Physical2DObject getObject(String id);
	public void registerEnvironmentProviderAgent(EnvironmentProviderAgent epa);
}
