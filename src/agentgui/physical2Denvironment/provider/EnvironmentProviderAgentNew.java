package agentgui.physical2Denvironment.provider;

import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Document;

import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.PassiveObject;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.ServiceHelper;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class EnvironmentProviderAgentNew extends Agent  {
	private EnvironmentProviderHelper helper = null;
	
	
		public void setup(){
			Object[] args = getArguments();
			if(args != null && args[0] != null && args[0] instanceof Physical2DEnvironment){
				try {
					this.helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
					helper.setEnvironment((Physical2DEnvironment) args[0]);
					if(args[1] != null && args[1] instanceof Document){
						helper.setSVGDoc((Document) args[1]);
					}
					this.addBehaviour(new UpdatePositionsBehaviour(this));
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
		private class UpdatePositionsBehaviour extends OneShotBehaviour{
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 3819944524810503066L;
			private EnvironmentProviderHelper helper;

			public UpdatePositionsBehaviour(Agent a) throws ServiceException {
				super(a);
				helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
			}

			
			

			@Override
			public void action() {
				HashSet<ActiveObject> moving = new HashSet<ActiveObject>( helper.getCurrentlyMovingAgents() );
				if(moving.size() > 0){	// Any moving agents?
					
					Iterator<ActiveObject> movingAgents = moving.iterator();
					while(movingAgents.hasNext()){
						ActiveObject movingAgent = movingAgents.next();
						
			
						// Update the agent's position
						movingAgent.getPosition().setXPos(movingAgent.getPosition().getXPos() );
						movingAgent.getPosition().setYPos(movingAgent.getPosition().getYPos());
						
						// Update the positions of all PassiveObjects controlled by this agent, if any
						Iterator<PassiveObject> controlledObjects = movingAgent.getAllPayload();
						while(controlledObjects.hasNext()){
							PassiveObject controlledObject = controlledObjects.next();
							controlledObject.getPosition().setXPos(movingAgent.getPosition().getXPos());
							controlledObject.getPosition().setYPos(movingAgent.getPosition().getYPos());
						}
					}
				}
				
			}
		}
	}
	
	
	
	
	
	
	


