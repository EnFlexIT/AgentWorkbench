package mas.environment.provider;

import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Document;

import mas.environment.ontology.ActiveObject;
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

		@Override
		protected void onTick() {
			HashSet<ActiveObject> moving = helper.getCurrentlyMoving();
			if(moving.size() > 0){
				Iterator<ActiveObject> movingAgents = moving.iterator();
				while(movingAgents.hasNext()){
					ActiveObject movingAgent = movingAgents.next();
					movingAgent.getPosition().setXPos(
							movingAgent.getPosition().getXPos() 
							+ movingAgent.getMovement().getXPosChange()/1000*PERIOD
					);
					movingAgent.getPosition().setYPos(
							movingAgent.getPosition().getYPos() 
							+ movingAgent.getMovement().getYPosChange()/1000*PERIOD
					);
				}
			}
		}
		
	}
	
}
