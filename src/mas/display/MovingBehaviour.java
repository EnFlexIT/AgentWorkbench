package mas.display;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;
import jade.core.event.NotificationService;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;

/**
 * Example class for moving agents
 * Changing position and informing agents via TopicManagementHelper interface 
 * @author Nils
 *
 */
public class MovingBehaviour extends TickerBehaviour {
	
	
	TopicManagementHelper tmh = null;
	AID positionTopic = null;
	
	
	int xSpeed = 5;
	int ySpeed = 2;
	
	public MovingBehaviour(Agent a, long period) {
		super(a, period);
		try {
			tmh = (TopicManagementHelper) a.getHelper(TopicManagementHelper.SERVICE_NAME);
			
			positionTopic = tmh.createTopic("position");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
		ACLMessage posUpdate = new ACLMessage(ACLMessage.INFORM);
		DisplayableAgent da = (DisplayableAgent) myAgent;
		int xPos = da.getX();
		int yPos = da.getY();
		xPos += xSpeed;
		yPos += ySpeed;
		da.setPos(xPos, yPos);
		if( xPos<=0 || xPos+da.getWidth()>=da.getPlaygroundWidth())
			xSpeed = -xSpeed;
		if( yPos<=0 || yPos+da.getHeight()>=da.getPlaygroundHeight())
			ySpeed = -ySpeed;
		posUpdate.addReceiver(positionTopic);
		posUpdate.setContent(xPos+","+yPos);		
		myAgent.send(posUpdate);
	}
	
	
	
	public int onEnd(){
		ACLMessage bye = new ACLMessage(ACLMessage.INFORM);
		bye.addReceiver(positionTopic);
		bye.setContent("bye");
		myAgent.send(bye);
		return 0;
	}

}
