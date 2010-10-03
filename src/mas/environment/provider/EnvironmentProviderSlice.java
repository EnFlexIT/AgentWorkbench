package mas.environment.provider;

import java.util.HashSet;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Movement;
import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
import jade.core.IMTPException;
import jade.core.Service.Slice;

public interface EnvironmentProviderSlice extends Slice {
	public static final String H_GET_ENVIRONMENT = "getEnvironment";
	public static final String H_GET_OBJECT = "getObject";
	public static final String H_GET_CURRENTLY_MOVING = "getCurrentlyMoving";
	public static final String H_SET_MOVEMENT = "setMovement";
	
	public Physical2DEnvironment getEnvironment() throws IMTPException;
	public Physical2DObject getObject(String id) throws IMTPException;
	public HashSet<ActiveObject> getCurrentlyMoving() throws IMTPException;
	public boolean setMovement(String agentID, Movement movement) throws IMTPException;
}
