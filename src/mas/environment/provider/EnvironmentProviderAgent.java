package mas.environment.provider;

import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Document;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.PassiveObject;
import mas.environment.ontology.Physical2DEnvironment;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;
/**
 * Agent managing a Physical2dEnvironment instance
 * @author Nils
 *
 */
public class EnvironmentProviderAgent extends Agent {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2239610711152280115L;
	/**
	 * Number of milliseconds between two position updates
	 */
	private final int PERIOD = 100;
	
	private EnvironmentProviderHelper helper = null;
	
	
	/**
	 * Setup method 
	 * This method initializes environment and envWrap properties and registers this agent at the local EnvironmentProviderService
	 */
	public void setup(){
		Object[] args = getArguments();
		if(args != null && args[0] != null && args[0] instanceof Physical2DEnvironment){
			try {
				this.helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
				helper.setEnvironment((Physical2DEnvironment) args[0]);
				if(args[1] != null && args[1] instanceof Document){
					helper.setSVGDoc((Document) args[1]);
				}
				this.addBehaviour(new UpdatePositionsBehaviour(this, PERIOD));
			} catch (ServiceException e) {
				System.err.println(getLocalName()+" - Error: Environment provider service not found, shutting down!");
				doDelete();
			}
		}else{
			System.err.println(getLocalName()+" - Error: No Physical2DEnvironment given, shutting down!");
			doDelete();
		}
		
	}
	/**
	 * This behaviour updates the positions of all moving environment objects
	 * @author Nils
	 *
	 */
	private class UpdatePositionsBehaviour extends TickerBehaviour{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3819944524810503066L;
		private EnvironmentProviderHelper helper;

		public UpdatePositionsBehaviour(Agent a, long period) throws ServiceException {
			super(a, period);
			helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onTick() {
			HashSet<ActiveObject> moving = helper.getCurrentlyMovingAgents();
			if(moving.size() > 0){	// Any moving agents?
				Iterator<ActiveObject> movingAgents = moving.iterator();
				
				while(movingAgents.hasNext()){
					ActiveObject movingAgent = movingAgents.next();
					
					// Calculate position change per period
					float xPosChange = movingAgent.getMovement().getXPosChange()/1000*PERIOD;
					float yPosChange = movingAgent.getMovement().getYPosChange()/1000*PERIOD;
					
					// Update the agent's position
					movingAgent.getPosition().setXPos(movingAgent.getPosition().getXPos() + xPosChange);
					movingAgent.getPosition().setYPos(movingAgent.getPosition().getYPos() + yPosChange);
					
					// Update the positions of all PassiveObjects controlled by this agent, if any
					Iterator<PassiveObject> controlledObjects = movingAgent.getAllPayload();
					while(controlledObjects.hasNext()){
						PassiveObject controlledObject = controlledObjects.next();
						controlledObject.getPosition().setXPos(controlledObject.getPosition().getXPos() + xPosChange);
						controlledObject.getPosition().setYPos(controlledObject.getPosition().getYPos() + yPosChange);
					}
				}
			}			
		}
		
	}
	
}
