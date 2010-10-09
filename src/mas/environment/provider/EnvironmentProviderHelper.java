 package mas.environment.provider;

import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Movement;
import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
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
	 * Lets an agent take control of an environment object 
	 * @param objectID The object's ID
	 * @param agentID The agent's ID
	 * @return True if successful, false if not (Can the object be controlled? Is another agent controlling it already?)
	 */
	public boolean takeObject(String objectID, String agentID);
	/**
	 * Lets an agent release a controlled object
	 * @param objectID The object's ID
	 */
	public void putObject(String objectID);
}
