package mas.display;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PositionUpdateReceiver extends CyclicBehaviour {
	
	MessageTemplate positionTemplate;
		
	public PositionUpdateReceiver(MessageTemplate mt){
		
		this.positionTemplate = mt;
	}
	@Override
	public void action() {
		ACLMessage posUpdate = myAgent.receive(positionTemplate);
		if(posUpdate != null){
			DisplayAgent da = (DisplayAgent)myAgent;
			String name = posUpdate.getSender().getLocalName();
			if(posUpdate.getContent().equals("bye")){
				da.removeAgent(name);
			}
			else{
				String newPos[] = posUpdate.getContent().split(",");
				if(da.knownAgents.contains(name)){
					da.updateAgent(name, newPos[0], newPos[1]);
				}else{
					da.addAgent(name, newPos[0], newPos[1]);
				}
			}
			
			
			
//			System.out.println(myAgent.getLocalName()+": Received new position "+posUpdate.getContent()+" from "+posUpdate.getSender().getLocalName());
		}else{
			block();
		}

	}

}
