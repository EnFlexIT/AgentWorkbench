package mas.environment.provider;

import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Position;
import jade.core.ServiceHelper;

public interface EnvironmentProviderHelper extends ServiceHelper {
	public void setEnvironment(Physical2DEnvironment environment);
	public boolean isEnvSet();
	public Position getObjectPosition(String id);
	public void horizontalTest(String testMsg);
}
