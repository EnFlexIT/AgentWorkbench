package mas.display;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.script.Window;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.SubscriptionInitiator;

/**
 * Offering display service for other agents. Takes care of displaying, collision management etc.
 *   
 * @author nils
 *
 */
public class DisplayAgent extends Agent {
	
	// SVG Namespace
	public static final String svgNs=SVGDOMImplementation.SVG_NAMESPACE_URI;
	
	private DisplayAgentGUI myGUI = null;
	private DisplayAgent myAgent = this;
	
	public Map <String, AnimAgent> animAgents = null;  // Hashmap storing animated agents, key=localName
	
	public void setup(){
		
		myGUI = new DisplayAgentGUI(this);
		animAgents=new HashMap<String, AnimAgent>();
		
		// Add Behaviours
//		this.addBehaviour(new RegistrationServer());
//		this.addBehaviour(new MoveServer());
		
		
		System.out.println("Display agent "+getLocalName()+"ready.");
		
		// Create template
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("displayable");
		
		// Subscribe at the DF for information about new displayables
		Behaviour dfSubscription = new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this, getDefaultDF(), template, null)){
			public void handleInform(ACLMessage inform){
				try{
					DFAgentDescription[] dfds = DFService.decodeNotification(inform.getContent());
					if(dfds != null){
						processDfNotification(dfds);
					}
				}catch (FIPAException fe){
					fe.printStackTrace();
				}
			}
		};
		addBehaviour(dfSubscription);
		
		
	}
	
	public void takeDown(){
		if(myGUI!=null){
			myGUI.dispose();
		}
		// Unsubscribe		
	}
	
	private void processDfNotification(DFAgentDescription[] dfds){
		if(dfds != null){
			for(int i=0; i<dfds.length; i++){
				Iterator<ServiceDescription> sds = dfds[i].getAllServices();
				
				// Empty list if services -> deregistration
				if(!sds.hasNext()){
					System.out.println(myAgent.getLocalName()+": Deregistration information for "+dfds[i].getName().getLocalName()+" received.");
				}else{
					ServiceDescription sd = sds.next();
					if(sd.getType().equals("displayable")){
						System.out.println(myAgent.getLocalName()+": Received information about DisplayableAgent "+
									dfds[i].getName().getLocalName()+" of type "+sd.getName()+".");
					}
				}
//				ACLMessage subscriptionMsg = new ACLMessage(ACLMessage.SUBSCRIBE);
//				subscriptionMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
//				subscriptionMsg.addReceiver(newDisp.getName());
//				addBehaviour(new SubscriptionInitiator(this, subscriptionMsg));
			}
		}
	}	
}
