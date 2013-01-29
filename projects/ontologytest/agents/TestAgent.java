package ontologytest.agents;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ontologytest.test.Chart;
import ontologytest.test.ChartSettingsGeneral;
import ontologytest.test.Protege34OntoOntology;
import ontologytest.test.impl.DefaultChart;
import ontologytest.test.impl.DefaultChartSettingsGeneral;

public class TestAgent extends Agent {

	private static final long serialVersionUID = -3354291505173818004L;

	private Codec codec = new XMLCodec();
	private Ontology ontology = Protege34OntoOntology.getInstance();
	
	
	@Override
	protected void setup() {
		super.setup();
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
		this.addBehaviour(new MessageReceiveBehaviour());
		System.out.println("Hi, I'm " + this.getLocalName());
		this.sendAMessage(new AID("Pong", AID.ISLOCALNAME));
		
	}
	
	private void sendAMessage(AID receiver) {
		
		ChartSettingsGeneral csg = new DefaultChartSettingsGeneral();
		csg.setChartTitle("TestTitle");
		csg.setRendererType("Renderer");
		
		Chart chart = new DefaultChart();
		chart.setVisualizationSettings(csg);
		
//		try {
//			ontology.add(new ConceptSchema(Protege34OntoOntology.CHART), DefaultChart.class);
//			ontology.add(new ConceptSchema(Protege34OntoOntology.CHARTSETTINGSGENERAL), DefaultChartSettingsGeneral.class);
//			
//		} catch (OntologyException e1) {
//			e1.printStackTrace();
//		}
		
		// --- Define a new action ------------------------
		Action act = new Action();
		act.setActor(getAID());
		act.setAction(chart);
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(getAID());
		msg.addReceiver(receiver);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		
		try {
			// --- ... send -------------------------------
			getContentManager().fillContent(msg, act);
			
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
		System.out.println(msg.toString());
	}
	
	
	/**
	 * The MessageReceiveBehaviour of this agent.
	 */
	private class MessageReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		@Override
		public void action() {
			
			Action act = null;
			Concept agentAction = null; 

			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
				
				System.out.println("Got Message !" + msg.getContent());
				
			} else {
				block();
			}			
		}
		
	}
		
	
	
}
