 package agentgui.physical2Denvironment.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;

import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.Movement;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.PositionUpdate;

import jade.core.AID;
import jade.core.ServiceHelper;
/**
 * This interfaces provides access to the EnvironmentProviderService for agents
 * @author Nils
 *
 */
public interface EnvironmentProviderHelper extends ServiceHelper {
	/**
	 * Gets the whole environment model managed by the EnvironmentProviderService
	 * @return The environment model
	 */
	public Physical2DEnvironment getEnvironment();
	/**
	 * Gets an environment object's data from the environment model   
	 * @param id The objects ID
	 * @return The Physical2DObject instance representing the object with the specified IF
	 */
	public Physical2DObject getObject(String id);
	/**
	 * Sets the environment model
	 * This method is intended to be used only by the EnvironmentProviderAgent when initializing the service.
	 * It is always executed at the local node, accessing remote slices is not implemented. 
	 * @param environment The environment model
	 */
	public void setEnvironment(Physical2DEnvironment environment);
	/**
	 * Gets a list of all currently moving agents.
	 * This method is intended to be used only by the EnvironmentProviderAgent for updating the positions
	 * of the agents and the objects controlled by them in the environment model.
	 * It is always executed at the local node, accessing remote slices is not implemented.  
	 * @return
	 */
	public HashSet<ActiveObject> getCurrentlyMovingAgents();
	/**
	 * Gets a list of all objects currently moving in the environment model.
	 * @return The list
	 */
	public HashSet<Physical2DObject> getCurrentlyMovingObjects();
	/**
	 * Sets an agent's current movement
	 * @param agentID The agent's ID
	 * @param movement The agent's movement
	 * @return True if the movement could be set, false if not (maximum speed exceeded?)
	 */
	public boolean setMovement(String agentID, Movement movement);
	/**
	 * Gets the SVG document visualizing the environment model
	 * @return The SVG document
	 */
	public Document getSVGDoc();
	/**
	 * Sets the SVG document visualizing the environment model.
	 * This method is intended to be used only by the EnvironmentProviderAgent when initializing the service.
	 * It is always executed at the local node, accessing remote slices is not implemented. 
	 * @param svgDoc The SVG document
	 */
	public void setSVGDoc(Document svgDoc);
	/**
	 * Gets a list of all environment objects existing inside of a playground
	 * @param playgroundID The playground's ID
	 * @return The list
	 */
	public List<Physical2DObject> getPlaygroundObjects(String playgroundID);
	/**
	 * Assigns a PassiveObject to an ActiveObject 
	 * @param passiveObjectID The object's ID
	 * @param activeObjectID The agent's ID
	 * @return Successful?
	 */
	public boolean assignPassiveObject(String passiveObjectID, String activeObjectID);
	/**
	 * Releases a PassiveObject from an ActiveObject controlling it
	 * @param passiveObjectID The PassiveObject's ID
	 */
	public void releasePassiveObject(String passiveObjectID);
	/**
	 * @return The name of the project the Physical2DEnvironment belongs to.
	 */
	public String getProjectName();
	public void stepModel(AID key, PositionUpdate updatedPosition);
	public HashMap<AID,PositionUpdate> getModel(int pos);
}
