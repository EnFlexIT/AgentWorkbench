package de.enflexit.awb.samples.example02;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

public class Piong extends Agent {

	private static final long serialVersionUID = 1276178649247011952L;

	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		
		if (this.getLocalName().equalsIgnoreCase("Ping")) {
			this.sendPingPongMessage("Serve Message");
		}
		this.addBehaviour(new MessageReceiveBehavour());
		
	}
	
	/**
	 * Send message.
	 * @param message the message
	 */
	private void sendPingPongMessage(String messageContent) {
		
		// --- Specify receiver ---------------------------
		String receiverLocalName;
		if (this.getLocalName().equalsIgnoreCase("Ping")) {
			receiverLocalName = "Pong";
		} else {
			receiverLocalName = "Ping";
		}
		AID receiverAID = new AID(receiverLocalName, AID.ISLOCALNAME);

		// --- Create simple message ----------------------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.getAID());
		msg.addReceiver(receiverAID);
		msg.setContent(messageContent);
		this.send(msg);
		
	}
	
	/**
	 * The Class MessageReceiveBehavour.
	 */
	private class MessageReceiveBehavour extends CyclicBehaviour {
		private static final long serialVersionUID = 254907738451044781L;
		@Override
		public void action() {
			
			ACLMessage msg = myAgent.receive();
			if (msg!=null) {
				// --- work on the delivered message --------
				System.out.println("Message received from " + msg.getSender().getLocalName() + ": " + msg.getContent());
				this.myAgent.addBehaviour( new DelaySender(this.myAgent,  1000*1) );
				
			} else {
				// --- wait for the next incoming message ---
				block();
			}
		}
	}
	
	/**
	 * The Class DelaySender.
	 */
	private class DelaySender extends WakerBehaviour  {

		private static final long serialVersionUID = -6801922819382741241L;
		
		public DelaySender(Agent agent, long timeout) {
			super(agent, timeout);
		}

		@Override
		protected void onWake() {
			if (this.myAgent.getLocalName().equalsIgnoreCase("Ping")) {
				sendPingPongMessage("Serve Message");
			} else {
				sendPingPongMessage("Return Message");
			}
			this.stop();
		}
	}
	
}
