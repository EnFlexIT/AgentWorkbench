package mas.agents;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


/**
   This example shows an agent that registers to receive messages about a given topic
   @author Giovanni Caire - Telecom Italia
 */
public class TopicMessageReceiverAgent extends Agent {
	
	protected void setup() {
		try {
			// Register to messages about topic "JADE"
			TopicManagementHelper topicHelper = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
			final AID topic = topicHelper.createTopic("JADE");
			topicHelper.register(topic);
			
			// Add a behaviour collecting messages about topic "JADE"
			addBehaviour(new CyclicBehaviour(this) {
				public void action() {
					ACLMessage msg = myAgent.receive(MessageTemplate.MatchTopic(topic));
					if (msg != null) {
						System.out.println("Agent "+myAgent.getLocalName()+": Message about topic "+topic.getLocalName()+" received. Content is "+msg.getContent());
					}
					else {
						block();
					}
				}
			} );
		}
		catch (Exception e) {
			System.err.println("Agent "+getLocalName()+": ERROR registering to topic \"JADE\"");
			e.printStackTrace();
		}
	}
	
//	@Override
//	protected void afterMove() {
//		// TODO Auto-generated method stub
//		super.afterMove();
//		try {
//		TopicManagementHelper topicHelper = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
//		final AID topic = topicHelper.createTopic("JADE");
//		
//			topicHelper.register(topic);
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			
//		
//	}
}