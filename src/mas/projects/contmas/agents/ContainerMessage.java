package mas.projects.contmas.agents;

import mas.projects.contmas.ontology.ContainerTerminalOntology;
import jade.content.lang.Codec;
import jade.content.lang.leap.LEAPCodec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.lang.acl.ACLMessage;

public class ContainerMessage extends ACLMessage {
	protected  Codec codec = new LEAPCodec();
	protected  Ontology ontology = ContainerTerminalOntology.getInstance();
	public ContainerMessage(Integer performative){
		super(performative);
		this.setLanguage(codec.getName());
		this.setOntology(ontology.getName());
	}
	/*
	public static ACLMessage tailorMessage(ACLMessage input){
		input.setLanguage(codec.getName());
		input.setOntology(ontology.getName());		
		return input;
	}
	*/
}
