package mas.display;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

class MovingBehaviour extends TickerBehaviour{
	PhysicalAgent agent;

	public MovingBehaviour(PhysicalAgent agent) {
		super(agent, 50);
		this.agent=agent;
	}

	@Override
	protected void onTick() {
		agent.posX+=agent.speedX;
		agent.posY+=agent.speedY;
		
		if(agent.displayAgents!=null){
			ACLMessage movingMsg=new ACLMessage(ACLMessage.REQUEST);
			for(int i=0;i<agent.displayAgents.length;i++)
				movingMsg.addReceiver(agent.displayAgents[i].getName());
			movingMsg.setConversationId("move");
			movingMsg.setContent(agent.posX+","+agent.posY);
			agent.send(movingMsg);
		}
	}
	
}
