package mas.environment.provider;

import java.util.HashSet;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Movement;
import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
import jade.core.ServiceHelper;

public interface EnvironmentProviderHelper extends ServiceHelper {
	public Physical2DEnvironment getEnvironment();
	public Physical2DObject getObject(String id);
	public void setEnvironment(Physical2DEnvironment environment);
	public HashSet<ActiveObject> getCurrentlyMoving();
	public boolean setMovement(String agentID, Movement movement);
}
