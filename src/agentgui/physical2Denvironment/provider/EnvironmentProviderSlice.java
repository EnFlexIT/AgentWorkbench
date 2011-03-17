package agentgui.physical2Denvironment.provider;

import jade.core.AID;
import jade.core.IMTPException;
import jade.core.Service.Slice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;

import agentgui.physical2Denvironment.ontology.Movement;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.PositionUpdate;

public interface EnvironmentProviderSlice extends Slice {
	public static final String H_GET_ENVIRONMENT = "getEnvironment";
	public static final String H_GET_OBJECT = "getObject";
	public static final String H_GET_CURRENTLY_MOVING_OBJECTS = "getCurrentlyMovingObjects";
	public static final String H_SET_MOVEMENT = "setMovement";
	public static final String H_GET_SVG_DOC = "getSVGDoc";
	public static final String H_GET_PLAYGROUND_OBJECTS = "getPlaygroundObjects";
	public static final String H_ASIGN_OBJECT = "assignObject";
	public static final String H_RELEASE_OBJECT = "releaseObject";
	public static final String H_IS_MASTER = "isMaster";
	public static final String H_GET_PROJECT_NAME = "getProjectName";
	public static final String H_STEP="stepSimulation";
	public static final String H_GET_MODEL="getModel";
	public static final String H_TRANSACTION_SIZE="transaction";
	
	
	public Physical2DEnvironment getEnvironment() throws IMTPException;
	public Physical2DObject getObject(String id) throws IMTPException;
	public HashSet<Physical2DObject> getCurrentlyMovingObjects() throws IMTPException;
	public boolean setMovement(String agentID, Movement movement) throws IMTPException;
	public Document getSVGDoc() throws IMTPException;
	public List<Physical2DObject> getPlaygroundObjects(String playgroundID) throws IMTPException;
	public boolean assignPassiveObject(String objectID, String agentID) throws IMTPException;
	public void releasePassiveObject(String objectID) throws IMTPException;
	public String getProjectName() throws IMTPException;
	public boolean isMaster() throws IMTPException;
	public void stepModel(AID key, PositionUpdate updatedPosition) throws IMTPException;
	public HashMap<AID, PositionUpdate> getModel(int pos)  throws IMTPException;
	public int getTransactionSize() throws IMTPException;

}
