package mas.environment.provider;

import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Movement;
import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
import jade.core.ServiceHelper;

public interface EnvironmentProviderHelper extends ServiceHelper {
	public Physical2DEnvironment getEnvironment();
	public Physical2DObject getObject(String id);
	public void setEnvironment(Physical2DEnvironment environment);
	public HashSet<ActiveObject> getCurrentlyMovingAgents();
	public HashSet<Physical2DObject> getCurrentlyMovingObjects();
	public boolean setMovement(String agentID, Movement movement);
	public Document getSVGDoc();
	public void setSVGDoc(Document svgDoc);
	public List<Physical2DObject> getPlaygroundObjects(String playgroundID);
	public boolean takeObject(String objectID, String agentID);
	public void putObject(String objectID);
}
