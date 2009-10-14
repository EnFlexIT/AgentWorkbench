package mas.display;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

class MovingBehaviour extends TickerBehaviour{
	MovingAgent agent;

	public MovingBehaviour(MovingAgent agent) {
		super(agent, 50);
		this.agent=agent;
	}

	@Override
	protected void onTick() {
		agent.xPos+=agent.xSpeed;
		agent.yPos+=agent.ySpeed;
		ACLMessage movingMsg=new ACLMessage(ACLMessage.REQUEST);
		movingMsg.addReceiver(agent.displayAgent);
		movingMsg.setConversationId("move");
		movingMsg.setContent(agent.xPos+","+agent.yPos);
		agent.send(movingMsg);
	}
	
}
