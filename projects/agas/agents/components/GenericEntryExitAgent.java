package agas.agents.components;

import static agas.agents.components.ServiceType.CONTROLLER;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesValuePair;

/**
 * An abstract generic class that possess attributes and behaviours for
 * {@link EntryAgent} and {@link ExitAgent}
 * 
 * @author Oleksandr Turchyn - University of Duisburg - Essen
 */

public abstract class GenericEntryExitAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = -3096451808731101519L;

	static Logger logger = Logger.getLogger(GenericEntryExitAgent.class.getName());

	protected AID controllerId;

	private java.util.List<Float> testData = new ArrayList<Float>();

	private float proposalGasVolume;

	@Override
	protected void setup() {
		super.setup();

		addBehaviour(new StartProposalBehaviour(this, 10000));
		addBehaviour(new StartRepeatProposalBehaviour(this, 1000));

		logger.log(Level.INFO, String.format("%s %s started.", getAgentType(), getLocalName()));
	}

	@Override
	protected void onEnvironmentStimulus() {
		super.onEnvironmentStimulus();

		findControllerAgent();
		loadTestData();
	}

	private void findControllerAgent() {
		DFAgentDescription[] result = findAgentsByServiceType(CONTROLLER.name());

		if (result.length > 0) {
			controllerId = result[0].getName();

			logger.log(Level.INFO, String.format("%s %s found ControllerAgent : %s", getAgentType(), getLocalName(), controllerId.getLocalName()));

		} else {
			logger.log(Level.SEVERE, "ControllerAgent not found");
		}
	}

	@SuppressWarnings("rawtypes")
	protected void loadTestData() {
		try {

			TimeSeriesChart timeSeriesChart = getTimeSeriesChart();

			List data = timeSeriesChart.getTimeSeriesChartData();

			TimeSeries timeSeries = (TimeSeries) data.get(0);

			List timeSeriesValuePairs = timeSeries.getTimeSeriesValuePairs();

			for (Iterator iterator = timeSeriesValuePairs.iterator(); iterator.hasNext();) {

				TimeSeriesValuePair timeSeriesValuePair =
						(TimeSeriesValuePair) iterator.next();

				testData.add(new Float(timeSeriesValuePair.getValue().getFloatValue()));
			}

		} catch (Exception e) {
			logger.log(Level.WARNING, String.format("%s %s : %s.", getAgentType(), getLocalName(), e.getMessage()));
		}
	}

	private TimeSeriesChart getTimeSeriesChart() {

		Object[] dataModel = (Object[]) myNetworkComponent.getDataModel();

		for (Object object : dataModel) {
			if (object instanceof TimeSeriesChart) {
				return (TimeSeriesChart) object;
			}
		}

		throw new IllegalArgumentException("TimeSeriesChart data not defined");
	}

	private boolean hasNextGasVolume() {
		return !testData.isEmpty();
	}

	private float getNextGasVolume() {

		if (testData.isEmpty()) {
			return 0;

		} else {
			Float incommingGasAmount = testData.get(0);
			testData.remove(0);

			return incommingGasAmount;
		}
	}

	private class StartProposalBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = -5374629751152487847L;

		public StartProposalBehaviour(Agent agent, long period) {
			super(agent, period);
		}

		@Override
		protected void onTick() {

			if (controllerId != null) {

				if (hasNextGasVolume()) {
					proposalGasVolume = getNextGasVolume();

					addBehaviour(new ProposalBehaviour());
				}

			} else {
				stop();
			}
		}
	}

	private class StartRepeatProposalBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = 2900164087731252386L;

		public StartRepeatProposalBehaviour(Agent agent, long period) {
			super(agent, period);
		}

		@Override
		protected void onTick() {

			if (controllerId != null) {

				if (proposalGasVolume > 0) {
					addBehaviour(new ProposalBehaviour());
				}

			} else {
				stop();
			}
		}
	}

	private class ProposalBehaviour extends Behaviour {

		private static final long serialVersionUID = -6565772117654196150L;

		private MessageTemplate msgTemplate =
				MessageTemplate.MatchConversationId(getConversationId().name());

		private int step = 1;

		public void action() {

			switch (step) {

			case 1:
				sendProposalMessage();

				step = 2;

				break;

			case 2:
				ACLMessage reply = myAgent.receive(msgTemplate);

				if (reply != null) {

					processReply(reply);

					step = 3;

				} else {
					block();
				}

				break;
			}
		}

		public boolean done() {
			return step == 3;
		}

		private void sendProposalMessage() {

			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			msg.addReceiver(controllerId);
			msg.setContent(Float.toString(proposalGasVolume));
			msg.setConversationId(getConversationId().name());

			send(msg);

			logger.log(Level.INFO,
					String.format("%s %s: propose %s sent to %s.", getAgentType(), getLocalName(), proposalGasVolume, controllerId.getLocalName()));
		}

		private void processReply(ACLMessage reply) {

			if (reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
				float aceptedGasVolume = Float.parseFloat(reply.getContent());

				proposalGasVolume -= aceptedGasVolume;

				logger.log(Level.INFO, String.format("%s %s: proposal accepted : %s", getAgentType(), myAgent.getLocalName(), aceptedGasVolume));

			} else if (reply.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
				// do nothing

				logger.log(Level.INFO, String.format("%s %s: proposal rejected", getAgentType(), myAgent.getLocalName()));

			} else {
				throw new IllegalArgumentException(String.format("Unsupported ACLMessage %s", reply.getPerformative()));
			}
		}

	}

	protected abstract ConversationId getConversationId();

	protected abstract String getAgentType();

}