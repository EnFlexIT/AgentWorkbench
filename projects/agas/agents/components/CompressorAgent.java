package agas.agents.components;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponent;


public class CompressorAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = 2792727750579482552L;

	private float currentGasAmount;
	
	@Override
	protected void setup() {
		super.setup();
		System.out.println(String.format("CompressorAgent %s gestartet.", getAID().getName()));	
		this.registerDFService(DFAgentDescriptionID.COMPRESSOR.name(), getLocalName(), null);
		
	}

	@Override
	protected void takeDown() {
		super.takeDown();

		deregisterDFService();
	}
	protected void onEnvironmentStimulus() {
		super.onEnvironmentStimulus();
		
		//addBehaviour(new ReceiveProposalGasAmountBehavior());
	}

	private class ReceiveProposalGasAmountBehavior extends CyclicBehaviour {

		@Override
		public void action() {
			
		
		}
	}
	
}
