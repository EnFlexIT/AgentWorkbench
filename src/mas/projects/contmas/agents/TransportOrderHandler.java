package mas.projects.contmas.agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public interface TransportOrderHandler {
	public void handleTransportOrder();
}