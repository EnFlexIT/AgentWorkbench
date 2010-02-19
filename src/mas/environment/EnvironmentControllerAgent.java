package mas.environment;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Agent zur Kommunikation zwischen EnvironmentController und den eigentlichen Simulations-Agenten
 * @author Nils
 *
 */
public class EnvironmentControllerAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnvironmentController ec;
	
	public void setup(){
		Object[] args = getArguments();
		if(args[0] != null && args[0] instanceof EnvironmentController){
			this.ec = (EnvironmentController) args[0];
		}
		
		try {
			TopicManagementHelper tmh = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
			AID positionTopic = tmh.createTopic("position");
			tmh.register(positionTopic);
			addBehaviour(new PosUpdateBehaviour(MessageTemplate.MatchTopic(positionTopic)));
		
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	class PosUpdateBehaviour extends CyclicBehaviour{
		
		MessageTemplate positionTemplate;
		
		public PosUpdateBehaviour(MessageTemplate mt){
			this.positionTemplate = mt;
		}

		@Override
		public void action() {
			ACLMessage posUpdate = receive(positionTemplate);
			if(posUpdate != null){
				String sender = posUpdate.getSender().getLocalName();
				String content = posUpdate.getContent();
				if(content.equals("bye")){
					// Agent wurde beendet -> entferne aus HashMap
					ec.getMainPlayground().removeElement(sender);
				}else{
					// Aktualisiere Positionsdaten des Absenders in der HashMap
					String[] pos = content.split(",");
					AgentObject agent = (AgentObject) ec.getMainPlayground().getObjects().get(sender);
					agent.setPosX(Integer.parseInt(pos[0]));
					agent.setPosY(Integer.parseInt(pos[1]));					
				}
				
			}else{
				block();
			}
			
		}
		
	}
	
}
