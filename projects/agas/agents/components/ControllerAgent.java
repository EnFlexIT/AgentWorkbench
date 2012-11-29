package agas.agents.components;

import static agas.agents.components.ConversationId.ENTRY_PROPOSAL;
import static agas.agents.components.ConversationId.EXIT_PROPOSAL;
import static agas.agents.components.ConversationId.PIPE_DATA;
import static agas.agents.components.DFAgentDescriptionID.CONTROLLER;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agentgui.simulationService.agents.SimulationAgent;

public class ControllerAgent extends SimulationAgent {

	private static final long serialVersionUID = 2040238832853890724L;

	private Map<AID, Pipe> pipes = new HashMap<AID, Pipe>();
	private List<AID> pipeAgentNames = new ArrayList<AID>();
	private List<AID> compressorAgentNames = new ArrayList<AID>();
	private float currentGasAmount;

	@Override
	protected void setup() {
		super.setup();

		this.registerDFService(CONTROLLER.name(), getLocalName(), null);

		addBehaviour(new StartControllerBehavior(this, 500));
	}

	@Override
	protected void takeDown() {
		super.takeDown();

		deregisterDFService();
	}

	private class StartControllerBehavior extends WakerBehaviour {

		public StartControllerBehavior(Agent agent, long timeout) {
			super(agent, timeout);
		}

		@Override
		protected void onWake() {
			myAgent.addBehaviour(new ControllerBehaviour(myAgent));
		}
	}

	private class ControllerBehaviour extends FSMBehaviour {

		public ControllerBehaviour(Agent agent) {
			super(agent);

			registerFirstState(new FindNetworkComponentsBehavior(agent), "FindNetworkComponents");
			registerState(new CollectDataBehavior(agent), "CollectData");
			registerState(new ProcessEntryProposalBehavior(), "ProcessEntryProposal");
			registerState(new ProcessExitProposalBehavior(), "ProcessExitProposal");

			registerDefaultTransition("FindNetworkComponents", "CollectData");
			registerDefaultTransition("CollectData", "ProcessEntryProposal");
			registerDefaultTransition("ProcessEntryProposal", "ProcessExitProposal");
			registerDefaultTransition("ProcessExitProposal", "ProcessEntryProposal");
		}
	}

	private class FindNetworkComponentsBehavior extends OneShotBehaviour {

		public FindNetworkComponentsBehavior(Agent agent) {
			super(agent);
		}

		@Override
		public void action() {
			System.out.println("ControllerAgent : FindNetworkComponentsBehavior : started");

			findPipes();
			findCompressors();
			System.out.println("ControllerAgent : FindNetworkComponentsBehavior : ended");
		}

		private void findPipes() {
		//Null Überprüfung hinzufügen
			DFAgentDescription[] pipeAgentDescriptions = findAgentsByServiceType(DFAgentDescriptionID.PIPE.name());
			for (DFAgentDescription pipeAgentDescription : pipeAgentDescriptions) {
				AID pipeName = pipeAgentDescription.getName();
				pipeAgentNames.add(pipeName);
				System.out.println(String.format("pipe %s found", pipeName.getLocalName()));
			}

		}
		private void findCompressors() {

			DFAgentDescription[] compressorAgentDescriptions = findAgentsByServiceType(DFAgentDescriptionID.COMPRESSOR.name());
			for (DFAgentDescription compressorAgentDescription : compressorAgentDescriptions) {
				AID compressorName = compressorAgentDescription.getName();
				compressorAgentNames.add(compressorName);
				System.out.println(String.format("compressor %s found", compressorName.getLocalName()));
			}

		}
	}

	private class CollectDataBehavior extends SequentialBehaviour {

		public CollectDataBehavior(Agent agent) {
			super(agent);
			
			addSubBehaviour(new CollectPipeDataBehavior(agent));
			//addSubBehaviour(new CollectCompressorDataBehavior(agent));
		}
	}
	
	private class CollectPipeDataBehavior extends Behaviour {

		private int repliesCount = 0;
		private int step = 1;

		public CollectPipeDataBehavior(Agent agent) {
			super(agent);
		}

		@Override
		public void action() {

			System.out.println("ControllerAgent : CollectPipeDataBehavior : started : step : " + step);

			switch (step) {
			case 1:
				sendRequest();
				step = 2;
				break;

			case 2:
				receivePipeData();
				System.out.println("ControllerAgent : CollectPipeDataBehavior : ended");
				break;
			}
		}

		@Override
		public boolean done() {
			return repliesCount == pipeAgentNames.size();
		}

		private void sendRequest() {

			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);

			for (AID pipeAgentName : pipeAgentNames) {
				request.addReceiver(pipeAgentName);
			}

			request.setConversationId(PIPE_DATA.name());

