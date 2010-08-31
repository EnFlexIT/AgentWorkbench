package game_of_life.LBC;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class StaticLoadBalancingCoodinator extends Agent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		
		addBehaviour(new receiverLoadOfContainer(this));
	}
	
class receiverLoadOfContainer extends CyclicBehaviour {
		
	private static final long serialVersionUID = 1L;
		TopicManagementHelper topicHelper;
		AID currentLoadOfContainer;
		
		public receiverLoadOfContainer(Agent agent) {
			
			super(agent);
		}

		public void action() {
			
			 
			try {
				
				// ------ Register to messages about topic "currentLoadOfContainer" -------
				topicHelper = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
				currentLoadOfContainer = topicHelper.createTopic("currentLoadOfContainer");
				topicHelper.register(currentLoadOfContainer);
				
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchTopic(currentLoadOfContainer));

				if (msg != null) {

					String AgentName = msg.getSender().getLocalName();
					String content = msg.getContent();
					System.out.println(" Sender: "+AgentName+" Receiver: "+getLocalName()+" Content: "+content);
				}

			} catch (Exception e) {

			}
		}
	}
}
