package mas.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TopicTestAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setup(){
		TopicManagementHelper tmh;
		try {
			tmh = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
			AID positionTopic = tmh.createTopic("position");
			tmh.register(positionTopic);
			addBehaviour(new ReceiveBehaviour(MessageTemplate.MatchTopic(positionTopic)));
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class ReceiveBehaviour extends CyclicBehaviour{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		MessageTemplate posTemp;
		
		public ReceiveBehaviour(MessageTemplate mt){
			this.posTemp = mt;
		}

		@Override
		public void action() {
			ACLMessage m = receive(posTemp);
			if(m != null){
				
			}else{
				block();
			}
		}
		
		
	}
}
