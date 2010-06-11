package mas.agents;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import mas.service.AgentGUIService;
import mas.service.AgentGUIServiceHelper;
/** 
* <p>Title Hello JJADE</p> 
* <p>Description: A very simple agent which replies ot any message with an INFORM message with content "Hello World"</p> 
* <p>Copyright: Copyright (c) 2003</p> 
* <p>Company: Ryerson University, Toronto, Canada</p> 
* @author David Grimshaw 
* @version 1.0 
*/ 
public class HelloWorldAgent extends Agent { 

	private static final long serialVersionUID = 1L;
	
	protected void setup() { 
		System.out.println("Hallo, ich bin der erste Agent von Christian ...");
		System.out.println("Local - Name:" + getAID().getLocalName() );
		System.out.println("GUID - Name:" + getAID().getName() );
		
		AgentGUIServiceHelper agentGUIHelper = null;
		
		try {
			agentGUIHelper = (AgentGUIServiceHelper) getHelper(AgentGUIService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		System.out.println( agentGUIHelper.getWorldTimeLocalAsDate().toString() ); 
		
	} 

	class HelloBehaviour extends CyclicBehaviour { 

		private static final long serialVersionUID = 1L;

		public void action() { 
			ACLMessage received = blockingReceive(); 
			ACLMessage reply = received.createReply(); 
			reply.setContent("Hello World!"); 
			reply.setPerformative(ACLMessage.INFORM); 
		} 
	} 

} 
