package mas.display;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class RegisterBehaviour extends OneShotBehaviour {

	@Override
	public void action() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(myAgent.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("displayable");
		sd.setName(((DisplayableAgent)myAgent).getAgentType());
		dfd.addServices(sd);
		try{
			DFService.register(myAgent, dfd);
			System.out.println(myAgent.getLocalName()+" successfully registered as displayable.");
		}catch(FIPAException fe){
			fe.printStackTrace();
		}

	}

}
