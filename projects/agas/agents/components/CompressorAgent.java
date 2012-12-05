package agas.agents.components;

import static agas.agents.components.ConversationId.COMPRESSOR_DATA;
import gasmas.ontology.Compressor;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The agent class for an {@link Compressor}.
 * 
 * @author Oleksandr Turchyn - University of Duisburg - Essen
 */

public class CompressorAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = 2792727750579482552L;

	private static Logger logger = Logger.getLogger(CompressorAgent.class.getName());

	@Override
	protected void setup() {
		super.setup();
		this.registerDFService(ServiceType.COMPRESSOR.name(), getLocalName(), null);

		logger.log(Level.INFO, String.format("CompressorAgent %s started.", getLocalName()));
	}

	@Override
	protected void takeDown() {
		super.takeDown();

		deregisterDFService();
	}

	protected void onEnvironmentStimulus() {
		super.onEnvironmentStimulus();

		addBehaviour(new SendCompressorDataBehaviour());
		addBehaviour(new CompressorBehaviour());
	}

	private class SendCompressorDataBehaviour extends Behaviour {

		private static final long serialVersionUID = 2117085076362031195L;

		private boolean isSent = false;

		@Override
		public void action() {

			MessageTemplate msgTemplate =
					MessageTemplate.MatchConversationId(COMPRESSOR_DATA.name());

			ACLMessage request = receive(msgTemplate);

			if (request != null) {
				sendCompressorData(request);

			} else {
				logger.log(Level.INFO, "CompressorAgent : SendCompressorDataBehaviour : wating for msg ");
				block();
			}
		}

		@Override
		public boolean done() {
			return isSent;
		}

		private void sendCompressorData(ACLMessage request) {

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

			msg.addReceiver(request.getSender());
			msg.setConversationId(COMPRESSOR_DATA.name());

			Compressor compressor = getCompressor();

			try {
				msg.setContentObject(compressor);

			} catch (IOException e) {
				e.printStackTrace();
			}

			send(msg);

			logger.log(Level.INFO,
					String.format("CompressorAgent %s : Compressor-data %s sent to %s.",
							getLocalName(), compressor, request.getSender().getLocalName()));
		}
	}

	private class CompressorBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = 2615824169476626875L;

		@Override
		public void action() {
			MessageTemplate msgTemplate =
					MessageTemplate.MatchConversationId(ConversationId.COMPRESSOR_CHANGE_PRESSURE.name());

			ACLMessage request = receive(msgTemplate);

			if (request != null) {

				float targetPressure = Float.parseFloat(request.getContent());

				logger.log(Level.INFO,
						String.format("CompressorAgent %s : request for change pressure recieved : target pressure %s", getLocalName(), targetPressure));

			} else {
				logger.log(Level.INFO, "CompressorAgent : CompressorBehaviour : wating for msg ");
				block();
			}
		}
	}

	private Compressor getCompressor() {

		Compressor compressor = null;

		Object[] dataModel = (Object[]) myNetworkComponent.getDataModel();

		if (dataModel != null) {
			for (Object object : dataModel) {
				if (object instanceof Compressor) {
					compressor = (Compressor) object;
				}
			}
		}

		if (compressor == null) {
			logger.log(Level.WARNING, String.format("CompressorAgent %s : compressor data not defined", getLocalName()));
		}

		return compressor;
	}

}
