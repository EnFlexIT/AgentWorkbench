package de.enflexit.awb.samples.example02;

import de.enflexit.awb.samples.example03.MyFirstOntology;
import de.enflexit.awb.samples.example03.PingPongMessage;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

public class Piong_Ontology extends Agent {

	private static final long serialVersionUID = 1276178649247011952L;

	// --- For the message exchange by using the Ontology ----
	private Ontology ontology = MyFirstOntology.getInstance();
	//private Codec codec = new SLCodec();
	private Codec codec = new SLCodec();
	
	private int pingPongCounter = 0;
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		
		// --- For the message exchange by using the Ontology ----
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
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
		// --- For the message exchange by using the Ontology ----
		msg.setOntology(this.ontology.getName());
		msg.setLanguage(this.codec.getName());
		
		// --- Create ontology content for the message ----
		this.pingPongCounter++;
		PingPongMessage ppm = new PingPongMessage();
		ppm.setPingPongCounter(this.pingPongCounter);
		ppm.setMsgText(messageContent);
		ppm.setRequiresAction(false);
		
		// --- Set the agent action -----------------------
		Action act = new Action();
		act.setActor(this.getAID());
		act.setAction(ppm);
		
		// --- Send Ontology Message ----------------------
		try {
			getContentManager().fillContent(msg, act);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
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
				Action action = null;
				try {
					// --- Receive Ontology Message --------- 
					action = (Action) getContentManager().extractContent(msg);
					
				} catch (UngroundedException e) {
					e.printStackTrace();
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
				
				if (action!=null && action.getAction() instanceof PingPongMessage) {
					PingPongMessage ppm = (PingPongMessage) action.getAction();
					pingPongCounter = ppm.getPingPongCounter();
					System.out.println("Message received from " + msg.getSender().getLocalName() + ": Content = '" + ppm.getMsgText() + "' - Counter = " + ppm.getPingPongCounter());
					
				} else {
					System.out.println("Message received from " + msg.getSender().getLocalName() + ": " + msg.getContent());
				}
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
