package agas.agents.components;

import static agas.agents.components.ConversationId.PIPE_DATA;
import gasmas.ontology.Pipe;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

public class PipeAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = 7003278414741221062L;

	@Override
	protected void setup() {
		super.setup();

		System.out.println(String.format("PipeAgent %s started", getAID().getName()));
	
		this.registerDFService(DFAgentDescriptionID.PIPE.name(), getLocalName(), null);
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
		deregisterDFService();
	}

	@Override
	protected void onEnvironmentStimulus() {
		super.onEnvironmentStimulus();

		addBehaviour(new SendPipeDataBehaviour());
	}

	private class SendPipeDataBehaviour extends Behaviour {

		private boolean isSent = false;
		
		@Override
		public void action() {
			
			System.out.println("PipeAgent : SendPipeDataBehaviour : started ");

			MessageTemplate msgTemplate = 
				MessageTemplate.MatchConversationId(PIPE_DATA.name());
			
			ACLMessage request = myAgent.receive(msgTemplate);

			if (request != null) {
				sendPipeData(request);
				
			} else {
				System.out.println("PipeAgent : SendPipeDataBehaviour : wating for msg ");
				block();
			}
		}

		@Override
		public boolean done() {
			return isSent;
		}
		
		private void sendPipeData(ACLMessage request) {

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			
			msg.addReceiver(request.getSender());
			msg.setConversationId(PIPE_DATA.name());
			
			Pipe pipe = getPipe();
			
			try {
				msg.setContentObject(pipe);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			send(msg);

			System.out.println(
					String.format("PipeAgent %s : pipe-data %s sent to %s.", 
							getLocalName(), pipe, request.getSender().getLocalName()));
		}
	}


	private Pipe getPipe() {

		Object[] dataModel = (Object[]) myNetworkComponent.getDataModel();

		for (Object object : dataModel) {
			if (object instanceof Pipe) {
				return (Pipe) object;
			}
		}

		throw new IllegalArgumentException("Pipe is not defined");
	}

}
