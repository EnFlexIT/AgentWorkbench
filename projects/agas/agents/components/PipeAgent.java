package agas.agents.components;

import static agas.agents.components.ConversationId.PIPE_DATA;
import gasmas.ontology.Pipe;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The agent class for an {@link Pipe}.
 * 
 * @author Oleksandr Turchyn - University of Duisburg - Essen
 */
public class PipeAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = 7003278414741221062L;

	private static Logger logger = Logger.getLogger(PipeAgent.class.getName());

	@Override
	protected void setup() {
		super.setup();

		this.registerDFService(ServiceType.PIPE.name(), getLocalName(), null);

		logger.log(Level.INFO, String.format("PipeAgent %s started.", getLocalName()));
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

		private static final long serialVersionUID = -1986080122790636880L;

		private boolean isSent = false;

		@Override
		public void action() {

			logger.log(Level.INFO, "PipeAgent : SendPipeDataBehaviour : started ");

			MessageTemplate msgTemplate =
					MessageTemplate.MatchConversationId(PIPE_DATA.name());

			ACLMessage request = myAgent.receive(msgTemplate);

			if (request != null) {
				sendPipeData(request);

			} else {
				logger.log(Level.INFO, "PipeAgent : SendPipeDataBehaviour : wating for msg ");
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

			logger.log(Level.INFO,
					String.format("PipeAgent %s : pipe-data %s sent to %s.",
							getLocalName(), pipe, request.getSender().getLocalName()));
		}
	}

	private Pipe getPipe() {

		Pipe pipe = null;

		Object[] dataModel = (Object[]) myNetworkComponent.getDataModel();

		if (dataModel != null) {
			for (Object object : dataModel) {
				if (object instanceof Pipe) {
					pipe = (Pipe) object;
				}
			}
		}

		if (pipe == null) {
			logger.log(Level.WARNING, String.format("PipeAgent %s : pipe data not defined .", getLocalName()));
		}

		return pipe;
	}

}
