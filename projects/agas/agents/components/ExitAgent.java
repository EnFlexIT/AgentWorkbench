package agas.agents.components;

import static agas.agents.components.ConversationId.EXIT_PROPOSAL;
import static agas.agents.components.DFAgentDescriptionID.CONTROLLER;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;


import java.util.ArrayList;
import java.util.Iterator;


import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesValuePair;

public class ExitAgent extends GenericNetworkAgent {


	private AID controllerId;
	private java.util.List<Float> testData = new ArrayList<Float>();

	@Override
	protected void setup() {
		super.setup();

		System.out.println(String.format("ExitAgent %s started", getLocalName()));
		
		addBehaviour(new StartProposalBehaviour(this, 10000));
	}

	@Override
	protected void onEnvironmentStimulus() {
		super.onEnvironmentStimulus();


		
		findControllerAgent();
		loadTestData();

	}

	private void findControllerAgent() {
		//Null Überprüfung hinzufügen
			DFAgentDescription[] result = findAgentsByServiceType(CONTROLLER.name());
			if (result.length > 0) {
				controllerId = result[0].getName();
			}
		System.out.println(String.format(
				"Exit %s found Controller : %s",
				getAID().getLocalName() ,controllerId.getLocalName()));
	}

	private void loadTestData() {
		try {

			System.out.println("ExitAgent timeSeries");

			TimeSeriesChart timeSeriesChart = getTimeSeriesChart();

			List data = timeSeriesChart.getTimeSeriesChartData();

			TimeSeries timeSeries = (TimeSeries) data.get(0);

			List timeSeriesValuePairs = timeSeries.getTimeSeriesValuePairs();

			for (Iterator iterator = timeSeriesValuePairs.iterator(); iterator
					.hasNext();) {

				TimeSeriesValuePair timeSeriesValuePair = (TimeSeriesValuePair) iterator
						.next();

				testData.add(new Float(timeSeriesValuePair.getValue()
						.getFloatValue()));
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private TimeSeriesChart getTimeSeriesChart() {

		Object[] dataModel = (Object[]) myNetworkComponent.getDataModel();

		for (Object object : dataModel) {
			if (object instanceof TimeSeriesChart) {
				return (TimeSeriesChart) object;
			}
		}

		throw new IllegalArgumentException("TimeSeriesChart is not defined");
	}

	private boolean hasNextDrainGasAmount() {
		return !testData.isEmpty();
	}

	private float getNextDrainGasAmount() {

		if (testData.isEmpty()) {
			return 0;

		} else {
			Float incommingGasAmount = testData.get(0);
			testData.remove(0);

			return incommingGasAmount;
		}
	}


	private class StartProposalBehaviour extends TickerBehaviour {

		public StartProposalBehaviour(Agent agent, long period) {
			super(agent, period);
		}

		@Override
		protected void onTick() {
			if (controllerId != null) {
				addBehaviour(new RequestBehaviour());
				
//				if (hasNextIncommingGasAmount()) {
//					sendProposalMessage(getNextIncommingGasAmount());							
//				} 
			
			} else {
				stop();
			}
		}
	}
	
	public class RequestBehaviour extends Behaviour {
		
		private MessageTemplate msgTemplate = 
			MessageTemplate.MatchConversationId(EXIT_PROPOSAL.name());
				
		private int step = 1;
		float proposalGasAmount;
		
		public void action() {
			switch (step) {
				case 1:					
					if (hasNextDrainGasAmount()) {
						float requestedGasAmount = getNextDrainGasAmount();
						sendRequestMessage(requestedGasAmount);							

						step = 2;
					
					} else {
						step = 3;
					}
					
					break;
				
				case 2:
					ACLMessage reply = myAgent.receive(msgTemplate);
					
					if (reply != null) {
						
						if (reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
							float aceptedGasAmount = Float.parseFloat(reply.getContent());
							
							System.out.println(
									String.format("ExitAgent %s: proposal accepted : %s", myAgent.getLocalName(), aceptedGasAmount));
							
							// if  aceptedGasAmount < proposalGasAmount ?

						} else if (reply.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
							// do nothing
							System.out.println(
									String.format("ExitAgent %s: proposal rejected", myAgent.getLocalName()));
							
						} else {
							throw new IllegalArgumentException(
									String.format("Unsupported ACLMessage %s", reply.getPerformative()));
						}
						
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
	}


	private void sendRequestMessage(Float requestGasAmount) {

		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		msg.addReceiver(controllerId);
		msg.setContent(requestGasAmount.toString());
		msg.setConversationId(EXIT_PROPOSAL.name());
		send(msg);

		System.out.println(String.format("ExitAgent: Propose %s sent to %s.", requestGasAmount, controllerId.getLocalName()));
	}
	
}
