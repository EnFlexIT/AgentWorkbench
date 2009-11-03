package mas.display;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

class CollisionBehaviour extends CyclicBehaviour{
	PhysicalAgent agent;
	
	public CollisionBehaviour(PhysicalAgent agent){
		this.agent=agent;
	}
	@Override
	public void action() {
		MessageTemplate mt=MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchConversationId("collision"));
		ACLMessage collisionMsg=agent.receive(mt);
		if(collisionMsg!=null){
			String[] colls=collisionMsg.getContent().split(",");
			if(colls[0].contains("true"))
				agent.speedX=-agent.speedX;
			if(colls[1].contains("true"))
				agent.speedY=-agent.speedY;
		}else{
			block();
		}
		
	}
	
}
