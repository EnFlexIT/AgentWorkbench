package contmas.de.unidue.stud.sehawagn.contmas.control;

import jade.content.AgentAction;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Modify;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.SniffOn;
import jade.lang.acl.ACLMessage;

public class Helper {
	private static final SLCodec codec= new SLCodec();
	private static Ontology fipaOntology= FIPAManagementOntology.getInstance();
	private static Ontology jadeOntology= JADEManagementOntology.getInstance();


	public static ACLMessage prepareManagementMessage(Agent a,String type, AID object,AID subject){
		AgentAction agAct=null;
		Ontology ontology=null;
		
		if(type.equals(Constants.ENVIRONMENT_ACTION_HOLD)){
			ontology=fipaOntology;
			subject=a.getAMS();
			
			AMSAgentDescription desc = new AMSAgentDescription();
		    desc.setName(object);
		    desc.setState(AMSAgentDescription.SUSPENDED);
			
			Modify mod=new Modify();
			mod.setDescription(desc);
			agAct=mod;

		} else if(type.equals(Constants.ENVIRONMENT_ACTION_RESUME)){
			ontology=fipaOntology;

			subject=a.getAMS();
			
			AMSAgentDescription desc = new AMSAgentDescription();
		    desc.setName(object);
		    desc.setState(AMSAgentDescription.ACTIVE);
			
			Modify mod=new Modify();
			mod.setDescription(desc);
			agAct=mod;

		} else if(type.equals(Constants.ENVIRONMENT_ACTION_SNIFF)){
			ontology=jadeOntology;

			SniffOn sniffon=new SniffOn();
			sniffon.addSniffedAgents(object);
			sniffon.setSniffer(subject);
			agAct=sniffon;
		}
		
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(subject);
		
		Action act=new Action();
		act.setAction(agAct);
		act.setActor(subject);
		
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		
		a.getContentManager().registerLanguage(codec);
		a.getContentManager().registerOntology(ontology);
		
		try{
			a.getContentManager().fillContent(msg,act);
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
}
