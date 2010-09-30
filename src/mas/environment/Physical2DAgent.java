package mas.environment;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Physical2DObject;
import mas.environment.provider.EnvironmentProviderHelper;
import mas.environment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;

/**
 * Abstract superclass for agents acting in a Physical2DEnvironment
 * @author Nils
 *
 */
public abstract class Physical2DAgent extends Agent{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7138137074364044922L;
	/**
	 * This agent's representation in the environment model
	 */
	protected ActiveObject myEnvironmentObject;
	/**
	 * The ID of this agent's environment object 
	 * Default: The agent's localName. Subclasses can assign other IDs in their setup method 
	 */
	protected String myObjectID;
	
	public void setup(){
		myObjectID = getLocalName();
		try {
			myEnvironmentObject = getMyEnvironmentOject();
			if(myEnvironmentObject == null){
				System.err.println(getLocalName()+": No matching environment object found, shutting down!");
				doDelete();
			}
		} catch (ServiceException e) {
			System.err.println(getLocalName()+": Environment provider service not found, shutting down!");
			doDelete();
		}
	}
	
	
	
	
	/**
	 * This method gets the ActiveObject representing this agent from the environment model
	 * @return The ActiceObject representing this agent. Null if no object with myObjectID was found 
	 * @throws ServiceException EnvironmentProviderHelper could not be retrieved
	 */
	private ActiveObject getMyEnvironmentOject() throws ServiceException{
			EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
			return (ActiveObject) helper.getObject(myObjectID);
	}
	/**
	 * Gets a Physical2DObject from the EnvironmentProviderService
	 * @param id The object ID to look for
	 * @return The Physical2DObject with the given ID. Null if no matching object was found.
	 * @throws ServiceException
	 */
	private Physical2DObject getEnvironmentObject(String id) throws ServiceException{
		EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
		return helper.getObject(myObjectID);
	}

}
