package agas.agents.components;

import static agas.agents.components.ConversationId.COMPRESSOR_CHANGE_PRESSURE;
import static agas.agents.components.ConversationId.COMPRESSOR_DATA;
import static agas.agents.components.ConversationId.ENTRY_PROPOSAL;
import static agas.agents.components.ConversationId.EXIT_PROPOSAL;
import static agas.agents.components.ConversationId.PIPE_DATA;
import static agas.agents.components.ServiceType.COMPRESSOR;
import static agas.agents.components.ServiceType.CONTROLLER;
import static agas.agents.components.ServiceType.PIPE;
import gasmas.ontology.Compressor;
import gasmas.ontology.Pipe;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import agentgui.simulationService.agents.SimulationAgent;

/**
 * The ControllerAgent class controls and coordinates the behaviour of network
 * components.
 * 
 * @author Oleksandr Turchyn - University of Duisburg - Essen
 */
public class ControllerAgent extends SimulationAgent {

	private static final long serialVersionUID = 2040238832853890724L;

	private static Logger logger = Logger.getLogger(ControllerAgent.class.getName());

	private NetworkCluster networkCluster;

	private List<AID> pipeAgentNames = new ArrayList<AID>();
	private List<AID> compressorAgentNames = new ArrayList<AID>();

	@Override
	protected void setup() {
		super.setup();

		this.registerDFService(CONTROLLER.name(), getLocalName(), null);

		addBehaviour(new StartControllerBehavior(this, 500));

		networkCluster = new NetworkCluster();

		logger.log(Level.INFO, String.format("ControllerAgent %s started.", getLocalName()));
	}

	@Override
	protected void takeDown() {
		super.takeDown();

		deregisterDFService();
	}

	private class StartControllerBehavior extends WakerBehaviour {

		private static final long serialVersionUID = -6864364939035635998L;

		public StartControllerBehavior(Agent agent, long timeout) {
			super(agent, timeout);
		}

		@Override
		protected void onWake() {
			myAgent.addBehaviour(new ControllerBehaviour());
		}
	}

	private class ControllerBehaviour extends FSMBehaviour {

		private static final long serialVersionUID = -6172610484458567236L;

		public ControllerBehaviour() {

			registerFirstState(new FindNetworkComponentsBehavior(), "FindNetworkComponents");
			registerState(new CollectDataBehavior(), "CollectData");
			registerState(new ProcessEntryProposalBehavior(), "ProcessEntryProposal");
			registerState(new ProcessExitProposalBehavior(), "ProcessExitProposal");

			registerDefaultTransition("FindNetworkComponents", "CollectData");
			registerDefaultTransition("CollectData", "ProcessEntryProposal");
			registerDefaultTransition("ProcessEntryProposal", "ProcessExitProposal");
			registerDefaultTransition("ProcessExitProposal", "ProcessEntryProposal");
		}
	}

	private class FindNetworkComponentsBehavior extends OneShotBehaviour {

		private static final long serialVersionUID = 2079405765535308334L;

		@Override
		public void action() {
			findPipes();
			findCompressors();
		}

		private void findPipes() {

			DFAgentDescription[] pipeAgentDescriptions = findAgentsByServiceType(PIPE.name());

			for (DFAgentDescription pipeAgentDescription : pipeAgentDescriptions) {
				AID pipeAgentName = pipeAgentDescription.getName();
				pipeAgentNames.add(pipeAgentName);

				logger.log(Level.INFO, String.format("PipeAgent %s found", pipeAgentName.getLocalName()));
			}
		}

		private void findCompressors() {

			DFAgentDescription[] compressorAgentDescriptions = findAgentsByServiceType(COMPRESSOR.name());

			for (DFAgentDescription compressorAgentDescription : compressorAgentDescriptions) {
				AID compressorAgentName = compressorAgentDescription.getName();
				compressorAgentNames.add(compressorAgentName);

				logger.log(Level.INFO, String.format("CompressorAgent %s found", compressorAgentName.getLocalName()));
			}
		}
	}

	private class CollectDataBehavior extends SequentialBehaviour {

		private static final long serialVersionUID = -3221400514290392523L;

		public CollectDataBehavior() {
			addSubBehaviour(new CollectPipeDataBehavior());
			addSubBehaviour(new CollectCompressorDataBehavior());
		}
	}

