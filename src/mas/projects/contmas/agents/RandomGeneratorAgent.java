package mas.projects.contmas.agents;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

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
		addBehaviour(new createRandomBayMap (this,mt));
		
		
		 mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new populateBayMap (this,mt));
		
	}
    
 	public class createRandomBayMap extends AchieveREResponder{
		public createRandomBayMap(Agent a, MessageTemplate mt) {
			super(a, mt);
		}
	    protected ACLMessage handleRequest(ACLMessage request) { 
	        ACLMessage reply = request.createReply(); 
			Concept content;
			try {
				content = ((AgentAction) getContentManager().extractContent(request));
		        if(content instanceof RequestRandomBayMap) {
			    	System.out.println("content instanceof RequestRandomBayMap");

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
			        return reply;
		        }
		        /*
		        else {
		            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD); 
		            reply.setContent("Fehler");
		        } 
		        return reply;
		        */
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				System.err.println("Exception RandomGeneratorAgent prepareResponse UngroundedException");
				e.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				System.err.println("Exception RandomGeneratorAgent prepareResponse CodecException");
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				System.err.println("Exception RandomGeneratorAgent prepareResponse OntologyException");
				e.printStackTrace();
			}
			return null;
	    } // end prepareResponse() 
	    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response){
			return null;
	    }
	}	

	public class populateBayMap extends AchieveREResponder{
		
		//TODO prepareResultNotification() method not re-defined
		//TODO Schema and Java class do not match
		
		public populateBayMap(Agent a, MessageTemplate mt) {
			super(a, mt);
		}
	    protected ACLMessage handleRequest(ACLMessage request) { 
	    	System.out.println("populateBayMap handleRequest start");
	        ACLMessage reply = request.createReply();
			AgentAction content;
			try {
				content = ((AgentAction) getContentManager().extractContent(request));

		        if(content instanceof RequestPopulatedBayMap) {

		        	BayMap LoadBay=((RequestPopulatedBayMap) content).getPopulate_on();

		            reply.setPerformative(ACLMessage.INFORM); 
		            
					Integer width, length, height;
					Random RandomGenerator=new Random();
					String containerName;
					Container c;
					BlockAddress ba;
					
		            //old
					width=LoadBay.getX_dimension();
					length=LoadBay.getY_dimension();
					height=LoadBay.getZ_dimension();
					//System.out.println("width:"+width+"length:"+length+"height:"+height);
					for(int z=0;z<height;z++){
						for(int y=0;y<length;y++){
							for(int x=0;x<width;x++){
								//System.out.println("runde");
								if(RandomGenerator.nextInt(2)==1 && (z==0 || 1==2)){ //TODO Abfrage, ob unterer Container schon vorhanden (keine Container "in die Luft" stellen)
									//System.out.println("zufällig");
									
									c=new Container();
									containerName="Container-ID: "+RandomGenerator.nextInt(65000);
									c.setId(containerName);
									ba=new BlockAddress();
									//ba.setAddresses_within(LoadBay);
									ba.setLocates(c);
									ba.setX_dimension(x);
									ba.setY_dimension(y);
									ba.setZ_dimension(z);
									//c.setOccupies(ba);
									//c.
		
									LoadBay.addIs_filled_with(ba);
									
								}
							}
						}
					}
		            //end old
		            
					ProvidePopulatedBayMap act=new ProvidePopulatedBayMap();
					act.setProvides(LoadBay);
					try{
						getContentManager().fillContent(reply, act);
					}catch (OntologyException e){
						System.err.println("Exception RandomGeneratorAgent prepareResponse OntologyException special");
						e.printStackTrace();
						return null;
					}

		        } else {
		            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD); 
		            reply.setContent("Fehler populated"); 
		        } 
		        return reply; 
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				System.err.println("Exception RandomGeneratorAgent prepareResponse UngroundedException");
				e.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				System.err.println("Exception RandomGeneratorAgent prepareResponse CodecException");
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				System.err.println("Exception RandomGeneratorAgent prepareResponse OntologyException");
				e.printStackTrace();
			}
			return null;
		}
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response){
			return response;
	    	
	    }
	}
	
}