			send(request);

			System.out.println("ControllerAgent: request pipe-data sended");

		}

		private void receivePipeData() {

			MessageTemplate msgTemplate =
					MessageTemplate.MatchConversationId(PIPE_DATA.name());

			ACLMessage msg = myAgent.receive(msgTemplate);

			if (msg != null) {
				System.out.println("ControllerAgent : CollectPipeDataBehavior : msg  received");

				try {
					Pipe pipe = (Pipe) msg.getContentObject();
					repliesCount++;

					System.out.println(
							String.format(
									"ControllerAgent %s : CollectPipeDataBehavior : pipe data from  %s recieved : %s",
									myAgent.getLocalName(),
									msg.getSender().getLocalName(),
									pipe));

				} catch (UnreadableException e) {
					e.printStackTrace();
				}

			} else {
				block();
				System.out.println("ControllerAgent : CollectPipeDataBehavior : wating for msg ");
			}
		}
	}

	public class ProcessEntryProposalBehavior extends OneShotBehaviour {

		@Override
		public void action() {

			MessageTemplate msgTemplate = 
				MessageTemplate.MatchConversationId(ENTRY_PROPOSAL.name());

			ACLMessage msg = myAgent.receive(msgTemplate);

			if (msg != null) {

				float proposalGasAmount = Float.parseFloat(msg.getContent());

				System.out.println(String.format(
						"ControllerAgent %s : gas amount recieved: %s",
						myAgent.getLocalName(), proposalGasAmount));
				
				float acceptedIncomingGasAmount = 
					calculateAcceptedIncomingGasAmount(proposalGasAmount);

				if (acceptedIncomingGasAmount > 0 ) {

					sendAcceptProposalMessage(msg.getSender(), acceptedIncomingGasAmount, ENTRY_PROPOSAL.name());

					addIncomingGasAmount(acceptedIncomingGasAmount);

				} else {
					sendRejectProposalMessage(msg.getSender(), ConversationId.ENTRY_PROPOSAL.name());
				}
			}
		}
	}

	// TODO oturchyn 2012-11-25 : implement
	private float calculateAcceptedIncomingGasAmount(float proposalGasAmount) {
		
		//calculateIncomingFlow 1000m_cube_per_hour
		
		//checkCurrentFlow 1000m_cube_per_hour

		//checkCurrentPressure barg
		
		//calculatePressureWithIncomingGasAmount barg
		
		//if newPressure > maxPressure // not OK
		// calculateMaxPossibleIncomingGasAmount
		
		//calculatePressureDifference
		
		//if difference != 0
		// changeCompressorParameters ? pressure or flow
		// sendCompressorParameters
		
		// return maxPossibleIncomingGasAmount
		// nicht größer als proposalGasAmount		
		return proposalGasAmount;
	}

	private void addIncomingGasAmount(float incomingGasAmount) {
		
		currentGasAmount += incomingGasAmount;
		
		System.out.println(String.format(
				"ControllerAgent %s : current gas amount : %s",
				getLocalName(), currentGasAmount));
	}

	public class ProcessExitProposalBehavior extends OneShotBehaviour {

		@Override
		public void action() {

			MessageTemplate msgTemplate = 
				MessageTemplate.MatchConversationId(EXIT_PROPOSAL.name());

			ACLMessage msg = myAgent.receive(msgTemplate);

			if (msg != null) {

				float requestedGasAmount = Float.parseFloat(msg.getContent());

				System.out.println(String.format("ControllerAgent %s : gas amount requested: %s", myAgent.getLocalName(), requestedGasAmount));

				if (currentGasAmount >= requestedGasAmount) {

					// calculate accepted gas amount
					float acceptedGasAmount = requestedGasAmount;
					sendAcceptProposalMessage(msg.getSender(), acceptedGasAmount, EXIT_PROPOSAL.name());

					currentGasAmount -= requestedGasAmount;

					System.out.println(String.format("ControllerAgent %s : current gas amount : %s", myAgent.getLocalName(), currentGasAmount));

				} else {
					sendRejectProposalMessage(msg.getSender(), EXIT_PROPOSAL.name());
				}
			} 
		}
	}

	private void sendAcceptProposalMessage(AID receiver, float acceptedGasAmount, String conversationId) {

		ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		msg.addReceiver(receiver);
		msg.setConversationId(conversationId);
		msg.setContent(Float.toString(acceptedGasAmount));
		send(msg);

		System.out.println("ControllerAgent: Accept :" + acceptedGasAmount);
	}

	private void sendRejectProposalMessage(AID receiver, String conversationId) {

		ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
		msg.addReceiver(receiver);
		msg.setConversationId(conversationId);
		send(msg);

		System.out.println("ControllerAgent: reject proposal");
	}

}