	private class CollectPipeDataBehavior extends Behaviour {

		private static final long serialVersionUID = 8528698924480101089L;

		private int repliesCount = 0;
		private int step = 1;

		@Override
		public void action() {

			switch (step) {
			case 1:
				sendPipeDataRequest();
				step = 2;
				break;

			case 2:
				receivePipeData();
				break;
			}
		}

		@Override
		public boolean done() {
			return repliesCount == pipeAgentNames.size();
		}

		private void sendPipeDataRequest() {

			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);

			for (AID pipeAgentName : pipeAgentNames) {
				request.addReceiver(pipeAgentName);
			}

			request.setConversationId(PIPE_DATA.name());

			send(request);

			logger.log(Level.INFO, "ControllerAgent : request for pipe data sended");
		}

		private void receivePipeData() {

			MessageTemplate msgTemplate =
					MessageTemplate.MatchConversationId(PIPE_DATA.name());

			ACLMessage msg = myAgent.receive(msgTemplate);

			if (msg != null) {

				try {
					Pipe pipe = (Pipe) msg.getContentObject();
					repliesCount++;

					logger.log(
							Level.INFO,
							String.format(
									"ControllerAgent %s : pipe data from  %s recieved : %s",
									myAgent.getLocalName(),
									msg.getSender().getLocalName(),
									pipe));

					networkCluster.addPipeData(pipe);

				} catch (UnreadableException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}

			} else {
				block();
				logger.log(Level.INFO, "ControllerAgent : CollectPipeDataBehavior : wating for message");
			}
		}
	}

	private class CollectCompressorDataBehavior extends Behaviour {

		private static final long serialVersionUID = 8746869153084580026L;

		private int repliesCount = 0;
		private int step = 1;

		@Override
		public void action() {

			switch (step) {
			case 1:
				sendCompressorDataRequest();
				step = 2;
				break;

			case 2:
				receiveCompressorData();
				break;
			}
		}

		private void sendCompressorDataRequest() {

			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);

			for (AID compressorAgentName : compressorAgentNames) {
				request.addReceiver(compressorAgentName);
			}

			request.setConversationId(COMPRESSOR_DATA.name());

			send(request);

			logger.log(Level.INFO, "ControllerAgent : request for compressor data sended");
		}

		@Override
		public boolean done() {
			return repliesCount == compressorAgentNames.size();
		}

		private void receiveCompressorData() {

			MessageTemplate msgTemplate =
					MessageTemplate.MatchConversationId(COMPRESSOR_DATA.name());

			ACLMessage msg = myAgent.receive(msgTemplate);

			if (msg != null) {
				try {
					Compressor compressor = (Compressor) msg.getContentObject();
					repliesCount++;

					logger.log(
							Level.INFO,
							String.format(
									"ControllerAgent %s : compressor data from  %s recieved : %s",
									getLocalName(),
									msg.getSender().getLocalName(),
									compressor));

					networkCluster.addCompressorData(compressor);

				} catch (UnreadableException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}

			} else {
				block();
				logger.log(Level.INFO, "ControllerAgent : CollectCompressorDataBehavior : wating for message");
			}
		}
	}

	public class ProcessEntryProposalBehavior extends OneShotBehaviour {

		private static final long serialVersionUID = -6438505932833056215L;

		@Override
		public void action() {

			MessageTemplate msgTemplate =
					MessageTemplate.MatchConversationId(ENTRY_PROPOSAL.name());

			ACLMessage msg = receive(msgTemplate);

			if (msg != null) {

				float proposalIncomingGasVolume = Float.parseFloat(msg.getContent());

				logger.log(Level.INFO,
						String.format("ControllerAgent %s : proposal incoming gas volume: %s", getLocalName(), proposalIncomingGasVolume));

				float acceptedIncomingGasVolume =
						networkCluster.calculateAcceptedIncomingGasVolume(proposalIncomingGasVolume);

				if (acceptedIncomingGasVolume > 0) {

					sendAcceptProposalMessage(msg.getSender(), acceptedIncomingGasVolume, ENTRY_PROPOSAL.name());

					networkCluster.addIncomingGasVolume(acceptedIncomingGasVolume);

				} else {
					sendRejectProposalMessage(msg.getSender(), ENTRY_PROPOSAL.name());
				}
			}
		}
	}

	public class ProcessExitProposalBehavior extends OneShotBehaviour {

		private static final long serialVersionUID = 6094971177585657573L;

		@Override
		public void action() {

			MessageTemplate msgTemplate =
					MessageTemplate.MatchConversationId(EXIT_PROPOSAL.name());

			ACLMessage msg = myAgent.receive(msgTemplate);

			if (msg != null) {

				float proposalOutgoingGasVolume = Float.parseFloat(msg.getContent());

				logger.log(Level.INFO,
						String.format("ControllerAgent %s : proposal outgoing gas volume : %s", getLocalName(), proposalOutgoingGasVolume));

				float acceptedOutgoingGasVolume =
						networkCluster.calculateAcceptedOutgoingGasVolume(proposalOutgoingGasVolume);

				if (acceptedOutgoingGasVolume > 0) {

					sendAcceptProposalMessage(msg.getSender(), acceptedOutgoingGasVolume, EXIT_PROPOSAL.name());

					networkCluster.removeOutgoingGasVolume(acceptedOutgoingGasVolume);

				} else {
					sendRejectProposalMessage(msg.getSender(), EXIT_PROPOSAL.name());
				}
			}
		}
	}

	private void sendAcceptProposalMessage(AID receiver, float acceptedGasVolume, String conversationId) {

		ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		msg.addReceiver(receiver);
		msg.setConversationId(conversationId);
		msg.setContent(Float.toString(acceptedGasVolume));
		send(msg);

		logger.log(Level.INFO, String.format("ControllerAgent %s : accept gas volume : %s", getLocalName(), acceptedGasVolume));
	}

	private void sendRejectProposalMessage(AID receiver, String conversationId) {

		ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
		msg.addReceiver(receiver);
		msg.setConversationId(conversationId);
		send(msg);

		logger.log(Level.INFO, String.format("ControllerAgent %s : reject proposal", getLocalName()));
	}

	private void sendRequestForChangePressure(float targetPressure) {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);

		for (AID compressorAgentName : compressorAgentNames) {
			request.addReceiver(compressorAgentName);
		}

		request.setConversationId(COMPRESSOR_CHANGE_PRESSURE.name());
		request.setContent(Float.toString(targetPressure));

		send(request);

		logger.log(Level.INFO, String.format("ControllerAgent : request for change pressure send to compressor(s) : targetPressure %s", targetPressure));
	}

	private class NetworkCluster {

		private static final float ENTRY_PRESSURE = 75.0f; // barg
		private static final float EXIT_PRESSURE = 75.0f; // barg

		private float networkPipeVolume; // m_cube
		private float networkPipePressureMax; // barg
		private float networkPipePressureCurrent; // barg

		private float networkCompressorPressureInMin; // barg
		private float networkCompressorPressureOutMax; // barg

		protected float calculateAcceptedIncomingGasVolume(float proposalIncomingGasVolume) {

			float forecastNetworkPressure = calculatePressureWithIncomingGasVolume(proposalIncomingGasVolume);

			if (forecastNetworkPressure > networkPipePressureMax) {
				return calculatePossibleIncomingGasVolume();
			}

			return proposalIncomingGasVolume;
		}

		public float calculateAcceptedOutgoingGasVolume(float proposalOutgoingGasVolume) {

			if (shouldOutgoingPressureBeCorrected()) {

				if (canOutgoingPressureBeCorrected()) {
					sendRequestForChangePressure(EXIT_PRESSURE);

				} else {
					return 0;
				}
			}

			float possibleOutgoingGasVolume = calculatePossibleOutomingGasVolume();

			if (possibleOutgoingGasVolume > proposalOutgoingGasVolume) {
				return proposalOutgoingGasVolume;

			} else {
				return possibleOutgoingGasVolume;
			}
		}

		private boolean shouldOutgoingPressureBeCorrected() {
			return networkPipePressureCurrent < EXIT_PRESSURE;
		}

		private boolean canOutgoingPressureBeCorrected() {
			return networkPipePressureCurrent >= networkCompressorPressureInMin
					&& networkCompressorPressureOutMax >= EXIT_PRESSURE;
		}

		private float calculatePossibleIncomingGasVolume() {
			return networkPipeVolume * (networkPipePressureMax - networkPipePressureCurrent) / ENTRY_PRESSURE;
		}

		private float calculatePossibleOutomingGasVolume() {
			return networkPipeVolume * networkPipePressureCurrent / EXIT_PRESSURE;
		}

		protected void addIncomingGasVolume(float incomingGasVolume) {

			networkPipePressureCurrent = calculatePressureWithIncomingGasVolume(incomingGasVolume);

			logCurrentPressure();
		}

		public void removeOutgoingGasVolume(float outgoingGasVolume) {

			networkPipePressureCurrent = calculatePressureWithOutgoingGasVolume(outgoingGasVolume);

			logCurrentPressure();
		}

		private float calculatePressureWithIncomingGasVolume(float incomingGasVolume) {
			return (incomingGasVolume * ENTRY_PRESSURE + networkPipeVolume * networkPipePressureCurrent)
					/ networkPipeVolume;
		}

		private float calculatePressureWithOutgoingGasVolume(float outgoingGasVolume) {
			return (networkPipeVolume * networkPipePressureCurrent - outgoingGasVolume * EXIT_PRESSURE)
					/ networkPipeVolume;
		}

		private void logCurrentPressure() {
			logger.log(Level.INFO, String.format("ControllerAgent %s : current pressure : %s", getLocalName(), networkPipePressureCurrent));
		}

		protected void addPipeData(Pipe pipe) {

			if (pipe != null) {
				addPipePressureMax(pipe);
				addPipeVolume(pipe);
			} else {
				logger.log(Level.WARNING, "Pipe data not definded");
			}
		}

		private void addPipePressureMax(Pipe pipe) {

			float pipePressureMax = pipe.getPressureMax().getValue();

			logger.log(Level.INFO, String.format("Pipe PressureMax : %s", pipePressureMax));

			if (networkPipePressureMax == 0.0f || networkPipePressureMax > pipePressureMax) {
				networkPipePressureMax = pipePressureMax;
			}

			logger.log(Level.INFO, String.format("NetworkCluster PipePressureMax : %s", networkPipePressureMax));
		}

		private void addPipeVolume(Pipe pipe) {

			float pipeVolume = calculatePipeVolume(pipe);

			logger.log(Level.INFO, String.format("Pipe Volume : %s", pipeVolume));

			networkPipeVolume += pipeVolume;

			logger.log(Level.INFO, String.format("NetworkCluster Volume : %s", networkPipeVolume));
		}

		private float calculatePipeVolume(Pipe pipe) {

			float pipeLength = pipe.getLength().getValue() * 1000; // km->m
			float pipeDiameter = pipe.getDiameter().getValue() / 1000; // mm->m

			float pipeRadius = pipeDiameter / 2; // m

			double pipeSquare = Math.pow(pipeRadius, 2) * Math.PI; // m_squared

			double pipeVolume = pipeSquare * pipeLength; // m_cube

			return (float) pipeVolume;
		}

		protected void addCompressorData(Compressor compressor) {

			if (compressor != null) {
				addCompressorPressureOutMax(compressor);
				addCompressorPressureInMin(compressor);

			} else {
				logger.log(Level.WARNING, "Compressor data not definded");
			}
		}

		private void addCompressorPressureOutMax(Compressor compressor) {
			float compressorPressureOutMax = compressor.getPressureOutMax().getValue();

			logger.log(Level.INFO, String.format("Compressor PressureOutMax : %s", compressorPressureOutMax));

			if (networkCompressorPressureOutMax == 0.0f || networkCompressorPressureOutMax > compressorPressureOutMax) {
				networkCompressorPressureOutMax = compressorPressureOutMax;
			}

			logger.log(Level.INFO, String.format("NetworkCluster CompressorPressureOutMax : %s", networkCompressorPressureOutMax));
		}

		private void addCompressorPressureInMin(Compressor compressor) {
			float compressorPressureInMin = compressor.getPressureInMin().getValue();

			logger.log(Level.INFO, String.format("Compressor PressureInMin : %s", compressorPressureInMin));

			if (networkCompressorPressureInMin == 0.0f || networkCompressorPressureInMin < compressorPressureInMin) {
				networkCompressorPressureInMin = compressorPressureInMin;
			}

			logger.log(Level.INFO, String.format("NetworkCluster CompressorPressureInMin : %s", networkCompressorPressureInMin));
		}
	}
}
