package mas.projects.contmas.agents;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.leap.LEAPCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

import java.io.IOException;
import java.util.Random;

import mas.projects.contmas.ontology.*;

public class RandomGeneratorAgent extends ContainerAgent{
	public RandomGeneratorAgent() {
		super("random-generation");
	}
	protected void setup(){ 
        super.setup();
        //create filter for incoming messages
        MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST); 
        
        System.out.println("RandomGenerator gestartet (selbst)");
		addBehaviour(new createRandomBayMap (this,mt));
		//addBehaviour(new populateBayMap(this));
	}
    
 	public class createRandomBayMap extends AchieveREResponder{
		public createRandomBayMap(Agent a, MessageTemplate mt) {
			super(a, mt);
		}
	    protected ACLMessage prepareResponse(ACLMessage request) { 
	        ACLMessage reply = request.createReply(); 
			Concept content;
			try {
				content = ((AgentAction) getContentManager().extractContent(request));
		        if(content instanceof RequestRandomBayMap) {
		        	RequestRandomBayMap input=(RequestRandomBayMap) content;
		            reply.setPerformative(ACLMessage.INFORM); 
					Integer width, length, height;
					Random RandomGenerator=new Random(); 
					width=RandomGenerator.nextInt(input.getX_dimension());
					length=RandomGenerator.nextInt(input.getY_dimension());
					height=RandomGenerator.nextInt(input.getZ_dimension());
					BayMap LoadBay=new BayMap();
					LoadBay.setX_dimension(width);
					LoadBay.setY_dimension(length);
					LoadBay.setZ_dimension(height);
					ProvideBayMap act=new ProvideBayMap();
					act.setProvides(LoadBay);
					getContentManager().fillContent(reply, act);

		        } else {
		            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD); 
		            reply.setContent("Fehler"); 
		        } 
		        return reply; 
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

	    } // end prepareResponse() 
	    
	}	
	/*
	public class populateBayMap extends SimpleBehaviour{
		public populateBayMap(Agent a){
			super(a);
		}
		private boolean finished = false;
		public void action() {
			ACLMessage rcvMsg= receive();
			BayMap LoadBay=null;
			if (rcvMsg!=null && rcvMsg.getPerformative()==rcvMsg.REQUEST){ 
				try {
					LoadBay=(BayMap) rcvMsg.getContentObject();
					Integer width, length, height;
					Random RandomGenerator=new Random();
					String containerName;

					width=LoadBay.Width;
					length=LoadBay.Length;
					height=LoadBay.Height;
					for(int z=0;z<height;z++){
						for(int y=0;y<length;y++){
							for(int x=0;x<width;x++){
								if(RandomGenerator.nextInt(2)==1){
									containerName="Container-ID: #"+RandomGenerator.nextInt(65000);
								}else{
									containerName="LEER";	
								}
								LoadBay.getContainerAt(x, y, z).setContainerName(containerName);
							}
						}
					}
					ACLMessage sndMsg = rcvMsg.createReply();
					try {
						sndMsg.setContentObject(LoadBay);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AID dest = rcvMsg.getSender();

					sndMsg.addReceiver( dest );
					send(sndMsg);

					this.finished=true;
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				block();
			}
		}
		public boolean done() {  
			return finished;  
		}
	}
	*/
}
